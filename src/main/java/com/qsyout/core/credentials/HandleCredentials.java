package com.qsyout.core.credentials;

import javax.servlet.http.HttpSession;

public interface HandleCredentials {

	boolean handle(HttpSession session,String url);
}
