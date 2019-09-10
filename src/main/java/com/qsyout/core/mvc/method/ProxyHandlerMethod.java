package com.qsyout.core.mvc.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;

import com.qsyout.core.annotation.JsonResult;
import com.qsyout.core.annotation.Permission;

public class ProxyHandlerMethod extends HandlerMethod {
	
	HandlerMethod proxyMethod;
	MethodParameter[] methodParameters;
	
	public ProxyHandlerMethod(Object bean, Method method) {
		super(bean, method);
		try {
			proxyMethod = new HandlerMethod(this,this.getClass().getMethod("proxy", HttpServletRequest.class,Object.class));
			methodParameters = super.getMethodParameters();
			methodParameters[1] = new ProxyMethodParameter(methodParameters[1],this);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
	}
	
	@Permission
	@JsonResult
	public final Object proxy(HttpServletRequest request,@RequestBody Object params){
		return null;
	};

	@Override
	public MethodParameter getReturnValueType(Object returnValue) {//clone MethodParameter新对象
		return this.methodParameters[1];
	}

	@Override
	public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
		if(!super.hasMethodAnnotation(annotationType)&&hasProxyMethodAnnotation(annotationType)){
			return proxyMethod.getMethodAnnotation(annotationType);
		}
		
		return super.getMethodAnnotation(annotationType);
	}

	@Override
	public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
		return hasProxyMethodAnnotation(annotationType)||super.hasMethodAnnotation(annotationType);
	}
	
	private <A> boolean hasProxyMethodAnnotation(Class<A> annotationType) {
		if(annotationType.getTypeName().equals("com.qsyout.core.annotation.JsonResult")){
			return true;
		}
		return false;
	}
	
	@Override
	public MethodParameter[] getMethodParameters() {
		return methodParameters;
	}
	
	public static class ProxyMethodParameter extends MethodParameter{
		
		ProxyHandlerMethod proxyHandlerMethod;

		public ProxyMethodParameter(MethodParameter original,ProxyHandlerMethod proxyHandlerMethod) {
			super(original);
			this.proxyHandlerMethod = proxyHandlerMethod;
		}

		@Override
		public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
			if(annotationType.getTypeName().equals("org.springframework.web.bind.annotation.RequestBody")){
				return true;
			}
			
			return super.hasParameterAnnotation(annotationType);
		}

		@Override
		public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
			return this.proxyHandlerMethod.getMethodAnnotation(annotationType);
		}
		
	}

}
