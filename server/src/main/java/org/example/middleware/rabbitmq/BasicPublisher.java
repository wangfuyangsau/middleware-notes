package org.example.middleware.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.example.middleware.config.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class BasicPublisher {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Environment env;
    public void sendMsg(String message){
        if(!Strings.isNullOrEmpty(message)){
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(env.getProperty("mq.basic.info.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("mq.basic.info.routing.key.name"));
            //将字符串二进制
            Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).build();
            rabbitTemplate.convertAndSend(msg);
            logger.info("send msg");
        }

    }
}
