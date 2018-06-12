package com.simba.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.simba.framework.util.applicationcontext.ApplicationContextUtil;

/**
 * socket服务端工具类
 * 
 * @author caozj
 * 
 */
public class SocketServerUtil {

	private static final Log logger = LogFactory.getLog(SocketServerUtil.class);

	private static final int timeout = 60 * 1000;

	/**
	 * 已经使用的socket端口
	 */
	private static final Set<Integer> ports = Collections.synchronizedSet(new HashSet<Integer>());

	/**
	 * 每个socket服务端对应的返回结果集
	 */
	private static Map<Integer, List<String>> respMap = Collections.synchronizedMap(new HashMap<Integer, List<String>>());

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
	public static List<String> getResp(int port) {
		List<String> resp = respMap.get(port);
		if (resp == null) {
			resp = new ArrayList<String>(0);
		}
		return resp;
	}

	/**
	 * 清空port的socket服务接收的所有数据
	 * 
	 * @param port
	 */
	public static void removeAll(int port) {
		List<String> resp = respMap.get(port);
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
	public static void remove(int port, String resp) {
		List<String> respList = respMap.get(port);
		if (respList != null) {
			respList.remove(resp);
		}
	}

	/**
	 * 启动socket服务(阻塞)
	 * 
	 * @param port
	 *            端口号
	 * @throws IOException
	 */
	public static void start(final int port) throws IOException {
		if (isUsedPort(port)) {
			throw new RuntimeException("端口已经使用:" + port);
		}
		ports.add(port);
		final ServerSocket serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(timeout);
		List<String> list = Collections.synchronizedList(new ArrayList<String>());
		respMap.put(port, list);
		new Thread(new Runnable() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				logger.info("启动socket服务:" + port);
				BufferedReader in = null;
				PrintWriter out = null;
				while (ports.contains(port)) {
					try {
						// 阻塞,直到有客户端连接
						Socket socket = serverSocket.accept();
						// 设置IO句柄
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
						StringBuilder resp = new StringBuilder();
						while (true) {
							String line = in.readLine();
							if (StringUtils.isBlank(line)) {
								break;
							}
							resp.append(line);
						}
						List<String> respList = respMap.get(port);
						String responseContent = resp.toString();
						respList.add(responseContent);
						respMap.put(port, respList);
						SocketResponseInterface si = ApplicationContextUtil.getBean(SocketResponseInterface.class);
						if (si != null) {
							String remoteIp = socket.getInetAddress().getHostAddress();
							int remotePort = socket.getPort();
							String returnContent = si.getReturn(port, remoteIp, remotePort, responseContent);
							if (StringUtils.isNotEmpty(returnContent)) {
								out.print(returnContent);
							}
						}
						out.flush();
						Thread.currentThread().sleep(1000);
					} catch (SocketTimeoutException e) {
						logger.warn("达到socket server接收时间，仍未收到任何socket请求");
					} catch (IOException e) {
						logger.error("接收客户端请求失败", e);
					} catch (InterruptedException e) {
						logger.error("休眠失败", e);
					} catch (Exception e) {
						logger.error("接收客户端请求失败", e);
					} finally {
						IOUtils.closeQuietly(out);
						IOUtils.closeQuietly(in);
					}

				}
				try {
					serverSocket.close();
				} catch (IOException e) {
					logger.error("关闭socket服务端异常", e);
				}
				logger.info("结束socket服务:" + port);
			}
		}).start();
	}

	/**
	 * 停止运行的socket服务(不会立刻停止，要等socket.accept执行完之后，才会停止)
	 * 
	 * @param port
	 */
	public static void stop(int port) {
		ports.remove(port);
	}

}
