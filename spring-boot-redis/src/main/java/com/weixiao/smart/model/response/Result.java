package com.weixiao.smart.model.response;

import lombok.Data;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-04-22 21:10.
 */
@Data
public class Result {
    private String resultCode;
    private String message;
    private boolean success;
}
