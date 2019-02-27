package com.ochain.eureka.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
//服务发现
@EnableEurekaServer
public class StartBoot {

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

	public static void main(String[] args) {
		SpringApplication.run(StartBoot.class, args);
	}

}