import com.weixiao.smart.test.Application;
import com.weixiao.smart.webmagic.main.model.JingdongItemModel;
import com.weixiao.smart.webmagic.main.pipeline.JsonFileModelPipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-14 20:46.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestClass {
    @Test
    public  void testMethod(){
        SuperClass clz = new SubClass();
        System.out.println(clz.name);
    }

    @Test
    public void testMatcher(){
        //String str = "https://list.jd.com/list.html?cat=9987,653,655&page=2&sort=sort_rank_asc&trans=1&JL=6_0_0&ms=5#J_main";
        String str = "https://list.jd.com/list.html?cat=9987,653,655&page=1&sort=sort_rank_asc&trans=1&JL=6_0_0&ms=5#J_main";
        //String pattern = "https://list.jd.com/list.html\\?cat=9987,653,655&page=(.*?)";
        String pattern = "(https://list.jd.com)?/list\\.html\\?cat=9987,653,655&page=[1-9]&sort=sort_rank_asc&trans=1&JL=6_0_0&ms=5#J_main";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        System.out.println(m.matches());
    }

    @Test
    public void testModelMagic(){
        OOSpider.create(Site.me().setTimeOut(10000).setSleepTime(0).setRetryTimes(2),
                new JsonFileModelPipeline(), JingdongItemModel.class)
        .setScheduler(new FileCacheQueueScheduler("/Users/apple_WeiXiao/Downloads/webmagic/jingdong/"))
        .addUrl("https://list.jd.com/list.html?cat=9987,653,655&page=1&sort=sort_rank_asc&trans=1&JL=6_0_0#J_main")
                .thread(15).run();

    }
}


class SubClass extends SuperClass
{

    public String name = "SubClass";

}

class SuperClass
{
    public String name = "SuperClass";
}
