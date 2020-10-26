package com.fei;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.util.Collector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
/**
 * @description:todo：模拟自动好评
 * @author: 飞
 * @date: 2020/10/26 0026 21:06
 */
public class AutoCommentDemo {

    public static void main(String[] args) throws Exception {

        /**
         * 目的：定时器得使用
         * 实现方式 :onTimer
         * 实现自动好评：我们是根据tuple2里的第一个元素，取模:%2，等于0就是自动好评
         *
         * 开发步骤：
         *  1.初始化流处理执行环境
         *  2.加载数据源
         *  3.分组
         *  4.process数据转换
         *  5.初始化MapState
         *  6.注册定时器
         *  7.判断是否评价过
         *  8.触发执行
         */
        //1.初始化流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.加载数据源
        DataStreamSource<Tuple2<String, Long>> source = env.addSource(new AutoSource());
        //3.分组
        source.keyBy(0)
                .process(new ProcessFunctionAuto());

        //8.触发执行
        env.execute();
    }

    private static class AutoSource extends RichSourceFunction<Tuple2<String,Long>> {
        @Override
        public void run(SourceContext<Tuple2<String, Long>> ctx) throws Exception {
            while (true){

                ctx.collect(Tuple2.of(UUID.randomUUID().toString(),System.currentTimeMillis()));
                TimeUnit.SECONDS.sleep(1);
            }
        }

        @Override
        public void cancel() {

        }
    }

    private static class ProcessFunctionAuto extends KeyedProcessFunction<Tuple,Tuple2<String, Long>,Object> {

        //5.初始化MapState
        //此对象是接收数据得
        MapState<String, Long> ms = null;
        @Override
        public void open(Configuration parameters) throws Exception {
            ms = getRuntimeContext().getMapState(new MapStateDescriptor<String, Long>("ms", String.class, Long.class));
        }

        @Override
        public void processElement(Tuple2<String, Long> value, Context ctx, Collector<Object> out) throws Exception {
            ms.put(value.f0,value.f1);
            //6.注册定时器
            ctx.timerService().registerProcessingTimeTimer(5000L); //5S定时触发
        }

        //使用定时器
        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<Object> out) throws Exception {
            //遍历mapState里的集合数据
            Iterable<Map.Entry<String, Long>> entries = ms.entries();
            for (Map.Entry<String, Long> entry : entries) {
                String key = entry.getKey();
                //7.判断是否评价过
                if(key.hashCode() %2 == 0){
                    System.out.println("已好评："+entry.getValue());
                }else{
                    System.out.println("未好评："+entry.getValue());
                }
            }
        }
    }
}
