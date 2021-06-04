package com.weixiao.samrt.springcloudconfigserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-27 00:05.
 */
@RestController
@RequestMapping("/config")
@Slf4j
public class TestController {

    @Autowired
    private StudentInfo studentInfo;

    @GetMapping("/getStudentInfo")
    public Object getStudentInfo() {
        log.info("student info {}", studentInfo);
        return studentInfo;
    }
}
