package cn.itcast;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;


/**
 * @description:
 * @author: 飞
 * @date: 2020/10/20 0020 19:53
 */
public class TrasformationDemoTwo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<String> source = env.readTextFile("F:\\桌面\\杂类\\大数据阶段四\\flink-day02\\资料\\测试数据\\data\\input\\click.log");
        source.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                TrasformationDemo.Click click = JSON.parseObject(s, TrasformationDemo.Click.class);
                String entryTime = click.getEntryTime();
                String s1 = DateFormatUtils.format(Long.parseLong(entryTime), "yyyy--MM--dd--HH");
                String s2 = DateFormatUtils.format(Long.parseLong(entryTime), "yyyy--MM");
                String s3 = DateFormatUtils.format(Long.parseLong(entryTime), "yyyy");
                collector.collect(Tuple2.of(s1, 1));
                collector.collect(Tuple2.of(s2, 1));
                collector.collect(Tuple2.of(s3, 1));

            }
        }).groupBy(0)
                .sum(1)
                .print();

    }
}
