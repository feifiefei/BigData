package com.itheima.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface TbAccountMapper {
    @Insert("insert INTO user VALUES (null, #{username},#{password},#{name},#{birthday},#{sex},#{address})")
    void insertUser(@Param("username") String username, @Param("password") String password, @Param("name") String name, @Param("birthday") Date birthday, @Param("sex") String sex, @Param("address") String address);

    @Select("select max(id) from user")
    int selectid();

    @Insert("insert INTO order_form VALUES (null, #{user_id},#{number},#{create_time},#{note})")
    void insertOrder(@Param("user_id") int userid, @Param("number") String number, @Param("create_time") Date createtime, @Param("note") String note);
}
