package com.itheima.service.impl;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/14 0014 10:16
 */
@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }
}
