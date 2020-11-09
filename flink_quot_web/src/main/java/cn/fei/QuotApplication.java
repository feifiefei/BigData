package cn.fei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Date 2020/11/7
 * 启动类:一定要放置在最外层目录
 */
@SpringBootApplication
public class QuotApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuotApplication.class,args);
    }
}
