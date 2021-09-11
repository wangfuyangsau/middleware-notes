package redis;

import org.example.middleware.dto.LoginEvent;
import org.example.middleware.dto.MessageDTO;
import org.example.middleware.rabbitmq.BasicPublisher;
import org.example.middleware.rabbitmq.ModelPubisher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class RabbitMqTest extends BaseTest {
    @Autowired
    private BasicPublisher basicPublisher;
    @Autowired
    private ModelPubisher modelPubisher;
    @Test
    public void test1() throws Exception{
        String msg = "我是消息";
        basicPublisher.sendMsg(msg);
    }
    @Test
    public void testModel1(){
        MessageDTO loginEvent = new MessageDTO();
        modelPubisher.sendModelMsg(loginEvent);
    }
    @Test
    public void testModel2(){
        MessageDTO loginEvent2 = new MessageDTO();
        modelPubisher.sendModelMsg2(loginEvent2);
    }

    @Test
    public void testModelTopicExchange1(){
        String key1= "local.middleware.mq.basic.info.routing.java.key";
        String key2= "local.middleware.mq.basic.info.routing.php.python.key";
        String key3= "local.middleware.mq.basic.info.routing.key";
        MessageDTO loginEvent2 = new MessageDTO();
        loginEvent2.setUserName("topic");
        modelPubisher.sendModelMsgWithRouting(loginEvent2,key2);
    }
}
