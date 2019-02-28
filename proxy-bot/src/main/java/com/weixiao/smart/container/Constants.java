package com.weixiao.smart.container;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-18 10:43.
 */
public class Constants {
    /**
     * 访问百度首页 校验proxy ip 是否可用
     */
    public static final String JUDGER_URL = "https://www.baidu.com/";
    public static final String USER_AGENT = "Mozilla/5.2 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";
    /**
     * connect timeout
     */
    public static final int TIME_OUT = 6000;


    /**
     * 校验频率
     */
    public static final int FIXED_RATE = 3 * 1000;
    /**
     * 校验次数
     */
    public static final int FIRST_CHECK_TIME = 5;
    /**
     * 再次校验次数
     */
    public static final int AGAIN_CHECK_TIME = 9;

    /**
     * controllerMap min size
     */
    public static final int CONTROLLER_MAP_MIN_SIZE = 50;

    /**
     * 重新抓取failUrl内容的容错次数
     */
    public static final int Fault_tolerance_COUNT = 3;


    /**
     * fixedThreadPool size
     */
    public static final int VERIFY_THREAD_POOL_SIZE = 20;
    public static final ExecutorService executorService = Executors.newFixedThreadPool(VERIFY_THREAD_POOL_SIZE);



}
