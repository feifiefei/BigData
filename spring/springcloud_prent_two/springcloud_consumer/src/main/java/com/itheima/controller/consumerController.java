package com.itheima.controller;

import com.itheima.pojo.TbUser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/17 0017 10:14
 */
@RestController
@RequestMapping("/consumer")
public class consumerController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/findById/{id}")
    @HystrixCommand(fallbackMethod = "error")
    public TbUser findById(@PathVariable("id") Integer id) {
        String url = "http://USER-SERVICE/tbUser/findById/" + id;
        return restTemplate.getForObject(url, TbUser.class);

    }

    public TbUser error(Integer id) {
        TbUser tbUser = new TbUser();
        tbUser.setId(id);
        tbUser.setNote(id + "出错了");
        return tbUser;
    }

}
