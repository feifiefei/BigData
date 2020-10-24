package com.itheima.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/15 0015 14:40
 */
@Mapper
@Repository
public interface UserMapper {
    //转出
    @Update("update nwuser set money = money - #{money} where name = #{name}")
    Integer outUser(@Param("name") String name, @Param("money") Double money);

    //转入
    @Update("update nwuser set money = money + #{money} where name = #{name}")
    Integer inUser(@Param("name") String name, @Param("money") Double money);

    //查询该用户是否转账用户是否存在并且余额是否充足
    @Select("SELECT exists(SELECT name from nwuser where name = #{name} and money >= #{money})")
    Boolean existOutUser(@Param("name") String name, @Param("money") Double money);

    @Select("SELECT exists(SELECT name from nwuser where name = #{name})")
    Boolean existInUser(@Param("name") String name);

    @Insert("insert into nwuser values(null,#{name},#{money})")
    Integer createInUser(@Param("name") String name, @Param("money") Double money);
}
