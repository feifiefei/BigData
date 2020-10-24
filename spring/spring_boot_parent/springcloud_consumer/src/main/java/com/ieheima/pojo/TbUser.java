package com.ieheima.pojo;

import lombok.Data;

@Data
public class TbUser {
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别，1男，2女
     */
    private Integer sex;
    /**
     * 出生日期
     */
    private Object birthday;
    /**
     * 创建时间
     */
    private Object created;
    /**
     * 更新时间
     */
    private Object updated;
    /**
     * 备注
     */
    private String note;
}