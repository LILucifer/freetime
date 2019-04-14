package com.weixiao.smart.quartz;

import com.weixiao.smart.container.Constants;
import com.weixiao.smart.container.ContainerUtils;
import com.weixiao.smart.entity.ProxyIpModel;
import com.weixiao.smart.jsoup.PageCountJsoup;
import com.weixiao.smart.jsoup.ProxyIpContextJsoupRunnable;
import com.weixiao.smart.judger.JudgmentRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-16 15:31.
 */
@Slf4j
@Component
@EnableScheduling
public class ProxyIpJsoupQuartz {


    /**
     * 当前页
     */
    private int currentPage = 1;
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
    private static ReentrantLock reentrantLock = new ReentrantLock();
    private static Condition condition = reentrantLock.newCondition();

    public static void conditionAwait() {
//        if (isNeedClosePreTask) {
//            return;
//        }
        try {
            reentrantLock.lock();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

    public static void conditionSignalAll() {
        try {
            reentrantLock.lock();
            //结束上次任务中的等待线程
            condition.signalAll();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void executor() {
        log.info("mission start ！ ");
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Constants.executorService;
        if (threadPoolExecutor.getQueue().size() > 0||threadPoolExecutor.getActiveCount()>0) {
            log.info("end running mission ----start");
            isNeedClosePreTask = true;
            conditionAwait();
            //线程关闭信号量复位
            isNeedClosePreTask = false;
            log.info("end running mission ----finished");
        }
//        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Constants.executorService;
//        while (threadPoolExecutor.getQueue().size() > 0) {
//        }
        long startTime = System.currentTimeMillis();
        pageCount = PageCountJsoup.getPageCount();
        if (pageCount <= 0) {
            return;
        }
        getProxyIpFromKuaidaili();
        log.info("this task cost time : {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis() - startTime));
        log.info("all ControllerMap = {}", ContainerUtils.getControllerMap());
        //任务结束释放线程池condition.await阻塞队列
        conditionSignalAll();
    }

    /**
     * 从 https://www.kuaidaili.com 获取免费代理IP
     */
    private void getProxyIpFromKuaidaili() {
        int maxDataPage = pageCount < 10 ? pageCount : 10;
        if (currentPage >= 10) {
            if (currentPage >= (pageCount + 10)) {
                return;
            }
            maxDataPage =currentPage + 10;
        }

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = currentPage; i <= maxDataPage; i++) {
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
        if (ContainerUtils.getControllerMap().size() < Constants.CONTROLLER_MAP_MIN_SIZE) {
            getProxyIpFromKuaidaili();
        }
    }


    /**
     * 检查proxy id 是否有效
     */
    private void VerifyProxyIpEffective() {
        log.info("VerifyProxyIpEffective  begin");
        ConcurrentHashMap<String, ProxyIpModel> tempMap = ContainerUtils.getJsoupResultMap();
        CountDownLatch countDownLatch = new CountDownLatch(tempMap.size());
        tempMap.forEach((key, value) -> {
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
                ContainerUtils.removeAllFailUrl();
                return;
            }
            resolveFailCount++;
            Set<String> tempFailUrl = ContainerUtils.getFailUrl();
            int failUrlSize = tempFailUrl.size();
            String[] failUrl = new String[failUrlSize];
            tempFailUrl.toArray(failUrl);
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

    @Scheduled(fixedRate = 1000*15)
    public void testQuartzConcurrent(){
        log.info("testQuartzConcurrent ----- {}" , LocalDateTime.now());
        int i = 1;
        while (i==1) {

        }
        log.info("testQuartzConcurrent end  ----- {}" , LocalDateTime.now());

    }
}
