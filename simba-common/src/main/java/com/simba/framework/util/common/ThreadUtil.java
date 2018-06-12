package com.simba.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 线程工具类
 * 
 * @author caozj
 *
 */
public class ThreadUtil {

	private static final Log logger = LogFactory.getLog(ThreadUtil.class);

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			logger.error("休眠失败", e);
		}
	}

}
