package com.weixiao.smart.microspecialty.fork;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.LockSupport;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-07-25 23:27.
 */
@Slf4j
public class CyclicBarrierTest {

    public void testCyclicBarrier(){
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("达到 4 mission！-----");
            }
        });


        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await();
                        System.out.println("mission start!...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        LockSupport.parkNanos(1000*1000*1000L);
    }

    public static void main(String[] args) {
        CyclicBarrierTest cyclicBarrierTest = new CyclicBarrierTest();
        cyclicBarrierTest.testCyclicBarrier();

    }
}
