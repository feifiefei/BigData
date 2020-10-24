package com.itheima.server;

import org.springframework.transaction.annotation.Transactional;

public interface TbAccountService {

    public void zhuanzhang(String outname, String inname, Double money);
}
