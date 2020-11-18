package cn.fei.kafka;

import cn.fei.avro.AvroSerializer;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @Date 2020/10/28
 */
//1.创建类，泛型参数继承avro基类
//T 表示传入对象是avro类型的对象
public class KafkaPro<T extends SpecificRecordBase> {

    //获取kafka生产者对象
    KafkaProducer kafkaProducer = new KafkaProducer(getProp());
    /**
     * 开发步骤：
     * 1.创建类，泛型参数继承avro基类
     * 2.设置生产者参数
     * 3.自定avro序列化
     * 4.添加发送数据方法
     */
    //2.设置生产者参数
    public Properties getProp(){
        /**
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
        properties.put("bootstrap.servers","node01:9092");
        properties.put("acks","0");
        properties.put("retries","0");
        properties.put("batch.size","16384");
        properties.put("linger.ms","1");
        properties.put("buffer.memory","33554432");
        properties.put("key.serializer", StringSerializer.class.getName());//表示Key键是字符串类型的序列化
        properties.put("value.serializer", AvroSerializer.class.getName());//Value是avro序列化数据，需要自定义avro序列化；

        return properties;
    }

    //4.添加发送数据方法
    public void sendData(String topic, T data){

        kafkaProducer.send(new ProducerRecord(topic,data));
    }


}
