package com.weixiao.smart.ebidding.rabbitmq;

import com.weixiao.smart.ebidding.rabbitmq.properties.RabbitMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.weixiao.smart.ebidding.rabbitmq.constant.MQConstant.MQ_EXCHANGE_TEAM_WORK;
import static com.weixiao.smart.ebidding.rabbitmq.constant.MQConstant.MQ_ROUTING_KEY_TEAM_WORK;

/**
 * @author lishixiang0925@126.com.
 * @description message producer
 * @Created 2019-03-30 20:02.
 */
@Slf4j
@Component
public class Producer implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMqProperties rabbitMqProperties;


    /**
     * 推送MQ消息
     *
     * @param content 推送内容
     */
    public void sendMQ(String content) {
        log.info("produce message : {}", content);
        rabbitTemplate.convertAndSend(MQ_EXCHANGE_TEAM_WORK, MQ_ROUTING_KEY_TEAM_WORK, content, new CorrelationData(rabbitMqProperties.getCorrelationDataId() + ""));
        rabbitTemplate.setConfirmCallback(this);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("信息到达QUEUE! RabbitMq ID ： {}  ", correlationData);
        } else {
            log.info("信息未到达QUEUE:{} ! RabbitMq ID ： {}  ", cause, correlationData);
        }
    }
}
