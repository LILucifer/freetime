package com.weixiao.smart.service;


import com.weixiao.smart.utils.ResultMessage;

import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description (订单接口)
 * @Created 2018-08-28 21:59.
 */
public interface IOrderService {

    /**
     *
     * @param orderMessage   OrderMessage convert to map
     * @return
     */
    public ResultMessage createOrder(Map<String , Object> orderMessage);

    /**
     * 初始化缓存商品库存数
     */
    public boolean initStocks(String commodityId ,int count , int caccheSeconds);
}
