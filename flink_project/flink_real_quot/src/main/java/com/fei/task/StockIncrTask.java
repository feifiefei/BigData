package com.fei.task;

import com.alibaba.fastjson.JSON;
import com.fei.bean.CleanBean;
import com.fei.bean.StockIncrBean;
import com.fei.config.QuotConfig;
import com.fei.function.KeyFunction;
import com.fei.function.StockIncreWindowFunction;
import com.fei.inter.ProcessDataInterface;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

import java.util.Properties;

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/2 0002 17:40
 */
public class StockIncrTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {

        /**
         * 开发步骤：
         * 1.数据分组
         * 2.划分时间窗口
         * 3.创建bean对象
         * 4.分时数据处理（新建分时窗口函数）
         * 5.数据转换成字符串
         * 6.数据存储(单表)
         * 注意：1.数据也是插入到druid
         *      2.要新建topic
         *      3.开启摄取数据的进程
         *      4.单表存储沪深两市数据
         *      5.基于分钟级数据，进行业务开发
         */
        //创建kafka生产者对象
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", QuotConfig.config.getProperty("bootstrap.servers"));
        FlinkKafkaProducer011<String> kafkaProder = new FlinkKafkaProducer011<>(QuotConfig.config.getProperty("stock.increase.topic"), new SimpleStringSchema(), properties);
        //数据分组
        waterData.keyBy(new KeyFunction())
                .timeWindow(Time.seconds(60))
                .apply(new StockIncreWindowFunction())
                .map(new MapFunction<StockIncrBean, String>() {
                    @Override
                    public String map(StockIncrBean value) throws Exception {
                        return JSON.toJSONString(value);
                    }
                })
                .addSink(kafkaProder);

    }
}
