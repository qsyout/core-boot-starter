package com.qsyout.core.mvc.method;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

public class JsonServletInvocableHandlerMethod extends ServletInvocableHandlerMethod {

	private HandlerMethodReturnValueHandler returnValueHandler;
	
	HandlerMethod handlerMethod;

	public JsonServletInvocableHandlerMethod(HandlerMethod handlerMethod,HandlerMethodReturnValueHandler returnValueHandler) {
		super(handlerMethod);
		this.handlerMethod = handlerMethod;
		this.returnValueHandler = returnValueHandler;
	}
	
	@Override
	public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer,
			Object... providedArgs) throws Exception {
		Object returnValue = null;
		Exception exception = null;
		
		try {
			returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
			setResponseStatus(webRequest);
			
			mavContainer.setRequestHandled(false);
			
			Assert.state(this.returnValueHandler != null, "No return value handlers");
		} catch (Exception e) {
			e.printStackTrace();
			exception = e;
		} finally {
			try {
				if(exception != null){
					returnValue = exception;
				}
				this.returnValueHandler.handleReturnValue(
						returnValue, this.handlerMethod.getReturnValueType(returnValue), mavContainer, webRequest);
			}
			catch (Exception ex) {
				if (logger.isTraceEnabled()) {
					logger.trace(getReturnValueHandlingErrorMessage("Error handling return value", returnValue), ex);
				}
				throw ex;
			}
		}
	}
	
	private String getReturnValueHandlingErrorMessage(String message, @Nullable Object returnValue) {
		StringBuilder sb = new StringBuilder(message);
		if (returnValue != null) {
			sb.append(" [type=").append(returnValue.getClass().getName()).append("]");
		}
		sb.append(" [value=").append(returnValue).append("]");
		return getDetailedErrorMessage(sb.toString());
	}
	
	private void setResponseStatus(ServletWebRequest webRequest) throws IOException {
		HttpStatus status = getResponseStatus();
		if (status == null) {
			return;
		}

		HttpServletResponse response = webRequest.getResponse();
		if (response != null) {
			String reason = getResponseStatusReason();
			if (StringUtils.hasText(reason)) {
				response.sendError(status.value(), reason);
			}
			else {
				response.setStatus(status.value());
			}
		}

		// To be picked up by RedirectView
		webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, status);
	}
	
	

}
