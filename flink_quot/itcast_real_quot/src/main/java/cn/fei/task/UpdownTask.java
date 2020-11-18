package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.bean.WarnBaseBean;
import cn.fei.inter.ProcessDataInterface;
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
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @Date 2020/11/4
 * 实时预警：涨跌幅业务开发
 */
public class UpdownTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {

        /**
         * 开发步骤：
         * 1.数据转换map
         * 2.封装bean对象
         * 3.加载redis涨跌幅数据
         * 4.模式匹配
         * 5.获取匹配模式流数据
         * 6.查询数据
         * 7.发送告警邮件
         */
        //涨跌幅：(期末收盘点位-期初前收盘点位)/期初前收盘点位*100%
        //1.数据转换map
        SingleOutputStreamOperator<WarnBaseBean> mapData = waterData.map(new MapFunction<CleanBean, WarnBaseBean>() {
            @Override
            public WarnBaseBean map(CleanBean value) throws Exception {
                //secCode、preClosePrice、highPrice、lowPrice、closePrice、eventTime
                //2.封装bean对象
                return new WarnBaseBean(
                        value.getSecCode(),
                        value.getPreClosePrice(),
                        new BigDecimal(0),
                        new BigDecimal(0),
                        value.getTradePrice(),
                        value.getEventTime()
                );
            }
        });

        //3.加载redis涨跌幅数据
        JedisCluster jedisCluster = RedisUtil.getJedisCluster();
        //取涨幅
        BigDecimal up2 = new BigDecimal(jedisCluster.hget("quot", "upDown2"));
        //取跌幅
        BigDecimal up1 = new BigDecimal(jedisCluster.hget("quot", "upDown1"));

        //4.模式匹配
        Pattern<WarnBaseBean, WarnBaseBean> pattern = Pattern.<WarnBaseBean>begin("begin")
                .where(new SimpleCondition<WarnBaseBean>() {
                    @Override
                    public boolean filter(WarnBaseBean value) throws Exception {
                        //涨跌幅：(期末收盘点位-期初前收盘点位)/期初前收盘点位*100%
                        BigDecimal upDownRate = (value.getClosePrice().subtract(value.getPreClosePrice())).divide(value.getPreClosePrice(), 2, RoundingMode.HALF_UP);
                        if (upDownRate.compareTo(up1) == 1 && upDownRate.compareTo(up2) == -1) {
                            return false; //如果计算的涨跌幅幅度范围，在涨跌幅预警阀值之间，这属于正常数据，我们不需要
                        }
                        return true;
                    }
                });
        //5.获取匹配模式流数据
        PatternStream<WarnBaseBean> cep = CEP.pattern(mapData.keyBy(WarnBaseBean::getSecCode), pattern);
        cep.select(new PatternSelectFunction<WarnBaseBean, Object>() {
            @Override
            public Object select(Map<String, List<WarnBaseBean>> pattern) throws Exception {

                //获取匹配到的阀值数据
                List<WarnBaseBean> begin = pattern.get("begin");
                //查询结果数据非空，发送告警邮件
                if(begin !=null && begin.size() >0){
                    MailSend.send("涨跌幅业务，此处数据异常，请监管部门高度关注："+begin.toString());
                }
                return begin;
            }
        }).print();
    }
}
