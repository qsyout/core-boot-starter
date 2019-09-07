package com.qsyout.core.mvc.regist;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsyout.core.base.BaseService;
import com.qsyout.core.consts.Core;
import com.qsyout.core.mvc.mapping.JsonHandlerMapping;
import com.qsyout.core.mvc.method.ProxyHandlerMethod;

@Service
public class JsonApiRegistService {
	
	private final String defaultSuffix = "do";
	private final String method = "excute";
	
	@Autowired
	JsonHandlerMapping handlerMapping;

	@SuppressWarnings("rawtypes")
	public void regist(BaseService json){
		try {
			String packageName = json.getClass().getPackage().getName().replaceFirst(Core.API_SUFFIX_PATTERN + ".", "/");
			String clazzName = json.getClass().getSimpleName();
			String url = packageName.replace(".", "/") + "/" + String.valueOf(clazzName.charAt(0)).toLowerCase() + clazzName.substring(1, clazzName.lastIndexOf("Service"))
				+ "." +  defaultSuffix;
			
			Method[] methods = json.getClass().getDeclaredMethods();
			
			for (int i = 0; i < methods.length; i++) {
				if(methods[i].getName().equals(method)&&!methods[i].isBridge()){
					handlerMapping.initMethod(url,new ProxyHandlerMethod(json,methods[i]));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
