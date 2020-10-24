package cn.itcast;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;


import java.util.Properties;

/**
 * @description:todo：Flink连接kafka
 * @author: 飞
 * @date: 2020/10/22 0022 20:13
 */
public class FlinkProducerToKafkaDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 开发步骤：
         * 	1.获取流处理执行环境
         * 	2.加载数据源，加载nc
         * 	3.配置kafka生产参数
         * 	4.获取kafka生产者对象
         * 	5.数据写入kafka
         * 	6.触发执行
         */
        //初始化环境，构建连接
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.socketTextStream("node1", 8090);
        //配置kafka的生产参数
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "node1:9092,node2:9092,node3:9092");
        //获取Kafka的生产者对象
        FlinkKafkaProducer producer = new FlinkKafkaProducer("test2", new SimpleStringSchema(), properties);
        //数据写入kafka
        source.addSink(producer);
        env.execute();

    }
}
