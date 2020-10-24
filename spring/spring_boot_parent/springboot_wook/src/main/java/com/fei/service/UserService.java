package com.itheima.service;


/**
 * @description:
 * @author: 飞
 * @date: 2020/10/15 0015 14:56
 */

public interface UserService {
    //定义转账
    boolean transfer(String outName, String Inname, Double money) throws Exception;
}
