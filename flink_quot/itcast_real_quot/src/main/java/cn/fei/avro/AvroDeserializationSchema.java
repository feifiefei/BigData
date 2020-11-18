package cn.fei.avro;

import cn.fei.config.QuotConfig;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @Date 2020/10/31
 * 反序列化kafka中的avro序列化数据
 */
//1.创建泛型反序列化类实现反序列化接口
public class AvroDeserializationSchema<T> implements DeserializationSchema<T> {
    /**
     * 开发步骤：
     * 1.创建泛型反序列化类实现反序列化接口
     * 2.创建构造方法
     * 3.avro反序列化数据
     * 4.获取反序列化数据类型
     */

    // 2.创建构造方法
    private String topicName;

    public AvroDeserializationSchema(String topicName) {
        this.topicName = topicName;
    }

    //覆写父类反序列化方法
    @Override
    public T deserialize(byte[] message) throws IOException {

        //定义反序列化约束对象
        SpecificDatumReader<T> avroSchema = null;
        if (topicName.equals(QuotConfig.config.getProperty("sse.topic"))) {
            avroSchema = new SpecificDatumReader(SseAvro.class);
        } else {
            avroSchema = new SpecificDatumReader(SzseAvro.class);
        }

        //获取反序列化编码对象
        ByteArrayInputStream bis = new ByteArrayInputStream(message);
        //二进制对象
        BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(bis, null);

        T read = avroSchema.read(null, binaryDecoder);
        return read;
    }

    @Override
    public boolean isEndOfStream(T nextElement) {
        return false;
    }

    //返回数据类型
    @Override
    public TypeInformation<T> getProducedType() {
        TypeInformation<T> of = null;
        if (topicName.equals(QuotConfig.config.getProperty("sse.topic"))) {
            of = (TypeInformation<T>) TypeInformation.of(SseAvro.class);
        } else {
            of = (TypeInformation<T>) TypeInformation.of(SzseAvro.class);
        }
        return of;
    }


}
