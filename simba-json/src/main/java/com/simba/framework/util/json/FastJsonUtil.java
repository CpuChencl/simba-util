package com.simba.framework.util.json;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * fastjson工具类
 *
 * @author caozj
 */
public class FastJsonUtil {

    private FastJsonUtil() {

    }

    /**
     * Object to json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return JSON.toJSONString(obj, true);
    }

    /**
     * Object to json
     *
     * @param obj
     * @param toPrettify
     * @return
     */
    public static String toJson(Object obj, boolean toPrettify) {
        return JSON.toJSONString(obj, toPrettify);
    }

    /**
     * json to Object
     *
     * @param <T>
     * @param content
     * @param valueType
     * @return
     */
    public static <T> T toObject(String content, Class<T> valueType) {
        return JSON.parseObject(content, valueType);
    }

    /**
     * json to List
     *
     * @param <T>
     * @param content
     * @param valueType
     * @return
     */
    public static <T> List<T> toList(String content, Class<T> valueType) {
        return JSON.parseArray(content, valueType);
    }
}
