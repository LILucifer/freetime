package com.weixiao.smart.service.impl.amq;

import com.alibaba.fastjson.JSON;
import com.weixiao.smart.service.amq.MessageConsumerService;
import com.weixiao.smart.model.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author lishixiang0925@126.com.
 * @description (消息消费者)
 * @Created 2018-08-30 22:16.
 */
@Service("consumerService")
public class MessageConsumerServiceImpl implements MessageConsumerService {
    private static Logger logger = LoggerFactory.getLogger(MessageConsumerServiceImpl.class);

    public void handleMessage(Object message) {
        if (message instanceof OrderMessage) {
            logger.info("get message from activemq " + JSON.toJSONString(message));
        }
    }
}
