package com.itheima;

import com.itheima.mapper.UserMapper;
import com.itheima.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootWookApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        userMapper.outUser("张三", 5000D);
    }

    @Test
    void transfer() throws Exception {
        userService.transfer("张三", "李四", 5000D);
    }

}
