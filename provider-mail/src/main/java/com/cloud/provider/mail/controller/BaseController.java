package com.cloud.provider.mail.controller;

import java.util.HashSet;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制基类 BaseController
 * @author wei.yong
 */
@RestController
public class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	//邮箱主机
	@Value("${spring.mail.host}")
	protected String mailHost;

	//邮箱用户名
	@Value("${spring.mail.username}")
	protected String mailUsername;

	//邮箱密码
	@Value("${spring.mail.password}")
	protected String mailPassword;

	//mail发件人显示名称
	@Value("${mail.personal}")
	protected String mailPersonal;

	//逗号
	public static final String COMMOA = ",";

	 //分号
  	public static final String SEMICOLON = ";";

	/**
  	 * 转换邮件收件地址
  	 * @param to
  	 * @return InternetAddress[]
  	 */
	public InternetAddress[] convertInternetAddress(String to) {
		String[] commTo = StringUtils.split(to, COMMOA);
		Set<InternetAddress> addressSet = new HashSet<InternetAddress>();
		for (String comm : commTo) {
			String[] semiTo = StringUtils.split(comm, SEMICOLON);
			for (String semi : semiTo) {
				try {
					addressSet.add(new InternetAddress(semi));
				} catch (AddressException e) {
					logger.error("(BaseController-convertInternetAddress)-转换邮件收件地址-异常, Exception = {}, message = {}", e, e.getMessage());
				}
			}
		}

		InternetAddress[] internetAddresss = addressSet.toArray(new InternetAddress[]{});
		return internetAddresss;
	}

}