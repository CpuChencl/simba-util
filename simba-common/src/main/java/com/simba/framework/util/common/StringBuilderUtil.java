package com.simba.framework.util.common;

/**
 * 字符处理工具类
 * 
 * @author caozj
 */
public class StringBuilderUtil {

	private final StringBuilder sb;

	public StringBuilderUtil() {
		sb = new StringBuilder();
	}

	/**
	 * 多参数构建StringBuilder
	 * 
	 * @param objects
	 */
	public StringBuilderUtil(Object... objects) {
		this();
		append(objects);
	}

	/**
	 * 多参数连接StringBuilder
	 * 
	 * @param objects
	 */
	public void append(Object... objects) {
		if (objects == null || objects.length <= 0) {
			return;
		}
		for (Object obj : objects) {
			sb.append(obj);
		}
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
