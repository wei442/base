package com.cloud.provider.mail.vo.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.cloud.common.dateformat.DateFormatConstants;
import com.cloud.provider.mail.po.MailAttachment;
import com.google.common.base.Converter;

import lombok.Data;

@Data
public class MailAttachmentVo implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Integer mailAttachmentId;

    private Integer mailId;

    private String fileName;

    private String filePath;

    @JSONField(format=DateFormatConstants.DF_YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    @JSONField(format=DateFormatConstants.DF_YYYY_MM_DD_HH_MM_SS)
    private Date updateTime;

    /**
     * 实体转换
     * @param mailAttachment
     * @return MailAttachmentVo
     */
    public MailAttachmentVo convertToMailAttachmentVo(MailAttachment mailAttachment) {
    	MailAttachmentVoConvert convert = new MailAttachmentVoConvert();
    	return convert.doBackward(mailAttachment);
	}

    /**
     * 实体列表转换
     * @param list
     * @return List<MailAttachmentVo>
     */
    public List<MailAttachmentVo> convertToMailAttachmentVoList(List<MailAttachment> list) {
    	MailAttachmentVoConvert convert = new MailAttachmentVoConvert();
    	List<MailAttachmentVo> mailAttachmentVoList = null;
    	MailAttachmentVo mailAttachmentVo = null;
    	if(list != null && !list.isEmpty()) {
    		mailAttachmentVoList = new ArrayList<MailAttachmentVo>(list.size());
    		ListIterator<MailAttachment> it = list.listIterator();
    		while(it.hasNext()) {
    			MailAttachment mailAttachment = it.next();
    			mailAttachmentVo = convert.doBackward(mailAttachment);
    			mailAttachmentVoList.add(mailAttachmentVo);
    		}
    	}
    	return mailAttachmentVoList;
    }

	/**
	 * 实体转换
	 * @author wei.yong
	 */
    private class MailAttachmentVoConvert extends Converter<MailAttachmentVo, MailAttachment> {

    	@Override
    	protected MailAttachment doForward(MailAttachmentVo mailAttachmentVo) {
    		return null;
    	}

    	/**
    	 * 实体转换vo
    	 * @param mailAttachment
    	 * @return MailAttachmentVo
    	 */
		@Override
		protected MailAttachmentVo doBackward(MailAttachment mailAttachment) {
			MailAttachmentVo mailAttachmentVo = new MailAttachmentVo();
			BeanUtils.copyProperties(mailAttachment, mailAttachmentVo);
			mailAttachmentVo.setMailAttachmentId(mailAttachment.getId());
			return mailAttachmentVo;
		}

    }

}