package com.simba.framework.util.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 批量替换工具类
 * 
 * @author caozj
 * 
 */
public class BatchReplaceUtil {

	private static Log logger = LogFactory.getLog(BatchReplaceUtil.class);

	private static class ReplaceInfo {
		public String from;
		public String to;
		public int length;
		public int offset;

		public ReplaceInfo(String from, String to, int length, int offset) {
			this.from = from;
			this.to = to;
			this.length = length;
			this.offset = offset;
		}
	}

	private static ReplaceInfo next(String source, ReplaceInfo[] ris, int start, boolean global) {
		int match_failure = 0;
		int last = 0x7fff;
		int point = 0;

		if (global == true) {
			for (int i = 0; i < ris.length; i++) {
				int offset = source.indexOf(ris[i].from, start);
				if (offset == -1) {
					match_failure++;
					ris[i].offset = 0x7fff;
				} else {
					ris[i].offset = offset;
					if (offset < last) {
						last = offset;
						point = i;
					}
				}
			}
		} else {
			for (int i = 0; i < ris.length; i++) {
				ReplaceInfo ri = ris[i];
				if (ri.offset == 0x7fff)
					match_failure++;
				else {
					if (ri.offset < last) {
						last = ri.offset;
						point = i;
					}
				}
			}
		}

		if (match_failure == ris.length)
			return null;
		else
			return ris[point];
	}

	/**
	 * 批量替换
	 * 
	 * @param source
	 *            - 替换源
	 * @param regrexes
	 *            - 替换规则
	 * @return
	 */
	public static String batchreplace(String source, Map<String, String> regrexes) {
		int start = 0;

		ReplaceInfo[] ris = new ReplaceInfo[regrexes.size()];

		Set<Entry<String, String>> entrysets = regrexes.entrySet();
		Iterator<Entry<String, String>> entrys = entrysets.iterator();

		int index = 0;

		while (entrys.hasNext()) {
			Entry<String, String> entry = entrys.next();
			String from = entry.getKey();
			String to = entry.getValue();
			ris[index] = new ReplaceInfo(from, to, from.length(), 0);
			index++;
		}

		if (index != ris.length) {
			logger.error("替换字符串出现异常：" + source);
			return source;
		}

		StringBuilder sb = new StringBuilder();

		boolean global = true;

		while (true) {
			ReplaceInfo ri = next(source, ris, start, global);

			if (ri == null) {
				if (start > 0) {
					sb.append(new String(source.substring(start)));
					source = sb.toString();
				}
				break;
			} else {
				sb.append(new String(source.substring(start, ri.offset))).append(ri.to);
				start = ri.offset + ri.length;

				int offset = source.indexOf(ri.from, start);

				if (offset == -1) {
					ri.offset = 0x7fff;
				} else {
					ri.offset = offset;
				}
				global = false;
			}
		}
		return source;
	}
}
