package cn.itcast;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.UnionOperator;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/21 0021 16:13
 */
public class UnionDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> source = env.readTextFile("G:\\bianchengxuexi\\flink_study\\data\\input\\dir\\words1.txt");
        DataSource<String> source1 = env.readTextFile("G:\\bianchengxuexi\\flink_study\\data\\input\\dir\\words2.txt");
        UnionOperator<String> union = source.union(source1);
        System.out.println(union.count());
    }
}
