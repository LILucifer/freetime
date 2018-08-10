package com.weixiao.smart.countdownLatch.download;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lishixiang0925@126.com.
 * @description (抓取数据主入口)
 * @Created 2018-07-14 10:28.
 */
public class ExecutorDownload {
    /*计数锁存储器*/
    private CountDownLatch countDownLatch  = new CountDownLatch(le);
    /*固定线程池*/
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final static int le = 6;
    private void mainEnter(){
        submitExecutors();
        lastMethod();
    }

    /**
     * 线程池
     */
    public void submitExecutors(){
        for (int i = 0; i < le; i++) {
            final int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000* finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " executor done");
                    countDownLatch.countDown();
                }
            });
        }
    }

    /**
     * 线程池任务执行完成后执行
     */
    public void lastMethod(){
        if (countDownLatch != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!executorService.isShutdown()) {//if the executor hasn't shouted down ,then shwot down
                        executorService.shutdown();
                    }
                    System.out.println("finished");
                }
            }).start();
        }
    }


    public static void main(String[] args) {
        ExecutorDownload ed = new ExecutorDownload();
        ed.mainEnter();
    }
}
