package com.itheima.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 11:02
 */
@RestController
public class HellowController {
    @GetMapping("/hello")
    public String hello(){
        return  "Hellow boy";
    }
}
