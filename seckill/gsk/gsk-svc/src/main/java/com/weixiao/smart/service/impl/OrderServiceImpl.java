package com.weixiao.smart.service.impl;

import com.weixiao.smart.service.IOrderService;
import com.weixiao.smart.model.OrderMessage;
import com.weixiao.smart.service.cache.ICacheService;
import com.weixiao.smart.utils.BeanUtils;
import com.weixiao.smart.utils.ResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-08-28 22:00.
 */
@Service("orderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ICacheService icacheService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public ResultMessage createOrder(Map<String, Object> orderMessage) {
        OrderMessage omsge = BeanUtils.copyProperties(OrderMessage.class, orderMessage);
        String key = "goods_stock" + omsge.getCommodityId();//Redis 缓存商品库存的key
        boolean result = checkAndReduceStock(key , omsge.getCount());
        if (result) {//成功获取购买库存数，接下来发送MQ生成订单

        }else{//获取购买权限失败，返回失败提示

        }
        logger.info("create order success");
        return null;
    }

    public boolean initStocks(String commodityId, int count, int caccheSeconds) {

        return false;
    }

    public boolean checkAndReduceStock(String key, int coun) {

        return false;
    }
}
