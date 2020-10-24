package com.itheima.controller;

import com.itheima.entity.TbUser;
import com.itheima.service.TbUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户信息表(TbUser)表控制层
 *
 * @author Fei
 * @since 2020-10-16 11:36:18
 */
@RestController
@RequestMapping("tbUser")
public class TbUserController {
    /**
     * 服务对象
     */
    @Resource
    private TbUserService tbUserService;

    @Value("${server.port}")
    private Integer port;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/findById/{id}")
    public TbUser findById(@PathVariable("id") Integer id) {
        TbUser tbUser = this.tbUserService.queryById(id);
        tbUser.setNote("服务提供者的端口号: " + port);
        return tbUser;
    }
}