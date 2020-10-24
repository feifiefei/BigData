package com.ieheima.controller;

import com.ieheima.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/16 0016 10:52
 */

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/findById/{id}")
    public TbUser findById(@PathVariable("id") String id) {
        //方案一：原生
        //  String url = "http://localhost:9091/tbUser/findById/" + id;
        //return restTemplate.getForObject(url, TbUser.class);
        //方案二
        //List<ServiceInstance> instances = discoveryClient.getInstances("USER-SERVICE");
        //ServiceInstance serviceInstance = instances.get(0);
        //String uri = serviceInstance.getUri() + "/tbUser/findById/" + id;
        //return restTemplate.getForObject(uri, TbUser.class);
        //方案三
        String url = "http://USER-SERVICE/tbUser/findById/" + id;
        return restTemplate.getForObject(url, TbUser.class);

    }

    @GetMapping("/instance")
    public List<ServiceInstance> getDiscovery() {
        List<ServiceInstance> instances = discoveryClient.getInstances("USER-SERVICE");
        List<String> services = discoveryClient.getServices();
        System.out.println(services + "=======================");
        return instances;
    }
}
