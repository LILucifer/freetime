package com.weixiao.smart.eureka;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-07 07:10.
 */
@SpringBootApplication
@EnableCircuitBreaker
public class SpringHystrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringHystrixApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate initRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }



}
