package com.weixiao.smart.webmagic.main.processor;

import com.weixiao.smart.webmagic.main.pipeline.DownloadPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lishixiang0925@126.com.
 * @description (千图网 图片抓取)
 * @Created 2018-07-23 21:18.
 */
public class Pic58PageProcessor implements PageProcessor {
    private static final String URL_LIST = "http://www.58pic.com/piccate/16-373-0-default-0_2_0_0_default_0-[1-9]{1,2}.html";
    private static final String URL_GET = "http://www.58pic.com/newpic/\\d+.html";
    private Site site = Site.me().setDomain("www.58pic.com").setUserAgent("Mozilla/6.2 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31").setRetryTimes(3);
    public Site getSite() {
        return site;
    }
    public void process(Page page) {
        if (page.getUrl().regex(URL_LIST).match()) {
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());//取页面列表url
            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"clickRecord\"AND@data-id=\"6\"]/div[@class=\"flow-box\"]").links().regex(URL_GET).all());//取图片列表中的item URL
        }else{
            List<String> list  = page.getHtml().xpath("//div[@id=\"show-area-height\"]/ul[@class=\"emoticon-model\"]/li/img/@src").all();
            Set<String> sets = new HashSet(list);
            sets.add(page.getHtml().xpath("//div[@id=\"show-area-height\"]/ul[@class=\"emoticonImg\"]/a/@href").toString());
            page.putField("imgUrlList" , sets);
            page.putField("content",page.getHtml().xpath("//div[@class=\"mainLeft-content\"]"));
            page.putField("title",page.getHtml().xpath("//head/title"));
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        DownloadPipeline downloadPipeline = new DownloadPipeline();
        Spider.create(new Pic58PageProcessor()).addUrl("http://www.58pic.com/piccate/16-373-0-default-0_2_0_0_default_0-2.html").addPipeline(downloadPipeline).thread(20).run();
        downloadPipeline.shutdown();
        System.out.println("time cost " + (System.currentTimeMillis()-start));
    }


}
