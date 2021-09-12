package org.example.middleware.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.example.middleware.config.RabbitMqConfig;
import org.example.middleware.dto.LoginEvent;
import org.example.middleware.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
@Component
public class ModelPubisher {
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
            //将字符串二进制,设置消息持久化，防止消息丢失
            Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
            rabbitTemplate.convertAndSend(msg);
            logger.info("send msg");
        }

    }

    public void sendModelMsg(MessageDTO message){
        if(message!=null){
            try {
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("mq.basic.info.exchange.name"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.basic.info.routing.key.name"));
                //将字符串二进制
                Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(message)).build();
                rabbitTemplate.convertAndSend(msg);
                logger.info("send msg!!");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendModelMsg2(MessageDTO message){
        if(message!=null){
            try {
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("mq.basic.info.exchange.name"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.basic.info.routing.key2.name"));
                //将字符串二进制
                Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(message)).build();
                rabbitTemplate.convertAndSend(msg);
                logger.info("send msg2!!");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }
    public void sendModelMsgWithRouting(MessageDTO message, String routingKey){
        if(message!=null){
            try {
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("mq.topic.info.exchange.name"));
                rabbitTemplate.setRoutingKey(routingKey);
                //将字符串二进制
                Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(message)).build();
                rabbitTemplate.convertAndSend(msg);
                logger.info("send msg with routingKey!!");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }
}
