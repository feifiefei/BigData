package com.fei.function;

import com.alibaba.fastjson.JSON;
import com.fei.bean.StockBean;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.client.Put;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/1 0001 21:19
 */
public class SecStockHbaseFunction implements AllWindowFunction<StockBean, List<Put>, TimeWindow> {
    @Override
    public void apply(TimeWindow window, Iterable<StockBean> values, Collector<List<Put>> out) throws Exception {
        /**
         * 开发步骤：
         * 1.新建List<Put>
         * 2.循环数据
         * 3.设置rowkey
         * 4.json数据转换
         * 5.封装put
         * 6.收集数据
         */
        //1.新建List<Put>
        List<Put> list = new ArrayList<>();
        for (StockBean value : values) {
            //设置rowkey
            String rowkey = value.getSecCode() + value.getEventTime();
            //json数据转化
            String str = JSON.toJSONString(rowkey);
            Put put = new Put(rowkey.getBytes());
            put.add("info".getBytes(), "data".getBytes(), str.getBytes());
            list.add(put);


        }
        out.collect(list);
    }
}
