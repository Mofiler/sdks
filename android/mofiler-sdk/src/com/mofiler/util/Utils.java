package com.mofiler.util;

public class Utils {

    public static boolean isNullOrWhitespace(String strtocheck){
        return !(strtocheck != null && strtocheck.trim().length() > 0 );
    }
    
    public static String getSafeString(String strtocheck){
    	if (strtocheck == null)
    		return "";
    	else
    		return strtocheck.trim();
    }
    
}
