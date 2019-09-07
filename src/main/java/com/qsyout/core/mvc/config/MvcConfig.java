package com.qsyout.core.mvc.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.qsyout.core.annotation.JsonResult;
import com.qsyout.core.base.JsonResultService;
import com.qsyout.core.consts.Core;
import com.qsyout.core.ex.BaseException;
import com.qsyout.core.ex.impl.ArgValidateException;
import com.qsyout.core.ex.impl.BusinessException;
import com.qsyout.core.ex.impl.ForbiddenException;
import com.qsyout.core.mvc.intercept.CoreInterceptor;

@Configuration
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MvcConfig implements WebMvcConfigurer {

	@Autowired
	Environment env;
	@Autowired
	CoreInterceptor interceptor;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}

	@Bean
	@ConditionalOnMissingBean(JsonResultService.class)
	public JsonResultService initJsonResultService() {

		return new JsonResultService() {

			@Override
			public int getNormal() {
				try {
					return Integer.parseInt(env.getProperty("qsyout.status.normal", "1200"));
				} catch (NumberFormatException e) {
				}
				return 1200;
			}
			
			@Override
			public int getStatus(BaseException ex) {
				int status = ex.getStatus();

				try {
					if (ex instanceof ArgValidateException)
						status = Integer.parseInt(env.getProperty("qsyout.status.error.validate", status + ""));
					else if (ex instanceof BusinessException)
						status = Integer.parseInt(env.getProperty("qsyout.status.error.business", status + ""));
					else if (ex instanceof ForbiddenException)
						status = Integer.parseInt(env.getProperty("qsyout.status.error.forbidden", status + ""));
					else
						status = Integer.parseInt(env.getProperty("qsyout.status.error.sesison", status + ""));
				} catch (NumberFormatException e) {
				}
				return status;
			}
			
			@Override
			public int systemError() {
				try {
					return Integer.parseInt(env.getProperty("qsyout.status.error.system", "2500"));
				} catch (NumberFormatException e) {
				}
				return 2500;
			}

			@Override
			public Object deal(Object returnValue, MethodParameter returnType, HttpServletRequest request) {
				JsonResult json = null;

				try {
					json = returnType.getMethodAnnotation(JsonResult.class);
				} catch (Exception e) {
				}

				Map result = new LinkedHashMap(4);

				result.put(Core.RETURN_STATUS, getNormal());

				if (json != null) {
					result.put(Core.IS_INFO, json.info());
					result.put(Core.RETURN_MESSAGE, json.message().toString());
				}

				if (returnValue instanceof BaseException) {
					result.put(Core.IS_INFO, true);
					result.put(Core.RETURN_MESSAGE, ((BaseException) returnValue).getMessage());
					result.put(Core.RETURN_STATUS, getStatus((BaseException) returnValue));
				} else if (returnValue instanceof Exception) {
					result.put(Core.IS_INFO, true);
					result.put(Core.RETURN_MESSAGE, Core.ERROR_MESSAGE);
					result.put(Core.RETURN_STATUS, systemError());
				} else {
					result.put(Core.RETURN_DATA, returnValue);
				}

				request.setAttribute(Core.RETURN_STATUS, result.get(Core.RETURN_STATUS));

				logger.info("Json data response ......" + result);

				return result;
			}

		};
	}
}
