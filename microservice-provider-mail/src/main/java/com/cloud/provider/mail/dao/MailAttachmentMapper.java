package com.cloud.provider.mail.dao;

import com.cloud.provider.mail.po.MailAttachment;
import com.cloud.provider.mail.po.MailAttachmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MailAttachmentMapper {
    long countByExample(MailAttachmentExample example);

    int deleteByExample(MailAttachmentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MailAttachment record);

    int insertSelective(MailAttachment record);

    List<MailAttachment> selectByExample(MailAttachmentExample example);

    MailAttachment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MailAttachment record, @Param("example") MailAttachmentExample example);

    int updateByExample(@Param("record") MailAttachment record, @Param("example") MailAttachmentExample example);

    int updateByPrimaryKeySelective(MailAttachment record);

    int updateByPrimaryKey(MailAttachment record);
}