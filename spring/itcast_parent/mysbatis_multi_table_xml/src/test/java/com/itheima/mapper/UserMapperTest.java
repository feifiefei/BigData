package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class UserMapperTest {
    SqlSession sqlSession;
    UserMapper userMapper;

    @Before
    public void buildConnection() throws IOException {
        // 1 加载配置文件
        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
        // 2 sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        // 3 sqlSession
        sqlSession = sqlSessionFactory.openSession(true);
        // 4 获取mapper接口的实现类
        userMapper = sqlSession.getMapper(UserMapper.class);
    }

    @Test
    public void findUserAndUserInfoById() {
        User user = userMapper.findUserAndOrderFormById(1);
        System.out.println("++++++++++++++++");
        System.out.println(user);
    }

    @After
    public void releaseResources() {
        // 6 释放资源
        sqlSession.close();
    }
}