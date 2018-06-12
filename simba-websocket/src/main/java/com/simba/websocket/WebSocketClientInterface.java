package com.simba.websocket;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * websocket客户端接口
 * 
 * @author caozhejun
 *
 */
public interface WebSocketClientInterface {

	/**
	 * 建立连接的时候执行
	 * 
	 * @param userSession
	 */
	public void onOpen(Session userSession);

	/**
	 * 关闭连接的时候执行
	 * 
	 * @param userSession
	 * @param reason
	 */
	public void onClose(Session userSession, CloseReason reason);

	/**
	 * 发生错误的时候执行
	 * 
	 * @param t
	 */
	public void onError(Throwable t);

	/**
	 * 收到消息的时候执行
	 * 
	 * @param message
	 */
	public void onMessage(String message);

}
