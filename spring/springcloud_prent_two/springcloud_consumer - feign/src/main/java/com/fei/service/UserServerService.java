package com.itheima.service;

import com.itheima.conf.FeignLongLevelConfig;
import com.itheima.pojo.TbUser;
import com.itheima.service.impl.UserServerServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/17 0017 11:32
 */
@FeignClient(value = "USER-SERVICE", fallback = UserServerServiceImpl.class, configuration = FeignLongLevelConfig.class)
@Service

public interface UserServerService {
    @RequestMapping("/tbUser/findById/{id}")
    public TbUser findById(@PathVariable("id") Integer id);
}
