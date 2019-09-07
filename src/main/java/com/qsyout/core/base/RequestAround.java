package com.qsyout.core.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface RequestAround {

	boolean checkLogin(HttpServletRequest request) throws Exception;
	boolean checkPermission(HttpSession session,String url) throws Exception;
	void log(String url,String name,String requestIP, int status,HttpServletRequest request,String sn,String localIP,int localPort) throws Exception;
}
