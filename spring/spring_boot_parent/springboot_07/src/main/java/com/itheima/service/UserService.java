package com.itheima.service;

import com.itheima.pojo.User;

import java.util.List;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/14 0014 10:14
 */
public interface UserService {
    public List<User> queryAll();
    public User findById(Integer id);

}
