package com.simba.framework.util.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 随机工具类
 * 
 * @author caozj
 */
public class RandomUtil {

	/** 随机种子 */
	private static final String RANDOM_SEED = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
	/** 数字随机种子 */
	private static final String NUM_RANDOM_SEED = "0123456789";

	/**
	 * 随机出32个字符,字母、数字和中划线的组合
	 * 
	 * @return
	 */
	public static final String random32Chars() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	/**
	 * 获取随机字符列表
	 * 
	 * @param num
	 * @param len
	 * @return
	 */
	public static final List<String> randomStrings(int num, int len) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < num; i++) {
			list.add(randomCharsWithTime(len));
		}
		return list;
	}

	/**
	 * 随机n个字符
	 * 
	 * @return
	 */
	public static final String randomNChars(int n) {
		if (n <= 0) {
			return "";
		}
		StringBuilder ret = new StringBuilder();
		Random random = new Random();
		int size = RANDOM_SEED.length();
		for (int i = 0; i < n; i++)
			ret.append(RANDOM_SEED.charAt(random.nextInt(size)));
		return ret.toString();
	}

	/**
	 * 随机带时间戳的字符(17+n)
	 * 
	 * @return
	 */
	public static final String randomCharsWithTime(int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date()) + randomNChars(n);
	}

	/**
	 * 随机带时间戳的数字串(13+n)
	 * 
	 * @param n
	 * @return
	 */
	public static final String randomNumWithTimeMillis(int n) {
		StringBuilder sb = new StringBuilder(System.currentTimeMillis() + "");// 13位
		Random random = new Random();
		int size = NUM_RANDOM_SEED.length();
		for (int i = 0; i < n; i++) {
			sb.append(NUM_RANDOM_SEED.charAt(random.nextInt(size)));
		}
		return sb.toString();
	}
}
