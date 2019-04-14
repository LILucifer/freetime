package com.weixiao.smart.container;

import com.weixiao.smart.entity.ProxyIpModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.weixiao.smart.judger.Judgment.judgeStrategy;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-13 20:39.
 */
@Slf4j
public class ContainerUtils {

    public static final String PROXY_IP_URL = "https://www.kuaidaili.com/free/";
    private static  ConcurrentHashMap<String, ProxyIpModel> controllerMap = new ConcurrentHashMap();
    private static  ConcurrentHashMap<String, ProxyIpModel> jsoupResultMap = new ConcurrentHashMap();
    private static  Set<String> failUrl = Collections.synchronizedSet(new LinkedHashSet<>());

    public static void addControllerMap(String key, ProxyIpModel value) {
        log.info("{} proxy ip was successed ", value);
        controllerMap.put(key, value);
    }

    public static ConcurrentHashMap getControllerMap() {
        return controllerMap;
    }

    public static void addJsoupResultMap(String key, ProxyIpModel value) {
        jsoupResultMap.put(key, value);
    }

    public static void removeJsoupResultMap(String key) {
        ProxyIpModel model = jsoupResultMap.get(key);
        log.info("{} proxy ip was useless ,and fail {} times", model,model.getFailCount());
        jsoupResultMap.remove(key);

    }

    public static ConcurrentHashMap getJsoupResultMap() {
        return jsoupResultMap;
    }

    public static ProxyIpModel getControllerMapProxyIpModelByRandom() {
        if (controllerMap.size() > 0) {
            int random = new Random().nextInt(controllerMap.size());
            ProxyIpModel[] proxyIpModels = controllerMap.values().toArray(new ProxyIpModel[controllerMap.size()]);
            //judgeStrategy(proxyIpModels[random]);

            return proxyIpModels[random];
        }
        return null;
    }


    public static void addFailUrl(String url) {
        failUrl.add(url);
    }

    public static Set<String> getFailUrl() {
        return failUrl;
    }

    public static void removeFailUrl(String url) {
        failUrl.remove(url);
    }
    public static void removeAllFailUrl(){
        failUrl.clear();
    }

}
