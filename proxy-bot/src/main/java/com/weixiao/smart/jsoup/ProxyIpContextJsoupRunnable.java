package com.weixiao.smart.jsoup;

import com.weixiao.smart.container.ContainerUtils;
import com.weixiao.smart.entity.ProxyIpModel;
import com.weixiao.smart.quartz.ProxyIpJsoupQuartz;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static com.weixiao.smart.container.ContainerUtils.addFailUrl;


/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-16 14:04.
 */
@Slf4j
public class ProxyIpContextJsoupRunnable implements Runnable {

    /**
     * 抓取内容URL
     */
    private String url = "";
    private CountDownLatch countDownLatch;
    private boolean isFinished = false;

    public ProxyIpContextJsoupRunnable(String url, CountDownLatch countDownLatch) {
        this.url = url;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        if (ProxyIpJsoupQuartz.isNeedClosePreTask) {//判定是否继续执行任务：下次任务启动时前次任务无需执行直接结束
            return;
        }

        try {
            Document doc = JsoupDoument.getDocument(url);
            getProxyIdContent(doc); //获取proxy id
        } catch (IOException e) {
            log.info(" in this url [ {} ] get Document was failed , add to FailUrl try again   ", url);
            addFailUrl(url);
        } finally {
            isFinished = true;
            countDownLatch.countDown();
        }
    }

    public void getDocumentByUrl() {
        try {
            Document doc = JsoupDoument.getDocument(url);
            getProxyIdContent(doc); //获取proxy id
        } catch (IOException e) {
            log.info(" in this url [ {} ] get Document was failed , add to FailUrl try again   ", url);
            addFailUrl(url);
        }
    }

    public void getProxyIdContent(Document document) {
        Elements elements = document.select("div[id=\"list\"] table tbody tr");
        if (elements != null && elements.size() > 0) {
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                String ip = element.select("td[data-title=\"IP\"]").text().toString();
                String port = element.select("td[data-title=\"PORT\"]").text().toString();
                String location = element.select("td[data-title=\"位置\"]").text().toString();
                String type = element.select("td[data-title=\"类型\"]").text().toString();
                String respondingSpeed = element.select("td[data-title=\"响应速度\"]").text().toString();
                String lastVerifyTime = element.select("td[data-title=\"最后验证时间\"]").text().toString();

                ProxyIpModel model = ProxyIpModel.builder().ip(ip)
                        .port(port)
                        .location(location)
                        .type(type)
                        .respondingSpeed(respondingSpeed)
                        .lastVerifyTime(lastVerifyTime).build();
                ContainerUtils.addJsoupResultMap(ip, model);
            }
        }
        log.info("jsoupResultMap size = {} ,jsoupResultMap = {} ", ContainerUtils.getJsoupResultMap().size(), ContainerUtils.getJsoupResultMap());
    }

    public static void main(String[] args) {
        String url = "https://www.kuaidaili.com/free/";
        ProxyIpContextJsoupRunnable jsoup = new ProxyIpContextJsoupRunnable(url, new CountDownLatch(2));
        jsoup.getDocumentByUrl();
    }

    public boolean isFinished() {
        return isFinished;
    }

}
