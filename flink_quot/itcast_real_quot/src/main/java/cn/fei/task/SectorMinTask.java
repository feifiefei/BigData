package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.bean.SectorBean;
import cn.fei.config.QuotConfig;
import cn.fei.function.KeyFunction;
import cn.fei.function.MinStockWindowFunction;
import cn.fei.function.SectorWindowFunction;
import cn.fei.inter.ProcessDataInterface;
import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

import java.util.Properties;

/**
 * @Date 2020/11/3
 * 板块分时行情（窗口：60s）
 */
public class SectorMinTask implements ProcessDataInterface {


    @Override
    public void process(DataStream<CleanBean> waterData) {

        /**
         * 开发步骤：
         * 1.数据分组
         * 2.划分个股时间窗口
         * 3.个股分时数据处理
         * 4.划分板块时间窗口
         * 5.板块分时数据处理
         * 6.数据转换成字符串
         * 7.数据写入kafka
         */

        //设置kafka生产者对象
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", QuotConfig.config.getProperty("bootstrap.servers"));
        FlinkKafkaProducer011<String> kafkaProducer = new FlinkKafkaProducer011<>(QuotConfig.config.getProperty("sse.sector.topic"), new SimpleStringSchema(), properties);

        //1.数据分组
        waterData.keyBy(new KeyFunction())
                //2.划分个股时间窗口
                .timeWindow(Time.minutes(1))
                //3.个股分时数据处理
                .apply(new MinStockWindowFunction())
                //4.划分板块时间窗口
                .timeWindowAll(Time.minutes(1))
                //5.板块分时数据处理
                .apply(new SectorWindowFunction())
                //6.数据转换成字符串
                .map(new MapFunction<SectorBean, String>() {
                    @Override
                    public String map(SectorBean value) throws Exception {
                        return JSON.toJSONString(value);
                    }
                }).addSink(kafkaProducer) ;//7.数据写入kafka

    }
}
