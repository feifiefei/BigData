package cn.fei.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 板块行情Bean对象
 */
@Data
public class SectorBean {

    private Long eventTime;
    private String sectorCode;
    private String sectorName;
    private BigDecimal preClosePrice;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private Long tradeVol; //分时成交量
    private Long tradeAmt; //分时成交金额
    private Long tradeVolDay; //总成交量
    private Long tradeAmtDay; //总成交金额
    private Long tradeTime;//格式化之后的事件时间，拼接rowkey使用
}