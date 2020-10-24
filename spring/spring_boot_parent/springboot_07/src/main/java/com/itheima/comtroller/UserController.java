package com.itheima.comtroller;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/14 0014 10:19
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/findAll")
    public List<User> findAll() {
        return userService.queryAll();
    }
    @GetMapping("/findById/{id}")
    public User findById(@PathVariable("id") Integer id) {
        return userService.findById(id);
    }
}
