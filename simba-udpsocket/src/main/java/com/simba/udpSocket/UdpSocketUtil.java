package com.simba.udpSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * udp类型的socket工具类
 * 
 * @author caozhejun
 *
 */
public class UdpSocketUtil {

	private static final int timeout = 60 * 1000;

	private static final Log logger = LogFactory.getLog(UdpSocketUtil.class);

	/**
	 * 已经使用的socket端口
	 */
	private static final Set<Integer> ports = Collections.synchronizedSet(new HashSet<Integer>());

	/**
	 * 端口对应的socket服务对象
	 */
	private static final Map<Integer, DatagramSocket> dsMap = new HashMap<>();

	/**
	 * 判断端口是否已经启动socket
	 * 
	 * @param port
	 *            端口号
	 * @return
	 */
	public static boolean isUsedPort(int port) {
		return ports.contains(port);
	}

	/**
	 * 获取所有已经使用的socket服务端口
	 * 
	 * @return
	 */
	public static Set<Integer> getAllPorts() {
		return ports;
	}

	/**
	 * 启动socket服务
	 * 
	 * @param port
	 *            端口号
	 * @throws SocketException
	 */
	public static void start(int port) throws SocketException {
		if (isUsedPort(port)) {
			throw new RuntimeException("端口已经使用:" + port);
		}
		DatagramSocket ds = new DatagramSocket(port);
		ds.setSoTimeout(timeout);
		dsMap.put(port, ds);
	}

	/**
	 * 停止socket服务
	 * 
	 * @param port
	 *            端口号
	 */
	public static void stop(int port) {
		DatagramSocket ds = dsMap.get(port);
		if (ds != null) {
			ds.close();
		}
		ports.remove(port);
		dsMap.remove(port);
	}

	/**
	 * 发送udp数据包
	 * 
	 * @param port
	 *            用来发送udp数据包的端口号
	 * @param content
	 *            数据
	 * @param destIp
	 *            目标ip
	 * @param destPort
	 *            目标端口号
	 * @throws IOException
	 */
	public static void send(int port, byte[] content, String destIp, int destPort) throws IOException {
		DatagramSocket ds = dsMap.get(port);
		if (ds == null) {
			throw new RuntimeException("端口还未启动:" + port);
		}

		DatagramPacket dp = new DatagramPacket(content, content.length, new InetSocketAddress(destIp, destPort));
		dp.setData(content);
		ds.send(dp);
	}

	/**
	 * 接收udp数据包(该方法会造成线程阻塞)
	 * 
	 * @param port
	 *            接收端口号
	 * @param length
	 *            接收数据长度
	 * @return
	 * @throws IOException
	 */
	public static DatagramPacket receive(int port, int length) {
		DatagramPacket packet = null;
		DatagramSocket ds = dsMap.get(port);
		if (ds == null) {
			throw new RuntimeException("端口还未启动:" + port);
		}
		try {
			byte[] buffer = new byte[length];
			packet = new DatagramPacket(buffer, buffer.length);
			ds.receive(packet);
		} catch (SocketTimeoutException e) {
			logger.warn("达到udp socket server接收时间，仍未收到任何udp socket请求");
		} catch (IOException e) {
			logger.error("接收udp socket请求出现异常", e);
		}
		return packet;
	}

}
