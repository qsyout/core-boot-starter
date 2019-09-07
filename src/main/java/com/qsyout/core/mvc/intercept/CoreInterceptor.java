package com.qsyout.core.mvc.intercept;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.qsyout.core.annotation.UserPermission;
import com.qsyout.core.base.RequestAround;
import com.qsyout.core.consts.Core;
import com.qsyout.core.entity.RequestModel;
import com.qsyout.core.util.StringUtil;
import com.qsyout.core.util.WebUtil;

@Component
public class CoreInterceptor implements HandlerInterceptor {

	@Autowired
	RequestAround interceptor;
	
	private Map<HandlerMethod, RequestModel> cache = new HashMap<HandlerMethod, RequestModel>();
	
	private String localIP;
	private int localPort = -1;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = WebUtil.getPathWithinApplication(request);
		
		if(!(handler instanceof HandlerMethod)||url == null||url.startsWith("/error")){
			return true;
		}
		
		if (localIP == null) {
			localIP = InetAddress.getLocalHost().getHostAddress();
		}
		if (localPort == -1) {
			localPort = request.getLocalPort();
		}
		
		MDC.put(Core.SN, StringUtil.uuid());
		MDC.put("path", (url.substring(0, url.lastIndexOf(".") != -1?url.lastIndexOf("."):url.length())) + "." + localPort);
		
		String ip = WebUtil.getRealIpAddress(request);
		
		request.setAttribute(Core.REQUEST_URL, url);
		request.setAttribute(Core.REQUEST_IP, ip);
		
		if(interceptor != null){
			
			RequestModel model = null;
			
			if(cache.containsKey(handler)){
				model = cache.get(handler);
			}
			
			if(model == null){
				model = new RequestModel(url);
				UserPermission permission = ((HandlerMethod) handler).getMethodAnnotation(UserPermission.class);
				
				if(permission != null){
					model.setControl(permission.control());
					model.setLog(permission.log());
					model.setName(permission.name());
					model.setOpen(permission.open());
				}
				
				cache.put((HandlerMethod) handler, model);
			}
			
			if(model.isOpen()){
				return true;
			}
			
			if(!interceptor.checkLogin(request)){
				String contentType = request.getHeader(Core.CONTENT_TYPE);

				if (contentType != null && contentType.contains(Core.APPLICATION_JSON)) {
					request.getRequestDispatcher(Core.TIME_OUT_MAPPING).forward(request, response);
				}

				if (contentType == null || contentType.contains(Core.TEXT_HTML)) {
					response.sendRedirect(request.getContextPath() + "/");
				}
				
				return false;
			}
			
			if(model.isControl()){
				if(!interceptor.checkPermission(request.getSession(),url)){
					request.getRequestDispatcher(Core.FORBIDDEN_MAPPING).forward(request, response);
					return false;
				}
			}
			
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		RequestModel model = cache.get(handler);
		
		if(model != null){
			int status = response.getStatus();
			try {
				status = (int) request.getAttribute(Core.RETURN_STATUS);
			} catch (Exception e) {
			}
			if(model.isLog()){
				interceptor.log((String)request.getAttribute(Core.REQUEST_URL), model.getName(), 
						(String)request.getAttribute(Core.REQUEST_IP), status, request, MDC.get(Core.SN), 
						localIP, localPort);
			}
		}
		
	}

}
