package com.itheima.dataSourse;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

public class DruidTest {
    public  static DataSource getDataSourse() {
        //创建连接池对象
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///spring_db?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    public static void main(String[] args) {
        DataSource dataSourse = getDataSourse();
        System.out.println(dataSourse);
    }
}
