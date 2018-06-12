package com.simba.fastdfs;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csource.common.FastdfsException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

/**
 * 操作fastdfs的工具类
 * 
 * @author caozhejun
 *
 */
public class FastdfsUtil {

	private static final Log logger = LogFactory.getLog(FastdfsUtil.class);

	/** 连接池 */
	private ConnectionPool connectionPool = null;

	/** 连接池默认最小连接数 */
	private long minPoolSize = 10;

	/** 连接池默认最大连接数 */
	private long maxPoolSize = 30;

	/** 默认等待时间（单位：秒） */
	private long waitTimes = 200;

	private FastdfsUtil() {
		init();
	}

	private void init() {
		String logId = UUID.randomUUID().toString();
		logger.info("[初始化线程池(Init)][" + logId + "][默认参数：minPoolSize=" + minPoolSize + ",maxPoolSize=" + maxPoolSize + ",waitTimes=" + waitTimes + "]");
		connectionPool = new ConnectionPool(minPoolSize, maxPoolSize, waitTimes);
	}

	private static final class FastdfsUtilHolder {
		private static FastdfsUtil instance = new FastdfsUtil();
	}

	public static FastdfsUtil getInstance() {
		return FastdfsUtilHolder.instance;
	}

	/**
	 * 上传到fastdfs
	 * 
	 * @param content
	 *            文件的字节数组
	 * @param fileName
	 *            文件名
	 * @return 可以访问的http地址
	 * @throws FastdfsException
	 * @throws IOException
	 */
	public String upload(byte[] content, String fileName) throws IOException, FastdfsException {
		String logId = UUID.randomUUID().toString();
		TrackerServer trackerServer = connectionPool.checkout(logId);
		StorageServer storageServer = null;
		StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
		String fileExtName = getFileExt(fileName);
		NameValuePair[] metaList = new NameValuePair[3];
		metaList[0] = new NameValuePair("fileName", fileName);
		metaList[1] = new NameValuePair("fileExtName", fileExtName);
		metaList[2] = new NameValuePair("fileLength", String.valueOf(content.length));
		String[] result = storageClient.upload_file(content, fileExtName, metaList);
		/** 上传完毕及时释放连接 */
		connectionPool.checkin(trackerServer, logId);
		String groupName = result[0];
		String remoteFile = result[1];
		String url = "http://" + trackerServer.getInetSocketAddress().getHostName() + ":" + ClientGlobal.getG_tracker_http_port() + "/" + groupName + "/" + remoteFile;
		return url;
	}

	/**
	 * 下载
	 * 
	 * @param groupName
	 * @param remoteFileName
	 * @param localPath
	 * @throws IOException
	 * @throws FastdfsException
	 */
	public void download(String groupName, String remoteFileName, String localPath) throws IOException, FastdfsException {
		String logId = UUID.randomUUID().toString();
		TrackerServer trackerServer = connectionPool.checkout(logId);
		StorageServer storageServer = null;
		StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
		byte[] content = storageClient.download_file(groupName, remoteFileName);
		FileUtils.writeByteArrayToFile(new File(localPath), content);
	}

	/**
	 * 删除文件
	 * 
	 * @param fileUrl
	 *            文件地址
	 * @throws IOException
	 * @throws FastdfsException
	 */
	public void delete(String fileUrl) throws IOException, FastdfsException {
		String[] result = parseUrl(fileUrl);
		delete(result[0], result[1]);
	}

	/**
	 * 删除文件
	 * 
	 * @param groupName
	 * @param remoteFileName
	 * @throws IOException
	 * @throws FastdfsException
	 */
	public void delete(String groupName, String remoteFileName) throws IOException, FastdfsException {
		String logId = UUID.randomUUID().toString();
		TrackerServer trackerServer = connectionPool.checkout(logId);
		StorageServer storageServer = null;
		StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
		storageClient.delete_file(groupName, remoteFileName);
		/** 上传完毕及时释放连接 */
		connectionPool.checkin(trackerServer, logId);
	}

	/**
	 * 获取文件长度
	 * 
	 * @param groupName
	 * @param remoteFileName
	 * @return
	 * @throws FastdfsException
	 * @throws IOException
	 */
	public long size(String groupName, String remoteFileName) throws IOException, FastdfsException {
		String logId = UUID.randomUUID().toString();
		TrackerServer trackerServer = connectionPool.checkout(logId);
		StorageServer storageServer = null;
		StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
		return storageClient.get_file_info(groupName, remoteFileName).getFileSize();
	}

	/**
	 * 获取文件的扩展名
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	private String getFileExt(String fileName) {
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index + 1);
	}

	/**
	 * 解析文件地址
	 * 
	 * @param fileUrl
	 * @return 0:groupName,1:remoteFileName
	 */
	public String[] parseUrl(String fileUrl) {
		int beginIndex = fileUrl.indexOf("/") + 2;
		int fromIndex = fileUrl.indexOf("/", beginIndex);
		int toIndex = fileUrl.indexOf("/", fromIndex + 1);
		String groupName = fileUrl.substring(fromIndex + 1, toIndex);
		String remoteFileName = fileUrl.substring(toIndex + 1);
		String[] result = new String[2];
		result[0] = groupName;
		result[1] = remoteFileName;
		return result;
	}
}
