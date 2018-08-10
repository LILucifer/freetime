package com.weixiao.smart.webmagic.main.pipeline;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-07-22 19:24.
 */
public class JsonFileModelPipeline extends FilePersistentBase implements PageModelPipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public JsonFileModelPipeline() {
        setPath("/Users/apple_WeiXiao/Downloads/webmagic/jingdong/");
    }

    public JsonFileModelPipeline(String filepath) {
        setPath("filepath");
    }

    @Override
    public void process(Object o, Task task) {
        String filename = path + "data.json";
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(getFile(filename), true));
            printWriter.write(JSON.toJSONString(o));
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("write file error" +JSON.toJSONString(o),  e);
        }

    }
}
