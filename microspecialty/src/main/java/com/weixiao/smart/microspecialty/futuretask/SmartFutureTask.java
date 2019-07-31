package com.weixiao.smart.microspecialty.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-07-27 00:17.
 */
public class SmartFutureTask<T>  implements Runnable{

    public SmartFutureTask(Callable<T> task) {
        this.callable = task;
    }

    private Callable<T> callable;

    LinkedBlockingDeque<Thread> waiters = new LinkedBlockingDeque<Thread>();

    private T result;
    @Override
    public void run() {

    }

    public T get(){
        return result;
    }
}
