package com.simba.framework.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.simba.model.constant.ConstantData;

/**
 * properties文件操作工具类
 * 
 * @author caozj
 *
 */
public class PropertiesUtil {

	private static final Log logger = LogFactory.getLog(PropertiesUtil.class);

	/**
	 * 按文件顺序读取properties文件内容
	 * 
	 * @param filePath
	 *            文件地址
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> readFileByOrder(String filePath) throws IOException {
		List<String> lines = FileUtils.readLines(new File(filePath), ConstantData.DEFAULT_CHARSET);
		return readLinesToProperties(lines);
	}

	/**
	 * 按文件顺序读取properties文件内容
	 * 
	 * @param resourcePath
	 *            文件地址，相对于classpath,例如/config.properties
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> readByOrder(String resourcePath) throws IOException {
		InputStream in = PropertiesUtil.class.getResourceAsStream(resourcePath);
		List<String> lines = IOUtils.readLines(in);
		return readLinesToProperties(lines);
	}

	private static Map<String, String> readLinesToProperties(List<String> lines) {
		Map<String, String> properties = new LinkedHashMap<>(lines.size());
		lines.forEach((line) -> {
			line = line.trim();
			if (line.startsWith("#") || line.length() == 0) {
				return;
			}
			int index = line.indexOf("=");
			if (index == -1) {
				properties.put(line, StringUtils.EMPTY);
			} else if (index == line.length() - 1) {
				properties.put(line.substring(0, line.length() - 1).trim(), StringUtils.EMPTY);
			} else {
				String key = line.substring(0, index);
				String value = line.substring(index + 1);
				properties.put(key.trim(), value.trim());
			}
		});
		return properties;
	}

	/**
	 * 读取properties文件内容
	 * 
	 * @param resourcePath
	 *            文件地址，相对于classpath,例如/config.properties
	 * @return
	 */
	public static Map<String, String> read(String resourcePath) {
		InputStream in = PropertiesUtil.class.getResourceAsStream(resourcePath);
		return readProperties(in);
	}

	/**
	 * 从输入流读取properties内容
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static Map<String, String> readProperties(InputStream in) {
		Map<String, String> m = new HashMap<String, String>();
		try {
			Properties p = new Properties();
			p.load(in);
			for (Object key : p.keySet()) {
				m.put(key.toString().trim(), p.getProperty(key.toString()).trim());
			}
		} catch (Exception e) {
			logger.error("读取properties文件异常", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return m;
	}

	/**
	 * 读取properties文件内容
	 * 
	 * @param filePath
	 *            文件地址
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Map<String, String> readFile(String filePath) throws FileNotFoundException {
		FileInputStream in = new FileInputStream(filePath);
		return readProperties(in);
	}

	/**
	 * 按顺序将properties文件内容保存
	 * 
	 * @param resourcePath
	 *            文件地址，相对于classpath,例如/config.properties
	 * @param m
	 * @throws IOException
	 */
	public static void saveByOrder(String resourcePath, Map<String, String> m) throws IOException {
		List<String> lines = buildLines(m);
		FileUtils.writeLines(new File(PropertiesUtil.class.getResource(resourcePath).getPath()), ConstantData.DEFAULT_CHARSET, lines);
	}

	/**
	 * 按顺序将properties文件内容保存
	 * 
	 * @param filePath
	 *            文件地址
	 * @param m
	 * @throws IOException
	 */
	public static void saveFileByOrder(String filePath, Map<String, String> m) throws IOException {
		List<String> lines = buildLines(m);
		FileUtils.writeLines(new File(filePath), ConstantData.DEFAULT_CHARSET, lines);
	}

	/**
	 * 根据属性拼出文件行
	 * 
	 * @param m
	 * @return
	 */
	private static List<String> buildLines(Map<String, String> m) {
		List<String> lines = new ArrayList<>(m.size());
		m.forEach((key, value) -> {
			lines.add(key + "=" + value);
		});
		return lines;
	}

	/**
	 * 保存properties文件
	 * 
	 * @param resourcePath
	 *            文件地址，相对于classpath,例如/config.properties
	 * @param m
	 * @throws FileNotFoundException
	 */
	public static void save(String resourcePath, Map<String, String> m) throws FileNotFoundException {
		InputStream in = PropertiesUtil.class.getResourceAsStream(resourcePath);
		OutputStream out = new FileOutputStream(PropertiesUtil.class.getResource(resourcePath).getPath());
		writeProperties(m, in, out);
	}

	/**
	 * 保存properties文件
	 * 
	 * @param filePath
	 *            文件地址
	 * @param m
	 * @throws FileNotFoundException
	 */
	public static void saveFile(String filePath, Map<String, String> m) throws FileNotFoundException {
		writeProperties(m, new FileInputStream(filePath), new FileOutputStream(filePath));
	}

	/**
	 * 将数据写到properties输出流中
	 * 
	 * @param m
	 * @param in
	 * @param out
	 */
	private static void writeProperties(Map<String, String> m, InputStream in, OutputStream out) {
		try {
			Properties p = new Properties();
			p.load(in);
			for (Map.Entry<String, String> entry : m.entrySet()) {
				p.setProperty(entry.getKey(), entry.getValue());
			}
			p.store(out, "");
		} catch (Exception e) {
			logger.error("保存properties文件异常", e);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

}
