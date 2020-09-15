package com.weixiao.smart.eureka.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-09-12 16:27.
 */
@RequestMapping("/clientTest")
@RestController
public class TestController {
    @RequestMapping("/test")
    public String test() {

        return "this is client2 test";
    }
}
