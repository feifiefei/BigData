package cn.fei.map;

import cn.fei.bean.IndexBean;
import cn.fei.constant.Constant;
import cn.fei.util.DateUtil;
import cn.fei.util.DbUtil;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.types.Row;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * @Date 2020/11/3
 */
public class IndexKlineMap extends RichMapFunction<IndexBean, Row> {
    /**
     * 一、初始化
     * 1.创建构造方法
     * 入参：kType：K线类型
     * firstTxdate：周期首个交易日
     * 2.获取交易日历表交易日数据
     * 3.获取周期首个交易日和T日
     * 4.获取K线下的汇总表数据（高、低、成交量、金额）
     */
    private String kType;
    private String firstTxDate;

    public IndexKlineMap(String kType, String firstTxDate) {
        this.kType = kType;
        this.firstTxDate = firstTxDate;
    }

    //一、初始化
    Map<String, Map<String, Object>> klineMap = null;
    String tradeDate = null;
    String firstTradeDate = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        //2.获取交易日历表交易日数据
        String sql = "SELECT * FROM tcc_date WHERE trade_date <= CURDATE() ORDER BY trade_date DESC LIMIT 1";
        Map<String, String> dateMap = DbUtil.queryKv(sql);
        // 3.获取周期首个交易日和T日
        tradeDate = dateMap.get("trade_date"); //最新交易日（T日）
        firstTradeDate = dateMap.get(firstTxDate); //周期首个交易日
        // 4.获取K线下的汇总表数据（高、低、成交量、金额）
        String sqlKline = "SELECT index_code ,MAX(high_price) as high_price ,MIN(low_price) as low_price," +
                "SUM(trade_vol) as trade_vol,SUM(trade_amt) as trade_amt \n" +
                "FROM bdp_quot_index_kline_day\n" +
                "WHERE trade_date BETWEEN " + firstTradeDate + "   AND " + tradeDate + "  \n" +
                "GROUP BY 1";

        klineMap = DbUtil.query("index_code", sqlKline);
    }

    @Override
    public Row map(IndexBean value) throws Exception {

        /**
         * 开发步骤：
         * 1.获取指数部分数据（前收、收、开盘、高、低、量、金额）
         * 2.获取T日和周首次交易日时间,转换成long型
         * 3.比较周期首个交易日和当天交易日大小，判断是否是周、月K线
         * 4.获取周/月K数据：成交量、成交额、高、低
         * 5.高、低价格比较
         * 6.计算成交量、成交额
         * 7.计算均价
         * 8.封装数据Row
         */
        //1.获取指数部分数据（前收、收、开盘、高、低、量、金额）
        BigDecimal preClosePrice = value.getPreClosePrice();
        BigDecimal openPrice = value.getOpenPrice();
        BigDecimal closePrice = value.getClosePrice();
        BigDecimal highPrice = value.getHighPrice();
        BigDecimal lowPrice = value.getLowPrice();
        Long tradeAmtDay = value.getTradeAmtDay();
        Long tradeVolDay = value.getTradeVolDay();

        //2.获取T日和周首次交易日时间,转换成long型
        Long tradeTime = DateUtil.stringToLong(tradeDate, Constant.format_yyyy_mm_dd);
        Long firstTradeTime = DateUtil.stringToLong(firstTradeDate, Constant.format_yyyy_mm_dd);
        if (firstTradeTime < tradeTime && (kType.equals("2") || kType.equals("3"))) { //是周/月K
            Map<String, Object> map = klineMap.get(value.getIndexCode());
            if (map != null && map.size() > 0) {

                //获取周期内的历史数据
                BigDecimal high_price = new BigDecimal(map.get("high_price").toString());
                BigDecimal low_price = new BigDecimal(map.get("low_price").toString());
                Long trade_vol = Long.valueOf(map.get("trade_vol").toString());
                long trade_amt = Double.valueOf(map.get("trade_amt").toString()).longValue();
                //5.高、低价格比较
                if (highPrice.compareTo(high_price) == -1) {
                    highPrice = high_price;
                }
                if (lowPrice.compareTo(low_price) == 1) {
                    lowPrice = low_price;
                }

                //6.计算成交量、成交额
                tradeAmtDay+= trade_amt;
                tradeVolDay+= trade_vol;
            }
        }

        //7.计算均价
        BigDecimal avgPrice = new BigDecimal(0);
        if(tradeVolDay != 0){
            avgPrice = new BigDecimal(tradeAmtDay).divide(new BigDecimal(tradeVolDay),2, RoundingMode.HALF_UP);
        }

        Row row = new Row(13);

        row.setField(0,new Timestamp(new Date().getTime()));
        row.setField(1,tradeDate);
        row.setField(2,value.getIndexCode());
        row.setField(3, value.getIndexName());
        row.setField(4,kType);
        row.setField(5,preClosePrice);
        row.setField(6,openPrice);
        row.setField(7,highPrice);
        row.setField(8,lowPrice);
        row.setField(9,closePrice);
        row.setField(10,avgPrice);
        row.setField(11,tradeVolDay);
        row.setField(12,tradeAmtDay);
        return row;
    }
}
