package com.itheima.server.impl;

import com.itheima.mapper.TbAccountMapper;
import com.itheima.server.TbAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbAccountServiceImpl implements TbAccountService {
    @Autowired
    private TbAccountMapper tbAccountMapper;

    @Override
    @Transactional //开启事务
    public void zhuanzhang(String outname, String inname, Double money) {
        tbAccountMapper.outMoney(outname, money);
        //Integer a = 1 / 0;
        tbAccountMapper.inMoney(inname, money);
    }
}
