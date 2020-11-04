package com.fei.map;

import com.fei.bean.StockBean;
import com.fei.constant.Constant;
import com.fei.util.DateUtil;
import com.fei.util.DbUtil;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.types.Row;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * @description:通用业务处理代码：日/周/月K
 * @author: 飞
 * @date: 2020/11/3 0003 20:39
 */
public class StockKlineMap extends RichMapFunction<StockBean, Row> {

    /**
     * 一、初始化
     * 1.创建构造方法
     * 入参：kType：K线类型
     * firstTxdate：周期首个交易日
     * 2.获取交易日历表交易日数据
     * 3.获取周期首个交易日和T日
     * 4.获取K(周、月)线下的汇总表数据（高、低、成交量、金额）
     */
    //1.创建构造方法‘
    private String kType;
    private String firstTxdate; //周期首个交易日

    public StockKlineMap(String kType, String firstTxdate) {
        this.kType = kType;
        this.firstTxdate = firstTxdate;
    }

    String tradeDate = null;
    String firstTradeDate = null;
    Map<String, Map<String, Object>> klineMap = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        //2.获取交易日历表最新交易日数据
        String sql = "SELECT * FROM  tcc_date WHERE trade_date <=  CURDATE() ORDER BY trade_date DESC LIMIT 1";
        Map<String, String> dateMap = DbUtil.queryKv(sql);
        //3.获取周期首个交易日和T日

        //如果是日K =》 tradeDate = firstTradeDate
        //如果是周/月K =》 tradeDate >=  firstTradeDate
        // (如果T日是周期首个交易日，日/周/月，都是同一天，如果T日不是周期首个交易日 tradeDate >  firstTradeDate)
        tradeDate = dateMap.get("trade_date");//获取T日
        firstTradeDate = dateMap.get(firstTxdate); //期首个交易日

        //4.获取K(周、月)线下的汇总表数据（高、低、成交量、金额）
        String sqlKline = "SELECT sec_code ,MAX(high_price) AS high_price,MIN(low_price) AS low_price ,SUM(trade_amt) AS trade_amt,\n" +
                "SUM(trade_vol) AS trade_vol FROM bdp_quot_stock_kline_day \n" +
                "WHERE trade_date BETWEEN " + firstTradeDate + " AND " + tradeDate + " \n" +
                "GROUP BY 1";

        klineMap = DbUtil.query("sec_code", sql);
    }

    @Override
    public Row map(StockBean value) throws Exception {

        /**
         * 二、业务处理
         * 1.获取个股部分数据（前收、收、开盘、高、低、量、金额）
         * 2.获取T日和周首次交易日时间,转换成long型
         * 3.比较周期首个交易日和当天交易日大小，判断是否是周、月K线
         * 4.获取周/月K数据：成交量、成交额、高、低
         * 5.高、低价格比较
         * 6.计算成交量、成交额
         * 7.计算均价
         * 8.封装数据Row
         */
        //1.获取个股部分数据（前收、收、开盘、高、低、量、金额）
        BigDecimal preClosePrice = value.getPreClosePrice();
        BigDecimal closePrice = value.getClosePrice();
        BigDecimal openPrice = value.getOpenPrice();
        BigDecimal highPrice = value.getHighPrice();
        BigDecimal lowPrice = value.getLowPrice();
        Long tradeVolDay = value.getTradeVolDay();
        Long tradeAmtDay = value.getTradeAmtDay();

        //2.获取T日和周首次交易日时间,转换成long型
        Long tradeTime = DateUtil.stringToLong(tradeDate, Constant.format_yyyy_mm_dd); //long型T日
        Long firstTradeTime = DateUtil.stringToLong(firstTradeDate, Constant.format_yyyy_mm_dd); //long型周期内首个交易日
        // 3.比较周期首个交易日和当天交易日大小，判断是否是周、月K线
        if (firstTradeTime < tradeTime && (kType.equals("2") || kType.equals("3"))) { //是周K或者是月K
            //4.获取周/月K数据：成交量、成交额、高、低
            Map<String, Object> map = klineMap.get(value.getSecCode());
            if (map != null && map.size() > 0) {
                Long tradeVol = Long.valueOf(map.get("trade_vol").toString()); //历史总成交量
                long tradeAmt = Double.valueOf(map.get("trade_amt").toString()).longValue(); //历史总成交金额
                //6.计算成交量、成交额
                tradeVolDay += tradeVol; //周期内的最新总成交量
                tradeAmtDay += tradeAmt;//周期内的最新总成交金额
                //获取历史最高价
                BigDecimal high_price = new BigDecimal(map.get("high_price").toString());
                //获取历史最低价
                BigDecimal low_price = new BigDecimal(map.get("low_price").toString());

                //5.高、低价格比较
                //获取最新的最高价
                if (highPrice.compareTo(high_price) == -1) {
                    highPrice = high_price;
                }

                //获取最新最低价
                if (lowPrice.compareTo(low_price) == 1) {
                    lowPrice = low_price;
                }
            }


        }

        //7.计算均价
        //成交金额/成交量
        BigDecimal avgPrice = new BigDecimal(0);
        if (tradeVolDay != 0) {
            avgPrice = new BigDecimal(tradeAmtDay).divide(new BigDecimal(tradeVolDay), 2, BigDecimal.ROUND_HALF_UP);
        }

        //8.封装数据Row
        Row row = new Row(13);

        row.setField(0, new Timestamp(new Date().getTime()));
        row.setField(1, tradeDate);
        row.setField(2, value.getSecCode());
        row.setField(3, value.getSecName());
        row.setField(4, kType);
        row.setField(5, preClosePrice);
        row.setField(6, openPrice);
        row.setField(7, highPrice);
        row.setField(8, lowPrice);
        row.setField(9, closePrice);
        row.setField(10, avgPrice);
        row.setField(11, tradeVolDay);
        row.setField(12, tradeAmtDay);
        return row;
    }


}
