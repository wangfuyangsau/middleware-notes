package org.example.middleware.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.middleware.config.RabbitMqConfig;
import org.example.middleware.dto.DeadInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
@Component
public class DeadPublisher {
    private static final Logger logger = LoggerFactory.getLogger(DeadPublisher.class);

    @Autowired
    private Environment env;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMsg(DeadInfo info,String routingKey){
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setExchange(env.getProperty("mq.topic.info.exchange.name"));
        rabbitTemplate.setRoutingKey(routingKey);
        Message msg = null;
        try {
            //将字符串二进制
            msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(info)).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MessageProperties messageProperties = msg.getMessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        //设置消息的TTL
        messageProperties.setExpiration(String.valueOf(10000));
        rabbitTemplate.convertAndSend(msg);
        logger.info("死信发送");

    }
}
