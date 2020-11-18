package cn.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Date 2020/11/1
 */
//3.创建bean对象,个股涨跌幅bean
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockIncrBean {

    //字段如下：
    //eventTime、secCode、secName、increase、tradePrice、updown、tradeVol、amplitude、
    //preClosePrice、tradeAmt、tradeTime、source
    private Long eventTime;
    private String secCode;
    private String secName;
    private BigDecimal increase ;//涨跌幅
    private BigDecimal tradePrice ;//最新价
    private BigDecimal updown; //涨跌
    private Long tradeVol;//总手/总成交量
    private BigDecimal amplitude;//振幅
    private BigDecimal preClosePrice;
    private Long tradeAmt;//总成交金额
    private Long tradeTime; //格式化时间
    private String source;
}
