package com.weixiao.smart.test.sychronized;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-22 20:26.
 */
public class Service {

    synchronized public static void methodA() {
        try {
            System.out.println("methodA start by " + Thread.currentThread().getName()+" at time " + System.currentTimeMillis());
            Thread.sleep(3000);
            System.out.println("methodA end by " + Thread.currentThread().getName()+" at time " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public static void methodB() {
        try {
            System.out.println("methodB start by " + Thread.currentThread().getName() +" at time " + System.currentTimeMillis());
            Thread.sleep(3000);
            System.out.println("methodB end by " + Thread.currentThread().getName()+" at time " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public  void methodC() {
        try {
            System.out.println("methodC start by " + Thread.currentThread().getName()+" at time " + System.currentTimeMillis());
            Thread.sleep(3000);
            System.out.println("methodC end by " + Thread.currentThread().getName()+" at time " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
