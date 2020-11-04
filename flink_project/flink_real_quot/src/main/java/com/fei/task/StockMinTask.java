package com.fei.task;

import com.alibaba.fastjson.JSON;
import com.fei.bean.CleanBean;
import com.fei.bean.StockBean;
import com.fei.config.QuotConfig;
import com.fei.function.KeyFunction;
import com.fei.function.MinStockWindowFunction;
import com.fei.inter.ProcessDataInterface;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.Properties;

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/2 0002 14:31
 */
public class StockMinTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {
        /**
         * 注意事项：
         * 1.数据存储在druid
         *   （1）插入druid数据源是json格式，需要把最终的数据转换成json字符串
         *    (2)开启摄取任务进程
         *   （3）需要提前创建好topic，再同步到druid
         *2.时间窗口是60s/1分钟
         *3.新建kafka生产者对象
         *4.侧边流，是对主数据流进行拆分，根据source进行拆分，分表存储
         *5.计算分时成交数据（量/金额），需要使用mapState
         */
        /**
         * 开发步骤：
         * 1.定义侧边流
         * 2.数据分组
         * 3.划分时间窗口
         * 4.分时数据处理（新建分时窗口函数）
         * 5.数据分流
         * 6.数据分流转换
         * 7.分表存储(写入kafka)
         */
        //定义侧边流
        OutputTag<StockBean> szseOpt = new OutputTag<>("szseOpt", TypeInformation.of(StockBean.class));
        //数据分组
        SingleOutputStreamOperator<StockBean> processData = waterData.keyBy(new KeyFunction())
                //划分时间窗口
                .timeWindow(Time.seconds(60))
                //分时数据处理
                .apply(new MinStockWindowFunction())
                //数据分流
                .process(new ProcessFunction<StockBean, StockBean>() {
                    @Override
                    public void processElement(StockBean value, Context ctx, Collector<StockBean> out) throws Exception {
                        if (value.getSource().equals(QuotConfig.config.getProperty("sse.topic"))) {
                            out.collect(value);
                        } else {
                            ctx.output(szseOpt, value);
                        }
                    }
                });
        //数据分流转换
        //沪市分时行情
        SingleOutputStreamOperator<String> sseStr = processData.map(new MapFunction<StockBean, String>() {
            @Override
            public String map(StockBean value) throws Exception {
                //插入druid数据源是json格式，需要把最终的数据转化为json字符串
                return JSON.toJSONString(value);
            }
        });
        //深市分时行情
        SingleOutputStreamOperator<String> szseStr = processData.getSideOutput(szseOpt).map(new MapFunction<StockBean, String>() {
            @Override
            public String map(StockBean value) throws Exception {
                return JSON.toJSONString(value);
            }
        });
        //分表存储写入kafka
        //新建kafka生产者对象
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", QuotConfig.config.getProperty("bootstrap.servers"));
        //sse
        FlinkKafkaProducer011<String> sseKafkaPro = new FlinkKafkaProducer011<>(QuotConfig.config.getProperty("sse.stock.topic"), new SimpleStringSchema(), properties);
        //szse
        FlinkKafkaProducer011<String> szseKafkaPro = new FlinkKafkaProducer011<>(QuotConfig.config.getProperty("szse.stock.topic"), new SimpleStringSchema(), properties);
        //写入kafka
        sseStr.addSink(sseKafkaPro);
        szseStr.addSink(szseKafkaPro);
    }
}
