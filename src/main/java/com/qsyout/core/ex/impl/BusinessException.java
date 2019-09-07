package com.qsyout.core.ex.impl;

import com.qsyout.core.ex.BaseException;

public class BusinessException extends BaseException {

	private static final long serialVersionUID = -2818697319968029177L;

	public BusinessException(String key, String message) {
		super(key, message);
	}

	@Override
	public int getStatus() {
		return 1500;
	}

}
