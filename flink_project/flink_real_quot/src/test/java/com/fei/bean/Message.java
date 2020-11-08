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
public class Message {

    private String userId;
    private String msg;
    private Long eventTime;
}
