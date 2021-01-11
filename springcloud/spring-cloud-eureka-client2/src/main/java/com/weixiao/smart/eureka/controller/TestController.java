package com.weixiao.smart.eureka.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import java.util.Random;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-09-12 16:27.
 */
@RequestMapping("/clientTest")
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        Random random = new Random();
        int time = random.nextInt(120);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "this is client2 test";
    }
}
