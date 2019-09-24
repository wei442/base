package com.cloud.provider.sms.vo.sms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.cloud.common.dateformat.DateFormatConstants;
import com.cloud.provider.sms.po.Sms;
import com.google.common.base.Converter;

import lombok.Data;

@Data
public class SmsVo implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Integer smsId;

    private String mobile;

    private String content;

    private Integer status;

    private Integer smsType;

    private Date sendTime;

    private String source;

    @JSONField(format=DateFormatConstants.DF_YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    @JSONField(format=DateFormatConstants.DF_YYYY_MM_DD_HH_MM_SS)
    private Date updateTime;

    /**
     * 实体转换
     * @param sms
     * @return SmsVo
     */
    public SmsVo convertToSmsVo(Sms sms) {
    	SmsVoConvert convert = new SmsVoConvert();
    	return convert.doBackward(sms);
	}

    /**
     * 实体列表转换
     * @param list
     * @return List<SmsVo>
     */
    public List<SmsVo> convertToSmsVoList(List<Sms> list) {
    	SmsVoConvert convert = new SmsVoConvert();
    	List<SmsVo> smsVoList = null;
    	SmsVo smsVo = null;
    	if(list != null && !list.isEmpty()) {
    		smsVoList = new ArrayList<SmsVo>(list.size());
    		ListIterator<Sms> it = list.listIterator();
    		while(it.hasNext()) {
    			Sms sms = it.next();
    			smsVo = convert.doBackward(sms);
    			smsVoList.add(smsVo);
    		}
    	}
    	return smsVoList;
    }

	/**
	 * 实体转换
	 * @author wei.yong
	 */
    private class SmsVoConvert extends Converter<SmsVo, Sms> {

    	@Override
    	protected Sms doForward(SmsVo smsVo) {
    		return null;
    	}

    	/**
    	 * 实体转换vo
    	 * @param sms
    	 * @return SmsVo
    	 */
		@Override
		protected SmsVo doBackward(Sms sms) {
			SmsVo smsVo = new SmsVo();
			BeanUtils.copyProperties(sms, smsVo);
			smsVo.setSmsId(sms.getId());
			return smsVo;
		}

    }

}