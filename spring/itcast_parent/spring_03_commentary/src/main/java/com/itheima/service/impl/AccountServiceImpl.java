package com.itheima.service.impl;

import com.itheima.Dao.AccountDao;
import com.itheima.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service("accountSeevice")
public class AccountServiceImpl implements AccountService {
    @Autowired
    @Qualifier("accountDao")
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
