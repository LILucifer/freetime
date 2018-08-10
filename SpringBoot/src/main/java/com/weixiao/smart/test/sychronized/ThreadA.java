package com.weixiao.smart.test.sychronized;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-22 20:51.
 */
public class ThreadA extends Thread {
    private Service service;

    public  ThreadA(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.methodA();
    }
}
