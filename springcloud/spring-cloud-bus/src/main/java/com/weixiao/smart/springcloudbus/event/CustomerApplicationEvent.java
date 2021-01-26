package com.weixiao.smart.springcloudbus.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-24 13:47.
 */
public class CustomerApplicationEvent extends ApplicationEvent {
    public CustomerApplicationEvent(String msg) {
        super(msg);
    }
}
