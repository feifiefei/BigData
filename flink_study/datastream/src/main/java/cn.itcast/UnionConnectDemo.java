package cn.itcast;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * @Date 2020/10/22
 */
public class UnionConnectDemo {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> s1 = env.fromElements("a", "b", "c");
        DataStreamSource<String> s2 = env.fromElements("a1", "b1", "c1");
        DataStreamSource<Integer> s3 = env.fromElements(1, 2, 3);

        //union
        //s1.union(s2).print(); //多条数据流类型，必须一致，

        //connect :两条不同数据流组合在一起，类型可以不一致，但是可以有共同得类型输出结果
        s1.connect(s3).map(new CoMapFunction<String, Integer, String>() {
            @Override
            public String map1(String value) throws Exception {
                return value;
            }

            @Override
            public String map2(Integer value) throws Exception {
                return value+"=====";
            }
        }).print();


        env.execute();
    }

}