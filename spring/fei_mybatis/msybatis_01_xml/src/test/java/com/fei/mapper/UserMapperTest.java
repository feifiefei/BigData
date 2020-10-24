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

import static org.junit.Assert.*;

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

    /**
     * 单表查询
     *
     * @throws IOException
     */
    @Test
    public void queryUserByUsernameAndPassword() throws IOException {

        // 5 调用方法
        User user = userMapper.queryUserByUsernameAndPassword("zhangsan", "123456");
        System.out.println(" ============ " + user);

    }

    /**
     * 查询全部
     */
    @Test
    public void queryAll() {
        List<User> users = userMapper.queryAll();
        for (User user : users) {
            System.out.println("=================================");
            System.out.println(user);
        }
    }

    /**
     * 更新
     */
    @Test
    public void updateUser() {
        User user = userMapper.queryUserById(8);
        user.setUsername("laoba");
        user.setName("老八");
        Integer integer = userMapper.updateUser(user);
        System.out.println("=========================返回值" + integer);
    }

    /**
     * 增加
     */
    @Test
    public void insertUser() {
        User user = new User();
        user.setUsername("diaochan");
        user.setPassword("111");
        user.setName("貂蝉");
        user.setBirthday(new Date());
        user.setAddress("上海");
        Integer rows = userMapper.insertUser(user);
        System.out.println("========================" + rows);
    }

    /**
     * 删除
     */
    @Test
    public void deleteUser() {
        Integer rows = userMapper.deleteById(22);
        System.out.println("==========================" + rows);
    }

    @After
    public void releaseResources() {
        // 6 释放资源
        sqlSession.close();
    }
}