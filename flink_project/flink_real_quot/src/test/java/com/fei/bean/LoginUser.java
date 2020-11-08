package com.fei.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2020/11/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    private Integer userId;
    private String ip;
    private String status;
    private Long eventTime;

}
