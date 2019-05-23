package com.weixiao.smart.ebidding.rabbitmq.receiver;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.weixiao.smart.ebidding.rabbitmq.constant.MQConstant.MQ_QUEUE_TEAM_WORK;

/**
 * @author lishixiang0925@126.com.
 * @description 测试channel.basicReject
 * @Created 2019-03-31 15:42.
 */
@Slf4j
@Component
//@RabbitListener(queues = {MQ_QUEUE_TEAM_WORK})
public class RejectReceiver {
    @RabbitHandler
    public void process(String content, Channel channel, Message message) {
        try {
            if ((int) (Math.random() * 100)%2 == 0) {
                log.info("RejectReceiver 接收处理队列 {} 当中的消息：{} ", MQ_QUEUE_TEAM_WORK, content);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false );
            }else {
                log.info("RejectReceiver 接收处理队列 {} 重新排队 当中的消息：{} ", MQ_QUEUE_TEAM_WORK, content);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }

        } catch (Exception e) {
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

}
