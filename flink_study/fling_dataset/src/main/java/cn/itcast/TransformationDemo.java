package cn.itcast;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.CsvReader;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.awt.*;
import java.util.Arrays;


/**
 * @description:
 * @author: 飞
 * @date: 2020/10/20 0020 10:48
 */
public class TransformationDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> source = env.readTextFile("file:///G:\\bianchengxuexi\\flink_study\\data\\input\\dir\\words1.txt");
        DataSource<String> source1 = env.fromElements("1", "2");
        DataSource<String> source2 = env.fromCollection(Arrays.asList("白展堂", "李大嘴", "吕秀才"));
        CsvReader csvFile = env.readCsvFile("file:///G:\\bianchengxuexi\\flink_study\\data\\input\\score.csv");

        //数据转换
        //source2.print();
        csvFile.lineDelimiter("\n").fieldDelimiter(",").pojoType(Score.class, "id", "name", "score").first(3).print();


    }



    @Data
    public static class Score {
        private Integer id;
        private String name;
        private Double score;
    }
}
