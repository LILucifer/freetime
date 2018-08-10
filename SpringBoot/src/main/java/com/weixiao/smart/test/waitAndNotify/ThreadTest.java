package com.weixiao.smart.test.waitAndNotify;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-21 17:52.
 */
public class ThreadTest extends  Thread {
    private InheritableThreadLocalTest test ;

    public ThreadTest(InheritableThreadLocalTest test) {
        this.test = test;
    }
    @Override
    public void run() {
        for(int i = 0 ; i<5;i++) {
            System.out.println(test.get());
        }
    }
}