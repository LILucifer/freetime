package com.weixiao.smart.ebidding.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-03-01 21:06.
 */
@Data
@Builder
public class Result<T> {
    /**
     * 结果编码
     */
    private String resCode;
    /**
     * 信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;
}
