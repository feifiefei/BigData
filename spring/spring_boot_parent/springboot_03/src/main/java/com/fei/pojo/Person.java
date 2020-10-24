package com.itheima.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 11:54
 */
@ConfigurationProperties(prefix = "person")
@Component
@Data
public class Person {
    private String username;
    private String password;
    private String sex;
    private String address;

    private String[] hobby;
}
