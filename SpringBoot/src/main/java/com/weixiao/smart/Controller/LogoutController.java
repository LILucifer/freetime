package com.weixiao.smart.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-12-06 21:35.
 */
@RestController
public class LogoutController {
    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }
    @RequestMapping("/logoutSuccess")
    public String logoutSuccess(){
        return "logout success";
    }

}
