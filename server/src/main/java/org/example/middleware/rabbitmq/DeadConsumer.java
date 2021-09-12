package org.example.middleware.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.middleware.dto.DeadInfo;
import org.example.middleware.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DeadConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DeadConsumer.class);
    @Autowired
    private Environment env;
    @Autowired
    private ObjectMapper objectMapper;
    @RabbitListener(queues = "${mq.basic.info.queue.name}",containerFactory = "singleListenerContainer")
    public void consumeMsg(@Payload byte[]msg){
        try {
            DeadInfo deadInfo = objectMapper.readValue(msg, DeadInfo.class);
            logger.info("死信收到消息:"+deadInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
