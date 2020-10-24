package com.itheima.server.impl;

import com.itheima.mapper.TbAccountMapper;
import com.itheima.pojo.Orderform;
import com.itheima.pojo.User;
import com.itheima.pojo.Userinfo;
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
    public void insertCell(User user, Orderform orderform) {
        tbAccountMapper.insertUser(user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(), user.getSex(), user.getAddress());
        int userid = tbAccountMapper.selectid();
        tbAccountMapper.insertOrder(userid, orderform.getNumber(), orderform.getCreatetime(), orderform.getNote());
    }
}
