package com.itheima.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/14 0014 20:31
 */
@Entity //标识这个类是一个实体类，让JPA认为这个pojo对应一个表与之映射
@Table(name = "tb_name")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 提供自动增长 已经添加数据后, 返回新增主键ID值
    private Integer sid;
    @Column(name = "name", length = 20) // 定义表中普通列 : name表中列名
    private String name;
    @Column(name = "sex", length = 5)
    private String sex;
    @Column(name = "age")
    private Integer age;
    @Column(name = "address", length = 20)
    private String address;
}
