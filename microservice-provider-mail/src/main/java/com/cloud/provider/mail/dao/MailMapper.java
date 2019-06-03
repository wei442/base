package com.cloud.provider.mail.dao;

import com.cloud.provider.mail.po.Mail;
import com.cloud.provider.mail.po.MailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MailMapper {
    long countByExample(MailExample example);

    int deleteByExample(MailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Mail record);

    int insertSelective(Mail record);

    List<Mail> selectByExample(MailExample example);

    Mail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Mail record, @Param("example") MailExample example);

    int updateByExample(@Param("record") Mail record, @Param("example") MailExample example);

    int updateByPrimaryKeySelective(Mail record);

    int updateByPrimaryKey(Mail record);
}