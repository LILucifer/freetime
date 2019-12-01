package com.weixiao.smart.ifelse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lishixiang0925@126.com.
 * @description 微信支付
 * @Created 2019-11-25 19:47.
 */
@Slf4j
@Service("weChatPayService")
public class WeChatPayServiceImpl implements IPayService {
    @Override
    public boolean pay(String orderId) {
        log.info("order {} was payed by {} ",orderId , getPayType());
        return false;
    }

    @Override
    public String getPayType() {
        return "WeChat";
    }
}
