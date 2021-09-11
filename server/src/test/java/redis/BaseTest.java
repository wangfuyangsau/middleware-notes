package redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.middleware.publisher.LoginPubulisher;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForReadableInstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(BaseTest.class)
@ComponentScan("org.example.middleware")
public class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    protected LoginPubulisher pubulisher;
    @Autowired
    protected ObjectMapper objectMapper;

}
