package org.example.middleware.mapper.red;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.middleware.pojo.red.RedRecord;
import org.example.middleware.pojo.red.RedRecordExample;
@Mapper
public interface RedRecordMapper {
    int countByExample(RedRecordExample example);

    int deleteByExample(RedRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RedRecord record);

    int insertSelective(RedRecord record);
    RedRecord selectIdByredId( String redId);

    List<RedRecord> selectByExample(RedRecordExample example);

    RedRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RedRecord record, @Param("example") RedRecordExample example);

    int updateByExample(@Param("record") RedRecord record, @Param("example") RedRecordExample example);

    int updateByPrimaryKeySelective(RedRecord record);

    int updateByPrimaryKey(RedRecord record);
}