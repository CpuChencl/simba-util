package com.simba.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 使用Java NIO实现的socket客户端
 * 
 * @author caozj
 *
 */
public class SocketChannelClientUtil {

	private static final Log logger = LogFactory.getLog(SocketChannelClientUtil.class);

	public static byte[] send(String ip, int port, byte[] content) throws IOException {
		SocketChannel socketChannel = null;
		byte[] result = null;
		try {
			socketChannel = SocketChannel.open();
			SocketAddress socketAddress = new InetSocketAddress(ip, port);
			socketChannel.connect(socketAddress);
			sendData(socketChannel, content);
			result = receiveData(socketChannel);
		} catch (Exception ex) {
			logger.error("发送socket请求异常", ex);
		} finally {
			if (socketChannel != null) {
				socketChannel.close();
			}
		}
		return result;
	}

	private static byte[] receiveData(SocketChannel socketChannel) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bytes = null;
		try {
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			int count = 0;
			while ((count = socketChannel.read(buffer)) >= 0) {
				buffer.flip();
				bytes = new byte[count];
				buffer.get(bytes);
				baos.write(bytes);
				buffer.clear();
			}
			bytes = baos.toByteArray();
			socketChannel.socket().shutdownInput();
		} finally {
			baos.close();
		}
		return bytes;
	}

	private static void sendData(SocketChannel socketChannel, byte[] content) throws IOException {
		ByteBuffer buffer = ByteBuffer.wrap(content);
		socketChannel.write(buffer);
		socketChannel.socket().shutdownOutput();
	}
}
