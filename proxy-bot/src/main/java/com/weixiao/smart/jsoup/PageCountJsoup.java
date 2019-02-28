package com.weixiao.smart.jsoup;

import com.weixiao.smart.container.Constants;
import com.weixiao.smart.container.ContainerUtils;
import com.weixiao.smart.entity.ProxyIpModel;
import com.weixiao.smart.judger.JudgmentRunnable;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lishixiang0925@126.com.
 * @description (获取总页数)
 * @Created 2019-01-16 11:24.
 */
@Slf4j
public class PageCountJsoup {
    private static int pageCount = 0;

    public static int getPageCount() {
        try {
            Document doc = JsoupDoument.getDocument(ContainerUtils.PROXY_IP_URL);
            pageCount = Integer.parseInt(doc.select("div[id=\"listnav\"] li a").last().text());
            log.info("pageCount = {}", pageCount);

            //使用获取到的Proxy id 去抓取数据（验证第一页的Proxy Ip 是否可用）
            if (ContainerUtils.getControllerMap().size() == 0) {
                ProxyIpContextJsoupRunnable runnable = new ProxyIpContextJsoupRunnable("", null);
                runnable.getProxyIdContent(doc); //获取第一页Proxy IP
                CountDownLatch countDownLatch = new CountDownLatch(ContainerUtils.getJsoupResultMap().size());
                ContainerUtils.getJsoupResultMap().forEach(( key , value)->{
                    Constants.executorService.execute(new JudgmentRunnable((ProxyIpModel) value,countDownLatch));
                });
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pageCount;
    }
}
