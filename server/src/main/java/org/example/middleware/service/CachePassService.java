package org.example.middleware.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.example.middleware.mapper.ItemMapper;
import org.example.middleware.pojo.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class CachePassService {
    private static final Logger logger = LoggerFactory.getLogger(CachePassService.class);

    @Autowired
    ItemMapper itemMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;

    private static  final  String prefix = "item";

    public Item getItemInfo(String itemCode) throws IOException {
     Item item = null;
     String itemKey = prefix+itemCode;
        ValueOperations valueOperations = redisTemplate.opsForValue();

            if(redisTemplate.hasKey(itemKey)){
                Object o = valueOperations.get(itemKey);
                if(o!=null&& !Strings.isNullOrEmpty(o.toString())){
                    logger.info("查找到");
                    item = objectMapper.readValue(o.toString(),Item.class);
                }
            }else{
                logger.info("没有查找到，去数据库查找");
                item = itemMapper.selectByCode(itemCode);
                if(item!=null){
                    logger.info("数据库中有数据");
                    valueOperations.set(itemKey,objectMapper.writeValueAsString(item));
                }else{
                    logger.warn("数据库中没有查找到，默认设置为空值");
                    valueOperations.set(itemKey,"",30L, TimeUnit.SECONDS);
                }

            }

      return  item;
    }


}
