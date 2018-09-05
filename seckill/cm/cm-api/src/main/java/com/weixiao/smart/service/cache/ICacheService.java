package com.weixiao.smart.service.cache;

import com.weixiao.smart.utils.ResultMessage;

import java.util.List;
import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description (redis缓存服务类)
 * @Created 2018-08-12 20:09.
 */
public interface ICacheService {

    /**
     * 添加缓存
     * @param key
     * @param value
     * @param cacheSeconds
     */
    public void set(String key, String value, int cacheSeconds);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public String get(String key);

    /**
     * 缓存对象
     * @param key
     * @param obj
     * @param cacheSeconds
     */
    public void setObj(String key, Object obj, int cacheSeconds);

    /**
     * 获取对象缓存
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getObj(String key, Class<T> clazz);

    /**
     * 获取Map<String , Object> 缓存
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getMap(String key, Class<T> clazz);

    /**
     * 缓存列表
     * @param key
     * @param list
     * @param cacheSeconds
     * @param <T>
     */
    public <T> void setObjList(String key, List<T> list, int cacheSeconds);

    /**
     * 获取列表缓存
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getObjList(String key, Class<T> clazz);

    /**
     *
     * @param key
     * @param count
     */
    public ResultMessage checkAndReduceStock(String key , int count);

}
