package com.weixiao.smart.webmagic.main.model;

import com.weixiao.smart.webmagic.main.pipeline.JsonFileModelPipeline;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * @author lishixiang0925@126.com.
 * @description (the book item information model)
 * @Created 2018-07-22 09:42.
 */

@TargetUrl("(https://list.jd.com)?/list\\.html\\?cat=9987,653,655&page=[1-9]&sort=sort_rank_asc&trans=1&JL=6_0_0&ms=5#J_main")
@HelpUrl("https://list.jd.com/list.html\\?cat=9987,653,655&page=(.*?)")
@ExtractBy(value = "//div[@id='plist']/ul/li" ,multi = true)
public class JingdongItemModel {

    @ExtractBy("//div[@class='gl-i-wrap j-sku-item']/div[@class='p-img']/a/img@src")
    private String img; //图片地址
    @ExtractBy("//div[@class='gl-i-wrap j-sku-item']/div[@class='p-name']/a/em/text()")
    private String name; //名称
    @ExtractBy("//div[@class='gl-i-wrap j-sku-item']/div[@class='p-price']/strong[@class='J_price js_ys']/i")
    private String price;//价格
    @ExtractBy("//div[@class='gl-i-wrap j-sku-item']/div[@class='p-shop']/span//a/text()")
    private String shop; // 店家
    @ExtractBy("//div[@class='gl-i-wrap j-sku-item']/div[@class='p-commit']/strong/text()")
    private String commit;//
    @ExtractBy("//div[@class='gl-i-wrap j-sku-item']/div[@class='p-img']/a@href")
    private String url;//详情页

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "JingdongItemModel{" +
                "img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", shop='" + shop + '\'' +
                ", commit='" + commit + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public static void main(String[] args) {
        OOSpider.create(Site.me().setTimeOut(10000).setSleepTime(0).setRetryTimes(2),
                new JsonFileModelPipeline(), JingdongItemModel.class)
//                .setScheduler(new FileCacheQueueScheduler("/Users/apple_WeiXiao/Downloads/webmagic/jingdong/"))
                .addUrl("https://list.jd.com/list.html?cat=9987,653,655&page=1&sort=sort_rank_asc&trans=1&JL=6_0_0&ms=5#J_main")
                .thread(15).run();
    }
}
