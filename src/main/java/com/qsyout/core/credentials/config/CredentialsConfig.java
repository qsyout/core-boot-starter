package com.qsyout.core.credentials.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qsyout.core.credentials.HandleCredentials;
import com.qsyout.core.credentials.impl.DACredentials;
import com.qsyout.core.credentials.impl.RBACredentials;

/**
 * 默认启用rbac型权限验证
 * 或者根据注解com.qsyout.credentials.type判断使用提供的权限验证
 * @author dangsj
 */
@Configuration
public class CredentialsConfig {

	@Bean
	@ConditionalOnProperty(prefix="com.qsyout.credentials",name="type",havingValue="dac")
	public HandleCredentials createDACHandleCredentials(){
		return new DACredentials();
	}
	
	@Bean(initMethod="init")
	@ConditionalOnMissingBean(HandleCredentials.class)
	public HandleCredentials createRBACHandleCredentials(){
		return new RBACredentials();
	}
}
