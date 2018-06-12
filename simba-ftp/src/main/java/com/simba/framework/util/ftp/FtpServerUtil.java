package com.simba.framework.util.ftp;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

/**
 * ftp服务器工具类
 * 
 * @author caozj
 *
 */
public class FtpServerUtil {

	private static final String defaultConfigFile = "/ftp/users.properties";

	private static final Log logger = LogFactory.getLog(FtpServerUtil.class);

	/**
	 * 启动默认的ftp服务
	 */
	public static void start() {
		start(getDefaultConfig(), 21);
	}

	/**
	 * 启动默认的ftp服务
	 * 
	 * @param port
	 *            端口号
	 */
	public static void start(int port) {
		start(getDefaultConfig(), port);
	}

	/**
	 * 启动默认的ftp服务
	 * 
	 * @param configFile
	 *            配置文件
	 */
	public static void start(File configFile) {
		start(21);
	}

	/**
	 * 启动默认的ftp服务
	 * 
	 * @param configFile
	 *            配置文件
	 * @param port
	 *            端口号
	 */
	public static void start(File configFile, int port) {
		FtpServerFactory ftpServerFactory = new FtpServerFactory();
		ListenerFactory listenerFactory = new ListenerFactory();

		listenerFactory.setPort(port);
		ftpServerFactory.addListener("default", listenerFactory.createListener());

		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();

		userManagerFactory.setFile(configFile);
		ftpServerFactory.setUserManager(userManagerFactory.createUserManager());

		FtpServer ftpServer = ftpServerFactory.createServer();
		try {
			ftpServer.start();
		} catch (FtpException e) {
			logger.error("启动ftp服务失败", e);
		}
	}

	/**
	 * 获取默认的配置文件
	 * 
	 * @return
	 */
	private static File getDefaultConfig() {
		return new File(FtpServerUtil.class.getResource(defaultConfigFile).getPath());
	}

}
