package com.weixiao.smart.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-16 14:40.
 */
@Builder
@Data
public class ProxyIpModel {
    /**
     * ip
     */
    private String ip;
    /**
     * 端口好
     */
    private String port;
    /**
     * 类型
     */
    private String type;
    /**
     * 位置
     */
    private String location;
    /**
     * 响应速度
     */
    private String respondingSpeed;
    /**
     * 最后验证时间
     */
    private String lastVerifyTime;

    /**
     * 验证失败次数
     */
    private int failCount = 0;

    /**
     * 验证成功次数
     */
    private int successCount = 0;

    public void increaseSuccessCount(){
        successCount++;
    }
    public void increaseFailCount(){
        failCount++;
    }

}
