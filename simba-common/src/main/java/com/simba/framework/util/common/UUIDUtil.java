package com.simba.framework.util.common;

import java.util.UUID;

/**
 * UUID工具类
 * 
 * @author caozhejun
 *
 */
public class UUIDUtil {

	/**
	 * 获取一个UUID
	 * 
	 * @return
	 */
	public static String get() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
