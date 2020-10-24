package cn.itcast;


import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/20 0020 9:57
 */
public class SourceDemo {
    /*
     * 1.获取批处理执行环境
     * 2.加载数据
     * 3.数据打印
     * */
    public static void main(String[] args) throws Exception {
        //获取环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //加载数据
        DataSource<String> source = env.fromElements("tom", "cat", "dog");
        source.map(new MapFunction<String, String>() {
            @Override
            public String map(String s) throws Exception {
                return s;
            }
        }).print();
    }

}
