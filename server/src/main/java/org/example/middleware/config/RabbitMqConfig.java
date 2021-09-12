package org.example.middleware.config;

import org.example.middleware.rabbitmq.ManualAckConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    @Autowired
    private ManualAckConsumer manualAckConsumer;
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

    @Bean(name = "singleListenerContainerAuto")
    public SimpleRabbitListenerContainerFactory getListenerFactoryAuto(){

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setMaxConcurrentConsumers(1);
        factory.setConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }
    @Bean(name = "singleListenerContainerManual")
    public SimpleMessageListenerContainer getListenerFactoryManual(@Qualifier("basicQueue") Queue queue){

        SimpleMessageListenerContainer factory = new SimpleMessageListenerContainer();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setMaxConcurrentConsumers(1);
        factory.setConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setQueues(queue);
        factory.setMessageListener(manualAckConsumer);
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
        //保证消息发送成功
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

    //死信队列
    @Bean
    public Queue basicDeadQueue(){
        //死信队列包含，死信交换机、死信路由、TTL
        Map<String,Object> args = new HashMap();
        args.put("x-dead-letter-exchange",env.getProperty("mq.dead.exchange.name"));
        args.put("x-dead-letter-routing-key","local.middleware.mq.dead.info.routing.java.key");
        args.put("x-message-ttl",10000);//10s

        return new Queue(env.getProperty("mq.dead.queue.name"),true,false,false,args);
    }
    //死信队列同基本路由的绑定
    @Bean
    public Binding basicBindingDead(){
        return  new BindingBuilder().bind(basicDeadQueue()).to(topicExchange()).with(env.getProperty("mq.basic.info.routing.key.name"));
    }
    //创建死信交换机
    @Bean
    public TopicExchange basicDeadExchange()
    {
        return  new TopicExchange(env.getProperty("mq.dead.exchange.name"),true,false);
    }
    //创建死信路由和真正队列之间的绑定
    @Bean
    public Binding basicDeadBangding(){
        return  new BindingBuilder().bind(basicQueue()).to(basicDeadExchange()).with(env.getProperty("mq.dead.routing.key.name"));

    }


}
