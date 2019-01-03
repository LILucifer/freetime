package com.weixiao.smart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-12-31 22:27.
 */
@RestController
@RequestMapping("/test")
public class ControllerTest {

    @RequestMapping("/temp")
    public String testString(){
        return "testString";
    }
}
