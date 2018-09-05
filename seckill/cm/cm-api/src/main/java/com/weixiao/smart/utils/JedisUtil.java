package com.weixiao.smart.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import redis.clients.jedis.JedisCluster;

import java.util.*;

/**
 * @author lishixiang0925@126.com.
 * @description (redis 缓存操作类)
 * @Created 2018-08-11 19:30.
 */
public class JedisUtil {
    public JedisCluster jedisCluster;
    private static JedisUtil jedisUtil;

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public void init() {
        jedisUtil = this;
        jedisUtil.jedisCluster = this.jedisCluster;
    }

    /**
     * 永久缓存
     */
    public static final int FOREVER_TIME = 0;
    /**
     * 长期缓存 24小时
     */
    public static int LONG_TIME = 3600 * 24;

    public static final String LOCK_SUCCESS = "OK";
    public static final String SET_IF_NOT_EXIST = "NX";
    public static final String SET_WITH_EXPIRE_TIME = "PX";
    /**
     * 分布式锁超时时间
     */
    public static final long EXPIRE_TIME =1000;
    public static final Long RELEASE_SUCCESS = 1L;

    /**
     * 基本写入缓存方法
     *
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public static String set(String key, String value, int cacheSeconds) {
        if (cacheSeconds == 0) {
            return jedisUtil.jedisCluster.set(key, value);
        } else {
            return jedisUtil.jedisCluster.setex(key, cacheSeconds, value);
        }
    }

    /**
     * 基本的取值方法
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return jedisUtil.jedisCluster.get(key);
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public void del(String key) {
        jedisUtil.jedisCluster.del(key);
    }

    /**
     * 是否存在指定key的缓存
     *
     * @param key
     * @return
     */
    public static boolean exists(String key) {
        return jedisUtil.jedisCluster.exists(key);
    }

    /**
     * 缓存单个对象
     *
     * @param key
     * @param obj
     * @param cacheSeconds 缓存时效 （永不超时值为0）
     */
    public static void setObj(String key, Object obj, int cacheSeconds) {
        if (cacheSeconds == 0) {
            jedisUtil.jedisCluster.set(key, JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect));
        } else {
            jedisUtil.jedisCluster.setex(key, cacheSeconds, JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect));
        }
    }

    /**
     * 从缓存中获取单个对象（Object or Map<String,Sting>）
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getObj(String key, Class<T> clazz) {
        return JSON.parseObject(jedisUtil.jedisCluster.get(key), clazz);
    }

    /**
     * 获取map对象格式为Map<String,Object>
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getMap(String key, Class<T> clazz) {
        Map<String, String> map = jedisUtil.getObj(key, Map.class);
        Map<String, T> resultMap = new HashMap<String, T>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resultMap.put(entry.getKey(), JSON.parseObject(JSON.toJSONString(entry.getValue()), clazz));
            }
        }
        return resultMap;
    }

    /**
     * 缓存List 数据
     * @param key
     * @param list
     * @param cacheSeconds
     * @param <T>
     */
    public static <T> void setObjList(String key, List<T> list , int cacheSeconds) {
        if(list.size()==0) return;
        for (T t : list) {
            jedisUtil.jedisCluster.rpush(key, JSON.toJSONString(t, SerializerFeature.DisableCircularReferenceDetect));
        }
        if (cacheSeconds != 0) {
            jedisUtil.jedisCluster.expire(key, cacheSeconds);
        }
    }

    /**
     * 获取Redis中缓存的List对象
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getObjList(String key , Class<T> clazz) {
        List<String> list = jedisUtil.jedisCluster.lrange(key, 0, -1);
        List<T> result = new ArrayList<T>();
        if (list.size() > 0) {
            for (String o : list) {
                result.add(JSON.parseObject(o, clazz));
            }
        }
        return result;
    }

    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock( String lockKey, String requestId, long expireTime) {

        String result = jedisUtil.jedisCluster.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedisUtil.jedisCluster.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }



}

