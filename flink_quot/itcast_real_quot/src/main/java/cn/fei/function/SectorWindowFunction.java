package cn.fei.function;

import cn.fei.bean.SectorBean;
import cn.fei.bean.StockBean;
import cn.fei.util.DbUtil;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.RichAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2020/11/3
 */
public class SectorWindowFunction extends RichAllWindowFunction<StockBean, SectorBean, TimeWindow> {

    /**
     * 一：open初始化
     * 1. 初始化数据：板块对应关系、最近交易日日K（上一交易日）
     * 2. 定义状态MapState<String, SectorBean >:上一个窗口板块数据
     * 3. 初始化基准价
     */
    Map<String, List<Map<String, Object>>> sectorStockMap = null;

    Map<String, Map<String, Object>> sectorKlineMap = null;
    MapState<String, SectorBean> sectorMs = null;
    //3. 初始化基准价
    BigDecimal basePrice = new BigDecimal(1000);

    @Override
    public void open(Configuration parameters) throws Exception {
        // 1. 初始化数据：板块对应关系、最近交易日日K（上一交易日）
        //查询板块个股对应关系表
        String sql = "SELECT * FROM bdp_sector_stock WHERE sec_abbr = 'ss'";
        sectorStockMap = DbUtil.queryForGroup("sector_code", sql);

        //最近交易日日K（上一交易日）
        String sqlKline = "SELECT * FROM bdp_quot_sector_kline_day WHERE trade_date  = (SELECT trade_date FROM tcc_date WHERE trade_date < CURDATE() ORDER BY trade_date DESC LIMIT 1 )\n";
        sectorKlineMap = DbUtil.query("sector_code", sqlKline);

        //2. 定义状态MapState<String, SectorBean >:上一个窗口板块数据
        sectorMs = getRuntimeContext().getMapState(new MapStateDescriptor<String, SectorBean>("sectorMs", String.class, SectorBean.class));
    }

