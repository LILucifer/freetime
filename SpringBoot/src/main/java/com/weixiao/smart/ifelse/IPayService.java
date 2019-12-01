package com.weixiao.smart.ifelse;

/**
 * @author lishixiang0925@126.com.
 * @description 订单支付
 * @Created 2019-11-25 07:48.
 */
public interface IPayService {


    boolean pay(String orderId);

    String getPayType();

}
