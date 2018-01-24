package com.bsit.common.utils;

import java.util.Random;

public class StringUtil {
	public static boolean isEmpty(Object s) {
		if(null==s){
			return true;
		}
		else if ( "".equals(s.toString()) || "".equals(s.toString().trim()) || "null".equalsIgnoreCase(s.toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 验证码
	 * @return
	 */
	public static String verificationCode() {
		Random ran = new Random();
		return (ran.nextInt() + "").substring(1, 5);
	}
	
	public static String toHtml(String src){
        String htmlStr = "";
	    for ( int i = 0 ; i != src.length () ; i++ ) {
	        int str = src.codePointAt (i);
	        htmlStr = htmlStr + "&#" + str + ";";
	    }
	    return htmlStr;
	}
	
}
