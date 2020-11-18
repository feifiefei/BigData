package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.bean.IndexBean;
import cn.fei.config.QuotConfig;
import cn.fei.function.KeyFunction;
import cn.fei.function.MinIndexWindowFunction;
import cn.fei.inter.ProcessDataInterface;
import com.alibaba.fastjson.JSON;
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
 * @Date 2020/11/1
 * 指数分时行情
 */
public class IndexMinTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {

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
        //1.定义侧边流
        //分流的是深市指数分时行情数据
        OutputTag<IndexBean> indexSzseOpt = new OutputTag<>("indexSzseOpt", TypeInformation.of(IndexBean.class));
        //2.数据分组
        SingleOutputStreamOperator<IndexBean> processData = waterData.keyBy(new KeyFunction())
                // 3.划分时间窗口
                .timeWindow(Time.minutes(1))
                //4.分时数据处理（新建分时窗口函数）
                .apply(new MinIndexWindowFunction())
                //5.数据分流
                .process(new ProcessFunction<IndexBean, IndexBean>() {
                    @Override
                    public void processElement(IndexBean value, Context ctx, Collector<IndexBean> out) throws Exception {
                        if (value.getSource().equals("sse")) {
                            out.collect(value);
                        } else {
                            ctx.output(indexSzseOpt, value);
                        }
                    }
                });

        //6.数据分流转换
        //sse:沪市指数
        SingleOutputStreamOperator<String> sseStr = processData.map(new MapFunction<IndexBean, String>() {
            @Override
            public String map(IndexBean value) throws Exception {
                return JSON.toJSONString(value);
            }
        });

        //szse：深市指数
        SingleOutputStreamOperator<String> szseStr = processData.getSideOutput(indexSzseOpt)//获取深市指数数据
                .map(new MapFunction<IndexBean, String>() {
                    @Override
                    public String map(IndexBean value) throws Exception {
                        return JSON.toJSONString(value);
                    }
                });

        //7.分表存储(写入kafka)
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", QuotConfig.config.getProperty("bootstrap.servers"));
        //sse
        FlinkKafkaProducer011<String> sseKafkaPro = new FlinkKafkaProducer011<>(QuotConfig.config.getProperty("sse.index.topic"), new SimpleStringSchema(), properties);

        //szse
        FlinkKafkaProducer011<String> szseKafkaPro = new FlinkKafkaProducer011<>(QuotConfig.config.getProperty("szse.index.topic"), new SimpleStringSchema(), properties);

        //写入kafka
        sseStr.addSink(sseKafkaPro);
        szseStr.addSink(szseKafkaPro);
    }
}
