package com.simba.udpSocket;

/**
 * UDP收到数据之后，处理接口
 * 
 * @author caozhejun
 *
 */
public interface UdpDealInterface {

	/**
	 * 处理udp socket收到的数据
	 * 
	 * @param localPort
	 *            本地用来接收udp socket数据的端口
	 * @param remoteIp
	 *            远程发送udp socket数据的ip地址
	 * @param remotePort
	 *            远程发送udp socket数据的端口号
	 * @param content
	 *            收到的udp socket数据
	 */
	void deal(int localPort, String remoteIp, int remotePort, byte[] content);

}
