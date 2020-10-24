package cn.itcast;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.configuration.Configuration;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/21 0021 20:06
 */
public class CountDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> source = env.fromElements("a", "b", "c", "d");
        MapOperator<String, Object> mapOperator = source.map(new RichMapFunction<String, Object>() {
            IntCounter intCounter = new IntCounter();

            //open方法是初始化方法，在map之前执行
            @Override
            public void open(Configuration parameters) throws Exception {
                getRuntimeContext().addAccumulator("cnt", intCounter);
            }

            @Override
            public Object map(String s) throws Exception {
                //累加器添加数据
                intCounter.add(1);
                return s;
            }
        });
        mapOperator.writeAsText("cnt");
        //这里不能打印,打印是一个触发算子，就无法获取累加器数据
        JobExecutionResult execute = env.execute();
        Object cnt = execute.getAccumulatorResult("cnt");
        System.out.println(cnt);
    }
}
