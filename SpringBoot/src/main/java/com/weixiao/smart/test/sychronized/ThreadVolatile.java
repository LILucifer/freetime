package com.weixiao.smart.test.sychronized;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-18 20:51.
 */
public class ThreadVolatile extends Thread {
    volatile private static int count;

    synchronized public static void test() {

        for (int i=0 ;i<100;i++){
            count++;
        }
        System.out.println("count = "+ count);
    }

    @Override
    public void run() {
        test();
    }
}
