package com.weixiao.smart.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author lishixiang0925@126.com.
 * @description eureka-client
 * @Created 2020-09-11 08:01.
 */

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableEurekaClient
public class SpringCloudEurekaConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaConsumerApplication.class, args);
    }


    @Bean
    @LoadBalanced
    public RestTemplate initRestTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}
