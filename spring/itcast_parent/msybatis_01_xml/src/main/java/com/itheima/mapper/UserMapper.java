package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    // 根据用户名和密码查询用户信息
    public User queryUserByUsernameAndPassword(@Param("uname") String username,
                                               @Param("upwd") String password);

    //根据id查询
    public User queryUserById(@Param("id") int id);

    //查询全部
    public List<User> queryAll();

    //修改用户
    public Integer updateUser(User user);

    //添加用户
    public Integer insertUser(User user);

    //删除用户
    public Integer deleteById(@Param("id") int id);
}
