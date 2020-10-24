package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 21:00
 */
@Mapper
@Repository //放入容器（可不加）
public interface UserMapper {
    //查询所有
    @Select("select * from user")
    List<User> queryAll();

    //根据id查询
    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);
}
