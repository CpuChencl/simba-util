package com.simba.framework.util.common;

import org.apache.commons.lang.StringUtils;

/**
 * 批量替换字符串中的某个特殊字符的工具类
 * 
 * @author caozj
 * 
 */
public class ReplaceUtil {

	/**
	 * 替换字符串
	 * 
	 * @param content
	 * @param flag
	 * @param params
	 * @return
	 */
	public static String replace(String content, String flag, String... params) {
		for (String param : params) {
			if (param == null) {
				param = StringUtils.EMPTY;
			}
			int index = content.indexOf(flag);
			if (index < 0) {
				break;
			}
			content = content.substring(0, index) + param + content.substring(index + 1, content.length());
		}
		return content;
	}

	/**
	 * 替换字符串中的问号
	 * 
	 * @param content
	 * @param params
	 * @return
	 */
	public static String replaceQustion(String content, String... params) {
		return replace(content, "?", params);
	}

}
