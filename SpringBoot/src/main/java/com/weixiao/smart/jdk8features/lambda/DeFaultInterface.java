package com.weixiao.smart.jdk8features.lambda;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-06 09:36.
 */
public interface DeFaultInterface {
    void printUserId();
    default void getUserName() {
        System.out.println("this is default method of  DefaultInterface  = getUserName");
    }
    default void getUserNo(){
        System.out.println("this is default method of  DefaultInterface  = getUserNo");
    }
}
