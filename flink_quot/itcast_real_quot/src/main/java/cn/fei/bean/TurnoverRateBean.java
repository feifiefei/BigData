package cn.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Date 2020/11/6
 * 封装换手率
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoverRateBean {

    //secCode、secName、tradePrice、tradeVol、negoCap
    private String secCode;
    private String secName;
    private BigDecimal tradePrice;
    private Long tradeVol;
    private BigDecimal negoCap;
}


