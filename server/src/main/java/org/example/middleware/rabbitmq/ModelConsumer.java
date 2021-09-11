package org.example.middleware.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.middleware.config.RabbitMqConfig;
import org.example.middleware.dto.LoginEvent;
import org.example.middleware.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
@Component
public class ModelConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);
    @Autowired
    public ObjectMapper objectMapper;
    //监听并消费队列中消息，这里采用单一监听者容器实例
    @RabbitListener(queues = "${mq.basic.info.queue.name}",containerFactory = "singleListenerContainer")
    public void cinsimeMsg(@Payload byte[]msg){
        try {
            MessageDTO loginEvent = objectMapper.readValue(msg, MessageDTO.class);

            logger.info("消息1*:"+loginEvent.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("消息异常1");
        }
    }

    @RabbitListener(queues = "${mq.basic.info.queue2.name}",containerFactory = "singleListenerContainer")
    public void cinsimeMsg2(@Payload byte[]msg){
        try {
            MessageDTO loginEvent = objectMapper.readValue(msg, MessageDTO.class);

            logger.info("消息2#:"+loginEvent.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("消息异常2");
        }
    }

}
