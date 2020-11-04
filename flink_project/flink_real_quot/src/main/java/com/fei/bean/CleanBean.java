package com.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Date 2020/10/31
 * 数据清洗bean对象，接收avro对象数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CleanBean {

    private String mdStreamId;
    private String secCode;
    private String secName;
    private Long tradeVolumn;
    private Long tradeAmt;
    private BigDecimal preClosePrice;
    private BigDecimal openPrice;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private BigDecimal tradePrice;
    private Long eventTime;
    private String source;
}
