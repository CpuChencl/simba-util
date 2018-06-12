package com.simba.websocket;

/**
 * websocket配置数据
 * 
 * @author caozhejun
 *
 */
public class WebSocketData {

	/**
	 * websocket请求的url地址
	 */
	private String url;

	/**
	 * 处理类全路径
	 */
	private String handler;

	/**
	 * 拦截器
	 */
	private String interceptor;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(String interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebSocketData [url=");
		builder.append(url);
		builder.append(", handler=");
		builder.append(handler);
		builder.append(", interceptor=");
		builder.append(interceptor);
		builder.append("]");
		return builder.toString();
	}

}
