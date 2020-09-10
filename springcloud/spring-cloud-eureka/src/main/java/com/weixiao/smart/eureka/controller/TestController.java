package com.weixiao.smart.eureka.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-09-10 07:43.
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "hello word";
    }
}
