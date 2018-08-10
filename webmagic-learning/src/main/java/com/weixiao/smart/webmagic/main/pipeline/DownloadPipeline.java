package com.weixiao.smart.webmagic.main.pipeline;

import com.weixiao.smart.download.MainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-07-24 21:18.
 */
public class DownloadPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private MainExecutor mainExecutor = new MainExecutor();
    private boolean isStart = false ;
    private AtomicInteger count = new AtomicInteger();
    @Override
    public void process(ResultItems resultItems, Task task) {
        logger.warn("get page: " + resultItems.getRequest().getUrl());
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
            if ("imageUrlList".equals(entry.getKey())) {
                List<String> sets = (List<String>) entry.getValue();
                if (sets != null && sets.size() > 0) {
                    for (String url : sets) {
                        if (url != null && url != "") {
                            count.incrementAndGet();
                            String [] urls = url.split("!");
                            mainExecutor.offer(urls[0]);
                        }
                    }
                }
            }
        }
        if (!isStart) {
            mainExecutor.excutoer();
            isStart = true;
        }
        logger.warn("----------------total download count = "+count.get());
    }

    /**
     * 关闭文件下载线程池
     */
    public void shutdown() {
        if (mainExecutor != null) {
            mainExecutor.shutdown();
        }
    }
}
