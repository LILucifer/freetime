package com.weixiao.smart.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-10 23:25.
 */
@Component
@FeignClient(name = "eureka-client")
public interface EurekaClientService {

    @RequestMapping(value = "/clientTest/test",method = RequestMethod.GET)
    public String indexTest();
}
