package com.weixiao.smart.threadlocal;

import com.sun.jmx.snmp.tasks.ThreadService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lishixiang0925@126.com.
 * @description 守护线程测试类
 * @Created 2020-04-06 11:19.
 */
public class DaemonThread {

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    ExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(), "DaemonRunner");
        thread.setDaemon(true);
        thread.start();
        try {
            System.out.println("thread run");
            Thread.sleep(3000);
            System.out.println("thread end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    static  class DaemonRunner implements Runnable {
        @Override
        public void run() {
            System.out.println("Daemon thread was running");
        }
    }
}
