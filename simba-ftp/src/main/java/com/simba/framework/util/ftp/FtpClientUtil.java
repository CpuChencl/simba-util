package com.simba.framework.util.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.simba.model.constant.ConstantData;

/**
 * ftp客户端工具类
 * 
 * @author caozj
 *
 */
public class FtpClientUtil {

	private FTPClient ftpClient;

	public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
	public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;
	private static final Log logger = LogFactory.getLog(FtpClientUtil.class);
	private static final int dataTimeout = 10 * 60 * 1000;
	private static final int timeout = 1 * 60 * 1000;
	private static final int conectTimeout = 30 * 1000;

	/**
	 * 
	 * @param host
	 *            ftp地址
	 * @param port
	 *            端口
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 */
	public FtpClientUtil(String host, int port, String userName, String password) {
		try {
			ftpClient = new FTPClient();
			ftpClient.setDataTimeout(dataTimeout);
			ftpClient.setDefaultTimeout(timeout);
			ftpClient.setConnectTimeout(conectTimeout);
			ftpClient.setControlEncoding(ConstantData.DEFAULT_CHARSET);
			ftpClient.enterLocalPassiveMode();
			ftpClient.connect(host, port);
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			logger.info("连接返回:" + ftpClient.getReplyCode());
			ftpClient.login(userName, password);
			// 必须在登陆之后设置文件类型，否则无效
			setFileType(BINARY_FILE_TYPE);
		} catch (Exception e) {
			logger.error("连接ftp服务器失败", e);
		}
	}

	/**
	 * 获取ftpclient对象
	 * 
	 * @return
	 */
	public FTPClient getFtpClient() {
		return ftpClient;
	}

	/**
	 * 设置文件类型
	 * 
	 * @param fileType
	 * @throws IOException
	 */
	public void setFileType(int fileType) throws IOException {
		ftpClient.setFileType(fileType);
	}

	/**
	 * 关闭连接
	 * 
	 * @throws IOException
	 */
	public void closeServer() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/**
	 * 更换工作目录
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean changeDirectory(String path) throws IOException {
		return ftpClient.changeWorkingDirectory(path);
	}

	/**
	 * 创建目录
	 * 
	 * @param pathName
	 * @return
	 * @throws IOException
	 */
	public boolean createDirectory(String pathName) throws IOException {
		return ftpClient.makeDirectory(pathName);
	}

	/**
	 * 删除目录(如果目录下面有文件或者目录，无法删除)
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean removeDirectory(String path) throws IOException {
		return ftpClient.removeDirectory(path);
	}

	/**
	 * 递归删除目录及所有子目录
	 * 
	 * @param path
	 *            目录
	 * @param isAll
	 *            是否全部删除
	 * @return
	 * @throws IOException
	 */
	public boolean removeDirectory(String path, boolean isAll) throws IOException {
		if (!isAll) {
			return removeDirectory(path);
		}
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr == null || ftpFileArr.length == 0) {
			return removeDirectory(path);
		}
		for (FTPFile ftpFile : ftpFileArr) {
			String name = ftpFile.getName();
			if (ftpFile.isDirectory()) {
				removeDirectory(path + "/" + name, true);
			} else if (ftpFile.isFile()) {
				deleteFile(path + "/" + name);
			} else if (ftpFile.isSymbolicLink()) {

			} else if (ftpFile.isUnknown()) {

			}
		}
		return ftpClient.removeDirectory(path);
	}

	/**
	 * 获取目录里的所有文件，不包括目录
	 * 
	 * @param path
	 *            目录
	 * @return
	 * @throws IOException
	 */
	public List<String> getFileList(String path) throws IOException {
		FTPFile[] ftpFiles = ftpClient.listFiles(path);
		List<String> retList = new ArrayList<String>();
		if (ftpFiles == null || ftpFiles.length == 0) {
			return retList;
		}
		for (FTPFile ftpFile : ftpFiles) {
			if (ftpFile.isFile()) {
				retList.add(ftpFile.getName());
			}
		}
		return retList;
	}

	/**
	 * 删除文件
	 * 
	 * @param pathName
	 *            文件路径
	 * @return
	 * @throws IOException
	 */
	public boolean deleteFile(String pathName) throws IOException {
		return ftpClient.deleteFile(pathName);
	}

	/**
	 * 上传文件
	 * 
	 * @param fileName
	 *            本地文件全路径
	 * @param newName
	 *            上传后的文件全路径
	 * @return
	 * @throws FileNotFoundException
	 */
	public boolean uploadFile(String fileName, String newName) throws FileNotFoundException {
		return uploadFile(new FileInputStream(fileName), newName);
	}

	/**
	 * 上传文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public boolean uploadFile(String fileName) throws IOException {
		return uploadFile(fileName, fileName);
	}

	/**
	 * 上传文件
	 * 
	 * @param iStream
	 *            文件流
	 * @param newName
	 *            上传后的文件全路径
	 * @return
	 */
	public boolean uploadFile(InputStream iStream, String newName) {
		boolean flag = false;
		try {
			flag = ftpClient.storeFile(newName, iStream);
		} catch (IOException e) {
			logger.error("上传文件到ftp异常", e);
			flag = false;
			return flag;
		} finally {
			IOUtils.closeQuietly(iStream);
		}
		return flag;
	}

	/**
	 * 下载文件
	 * 
	 * @param remoteFileName
	 *            ftp上的文件全路径
	 * @param localFileName
	 *            下载到本地的全路径
	 * @return
	 * @throws IOException
	 */
	public boolean download(String remoteFileName, String localFileName) throws IOException {
		boolean flag = false;
		File outfile = new File(localFileName);
		OutputStream oStream = null;
		try {
			oStream = new FileOutputStream(outfile);
			flag = ftpClient.retrieveFile(remoteFileName, oStream);
		} catch (IOException e) {
			logger.error("ftp下载文件异常", e);
			flag = false;
			return flag;
		} finally {
			IOUtils.closeQuietly(oStream);
		}
		return flag;
	}

	/**
	 * 下载文件
	 * 
	 * @param sourceFileName
	 *            ftp上的文件全路径
	 * @return
	 * @throws IOException
	 */
	public InputStream downFile(String sourceFileName) throws IOException {
		return ftpClient.retrieveFileStream(sourceFileName);
	}
}
