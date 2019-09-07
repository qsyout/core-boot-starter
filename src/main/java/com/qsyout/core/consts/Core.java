package com.qsyout.core.consts;

public interface Core {
	
	String SN = "sn";
	String LOCAL_PATH = "localPath";
	String REQUEST_IP = "requestIP";
	String REQUEST_URL = "requestUrl";
	String API_PREFIX_PATH = "com.qsyout.modules";
	
	String CONTENT_TYPE = "content-type";
	String APPLICATION_JSON = "json";
	String TEXT_HTML = "text/html";
	
	String TIME_OUT_MAPPING = "/forward/timeout.core";
	String FORBIDDEN_MAPPING = "/forward/forbidden.core";
	
	String STATIC_LOCATIONS = "spring.resources.static-locations";
	
	String RETURN_STATUS = "status";
	String IS_INFO = "info";
	String RETURN_MESSAGE = "message";
	String ERROR_MESSAGE = "系统错误";
	String RETURN_DATA = "data";
	
	String AUTH_URLS = "authUrls";
	String ROLE_ID = "roleID";
	String LOGIN_MARKED = "loginMarked";
	
	/**
	 * 身份证号正则
	 */
	String ID_CARD_NO_REGEX = "^$|(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";
	/**
	 * 国内手机号码正则
	 */
	String PHONE_NO_REGEX = "^$|(0?(13|14|15|18|17)[0-9]{9})";
	/**
	 * 国内固定电话号码正则
	 */
	String LAND_PHONE_NO_REGEX = "^$|(\\d{3}-\\d{8}|\\d{4}-\\{7,8})";
	/**
	 * email正则
	 */
	String EMAIL_REGEX = "^$|([\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?)";
}
