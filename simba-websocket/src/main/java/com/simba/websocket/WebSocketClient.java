package com.simba.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * websocket客户端工具类
 * 
 * @author caozhejun
 *
 */
@ClientEndpoint
public class WebSocketClient {

	private CountDownLatch signal = new CountDownLatch(1);

	private static final Log logger = LogFactory.getLog(WebSocketClient.class);

	private Session userSession = null;

	private WebSocketClientInterface client = null;

	public WebSocketClient(String url, WebSocketClientInterface ci) {
		client = ci;
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, new URI(url));
			new Thread(() -> {
				try {
					signal.await();
				} catch (Exception e) {
					logger.error(e);
				}
			}).start();
		} catch (Exception e) {
			logger.error("连接到websocket服务器异常:" + url, e);
		}
	}

	@OnOpen
	public void onOpen(Session userSession) {
		this.userSession = userSession;
		client.onOpen(userSession);

	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		this.userSession = null;
		client.onClose(userSession, reason);
		signal.countDown();
	}

	@OnError
	public void onError(Throwable t) {
		this.userSession = null;
		client.onError(t);
		signal.countDown();
	}

	@OnMessage
	public void onMessage(String message) {
		client.onMessage(message);
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		if (userSession != null) {
			userSession.getBasicRemote().sendText(message);
		} else {
			logger.warn("连接已经关闭");
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (userSession != null) {
			userSession.close();
		}
	}

}
