package com.weixiao.smart.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lishixiang0925@126.com.
 * @description (程序启动入口)
 * @Created 2018-09-01 15:21.
 */
public class Gskstart {
    private static Logger logger = LoggerFactory.getLogger(Gskstart.class);
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-context.xml");
        context.start();
        logger.info("gsk-svc was started success");
    }
}
