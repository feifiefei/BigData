package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.bean.WarnAmplitudeBean;
import cn.fei.bean.WarnBaseBean;
import cn.fei.inter.ProcessDataCepInterface;
import cn.fei.mail.MailSend;
import cn.fei.util.RedisUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @Date 2020/11/4
 * 实时预警：振幅业务开发
 */
public class AmplitudeTask implements ProcessDataCepInterface {
    @Override
    public void process(DataStream<CleanBean> waterData, StreamExecutionEnvironment env) {

        /**
         * 总体开发步骤：
         * 1.数据转换
         * 2.初始化表执行环境
         * 3.注册表（流）
         * 4.sql执行
         * 5.表转流
         * 6.模式匹配,比较阀值
         * 7.查询数据
         * 8.发送告警邮件
         */
        //1.数据转换
        SingleOutputStreamOperator<WarnBaseBean> mapData = waterData.map(new MapFunction<CleanBean, WarnBaseBean>() {
            @Override
            public WarnBaseBean map(CleanBean value) throws Exception {
                // secCode、preClosePrice、highPrice、lowPrice、closePrice、eventTime
                return new WarnBaseBean(
                        value.getSecCode(),
                        value.getPreClosePrice(),
                        value.getMaxPrice(),
                        value.getMinPrice(),
                        value.getTradePrice(),
                        value.getEventTime()
                );
            }
        });

        // 2.初始化表执行环境
        StreamTableEnvironment tblEnv = TableEnvironment.getTableEnvironment(env);
        //3.注册表（流）
        //eventTime.rowtime :表示事件时间
        //tbl：表示注册得表名
        tblEnv.registerDataStream("tbl", mapData, "secCode,preClosePrice,highPrice,lowPrice,eventTime.rowtime");

        //4.sql执行

        String sql = "select secCode,preClosePrice,max(highPrice) as highPrice ," +
                " min(lowPrice) as lowPrice  from tbl group by secCode,preClosePrice,tumble(eventTime,interval '2' second)";
        //查询
        Table table = tblEnv.sqlQuery(sql);
        //5.表转流
        DataStream<WarnAmplitudeBean> rowDataStream = tblEnv.toAppendStream(table, WarnAmplitudeBean.class);

        //振幅： (max(最高点位)-min(最低点位))/期初前收盘点位*100%
        //6.模式匹配,比较阀值

        //取出redis振幅预警阀值
        JedisCluster jedisCluster = RedisUtil.getJedisCluster();
        String threshold = jedisCluster.hget("quot", "amplitude");

        Pattern<WarnAmplitudeBean, WarnAmplitudeBean> pattern = Pattern.<WarnAmplitudeBean>begin("begin")
                .where(new SimpleCondition<WarnAmplitudeBean>() {
                    @Override
                    public boolean filter(WarnAmplitudeBean value) throws Exception {
                        //计算振幅
                        BigDecimal amplitudeRate = (value.getHighPrice().subtract(value.getLowPrice())).divide(value.getPreClosePrice(), 2, RoundingMode.HALF_UP);
                        if (amplitudeRate.compareTo(new BigDecimal(threshold)) == 1) {
                            return true; //返回true，表示计算结果大于预警阀值
                        }
                        return false;
                    }
                });

        //7.查询数据
        //将模式匹配规则作用在实时流上
        PatternStream<WarnAmplitudeBean> cep = CEP.pattern(rowDataStream.keyBy(WarnAmplitudeBean::getSecCode), pattern);
        //查询匹配到的规则数据
        SingleOutputStreamOperator<Object> warnData = cep.select(new PatternSelectFunction<WarnAmplitudeBean, Object>() {
            @Override
            public Object select(Map<String, List<WarnAmplitudeBean>> pattern) throws Exception {

                List<WarnAmplitudeBean> begin = pattern.get("begin");
                //获取告警数据，进行预警
                if (begin != null && begin.size() > 0) {
                    //发送告警邮件：
                    MailSend.send(begin.toString());
                }
                return begin;
            }
        });

        warnData.print("告警数据打印：");
    }
}
