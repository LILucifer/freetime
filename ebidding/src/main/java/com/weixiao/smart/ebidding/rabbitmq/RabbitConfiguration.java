package com.weixiao.smart.ebidding.rabbitmq;

import com.weixiao.smart.ebidding.rabbitmq.properties.RabbitMqProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.weixiao.smart.ebidding.rabbitmq.constant.MQConstant.*;

/**
 * @author lishixiang0925@126.com.
 * @description MQ configuration
 * @Created 2019-03-30 20:06.
 */
//@Configuration
public class RabbitConfiguration {
    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMqProperties.getHost(),
                rabbitMqProperties.getPort());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        return new Queue(MQ_QUEUE_TEAM_WORK, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(MQ_EXCHANGE_TEAM_WORK);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(MQ_ROUTING_KEY_TEAM_WORK);
    }
}
