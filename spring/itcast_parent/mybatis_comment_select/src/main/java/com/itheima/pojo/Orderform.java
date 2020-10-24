package com.itheima.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Orderform {

    private Integer oid;
    private Integer userid;
    private String number;
    private Date createtime;
    private String note;

}
