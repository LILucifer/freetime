package com.weixiao.smart.service.impl;

import com.alibaba.fastjson.JSON;
import com.weixiao.smart.service.IOrderService;
import com.weixiao.smart.model.OrderMessage;
import com.weixiao.smart.service.amq.MessageProviderService;
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
 * @description (订单Service)
 * @Created 2018-08-28 22:00.
 */
@Service("orderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ICacheService icacheService;
    @Autowired
    private MessageProviderService providerService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 订单分布式锁key
     */
    private static final String LOCK_KEY = "stock_lock_key";


    public ResultMessage createOrder(Map<String, Object> orderMessage) {
        OrderMessage omsge = BeanUtils.copyProperties(OrderMessage.class, orderMessage);
        logger.warn("createOrder 成功创建订单"+ JSON.toJSONString(omsge));
        return null;
    }

    public ResultMessage getOrder(Map<String, Object> objectMap) {
        ResultMessage rmsg = new ResultMessage();
        OrderMessage omsge = BeanUtils.copyProperties(OrderMessage.class, objectMap);
        String key = "goods_stock" + omsge.getCommodityId();//Redis 缓存商品库存的key
        boolean result = checkAndReduceStock(key , omsge.getCount());
        if (result) {//成功获取购买库存数，接下来发送MQ生成订单
            logger.info(omsge.getUserId() + "抢购成功！获取库存数=" + omsge.getCount());
            providerService.send(objectMap);
            rmsg.setResult(ResultMessage.SUCCESS);
            rmsg.setMessage( "抢购成功！获取库存数=" + omsge.getCount());
        }else{//获取购买权限失败，返回失败提示
            logger.info(omsge.getUserId() + "抢购失败");
            rmsg.setResult(ResultMessage.FAIL);
            rmsg.setMessage( "抢购失败！");
        }
        logger.info("create order success");
        return rmsg;
    }

    public boolean initStocks(String commodityId, int count, int caccheSeconds) {
        String key = "goods_stock:" + commodityId;
        try {
            icacheService.set(key,count+"",caccheSeconds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean checkAndReduceStock(String key, int count) {
        String requestId = "GUID";
        ResultMessage resultMessage = icacheService.checkAndReduceStock(key, LOCK_KEY, count, requestId);
        logger.info(resultMessage.getMessage());
        if (resultMessage.getResult() == 1) {
            return true;
        }
        return false;
    }
}
