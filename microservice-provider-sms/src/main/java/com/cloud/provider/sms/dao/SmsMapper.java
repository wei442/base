package com.cloud.provider.sms.dao;

import com.cloud.provider.sms.po.Sms;
import com.cloud.provider.sms.po.SmsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsMapper {
    long countByExample(SmsExample example);

    int deleteByExample(SmsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Sms record);

    int insertSelective(Sms record);

    List<Sms> selectByExample(SmsExample example);

    Sms selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Sms record, @Param("example") SmsExample example);

    int updateByExample(@Param("record") Sms record, @Param("example") SmsExample example);

    int updateByPrimaryKeySelective(Sms record);

    int updateByPrimaryKey(Sms record);
}