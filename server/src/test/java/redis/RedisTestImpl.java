package redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RedisTestImpl.class)
@EnableAutoConfiguration
public class RedisTestImpl{
    private static final Logger logger = LoggerFactory.getLogger(RedisTestImpl.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testAdd(){
        logger.info("test start");
        final String key = "key";
        final String value = "value";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);

        Object o = valueOperations.get(key);
        logger.info("输出内容为{}", o);



    }

}
