package com.itheima.mapper;

import com.itheima.pojo.TbAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbAccountMapper {

    List<TbAccount> findAccountByAll();

    TbAccount findAccountById(int id);

    void outMoney(@Param("name") String name, @Param("money") Double money);

    void inMoney(@Param("name") String name, @Param("money") Double money);

}
