package com.weixiao.smart.eureka.command;

import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-07 07:30.
 */
public class CustomerHystrixCommand extends HystrixCommand<Object> {

    private RestTemplate restTemplate;
    public CustomerHystrixCommand(RestTemplate restTemplate) {

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("spring-cloud-Hystrix-command-group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("spring-cloud-hystrix-command"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("spring-cloud-hystrix-thread-pool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(100) //设置超时时间限制--超时则执行容错策略
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)
                )
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(2).withMaxQueueSize(5)));
        this.restTemplate = restTemplate;
    }

    @Override
    protected Object run() throws Exception {
        return restTemplate.getForEntity("http://EUREKA-CLIENT/clientTest/test", String.class);
    }

    @Override
    protected Object getFallback() {
        //核心方法，降级之后会来执行到此
        return "服务降级了";
    }
}

