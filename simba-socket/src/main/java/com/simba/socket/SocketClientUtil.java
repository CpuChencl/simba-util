package com.simba.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * socket客户端工具类
 * 
 * @author caozj
 * 
 */
public class SocketClientUtil {

	private static final Log logger = LogFactory.getLog(SocketClientUtil.class);

	private static final int TIMEOUT = 60 * 1000;

	private static final int FILETIMEOUT = 60000;

	/**
	 * 发送文件
	 * 
	 * @param host
	 *            主机名称或IP
	 * @param port
	 *            端口
	 * @param file
	 *            文件
	 */
	public static void sendFile(String host, int port, File file) {
		String content = null;
		try {
			content = FileUtils.readFileToString(file);
		} catch (IOException e) {
			logger.error("读取文件失败", e);
		}
		sendContent(host, port, content, FILETIMEOUT);
	}

	/**
	 * 发送socket
	 * 
	 * @param host
	 *            主机名称或IP
	 * @param port
	 *            端口
	 * @param content
	 *            指令内容
	 */
	public static void sendContent(String host, int port, String content) {
		sendContent(host, port, content, TIMEOUT);
	}

	/**
	 * 发送socket
	 * 
	 * @param host
	 *            主机名称或IP
	 * @param port
	 *            端口
	 * @param content
	 *            指令内容
	 * @param timeout
	 *            超时时间
	 */
	public static void sendContent(String host, int port, String content, int timeout) {
		Socket s = null;
		PrintWriter out = null;
		try {
			s = new Socket(host, port);
			s.setSoTimeout(timeout);
			out = new PrintWriter(s.getOutputStream());
			out.write(content);
			out.flush();
		} catch (Exception e) {
			logger.error("发送内容失败", e);
		} finally {
			closeSocket(s);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 向SocketServer发送通信指令并获取回复数据
	 * 
	 * @param host
	 *            主机名称或IP
	 * @param port
	 *            端口
	 * @param content
	 *            指令内容
	 * 
	 * @return
	 */
	public static String sendAndGetReply(String host, int port, String content) {
		return sendAndGetReply(host, port, content, TIMEOUT);
	}

	/**
	 * 向SocketServer发送通信指令并获取回复数据
	 * 
	 * @param host
	 *            主机名称或IP
	 * @param port
	 *            端口
	 * @param content
	 *            指令内容
	 * @param timeout
	 *            超时时间
	 * 
	 * @return
	 */
	public static String sendAndGetReply(String host, int port, String content, int timeout) {
		Socket s = null;
		BufferedReader in = null;
		PrintWriter out = null;
		String line = null;
		StringBuilder resp = new StringBuilder();
		try {
			s = new Socket(host, port);
			s.setSoTimeout(timeout);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
			out.write(content);
			out.flush();
			s.shutdownOutput();
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (StringUtils.isNotBlank(line = in.readLine())) {
				resp.append(line);
			}
		} catch (Exception e) {
			logger.error("发送内容失败", e);
		} finally {
			closeSocket(s);
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
		return resp.toString();
	}

	/**
	 * 关闭socket
	 * 
	 * @param s
	 */
	private static void closeSocket(Socket s) {
		if (s != null) {
			try {
				s.close();
			} catch (IOException e) {
				logger.error("关闭socket失败", e);
			}
		}
	}
}
