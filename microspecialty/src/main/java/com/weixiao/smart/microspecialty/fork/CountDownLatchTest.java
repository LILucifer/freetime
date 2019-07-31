package com.weixiao.smart.microspecialty.fork;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-07-25 21:57.
 */
@Slf4j
public class CountDownLatchTest {

    public void countDownLathcTest(){
        final CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                    try {
                        countDownLatch.await();
                        log.info("this countDownLatch was done!!....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    public void smartCountDownLatchTest(){
        final SmartCountDownLatch smartCountDownLatch = new SmartCountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    smartCountDownLatch.countDown();
                    try {
                        smartCountDownLatch.await();
                        log.info("this countDownLatch was done!!....");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    public static void main(String[] args) {
        CountDownLatchTest countDownLatchTest = new CountDownLatchTest();
        //countDownLatchTest.countDownLathcTest();
        countDownLatchTest.smartCountDownLatchTest();

        Semaphore semaphore = new Semaphore(6);

    }

}
