package com.weixiao.smart.service.amq;

import com.alibaba.fastjson.JSON;
import com.weixiao.smart.model.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @author lishixiang0925@126.com.
 * @description (消息监听器)
 * @Created 2018-08-30 20:55.
 */
public class TopicMessagesListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(TopicMessagesListener.class);
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objMessage = (ObjectMessage) message;
            try {
                Object obj = objMessage.getObject();
                if (obj instanceof OrderMessage) {
                    OrderMessage orderMessage = (OrderMessage) obj;
                    logger.info("consumer get message and deal with : "+ JSON.toJSONString(orderMessage));
                }else{

                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        logger.info("get mq message ");
    }
}
