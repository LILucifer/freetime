package com.weixiao.smart.ebidding.rabbitmq.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lishixiang0925@126.com.
 * @description MQ 配置项
 * @Created 2019-03-30 20:08.
 */
@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Component
public class RabbitMqProperties {
    /**
     * mq ip
     */
    private String host;
    /**
     * 端口号
     */
    private int port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 连接密码
     */
    private String password;
    /**
     *
     */
    private String addresses;

    /**
     * 消息ID CorrelationData
     */
    private AtomicInteger integer = new AtomicInteger();

    public Integer getCorrelationDataId(){
        return integer.getAndIncrement();
    }
}
