package com.itheima;

import com.itheima.pojo.Student;
import com.itheima.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootDatajpa10ApplicationTests {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void contextLoads() {
        Student student = new Student();
        student.setName("墨鸦");
        student.setAge(28);
        student.setSex("男");
        student.setAddress("白鸟");
        Student stu = studentRepository.save(student);
        System.out.println(stu);
    }

    @Test
    public void test02() {
        Sort sort = new Sort(Sort.Direction.DESC, "sid");
        List<Student> students = studentRepository.findAll(sort);
        System.out.println(students);
    }

    @Test
    public void test04() {

        List<Student> nameAndSex = studentRepository.findByNameAndSex("墨鸦", "男");
        System.out.println(nameAndSex);

    }

}
