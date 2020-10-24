package com.itheima.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 18:19
 */
@RestController //标识返回字符串类型
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello spring boot";
    }
}
