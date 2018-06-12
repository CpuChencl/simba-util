package com.simba.framework.util.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解工具类
 * 
 * @author caozj
 *
 */
public class AnnotationUtil {

	/**
	 * 获取类上所有的注解
	 * 
	 * @param c
	 * @return
	 */
	public static Annotation[] getClassAnnotations(Class<?> c) {
		return c.getAnnotations();
	}

	/**
	 * 获取类上指定的注解
	 * 
	 * @param c
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T getClassAnnotation(Class<?> c, Class<T> annotationClass) {
		return c.getAnnotation(annotationClass);
	}

	/**
	 * 获取属性上所有的注解
	 * 
	 * @param c
	 * @param field
	 * @return
	 */
	public static Annotation[] getFiledAnnotions(Class<?> c, String field) {
		Field[] fields = c.getDeclaredFields();
		Field f = null;
		for (Field fi : fields) {
			if (fi.getName().equals(field)) {
				f = fi;
				break;
			}
		}
		if (f == null) {
			return null;
		}
		return f.getAnnotations();
	}

	/**
	 * 获取属性上指定的注解
	 * 
	 * @param c
	 * @param field
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T getFiledAnnotion(Class<?> c, String field, Class<T> annotationClass) {
		Field[] fields = c.getDeclaredFields();
		Field f = null;
		for (Field fi : fields) {
			if (fi.getName().equals(field)) {
				f = fi;
				break;
			}
		}
		if (f == null) {
			return null;
		}
		return f.getAnnotation(annotationClass);
	}

	/**
	 * 获取类的所有属性的所有注解
	 * 
	 * @param c
	 * @return
	 */
	public static Map<String, Annotation[]> getFieldsAnnotations(Class<?> c) {
		Field[] fields = c.getDeclaredFields();
		Map<String, Annotation[]> m = new HashMap<>(fields.length);
		for (Field fi : fields) {
			m.put(fi.getName(), fi.getAnnotations());
		}
		return m;
	}

	/**
	 * 获取方法上的所有的注解
	 * 
	 * @param c
	 * @param methodName
	 * @return
	 */
	public static Annotation[] getMethodAnnotations(Class<?> c, String methodName) {
		Method[] methods = c.getMethods();
		Method m = null;
		for (Method method : methods) {
			if (methodName.equals(method.getName())) {
				m = method;
				break;
			}
		}
		if (m == null) {
			return null;
		}
		return m.getAnnotations();
	}

	/**
	 * 获取方法上指定的注解
	 * 
	 * @param c
	 * @param methodName
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T getMethodAnnotation(Class<?> c, String methodName, Class<T> annotationClass) {
		Method[] methods = c.getMethods();
		Method m = null;
		for (Method method : methods) {
			if (methodName.equals(method.getName())) {
				m = method;
				break;
			}
		}
		if (m == null) {
			return null;
		}
		return m.getAnnotation(annotationClass);
	}

}
