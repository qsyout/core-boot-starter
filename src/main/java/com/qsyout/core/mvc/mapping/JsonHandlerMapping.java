package com.qsyout.core.mvc.mapping;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import com.qsyout.core.mvc.intercept.CoreInterceptor;
import com.qsyout.core.util.WebUtil;

@Component
public class JsonHandlerMapping extends AbstractHandlerMapping implements InitializingBean {

	@Autowired
	CoreInterceptor interceptor;
	
	private Map<String, HandlerMethod> apis = new HashMap<String, HandlerMethod>();
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
		if(request.getMethod().equals(RequestMethod.POST.name())){
			return apis.get(WebUtil.getPathWithinApplication(request));
		}
		
		return null;
	}
	
	public void initMethod(String url, HandlerMethod handlerMethod) {
		
		logger.info("初始化接口：{}，请求地址：{}",handlerMethod.getBean(),url);
		synchronized (apis) {
			apis.put(url, handlerMethod);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.setInterceptors(interceptor);
		this.initInterceptors();
		this.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
	
}
