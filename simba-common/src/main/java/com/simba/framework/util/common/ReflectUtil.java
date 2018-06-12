package com.simba.framework.util.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类
 * 
 * @author caozj
 *
 */
public class ReflectUtil {

	/**
	 * 调用对象方法
	 * 
	 * @param className
	 *            完整类路径
	 * @param methodName
	 *            方法名
	 * @param args
	 *            参数 n
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeObjectMethod(String className, String methodName, Object... args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		Class classObject = Class.forName(className);
		Object result = null;
		Method method = null;
		if (args != null && args.length > 0) {
			Class[] paramClasses = new Class[args.length];
			for (int j = 0; j < args.length; j++) {
				paramClasses[j] = args[j].getClass();
			}
			method = classObject.getDeclaredMethod(methodName, paramClasses);
			result = method.invoke(classObject.newInstance(), args);
		} else {
			method = classObject.getDeclaredMethod(methodName);
			result = method.invoke(classObject.newInstance(), args);
		}
		return result;
	}

	/**
	 * 调用类的静态方法
	 * 
	 * @param className
	 *            完整类路径
	 * @param methodName
	 *            方法名
	 * @param args
	 *            参数 n
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeClassMethod(String className, String methodName, Object... args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		Class classObject = Class.forName(className);
		Object result = null;
		Method method = null;
		if (args != null && args.length > 0) {
			Class[] paramClasses = new Class[args.length];
			for (int j = 0; j < args.length; j++) {
				paramClasses[j] = args[j].getClass();
			}
			method = classObject.getDeclaredMethod(methodName, paramClasses);
			result = method.invoke(classObject, args);
		} else {
			method = classObject.getDeclaredMethod(methodName);
			result = method.invoke(classObject, args);
		}
		return result;
	}

	/**
	 * 获取一个类的所有属性名
	 * 
	 * @param c
	 *            class对象
	 * @return 属性名列表
	 */
	public static List<String> getAllPropertiesName(Class<?> c) {
		Field[] fields = c.getDeclaredFields();
		List<String> properties = new ArrayList<String>(fields.length);
		for (Field f : fields) {
			properties.add(f.getName());
		}
		return properties;
	}

}
