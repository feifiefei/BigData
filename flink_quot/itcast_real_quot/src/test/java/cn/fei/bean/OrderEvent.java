package cn.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2020/11/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {

    private Integer orderId; //用户id
    private String status;//订单状态
    private Long eventTime;
}
