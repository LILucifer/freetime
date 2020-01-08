package com.weixiao.smart.ifelse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lishixiang0925@126.com.
 * @description 订单支付
 * @Created 2019-11-25 07:51.
 */
@Slf4j
@Service("payService")
public class PayService {

    final Map<String, IPayService> payServiceMap = new ConcurrentHashMap<>();
    public PayService(List<IPayService> payServices) {
        for (IPayService payService : payServices) {
            payServiceMap.put(payService.getPayType(), payService);
        }
    }
    public void pay(String orderId, String orderType) {
        //完全放弃if else 的写法
        IPayService payService = payServiceMap.get(orderType);
        if (payService != null) {
            payService.pay(orderId);
        } else {
            log.info("不合法支付类型:{}", orderType);
        }
    }
}
