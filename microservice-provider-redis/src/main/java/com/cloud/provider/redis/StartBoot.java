package com.cloud.provider.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import redis.clients.jedis.JedisSentinelPool;

@SpringBootApplication
//服务发现
@EnableEurekaClient
public class StartBoot {

	//redis哨兵(sentinel) master名称
	@Value("${jedis.sentinelMasterName}")
	public String sentinelMasterName;

	//redis哨兵(sentinel) ip
	@Value("${jedis.sentinelIp}")
	public String sentinelIp;

	//redis哨兵(sentinel) 密码
	@Value("${jedis.sentinelPassword}")
	public String sentinelPassword;

	//最大连接数, 默认8个
	@Value("${jedis.maxTotal}")
	public String maxTotal;

	//最大空闲连接数
	@Value("${jedis.maxIdle}")
	public String maxIdle;

	//最小空闲连接数
	@Value("${jedis.minIdle}")
	public String minIdle;

	//连接时的最大等待毫秒数
	@Value("${jedis.maxWait}")
	public String maxWait;

	//在获取连接的时候检查有效性
	@Value("${jedis.testOnBorrow}")
	public String testOnBorrow;

	//在return给pool时，是否提前进行validate操作
	@Value("${jedis.testOnReturn}")
	public String testOnReturn;

	//空闲时检查有效性
	@Value("${jedis.testWhileIdle}")
	public String testWhileIdle;

	//一个对象至少停留在idle状态的最短时间，大于0
	@Value("${jedis.minEvictableIdleTimeMillis}")
	public String minEvictableIdleTimeMillis;

	@Value("${jedis.timeBetweenEvictionRunsMillis}")
	//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
	public String timeBetweenEvictionRunsMillis;

	//表示idle object evitor每次扫描的最多的对象数
	@Value("${jedis.numTestsPerEvictionRun}")
	public String numTestsPerEvictionRun;

	//超时时间
	@Value("${jedis.timeout}")
	public Integer timeout;

	/**
	 * 使用fastjson
	 * @return HttpMessageConverters
	 */
	@Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
		SerializerFeature[] serializerFeature = new SerializerFeature[]{
			SerializerFeature.PrettyFormat, SerializerFeature.MapSortField,
			SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty
		};
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(serializerFeature);
		fastJsonConfig.setDateFormat(JSON.DEFFAULT_DATE_FORMAT);
		fastConverter.setSupportedMediaTypes(mediaTypes);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
    }

	/**
	 * 启动web安全,允许所有请求,关闭CSRF保护
	 * @author wei.yong
	 */
	@Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
        }
    }

	/**
	 * 获取jedis哨兵连接池
	 * @return JedisSentinelPool
	 */
	@Bean
    public JedisSentinelPool getJedisSentinelPool() {
    	GenericObjectPoolConfig<Object> config = new GenericObjectPoolConfig<Object>();
    	if (StringUtils.isNumeric(maxTotal)) config.setMaxTotal(Integer.parseInt(maxTotal));
    	if (StringUtils.isNumeric(maxIdle)) config.setMaxIdle(Integer.parseInt(maxIdle));
    	if (StringUtils.isNumeric(minIdle)) config.setMinIdle(Integer.parseInt(minIdle));
		if (StringUtils.isNumeric(maxWait)) config.setMaxWaitMillis(Integer.parseInt(maxWait));
		if (StringUtils.isNumeric(minEvictableIdleTimeMillis)) config.setMinEvictableIdleTimeMillis(Long.parseLong(minEvictableIdleTimeMillis));
		if (StringUtils.isNumeric(timeBetweenEvictionRunsMillis)) config.setTimeBetweenEvictionRunsMillis(Long.parseLong(timeBetweenEvictionRunsMillis));
		if (StringUtils.isNumeric(numTestsPerEvictionRun)) config.setNumTestsPerEvictionRun(Integer.parseInt(numTestsPerEvictionRun));

		if ("true".equals(testOnBorrow) || "false".equals(testOnBorrow)) config.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
		if ("true".equals(testOnReturn) || "false".equals(testOnReturn)) config.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
		if ("true".equals(testWhileIdle) || "false".equals(testWhileIdle)) config.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));

		Set<String> sentinels = new HashSet<String>();
		String[] sentinelIps = StringUtils.split(sentinelIp, ",");
		if(sentinelIps != null && sentinelIps.length >0) {
			for (String str : sentinelIps) {
				sentinels.add(str);
			}
		}

		JedisSentinelPool jedisSentinelPool = null;
		if (timeout != null) {
			jedisSentinelPool = new JedisSentinelPool(sentinelMasterName, sentinels, config, timeout, sentinelPassword);
		} else {
			jedisSentinelPool = new JedisSentinelPool(sentinelMasterName, sentinels, config, sentinelPassword);
		}
		return jedisSentinelPool;
    }

    public static void main(String[] args) {
        SpringApplication.run(StartBoot.class, args);
    }

}