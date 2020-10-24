package cn.itcast;

import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/22 0022 18:03
 */
public class SplitSelectDdemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Integer> source = env.fromElements(1, 2, 3, 4, 5, 6);
        SplitStream<Integer> split = source.split(new OutputSelector<Integer>() {
            @Override
            public Iterable<String> select(Integer value) {
                ArrayList<String> list = new ArrayList<>();
                if (value % 2 == 0) {
                    list.add("偶数");
                } else {
                    list.add("奇数");
                }
                return list;
            }
        });

        //数据查询
        split.select("偶数").print();
        env.execute();
    }
}
