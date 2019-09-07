package com.qsyout.core.util;

import java.util.UUID;

public class StringUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
    
    public static boolean isNull(String str){
    	return str == null;
    }
    
    public static boolean isNotNull(String str){
    	return str != null;
    }
    
    public static boolean isBlank(String str){
    	return str == null||"".equals(str.trim());
    }
    
    public static boolean isNotBlank(String str){
    	return str != null&&!"".equals(str.trim());
    }
}
