package com.weixiao.smart;

import com.weixiao.smart.readpropeties.CongfigurationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Properties;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-09-24 21:59.
 */
@Slf4j
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
//        SpringApplication application = new SpringApplication(SpringBootApplication.class);
//        application.setWebEnvironment(false);
//        application.run(args);
        //new SpringApplicationBuilder(SpringBootApplication.class).web(WebApplicationType.NONE).run(args);
        Properties properties = CongfigurationUtil.getconfigs();
        String basePathBuilderImpl = properties.getProperty("config.basePathBuilderImpl");
        log.info(basePathBuilderImpl);
    }
}
