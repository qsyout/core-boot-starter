package com.qsyout.core.mvc.resolver;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.qsyout.core.annotation.JsonResult;
import com.qsyout.core.base.JsonResultService;
import com.qsyout.core.ex.impl.ArgValidateException;

@Component
public class JsonParameterResultResolver extends FastJsonHttpMessageConverter implements HandlerMethodReturnValueHandler,InitializingBean {

	@Autowired
	JsonResultService deal;
	
	@Autowired
    Validator validator;
	
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.hasMethodAnnotation(JsonResult.class);
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);

		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		Assert.state(response != null, "No HttpServletResponse");

		response.setHeader("Content-Type", "application/json;charset=UTF-8");

		super.writeInternal(deal.deal(returnValue, returnType,null), new ServletServerHttpResponse(response));
	}

	
	
	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		
		Object params = super.read(type, contextClass, inputMessage);
		
		Set<ConstraintViolation<Object>> set = validator.validate(params);
		
		if (!set.isEmpty()) {
			for (ConstraintViolation<Object> model : set) {
				throw new ArgValidateException(model.getLeafBean().getClass().getName()+"."+model.getPropertyPath(),"参数("+model.getPropertyPath()+"): "+model.getMessage());
			} 
		}
		
		return params;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<MediaType> fastMediaTypes = new ArrayList<MediaType>();

		FastJsonConfig fastJsonConfig = new FastJsonConfig();

		fastJsonConfig.setSerializerFeatures(
				SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.DisableCircularReferenceDetect);
		
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

		super.setSupportedMediaTypes(fastMediaTypes);
		super.setFastJsonConfig(fastJsonConfig);
	}
	
	
}
