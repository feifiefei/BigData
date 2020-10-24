package com.itheima.Dao.impl;

import com.itheima.Dao.AccountDao;

public class AccountFaoImpl implements AccountDao {
    private String bankName;

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public void out(String username, Double money) {
        System.out.println("出账操作:" + bankName + "下：" + username + "出账：" + money);
    }

    @Override
    public void in(String username, Double money) {
        System.out.println("入账操作:" + bankName + "下：" + username + "入账：" + money);
    }
}
