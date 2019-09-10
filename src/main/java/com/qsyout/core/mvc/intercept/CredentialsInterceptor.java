package com.qsyout.core.mvc.intercept;

import java.net.InetAddress;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.qsyout.core.annotation.Permission;
import com.qsyout.core.consts.Core;
import com.qsyout.core.util.StringUtil;
import com.qsyout.core.util.WebUtil;

@Component
public class CredentialsInterceptor implements HandlerInterceptor {

	private String localIP;
	private int localPort = -1;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = WebUtil.getPathWithinApplication(request);
		
		if(!(handler instanceof HandlerMethod)||url == null||url.startsWith("/error")){
			return true;
		}
		
		HandlerMethod method = (HandlerMethod)handler;
		
		if (localIP == null) {
			localIP = InetAddress.getLocalHost().getHostAddress();
		}
		if (localPort == -1) {
			localPort = request.getLocalPort();
		}
		
		MDC.put(Core.SN, StringUtil.uuid());
		MDC.put("path", (url.substring(0, url.lastIndexOf(".") != -1?url.lastIndexOf("."):url.length())) + "." + localPort);
		
		Permission permission = method.getMethodAnnotation(Permission.class);
		
		if(permission == null){ //直接访问
			return true;
		}
		
		String contentType = request.getHeader(Core.CONTENT_TYPE);
		
		if(request.getSession().getAttribute(Core.LOGIN_MARKED) == null){ // 验证是否已登录
			
			if (contentType != null && contentType.contains(Core.APPLICATION_JSON)) {
				request.getRequestDispatcher(Core.TIME_OUT_MAPPING).forward(request, response);
			}

			if (contentType == null || contentType.contains(Core.TEXT_HTML)) {
				response.sendRedirect(request.getContextPath() + "/");
			}
			
			return false;
		}
		
		List<String> urls = (List<String>) request.getSession().getAttribute(Core.AUTH_URLS);
		if(permission.control()&&!(urls == null||!urls.contains(url))){ // 验证是否具备某些权限
			if (contentType != null && contentType.contains(Core.APPLICATION_JSON))
				request.getRequestDispatcher(Core.FORBIDDEN_MAPPING).forward(request, response);
			return false;
		}
		
		return true;
	}

}
