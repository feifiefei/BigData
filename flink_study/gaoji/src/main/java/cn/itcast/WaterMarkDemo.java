package cn.itcast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import javax.annotation.Nullable;

/**
 * @description:todo： 水位线设置
 * @author: 飞
 * @date: 2020/10/23 0023 11:36
 */
public class WaterMarkDemo {
    /**
     * 开发步骤：
     * 1.初始化执行环境
     * 2.设置事件时间、并行度
     * 3.加载数据源
     * 4.数据转换：新建bean对象
     * 5.设置水位线
     * 6.数据分组
     * 7.划分时间窗口
     * 8.数据聚合
     * 9.打印数据
     * 10.触发执行
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置事件时间、并行度
        env.setParallelism(1);//设置成单线程
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<String> source = env.socketTextStream("node1", 8091);
        SingleOutputStreamOperator<Boss> mapData = source.map(new MapFunction<String, Boss>() {
            @Override
            public Boss map(String s) throws Exception {
                String[] split = s.split(",");
                return new Boss(
                        Long.valueOf(split[0]),
                        split[1],
                        split[2],
                        Double.valueOf(split[3])
                );
            }
        });
//        mapData.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Boss>() {
//            //设置延迟数据
//            Long delayTime = 2000L;
//            //设置一个当前水位线时间
//            Long currentTimeStamp = 0L;
//
//            //获取水位线数据，后执行
//            @Nullable
//            @Override
//            public Watermark getCurrentWatermark() {
//                return new Watermark(currentTimeStamp - delayTime);//延迟时间，触发延迟计算会延迟两秒钟
//            }
//
//            //抽取事件时间，先执行
//            @Override
//            public long extractTimestamp(Boss boss, long l) {
//                Long eventTime = boss.getEventTime();//数据源本身时间
//                //好处：保证整个时间轴一直往前走，不会倒退
//                currentTimeStamp = Math.max(eventTime, currentTimeStamp);
//                return eventTime;
//            }
//        })
        //新建侧边流收集延迟数据
        OutputTag<Boss> opt = new OutputTag<>("opt", TypeInformation.of(Boss.class));
        //分组
        SingleOutputStreamOperator<Boss> result = mapData.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Boss>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(Boss element) {
                return element.getEventTime();
            }
        })
                .keyBy("company")
                .timeWindow(Time.seconds(3))//依赖于事件时间
                //在原来延迟时间的基础上再延迟2s。
                .allowedLateness(Time.seconds(2))
                //侧输出流
                .sideOutputLateData(opt)
                //聚合
                .max("price");
        result.print("正常数据：");
        result.getSideOutput(opt).print("延迟数据：");


        env.execute();

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Boss {
        private Long eventTime;
        private String company;
        private String product;
        private Double price;
    }
}
