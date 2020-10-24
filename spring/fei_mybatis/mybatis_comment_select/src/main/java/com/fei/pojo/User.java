package com.itheima.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class User {

    private Integer id;
    private String username;
    private String password;
    private String name;
    private Date birthday;
    private String sex;
    private String address;
    private Userinfo userinfo;
    private List<Orderform> orderformList;
}
