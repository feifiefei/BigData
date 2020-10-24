package cn.itcast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @description:todo: window的设置
 * @author: 飞
 * @date: 2020/10/23 0023 9:53
 */
public class WindowCarInDemo {
    public static void main(String[] args) throws Exception {
        /**
         *开发步骤：
         * 获取流处理执行环境
         * 加载socket数据源
         * 数据转换
         * 数据分组
         * 求和
         * 打印
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.socketTextStream("node1", 8090);
        SingleOutputStreamOperator<Car> map = source.map(new MapFunction<String, Car>() {
            @Override
            public Car map(String s) throws Exception {
                String[] split = s.split(",");
                return new Car(
                        Integer.valueOf(split[0]),
                        Integer.valueOf(split[1])
                );
            }
        });
        map.keyBy("id")
                //划分窗口（时间滚动窗口）
                //.timeWindow(Time.seconds(3))
                //时间滑动窗口
                //.timeWindow(Time.seconds(6), Time.seconds(3))
                //数量滚动窗口（根据kekBy出现三次，统计一次）
                //.countWindow(3)
                //数量滚动窗口,key键每出现三次统计最近六次的数据
                //.countWindow(6, 3)
                //数据中断发送时间超过10s中触发事件
                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(10)))
                //求和
                .sum("count")
                .print();
        env.execute();

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Car {
        private Integer id;
        private Integer count;
    }
}
