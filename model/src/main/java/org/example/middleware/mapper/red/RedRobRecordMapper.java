package org.example.middleware.mapper.red;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.middleware.pojo.red.RedRobRecord;
import org.example.middleware.pojo.red.RedRobRecordExample;
@Mapper
public interface RedRobRecordMapper {
    int countByExample(RedRobRecordExample example);

    int deleteByExample(RedRobRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RedRobRecord record);

    int insertSelective(RedRobRecord record);

    List<RedRobRecord> selectByExample(RedRobRecordExample example);

    RedRobRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RedRobRecord record, @Param("example") RedRobRecordExample example);

    int updateByExample(@Param("record") RedRobRecord record, @Param("example") RedRobRecordExample example);

    int updateByPrimaryKeySelective(RedRobRecord record);

    int updateByPrimaryKey(RedRobRecord record);
}