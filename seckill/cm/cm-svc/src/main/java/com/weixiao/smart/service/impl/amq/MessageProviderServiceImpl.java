package com.weixiao.smart.service.impl.amq;

import com.weixiao.smart.service.amq.MessageProviderService;
import com.weixiao.smart.service.amq.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * @author lishixiang0925@126.com.
 * @description (消息生产者)
 * @Created 2018-08-30 22:18.
 */
public class MessageProviderServiceImpl  implements MessageProviderService{
    private static Logger logger = LoggerFactory.getLogger(MessageProviderServiceImpl.class);

    @Resource
    private JmsTemplate jmsTemplate;
    public void send(final Object model) {
        String destination = jmsTemplate.getDefaultDestinationName().toString();
        logger.info(destination);
        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage((OrderMessage)model);
            }
        };
        jmsTemplate.send(messageCreator);

    }

    public void sendByMessageConverter(Object model) {
        String destination = jmsTemplate.getDefaultDestinationName().toString();
        logger.info(destination);
        jmsTemplate.convertAndSend(model);
    }
}
