package com.qsyout.core.forward;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qsyout.core.annotation.JsonResult;
import com.qsyout.core.consts.Core;
import com.qsyout.core.ex.impl.ForbiddenException;
import com.qsyout.core.ex.impl.SessionInvalidException;

@Controller
public class ForwardController {

	@RequestMapping(Core.TIME_OUT_MAPPING)
	@JsonResult
	public void timeout(HttpServletRequest request){
		throw new SessionInvalidException("ForwardController.01","登录失效;");
	}

	@RequestMapping(Core.FORBIDDEN_MAPPING)
	@JsonResult
	public void forbidden(){
		throw new ForbiddenException("ForwardController.02","无权限的操作!");
	}

}
