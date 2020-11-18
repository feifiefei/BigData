package cn.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2020/11/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long goodsId;
    private Double goodsPrice;
    private String goodsName;
    private String alias;
    private Long orderTime;
    private Boolean status; //是否超过预警阀值得状态，默认flase
}
