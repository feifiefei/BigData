package com.itheima.repository;

import com.itheima.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/14 0014 20:34
 */
public interface StudentRepository extends JpaRepository<Student, Integer> {
    //根据姓名和性别查询用户信息
    public List<Student> findByNameAndSex(String name, String sex);

}
