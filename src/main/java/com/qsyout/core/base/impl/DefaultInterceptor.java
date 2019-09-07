package com.qsyout.core.base.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.qsyout.core.base.RequestAround;
import com.qsyout.core.consts.Core;
import com.qsyout.core.credentials.HandleCredentials;

public class DefaultInterceptor implements RequestAround {

	@Autowired
	HandleCredentials handleCredentials;
	@Autowired
	Environment env;
	
	@Override
	public boolean checkLogin(HttpServletRequest request) throws Exception {
		return "dev".equals(env.getProperty("spring.profiles.active"))||request.getSession().getAttribute(Core.LOGIN_MARKED) != null;
	}

	@Override
	public boolean checkPermission(HttpSession session, String url) throws Exception {
		return "dev".equals(env.getProperty("spring.profiles.active"))||handleCredentials.handle(session, url);
	}

	@Override
	public void log(String url, String name, String requestIP, int status, HttpServletRequest request, String sn,
			String localIP, int localPort) throws Exception {
		
	}

}
