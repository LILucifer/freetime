import com.weixiao.smart.Application;
import com.weixiao.smart.quartz.ProxyIpJsoupQuartz;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-04-03 22:37.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JunitTestOnProxyBot {
    @Autowired
    private ProxyIpJsoupQuartz jsoupQuartz;

    @Test
    public void test1() {
        ProxyIpJsoupQuartz jsoupQuartz = new ProxyIpJsoupQuartz();
        jsoupQuartz.executor();
    }

    @Test
    public void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 6; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    jsoupQuartz.executor();
                }
            });
            try {
                Thread.sleep(1000 * 60 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}
