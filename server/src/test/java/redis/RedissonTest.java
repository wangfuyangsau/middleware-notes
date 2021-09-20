package redis;

import org.junit.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class RedissonTest extends BaseTest{
    @Autowired
    private RedissonClient client;
    @Test
    public void test(){
      final String key = "myBloomFilterDataV2";
      Long total =100000L;
        RBloomFilter<Integer> bloomFilter = client.getBloomFilter(key);
        bloomFilter.tryInit(total,0.01);
        for(int i=1;i<=total;i++){
            bloomFilter.add(i);
        }
        logger.info("是否包含1：{}",bloomFilter.contains(1));
        logger.info("是否包含-11：{}",bloomFilter.contains(-11));
        logger.info("是否包含10000：{}",bloomFilter.contains(10000));
        logger.info("是否包含1000000：{}",bloomFilter.contains(1000000));
    }

    @Test
    public void thread(){}

}
