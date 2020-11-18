package cn.fei.function;

import cn.fei.bean.CleanBean;
import cn.fei.bean.IndexBean;
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
//1.新建MinIndexWindowFunction 窗口函数
//获取指数分时行情数据（分时成交量/金额）
public class MinIndexWindowFunction extends RichWindowFunction<CleanBean, IndexBean, String, TimeWindow> {

    //2.初始化 MapState<String, IndexBean>
    //缓存上一窗口的指数分时行情数据
    MapState<String, IndexBean> indexMs = null;

    @Override
    public void open(Configuration parameters) throws Exception {

        indexMs = getRuntimeContext().getMapState(new MapStateDescriptor<String, IndexBean>("indexMs", String.class, IndexBean.class));
    }

    @Override
    public void apply(String s, TimeWindow window, Iterable<CleanBean> input, Collector<IndexBean> out) throws Exception {

        /**
         * 开发步骤：
         * 1.新建MinIndexWindowFunction 窗口函数
         * 2.初始化 MapState<String, IndexBean>
         * 3.记录最新指数
         * 4.获取分时成交额和成交数量
         * 5.格式化日期
         * 6.封装输出数据
         * 7.更新MapState
         */
        //3.记录最新指数
        CleanBean cleanBean = null;
        for (CleanBean line : input) {
            if (cleanBean == null) {
                cleanBean = line;
            }
            if (cleanBean.getEventTime() < line.getEventTime()) {
                cleanBean = line;
            }
        }

        //4.获取分时成交额和成交数量
        //获取的是上一窗口的指数行情数据
        Long curTradeVol = 0L;
        Long curTradeAmt = 0L;
        IndexBean indexBeanLast = indexMs.get(cleanBean.getSecCode());
        if (indexBeanLast != null) {

            Long tradeAmtDayLast = indexBeanLast.getTradeAmtDay();//上一窗口的总成交金额
            Long tradeVolDayLast = indexBeanLast.getTradeVolDay();//上一窗口的总成交量

            //获取当前窗口的总成交金额/量
            Long tradeAmt = cleanBean.getTradeAmt();
            Long tradeVolumn = cleanBean.getTradeVolumn();

            //计算分时成交量/金额
            curTradeVol = tradeVolumn - tradeVolDayLast;
            curTradeAmt = tradeAmt - tradeAmtDayLast;
        }

        // 5.格式化日期
        Long tradeTime = DateUtil.longTimeTransfer(cleanBean.getEventTime(), Constant.format_YYYYMMDDHHMMSS);

        //6.封装输出数据
        IndexBean indexBean = new IndexBean(
                cleanBean.getEventTime(),
                cleanBean.getSecCode(),
                cleanBean.getSecName(),
                cleanBean.getPreClosePrice(),
                cleanBean.getOpenPrice(),
                cleanBean.getMaxPrice(),
                cleanBean.getMinPrice(),
                cleanBean.getTradePrice(),
                curTradeVol, curTradeAmt,
                cleanBean.getTradeVolumn(),
                cleanBean.getTradeAmt(),
                tradeTime,
                cleanBean.getSource()
        );

        out.collect(indexBean);
        //7.更新MapState
        indexMs.put(cleanBean.getSecCode(),indexBean);
    }
}
