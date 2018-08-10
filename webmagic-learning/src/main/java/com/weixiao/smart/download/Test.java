package com.weixiao.smart.download;

import com.weixiao.smart.download.util.ImagesUrl;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-07-25 23:50.
 */
public class Test {
    private MainExecutor mainExecutor = new MainExecutor();
    private boolean isStart = false ;

    public void start() {
        List<String> urls = ImagesUrl.getInstance().getImgUrl();
        for (String url : urls) {
            mainExecutor.offer(url);
        }
        if (!isStart) {
            mainExecutor.excutoer();
            isStart = true;
        }
    }

    public static void main(String[] args) {
//        new Test().start();
//        String url = "http://mangaso.com/%E3%82%B5%E3%83%B3%E3%82%AF%E3%83%AA2017-autumn-god%E7%B7%91%E8%8C%B6-%E3%81%B6%E3%83%BC%E3%81%A1%E3%82%83%E3%82%93-bad-luck-after-%E3%83%89%E3%83%A9%E3%82%B4%E3%83%B3%E3%82%AF%E3%82%A8/4/";
//        String regex = "http://mangaso.com/%E3%82%B5%E3%83%B3%E3%82%AF%E3%83%AA2017-autumn-god%E7%B7%91%E8%8C%B6-%E3%81%B6%E3%83%BC%E3%81%A1%E3%82%83%E3%82%93-bad-luck-after-%E3%83%89%E3%83%A9%E3%82%B4%E3%83%B3%E3%82%AF%E3%82%A8(/[\\w- ./?%&=!]*)?";
//        Pattern pattern = Pattern.compile(regex);
//        System.out.println(pattern.matcher(url).matches());
        String content = "<span class=\"current-page\">1</span>";
        content = content.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
        System.out.println(content);
    }
}
