package com.weixiao.smart.ifelse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-11-26 23:55.
 */
@Slf4j
@Service("bankPayService")
public class BankPayServiceImpl implements IPayService {
    @Override
    public boolean pay(String orderId) {
        log.info("order {} was payed by {} ",orderId , getPayType());
        return false;
    }

    @Override
    public String getPayType() {
        return "bankPay";
    }

}
