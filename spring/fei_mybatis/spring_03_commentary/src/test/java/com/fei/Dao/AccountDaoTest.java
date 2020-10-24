package com.itheima.Dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
//指定单元测试环境
//指定配置文件位置
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class AccountDaoTest {
    //注入数据层对象
    @Autowired
    private AccountDao accountDao;

    @Test
    public void out() {
        accountDao.out("张三", 2000D);
    }

    @Test
    public void in() {
        accountDao.in("王五", 2000D);
    }
}