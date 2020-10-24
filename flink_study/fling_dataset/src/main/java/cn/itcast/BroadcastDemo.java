package cn.itcast;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @description:todo: flink堆缓存之广播
 * @author: 飞
 * @date: 2020/10/21 0021 21:25
 */
public class BroadcastDemo {
    public static void main(String[] args) throws Exception {
        /**
         *将studentDS(学号,姓名)集合广播出去（广播到各个TaskManager）
         * 然后时候scoreDS（学号,学科,成绩）和广播数据（学号,姓名）进行关联,
         * 得到这样格式的数据：（姓名,学科,成绩）
         */
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<Tuple2<Integer, String>> studentSouce = env.fromCollection(Arrays.asList(Tuple2.of(1, "张三"), Tuple2.of(2, "李四"), Tuple2.of(3, "王五")));
        DataSource<Tuple3<Integer, String, Integer>> scoreSource = env.fromCollection(Arrays.asList(Tuple3.of(1, "语文", 50), Tuple3.of(2, "数学", 70), Tuple3.of(3, "英文", 86)));
        MapOperator<Tuple3<Integer, String, Integer>, Object> result = scoreSource.map(new RichMapFunction<Tuple3<Integer, String, Integer>, Object>() {

            List<Tuple2<Integer, String>> broadcast = null;

            @Override
            public void open(Configuration parameters) throws Exception {
                //获取广播变量
                broadcast = getRuntimeContext().getBroadcastVariable("broadcast");
            }

            @Override
            public Object map(Tuple3<Integer, String, Integer> value) throws Exception {
                for (Tuple2<Integer, String> line : broadcast) {
                    if (line.f0 == value.f0) {
                        return Tuple3.of(line.f1, value.f1, value.f2);
                    }
                }
                return null;
            }
        })
                .withBroadcastSet(studentSouce, "broadcast");
        result.print();

    }
}
