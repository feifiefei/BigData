package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 10:25
 */
//此注解用于标识当前类为springboot的启动类（引导类）
@SpringBootApplication
public class SpingBoot01Application {
    public static void main(String[] args) {
        SpringApplication.run(SpingBoot01Application.class, args);
    }
}
