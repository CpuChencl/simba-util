package com.simba.framework.util.data;

import java.util.Map;

/**
 * 线程数据工具类(存在其中的数据，只在当前线程中有效)
 * 
 * @author caozj
 *
 */
public class ThreadDataUtil {

  public static final ThreadLocal<Map<String, Object>> data = new ThreadData();

  /**
   * 清空线程中的所有数据
   */
  public static void clear() {
    data.remove();
  }

  /**
   * 根据key取出对应的值
   * 
   * @param key
   * @return
   */
  public static Object get(String key) {
    Map<String, Object> map = data.get();
    return map.get(key);
  }

  /**
   * 设置值
   * 
   * @param key
   * @param value
   */
  public static void set(String key, Object value) {
    Map<String, Object> map = data.get();
    map.put(key, value);
    data.set(map);
  }

  /**
   * 删除key对应的值
   * 
   * @param key
   */
  public static void remove(String key) {
    Map<String, Object> map = data.get();
    map.remove(key);
    data.set(map);
  }

}
