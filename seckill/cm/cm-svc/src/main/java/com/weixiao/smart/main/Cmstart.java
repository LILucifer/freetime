package com.weixiao.smart.main;

import com.weixiao.smart.service.amq.MessageProviderService;
import com.weixiao.smart.model.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-08-28 21:17.
 */
public class Cmstart {
    private static Logger logger = LoggerFactory.getLogger(Cmstart.class);
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-context.xml");
        context.start();
        logger.warn("cm-svc was started");
        try {
            for(;;){Thread.sleep(Long.MAX_VALUE);}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*MessageProviderService providerService = (MessageProviderService) context.getBean("providerService");
        for(int i= 0 ; i<10 ;i++){
            OrderMessage orderMessage = new OrderMessage();
            orderMessage.setCommodityId("----"+i);
            orderMessage.setUserId("me-"+i);
            orderMessage.setCount(3+i);
            providerService.send(orderMessage);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }
}
