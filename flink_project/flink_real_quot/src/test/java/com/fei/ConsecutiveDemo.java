package com.fei;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/4 0004 21:00
 */
public class ConsecutiveDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 需求：从数据源中依次提取"c","a","b"元素
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.fromElements("c", "d", "a", "a", "a", "d", "a", "b");
        //4.设置匹配模式，匹配"c","a","b"
        //  多次匹配"a"：组合模式
        Pattern<String, String> pattern = Pattern.<String>begin("begin")
                .where(new SimpleCondition<String>() {
                    @Override
                    public boolean filter(String value) throws Exception {
                        return value.equals("c"); //匹配"c"
                    }
                }).followedBy("middle")
                .where(new SimpleCondition<String>() {
                    @Override
                    public boolean filter(String value) throws Exception {
                        return value.equals("a"); //匹配a
                    }
                })
                .oneOrMore()
//                .consecutive() //连续多次匹配
                .allowCombinations() //允许组合
                .followedBy("end")
                .where(new SimpleCondition<String>() {
                    @Override
                    public boolean filter(String value) throws Exception {
                        return value.equals("b");  //匹配b
                    }
                });

        // 5.匹配数据提取Tuple3
        PatternStream<String> cep = CEP.pattern(source, pattern);
        cep.select(new PatternSelectFunction<String, Object>() {
            @Override
            public Object select(Map<String, List<String>> pattern) throws Exception {
                //分别获取模式下得数据
                List<String> begin = pattern.get("begin");
                List<String> middle = pattern.get("middle");
                List<String> end = pattern.get("end");

                return Tuple3.of(begin, middle, end);
            }
        }).print();

        env.execute();
    }
}