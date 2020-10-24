package com.itheima.mapper;

import com.itheima.pojo.TbAccount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TbAccountMapper {
    @Select("select * from tb_account")
    List<TbAccount> findAccountByAll();

    @Select("select * from tb_account where id = #{id}")
    TbAccount findAccountById(int id);

    @Update("update tb_account set money = money - #{money} where name = #{name}")
    void outMoney(@Param("name") String name, @Param("money") Double money);

    @Update("update tb_account set money = money + #{money} where name = #{name}")
    void inMoney(@Param("name") String name, @Param("money") Double money);

}
