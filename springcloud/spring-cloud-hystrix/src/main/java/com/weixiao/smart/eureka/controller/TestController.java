package com.weixiao.smart.eureka.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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




    @HystrixCommand(fallbackMethod = "testFallAbckMethod",
            commandKey = "spring-cloud-hystrix-command-annotation",
            groupKey = "spring-cloud-Hystrix-command-group-annotation",
            threadPoolKey = "spring-cloud-hystrix-thread-pool-annotation",
            threadPoolProperties = {
                    @HystrixProperty(name="coreSize",value = "2"),
                    @HystrixProperty(name="queueSizeRejectionThreshold",value = "5")
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100"),
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="5000")
            }
    )
    @GetMapping("/test2")
    public Object  testHystrixsByAnnotation(){
        return  restTemplate.getForEntity("http://EUREKA-CLIENT/clientTest/test", String.class);
    }


    private String testFallAbckMethod() {

        return "服务器熔断了 by annotation ";

    }

}
