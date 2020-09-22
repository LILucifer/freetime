package com.weixiao.smart.eureka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-09-12 16:27.
 */
@RequestMapping("/consumerRibbon")
@RestController
public class TestRibbonController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/test")
    public Object test() {
        ServiceInstance instance = loadBalancerClient.choose("consumer-server");
        String ip = instance.getHost();
        int port = instance.getPort();
        return restTemplate.getForEntity("http://"+ip+":"+port+"/clientTest/test", String.class);
    }
}
