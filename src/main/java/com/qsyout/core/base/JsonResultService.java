package com.qsyout.core.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;

import com.qsyout.core.ex.BaseException;

public interface JsonResultService {

	Object deal(Object returnValue,MethodParameter returnType, HttpServletRequest request);
	
	int getNormal();
	int systemError();
	
	int getStatus(BaseException ex);
}
