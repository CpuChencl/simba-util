package com.simba.framework.util.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.simba.framework.util.file.FileUtils;

/**
 * 执行命令行的工具类
 * 
 * @author caozhejun
 *
 */
public class ExecuteUtil {

	private static final Log logger = LogFactory.getLog(ExecuteUtil.class);

	/**
	 * 同步执行命令行等待返回结果
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 */
	public static String execute(String command) throws IOException {
		Process proc = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		return IOUtils.toString(br);
	}

	/**
	 * 异步执行命令行
	 * 
	 * @param command
	 */
	public static void asyncExecute(String command) {
		new Thread(() -> {
			try {
				logger.info("异步执行命令行结果:" + ExecuteUtil.execute(command));
			} catch (Exception e) {
				logger.error("异步执行命令行发生异常", e);
			}
		}).start();
	}

	/**
	 * 执行bat文件
	 * 
	 * @param batFile
	 *            bat脚本全路径
	 * @throws IOException
	 */
	public static void executeBat(String batFile) throws IOException {
		File cf = new File(batFile);
		String cfDir = cf.getParent();
		String command = FileUtils.getWindowsPanFu(cfDir) + ": && ";
		command += " cd " + cfDir + " && ";
		command += cf.getName();
		ExecuteUtil.executeMultWindows(command);
	}

	/**
	 * 执行shell脚本
	 * 
	 * @param shellFile
	 *            shell脚本全路径
	 * @throws IOException
	 */
	public static void executeShell(String shellFile) throws IOException {
		File cf = new File(shellFile);
		String cfDir = cf.getParent();
		String command = " cd " + cfDir + " && ";
		command += " ./" + cf.getName();
		ExecuteUtil.executeMultLinux(command);
	}

	/**
	 * 执行多windows命令，多个命令使用 && 连接
	 * 
	 * @param command
	 * @throws IOException
	 */
	public static void executeMultWindows(String command) throws IOException {
		command = "cmd.exe /c  " + command;
		logger.info("执行命令:" + command);
		ExecuteUtil.execute(command);
	}

	/**
	 * 执行多条linux命令，多个命令使用 && 连接
	 * 
	 * @param command
	 * @throws IOException
	 */
	public static void executeMultLinux(String command) throws IOException {
		command = "/bin/sh -c   " + command;
		logger.info("执行命令:" + command);
		ExecuteUtil.execute(command);
	}

}
