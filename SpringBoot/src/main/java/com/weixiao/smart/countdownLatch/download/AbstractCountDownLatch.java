package com.weixiao.smart.countdownLatch.download;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author lishixiang0925@126.com.
 * @description (静态封装计数锁存储器)
 * @Created 2018-07-16 21:14.
 */
public abstract class AbstractCountDownLatch {
    /**
     * 计数锁存储器
     * */
    private CountDownLatch countDownLatch  = null;
    /**
     * 默认为固定线程池
     * */
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    /**
    * Executor Job 总数
    */
    private int JOB_COUNT = 0;

    private List<Runnable> joblist = new ArrayList();

    public AbstractCountDownLatch(CountDownLatch countDownLatch, ExecutorService executorService) {
        this.countDownLatch = countDownLatch;
        this.executorService = executorService;
    }

    /**
     * 开启线程池 ， 启动任务
     */
    public final  void   start() {
        if (joblist.size() == 0) {
            throw new NullPointerException("joblist Not allowed to be empty ");
        }
        if (executorService == null) {
            //if executorService was null , then initialize a FixedThreadPool with 5 maximumPoolSize
            executorService = Executors.newFixedThreadPool(5);
        }
        if (countDownLatch == null) {
            countDownLatch = new CountDownLatch(joblist.size());
        }
        for (Runnable object : joblist) {
            executorService.submit(object);
        }

    }
    /**
     * 返回线程job执行结果
     * @param future
     */
    public void executorResult(Future future) {
    }

    /**
     * 线程池是否关闭
     * @return
     */
    public boolean isShotDown(){
        if (executorService == null) {
            throw  new NullPointerException("executorService is null ");
        }
        return executorService.isShutdown();
    }

    /**
     * add one job
     * @param object
     */
    public void addJob(Runnable object) {
        joblist.add(object);
    }

    /**
     * add jobs
     * @param collection
     */
    public void addAllJob(Collection<Runnable> collection) {
        joblist.addAll(collection);
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
