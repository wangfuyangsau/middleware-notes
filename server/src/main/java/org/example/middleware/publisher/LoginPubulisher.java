package org.example.middleware.publisher;

import org.example.middleware.dto.LoginEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class LoginPubulisher {
    private static final Logger logger = LoggerFactory.getLogger(LoginPubulisher.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    public void sendMsg(){
        LoginEvent loginEvent = new LoginEvent(this,"debug",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"127.0.0.1");
        publisher.publishEvent(loginEvent);
        logger.info("发送事件了");
    }

}
