package com.fei.kafka;

import com.fei.avro.AvroSerializer;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static java.lang.System.getProperties;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/28 0028 17:13
 */
//T表示传入对象是avro类型的对象
public class KafkaPro<T extends SpecificRecordBase> {
    //获取kafka生产者对象
    KafkaProducer producer = new KafkaProducer<>(getProp());


    public Properties getProp() {
        /**
         * 生产者参数：
         * bootstrap.servers ：broker地址
         * acks ：0,1和-1
         * retries：重试次数
         * batch.size：批量发送大小默认16384 （16kB）
         * linger.ms： 定时发送1ms
         * buffer.memory: 缓存大小33554432
         * key.serializer：key序列化
         * value.serializer： value序列化
         */
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "node01:9092");
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "0");
        properties.setProperty("batch.size", "16384");
        properties.setProperty("linger.ms", "1");
        properties.setProperty("buffer.memory", "33554432");
        properties.setProperty("key.serializer", StringSerializer.class.getName());//表示key键值是字符串类型的序列化
        properties.setProperty("value.serializer", AvroSerializer.class.getName());//value是avro序列化数据，需要自定义avro序列化
        return properties;
    }

    //添加发送数据方法
    public void sendData(String topic, T data) {
        producer.send(new ProducerRecord(topic, data));
    }

}
