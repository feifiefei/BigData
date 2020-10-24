package com.itheima.controller;

import com.itheima.pojo.Books;
import com.itheima.pojo.Hobby;
import com.itheima.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 11:29
 */
@RestController
public class HellowController {
    @Value("${name}")
    private String name;
    @Value("${address}")
    private String address;
    @Autowired
    private Person person;
    @Autowired
    private Hobby hobby;
    @Autowired
    private Books books;

    @GetMapping("/hello")
    public String hello() {
       return "helow zzz????" + name + address + person + hobby + books;
    }
}
