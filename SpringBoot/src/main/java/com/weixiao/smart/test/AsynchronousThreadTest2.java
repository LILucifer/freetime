package com.weixiao.smart.test;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhouhaiming  Email: dg_chow@163.com
 * @date 2018/09/18 17:10
 */
public class AsynchronousThreadTest2 {
    static Object o = new Object();

    static Lock lock = new ReentrantLock();
    static Condition conditionA = lock.newCondition();
    static Condition conditionB = lock.newCondition();
    static Condition conditionC = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Milk milk = new Milk();
        ExecutorService executorService = Executors.newCachedThreadPool();

        printABC(executorService);
    }

    public static void printABC(ExecutorService executorService) throws InterruptedException {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                pringA();
            }
        });

        TimeUnit.MICROSECONDS.sleep(10000);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                pringB();
            }
        });
        TimeUnit.MICROSECONDS.sleep(10000);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                pringC();
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        executorService.shutdownNow();
    }


    public static void pringA() {
        int i = 0;
        try {
            lock.lock();
            while (i < 5) {
                System.out.print("A");
                i++;
                conditionB.signal();
                conditionA.await();
            }
            conditionB.signal();
            System.out.println("*A*");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void pringB() {
        int i = 0;
        try {
            lock.lock();
            while (i < 5) {
                System.out.print("B");
                i++;
                conditionC.signal();
                conditionB.await();
            }
            conditionC.signal();
            System.out.println("*B*");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void pringC() {
        int i = 0;
        try {
            lock.lock();
            while (i < 5) {
                System.out.println("C");
                i++;
                conditionA.signal();
                conditionC.await();
            }
            System.out.println("*C*");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
