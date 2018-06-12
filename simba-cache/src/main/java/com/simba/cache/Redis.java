package com.simba.cache;

import java.util.List;
import java.util.Set;

public interface Redis {

	Object get(String key);

	void set(String key, Object value);

	void setString(String key, String value);

	void expire(String key, int timeoutSecond);

	void expireString(String key, int timeoutSecond);

	void set(String key, Object value, int second);

	void setString(String key, String value, int second);

	void hset(String key, String field, Object value);

	void hsetString(String key, String field, String value);

	Object hget(String key, String field);

	String hgetString(String key, String field);

	void remove(String key);

	void removeString(String key);

	long getAutoId(String key);

	long getNum(String key);

	void clearAutoId(String key);

	void lpush(String key, Object value);

	void lpushString(String key, String value);

	void rpush(String key, Object value);

	void rpushString(String key, String value);

	List<Object> lrange(String key);

	List<String> lrangeString(String key);

	Object lpop(String key);

	String lpopString(String key);

	Object rpop(String key);

	String rpopString(String key);

	Object lindex(String key, int index);

	String lindexString(String key, int index);

	List<String> keys(String pattern);

	List<String> keysString(String pattern);

	List<String> keys();

	List<String> keysString();

	long llen(String key);

	long llenString(String key);

	boolean tryLock(String key);

	boolean tryLock(String key, int timeoutSecond);

	void releaseLock(String key);

	/**
	 * 如果 key 已经存在，并且值为字符串，那么这个命令会把 value 追加到原来值（value）的结尾。 如果 key
	 * 不存在，那么它将首先创建一个空字符串的key，再执行追加操作，这种情况 APPEND 将类似于 SET 操作
	 * 
	 * @param key
	 * @param value
	 */
	void append(String key, String value);

	void appendString(String key, String value);

	boolean exist(String key);

	boolean existString(String key);

	/**
	 * 返回 key 指定的哈希集中所有字段的名字
	 * 
	 * @param key
	 * @return
	 */
	Set<String> hkeys(String key);

	Set<String> hkeysString(String key);

	/**
	 * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作： count
	 * > 0: 从头往尾移除值为 value 的元素。 count < 0: 从尾往头移除值为 value 的元素。 count = 0: 移除所有值为
	 * value 的元素。
	 * 
	 * @param key
	 * @param count
	 * @param value
	 */
	void lrem(String key, long count, String value);

	void lremString(String key, long count, String value);
}
