package com.weixiao.smart.service.amq;

import com.weixiao.smart.model.OrderMessage;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * @author lishixiang0925@126.com.
 * @description (消息转换器)
 * @Created 2018-08-30 21:32.
 */
public class ActiveMqMessageConverter  implements MessageConverter {
    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
        ObjectMessage objms = session.createObjectMessage();
        if (o instanceof OrderMessage) {
            objms.setObject((OrderMessage) o);
        }else{
            throw new JMSException("Object:[" + o + "] is not legal message");
        }
        return objms;
    }

    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (message instanceof ObjectMessage) {
            ObjectMessage objms = (ObjectMessage) message;
            Object object = objms.getObject();
            if (object instanceof OrderMessage) {
                return (OrderMessage)object;
            }else{
                throw new JMSException("Msg:[" + message + "] is not legal message");
            }
        }else {
            throw new JMSException("Msg:[" + message + "] is not ObjectMessage");
        }
    }
}
