package org.example.middleware.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RabbitMqConfig {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;
    //consumer singleton
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory getListenerFactory(){

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setMaxConcurrentConsumers(1);
        factory.setConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        return factory;
    }

    // multi consumers
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory getMultiListenerFactory(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory,connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //config message make sure consume mode,such as none
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(10);
        factory.setMaxConcurrentConsumers(15);
        factory.setPrefetchCount(10);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                logger.info("msg send successfully:correlationData({}),ack({}),cause({})",correlationData,b,s);
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                logger.info("msg lost:exchange({}),route({}),replyCode({}),replyText({}),message({})",message,i,s,s1,s2);
            }
        });
        return rabbitTemplate;
    }

    @Autowired
    private Environment env;

    @Bean(name ="basicQueue")
    public Queue basicQueue(){
        return new Queue(env.getProperty("mq.basic.info.queue.name"),true);
    }
    @Bean(name ="basicQueue2")
    public Queue basicQueue2(){
        return new Queue(env.getProperty("mq.basic.info.queue2.name"),true);
    }

    @Bean
    public DirectExchange basicExchange(){
        return  new DirectExchange(env.getProperty("mq.basic.info.exchange.name"),true,false);
    }
    @Bean
    public TopicExchange topicExchange(){
        return  new TopicExchange(env.getProperty("mq.topic.info.exchange.name"),true,false);
    }
    @Bean
    public Binding basicBinding(){
        return  new BindingBuilder().bind(basicQueue()).to(topicExchange()).with(env.getProperty("mq.basic.info.routing.key.name"));
    }
    @Bean
    public Binding basicBinding2(){
        return  new BindingBuilder().bind(basicQueue2()).to(topicExchange()).with(env.getProperty("mq.basic.info.routing.key2.name"));
    }


}
