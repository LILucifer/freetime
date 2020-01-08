package com.weixiao.smart.ifelse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lishixiang0925@126.com.
 * @description 支付宝支付
 * @Created 2019-11-26 23:54.
 */
@Slf4j
@Service("ailiPayService")
public class AiliPayServiceImpl implements IPayService {
    @Override
    public boolean pay(String orderId) {
        log.info("order {} was payed by {} ",orderId , getPayType());
        return false;
    }

    @Override
    public String getPayType() {
        return "ailiPay";
    }
}
