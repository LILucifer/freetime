package com.weixiao.smart.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author lishixiang0925@126.com.
 * @description eureka-client
 * @Created 2020-09-11 08:01.
 */

//@SpringBootApplication
//@RibbonClients(
//        @RibbonClient(value = "consumer-server")
//)
public class SpringCloudRibbonConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudRibbonConsumerApplication.class, args);
    }


    @Bean
    public RestTemplate initRestTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}
