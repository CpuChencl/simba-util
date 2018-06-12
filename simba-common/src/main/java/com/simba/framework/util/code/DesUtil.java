package com.simba.framework.util.code;

import java.io.File;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.io.FileUtils;
import org.apache.xmlbeans.impl.util.Base64;

import com.simba.model.constant.ConstantData;

/**
 * DES加解密工具类(密钥必须超过8个字符)
 * 
 * @author caozj
 *
 */
public class DesUtil {

	private final static String DES = "DES";

	/**
	 * 加密字符串
	 * 
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String source, String key) throws Exception {
		byte[] bt = encrypt(source.getBytes(ConstantData.DEFAULT_CHARSET), key.getBytes(ConstantData.DEFAULT_CHARSET));
		return new String(Base64.encode(bt), ConstantData.DEFAULT_CHARSET);
	}

	/**
	 * 解密字符串
	 * 
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String source, String key) throws Exception {
		byte[] bt = decrypt(Base64.decode(source.getBytes(ConstantData.DEFAULT_CHARSET)), key.getBytes(ConstantData.DEFAULT_CHARSET));
		return new String(bt, ConstantData.DEFAULT_CHARSET);
	}

	/**
	 * 加密文件
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @param key
	 * @throws Exception
	 */
	public static void encrypt(File sourceFile, File destFile, String key) throws Exception {
		byte[] content = FileUtils.readFileToByteArray(sourceFile);
		byte[] des = encrypt(content, key.getBytes(ConstantData.DEFAULT_CHARSET));
		FileUtils.writeByteArrayToFile(destFile, des);
	}

	/**
	 * 解密文件
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @param key
	 * @throws Exception
	 */
	public static void decrypt(File sourceFile, File destFile, String key) throws Exception {
		byte[] content = FileUtils.readFileToByteArray(sourceFile);
		byte[] des = decrypt(content, key.getBytes(ConstantData.DEFAULT_CHARSET));
		FileUtils.writeByteArrayToFile(destFile, des);
	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		return cipher.doFinal(data);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		return cipher.doFinal(data);
	}

}
