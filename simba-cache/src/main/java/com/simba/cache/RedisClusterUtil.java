package com.simba.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.simba.framework.util.common.SerializeUtil;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 集群版redis工具类
 * 
 * @author caozhejun
 *
 */
@Component
public class RedisClusterUtil implements Redis {

	private Log logger = LogFactory.getLog(this.getClass());

	private JedisCluster jedisCluster = null;

	private String redisClusterAddress;

	@Autowired
	private Environment env;

	@PostConstruct
	private void init() {
		redisClusterAddress = env.getProperty("redis.cluster.address");
		if (StringUtils.isEmpty(redisClusterAddress)) {
			logger.info("没有配置redis集群(redis.cluster.address)[ip1:port1,ip2:port2]，不启动redis集群客户端");
			return;
		}
		String[] address = redisClusterAddress.split(",");
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		for (String node : address) {
			if (StringUtils.isEmpty(node)) {
				continue;
			}
			String[] hostAndPort = node.split(":");
			if (hostAndPort.length != 2) {
				continue;
			}
			HostAndPort hp = new HostAndPort(hostAndPort[0], NumberUtils.toInt(hostAndPort[1], 6379));
			nodes.add(hp);
		}
		jedisCluster = new JedisCluster(nodes);
	}

	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public Object get(String key) {
		byte[] data = jedisCluster.get(key.getBytes());
		if (data == null || data.length <= 0) {
			return null;
		}
		return SerializeUtil.unserialize(data);
	}

	public void set(String key, Object value) {
		jedisCluster.set(key.getBytes(), SerializeUtil.serialize(value));
	}

	public void set(String key, Object value, int second) {
		jedisCluster.set(key.getBytes(), SerializeUtil.serialize(value));
		jedisCluster.expire(key.getBytes(), second);
	}

	public void hset(String key, String field, Object value) {
		jedisCluster.hset(key.getBytes(), field.getBytes(), SerializeUtil.serialize(value));
	}

	/**
	 * 设置过期时间
	 * 
	 * @param key
	 * @param timeout
	 *            单位秒
	 */
	public void expire(String key, int timeout) {
		jedisCluster.expire(key.getBytes(), timeout);
	}

	public Object hget(String key, String field) {
		byte[] data = jedisCluster.hget(key.getBytes(), field.getBytes());
		if (data == null || data.length <= 0) {
			return null;
		}
		return SerializeUtil.unserialize(data);
	}

	public void remove(String key) {
		jedisCluster.del(key.getBytes());
	}

	public long getAutoId(String key) {
		return jedisCluster.incr(key);
	}

	public void lpush(String key, Object value) {
		jedisCluster.lpush(key.getBytes(), SerializeUtil.serialize(value));
	}

	public void rpush(String key, Object value) {
		jedisCluster.rpush(key.getBytes(), SerializeUtil.serialize(value));
	}

	public List<Object> lrange(String key) {
		List<Object> result = null;
		List<byte[]> list = jedisCluster.lrange(key.getBytes(), 0, -1);
		result = new ArrayList<Object>(list.size());
		for (byte[] o : list) {
			result.add(SerializeUtil.unserialize(o));
		}
		return result;
	}

	public Object lpop(String key) {
		byte[] bs = jedisCluster.lpop(key.getBytes());
		if (bs == null || bs.length <= 0) {
			return null;
		}
		return SerializeUtil.unserialize(bs);
	}

	public Object rpop(String key) {
		byte[] bs = jedisCluster.rpop(key.getBytes());
		if (bs == null || bs.length <= 0) {
			return null;
		}
		return SerializeUtil.unserialize(bs);
	}

	public Object lindex(String key, int index) {
		byte[] bs = jedisCluster.lindex(key.getBytes(), index);
		if (bs == null || bs.length <= 0) {
			return null;
		}
		return SerializeUtil.unserialize(bs);
	}

	public long llen(String key) {
		return jedisCluster.llen(key.getBytes());
	}

