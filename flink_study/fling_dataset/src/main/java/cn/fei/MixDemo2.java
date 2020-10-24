package cn.itcast;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;


/**
 * @description:
 * @author: 飞
 * @date: 2020/10/20 0020 21:24
 */
public class MixDemo2 {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> source = env.readTextFile("G:\\bianchengxuexi\\flink_study\\data\\input\\subject.csv");
        source.map(new MapFunction<String, Tuple2<Integer, String>>() {
            @Override
            public Tuple2<Integer, String> map(String s) throws Exception {
                String[] split = s.split(",");
                Integer aa = Integer.valueOf(split[0]);

                return Tuple2.of(aa, split[1]);
            }
        })
                //.maxBy(0)
                //.aggregate(Aggregations.MIN,0)
                //.groupBy(1)
                /*.reduce(new ReduceFunction<Tuple2<Integer, String>>() {
                    @Override
                    public Tuple2<Integer, String> reduce(Tuple2<Integer, String> t2, Tuple2<Integer, String> t1) throws Exception {
                        return Tuple2.of(t1.f0 + t2.f0, t1.f1);
                    }
                })*/
                .reduceGroup(new GroupReduceFunction<Tuple2<Integer, String>, Tuple2<Integer, String>>() {
                    @Override
                    public void reduce(Iterable<Tuple2<Integer, String>> iterable, Collector<Tuple2<Integer, String>> collector) throws Exception {
                        String value = "";
                        Integer key = 0;
                        for (Tuple2<Integer, String> tuple2 : iterable) {
                            key = tuple2.f0+key;
                            value = tuple2.f1 + value;
                        }
                       collector.collect(Tuple2.of(key,value));
                    }
                }).print();


    }
}
