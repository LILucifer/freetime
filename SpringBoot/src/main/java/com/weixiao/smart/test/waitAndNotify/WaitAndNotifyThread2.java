package com.weixiao.smart.test.waitAndNotify;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-19 22:16.
 */
public class WaitAndNotifyThread2 extends Thread {
    Object lock ;

    public Object getLock() {
        return lock;
    }

    public void setLock(Object lock) {
        this.lock = lock;
    }

    public WaitAndNotifyThread2(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {

        synchronized (lock) {
            try {
                System.out.println("thread2 start" + Thread.currentThread().getName()+"   "+ System.currentTimeMillis());
                lock.notify();
                System.out.println("thread2 stop"+ Thread.currentThread().getName()+"   "+ System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
