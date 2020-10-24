package com.itheima.server;

import com.itheima.conf.SpringConf;
import com.itheima.pojo.Orderform;
import com.itheima.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConf.class})
public class TbAccountServiceTest {
    @Autowired
    private TbAccountService tbAccountService;

    @Test
    public void insertCell() {
        User user = new User();
        Orderform orderform = new Orderform();
        user.setUsername("zhangwang");
        user.setPassword("123");
        user.setBirthday(new Date());
        user.setUsername("张忘");
        user.setSex("男");
        user.setAddress("上海");
        orderform.setCreatetime(new Date());
        orderform.setNote("大人，我看行");
        orderform.setNumber("18");
        tbAccountService.insertCell(user, orderform);
    }
}