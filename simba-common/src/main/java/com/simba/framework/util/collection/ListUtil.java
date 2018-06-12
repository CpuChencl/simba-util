package com.simba.framework.util.collection;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

/**
 * list工具类
 * 
 * @author caozj
 *
 */
public class ListUtil {

	public static boolean isEmpty(List<?> list) {
		return list != null && list.size() > 0 ? false : true;
	}

	public static String join(List<String> list) {
		return join(list, ",");
	}

	public static String join(List<String> list, final String sperator) {
		if (isEmpty(list)) {
			return StringUtils.EMPTY;
		}
		Optional<String> reduced = list.stream().reduce((a, b) -> {
			return a + sperator + b;
		});
		return reduced.isPresent() ? reduced.get() : StringUtils.EMPTY;
	}

}
