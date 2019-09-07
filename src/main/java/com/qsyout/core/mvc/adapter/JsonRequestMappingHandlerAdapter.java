package com.qsyout.core.mvc.adapter;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.qsyout.core.annotation.JsonResult;
import com.qsyout.core.mvc.method.JsonServletInvocableHandlerMethod;
import com.qsyout.core.mvc.resolver.JsonParameterResultResolver;

@Component
public class JsonRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter implements Ordered{

	@Autowired
	RequestMappingHandlerAdapter adapter;
	
	@Autowired
	JsonParameterResultResolver jsonResultResolver;
	
	@Override
	protected boolean supportsInternal(HandlerMethod handlerMethod) {
		return handlerMethod.hasMethodAnnotation(JsonResult.class);
	}

	@Override
	public void afterPropertiesSet() {
		List<HandlerMethodReturnValueHandler> returnValueHandlers = new LinkedList<HandlerMethodReturnValueHandler>();
		returnValueHandlers.add(jsonResultResolver);
		
		this.setReturnValueHandlers(returnValueHandlers);
		
		this.setMessageConverters(adapter.getMessageConverters());
		super.afterPropertiesSet();
	}
	
	@Override
	protected ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
		return new JsonServletInvocableHandlerMethod(handlerMethod, jsonResultResolver);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
	
}
