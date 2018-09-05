package com.weixiao.smart.service.impl;

import com.weixiao.smart.service.cache.ICacheService;
import com.weixiao.smart.utils.JedisUtil;
import com.weixiao.smart.utils.ResultMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description (缓存服务service实现)
 * @Created 2018-08-12 21:32.
 */
@Service("icacheService")
public class ICacheServiceImpl implements ICacheService {
    public void set(String key, String value, int cacheSeconds) {
        JedisUtil.set(key, value, cacheSeconds);
    }

    public String get(String key) {
        return JedisUtil.get(key);
    }

    public void setObj(String key, Object obj, int cacheSeconds) {
        JedisUtil.setObj(key,obj,cacheSeconds);
    }

    public <T> T getObj(String key, Class<T> clazz) {
        return JedisUtil.getObj(key,clazz);
    }

    public <T> Map<String, T> getMap(String key, Class<T> clazz) {
        return JedisUtil.getMap(key,clazz);
    }

    public <T> void setObjList(String key, List<T> list, int cacheSeconds) {
        JedisUtil.setObjList(key,list,cacheSeconds);
    }

    public <T> List<T> getObjList(String key, Class<T> clazz) {
        return getObjList(key,clazz);
    }

    public ResultMessage checkAndReduceStock(String key , String lockKey,  int count, String requestId)  {
        ResultMessage resultMessage = new ResultMessage();
        String message = "";
        int result = ResultMessage.FAIL;
        while (true) {
            boolean lock = JedisUtil.tryGetDistributedLock(lockKey, requestId, JedisUtil.EXPIRE_TIME);//获取分布式锁
            if (lock) {
                int stock = Integer.parseInt(JedisUtil.get(key));
                if (stock > 0) {
                    if ((stock - count) < 0) {//检查库存是否足够
                        message = "库存不足";
                        JedisUtil.releaseDistributedLock(lockKey, requestId);//释放锁
                        break;
                    }else{
                        try {
                            set(key, String.valueOf(stock - count),JedisUtil.LONG_TIME);
                            JedisUtil.releaseDistributedLock(lockKey, requestId);//释放锁
                            message = "成功递减库存数，成功获取订单";
                            result = ResultMessage.SUCCESS;
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            JedisUtil.releaseDistributedLock(lockKey, requestId);//释放锁
                            message = "获取订单失败";
                            break;
                        }
                    }
                }else{
                    message = "库存不足";
                    JedisUtil.releaseDistributedLock(lockKey, requestId);//释放锁
                    break;
                }
            }
        }
        resultMessage.setResult(result);
        resultMessage.setMessage(message);
        return resultMessage;
    }
}
