package com.fei.task;

import com.fei.bean.CleanBean;
import com.fei.config.QuotConfig;
import com.fei.function.KeyFunction;
import com.fei.function.SecStockHbaseFunction;
import com.fei.function.SecStockWindowFunction;
import com.fei.inter.ProcessDataInterface;
import com.fei.sink.SinkHbase;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @description:每5s生成一条最新行情数据(事件时间最大的数据，就是最新一条行情数据)
 * @author: 飞
 * @date: 2020/11/1 0001 20:44
 */
public class StockSecTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {
        /**
         * 1.每5s生成一条最新行情数据(事件时间最大的数据，就是最新一条行情数据)
         * 2.批量插入 ，需要把数据封装进List<Put>
         * 3.窗口时间5s
         */
        /**
         * 开发步骤：
         * 1.数据分组
         * 2.划分时间窗口
         * 3.新建个股数据写入bean对象
         * 4.秒级窗口函数业务处理
         * 5.数据写入操作
         *   * 封装ListPuts
         *   * 数据写入
         */
        //分组
        waterData.keyBy(new KeyFunction())
                //划分时间窗口
                .timeWindow(Time.seconds(5))
                //秒级窗口函数业务处理
                .apply(new SecStockWindowFunction())
                .timeWindowAll(Time.seconds(5))
                //5.数据写入操作
                //封装ListPuts
                .apply(new SecStockHbaseFunction())
                //数据写入
                .addSink(new SinkHbase(QuotConfig.config.getProperty("stock.hbase.table.name")));


    }
}
