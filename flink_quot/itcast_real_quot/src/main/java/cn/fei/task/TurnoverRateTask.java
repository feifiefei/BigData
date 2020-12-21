package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.bean.TurnoverRateBean;
import cn.fei.inter.ProcessDataInterface;
import cn.fei.mail.MailSend;
import cn.fei.util.DbUtil;
import cn.fei.util.RedisUtil;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Date 2020/11/6
 * 实时预警：换手率
 */
public class TurnoverRateTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {

        /**
         * 总体开发步骤：
         * 1.创建bean对象
         * 2.数据转换process
         *   (1)加载mysql流通股本数据
         *   (2)封装 bean对象数据
         * 3.加载redis换手率数据
         * 4.模式匹配
         * 5.查询数据
         * 6.发送告警邮件
         * 7.数据打印
         *
         */

        //获取TurnoverRateBean对象数据
        SingleOutputStreamOperator<TurnoverRateBean> processData = waterData.process(new ProcessFunction<CleanBean, TurnoverRateBean>() {

            //(1)加载mysql流通股本数据
            Map<String, Map<String, Object>> map = null;

            @Override
            public void open(Configuration parameters) throws Exception {

                String sql = "SELECT * FROM bdp_sector_stock";
                //key:secCode,value:每一个secCode对应表中得一条数据
                map = DbUtil.query("sec_code", sql);
            }

            @Override
            public void processElement(CleanBean value, Context ctx, Collector<TurnoverRateBean> out) throws Exception {
                String secCode = value.getSecCode();
                //从板块成分股表里，获取secCode对应得数据
                Map<String, Object> mapNegoCap = this.map.get(secCode);
                if (mapNegoCap != null) {
                    //获取流通股本
                    BigDecimal negoCap = new BigDecimal(mapNegoCap.get("nego_cap").toString());
                    //(2)封装bean对象数据
                    //secCode、secName、tradePrice、tradeVol、negoCap
                    out.collect(new TurnoverRateBean(
                            value.getSecCode(),
                            value.getSecName(),
                            value.getTradePrice(),
                            value.getTradeVolumn(),
                            negoCap
                    ));
                }

            }
        });

        //3.加载redis换手率数据
        JedisCluster jedisCluster = RedisUtil.getJedisCluster();
        String threshold = jedisCluster.hget("quot", "turnoverRate");//换手率阀值

        //4.模式匹配
        Pattern<TurnoverRateBean, TurnoverRateBean> pattern = Pattern.<TurnoverRateBean>begin("begin")
                .where(new SimpleCondition<TurnoverRateBean>() {
                    @Override
                    public boolean filter(TurnoverRateBean value) throws Exception {
                        //计算换手率
                        //（在中国：成交量/流通股本×100%）
                        BigDecimal turnoverRate = new BigDecimal(value.getTradeVol()).divide(value.getNegoCap(), 2, BigDecimal.ROUND_HALF_UP);
                        if (turnoverRate.compareTo(new BigDecimal(threshold)) == 1) {
                            return true; //告警数据
                        }
                        return false; //正常数据
                    }
                });

        //实时流，匹配模式规则
        PatternStream<TurnoverRateBean> cep = CEP.pattern(processData.keyBy(TurnoverRateBean::getSecCode), pattern);

        // 5.查询数据
        //查询匹配到得规则数据
        cep.select(new PatternSelectFunction<TurnoverRateBean, Object>() {
            @Override
            public Object select(Map<String, List<TurnoverRateBean>> pattern) throws Exception {

                List<TurnoverRateBean> begin = pattern.get("begin");
                if(begin != null && begin.size() >0){
                    //邮件告警
                    MailSend.send(begin.toString());
                }
                return begin;
            }
        }).print();

    }
}
