package com.simba.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.simba.framework.util.common.SerializeUtil;
import com.simba.model.constant.ConstantData;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis字节缓存实现
 * 
 * @author caozj
 */
@Component
@Primary
public class RedisUtil implements Redis {

	private Log logger = LogFactory.getLog(this.getClass());

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort = 6379;

	@Value("${spring.redis.password}")
	private String redisPassword;

	private int timeout = ConstantData.REDIS_TOMEOUT;

	private JedisPool pool = null;

	@PostConstruct
	public void init() {
		if (StringUtils.isEmpty(redisHost)) {
			logger.info("redisHost没配置(spring.redis.host),不初始化redis客户端");
			return;
		}
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(200);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(1000 * 30);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			if (StringUtils.isEmpty(redisPassword)) {
				pool = new JedisPool(config, redisHost, redisPort, timeout);
			} else {
				pool = new JedisPool(config, redisHost, redisPort, timeout, redisPassword);
			}
			logger.info("pool:" + pool);
		} catch (Exception e) {
			throw new RuntimeException("不能初始化Redis客户端", e);
		}
	}

	public Object get(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			byte[] data = jedis.get(key.getBytes());
			if (data == null || data.length <= 0) {
				return null;
			}
			return SerializeUtil.unserialize(data);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	public Jedis getJedis() {
		Jedis jedis = pool.getResource();
		return jedis;
	}

	public void set(String key, Object value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.set(key.getBytes(), SerializeUtil.serialize(value));
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	/**
	 * 设置过期时间
	 * 
	 * @param key
	 * @param timeout
	 *            单位秒
	 */
	public void expire(String key, int timeout) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.expire(key.getBytes(), timeout);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	public void set(String key, Object value, int second) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.set(key.getBytes(), SerializeUtil.serialize(value));
			jedis.expire(key.getBytes(), second);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	public void hset(String key, String field, Object value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.hset(key.getBytes(), field.getBytes(), SerializeUtil.serialize(value));
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	public Object hget(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			byte[] data = jedis.hget(key.getBytes(), field.getBytes());
			if (data == null || data.length <= 0) {
				return null;
			}
			return SerializeUtil.unserialize(data);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	public void remove(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.del(key.getBytes());
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	public long getAutoId(String key) {
		Jedis jedis = null;
		long id = 1;
		try {
			jedis = pool.getResource();
			id = jedis.incr(key);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
		return id;
	}

	public void lpush(String key, Object value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.lpush(key.getBytes(), SerializeUtil.serialize(value));
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	public void rpush(String key, Object value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.rpush(key.getBytes(), SerializeUtil.serialize(value));
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	public List<Object> lrange(String key) {
		Jedis jedis = null;
		List<Object> result = null;
		try {
			jedis = pool.getResource();
			List<byte[]> list = jedis.lrange(key.getBytes(), 0, -1);
			result = new ArrayList<Object>(list.size());
			for (byte[] o : list) {
				result.add(SerializeUtil.unserialize(o));
			}
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
		return result;
	}

	public Object lpop(String key) {
		Jedis jedis = null;
		Object object = null;
		try {
			jedis = pool.getResource();
			byte[] bs = jedis.lpop(key.getBytes());
			if (bs == null || bs.length <= 0) {
				return null;
			}
			object = SerializeUtil.unserialize(bs);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
		return object;
	}

	public Object rpop(String key) {
		Jedis jedis = null;
		Object object = null;
		try {
			jedis = pool.getResource();
			byte[] bs = jedis.rpop(key.getBytes());
			if (bs == null || bs.length <= 0) {
				return null;
			}
			object = SerializeUtil.unserialize(bs);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
		return object;
	}

	public Object lindex(String key, int index) {
		Jedis jedis = null;
		Object object = null;
		try {
			jedis = pool.getResource();
			byte[] bs = jedis.lindex(key.getBytes(), index);
			if (bs == null || bs.length <= 0) {
				return null;
			}
			object = SerializeUtil.unserialize(bs);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
		return object;
	}

	public void close(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public List<String> keys(String pattern) {
		Jedis jedis = null;
		List<String> list = null;
		try {
			jedis = pool.getResource();
			Set<byte[]> set = jedis.keys(pattern.getBytes());
			list = new ArrayList<String>(set.size());
			for (byte[] bs : set) {
				list.add(new String(bs));
			}
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return list;
	}

	public List<String> keys() {
		return keys("*");
	}

	public long llen(String key) {
		Jedis jedis = null;
		long object;
		try {
			jedis = pool.getResource();
			object = jedis.llen(key.getBytes());
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return object;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	public String getRedisPassword() {
		return redisPassword;
	}

	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}

	public boolean tryLock(String key) {
		Jedis jedis = null;
		boolean lock = false;
		try {
			jedis = pool.getResource();
			Long exist = jedis.setnx(key.getBytes(), key.getBytes());
			if (exist > 0) {
				lock = true;
			}
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return lock;
	}

	public boolean tryLock(String key, int timeout) {
		Jedis jedis = null;
		boolean lock = false;
		try {
			jedis = pool.getResource();
			Long exist = jedis.setnx(key.getBytes(), key.getBytes());
			if (exist > 0) {
				lock = true;
				jedis.expire(key.getBytes(), timeout);
			}
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return lock;
	}

	public void releaseLock(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.del(key.getBytes());
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 如果 key 已经存在，并且值为字符串，那么这个命令会把 value 追加到原来值（value）的结尾。 如果 key
	 * 不存在，那么它将首先创建一个空字符串的key，再执行追加操作，这种情况 APPEND 将类似于 SET 操作。
	 * 
	 * @param key
	 * @param value
	 */
	public void append(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.append(key.getBytes(), SerializeUtil.serialize(value));
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public boolean exist(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.exists(key.getBytes());
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> hkeys(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<byte[]> bset = jedis.hkeys(key.getBytes());
			Set<String> set = new HashSet<>(bset.size());
			bset.forEach((byte[] bs) -> {
				set.add(new String(bs));
			});
			return set;
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public void lrem(String key, long count, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.lrem(key.getBytes(), count, SerializeUtil.serialize(value));
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public long getNum(String key) {
		Jedis jedis = null;
		long id = 0L;
		try {
			jedis = pool.getResource();
			String autoId = (String) jedis.get(key);
			if (StringUtils.isNotEmpty(autoId)) {
				id = NumberUtils.toLong(autoId);
			}
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
		return id;
	}

	@Override
	public void clearAutoId(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public void setString(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public void expireString(String key, int timeoutSecond) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.expire(key, timeout);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public void setString(String key, String value, int second) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.set(key, value);
			jedis.expire(key, second);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public void hsetString(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public String hgetString(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hget(key, field);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public void removeString(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public void lpushString(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public void rpushString(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.rpush(key, value);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public List<String> lrangeString(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lrange(key, 0, -1);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public String lpopString(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lpop(key);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public String rpopString(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.rpop(key);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public String lindexString(String key, int index) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lindex(key, index);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			close(jedis);
		}
	}

	@Override
	public List<String> keysString(String pattern) {
		Jedis jedis = null;
		List<String> list = null;
		try {
			jedis = pool.getResource();
			Set<String> set = jedis.keys(pattern);
			list = new ArrayList<>(set);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return list;
	}

	@Override
	public List<String> keysString() {
		return keysString("*");
	}

	@Override
	public long llenString(String key) {
		Jedis jedis = null;
		long object = 0L;
		try {
			jedis = pool.getResource();
			object = jedis.llen(key);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return object;
	}

	@Override
	public void appendString(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.append(key, value);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public boolean existString(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Set<String> hkeysString(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<String> set = jedis.hkeys(key);
			return set;
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public void lremString(String key, long count, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.lrem(key, count, value);
		} catch (Exception e) {
			logger.error("Redis出现错误", e);
			throw new RuntimeException("Redis出现错误！", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

}
