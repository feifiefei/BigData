package com.itheima.test;

import com.itheima.conf.SpringConf;
import com.itheima.mapper.TbAccountMapper;
import com.itheima.pojo.TbAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {SpringConf.class})
public class SpringDaoTest {
    @Autowired
    private TbAccountMapper tbAccountMapper;

    @Test
    public void test01() {
        List<TbAccount> accountByAll = tbAccountMapper.findAccountByAll();
        System.out.println(accountByAll);
    }

    @Test
    public void test02() {
        TbAccount accountById = tbAccountMapper.findAccountById(1);
        System.out.println(accountById);
    }
}
