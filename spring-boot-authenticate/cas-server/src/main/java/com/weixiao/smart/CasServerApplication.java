package com.weixiao.smart;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-12-13 08:16.
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CasServerApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {

        SpringApplication.run(CasServerApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CasServerApplication.class);
    }
}
