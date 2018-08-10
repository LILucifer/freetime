package webmagic.main.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-07-19 08:01.
 */
public class MinibooksPageProcessor implements PageProcessor {
    private Site  site =Site.me().setRetryTimes(3).setTimeOut(10000);

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return site;
    }
}