	public boolean tryLock(String key) {
		boolean lock = false;
		Long exist = jedisCluster.setnx(key.getBytes(), key.getBytes());
		if (exist > 0) {
			lock = true;
		}
		return lock;
	}

	public boolean tryLock(String key, int timeout) {
		boolean lock = false;
		Long exist = jedisCluster.setnx(key.getBytes(), key.getBytes());
		if (exist > 0) {
			lock = true;
			jedisCluster.expire(key.getBytes(), timeout);
		}
		return lock;
	}

	public void releaseLock(String key) {
		jedisCluster.del(key.getBytes());
	}

	/**
	 * 如果 key 已经存在，并且值为字符串，那么这个命令会把 value 追加到原来值（value）的结尾。 如果 key
	 * 不存在，那么它将首先创建一个空字符串的key，再执行追加操作，这种情况 APPEND 将类似于 SET 操作。
	 * 
	 * @param key
	 * @param value
	 */
	public void append(String key, String value) {
		jedisCluster.append(key.getBytes(), SerializeUtil.serialize(value));
	}

	@Override
	public List<String> keys(String pattern) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> keys() {
		return this.keys("*");
	}

	@Override
	public boolean exist(String key) {
		return jedisCluster.exists(key.getBytes());
	}

	@Override
	public Set<String> hkeys(String key) {
		Set<byte[]> bset = jedisCluster.hkeys(key.getBytes());
		Set<String> set = new HashSet<>(bset.size());
		bset.forEach((byte[] bs) -> {
			set.add(new String(bs));
		});
		return set;
	}

	@Override
	public void lrem(String key, long count, String value) {
		jedisCluster.lrem(key.getBytes(), count, SerializeUtil.serialize(value));
	}

	@Override
	public long getNum(String key) {
		String autoId = jedisCluster.get(key);
		long num = 0;
		if (StringUtils.isNotEmpty(autoId)) {
			num = NumberUtils.toLong(autoId);
		}
		return num;
	}

	@Override
	public void clearAutoId(String key) {
		jedisCluster.del(key);
	}

	@Override
	public void setString(String key, String value) {
		jedisCluster.set(key, value);
	}

	@Override
	public void expireString(String key, int timeoutSecond) {
		jedisCluster.expire(key, timeoutSecond);
	}

	@Override
	public void setString(String key, String value, int second) {
		jedisCluster.set(key, value);
		jedisCluster.expire(key, second);
	}

	@Override
	public void hsetString(String key, String field, String value) {
		jedisCluster.hset(key, field, value);
	}

	@Override
	public String hgetString(String key, String field) {
		return jedisCluster.hget(key, field);
	}

	@Override
	public void removeString(String key) {
		jedisCluster.del(key);
	}

	@Override
	public void lpushString(String key, String value) {
		jedisCluster.lpush(key, value);
	}

	@Override
	public void rpushString(String key, String value) {
		jedisCluster.rpush(key, value);
	}

	@Override
	public List<String> lrangeString(String key) {
		return jedisCluster.lrange(key, 0, -1);
	}

	@Override
	public String lpopString(String key) {
		return jedisCluster.lpop(key);
	}

	@Override
	public String rpopString(String key) {
		return jedisCluster.rpop(key);
	}

	@Override
	public String lindexString(String key, int index) {
		return jedisCluster.lindex(key, index);
	}

	@Override
	public List<String> keysString(String pattern) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> keysString() {
		return keysString("*");
	}

	@Override
	public long llenString(String key) {
		return jedisCluster.llen(key);
	}

	@Override
	public void appendString(String key, String value) {
		jedisCluster.append(key, value);
	}

	@Override
	public boolean existString(String key) {
		return jedisCluster.exists(key);
	}

	@Override
	public Set<String> hkeysString(String key) {
		return jedisCluster.hkeys(key);
	}

	@Override
	public void lremString(String key, long count, String value) {
		jedisCluster.lrem(key, count, value);
	}
}
