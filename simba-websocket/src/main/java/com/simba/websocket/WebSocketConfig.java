package com.simba.websocket;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.simba.framework.util.json.FastJsonUtil;

/**
 * websocket配置类
 * 
 * @author caozhejun
 *
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	private static final Log logger = LogFactory.getLog(WebSocketConfig.class);

	@Value("${websocket.config}")
	private String websocket;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		if (StringUtils.isEmpty(websocket)) {
			return;
		}
		String sockJs = "/sockjs";
		List<WebSocketData> list = FastJsonUtil.toList(websocket, WebSocketData.class);
		list.forEach((data) -> {
			String handler = data.getHandler();
			String interceptor = data.getInterceptor();
			String url = data.getUrl();
			try {
				WebSocketHandler handlerObject = (WebSocketHandler) Class.forName(handler).newInstance();
				if (StringUtils.isEmpty(interceptor)) {
					registry.addHandler(handlerObject, url).setAllowedOrigins("*");
					registry.addHandler(handlerObject, sockJs + url).setAllowedOrigins("*").withSockJS();
				} else {
					HttpSessionHandshakeInterceptor iterceptorObject = (HttpSessionHandshakeInterceptor) Class
							.forName(interceptor).newInstance();
					registry.addHandler(handlerObject, url).addInterceptors(iterceptorObject).setAllowedOrigins("*");
					registry.addHandler(handlerObject, sockJs + url).addInterceptors(iterceptorObject)
							.setAllowedOrigins("*").withSockJS();
				}
				logger.info("websocket注册成功:" + data.toString());
			} catch (Exception e) {
				logger.error("初始化websocket失败:" + data.toString(), e);
			}
		});
		logger.info("websocket注册完成");
	}

}
