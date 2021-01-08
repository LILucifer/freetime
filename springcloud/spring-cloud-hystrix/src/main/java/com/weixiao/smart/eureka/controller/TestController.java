package com.weixiao.smart.eureka.controller;

import com.weixiao.smart.eureka.command.CustomerHystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-09-12 16:27.
 */
@RequestMapping("/hystrix")
@RestController
public class TestController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    public Object testCustomerCommand() {
        return new CustomerHystrixCommand(restTemplate).execute();
    }
}
