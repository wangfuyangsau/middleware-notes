package org.example.middleware.mapper.red;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.middleware.pojo.red.RedDetail;
import org.example.middleware.pojo.red.RedDetailExample;
@Mapper
public interface RedDetailMapper {
    int countByExample(RedDetailExample example);

    int deleteByExample(RedDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RedDetail record);

    int insertSelective(RedDetail record);

    List<RedDetail> selectByExample(RedDetailExample example);

    RedDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RedDetail record, @Param("example") RedDetailExample example);

    int updateByExample(@Param("record") RedDetail record, @Param("example") RedDetailExample example);

    int updateByPrimaryKeySelective(RedDetail record);

    int updateByPrimaryKey(RedDetail record);
}