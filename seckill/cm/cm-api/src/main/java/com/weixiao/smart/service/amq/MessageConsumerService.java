package com.weixiao.smart.service.amq;

/**
 * @author lishixiang0925@126.com.
 * @description (消息消费者)
 * @Created 2018-08-30 21:51.
 */
public interface MessageConsumerService {

    void handleMessage(Object message);
}
