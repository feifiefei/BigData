package cn.itcast;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FilterOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/21 0021 17:29
 */
public class RebalanceDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        FilterOperator<Long> source = env.generateSequence(0, 100)
                .filter(new FilterFunction<Long>() {
                    @Override
                    public boolean filter(Long aLong) throws Exception {

                        return aLong > 50;

                    }
                });
        source
                .rebalance()
                .map(new RichMapFunction<Long, Tuple2<Integer, Long>>() {
                    int id = 0;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        id = getRuntimeContext().getIndexOfThisSubtask();
                    }

                    @Override
                    public Tuple2<Integer, Long> map(Long aLong) throws Exception {
                        return Tuple2.of(id, aLong);
                    }
                })
                .print();

    }
}
