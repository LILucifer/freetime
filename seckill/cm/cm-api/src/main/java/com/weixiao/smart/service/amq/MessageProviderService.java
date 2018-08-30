package com.weixiao.smart.service.amq;

/**
 * @author lishixiang0925@126.com.
 * @description (消息生产者)
 * @Created 2018-08-30 21:51.
 */
public interface MessageProviderService {
    /**
     * 发送消息到AMQ
     * 未使用MessageConverter 转换成标准Message格式消息
     * @param model
     */
    void send(Object model);
    /**
     * 发送消息到AMQ
     * 使用MessageConverter 转换成标准Message格式消息
     * @param model
     */
    void sendByMessageConverter(Object model);
}
