package com.qsyout.core.credentials.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.qsyout.core.consts.Core;
import com.qsyout.core.credentials.HandleCredentials;

/**
 * 基于自主访问控制（DAC: Discretionary Access Control）
 * 模式：strictly 严格遵循用户具备的权限进行验证
 * 登录时进行权限缓存，对应用户或者角色的权限发生变更后，无法及时更新，用户重新登录后生效
 * @author dangsj
 */
@SuppressWarnings("unchecked")
public class DACredentials implements HandleCredentials {

	@Override
	public boolean handle(HttpSession session, String url) {
		List<String> urls = (List<String>) session.getAttribute(Core.AUTH_URLS);
		if(urls != null&&urls.contains(url)){
			return true;
		}
		return false;
	}

}
