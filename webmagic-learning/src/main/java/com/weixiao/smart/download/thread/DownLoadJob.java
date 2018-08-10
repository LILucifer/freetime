package com.weixiao.smart.download.thread;

import com.weixiao.smart.download.util.DownloadUtil;
import com.weixiao.smart.download.util.ExecutorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author lishixiang@echinetech.com.
 * @description (job)
 * @Created 2018-05-13 19:44.
 */
public class DownLoadJob implements Callable<ExecutorResult>  {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String url;
    private String path;
    private String filename;
    public DownLoadJob(String url) {
        this.url = url;
    }

    public DownLoadJob(String url, String path ,String filename) {
        this.url = url;
        this.path = path;
        this.filename = filename;
    }

    @Override
    public ExecutorResult call() throws Exception {
        Boolean success =false;
        if (path != null && path != "") {
            success = new DownloadUtil(path).downloadFile(url , filename);
        }else{
            success = DownloadUtil.downloadFile(url);
        }


        if (success) {
            logger.warn(" 下载成功----- "+url);
        }else{
            logger.warn(" 下载失败----- "+url);
        }
        ExecutorResult executorResult = new ExecutorResult(url, success);

        return executorResult;
    }
}
