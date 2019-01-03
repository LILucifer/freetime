package com.weixiao.smart.download.thread;

import com.weixiao.smart.download.util.DownloadUtil;
import com.weixiao.smart.download.util.ExecutorResult;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * @author lishixiang@echinetech.com.
 * @description (job)
 * @Created 2018-05-13 19:44.
 */
//@Configurable
//@Component
public class DownLoadJob implements Callable<ExecutorResult>  {

    private String url;
    public DownLoadJob(String url) {
        this.url = url;
    }

    @Override
    public ExecutorResult call() throws Exception {
        Boolean success = DownloadUtil.downloadFile(url);

        if (success) {
            System.out.println(" 下载成功----- "+url);
        }else{
            System.out.println(" 下载失败----- "+url);
        }
        ExecutorResult executorResult = new ExecutorResult(url, success);

        return executorResult;
    }
}
