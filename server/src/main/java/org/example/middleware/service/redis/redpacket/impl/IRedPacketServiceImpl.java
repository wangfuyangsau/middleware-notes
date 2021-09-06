package org.example.middleware.service.redis.redpacket.impl;

import com.google.common.base.Strings;
import org.example.middleware.dto.RedPacketDTO;
import org.example.middleware.service.redis.redpacket.IRedPacketService;
import org.example.middleware.service.redis.redpacket.IRedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
        List<Integer> list = new ArrayList<>();
        for(int i=1;i<11;i++){
            list.add(i);
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
        return null;
    }
}
