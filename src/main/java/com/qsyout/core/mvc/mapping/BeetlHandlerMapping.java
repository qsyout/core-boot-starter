package com.qsyout.core.mvc.mapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.qsyout.core.annotation.UserPermission;
import com.qsyout.core.mvc.intercept.CoreInterceptor;
import com.qsyout.core.util.StringUtil;
import com.qsyout.core.util.WebUtil;

@Component
public class BeetlHandlerMapping extends AbstractHandlerMapping implements InitializingBean {

	@Autowired
	CoreInterceptor interceptor;
	@Autowired
	RequestMappingHandlerMapping requestMappingHandlerMapping;

	private List<String> views = new ArrayList<String>();
	
	private HandlerMethod method;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void afterPropertiesSet() throws Exception { // 初始化beetl视图路径
		this.setInterceptors(interceptor);
		this.initInterceptors();
		this.setOrder(requestMappingHandlerMapping.getOrder() + 1);
		
		this.initMethod();
		
		method = new HandlerMethod(new ViewController(), "view", HttpServletRequest.class);
	}

	private void initMethod() {
		ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = resolver.getResources("classpath*:/views/**");
			if(resources != null){
				for (Resource resource : resources) { 
					recursion(resource.getFile());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void recursion(File dir){
		if(dir.isDirectory()){
			File[] children = dir.listFiles();
			for (File child : children) {
				recursion(child);
			}
		}else{
			file2Url(dir);
		}
	}
	
	private void file2Url(File file){
		String path = file.getPath();
		String url = null;
		try {
			url = path.substring(path.indexOf("\\views") + 6).replaceAll("\\\\", "/");
		} catch (Exception e) {
		}
		if(!views.contains(url)&&StringUtil.isNotBlank(url)){
			views.add(url);
			logger.info("初始化beetl请求地址：{}",url);
		}
	}

	@Override
	protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
		if(request.getMethod().equals(RequestMethod.GET.name())&&views.contains(WebUtil.getPathWithinApplication(request))){
			return method;
		}
		return null;
	}
	
	public static class ViewController{
		
		@UserPermission(open=false,log=true)
		public String view(HttpServletRequest request){
			return WebUtil.getPathWithinApplication(request);
		}
	}

}
