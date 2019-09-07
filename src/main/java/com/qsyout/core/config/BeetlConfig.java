package com.qsyout.core.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.qsyout.core.base.ContextViewValRegist;

@Configuration
public class BeetlConfig {

	@Autowired
	Environment env;
	@Autowired(required=false)
	ContextViewValRegist regist;

	@Bean
	public ClasspathResourceLoader createClasspathResourceLoader() {
		ClasspathResourceLoader loader = new ClasspathResourceLoader("views", "UTF-8");
		loader.setAutoCheck("dev".equals(env.getProperty("spring.profiles.active", "prod")));
		return loader;
	}

	@Bean(initMethod = "init")
	public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration(ClasspathResourceLoader loader) {
		BeetlGroupUtilConfiguration config = new BeetlGroupUtilConfiguration();
		config.setResourceLoader(loader);
		if (regist != null) {
			Map<String, Object> sharedVars = new HashMap<String, Object>();
			regist.regist(sharedVars);
			config.setSharedVars(sharedVars);
		}
		return config;
	}

	@Bean
	public BeetlSpringViewResolver createGroupTemplate(BeetlGroupUtilConfiguration config) throws IOException {
		BeetlSpringViewResolver resolver = new BeetlSpringViewResolver();

		resolver.setContentType("text/html;charset=UTF-8");
		resolver.setOrder(0);
		resolver.setConfig(config);

		return resolver;
	}
}
