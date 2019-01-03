import com.weixiao.smart.download.main.MainExecutor;
import com.weixiao.smart.download.util.FileOperate;
import com.weixiao.smart.sort.*;
import com.weixiao.smart.test.Application;
import com.weixiao.smart.test.sychronized.*;
import com.weixiao.smart.test.timer.Mytask;
import com.weixiao.smart.test.waitAndNotify.*;
import org.apache.commons.collections.BinaryHeap;
import org.apache.tomcat.util.collections.SynchronizedQueue;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.awt.Mutex;
import sun.nio.ch.ThreadPool;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.*;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-18 20:18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestThread {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void testMethodThreadPrint() {
        try {
            PrintString printString = new PrintString();
            new Thread(printString).start();
            Thread.sleep(2000);

            System.out.println("printString Thread will be shutdown immediately");
            printString.setContentPrint(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDifferentBettweenVolatileAndSychronied(){
        ThreadVolatile[] threadVolatiles = new ThreadVolatile[100];

        for(int i = 0 ; i<100;i++) {
            threadVolatiles[i] = new ThreadVolatile();
        }
        for(int i = 0 ; i<100;i++) {
            threadVolatiles[i].start();
        }
    }

    @Test
    public void waitAndNotify(){
        try {
            Object lock = new Object();
            WaitAndNotifyThread1 waitAndNotifyThread1 = new WaitAndNotifyThread1(lock);
            waitAndNotifyThread1.start();
            Thread.sleep(5000);
            WaitAndNotifyThread2 waitAndNotifyThread2 = new WaitAndNotifyThread2(lock);
            waitAndNotifyThread2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crossBackup() {

        DBtools dBtools = new DBtools();
        for(int i = 0 ; i<10;i++) {
            BackupA backupA = new BackupA(dBtools);
            backupA.start();
            //backupA.join(1000);
            BackupB backupB = new BackupB(dBtools);
            backupB.start();
        }

    }

    @Test
    public void inheritableThreadLocalTest(){
        try {
            InheritableThreadLocalTest test = new InheritableThreadLocalTest();
            for(int i = 0; i<5;i++) {
                System.out.println(test.get());
            }
            Thread.sleep(1000);

            ThreadTest threadTest = new ThreadTest(test);
            threadTest.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
    }
    @Test
    public void testTimer() {
        System.out.println("now time " + new Date());
        Mytask mytask = new Mytask();
        Timer timer = new Timer(true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,5);
        Date date = calendar.getTime();
        timer.schedule(mytask,new Date());
    }

    @Test
    public void testSynchronized() {
        Service service = new Service();
        ThreadA threadA = new ThreadA(service);
        threadA.setName("threadA");
        ThreadB threadB = new ThreadB(service);
        threadB.setName("threadB");
        ThreadC threadC = new ThreadC(service);
        threadC.setName("threadC");

        threadA.start();
        threadB.start();
        threadC.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        threadA.join();
        threadA.interrupt();

        ThreadLocal threadLocal;
        ReentrantLock reentrantLock;
        ReentrantReadWriteLock reentrantReadWriteLock;

        AtomicInteger atomicInteger;
        AtomicReference atomicReference;
        ConcurrentHashMap concurrentHashMap;
        LinkedList linkedList;



        //ThreadPool threadPool;
        //com.sun.corba.se.spi.orbutil.threadpool.ThreadPool threadPool1;
        //ThreadPoolExecutor threadPoolExecutor;
        //Executor.newFixedThreadPool(3);
        // service =
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Mutex mutex;
        LockSupport lockSupport;
        AbstractQueuedSynchronizer abstractQueuedSynchronizer;
        ConcurrentMap concurrentMap;
        ConcurrentLinkedQueue concurrentLinkedQueue;
        ArrayBlockingQueue blockingQueue;
        DelayQueue delayQueue;
        SynchronizedQueue synchronizedQueue;
        ForkJoinTask forkJoinTask;
        RecursiveTask recursiveTask;
        ForkJoinPool forkJoinPool;
        AtomicInteger atomicInteger1;
        Semaphore semaphore;
        Collection collection;
        Map map;
        List list;
        LinkedList linkedList1;
        BinaryHeap binaryHeap;
        HashMap hashMap;
        ConcurrentHashMap concurrentHashMap1;
        Lock lock;
        LockSupport lockSupport1;
        Exception exception;
        Error error;
        Arrays arrays;
        String temp;


    }

    @Test
    public void testDownloadImg() {
        MainExecutor mainExecutor = new MainExecutor();
        mainExecutor.excutoer();
    }

    @Test
    public void testDeleteFile() {
        FileOperate fileOperate = new FileOperate();
        fileOperate.deleteFile("/Users/apple_WeiXiao/Downloads/test" );
        System.out.println("count="+ fileOperate.count);
    }

    @Test
    public void testSort() {
        int[] sortatt = {45,3,64,75,86,34,234,87,98};
        int[] sortatt2 = {45,3,64,75,86,34,234,87,98};
        InsertSort insertSort = new InsertSort();
        insertSort.printArray(sortatt);
        System.out.println("insertSort start");
        insertSort.insertSort1(sortatt2);
        System.out.println("--------");
        insertSort.insertSort2(sortatt);
        System.out.println("--------");
        int[] sortatt3 = {45,3,64,75,86,34,234,87,98};
        ShellSort shellSort = new ShellSort();
        System.out.println("shellSort start");
        shellSort.shellSort(sortatt3);
        System.out.println("shellSort end--------");

        System.out.println("HeapSort start");
        int[] att3 = {45, 3, 64, 75, 86, 34, 234, 87, 98};
        HeapSort heapSort = new HeapSort(false , att3);
        System.out.println(Arrays.toString(att3));
        System.out.println("heapSort end");

        System.out.println("HeapSort2 start");
        int[] att4 = {45, 3, 64, 75, 86, 34, 234, 87, 98};
        HeapSort2.sort(att4);
        System.out.println(Arrays.toString(att4));
        System.out.println("heapSort2 end");

        System.out.println("MargeSort start");
        int[] att5 = {45, 3, 64, 75, 86, 34, 234, 87, 98};
        MergeSort mergeSort = new MergeSort();
        mergeSort.mergeSort(att5);
        System.out.println("MergeSort end ");
    }


    @Test
    public void testThreadPool(){
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(  int i=0;i<10;i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "  "+ Math.random());
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        try {
            executorService.awaitTermination(60,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
