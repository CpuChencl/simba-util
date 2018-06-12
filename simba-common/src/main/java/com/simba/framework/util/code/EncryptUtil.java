package com.simba.framework.util.code;

import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 加密工具类
 * 
 * @author caozj
 * 
 */
public class EncryptUtil {

	private static final Log logger = LogFactory.getLog(EncryptUtil.class);

	/**
	 * md5加密
	 * 
	 * @param str 需要进行md5加密的字符
	 * @return 已进行md5的加密的字符
	 */
	public static String md5(String str) {
		return encode(str, "MD5").toLowerCase();
	}

	/**
	 * sha1 加密
	 * 
	 * @param str 需要进行sha1加密的字符
	 * @return 已进行sha1的加密的字符
	 */
	public static String sha1(String str) {
		return encode(str, "SHA-1");
	}

	/**
	 * 按类型对字符串进行加密并转换成16进制输出
	 * 
	 * @param str
	 *            字符串
	 * @param type
	 *            可加密类型md5, des , sha1
	 * @return 加密后的字符串
	 */
	private static String encode(String str, String type) {
		try {
			MessageDigest alga = MessageDigest.getInstance(type);
			alga.update(str.getBytes());
			byte digesta[] = alga.digest();
			String hex = byte2hex(digesta);
			return hex;
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * 将字节数组转换成16进制字符
	 * 
	 * @param b
	 *            需要转换的字节数组
	 * @return 转换后的16进制字符
	 */
	private static String byte2hex(byte b[]) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < b.length; n++) {
			String stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1) {
				sb.append("0");
			}
			sb.append(stmp);
		}

		return sb.toString().toUpperCase();
	}

	/**
	 * 将成16进制转换字符串字符
	 * 
	 * @param s
	 *            需要转换的16进制字符串 return 转换后的字符
	 */
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	/**
	 * 将字符串转换成16进制字符
	 * 
	 * @param s
	 *            需要转换的字符串 return 转换后的16进制字符
	 */
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
		}
		return new String(baKeyword);
	}

	/**
	 * 将字节转换成16进制字符
	 * 
	 * @param ib
	 *            需要转换的字节
	 * @return 转换后的16进制字符
	 */
	protected static String byteToHexString(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0f];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	public static void main(String[] args) {
		String u = "123456";
		String p = "admin123";
		String key = "test";
		System.out.println(EncryptUtil.md5(u + key));
		System.out.println(EncryptUtil.md5(p + key));
	}

}
