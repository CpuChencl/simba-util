package com.simba.socket;

/**
 * 接收到socket请求之后，返回的内容接口
 * 
 * @author caozhejun
 *
 */
public interface SocketResponseInterface {

	/**
	 * 接收到socket请求之后，返回的信息
	 * 
	 * @param localPort
	 *            本地接收数据的端口号
	 * @param ip
	 *            socket请求的ip地址
	 * @param port
	 *            socket请求的端口号
	 * @param content
	 *            socket请求收到的内容
	 * @return
	 */
	String getReturn(int localPort, String ip, int port, String content);

}
