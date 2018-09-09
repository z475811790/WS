package com.core.util;

/**
 * @author xYzDl
 * @date 2018年1月23日 下午9:23:54
 * @description 字符串工具类
 */
public class StringUtil {
	/**
	 * 判断字符串是否为null或空白串或空的字符串
	 * 
	 * @param string
	 *            源字符串
	 * @return
	 */
	public static Boolean isNullOrWhiteSpaceOrEmpty(String string) {
		if (null == string)
			return true;
		if ("".equals(string))
			return true;
		if ("".equals(string.trim()))
			return true;
		return false;
	}
}
