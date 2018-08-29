package com.weixiao.smart.webmagic.main.processor;

import com.weixiao.smart.webmagic.main.pipeline.DownloadPipeline;
import com.weixiao.smart.webmagic.main.pipeline.MangasoPipeline;
import org.jsoup.Jsoup;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-08-01 18:56.
 */
public class MangasoPageProcessor implements PageProcessor {
    //private static final String URL = "http://mangaso.com/mon-mon-%E4%BB%8A%E5%AE%B5%E3%80%81%E5%A6%BB%E3%81%8C%E6%99%92%E3%81%95%E3%82%8C%E3%81%A6-4k%E6%8E%83%E5%9C%96%E7%B5%84";
    //private static final String URL = "http://www.19acg.com/%E9%A2%A8%E7%9A%84%E5%B7%A5%E6%88%BFrusty-soul%E3%80%81%E6%88%96%E5%8D%81%E3%81%9B%E3%81%AD%E3%81%8B-curse-eater-%E5%91%AA%E8%A9%9B%E5%96%B0%E3%82%89%E3%81%84%E5%B8%AB-curse-eater-%E8%A9%9B";
    //private static final String URL = "http://www.19acg.com/%E8%89%A6%E9%9A%8A%E6%94%B6%E8%97%8F%E5%9C%A8%E5%B7%A5%E5%8F%A3%E8%A7%B8%E6%89%8B%E4%B8%AD%E5%A2%AE%E8%90%BD%E7%9A%84%E8%89%A6%E5%A8%98%E5%80%912-40p-2/1060";
    //private static final String URL = "http://www.19acg.com/%E7%90%B4%E7%BE%A9%E5%BC%93%E4%BB%8B%E8%9C%9C%E9%A4%A1%E6%BB%BF%E6%BB%BF%E7%9A%84%E7%94%9C%E7%BE%8E%E5%B7%A8%E4%B9%B3%E5%B9%AB%E4%BD%A0%E6%91%A9%E6%93%A6%E4%B8%80%E4%B8%8B-%E4%B8%8A84p";
    //private static final String URL = "http://mangaso.com/4k%E6%8E%83%E5%9C%96%E7%B5%84%E4%BC%8A%E9%A7%92%E4%B8%80%E5%B9%B3-%E5%A5%A5%E6%A7%98%E3%83%AF%E3%83%AC%E3%83%A1%E3%83%A9%E3%83%B3%E3%83%89-194p";
    //private static final String URL = "http://mangaso.com/%E7%81%AB%E5%BD%B1%E5%BF%8D%E8%80%85%E5%B7%A8%E4%B9%B3%E5%BF%8D%E8%80%85%E4%B9%B3%E5%BD%B1-46p";
    //private static final String URL = "http://mangaso.com/%E7%81%AB%E5%BD%B1%E5%BF%8D%E8%80%85%E7%B6%B1%E6%89%8B%E7%9A%84%E6%B7%AB%E6%8E%A5%E5%BE%85-41p";
    //private static final String URL ="http://mangaso.com/%E7%90%B4%E7%BE%A9%E5%BC%93%E4%BB%8B%E8%9C%9C%E9%A4%A1%E6%BB%BF%E6%BB%BF%E7%9A%84%E7%94%9C%E7%BE%8E%E5%B7%A8%E4%B9%B3%E5%B9%AB%E4%BD%A0%E6%91%A9%E6%93%A6%E4%B8%80%E4%B8%8B-%E4%B8%8A84p-2";
    //private static final String URL = "http://mangaso.com/%E7%90%B4%E7%BE%A9%E5%BC%93%E4%BB%8B%E8%9C%9C%E9%A4%A1%E6%BB%BF%E6%BB%BF%E7%9A%84%E7%94%9C%E7%BE%8E%E5%B7%A8%E4%B9%B3%E5%B9%AB%E4%BD%A0%E6%91%A9%E6%93%A6%E4%B8%80%E4%B8%8B-%E4%B8%8A84p";
    //private static final String URL = "http://www.19acg.com/%E3%81%95%E3%81%84%E3%81%A0%E4%B8%80%E6%98%8E%E5%A5%BD%E5%87%8C%E8%BE%B1%E5%A5%B3%E5%95%8A%E2%99%A1-%E4%B8%8A101p/483";
    //private static final String URL = "http://www.19acg.com/%E5%B0%91%E5%A5%B3%E8%88%87%E6%88%B0%E8%BB%8A%E7%95%B6%E5%A6%BB%E5%AD%90%E8%AE%8A%E6%88%90%E5%A5%B3%E4%BA%BA%E8%A5%BF%E4%BD%8F%E5%BF%97%E7%A9%97-37p/803/";
    //private static final String URL = "http://www.19acg.com/%E9%BB%92%E6%BE%A4r%E5%81%B7%E8%B5%B0%E4%BD%A0%E7%9A%84%E5%A6%BB%E5%AD%90-%E4%B8%8B92p/469/";
    //private static final String URL = "http://www.19acg.com/%E7%90%B4%E7%BE%A9%E5%BC%93%E4%BB%8B%E8%9C%9C%E9%A4%A1%E6%BB%BF%E6%BB%BF%E7%9A%84%E7%94%9C%E7%BE%8E%E5%B7%A8%E4%B9%B3%E5%B9%AB%E4%BD%A0%E6%91%A9%E6%93%A6%E4%B8%80%E4%B8%8B-%E4%B8%8A84p/2132/";
    //private static final String URL = "http://www.19acg.com/%E7%90%B4%E7%BE%A9%E5%BC%93%E4%BB%8B%E8%9C%9C%E9%A4%A1%E6%BB%BF%E6%BB%BF%E7%9A%84%E7%94%9C%E7%BE%8E%E5%B7%A8%E4%B9%B3%E5%B9%AB%E4%BD%A0%E6%91%A9%E6%93%A6%E4%B8%80%E4%B8%8B-%E4%B8%8B106p/2134/";
    //private static final String URL = "http://www.19acg.com/%E9%B3%B3%E3%81%BE%E3%81%B2%E3%82%8D%E3%82%AD%E3%83%A2%E3%83%81%E3%82%A4%E3%82%A4%E3%82%A2%E3%83%8A-%E5%BE%88%E8%88%92%E6%9C%8D%E6%B7%AB%E7%8C%A5%E7%9A%84%E7%A9%B4%E4%B8%8A120p/384/";
    //private static final String URL = "http://www.19acg.com/%E9%B3%A9%E7%9F%A2%E8%B1%86%E4%B8%83%E6%80%A7%E6%AC%B2%E9%81%8E%E5%89%A9%E7%9A%84%E4%BA%BA%E5%A6%BB%E5%80%91%E7%9A%84%E5%8D%88%E5%BE%8C-%E4%B8%8A100p/1393/";
    //private static final String URL = "http://www.19acg.com/%E6%9D%A2%E8%87%93%E9%9B%BB%E6%A2%AF%E9%87%8C%E7%9A%84%E5%A2%AE%E8%90%BD%E4%BA%BA%E5%A6%BBchineseversion1-0-73p/749/";
    //private static final String URL = "http://www.19acg.com/%E7%A0%82%E5%B7%9D%E5%A4%9A%E8%89%AF%E6%8F%92%E5%85%A5%E5%90%9E%E5%99%AC%E5%A2%AE%E8%90%BD%E7%9A%84%E4%BA%BA%E5%A6%BB%E5%80%91-%E4%B8%8A100p/241/";
    //private static final String URL = "http://www.19acg.com/%E3%82%86%E3%81%85%E3%80%9C%E3%81%8C%E6%B7%AB%E4%BA%82%E4%BA%BA%E5%A6%BB%E9%80%B2%E8%A1%8C%E5%BC%8F-%E4%B8%8A100p/188/";
    private static final String URL = "http://www.19acg.com/%E3%81%BF%E3%81%86%E3%82%89%E3%81%9F%E3%81%91%E3%81%B2%E3%82%8D%E6%94%AF%E9%85%8D%E3%80%90%E8%A2%AB%E5%9B%9A%E7%A6%81%E7%9A%84%E5%AD%A4%E5%B3%B6%E3%80%91%E7%AF%87%E7%84%A1%E4%BF%AE%E6%AD%A3/497/";
    private Site site = Site.me().setDomain("mangaso.com").setUserAgent("Mozilla/5.2 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31").setRetryTimes(3).setTimeOut(120000);
    private static String savePath;

