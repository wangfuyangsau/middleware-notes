package org.example.middleware.listener;

import org.example.middleware.dto.LoginEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class EventListener implements ApplicationListener<LoginEvent> {
    private static final Logger logger = LoggerFactory.getLogger(EventListener.class);
    @Override
    @Async
    public void onApplicationEvent(LoginEvent loginEvent) {
              logger.info("接受到消息{}",loginEvent);
    }
}
