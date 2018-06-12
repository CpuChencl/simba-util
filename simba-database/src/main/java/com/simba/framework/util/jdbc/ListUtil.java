package com.simba.framework.util.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ListUtil {

	public ListUtil() {
	}

	public static int size(Object[] list) {
		if (list == null) {
			return 0;
		} else {
			return list.length;
		}
	}

	public static int size(List<?> list) {
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	public static List<String> uniq(List<String> list) {
		if (list == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		for (String str : list) {
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return result;
	}

	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}

	public static boolean isEmpty(List<?> list) {
		if (list == null || list.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static List<String> toStringResult(List<String> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list;
	}

	public static String[] defaultStrings(String[] strs) {
		if (strs == null) {
			return new String[0];
		}
		return strs;
	}

	public static <T> List<T> defaultList(List<T> list) {
		if (list == null) {
			return new ArrayList<T>();
		}
		return list;
	}

	public static List<?> noNull(List<?> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		// 删除空记录
		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next == null) {
				iterator.remove();
			}
		}
		return list;
	}

	public static List<String> toList(Set<String> set) {
		if (set == null || set.isEmpty()) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	public static Set<Integer> toIntSet(List<Integer> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		Set<Integer> set = new LinkedHashSet<Integer>();
		for (Integer element : list) {
			set.add(element);
		}
		return set;
	}

	public static Set<String> toSet(List<String> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		Set<String> set = new LinkedHashSet<String>();
		for (String element : list) {
			set.add(element);
		}
		return set;
	}

	public static List<String> toList(String content) {
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		String[] strs = content.split(",");
		List<String> list = new ArrayList<String>();
		for (String str : strs) {
			list.add(str);
		}
		return list;
	}

	public static List<String> toList(String content, boolean trim) {
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		if (!trim) {
			return toList(content);
		}
		content = content.trim();
		String[] strs = content.split(",");
		List<String> list = new ArrayList<String>();
		for (String str : strs) {
			str = str.trim();
			list.add(str);
		}
		return list;
	}

	public static List<Integer> toIntList(Set<String> members) {
		if (members == null || members.isEmpty()) {
			return null;
		}
		List<Integer> result = new ArrayList<Integer>();
		Iterator<String> iterator = members.iterator();
		while (iterator.hasNext()) {
			String str = iterator.next();
			Integer newsId = Integer.parseInt(str);
			result.add(newsId);
		}
		return result;
	}

	public static String[] getIntKeys(String prefix, Set<Integer> idSet) {
		String[] keys = new String[idSet.size()];
		int index = 0;
		for (Integer id : idSet) {
			keys[index] = prefix + ":" + id;
			index++;
		}
		return keys;
	}

	public static String[] getKeys(String prefix, Set<String> usernameSet) {
		String[] keys = new String[usernameSet.size()];
		int index = 0;
		for (String username : usernameSet) {
			keys[index] = prefix + ":" + username;
			index++;
		}
		return keys;
	}

	public static String[] toStringArray(List<Integer> list) {
		if (isEmpty(list)) {
			return null;
		}
		String[] fields = new String[list.size()];
		int index = 0;
		for (Integer num : list) {
			fields[index] = Integer.toString(num);
			index++;
		}
		return fields;
	}

	public static List<Integer> toIntList(List<String> members) {
		if (members == null || members.isEmpty()) {
			return null;
		}
		List<Integer> result = new ArrayList<Integer>();
		Iterator<String> iterator = members.iterator();
		while (iterator.hasNext()) {
			String str = iterator.next();
			if (str == null) {
				result.add(null);
			} else {
				int newsId = Integer.parseInt(str);
				result.add(newsId);
			}
		}
		return result;
	}

	public static List<String> makeList(String prefix, int start, int size) {
		List<String> list = new ArrayList<String>();
		int end = start + size;
		for (int i = start; i < end; i++) {
			list.add(prefix + i);
		}
		return list;
	}

}
