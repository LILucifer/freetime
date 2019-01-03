package com.weixiao.smart.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-11-28 22:48.
 */
@RestController
@RequestMapping("smart")
public class SmartController {

    @RequestMapping("/add")
    public String add(){
        System.out.println("add method");
        return "this add method";
    }

    @RequestMapping("/login")
    public String login(String userName, String passWord) {
        return "login success";
    }
    public String logout() {
        return "";
    }
}
