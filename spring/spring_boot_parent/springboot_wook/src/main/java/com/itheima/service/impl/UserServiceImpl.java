package com.itheima.service.impl;

import com.itheima.mapper.UserMapper;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/15 0015 14:59
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional
    public boolean transfer(String outName, String inName, Double money) throws Exception {
        if (userMapper.existOutUser(outName, money)) {
            if (userMapper.existInUser(inName)) {
                userMapper.outUser(outName, money);
                userMapper.inUser(inName, money);
                return true;
            } else {
                new Exception();
                return false;
            }
        } else {
            new Exception();
            return false;
        }


    }
}
