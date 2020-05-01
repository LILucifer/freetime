package com.weixiao.smart.test.sychronized;

import java.util.concurrent.locks.LockSupport;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-04-02 22:08.
 */
public class HashMapDemo {
    public static void main(String[] args) {

        LockSupport.unpark(Thread.currentThread());

        LockSupport.park();

        System.out.println("hello");

        LockSupport.unpark(Thread.currentThread());

        LockSupport.unpark(Thread.currentThread());

        LockSupport.park();

        LockSupport.park();

        System.out.println("hello");

    }
}
