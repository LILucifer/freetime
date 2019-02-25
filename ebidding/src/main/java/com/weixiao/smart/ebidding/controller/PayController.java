package com.gpdi.ebidding.pay.controller;

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


    @RequestMapping(value = "/testPay")
    public String testPay(@RequestParam("userId") String userId){
        log.info("userId = {}",userId);
        return "testPay";
    }
}
