package com.weixiao.smart.quartz;

import com.weixiao.smart.container.Constants;
import com.weixiao.smart.container.ContainerUtils;
import com.weixiao.smart.entity.ProxyIpModel;
import com.weixiao.smart.jsoup.PageCountJsoup;
import com.weixiao.smart.jsoup.ProxyIpContextJsoupResult;
import com.weixiao.smart.jsoup.ProxyIpContextJsoupRunnable;
import com.weixiao.smart.judger.JudgmentRunnable;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-16 15:31.
 */
@Slf4j
public class ProxyIpJsoupQuartz {


    /**
     * 当前页
     */
    private int currentPage = 0;
    /**
     * https://www.kuaidaili.com 总页数
     */
    private int pageCount = 0;

    /**
     * 关闭线程信号量 , 当次任务是否结束
     */
    public static volatile boolean isNeedClosePreTask = false;
    /**
     * 线程总控开关
     */
    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition condition = reentrantLock.newCondition();

    public void executor() {

        isNeedClosePreTask = true;
        while (reentrantLock.hasWaiters(condition)) {
            condition.signalAll();//结束上次任务中的等待线程
        }

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Constants.executorService;
        while (threadPoolExecutor.getQueue().size()>0) {
        }
        isNeedClosePreTask = false; //线程关闭信号量复位





        long startTime = System.currentTimeMillis();
        pageCount = PageCountJsoup.getPageCount();
        if (pageCount <= 0) {
            return;
        }
        getProxyIpFromKuaidaili();
        log.info("this task cost time : {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis() - startTime));
    }

    /**
     * 从 https://www.kuaidaili.com 获取免费代理IP
     */
    private void getProxyIpFromKuaidaili() {
        while (ContainerUtils.getControllerMap().size() < Constants.CONTROLLER_MAP_MIN_SIZE) {
            CountDownLatch countDownLatch = new CountDownLatch(pageCount);
            for (int i = 1; i <= currentPage + 10 && i <= pageCount; i++) {
                String url = ContainerUtils.PROXY_IP_URL + "inha/" + i;
                Constants.executorService.execute(new ProxyIpContextJsoupRunnable(url, countDownLatch));
                currentPage++;
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            VerifyProxyIpEffective();

            resolveFailUrl();//容错处理获取失败的页面
        }
    }


    /**
     * 检查proxy id 是否有效
     */
    private void VerifyProxyIpEffective() {
        log.info("VerifyProxyIpEffective  begin");
        CountDownLatch countDownLatch = new CountDownLatch(ContainerUtils.getJsoupResultMap().size());
        ContainerUtils.getJsoupResultMap().forEach((key, value) -> {
            ProxyIpModel model = (ProxyIpModel) value;
            Constants.executorService.execute(new JudgmentRunnable(model, countDownLatch));
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("controllerMap size = {} , controllerMap = {}", ContainerUtils.getControllerMap().size(), ContainerUtils.getControllerMap());
        log.info("VerifyProxyIpEffective  end");
    }

    /**
     * 处理获取 ProxyIp 失败的URL
     */
    int resolveFailCount = 0;

    public void resolveFailUrl() {
        if (ContainerUtils.getFailUrl().size() > 0) {
            if (resolveFailCount >= Constants.Fault_tolerance_COUNT) {//容错次数
                resolveFailCount = 0;
                return;
            }
            resolveFailCount++;
            int failUrlSize = ContainerUtils.getFailUrl().size();
            String[] failUrl = new String[failUrlSize];
            ContainerUtils.getFailUrl().toArray(failUrl);
            ContainerUtils.removeAllFailUrl();//清空failUrl
            CountDownLatch countDownLatch = new CountDownLatch(failUrlSize);
            for (String url : failUrl) {
                Constants.executorService.execute(new ProxyIpContextJsoupRunnable(url, countDownLatch));
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            VerifyProxyIpEffective();
            resolveFailUrl();
        }
    }
}
