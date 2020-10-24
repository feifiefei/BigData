package com.itheima.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TbAccountServiceTest {
    @Autowired
    private TbAccountService tbAccountService;


    @Test
    public void zhuanzhang() {
        tbAccountService.zhuanzhang("aaa", "bbb", 500D);
    }
}