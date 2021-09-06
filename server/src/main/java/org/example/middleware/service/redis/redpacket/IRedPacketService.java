package org.example.middleware.service.redis.redpacket;

import org.example.middleware.dto.RedPacketDTO;

import java.math.BigDecimal;

public interface IRedPacketService {
    String handOut(RedPacketDTO dto) throws Exception;

    BigDecimal rob(Integer userId,String redId) throws Exception;
}
