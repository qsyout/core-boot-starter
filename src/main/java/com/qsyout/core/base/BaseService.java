package com.qsyout.core.base;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.qsyout.core.dao.IbatisDao;
import com.qsyout.core.mvc.regist.JsonApiRegistService;

public abstract class BaseService<T> implements InitializingBean {
	
	@Autowired
	protected IbatisDao ibatisDao;
	@Autowired
	private JsonApiRegistService regist;
	
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public abstract Object excute(HttpServletRequest request, T params);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		regist.regist(this);
	}
	
}
