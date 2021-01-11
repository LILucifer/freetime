package com.weixiao.smart.feign.controller;

import com.weixiao.smart.feign.service.EurekaClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-10 23:23.
 */
@RestController
@RequestMapping("/feign")
public class TestController {


    @Autowired
    private EurekaClientService eurekaClientService;


    @GetMapping("/index")
    public Object index() {

        return eurekaClientService.indexTest();

    }
}
