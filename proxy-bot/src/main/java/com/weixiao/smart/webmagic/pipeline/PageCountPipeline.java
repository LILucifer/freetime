package com.weixiao.smart.webmagic.pipeline;


import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-13 22:05.
 */
@Slf4j
public class PageCountPipeline implements Pipeline {
    private static int pageCount = 0;

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if ("pageCount".equals(entry.getKey())) {
                pageCount = Integer.parseInt(entry.getValue().toString());
//                System.out.println("total page size = "+entry.getValue().toString());
                log.info("total page size = {}",entry.getValue().toString());
            }
        }
    }

    public  int getPageCount() {
        return pageCount;
    }
}
