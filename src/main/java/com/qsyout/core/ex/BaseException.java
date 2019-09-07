package com.qsyout.core.ex;

public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 9084157563719537050L;

	public String key;
	public String message;
	
	public abstract int getStatus();
	
	@Override
	public String getMessage(){
		return message;
	}

	public BaseException(String key, String message) {
		super();
		this.key = key;
		this.message = message;
	}
}
