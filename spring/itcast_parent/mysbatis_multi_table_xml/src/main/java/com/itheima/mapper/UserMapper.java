package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    //需求查询1号用户基本信息和他的拓展信息
    public User findUserWithInfoById(@Param("id") int id);
    //查询1号用户信息及其相关所有订单
    public User findUserAndOrderFormById(@Param("id") int id);
}
