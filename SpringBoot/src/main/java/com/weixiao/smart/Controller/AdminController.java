package com.weixiao.smart.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-12-06 21:00.
 */
@RestController()
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping("/add")
    public String add(){
        System.out.println("this is admin's add method");
        return "this is admin's add method";
    }
}
