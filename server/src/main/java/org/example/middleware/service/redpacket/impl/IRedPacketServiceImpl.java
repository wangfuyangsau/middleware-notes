package org.example.middleware.service.redpacket.impl;

import org.example.middleware.dto.RedPacketDTO;
import org.example.middleware.service.redpacket.IRedPacketService;
import org.example.middleware.service.redpacket.IRedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class IRedPacketServiceImpl implements IRedPacketService {
    private static final String kerPre= "redis:red:packet:";

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private IRedService redService;
    @Override
    public String handOut(RedPacketDTO dto) throws Exception {

        String timsStamp = String.valueOf(System.nanoTime());
        String redId = new StringBuffer(kerPre).append(dto.getUserId()).
                append(":").append(timsStamp).toString();
        //红包的随机金额，这里就平均分吧
        Integer amount = dto.getAmount();
        Integer total = dto.getTotal();
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<total;i++){
            list.add(amount/total);
        }
        ListOperations listOperations = redisTemplate.opsForList();

        listOperations.leftPushAll(redId,list);

        String redTotalKey = redId+":total";

        redisTemplate.opsForValue().set(redTotalKey,dto.getTotal());

        // 异步记录红包的全局唯一表示、个数、随机金额列表
        redService.recordRedpacket(dto,redId,list);
        return  redId;



    }

    @Override
    public BigDecimal rob(Integer userId, String redId) throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object o = valueOperations.get(redId + userId + ":rob");
        //用户已经抢过了，直接返回金额
        if(o!=null){
            return  new BigDecimal(o.toString());
        }
        boolean click = click(redId);
        if(click){
            //有，从列表中去一个,上分布式锁，保证用户只能抢到1次
            String lockKey = redId+userId+"-lock";
            Boolean lock = valueOperations.setIfAbsent(lockKey, redId);
            redisTemplate.expire(lockKey,24L,TimeUnit.HOURS);
            try {
                if(lock){
                    Object value = redisTemplate.opsForList().rightPop(redId);
                    if(value!=null){
                        //有钱
                        String redTotalKey = redId+":total";
                        Integer currToal =valueOperations.get(redTotalKey)!=null?(Integer)valueOperations.get(redTotalKey):0;
                        valueOperations.set(redTotalKey,currToal-1);
                        valueOperations.set(redId+userId+":rob",value.toString(),24L, TimeUnit.HOURS);
                        redService.recordRobRedPacket(userId,redId,new BigDecimal(value.toString()));
                        return new BigDecimal(value.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw  new Exception("加分布式锁失败");
            }


        }
       return  null;
    }

    private boolean click(String redId) throws Exception{
        ValueOperations valueOperations = redisTemplate.opsForValue();
            String redTotalKey = redId+":total";
        Object o = valueOperations.get(redTotalKey);
        if(o!=null&&Integer.valueOf(o.toString())>0){
            return  true;
        }
        else{
            return false;
        }
    }
}
