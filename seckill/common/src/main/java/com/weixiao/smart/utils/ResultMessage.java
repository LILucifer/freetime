package com.weixiao.smart.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description (消息处理)
 * @Created 2018-09-02 17:42.
 */
public class ResultMessage  implements Serializable{
    private static final long serialVersionUID = -8425095408173704368L;

    /**
     * 成功
     */
    private static final int SUCCESS = 1;
    /**
     * 失败
     */
    private static final int FAIL = 0;
    /**
     * 结果
     */
    private int result = 1;
    /**
     * 消息
     */
    private String message = "";
    /**
     * 返回信息
     */
    private Map<String, Object> map;

    public ResultMessage() {
    }

    public ResultMessage(int result, String message) {
        this.result = result;
        this.message = message;
    }

    public ResultMessage(int result, String message, Map<String, Object> map) {
        this.result = result;
        this.message = message;
        this.map = map;
    }


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "ResultMessage{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", map=" + map +
                '}';
    }
}
