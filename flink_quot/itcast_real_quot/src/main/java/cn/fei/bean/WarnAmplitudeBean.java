package cn.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Date 2020/11/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarnAmplitudeBean {

    //secCode、preClosePrice、highPrice、lowPrice
    private String secCode;
    private BigDecimal preClosePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
}
