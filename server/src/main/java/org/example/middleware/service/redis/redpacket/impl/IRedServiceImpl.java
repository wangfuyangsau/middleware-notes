package org.example.middleware.service.redis.redpacket.impl;

import org.example.middleware.dto.RedPacketDTO;
import org.example.middleware.mapper.red.RedDetailMapper;
import org.example.middleware.mapper.red.RedRecordMapper;
import org.example.middleware.mapper.red.RedRobRecordMapper;
import org.example.middleware.pojo.red.RedDetail;
import org.example.middleware.pojo.red.RedRecord;
import org.example.middleware.service.redis.redpacket.IRedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Service
public class IRedServiceImpl  implements IRedService {

    @Autowired
    private RedRecordMapper redRecordMapper;
    @Autowired
    private RedDetailMapper redDetailMappper;
    @Autowired
    private RedRobRecordMapper redRobRecordMapper;
    @Override
    @Async
    @Transactional
    public void recordRedpacket(RedPacketDTO dto, String redId, List<Integer> list) throws Exception {
        RedRecord redRecord = new RedRecord();
        redRecord.setUserId(dto.getUserId());
        redRecord.setRedPacket(redId);
        redRecord.setTotal(dto.getTotal());
        redRecord.setAmount(BigDecimal.valueOf(dto.getAmount()));
        redRecord.setCreateTime(new Date());

        redRecordMapper.insertSelective(redRecord);
        RedRecord redRecord1 = redRecordMapper.selectIdByredId(redId);
        RedDetail detail;
        for(Integer i:list){
            detail = new RedDetail();
            detail.setRecordId(redRecord1.getId());
            detail.setAmount(BigDecimal.valueOf(i));
            detail.setCreateTime(new Date());
            redDetailMappper.insertSelective(detail);
        }
    }
    @Async
    @Override
    public void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception {

    }
}
