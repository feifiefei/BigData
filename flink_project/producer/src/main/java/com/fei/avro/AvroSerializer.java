package com.fei.avro;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @description:todo: 泛型参数继承avro基类，实现序列化接口
 * @author: 飞
 * @date: 2020/10/28 0028 17:32
 */
public class AvroSerializer<T extends SpecificRecordBase> implements Serializer< T > {
    /**
     * 开发步骤：
     *  1.泛型参数继承avro基类，实现序列化接口
     *  2.重写序列化方法
     *  3.新建字节数组输出流对象
     *  4.获取二进制对象BinaryEncoder
     *  5.输出数据
     */
    @Override
    public void configure(Map map, boolean b) {

    }
    //重写此方法，实现自定义序列化
    @Override
    public byte[] serialize(String topic, T data) {
        //设置schema约束对象
        SpecificDatumWriter<Object> datumWriter = new SpecificDatumWriter<>(data.getSchema());
        //新建字节数组输出流对象
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //获取二进制编码对象，通过工厂对象获取
        BinaryEncoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(bos, null);
        //通过write方法，会将avro类型的数据，编码成字节流，数据会存储在bos字节流输出对象
        try {
            datumWriter.write(data,binaryEncoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    public void close() {

    }
}
