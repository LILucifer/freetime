package com.weixiao.smart;

import com.weixiao.smart.minicode.MiniCodeTest;
import org.springframework.boot.SpringApplication;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-09-24 21:59.
 */
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
        MiniCodeTest test = new MiniCodeTest();
        test.mainExecute();

    }
}
