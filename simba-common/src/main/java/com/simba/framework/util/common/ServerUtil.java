package com.simba.framework.util.common;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务器工具类
 * 
 * @author caozj
 * 
 */
public class ServerUtil {

	/**
	 * 获取代理服务器IP. .
	 * 
	 * @param request
	 * @return
	 */
	public static String getProxyIp(HttpServletRequest request) {
		String proxyIp = request.getHeader("X-Real-IP");
		if (proxyIp == null) {
			proxyIp = request.getHeader("RealIP");
		}
		if (proxyIp == null) {
			proxyIp = request.getRemoteAddr();
		}
		return proxyIp;
	}

}
