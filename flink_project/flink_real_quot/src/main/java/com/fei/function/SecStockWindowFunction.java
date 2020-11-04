package com.fei.function;

import com.fei.bean.CleanBean;
import com.fei.bean.StockBean;
import com.fei.constant.Constant;
import com.fei.util.DateUtil;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/1 0001 20:54
 */
public class SecStockWindowFunction implements WindowFunction<CleanBean, StockBean, String, TimeWindow> {
    @Override
    public void apply(String s, TimeWindow timeWindow, Iterable<CleanBean> iterable, Collector<StockBean> collector) throws Exception {
        //开发步骤：
        //2.记录最新个股
        //3.格式化日期
        //4.封装输出数据
        //记录最新的个股
        //每5s生成一条最新行情数据(事件时间最大的数据，就是最新一条行情数据)
        CleanBean cleanBean = null;
        for (CleanBean bean : iterable) {
            if (cleanBean == null) {
                cleanBean = bean;
            }
            if (cleanBean.getEventTime() < bean.getEventTime()) {
                cleanBean = bean;
            }
        }
        //格式化日期
        //格式化之后的事件时间,做rowkey拼接使用
        Long tradeTime = DateUtil.longTimeTransfer(cleanBean.getEventTime(), Constant.format_YYYYMMDDHHMMSS);
        //封装输出数据
        //  //eventTime、secCode、secName、preClosePrice、openPrice、highPrice、lowPrice、closePrice、
        //    //tradeVol、tradeAmt、tradeVolDay、tradeAmtDay、tradeTime、source
        StockBean stockBean = new StockBean(
                cleanBean.getEventTime(),
                cleanBean.getSecCode(),
                cleanBean.getSecName(),
                cleanBean.getPreClosePrice(),
                cleanBean.getOpenPrice(),
                cleanBean.getMaxPrice(),
                cleanBean.getMinPrice(),
                cleanBean.getTradePrice(),
                0l, 0l,
                cleanBean.getTradeVolumn(),
                cleanBean.getTradeAmt(),
                tradeTime,
                cleanBean.getSource()

        );

        collector.collect(stockBean);
    }
}
