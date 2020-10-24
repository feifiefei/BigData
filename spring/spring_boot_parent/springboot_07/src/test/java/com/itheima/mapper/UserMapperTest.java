package com.itheima.mapper;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 21:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void queryAll() {
        List<User> userList = userMapper.queryAll();
        for (User user : userList) {
            System.out.println(user);

        }
    }

    @Test
    public void findById() {
        User userMapperById = userMapper.findById(1);
        System.out.println(userMapperById);
    }
    @Autowired
    private UserService userService;
    @Test
    public void test03(){
        List<User> userList = userMapper.queryAll();
        System.out.println(userList);
    }
}