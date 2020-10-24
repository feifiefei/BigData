package cn.itcast;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.Properties;

/**
 * @description:todo:flink管理kafka的消费位置
 * @author: 飞
 * @date: 2020/10/22 0022 17:19
 */
public class StreamKafkaDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 开发步骤：
         * 1.获取流处理执行环境
         * 2.自定义数据源
         *  （1）配置kafka消费参数
         *  //核心配置 ：broker,消费组
         *  （2）flink整合kafka
         *  （3）设置kafka消费起始位置
         *  （4）加载kafka消费数据
         * 3.打印数据
         * 4.触发执行
         */
        //1.获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //自定义数据源
        //配置kafka消费参数
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "node1:9092,node2:9092,node3:9092");
        properties.setProperty("group.id", "test");//消费组
        // properties.setProperty("auto.offset.reset","latest"); //kafka自身管理得偏移量，从最近偏移量消费
        properties.setProperty("flink.partition-discovery.interval-millis", "5000");//分区数自动感知
        //flink整合kafka
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>("test", new SimpleStringSchema(), properties);
        //设置kafka消费起始位置
        // kafkaConsumer.setStartFromEarliest();//从头消费，与kafka的偏移量无关
       // kafkaConsumer.setStartFromLatest();//从最新数据消费，与kafka的偏移量无关
        //kafkaConsumer.setStartFromTimestamp();//从指定的时间戳开始消费
        kafkaConsumer.setStartFromGroupOffsets();//默认参数，从最近的偏移量开始消费数据
        //从kafka指定topic得指定分区和偏移量消费数据
//        HashMap<KafkaTopicPartition, Long> map = new HashMap<>();
//        map.put(new KafkaTopicPartition("test", 0), 1L);
//        map.put(new KafkaTopicPartition("test", 1), 1L);
//        map.put(new KafkaTopicPartition("test", 2), 1L);
//        kafkaConsumer.setStartFromSpecificOffsets(map);
        //加载kafka消费数据
        DataStreamSource<String> sou = env.addSource(kafkaConsumer);
        //打印数据
        sou.print();
        //触发执行
        env.execute();

    }

}
