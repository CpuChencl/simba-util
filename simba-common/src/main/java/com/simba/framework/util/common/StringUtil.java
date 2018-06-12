package com.simba.framework.util.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.impl.util.Base64;

/**
 * 字符串工具类
 * 
 * @author caozj
 *
 */
public class StringUtil {

	private static final Log logger = LogFactory.getLog(StringUtil.class);

	/**
	 * 首字母小写
	 * 
	 * @param content
	 * @return
	 */
	public static String getFirstLower(String content) {
		return content.substring(0, 1).toLowerCase() + content.substring(1);
	}

	/**
	 * 首字母大写
	 * 
	 * @param content
	 * @return
	 */
	public static String getFirstUpper(String content) {
		return content.substring(0, 1).toUpperCase() + content.substring(1);
	}

	/**
	 * 连接字符串列表
	 * 
	 * @param list
	 *            字符串列表
	 * @param split
	 *            连接字符
	 * @return
	 */
	public static String join(List<String> list, String split) {
		if (list == null || list.size() == 0) {
			return StringUtils.EMPTY;
		}
		String result = "";
		for (String s : list) {
			result += s + split;
		}
		if (list.size() > 0) {
			result = result.substring(0, result.length() - split.length());
		}
		return result;
	}

	/**
	 * 连接字符串列表
	 * 
	 * @param list
	 *            字符串列表
	 * @param split
	 *            连接字符
	 * @return
	 */
	public static String join(String[] list, String split) {
		if (list == null || list.length == 0) {
			return StringUtils.EMPTY;
		}
		String result = "";
		for (String s : list) {
			result += s + split;
		}
		if (list.length > 0) {
			result = result.substring(0, result.length() - split.length());
		}
		return result;
	}

	/**
	 * 使用gzip进行压缩(压缩比例大于zip,只有当字符串很大时才使用压缩)
	 * 
	 * @param text
	 * @return
	 */
	public static String gzip(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(text.getBytes());
		} catch (IOException e) {
			logger.error("对字符串进行gzip异常", e);
		} finally {
			IOUtils.closeQuietly(gzip);
		}
		return new String(Base64.encode(out.toByteArray()));
	}

	/**
	 * 使用gzip进行解压缩
	 * 
	 * @param text
	 * @return
	 */
	public static String ungzip(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ingzip = null;
		byte[] compressed = null;
		String decompressed = null;
		try {
			compressed = Base64.decode(text.getBytes());
			in = new ByteArrayInputStream(compressed);
			ingzip = new GZIPInputStream(in);
			IOUtils.copy(ingzip, out);
			decompressed = out.toString();
		} catch (IOException e) {
			logger.error("对字符串进行ungzip异常", e);
		} finally {
			IOUtils.closeQuietly(ingzip);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return decompressed;
	}

	/**
	 * 使用zip进行压缩(压缩比例小于gzip，只有当字符串很大时才使用压缩)
	 * 
	 * @param text
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static final String zip(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(text.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = new String(Base64.encode(compressed));
		} catch (IOException e) {
			logger.error("对字符串进行zip异常", e);
		} finally {
			IOUtils.closeQuietly(zout);
			IOUtils.closeQuietly(out);
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = Base64.decode(text.getBytes());
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			IOUtils.copy(zin, out);
			decompressed = out.toString();
		} catch (IOException e) {
			logger.error("对字符串进行unzip异常", e);
		} finally {
			IOUtils.closeQuietly(zin);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return decompressed;
	}

	/**
	 * 从文本中获取第一个数字
	 * 
	 * @param text
	 * @return
	 */
	public static String getFirstNumber(String text) {
		String number = "";
		boolean get = false;
		int length = text.length();
		for (int i = 0; i < length; i++) {
			String l = text.substring(i, i + 1);
			if (NumberUtils.isDigits(l)) {
				get = true;
				number += l;
			}
			if (get && !NumberUtils.isDigits(l)) {
				break;
			}
		}
		return number;
	}

}
