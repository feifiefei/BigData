package com.itheima.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 14:50
 */
@ConfigurationProperties(prefix = "hobby")
@Component
@Data
public class Hobby {
    private String[] hobby;
}
