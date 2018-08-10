package com.weixiao.smart.countdownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lishixiang0925@126.com.
 * @description (CountDownLatch practice)
 * @Created 2018-07-14 09:57.
 */
public class MainEnter {


    public static void main(String[] args) {
        Practice practice = new Practice();
        practice.methodPrint();
        new Thread(practice).start();
        try {
            practice.count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static class Practice implements Runnable {
        private CountDownLatch count = new CountDownLatch(5);
        private ExecutorService executorService = Executors.newFixedThreadPool(5);

        private void methodPrint (){
            for (  int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {

                        System.out.println(Thread.currentThread().getName() + "String test ");
                        count.countDown();
                    }
                });
            }

        }

        @Override
        public void run() {
            System.out.println("finished");

        }
    }

}
