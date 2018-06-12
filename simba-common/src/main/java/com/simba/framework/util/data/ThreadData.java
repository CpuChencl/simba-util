package com.simba.framework.util.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 重写ThreadLocal类，初始化时返回一个空的Map
 * 
 * @author caozj
 *
 */
public class ThreadData extends ThreadLocal<Map<String, Object>> {

  @Override
  protected Map<String, Object> initialValue() {
    return new HashMap<>();
  }

}
