package com.itheima.controller;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/15 0015 15:56
 */
@RestController
@RequestMapping("/account")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/pay")
    public String outName(User user) throws Exception {
        String outName = user.getOutName();
        String inName = user.getInName();
        Double money = user.getMoney();
        String result;
        if (userService.transfer(outName, inName, money)) {
            result = "转账成功";
        } else {
            result = "转账失败";
        }
        return result;
    }
}
