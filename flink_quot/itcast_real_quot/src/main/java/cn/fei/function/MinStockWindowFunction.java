package cn.fei.function;

import cn.fei.bean.CleanBean;
import cn.fei.bean.StockBean;
import cn.fei.constant.Constant;
import cn.fei.util.DateUtil;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @Date 2020/11/1
 */
//1.新建MinStockWindowFunction 窗口函数
public class MinStockWindowFunction extends RichWindowFunction<CleanBean, StockBean, String, TimeWindow> {

    // 2.初始化 MapState<String, StockBean>;Key:secCode,value:StockBean
    //缓存的是上一窗口（上一分钟）的分时行情数据
    MapState<String, StockBean> stockMs = null;
    @Override
    public void open(Configuration parameters) throws Exception {
       stockMs = getRuntimeContext().getMapState(new MapStateDescriptor<String, StockBean>("stockMs", String.class, StockBean.class));
    }

    @Override
    public void apply(String key, TimeWindow window, Iterable<CleanBean> input, Collector<StockBean> out) throws Exception {

        /**
         * 开发步骤：
         * 1.新建MinStockWindowFunction 窗口函数
         * 2.初始化 MapState<String, StockBean>
         * 3.记录最新个股
         * 4.获取分时成交额和成交数量
         * 5.格式化日期
         * 6.封装输出数据
         * 7.更新MapState
         */
        //3.记录最新个股
        //获取当前窗口60s内的最新一条行情数据=》分时行情数据
        CleanBean cleanBean = null;
        for (CleanBean line : input) {

            if(cleanBean == null ){
                cleanBean = line;
            }
            if(cleanBean.getEventTime()< line.getEventTime()){
                cleanBean = line;
            }
        }

        //4.获取分时成交额和成交数量
        //分时成交量 （当前分钟的总成交量- 上一分钟的总成交量）
        //分时成交金额 （当前分钟的总成交金额- 上一分钟的总成交金额）
        StockBean stockBeanLast = stockMs.get(cleanBean.getSecCode()); //上一窗口的分时行情
        Long tradeVol = 0l;//分时成交量
        Long tradeAmt = 0l;
        if(stockBeanLast != null){
            //获取上一分钟的总成交量/金额
            Long tradeVolDayLast = stockBeanLast.getTradeVolDay();
            Long tradeAmtDayLast = stockBeanLast.getTradeAmtDay();
            //分时成交量
            tradeVol = cleanBean.getTradeVolumn() - tradeVolDayLast;
            //分时成交金额
            tradeAmt = cleanBean.getTradeAmt() - tradeAmtDayLast;
        }

        //5.格式化日期
        Long tradeTime = DateUtil.longTimeTransfer(cleanBean.getEventTime(), Constant.format_YYYYMMDDHHMMSS);

        // 6.封装输出数据
        StockBean stockBean = new StockBean(
                cleanBean.getEventTime(),
                cleanBean.getSecCode(),
                cleanBean.getSecName(),
                cleanBean.getPreClosePrice(),
                cleanBean.getOpenPrice(),
                cleanBean.getMaxPrice(),
                cleanBean.getMinPrice(),
                cleanBean.getTradePrice(),
                tradeVol, tradeAmt,
                cleanBean.getTradeVolumn(),
                cleanBean.getTradeAmt(),
                tradeTime,
                cleanBean.getSource());
        out.collect(stockBean);


        //7.更新MapState
        stockMs.put(cleanBean.getSecCode(),stockBean);
    }
}
