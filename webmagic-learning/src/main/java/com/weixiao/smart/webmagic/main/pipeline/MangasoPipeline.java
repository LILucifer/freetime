package com.weixiao.smart.webmagic.main.pipeline;

import com.weixiao.smart.download.MainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-08-01 19:20.
 */
public class MangasoPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private MainExecutor mainExecutor = new MainExecutor();
    private boolean isStart = false ;
    private AtomicInteger count = new AtomicInteger();
    @Override
    public void process(ResultItems resultItems, Task task) {
        logger.warn("get page: " + resultItems.getRequest().getUrl());
        List<String> sets = new ArrayList<String>();
        String imgName = "";
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
//            if ("imageUrlList".equals(entry.getKey())) {
//                sets = (List<String>) entry.getValue();
//            }
//            if ("imageName".equals(entry.getKey())) {
//                imgName = (String) entry.getValue();
//            }
        }
//        int c = 1;
//        for (String url : sets) {
//            if (url != null && url != "") {
//                String name = imgName;
//                if (sets.size() > 1) {
//                    name = imgName + "_" + c;
//                }
//                url = url + "!" + name+getPostfix(url);
//                count.incrementAndGet();
//                mainExecutor.offer(url);
//            }
//        }
//        if (!isStart) {
//            mainExecutor.excutoer();
//            isStart = true;
//        }
        logger.warn("----------------total download count = "+count.get());
    }

    private String getPostfix(String imgUrl) {
        if (imgUrl == null) {
            return null;
        }
        String[] strs = imgUrl.split("\\.");
        return "."+strs[strs.length - 1];
    }

    /**
     * 关闭文件下载线程池
     */
    public void shutdown() {
        if (mainExecutor != null) {
            mainExecutor.shutdown();
        }
    }
    public void setSavePath(String path){
        mainExecutor.setSavePath(path);
    }

}
