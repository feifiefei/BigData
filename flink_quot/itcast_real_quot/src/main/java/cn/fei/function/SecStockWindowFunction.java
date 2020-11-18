package cn.fei.function;

import cn.fei.bean.CleanBean;
import cn.fei.bean.StockBean;
import cn.fei.constant.Constant;
import cn.fei.util.DateUtil;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @Date 2020/11/1
 * 5s钟一个窗口
 */
//1.新建SecStockWindowFunction 窗口函数
public class SecStockWindowFunction implements WindowFunction<CleanBean, StockBean,String, TimeWindow> {
    @Override
    public void apply(String str, TimeWindow window, Iterable<CleanBean> input, Collector<StockBean> out) throws Exception {


        //开发步骤：
        //2.记录最新个股
        //3.格式化日期
        //4.封装输出数据

        //2.记录最新个股
        //每5s生成一条最新行情数据(事件时间最大的数据，就是最新一条行情数据)
        CleanBean cleanBean = null;
        for (CleanBean line : input) {
            if(cleanBean == null ){
                cleanBean = line;
            }
            //比较事件时间
            if(cleanBean.getEventTime()< line.getEventTime()){
                cleanBean = line;
            }
        }

        //3.格式化日期
        //格式化之后的事件时间,做rowkey拼接使用
        Long tradeTime = DateUtil.longTimeTransfer(cleanBean.getEventTime(), Constant.format_YYYYMMDDHHMMSS);
        //4.封装输出数据
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
                cleanBean.getSource());

        //数据收集
        out.collect(stockBean);
    }
}
