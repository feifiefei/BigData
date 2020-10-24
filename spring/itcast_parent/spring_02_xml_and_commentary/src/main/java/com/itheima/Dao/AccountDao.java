package com.itheima.Dao;

public interface AccountDao {
    public void out(String username,Double money);
    public void in(String username,Double money);
}
