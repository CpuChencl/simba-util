package com.simba.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.simba.framework.util.applicationcontext.ApplicationContextUtil;

/**
 * 使用Java NIO的Channel实现的socket server端
 * 
 * @author caozj
 *
 */
public class SocketChannelServerUtil {

	private static final Log logger = LogFactory.getLog(SocketChannelServerUtil.class);

	private static Selector selector = null;

	/**
	 * 已经使用的socket端口
	 */
	private static final Set<Integer> ports = Collections.synchronizedSet(new HashSet<Integer>());

	/**
	 * 已经启动的socket通道
	 */
	private static Map<Integer, ServerSocketChannel> serverChannelMap = Collections
			.synchronizedMap(new HashMap<Integer, ServerSocketChannel>());

	/**
	 * 每个socket服务端对应的返回结果集
	 */
	private static Map<Integer, List<byte[]>> respMap = Collections
			.synchronizedMap(new HashMap<Integer, List<byte[]>>());

	/**
	 * 判断端口是否已经启动socket
	 * 
	 * @param port
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
	 * 获取port的socket服务接收的所有数据
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
	 * 清空port的socket服务接收的所有数据
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
	 * 删除port的socket服务接收的数据
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
	 * 启动socket服务(非阻塞)
	 * 
	 * @param port
	 *            端口号
	 * @throws IOException
	 */
	public static void start(final int port) throws IOException {
		if (selector == null) {
			selector = Selector.open();
		}
		if (isUsedPort(port)) {
			throw new RuntimeException("端口已经使用:" + port);
		}
		ports.add(port);
		List<byte[]> list = Collections.synchronizedList(new ArrayList<byte[]>());
		respMap.put(port, list);
		new Thread(() -> {
			ServerSocketChannel serverSocketChannel = null;
			try {
				logger.info("开始启动socket channel server:" + port);
				serverSocketChannel = ServerSocketChannel.open();
				serverSocketChannel.configureBlocking(false);
				serverSocketChannel.socket().setReuseAddress(true);
				serverSocketChannel.socket().bind(new InetSocketAddress(port));
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
				serverChannelMap.put(port, serverSocketChannel);
				logger.info("启动socket channel server成功:" + port);
				while (selector.select() > 0 && ports.contains(port)) {
					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					while (it.hasNext()) {
						SelectionKey readyKey = it.next();
						it.remove();
						execute((ServerSocketChannel) readyKey.channel(), port);
					}
				}
			} catch (Exception e) {
				logger.error("运行socket服务异常:" + port, e);
			} finally {
				try {
					SocketChannelServerUtil.stop(port);
				} catch (Exception e) {
					logger.error("关闭socket channel server失败:" + port, e);
				}
			}
		}).start();
	}

	private static void execute(ServerSocketChannel channel, int port) throws IOException {
		SocketChannel socketChannel = null;
		try {
			socketChannel = channel.accept();
			byte[] content = receiveData(socketChannel);
			if (content != null) {
				List<byte[]> respList = respMap.get(port);
				respList.add(content);
				respMap.put(port, respList);
			}
			sendData(socketChannel, content, port);
		} finally {
			if (socketChannel != null) {
				socketChannel.close();
			}
		}
	}

	/**
	 * 返回数据给客户端，这里将由业务系统自己补充
	 * 
	 * @param socketChannel
	 * @param content
	 * @param port
	 * @throws IOException
	 */
	private static void sendData(SocketChannel socketChannel, byte[] content, int port) throws IOException {
		SocketResponseInterface si = ApplicationContextUtil.getBean(SocketResponseInterface.class);
		if (si != null) {
			String address = socketChannel.getRemoteAddress().toString();
			String[] hosts = address.split(":");
			String returnContent = si.getReturn(port, hosts[0], NumberUtils.toInt(hosts[1]), new String(content));
			if (StringUtils.isNotEmpty(returnContent)) {
				ByteBuffer buffer = ByteBuffer.wrap(new String(returnContent).getBytes());
				socketChannel.write(buffer);
			}
		}
	}

	private static byte[] receiveData(SocketChannel socketChannel) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] bytes = null;
		try {
			int size = 0;
			while ((size = socketChannel.read(buffer)) >= 0) {
				buffer.flip();
				bytes = new byte[size];
				buffer.get(bytes);
				baos.write(bytes);
				buffer.clear();
			}
			bytes = baos.toByteArray();
		} finally {
			baos.close();
		}
		return bytes;
	}

	/**
	 * 停止运行的socket服务
	 * 
	 * @param port
	 * @throws IOException
	 */
	public static void stop(int port) throws IOException {
		ServerSocketChannel serverChannel = serverChannelMap.get(port);
		if (serverChannel != null) {
			serverChannel.close();
			serverChannelMap.remove(port);
		}
		ports.remove(port);
		if (ports.size() == 0 && selector != null) {
			selector.close();
			selector = null;
		}
	}
}
