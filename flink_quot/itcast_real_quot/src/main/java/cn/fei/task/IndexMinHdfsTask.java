package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.config.QuotConfig;
import cn.fei.function.KeyFunction;
import cn.fei.function.MinIndexWindowFunction;
import cn.fei.inter.ProcessDataInterface;
import cn.fei.map.IndexPutHdfsMap;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.fs.StringWriter;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;

/**
 * @Date 2020/11/1
 * 指数分时备份至HDFS
 */
public class IndexMinHdfsTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {

        /**
         * 开发步骤：
         * 1.设置HDFS存储路径
         * 2.设置数据文件参数
         *   （大小、分区、格式、前缀、后缀）
         * 3.数据分组
         * 4.划分时间窗口
         * 5.数据处理
         * 6.转换并封装数据
         * 7.写入HDFS
         */
        //1.设置HDFS存储路径
        BucketingSink<String> bucketingSink = new BucketingSink<>(QuotConfig.config.getProperty("index.sec.hdfs.path"));
        //2.设置数据文件参数
        //  （大小、分区、格式、前缀、后缀）
        bucketingSink.setBatchSize(Long.valueOf(QuotConfig.config.getProperty("hdfs.batch")));
        bucketingSink.setBucketer(new DateTimeBucketer<>(QuotConfig.config.getProperty("hdfs.bucketer")));
//        bucketingSink.setBucketer(new DateTimeBucketer<>("yyyy/MM/dd"));
        bucketingSink.setWriter(new StringWriter<>());//字符串格式
        bucketingSink.setInProgressPrefix("index-");
        bucketingSink.setPendingPrefix("index2-");
        bucketingSink.setInProgressSuffix(".txt");
        bucketingSink.setPendingSuffix(".txt");

        //3.数据分组
        waterData.keyBy(new KeyFunction())
                //4.划分时间窗口
                .timeWindow(Time.seconds(1))
                // 5.数据处理
                .apply(new MinIndexWindowFunction())
                //6.转换并封装数据
                //把indexBean转换成字符串
                .map(new IndexPutHdfsMap())
                .addSink(bucketingSink).setParallelism(2);
    }
}
