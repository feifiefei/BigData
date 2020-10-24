package com.itheima.controller;


import com.itheima.service.UserServerService;
import com.itheima.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/17 0017 10:14
 */
@RestController
@RequestMapping("/consumer")
public class consumerController {
    @Autowired
    private UserServerService userServer;

    @Value("${server.port}")
    Integer port;

    @GetMapping("/findById/{id}")
    public TbUser findById(@PathVariable("id") Integer id) {
        TbUser user = this.userServer.findById(id);
        user.setNote(user.getNote() + "消费者端口号" + port);
        return user;
    }
}
