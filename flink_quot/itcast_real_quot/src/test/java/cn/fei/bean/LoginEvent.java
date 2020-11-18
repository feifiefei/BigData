package cn.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2020/11/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginEvent {

    private String id; //用户id
    private String ip;//用户ip
    private String status;//用户状态
    private int count;//失败次数
}
