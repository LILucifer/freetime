package com.weixiao.smart.ebidding.controller;

import com.weixiao.smart.ebidding.entity.Result;
import com.weixiao.smart.ebidding.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/2/25 15:25
 */
@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {


    @RequestMapping(value = "/testPay", method = RequestMethod.POST)
    public Result testPay(@RequestBody Student student) {
        log.info("student = {}", student.toString());
        if (true) {
            throw new RuntimeException("sss");
        }
        return Result.builder().data(student).build();
    }

    @RequestMapping(value = "/testPay", method = RequestMethod.PATCH)
    public Result testPay2(@RequestBody Student student) {
        log.info("student = {}", student.toString());
        return Result.builder().data(student).build();
    }
}
