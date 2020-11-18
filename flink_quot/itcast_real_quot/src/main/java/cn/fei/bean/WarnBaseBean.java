package cn.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Date 2020/11/4
 * 预警业务基类bean
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarnBaseBean {

    /**
     * secCode、preClosePrice、highPrice、lowPrice、closePrice、eventTime
     */
    private String secCode;
    private BigDecimal preClosePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private Long eventTime;

}
