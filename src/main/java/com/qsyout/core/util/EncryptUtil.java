package com.qsyout.core.util;

import org.apache.shiro.crypto.hash.Md5Hash;

public class EncryptUtil {

	/**
	 * 加密
	 */
	public static String encrypt(String original,String salt){
		Md5Hash md = new Md5Hash(original+salt);
		return md.toString().toUpperCase();
	}
}
