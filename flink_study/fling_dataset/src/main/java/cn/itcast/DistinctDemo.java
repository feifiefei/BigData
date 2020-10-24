package cn.itcast;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/21 0021 16:16
 */
public class DistinctDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> source = env.readTextFile("G:\\bianchengxuexi\\flink_study\\data\\input\\dir\\words1.txt");
        source
                .distinct()
                .print();
    }
}
