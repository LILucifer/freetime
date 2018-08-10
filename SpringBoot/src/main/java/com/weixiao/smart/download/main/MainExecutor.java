package com.weixiao.smart.download.main;

import com.weixiao.smart.download.thread.DownLoadJob;
import com.weixiao.smart.download.util.ExecutorResult;
import com.weixiao.smart.download.util.ImagesUrl;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-05-13 20:43.
 */
public class MainExecutor {
    private BlockingQueue<Future<ExecutorResult>> blockingDeque = new LinkedBlockingQueue<Future<ExecutorResult>>();
    private Queue<Future<ExecutorResult>> futureList = new ConcurrentLinkedQueue<Future<ExecutorResult>>();
    private int wrongCount = 3;

    public void excutoer() {
        List<String> urls = ImagesUrl.getInstance().getImgUrl();


        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (String url : urls) {
            Future<ExecutorResult> future = executorService.submit(new DownLoadJob(url));
            futureList.add(future);
        }

        resolveResult2(futureList, executorService);
        executorService.shutdown();


    }

    private ConcurrentMap<String, Integer> recursion = new ConcurrentHashMap<String, Integer>();
    public void resolveResult(Queue<Future<ExecutorResult>> list, ExecutorService executorService) {
        if (list.size() > 0) {
            for (Future<ExecutorResult> future : list) {
                try {
                    ExecutorResult executorResult = future.get();
                    if (!executorResult.getSuccess()) {
                        String url = executorResult.getUrl();
                        Integer count = 0;
                        if (recursion.containsKey(url)) {
                            count = recursion.get(url);
                        }
                        if (count >= wrongCount) {
                            System.out.println("3次下载失败---"+url);
                        }else{
                            Future<ExecutorResult> future1 = executorService.submit(new DownLoadJob(url));
                            count = count + 1;
                            if (recursion.containsKey(url)) {
                                recursion.replace(url, count - 1, count);
                            }else{
                                recursion.put(url, count);
                            }
                            list.add(future1);
                        }
                    }
                    list.remove(future);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            resolveResult(list, executorService);
        }
    }

    private int count = 0;
    public void resolveResult2(Queue<Future<ExecutorResult>> list, ExecutorService executorService) {
        if (list.size() > 0) {
            if(count>=wrongCount)
                return;
            count++;
            for (Future<ExecutorResult> future : list) {
                try {
                    ExecutorResult executorResult = future.get();
                    if (!executorResult.getSuccess()) {
                        String url = executorResult.getUrl();
                            Future<ExecutorResult> future1 = executorService.submit(new DownLoadJob(url));
                            list.add(future1);
                        }
                    list.remove(future);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            resolveResult2(list, executorService);
        }
    }
}
