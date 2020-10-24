package cn.itcast;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @description:todo: flink简单的api
 * @author: 飞
 * @date: 2020/10/19 0019 21:11
 */
public class WorldCount {
    public static void main(String[] args) throws Exception {
        /**
         *单词统计
         * 开发步骤：
         * 1.初始化环境
         * 2.加载数据源
         * 3.数据转化
         * 4.数据打印
         * 5、触发执行
         */
        //初始化环境变量
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //加载数据源
        DataSource<String> source = env.fromElements("a b c d a d c c a");
        //数据转换
        source.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                String[] arr = value.split(" ");
                for (String str : arr) {
                    out.collect(str);
                }

            }
        }).map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return Tuple2.of(s, 1);
            }
        }).
                groupBy(0)
                .sum(1).
                print();


    }
}
