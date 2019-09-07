package com.qsyout.core.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import com.qsyout.core.base.FileService;
import com.qsyout.core.base.RequestAround;
import com.qsyout.core.base.impl.DefaultInterceptor;
import com.qsyout.core.base.impl.LocaleFileService;
import com.qsyout.core.consts.Core;
import com.qsyout.core.mvc.intercept.CoreInterceptor;

@Configuration
@ComponentScan("com.qsyout.core")
public class DefaultConfig {
	
	@Autowired
	Environment env;
	@Autowired
	CoreInterceptor interceptor;
	
	@Bean
	@ConditionalOnMissingBean(FileService.class)
	public FileService createDefaultFileService(){
		String[] locations = env.getProperty(Core.STATIC_LOCATIONS, "").split(",");
		
		for (int i = 0; i < locations.length; i++) {
			if(locations[i].startsWith("file:")){
				return new LocaleFileService(locations[i].trim().substring(5));
			}
		}
		
		try {
			return new LocaleFileService(ResourceUtils.getFile("classpath:").getAbsolutePath() + "/static");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Bean
	@ConditionalOnMissingBean(RequestAround.class)
	public RequestAround createRequestAround(){
		return new DefaultInterceptor();
	}
	
}
