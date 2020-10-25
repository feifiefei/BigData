package com.fei;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;

import java.util.Properties;

/**
 * @description:todo: kafka端到端一次性语义
 * @author: 飞
 * @date: 2020/10/25 0025 18:52
 */
public class ReadKafkaAndSinkKafkaDemo {
    public static void main(String[] args) throws Exception {

        /**
         * 需求：消费kafka数据再写入kafka ,实现端到端得一次性语义
         * 开发步骤：
         * 1.初始化环境
         * 2.设置检查点
         * 3.配置kafka消费参数
         * 4.获取kafka数据
         * 5.获取生产者对象，设置一次性语义
         * 6.生产数据
         * 7.触发执行
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置检查点
        env.enableCheckpointing(5000L);
        env.setStateBackend(new FsStateBackend("file:///checkpoint"));
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //配置kafka的消费参数
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "node1:9092,node2:9092,node3:9092");
        properties.setProperty("group.id", "test");
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>("test", new SimpleStringSchema(), properties);
        kafkaConsumer.setStartFromEarliest();//从头消费
        //获取kafka数据
        DataStreamSource<String> source = env.addSource(kafkaConsumer);
        source.print("========================");
        //获取生产者对象，设置一次性语义
        Properties properties2 = new Properties();
        properties2.setProperty("bootstrap.servers", "node1:9092,node2:9092,node3:9092");
        //设置事务超时时间
        properties2.setProperty("transaction.timeout.ms", 60000 * 15 + "");
        FlinkKafkaProducer<String> kafkaProducer = new FlinkKafkaProducer<>("test2", new KeyedSerializationSchemaWrapper<>(new SimpleStringSchema()), properties2, FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
        //生产数据
        source.addSink(kafkaProducer);
        env.execute();

    }
}
