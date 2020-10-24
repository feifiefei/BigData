package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@SpringCloudApplication
public class SpringcloudConsumerFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudConsumerFeignApplication.class, args);
    }
    @LoadBalanced // 开启负载均衡
    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

}
