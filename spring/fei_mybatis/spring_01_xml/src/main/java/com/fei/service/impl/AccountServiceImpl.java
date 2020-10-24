package com.itheima.service.impl;

import com.itheima.Dao.AccountDao;
import com.itheima.service.AccountService;

public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String outUserName, String inUserName, Double Money) {
        accountDao.out(outUserName, Money);
        accountDao.in(inUserName, Money);
    }
}
