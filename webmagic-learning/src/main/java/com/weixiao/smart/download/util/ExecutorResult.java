package com.weixiao.smart.download.util;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-05-14 07:49.
 */
public class ExecutorResult {

    public ExecutorResult(String url, Boolean success) {
        this.url = url;
        this.success = success;
    }

    private int count = 0; //容错次数
    private String url;
    private Boolean success;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
