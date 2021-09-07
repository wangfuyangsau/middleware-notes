package org.example.middleware.service.redpacket;

import org.example.middleware.dto.RedPacketDTO;

import java.math.BigDecimal;

public interface IRedPacketService {
    String handOut(RedPacketDTO dto) throws Exception;

    BigDecimal rob(Integer userId,String redId) throws Exception;
}