    @Override
    public void apply(TimeWindow window, Iterable<StockBean> values, Collector<SectorBean> out) throws Exception {

        /**
         * 开发步骤
         * 1.循环窗口内个股数据并缓存
         * 2.轮询板块对应关系表下的个股数据、并获取指定板块下的个股
         * 3.初始化全部数据
         * 4.轮询板块下的个股数据，并获取板块名称、个股代码、个股流通股本和前一日板块总流通市值
         * 5.计算：开盘价/收盘价累计流通市值、累计（交易量、交易金额）（注意：是个股累计值，获取缓存个股数据）
         *    累计个股开盘流通市值 = SUM(板块下的个股开盘价*个股流通股本)
         *    累计个股收盘流通市值 = SUM(板块下的个股收盘价*个股流通股本)
         * 6.判断是否是首日上市，并计算板块开盘价和收盘价
         *    板块开盘价格 = 板块前收盘价*当前板块以开盘价计算的总流通市值/当前板块前一交易日板块总流通市值
         *    板块当前价格 = 板块前收盘价*当前板块以收盘价计算的总流通市值/当前板块前一交易日板块总流通市值
         * 7.初始化板块高低价
         * 8.获取上一个窗口板块数据（高、低、成交量、成交金额）
         * 9.计算最高价和最低价（前后窗口比较）
         * 10.计算分时成交量和成交金额
         * 11.开盘价与高低价比较
         * 12.封装结果数据
         * 13.缓存当前板块数据
         */
        //1.循环窗口内个股数据并缓存
        Map<String, StockBean> map = new HashMap<>();
        for (StockBean value : values) {
            map.put(value.getSecCode(), value);
        }
        //2.轮询板块对应关系表下的个股数据、并获取指定板块下的个股列表
        for (String sectorCode : sectorStockMap.keySet()) {
            //指定板块下的个股列表
            List<Map<String, Object>> list = sectorStockMap.get(sectorCode);
            //3.初始化全部数据
            Long eventTime = 0l;
            String sectorName = null;
            BigDecimal preClosePrice = new BigDecimal(0);
            BigDecimal openPrice = new BigDecimal(0);
            BigDecimal highPrice = new BigDecimal(0);
            BigDecimal lowPrice = new BigDecimal(0);
            BigDecimal closePrice = new BigDecimal(0);
            Long tradeVol = 0l; //分时成交量
            Long tradeAmt = 0l; //分时成交金额
            Long tradeVolDay = 0l; //总成交量
            Long tradeAmtDay = 0l; //总成交金额
            Long tradeTime = 0l;//格式化之后的事件时间，拼接rowkey使用

            //4.轮询板块下的个股数据，并获取板块名称、个股代码、个股流通股本和前一日板块总流通市值
            //累计个股开盘流通市值
            BigDecimal totalOpenNegoCap = new BigDecimal(0);
            //累计个股收盘流通市值
            BigDecimal totalCloseNegoCap = new BigDecimal(0);
            BigDecimal preSectorNegoCap = new BigDecimal(0);
            for (Map<String, Object> stockMap : list) {
                //板块名称
                sectorName = stockMap.get("sector_name").toString();
                String secCode = stockMap.get("sec_code").toString();
                BigDecimal negoCap = new BigDecimal(stockMap.get("nego_cap").toString()); //个股流通股本
                //前一日板块总流通市值
                preSectorNegoCap = new BigDecimal(stockMap.get("pre_sector_nego_cap").toString());

                //5.计算：开盘价/收盘价累计流通市值、累计（交易量、交易金额）（注意：是个股累计值，获取缓存个股数据）
                //   累计个股开盘流通市值 = SUM(板块下的个股开盘价*个股流通股本)
                //   累计个股收盘流通市值 = SUM(板块下的个股收盘价*个股流通股本)
                StockBean stockBean = map.get(secCode);
                if (stockBean != null) { //说明实时流中的个股数据，一定存在于板块个股对应关系表中
                    eventTime = stockBean.getEventTime();
                    tradeTime = stockBean.getTradeTime();
                    //累计（交易量、交易金额）
                    //板块交易量
                    tradeVolDay += stockBean.getTradeVolDay();
                    //板块交易金额
                    tradeAmtDay += stockBean.getTradeAmtDay();
                    //板块下的个股开盘价*个股流通股本  = 单只个股的开盘流通市值
                    BigDecimal singleStockOpen = stockBean.getOpenPrice().multiply(negoCap).setScale(2, RoundingMode.HALF_UP);
                    totalOpenNegoCap = totalOpenNegoCap.add(singleStockOpen);
                    //板块下的个股收盘价*个股流通股本  = 单只个股的收盘流通市值
                    BigDecimal singleStockClose = stockBean.getClosePrice().multiply(negoCap).setScale(2, RoundingMode.HALF_UP);
                    totalCloseNegoCap = totalCloseNegoCap.add(singleStockClose);
                }
            }

            //6.判断是否是首日上市，并计算板块开盘价和收盘价
            //   板块开盘价格 = 板块前收盘价*当前板块以开盘价计算的总流通市值/当前板块前一交易日板块总流通市值
            //   板块当前价格 = 板块前收盘价*当前板块以收盘价计算的总流通市值/当前板块前一交易日板块总流通市值
            if (sectorKlineMap == null || sectorKlineMap.get(sectorCode) == null) { //板块首日上市
                preClosePrice = basePrice;
                openPrice = (preClosePrice.multiply(totalOpenNegoCap)).divide(preSectorNegoCap,2,RoundingMode.HALF_UP);
                closePrice = (preClosePrice.multiply(totalCloseNegoCap)).divide(preSectorNegoCap,2,RoundingMode.HALF_UP);
            }else{
                Map<String, Object> map1 = sectorKlineMap.get(sectorCode);
                if(map1 != null){ //说明此板块已经上市，而且前一交易日已经有数据
                    //取前收盘价
                    preClosePrice = new BigDecimal(map1.get("close_price").toString());
                    openPrice = (preClosePrice.multiply(totalOpenNegoCap)).divide(preSectorNegoCap,2,RoundingMode.HALF_UP);
                    closePrice = (preClosePrice.multiply(totalCloseNegoCap)).divide(preSectorNegoCap,2,RoundingMode.HALF_UP);
                }
            }

            //7.初始化板块高低价
            highPrice = closePrice;
            lowPrice = closePrice;

            //8.获取上一个窗口板块数据（高、低、成交量、成交金额）
            SectorBean sectorBeanLast= sectorMs.get(sectorCode);
            if(sectorBeanLast != null){
                BigDecimal highPriceLast = sectorBeanLast.getHighPrice();
                BigDecimal lowPriceLast = sectorBeanLast.getLowPrice();
                Long tradeVolDayLast = sectorBeanLast.getTradeVolDay();
                Long tradeAmtDayLast = sectorBeanLast.getTradeAmtDay();
                tradeVol = tradeVolDay - tradeVolDayLast;
                tradeAmt = tradeAmtDay - tradeAmtDayLast;

                //取最高价
                if(highPrice.compareTo(highPriceLast) == -1){
                    highPrice = highPriceLast;
                }
                //取最低价
                if(lowPrice.compareTo(lowPriceLast) ==1){
                    lowPrice = lowPriceLast;
                }
            }

            //11.开盘价与高低价比较
            if(openPrice.compareTo(highPrice) == 1){
                highPrice = openPrice;
            }
            if(openPrice.compareTo(lowPrice) == -1){
                lowPrice = openPrice;
            }


            //12.封装结果数据
            //一个板块代码，对应一条板块行情数据
            SectorBean sectorBean = new SectorBean();
            //eventTime、sectorCode、sectorName、preClosePrice、openPrice、highPrice、lowPrice、closePrice、
            //tradeVol、tradeAmt、tradeVolDay、tradeAmtDay、tradeTime
            sectorBean.setEventTime(eventTime);
            sectorBean.setSectorCode(sectorCode);
            sectorBean.setSectorName(sectorName);
            sectorBean.setPreClosePrice(preClosePrice);
            sectorBean.setOpenPrice(openPrice);
            sectorBean.setHighPrice(highPrice);
            sectorBean.setLowPrice(lowPrice);
            sectorBean.setClosePrice(closePrice);
            sectorBean.setTradeVol(tradeVol);
            sectorBean.setTradeAmt(tradeAmt);
            sectorBean.setTradeVolDay(tradeVolDay);
            sectorBean.setTradeAmtDay(tradeAmtDay);
            sectorBean.setTradeTime(tradeTime);
            out.collect(sectorBean);
            //缓存当前窗口数据
            sectorMs.put(sectorCode,sectorBean);
        }

    }
}
