package cn.itcast;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/20 0020 20:45
 */
public class TransformationDemo3 {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<Integer> source = env.fromElements(1, 100);
        DataSource<Long> source1 = env.generateSequence(1, 100);
        source1.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long integer) throws Exception {
                return integer % 2 == 0;
            }
        })
                .print();
    }
}