    @Override
    public void process(Page page) {
        String temp = page.getHtml().xpath("//nav[@class=\"page-pagination\"]/a[@class=\"next\"]/@href").toString();
        String regex = URL + ".*";
        //page.addTargetRequests(page.getHtml().xpath("//nav[@class=\"page-pagination\"]/a[@class=\"next\"]/@href").regex(regex).all());//取页面列表url
        page.addTargetRequests(page.getHtml().xpath("//div[@id=\"main\"]/p").links().regex(regex).all());//取页面列表url
//        List<String> urlList = page.getHtml().xpath("//div[@class=\"entry-content content-reset\"]/p/a/img/@src").all();
        List<String> urlList = page.getHtml().xpath("//div[@class=\"entry-content content-reset\"]/p/img/@src").all();
        page.putField("imageUrlList", urlList);
        page.putField("imageName", getText(page.getHtml().xpath("//nav[@class=\"page-pagination\"]/a[@class=\"next\"]/span[@class=\"tx\"]/span[@class=\"current-page\"]").get()));

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void setSavePath() {
        String path = "/Volumes/Seagate\\ Expansion\\ Drive/mac_file/webMagic/";
        String temp = "サンクリ2017-autumn-god緑茶-ぶーちゃん-bad-luck-after-ドラゴンクエ/";
        savePath = path + temp;
    }

    private static String getName(String imgUrl) {
        if (imgUrl == null) {
            return null;
        }
        String[] strs = imgUrl.split("/");
        return strs[strs.length - 1];
    }

    private String getText(String content) {
        return content.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
    }

    public static void main(String[] args) {
        setSavePath();
        long start = System.currentTimeMillis();
        MangasoPipeline mangasoPipeline = new MangasoPipeline();
        mangasoPipeline.setSavePath(savePath);
        Spider.create(new MangasoPageProcessor()).addUrl(URL).addPipeline(mangasoPipeline).thread(2).run();
        mangasoPipeline.shutdown();
        System.out.println("time cost " + (System.currentTimeMillis() - start));
    }
}
