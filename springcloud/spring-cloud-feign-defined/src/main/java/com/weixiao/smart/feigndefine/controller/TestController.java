package com.weixiao.smart.feigndefine.controller;

import com.weixiao.smart.feigndefine.service.FeignDefinedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-13 20:54.
 */
@RestController
@RequestMapping("")
public class TestController {

    @Autowired
    FeignDefinedService feignDefinedService;

    @GetMapping("")
    public Object getIndex(){
        return feignDefinedService.testMethod();
    }
}
