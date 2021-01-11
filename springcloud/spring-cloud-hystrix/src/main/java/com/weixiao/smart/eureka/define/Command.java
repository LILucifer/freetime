package com.weixiao.smart.eureka.define;

/**
 * @author lishixiang0925@126.com.
 * @description 自定义hystrix执行接口
 * @Created 2021-01-10 00:02.
 */
public interface Command<T> {
    T run();

    T fallBack();

}
