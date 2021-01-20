package com.weixiao.smart.feigndefine.service;

import com.weixiao.smart.feigndefine.annotation.FeignClient;
import com.weixiao.smart.feigndefine.annotation.FeignGet;
import org.springframework.stereotype.Component;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-12 06:29.
 */
@Component
//@FeignClient(baseUrl = "http://EUREKA-CLIENT")
@FeignClient(baseUrl = "http://www.baidu.com:80")
public interface FeignDefinedService {

    @FeignGet(url = "/index.html")
    String testMethod();
}
