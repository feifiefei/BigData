package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.rmi.server.RemoteServer;

@SpringBootApplication
public class SpringbootRestTemplate08Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRestTemplate08Application.class, args);
    }

    //将RestTemplate注入spring
    @Bean
    public RestTemplate createRestTemplate(){
        return new RestTemplate();
    }
}
