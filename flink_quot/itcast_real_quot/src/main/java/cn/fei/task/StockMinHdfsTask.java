package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.config.QuotConfig;
import cn.fei.function.KeyFunction;
import cn.fei.function.MinStockWindowFunction;
import cn.fei.inter.ProcessDataInterface;
import cn.fei.map.StockPutHdfsMap;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.fs.StringWriter;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;

/**
 * @Date 2020/11/1
 * 分时行情数据备份至HDFS
 */
public class StockMinHdfsTask implements ProcessDataInterface {


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
        //实时写数据到文件是基于flink-1.7.0
        //1.设置HDFS存储路径
        //获取写入对象
        BucketingSink<String> bucketingSink = new BucketingSink<>(QuotConfig.config.getProperty("stock.sec.hdfs.path"));//设置文件路径
        //2.设置数据文件参数
        //  （大小、分区、格式、前缀、后缀）
        bucketingSink.setBatchSize(Long.valueOf(QuotConfig.config.getProperty("hdfs.batch")));//大小
        bucketingSink.setBucketer(new DateTimeBucketer<>(QuotConfig.config.getProperty("hdfs.bucketer")));//按时间分区
        //格式 =》String
        bucketingSink.setWriter(new StringWriter<>());
        //前缀、后缀
        bucketingSink.setInProgressPrefix("stock-");//正在写入的文件
        bucketingSink.setPendingPrefix("stock2-");
        bucketingSink.setInProgressSuffix(".txt");
        bucketingSink.setPendingSuffix(".txt");

        //3.数据分组
        waterData.keyBy(new KeyFunction())
                //4.划分时间窗口
                .timeWindow(Time.minutes(1))
                //5.数据处理
                .apply(new MinStockWindowFunction())
                //6.转换并封装数据
                .map(new StockPutHdfsMap())
                //7.写入HDFS
                .addSink(bucketingSink);
    }
}
