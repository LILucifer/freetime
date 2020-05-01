package com.weixiao.smart.microspecialty.lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lishixiang0925@126.com.
 * @description ReentrantLock
 * @Created 2019-07-22 23:18.
 */
public class SmartReentrantLock implements Lock {

    //等待队列
    private LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<Thread>();
    //拥有当前锁的线程
    private AtomicReference<Thread> owner = new AtomicReference<Thread>();
    //重入次数
    private AtomicInteger count = new AtomicInteger(0);
    @Override
    public void lock() {
        //尝试获取锁
        if (!tryLock()) {
            //如果失败。进入等待队列
            waiters.offer(Thread.currentThread());
            //自旋  不断尝试获取锁
            for (;;) {
                Thread thread = waiters.peek();
                if (thread == Thread.currentThread()) {
                    //判断是否当前线程，如果是再次尝试获取锁
                    if (!tryLock()) {
                        //获取锁失败，线程挂起
                        LockSupport.park();
                        return;
                    } else {
                        //抢锁成功-->出队列
                        waiters.poll();
                        return;
                    }
                }else{
                    //如果不是则挂起线程
                    LockSupport.park();
                    return;
                }
            }

        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        int value = count.get();
        //1、判断count是否为0 ，不为0则锁被占用-->判断是否重入锁
        if (value != 0) {
            //判断占用锁的是否为当前线程，是则count+1
            if (owner.get() == Thread.currentThread()) {
                count.compareAndSet(value,value+1);
                return true;
            }else{
                //若不是当前线程占用锁，互斥，抢锁失败，则返回false
                return false;
            }
        } else {
            //count ==0 当前锁未被占用， CAS 修改count+1
            if (count.compareAndSet(value, value + 1)) {
                //抢锁成功
                owner.set(Thread.currentThread());
                return true;
            }else {
                //CAS操作失败 ，抢锁失败
                return false;
            }

        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        //尝试解锁
        if (tryUnlock()) {
            //解锁成功 唤醒等待队列的线程抢占锁
            Thread th = waiters.peek();
            if (th != null) {
                LockSupport.unpark(th);
            }
        }

    }

    public boolean tryUnlock() {
        //判断是否当前线程占有所，不是则抛出异常
        if (owner.get() != Thread.currentThread()) {
            throw new IllegalMonitorStateException();
        }else{
            int value = count.get();
            //如果是，就将count-1  若count变为0 ，则解锁成功
            if (count.compareAndSet(value, value - 1)) {
                if (count.get() == 0) {
                    //将当前占有锁线程  owner =null
                    owner.compareAndSet(Thread.currentThread(), null);
                }
                return true;
            }else {
                return false;
            }
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}

