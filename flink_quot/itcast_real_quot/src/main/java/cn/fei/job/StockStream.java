package cn.fei.job;

import cn.fei.avro.AvroDeserializationSchema;
import cn.fei.avro.SseAvro;
import cn.fei.avro.SzseAvro;
import cn.fei.bean.CleanBean;
import cn.fei.config.QuotConfig;
import cn.fei.map.SseMap;
import cn.fei.map.SzseMap;
import cn.fei.task.*;
import cn.fei.util.QuotUtil;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Properties;

/**
 * @Date 2020/10/31
 * 个股业务开发：秒级/分时/K线/分时数据备份/个股涨跌幅行情
 */
//1.创建StockStream单例对象，创建main方法
public class StockStream {

    /**
     * 个股总体开发步骤：
     * 1.创建StockStream单例对象，创建main方法
     * 2.获取流处理执行环境
     * 3.设置事件时间、并行度
     * 4.设置检查点机制
     * 5.设置重启机制
     * 6.整合Kafka(新建反序列化类)
     * 7.数据过滤（时间和null字段）
     * 8.数据转换、合并
     * 9.过滤个股数据
     * 10.设置水位线
     * 11.业务数据处理
     * 12.触发执行
     */
    public static void main(String[] args) throws Exception {

        //2.获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //3.设置事件时间、并行度
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);//便于开发和测试

        //本地开发和测试的时候，可以关闭检查点，但是生产环境一定要打开检查点
        //4.设置检查点机制
//        env.enableCheckpointing(5000l);//开启检查点，设置检查点制作间隔时间5s
//        //设置检查点存储
//        env.setStateBackend(new FsStateBackend("hdfs://node01:8020/checkpoint/stock"));
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE); //强一致性
//        env.getCheckpointConfig().setFailOnCheckpointingErrors(false); //检查点制作失败，任务继续运行
//        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);//最大线程数
//        //任务取消的时候，保留检查点，需要手动删除老的检查点
//        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//
//        // 5.设置重启机制
//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.seconds(5)));


        //6.整合Kafka(新建反序列化类)
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", QuotConfig.config.getProperty("bootstrap.servers"));
        properties.setProperty("group.id", QuotConfig.config.getProperty("group.id"));

        //新建消费对象
        //沪市：sse
        FlinkKafkaConsumer011<SseAvro> sseKafkaConsumer = new FlinkKafkaConsumer011<SseAvro>(QuotConfig.config.getProperty("sse.topic"), new AvroDeserializationSchema(QuotConfig.config.getProperty("sse.topic")), properties);
        //深市：szse
        FlinkKafkaConsumer011<SzseAvro> szseKafkaConsumer = new FlinkKafkaConsumer011<SzseAvro>(QuotConfig.config.getProperty("szse.topic"), new AvroDeserializationSchema(QuotConfig.config.getProperty("szse.topic")), properties);

        sseKafkaConsumer.setStartFromEarliest();
        szseKafkaConsumer.setStartFromEarliest();

        //加载数据
        DataStreamSource<SseAvro> sseSource = env.addSource(sseKafkaConsumer);
        DataStreamSource<SzseAvro> szseSource = env.addSource(szseKafkaConsumer);
//        sseSource.print("沪市：");
//        szseSource.print("深市:");

        //7.数据过滤（时间和null(0)字段）
        //sse过滤
        SingleOutputStreamOperator<SseAvro> sseFilterData = sseSource.filter(new FilterFunction<SseAvro>() {
            @Override
            public boolean filter(SseAvro value) throws Exception {
                return QuotUtil.checkTime(value) && QuotUtil.checkData(value);
            }
        });

        //szse过滤
        SingleOutputStreamOperator<SzseAvro> szseFilterData = szseSource.filter(new FilterFunction<SzseAvro>() {
            @Override
            public boolean filter(SzseAvro value) throws Exception {
                return QuotUtil.checkTime(value) && QuotUtil.checkData(value);
            }
        });

        // 8.数据转换、合并
        //使用union需要保证类型一致
        DataStream<CleanBean> unionData = sseFilterData.map(new SseMap()).union(szseFilterData.map(new SzseMap()));

        //9.过滤个股数据
        SingleOutputStreamOperator<CleanBean> filterData = unionData.filter(new FilterFunction<CleanBean>() {
            @Override
            public boolean filter(CleanBean value) throws Exception {
                return QuotUtil.isStock(value);
            }
        });

        //10.设置水位线
        DataStream<CleanBean> waterData = filterData.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<CleanBean>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(CleanBean element) {
                return element.getEventTime();
            }
        });

        waterData.print("水位数据：");
        /**
         * 1.秒级行情(5s)(掌握)
         * 2.分时行情（60s）（掌握）
         * 3.分时行情备份（掌握）
         * 4.个股涨幅榜（60s）
         * 5.个股K线
         */
        //1.秒级行情(5s)
        new StockSecTask().process(waterData);
        //2.分时行情（60s）
        new StockMinTask().process(waterData);
        //3.分时行情备份
        new StockMinHdfsTask().process(waterData);
        //4.个股涨幅榜
        new StockIncrTask().process(waterData);

        //5.个股K线
        new StockKlineTask().process(waterData);

        //触发执行
        env.execute("stock stream");
    }
}
