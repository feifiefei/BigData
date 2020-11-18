package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.config.QuotConfig;
import cn.fei.function.KeyFunction;
import cn.fei.function.MinStockWindowFunction;
import cn.fei.function.SectorWindowFunction;
import cn.fei.inter.ProcessDataInterface;
import cn.fei.map.SectorPutHdfsMap;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.fs.StringWriter;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;

/**
 * @Date 2020/11/3
 */
public class SectorMinHdfsTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {
        /**
         * 开发步骤：
         * 1.设置HDFS存储路径
         * 2.设置数据文件参数
         *  （大小、分区、格式、前缀、后缀）
         * 3.数据分组
         * 4.划分个股时间窗口
         * 5.个股窗口数据处理
         * 6.划分板块时间窗口
         * 7.板块窗口数据处理
         * 8.转换并封装数据
         * 9.写入HDFS
         */
        //1.设置HDFS存储路径
        BucketingSink<String> bucketingSink = new BucketingSink<>(QuotConfig.config.getProperty("sector.sec.hdfs.path"));
        //2.设置数据文件参数
        // （大小、分区、格式、前缀、后缀）
        bucketingSink.setBatchSize(Long.valueOf(QuotConfig.config.getProperty("hdfs.batch")));
        bucketingSink.setWriter(new StringWriter<>());
        bucketingSink.setBucketer(new DateTimeBucketer<>(QuotConfig.config.getProperty("hdfs.bucketer")));
        bucketingSink.setInProgressPrefix("sector-");
        bucketingSink.setPendingPrefix("sector2-");
        bucketingSink.setInProgressSuffix(".txt");
        bucketingSink.setPendingSuffix(".txt");

        //3.数据分组
        waterData.keyBy(new KeyFunction())
                //4.划分个股时间窗口
                .timeWindow(Time.minutes(1))
                //5.个股窗口数据处理
                .apply(new MinStockWindowFunction())
                //6.划分板块时间窗口
                .timeWindowAll(Time.minutes(1))
                //7.板块窗口数据处理
                .apply(new SectorWindowFunction())
                //8.转换并封装数据
                .map(new SectorPutHdfsMap())
                //9.写入HDFS
                .addSink(bucketingSink);

    }
}
