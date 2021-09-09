package redis;

import org.example.middleware.publisher.LoginPubulisher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EventTest extends BaseTest{
    @Autowired
    private LoginPubulisher pubulisher;
    @Test
    public void testEvent(){
           pubulisher.sendMsg();
    }
}
