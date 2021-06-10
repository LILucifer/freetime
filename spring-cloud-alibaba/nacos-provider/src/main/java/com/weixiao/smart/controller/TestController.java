package com.weixiao.smart.controller;

import com.weixiao.smart.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2021/6/5 12:06
 */
@RestController
public class TestController {
    @Autowired
    private UserInfo userInfo;

    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    public String getUserInfo(){
        return userInfo.toString()+"userId = ";
    }

    @RequestMapping(value = "/echo/{value}", method = RequestMethod.GET)
    public String echo(@PathVariable String value) {
        return "Hello Nacos（service-provider） Discovery " + value;
    }
}
