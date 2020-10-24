package com.itheima;

import com.itheima.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRestTemplate08ApplicationTests {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test01() {
        User[] users = restTemplate.getForObject("http://localhost:8080/user/findAll", User[].class);
        List<User> userList = Arrays.asList(users);
        System.out.println(userList);
    }
    @Test
    public void test02(){
        User user = restTemplate.getForObject("http://localhost:8080/user/findById/2", User.class);
        System.out.println(user);
    }

}
