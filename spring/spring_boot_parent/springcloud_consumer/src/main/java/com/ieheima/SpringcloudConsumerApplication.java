package com.ieheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringcloudConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudConsumerApplication.class, args);
    }

    @LoadBalanced //开启负载均衡
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
