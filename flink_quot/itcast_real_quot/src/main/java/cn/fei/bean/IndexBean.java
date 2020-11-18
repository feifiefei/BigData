package cn.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Date 2020/11/1
 * 指数行情bean对象
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IndexBean {

    /**
     * 事件时间
     */
    private Long eventTime;
    /**
     * 指数代码
     */
    private String indexCode;
    /**
     * 指数名称
     */
    private String indexName;
    /**
     * 昨收盘价
     */
    private BigDecimal preClosePrice;
    /**
     * 开盘价
     */
    private BigDecimal openPrice;
    /**
     * 当日最高价
     */
    private BigDecimal highPrice;
    /**
     * 当日最低价
     */
    private BigDecimal lowPrice;
    /**
     * 当前价
     */
    private BigDecimal closePrice;
    /**
     * 当前成交量(分时)
     */
    private Long tradeVol;
    /**
     * 当前成交额（分时）
     */
    private Long tradeAmt;
    /**
     * 日成交量
     */
    private Long tradeVolDay;
    /**
     * 日成交额
     */
    private Long tradeAmtDay;
    /**
     * 交易时间（格式化时间）
     */
    private Long tradeTime;
    /**
     * 数据来源
     */
    private String source;

}
