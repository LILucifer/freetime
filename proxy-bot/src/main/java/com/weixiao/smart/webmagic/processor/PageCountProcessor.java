package com.weixiao.smart.webmagic.processor;

import com.weixiao.smart.webmagic.pipeline.PageCountPipeline;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-13 22:12.
 */
@Slf4j
public class PageCountProcessor implements PageProcessor {

    private static final String URL = "https://www.kuaidaili.com/free";
    private Site site = Site.me().setDomain("mangaso.com").setUserAgent("Mozilla/5.2 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31").setRetryTimes(3).setTimeOut(120000);

    @Override
    public void process(Page page) {
        List<String> pageList = page.getHtml().xpath("//div[@id=\"listnav\"]/ul/li/a/text()").all();
        int pageCount = 0;
        if (pageList.size() > 0) {
            pageCount = Integer.parseInt(pageList.get(pageList.size()-1));
        }
        log.info("pageList = {}" , pageList.toString() );
//        System.out.println("pageList = " + pageList.toString());
        page.putField("pageCount" , pageCount);

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        PageCountPipeline pageCountPipeline = new PageCountPipeline();
        Spider.create(new PageCountProcessor()).addUrl(URL).addPipeline(pageCountPipeline).thread(1).run();
        int pageCount = pageCountPipeline.getPageCount();
        System.out.println("end ! pageCount = " + pageCount);
    }
}
