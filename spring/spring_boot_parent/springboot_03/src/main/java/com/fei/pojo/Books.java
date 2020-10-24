package com.itheima.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 14:32
 */

@ConfigurationProperties(prefix = "books")
@Component
@Data
public class Books {
    private Map<String, String> books;
}
