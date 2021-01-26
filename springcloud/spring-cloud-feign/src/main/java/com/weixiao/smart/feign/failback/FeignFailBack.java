package com.weixiao.smart.feign.failback;

import com.weixiao.smart.feign.service.EurekaClientService;
import org.springframework.stereotype.Component;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-13 21:22.
 */
@Component
public class FeignFailBack implements EurekaClientService {
    @Override
    public String indexTest() {
        return "服务降级了";
    }
}
