package cn.itcast;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @description：todo：flink分布式缓存
 * @author: 飞
 * @date: 2020/10/21 0021 21:50
 */
public class DistributeCacheDemo {
    public static void main(String[] args) throws Exception {

        /**
         * 需求：
         * 将scoreDS(学号, 学科, 成绩)中
         * 的数据和分布式缓存中的数据(学号,姓名)关联,得到这样格式的数据: (学生姓名,学科,成绩)
         *
         */
        /**
         * 开发步骤：
         * 	1.获取执行环境
         * 	2.加载数据源
         * 	3.注册分布式缓存文件
         * 	4.数据转换
         * 	（1）获取分布式缓存文件
         * 	（2）FileUtils解析文件
         * 	（3）数据组合
         * 	5.数据打印执行
         */
        //1.获取执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //2.加载数据源
        //学生分数
        DataSource<Tuple3<Integer, String, Integer>> scoreSource = env.fromCollection(Arrays.asList(Tuple3.of(1, "语文", 50), Tuple3.of(2, "数学", 70), Tuple3.of(3, "英文", 86)));
        //3.注册分布式缓存文件
        env.registerCachedFile("G:\\bianchengxuexi\\flink_study\\data\\input\\distribute_cache_student", "cache");
        //数据转换
        scoreSource.map(new RichMapFunction<Tuple3<Integer, String, Integer>, Object>() {
            List<String> list = null;

            @Override
            public void open(Configuration parameters) throws Exception {
                //获取分布式缓存文件
                File file = getRuntimeContext().getDistributedCache().getFile("cache");
                //FileUtils解析文件
                list = FileUtils.readLines(file);
            }

            @Override
            public Object map(Tuple3<Integer, String, Integer> value) throws Exception {
                for (String s : list) {
                    String[] arr = s.split(",");
                    if (Integer.valueOf(arr[0]) == value.f0) {
                        return Tuple3.of(arr[1], value.f1, value.f2);
                    }
                }
                return null;
            }
        })
                .print();
    }
}
