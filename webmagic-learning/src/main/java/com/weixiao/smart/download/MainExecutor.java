package com.weixiao.smart.download;

import com.sun.deploy.util.StringUtils;
import com.weixiao.smart.download.thread.DownLoadJob;
import com.weixiao.smart.download.util.ExecutorResult;
import org.jsoup.helper.StringUtil;
import sun.swing.BeanInfoUtils;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-05-13 20:43.
 */
public class MainExecutor {
    /**
     * 下载结果队列
     */
    private Queue<Future<ExecutorResult>> futureList = new ConcurrentLinkedQueue<Future<ExecutorResult>>();
    /**
     * 容错次数
     */
    private int wrongCount = 3;
    /**
     * 线程池 ，默认为FixedThreadPool
     */
    private int fixedLength = 5;
    private ExecutorService executorService = Executors.newFixedThreadPool(fixedLength);

    /**
     * 下载链接
     */
    LinkedBlockingQueue<String> urls = new LinkedBlockingQueue<String>();
    /**
     * 下载保存目录
     */
    private  String savePath;

    public MainExecutor() {
        super();
    }

    public MainExecutor(ExecutorService executorService, LinkedBlockingQueue<String> urls) {
        this.executorService = executorService;
        this.urls = urls;
    }

    private  volatile  boolean isrunning = true;

    public void excutoer() {
        while (isrunning) {
            try {
                String url  = urls.poll(1, TimeUnit.SECONDS);
                if (isNotEmpty(url)) {
                    Future<ExecutorResult> future;
                    String[] urls = url.split("!");
                    if (savePath != null && savePath != "") {
                        if (urls.length == 2) {
                            future  = executorService.submit(new DownLoadJob(urls[0],savePath,urls[1]));
                        }
                    }else{
                        future  = executorService.submit(new DownLoadJob(url));
                    }

                    //futureList.add(future);
                    //resolveResult(futureList, executorService);
                }else{
                    isrunning = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private AtomicInteger  count = new AtomicInteger();

    /**
     * 处理下载结果，下载失败重试下载，下载容错默认3次
     * @param list
     * @param executorService
     */
    public void resolveResult(Queue<Future<ExecutorResult>> list, ExecutorService executorService) {
        if (list.size() > 0) {
            if(count.get()>wrongCount)
                return;
            count.incrementAndGet();
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
            resolveResult(list, executorService);
        }
    }

    public boolean isNotEmpty(String value) {
        if (value != null && value != "") {
            return true;
        }else{
            return false;
        }
    }

    public boolean isUrl(String url) {
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=!]*)?";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(url).matches();
    }
    public void shutdown() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public LinkedBlockingQueue<String> getUrls() {
        return urls;
    }

    public void setUrls(LinkedBlockingQueue<String> urls) {
        this.urls = urls;
    }

    public int getFixedLength() {
        return fixedLength;
    }

    public void setFixedLength(int fixedLength) {
        this.fixedLength = fixedLength;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void offer(String  c) {
        try {
            urls.offer(c,2,TimeUnit.SECONDS);
            if (!isrunning) {
                isrunning=true;
                excutoer();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
