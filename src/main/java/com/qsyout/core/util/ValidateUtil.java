package com.qsyout.core.util;

import java.util.regex.Pattern;

import com.qsyout.core.consts.Core;
import com.qsyout.core.ex.impl.ArgValidateException;

public class ValidateUtil {
	
	/**
	 * Validate that the specified argument is not null;otherwise throwing an exception with the default message. 
	 * @param <T>
	 */
	public static <T> void notNull(T t){
		notNull(t, "Unknown argument is null!");
	}
	
	/**
	 * Validate that the specified argument is not null;otherwise throwing an exception with the specified message. 
	 */
	public static <T> void notNull(T t,String message){
		if(t == null){
			throw new ArgValidateException("ValidateUtil.01", message);
		}
	}
	
	/**
	 * Validate that the specified argument is not null and not blank;otherwise throwing an exception with the default message. 
	 */
	public static void notBlank(String str){
		notBlank(str, "Unknown argument is null or blank!");
	}
	
	/**
	 * Validate that the specified argument is not null and not blank;otherwise throwing an exception with the specified message. 
	 */
	public static void notBlank(String str,String message){
		if(str == null||"".equals(str)){
			throw new ArgValidateException("ValidateUtil.02", message);
		}
	}
	
	/**
	 * Validate that the specified argument character sequence matches the specified regularexpression pattern; otherwise throwing an exception with the default message.
	 */
	public static void match(String regex,String str){
		match(regex, str, "unexpected argument sequence input!");
	}

	/**
	 * Validate that the specified argument character sequence matches the specified regularexpression pattern; otherwise throwing an exception with the specified message. 
	 */
	public static void match(String regex,String str,String message){
		if (str == null||regex == null||!Pattern.matches(str,regex)) {
			throw new ArgValidateException("ValidateUtil.03", message);
		}
	}
	
	/**
	 * Validate that the idCardNo character sequence is legal; otherwise throwing an exception with the default message. 
	 */
	public static void idCardNo(String idCardNo){
		idCardNo(idCardNo, "unexpected IDCardNo sequence input!");
	}
	
	/**
	 * Validate that the idCardNo character sequence is legal; otherwise throwing an exception with the specified message. 
	 */
	public static void idCardNo(String idCardNo,String message){
		match(Core.ID_CARD_NO_REGEX, idCardNo,message);
	}
	
	/**
	 * Validate that the phoneNo character sequence is legal; otherwise throwing an exception with the default message. 
	 */
	public static void mobilePhoneNo(String phoneNo){
		mobilePhoneNo(phoneNo, "unexpected phoneNo sequence input!");
	}
	
	/**
	 * Validate that the phoneNo character sequence is legal; otherwise throwing an exception with the specified message. 
	 */
	public static void mobilePhoneNo(String phoneNo,String message){
		match(Core.PHONE_NO_REGEX, phoneNo,message);
	}
	
	/**
	 * Validate that the landPhoneNo character sequence is legal; otherwise throwing an exception with the default message. 
	 */
	public static void landPhoneNo(String landPhoneNo){
		landPhoneNo(landPhoneNo, "unexpected landPhoneNo sequence input!");
	}
	
	/**
	 * Validate that the landPhoneNo character sequence is legal; otherwise throwing an exception with the specified message. 
	 */
	public static void landPhoneNo(String landPhoneNo,String message){
		match(Core.LAND_PHONE_NO_REGEX, landPhoneNo,message);
	}
	
	/**
	 * Validate that the email character sequence is legal; otherwise throwing an exception with the default message. 
	 */
	public static void email(String email){
		email(email, "unexpected email sequence input!");
	}
	
	/**
	 * Validate that the email character sequence is legal; otherwise throwing an exception with the specified message. 
	 */
	public static void email(String email,String message){
		match(Core.EMAIL_REGEX, email,message);
	}
	
}
