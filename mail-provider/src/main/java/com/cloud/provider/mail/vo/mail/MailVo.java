package com.cloud.provider.mail.vo.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.cloud.common.dateformat.DateFormatConstants;
import com.cloud.provider.mail.po.Mail;
import com.google.common.base.Converter;

import lombok.Data;

@Data
public class MailVo implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Integer mailId;

    private String userName;

    private String password;

    private String host;

    private String mailFrom;

    private String mailTo;

    private String mailCc;

    private String mailBcc;

    private String subject;

    private String content;

    private Integer status;

    private Integer mailType;

    private Date sendTime;

    private String source;

    @JSONField(format=DateFormatConstants.DF_YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    @JSONField(format=DateFormatConstants.DF_YYYY_MM_DD_HH_MM_SS)
    private Date updateTime;

    /**
     * 实体转换
     * @param mail
     * @return MailVo
     */
    public MailVo convertToMailVo(Mail mail) {
    	MailVoConvert convert = new MailVoConvert();
    	return convert.doBackward(mail);
	}

    /**
     * 实体列表转换
     * @param list
     * @return List<MailVo>
     */
    public List<MailVo> convertToMailVoList(List<Mail> list) {
    	MailVoConvert convert = new MailVoConvert();
    	List<MailVo> mailVoList = null;
    	MailVo mailVo = null;
    	if(list != null && !list.isEmpty()) {
    		mailVoList = new ArrayList<MailVo>(list.size());
    		ListIterator<Mail> it = list.listIterator();
    		while(it.hasNext()) {
    			Mail mail = it.next();
    			mailVo = convert.doBackward(mail);
    			mailVoList.add(mailVo);
    		}
    	}
    	return mailVoList;
    }

	/**
	 * 实体转换
	 * @author wei.yong
	 */
    private class MailVoConvert extends Converter<MailVo, Mail> {

    	@Override
    	protected Mail doForward(MailVo mailVo) {
    		return null;
    	}

    	/**
    	 * 实体转换vo
    	 * @param mail
    	 * @return MailVo
    	 */
		@Override
		protected MailVo doBackward(Mail mail) {
			MailVo mailVo = new MailVo();
			BeanUtils.copyProperties(mail, mailVo);
			mailVo.setMailId(mail.getId());
			return mailVo;
		}

    }

}