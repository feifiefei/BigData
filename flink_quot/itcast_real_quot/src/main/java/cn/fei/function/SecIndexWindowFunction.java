package cn.fei.function;

import cn.fei.bean.CleanBean;
import cn.fei.bean.IndexBean;
import cn.fei.constant.Constant;
import cn.fei.util.DateUtil;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @Date 2020/11/1
 */
public class SecIndexWindowFunction extends RichWindowFunction<CleanBean, IndexBean, String, TimeWindow> {
    @Override
    public void apply(String s, TimeWindow window, Iterable<CleanBean> input, Collector<IndexBean> out) throws Exception {
        /**
         * 开发步骤：
         * 1.新建SecIndexWindowFunction 窗口函数
         * 2.记录最新指数
         * 3.格式化日期
         * 4.封装输出数据
         */
        //2.记录最新指数
        CleanBean cleanBean = null;
        for (CleanBean line : input) {
            if (cleanBean == null) {
                cleanBean = line;
            }
            if (cleanBean.getEventTime() < line.getEventTime()) {
                cleanBean = line;
            }
        }

        //3.格式化日期
        Long tradeTime = DateUtil.longTimeTransfer(cleanBean.getEventTime(), Constant.format_YYYYMMDDHHMMSS);

        //4.封装输出数据
        //eventTime、indexCode、indexName、preClosePrice、openPrice、highPrice、lowPrice、closePrice、
        //tradeVol、tradeAmt、tradeVolDay、tradeAmtDay、tradeTime、source
        out.collect(new IndexBean(
                cleanBean.getEventTime(),
                cleanBean.getSecCode(),
                cleanBean.getSecName(),
                cleanBean.getPreClosePrice(),
                cleanBean.getOpenPrice(),
                cleanBean.getMaxPrice(),
                cleanBean.getMinPrice(),
                cleanBean.getTradePrice(),
                0l,0l,
                cleanBean.getTradeVolumn(),
                cleanBean.getTradeAmt(),
                tradeTime,
                cleanBean.getSource()
        ));

    }
}
