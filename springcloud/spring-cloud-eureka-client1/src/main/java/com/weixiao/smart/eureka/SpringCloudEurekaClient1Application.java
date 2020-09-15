package com.weixiao.smart.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lishixiang0925@126.com.
 * @description eureka-client
 * @Created 2020-09-11 08:01.
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
public class SpringCloudEurekaClient1Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaClient1Application.class, args);
    }

}
