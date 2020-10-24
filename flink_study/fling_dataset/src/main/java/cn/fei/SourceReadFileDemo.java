package cn.itcast;


import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/20 0020 18:22
 */
public class SourceReadFileDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //设置递归读取目录
        Configuration configuration = new Configuration();
        configuration.setBoolean("recursive.file.enumeration", true);
        //读取指定目录下的所有文件
        env.readTextFile("G:\\bianchengxuexi\\flink_study\\data\\input\\dir")
                .withParameters(configuration)
                .print();

    }
}
