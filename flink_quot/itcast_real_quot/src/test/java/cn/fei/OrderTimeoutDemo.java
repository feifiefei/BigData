package cn.fei;

import cn.fei.bean.OrderEvent;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Date 2020/11/6
 * 订单超时案例讲解
 */
public class OrderTimeoutDemo {

    public static void main(String[] args) throws Exception {
        /**
         * 需求：
         * 用户下单以后，应该设置订单失效时间，用来提高用户的支付意愿
         * 如果用户下单15分钟未支付，则输出监控信息，输出超时数据
         */

        /**
         * 1.获取流处理执行环境
         * 2.设置并行度,设置事件时间
         * 3.加载数据源,提取事件时间
         * 4.定义匹配模式followedBy，设置时间长度
         * 5.匹配模式（分组）
         * 6.设置侧输出流
         * 7.数据处理(获取begin数据)
         * 8.打印（正常/超时订单）
         * 9.触发执行
         */
        // 1.获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.设置并行度,设置事件时间
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //3.加载数据源,提取事件时间
        SingleOutputStreamOperator<OrderEvent> source = env.fromCollection(Arrays.asList(
                new OrderEvent(1, "create", 1558430842000L),//2019-05-21 17:27:22
                new OrderEvent(2, "create", 1558430843000L),//2019-05-21 17:27:23
                new OrderEvent(2, "other", 1558430845000L), //2019-05-21 17:27:25
                new OrderEvent(2, "pay", 1558430850000L),   //2019-05-21 17:27:30
                new OrderEvent(1, "pay", 1558431920000L)    //2019-05-21 17:45:20
        )).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<OrderEvent>(Time.seconds(0)) {
            @Override
            public long extractTimestamp(OrderEvent element) {
                return element.getEventTime();
            }
        });

        //4.定义匹配模式followedBy，设置时间长度
        //设置匹配规则
        Pattern<OrderEvent, OrderEvent> pattern = Pattern.<OrderEvent>begin("begin")
                .where(new SimpleCondition<OrderEvent>() {
                    @Override
                    public boolean filter(OrderEvent value) throws Exception {
                        return value.getStatus().equals("create");
                    }
                }).followedBy("end")
                .where(new SimpleCondition<OrderEvent>() {
                    @Override
                    public boolean filter(OrderEvent value) throws Exception {
                        return value.getStatus().equals("create");
                    }
                }).within(Time.minutes(15));

        //5.匹配模式（分组）
        //规则与数据流匹配
        PatternStream<OrderEvent> cep = CEP.pattern(source.keyBy(OrderEvent::getOrderId), pattern);

        //6.设置侧输出流
        OutputTag<OrderEvent> opt = new OutputTag<>("opt", TypeInformation.of(OrderEvent.class));

        //7.数据处理(获取begin数据)
        SingleOutputStreamOperator<Object> res = cep.select(opt, new PatternTimeoutFunction<OrderEvent, OrderEvent>() {
            //匹配超时数据，超时数据，会自动封装到侧边流里面
            @Override
            public OrderEvent timeout(Map<String, List<OrderEvent>> pattern, long timeoutTimestamp) throws Exception {

                //获取超时数据
                OrderEvent begin = pattern.get("begin").get(0);
                return begin;
            }
        }, new PatternSelectFunction<OrderEvent, Object>() { //匹配得是正常数据
            @Override
            public Object select(Map<String, List<OrderEvent>> pattern) throws Exception {
                OrderEvent begin = pattern.get("end").get(0);
                return begin;
            }
        });

        res.print("正常数据：");
        res.getSideOutput(opt).print("超时数据");

        env.execute();
    }

}
