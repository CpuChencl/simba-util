package com.simba.udpSocket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.simba.framework.util.applicationcontext.ApplicationContextUtil;

/**
 * NIO的UDP Socket Server 工具类
 * 
 * @author caozhejun
 *
 */
public class NIOUdpServerUtil {

	private static final Log logger = LogFactory.getLog(NIOUdpServerUtil.class);

	private static Selector selector = null;

	/**
	 * 已经使用的udp socket端口
	 */
	private static final Set<Integer> ports = Collections.synchronizedSet(new HashSet<Integer>());

	/**
	 * 已经启动的udp socket通道
	 */
	private static Map<Integer, DatagramChannel> serverChannelMap = Collections.synchronizedMap(new HashMap<Integer, DatagramChannel>());

	/**
	 * 每个udp socket服务端对应的返回结果集
	 */
	private static Map<Integer, List<byte[]>> respMap = Collections.synchronizedMap(new HashMap<Integer, List<byte[]>>());

	/**
	 * 判断端口是否已经启动udp socket
	 * 
	 * @param port
	 * @return
	 */
	public static boolean isUsedPort(int port) {
		return ports.contains(port);
	}

	/**
	 * 获取所有已经使用的udp socket服务端口
	 * 
	 * @return
	 */
	public static Set<Integer> getAllPorts() {
		return ports;
	}

	/**
	 * 获取port的udp socket服务接收的所有数据
	 * 
	 * @param port
	 * @return
	 */
	public static List<byte[]> getResp(int port) {
		List<byte[]> resp = respMap.get(port);
		if (resp == null) {
			resp = new ArrayList<byte[]>(0);
		}
		return resp;
	}

	/**
	 * 清空port的udp socket服务接收的所有数据
	 * 
	 * @param port
	 */
	public static void removeAll(int port) {
		List<byte[]> resp = respMap.get(port);
		if (resp != null) {
			resp.clear();
		}
	}

	/**
	 * 删除port的udp socket服务接收的数据
	 * 
	 * @param port
	 * @param resp
	 */
	public static void remove(int port, byte[] resp) {
		List<byte[]> respList = respMap.get(port);
		if (respList != null) {
			respList.remove(resp);
		}
	}

	/**
	 * 启动udp socket服务(非阻塞)
	 * 
	 * @param port
	 * @throws IOException
	 */
	public static void start(final int port) throws IOException {
		if (isUsedPort(port)) {
			throw new RuntimeException("端口已经使用:" + port);
		}
		new Thread(() -> {
			try {
				logger.info("开始启动udp socket channel server:" + port);
				List<byte[]> list = Collections.synchronizedList(new ArrayList<byte[]>());
				respMap.put(port, list);
				Selector selector = Selector.open();
				DatagramChannel channel = DatagramChannel.open();
				channel.configureBlocking(false);
				DatagramSocket socket = channel.socket();
				socket.bind(new InetSocketAddress(port));
				channel.register(selector, SelectionKey.OP_READ);
				serverChannelMap.put(port, channel);
				logger.info("启动udp socket channel server成功:" + port);
				ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
				while (ports.contains(port)) {
					int n = selector.select();
					if (n == 0) {
						continue;
					}
					Set<SelectionKey> readyKeys = selector.selectedKeys();
					for (SelectionKey key : readyKeys) {
						readyKeys.remove(key);
						if (key.isReadable()) {
							DatagramChannel dc = (DatagramChannel) key.channel();
							InetSocketAddress client = (InetSocketAddress) dc.receive(receiveBuffer); // 接收来自任意一个Client的数据报
							key.interestOps(SelectionKey.OP_READ);
							byte[] content = receiveBuffer.array();
							if (content != null) {
								List<byte[]> respList = respMap.get(port);
								respList.add(content);
								respMap.put(port, respList);
							}
							UdpDealInterface ui = ApplicationContextUtil.getBean(UdpDealInterface.class);
							if (ui != null) {
								String clientIp = client.getAddress().getHostAddress();
								int clientPort = client.getPort();
								ui.deal(port, clientIp, clientPort, content);
							}
							receiveBuffer.clear();
						}
					}
				}
			} catch (IOException e) {
				logger.error("运行udp socket服务异常:" + port, e);
			} finally {
				try {
					NIOUdpServerUtil.stop(port);
				} catch (Exception e) {
					logger.error("关闭udp socket channel server失败:" + port, e);
				}
			}
		}).start();
		ports.add(port);
	}

	/**
	 * 停止udp socket服务
	 * 
	 * @param port
	 * @throws IOException
	 */
	public static void stop(int port) throws IOException {
		DatagramChannel channel = serverChannelMap.get(port);
		if (channel != null) {
			channel.close();
			serverChannelMap.remove(port);
		}
		ports.remove(port);
		if (ports.size() == 0 && selector != null) {
			selector.close();
			selector = null;
		}
	}

}
