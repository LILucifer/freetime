package com.weixiao.smart;

import com.weixiao.smart.entity.ProxyIpModel;
import com.weixiao.smart.judger.JudgmentRunnable;
import com.weixiao.smart.quartz.ProxyIpJsoupQuartz;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-08 22:17.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        ProxyIpJsoupQuartz jsoupQuartz = new ProxyIpJsoupQuartz();
        jsoupQuartz.executor();

        /*ExecutorService executorService = Executors.newFixedThreadPool(3);

        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            ProxyIpModel model =  ProxyIpModel.builder().build();
            executorService.execute(new JudgmentRunnable(model,countDownLatch));
        }*/

    }
}
