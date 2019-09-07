package com.qsyout.core.credentials.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.qsyout.core.consts.Core;
import com.qsyout.core.credentials.HandleCredentials;

/**
 * 基于角色的权限访问控制（Role-Based Access Control）
 * 模式：strictly 严格模式 严格遵循基于角色的权限控制
 * 当角色的权限发生变化则权限验证实时生效
 * @author dangsj
 */
@SuppressWarnings("unchecked")
public class RBACredentials implements HandleCredentials {
	
	@Autowired(required=false)
	CacheManager cacheManager;
	
	private final String RBAC_CACHE = "rbac";
	
	@Override
	public boolean handle(HttpSession session, String url) {
		if(session.getAttribute(Core.ROLE_ID) == null){
			return false;
		}
		
		if(cacheManager != null){
			Cache cache = cacheManager.getCache(RBAC_CACHE);
			if(cache != null){
				List<String> urls = cache.get(session.getAttribute(Core.ROLE_ID), List.class);
				if(urls != null&&urls.contains(url)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 将所有角色id的权限进行缓存
	 */
	public void init(){
		System.err.println("init方法执行中……");
	}

}
