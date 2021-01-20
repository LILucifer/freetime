package com.weixiao.smart.feigndefine.failback;

import com.weixiao.smart.feigndefine.service.FeignDefinedService;
import org.springframework.stereotype.Component;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-13 21:15.
 */
@Component
public class FeignFailBack implements FeignDefinedService {
    @Override
    public String testMethod() {
        return "服务降级了";
    }
}
