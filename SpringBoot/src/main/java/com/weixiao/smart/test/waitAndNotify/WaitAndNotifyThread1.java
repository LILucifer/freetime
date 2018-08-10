package com.weixiao.smart.test.waitAndNotify;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-19 22:09.
 */
public class WaitAndNotifyThread1  extends  Thread{

    Object lock ;

    public Object getLock() {
        return lock;
    }

    public void setLock(Object lock) {
        this.lock = lock;
    }

    public WaitAndNotifyThread1(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {

        synchronized (lock) {
            try {
                System.out.println("thread1 start" + Thread.currentThread().getName()+"   "+ System.currentTimeMillis());
                lock.wait();
                System.out.println("thread1 stop" + Thread.currentThread().getName() +"   "+ System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
