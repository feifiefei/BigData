package com.itheima.service;

import com.itheima.Dao.AccountDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AccountServiceTest {
    @Autowired
    AccountService accountService;

    @Test
    public void transfer() {
        accountService.transfer("张三", "李四", 50000D);
    }
}