package org.example.middleware.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.example.middleware.config.RabbitMqConfig;
import org.example.middleware.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ManualAckConsumer  implements ChannelAwareMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(ManualAckConsumer.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void onMessage(Message message, Channel channel)  {
        MessageProperties messageProperties = message.getMessageProperties();
        //消息的全局唯一标识
        long deliveryTag = messageProperties.getDeliveryTag();
        try {

            byte[] body = message.getBody();
            MessageDTO messageDTO = objectMapper.readValue(body, MessageDTO.class);
            //收到消息了
            logger.info("收到消息了，进行手动确认");
            //第一个参数为消息的标识，第二个参数为是否允许批量确认
            channel.basicAck(deliveryTag,true);
        } catch (IOException e) {
            e.printStackTrace();
            //如果处理消息的过程中发生了异常，依旧手工确认，因为已经收到消息了，需要确认将队列中的消息消费掉，避免重复消费
            try {
                channel.basicReject(deliveryTag,true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                logger.error("消息拒绝异常");
            }
        }

    }
}
