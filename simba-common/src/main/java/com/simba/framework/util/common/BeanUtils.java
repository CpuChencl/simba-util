package com.simba.framework.util.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * bean操作的一些工具类
 * 
 * @author caozhejun
 *
 */
public class BeanUtils {

	private static final Log logger = LogFactory.getLog(BeanUtils.class);

	/**
	 * 将bean生成map对象(如果属性没有值，则不放进map中)
	 *
	 * @param bean
	 * @return map对象
	 */
	public static Map<String, String> xmlBean2Map(Object bean) {
		Map<String, String> result = new HashMap<>();
		List<Field> fields = new ArrayList<>(Arrays.asList(bean.getClass().getDeclaredFields()));
		fields.addAll(Arrays.asList(bean.getClass().getSuperclass().getDeclaredFields()));
		for (Field field : fields) {
			try {
				boolean isAccessible = field.isAccessible();
				field.setAccessible(true);
				if (field.get(bean) == null) {
					field.setAccessible(isAccessible);
					continue;
				}
				result.put(field.getName(), field.get(bean).toString());
				field.setAccessible(isAccessible);
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
				logger.error("将bean生成map对象发生异常", e);
			}
		}
		return result;
	}
}
