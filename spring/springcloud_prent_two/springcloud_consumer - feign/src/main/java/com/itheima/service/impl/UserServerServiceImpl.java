package com.itheima.service.impl;

import com.itheima.pojo.TbUser;
import com.itheima.service.UserServerService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/17 0017 11:55
 */
@Service
public class UserServerServiceImpl implements UserServerService {
    @Override
    public TbUser findById(Integer id) {
        TbUser tbUser = new TbUser();
        tbUser.setId(id);
        tbUser.setNote("此为宕机熔断内容");
        return tbUser;
    }
}
