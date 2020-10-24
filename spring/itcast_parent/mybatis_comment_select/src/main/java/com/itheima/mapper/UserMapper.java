package com.itheima.mapper;

import com.itheima.pojo.Orderform;
import com.itheima.pojo.User;
import com.itheima.pojo.Userinfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    //#################################################
    //单表操作
    @Select("select * from user where id = #{id}")
    User findById(int id);

    @Select("select * from order_form where user_id = #{user_id}")
    @Results({
            @Result(property = "userid", column = "user_id"),
            @Result(property = "createtime", column = "create_time")
    })
    List<Orderform> findOrderFormByUserId(@Param("user_id") int userId);

    //#################################################################
    //一对一
    @Select("select * from user where id = #{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "userinfo", column = "id", one = @One(select = "findUserInfoById")),
            //一对多
            @Result(property = "orderformList", column = "id", many = @Many(select = "findOrderFormByUserIdMore"))
    })
    User findUserById(int id);

    @Select("select * from user_info where id = #{id}")
    Userinfo findUserInfoById(int id);

    //#################################################################
    //一对多

    @Select("select * from order_form where user_id = #{user_id}")
    @Results({
            @Result(property = "userid", column = "user_id"),
            @Result(property = "createtime", column = "create_time")

    })
    List<Orderform> findOrderFormByUserIdMore(@Param("user_id") int userId);

}
