package cn.fei.function;

import cn.fei.bean.CleanBean;
import cn.fei.bean.StockIncrBean;
import cn.fei.constant.Constant;
import cn.fei.util.DateUtil;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Date 2020/11/1
 */
//1.新建StockIncreWindowFunction 窗口函数
public class StockIncreWindowFunction extends RichWindowFunction<CleanBean, StockIncrBean,String, TimeWindow> {
    @Override
    public void apply(String s, TimeWindow window, Iterable<CleanBean> input, Collector<StockIncrBean> out) throws Exception {


        /**
         * 开发步骤：
         * 1.新建MinStockIncrWindowFunction 窗口函数
         * 2.记录最新个股
         * 3.格式化日期
         * 4.指标计算
         *   涨跌、涨跌幅、振幅
         * 5.封装输出数据
         */
        //2.记录最新个股
        CleanBean cleanBean = null;
        for (CleanBean line : input) {
            if(cleanBean == null ){
                cleanBean = line;
            }
            if(cleanBean.getEventTime() < line.getEventTime()){
                cleanBean = line;
            }
        }

        //3.格式化日期
        Long tradeTime = DateUtil.longTimeTransfer(cleanBean.getEventTime(), Constant.format_YYYYMMDDHHMMSS);
        //4.指标计算
        //  涨跌、涨跌幅、振幅
        /**
         * 今日涨跌=当前价-前收盘价
         * 今日涨跌幅（%）=（当前价-前收盘价）/ 前收盘价 * 100%
         * 今日振幅 =（当日最高点的价格－当日最低点的价格）/昨天收盘价×100% ，反应价格波动情况
         */
        //涨跌
        BigDecimal updown = cleanBean.getTradePrice().subtract(cleanBean.getPreClosePrice());
        //涨跌幅
        BigDecimal increase = updown.divide(cleanBean.getPreClosePrice(), 2, RoundingMode.HALF_UP); //四舍五入，保留两位小数
        //        //今日振幅
        BigDecimal amplitude = (cleanBean.getMaxPrice().subtract(cleanBean.getMinPrice())).divide(cleanBean.getPreClosePrice(), 2, RoundingMode.HALF_UP);

        //5.封装输出数据
        //eventTime、secCode、secName、increase、tradePrice、updown、tradeVol、amplitude、
        //preClosePrice、tradeAmt、tradeTime、source
        out.collect(new StockIncrBean(
                cleanBean.getEventTime(),
                cleanBean.getSecCode(),
                cleanBean.getSecName(),
                increase,
                cleanBean.getTradePrice(),
                updown,
                cleanBean.getTradeVolumn(),
                amplitude,
                cleanBean.getPreClosePrice(),
                cleanBean.getTradeAmt(),
                tradeTime,
                cleanBean.getSource()
        ));

    }
}
