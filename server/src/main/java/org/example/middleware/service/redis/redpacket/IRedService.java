package org.example.middleware.service.redis.redpacket;

import org.example.middleware.dto.RedPacketDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IRedService {
    void recordRedpacket(RedPacketDTO dto, String redId, List<Integer> list) throws Exception;

    void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception;

}
