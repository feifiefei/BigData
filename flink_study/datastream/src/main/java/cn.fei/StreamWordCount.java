package cn.itcast;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;


/**
 * @description:
 * @author: 飞
 * @date: 2020/10/22 0022 10:00
 */
public class StreamWordCount {
    public static void main(String[] args) throws Exception {
        //获取流处理环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //接收数据
        DataStreamSource<String> node1 = env.socketTextStream("node1", 8090);
        node1.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                String[] s1 = s.split(" ");
                for (String s2 : s1) {
                    collector.collect(s2);
                }
            }
        }).map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return Tuple2.of(s, 1);
            }
        })
                .keyBy(0)
                .sum(1)
                .print();
        env.execute();
    }

}
