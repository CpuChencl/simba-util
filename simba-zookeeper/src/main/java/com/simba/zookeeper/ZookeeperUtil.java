package com.simba.zookeeper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.simba.framework.util.common.SerializeUtil;

/**
 * zookeeper工具类(临时节点只能建立在永久节点上)(监听都是一次性的)
 * 
 * 
 * @author caozj
 *
 */
public class ZookeeperUtil {

	private static final Log logger = LogFactory.getLog(ZookeeperUtil.class);

	private static final String lockFlag = "l";

	/**
	 * 连接服务器
	 *
	 * @return
	 */
	public static ZooKeeper connect(String hosts) {
		ConnectionWatcher cw = new ConnectionWatcher();
		ZooKeeper zk = cw.connection(hosts);
		return zk;
	}

	/**
	 * 连接服务器
	 * 
	 * @param hosts
	 * @param sessionTimeout
	 * @return
	 */
	public static ZooKeeper connect(String hosts, int sessionTimeout) {
		ConnectionWatcher cw = new ConnectionWatcher();
		ZooKeeper zk = cw.connection(hosts, sessionTimeout);
		return zk;
	}

	/**
	 * 连接服务器
	 * 
	 * @param hosts
	 * @param sessionTimeout
	 * @param cw
	 * @return
	 * @throws IOException
	 */
	public static ZooKeeper connect(String hosts, int sessionTimeout, ConnectionWatcher cw) throws IOException {
		return cw.connection(hosts, sessionTimeout);
	}

	/**
	 * 连接服务器
	 * 
	 * @param hosts
	 * @param cw
	 * @return
	 * @throws IOException
	 */
	public static ZooKeeper connect(String hosts, ConnectionWatcher cw) throws IOException {
		return cw.connection(hosts);
	}

	/**
	 * 删除节点(递归删除所有子节点)
	 * 
	 * @param path
	 * @param zk
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static void delete(String path, ZooKeeper zk) throws KeeperException, InterruptedException {
		// 如果节点存在则删除之
		if (exist(path, zk)) {
			// 先删除子节点
			List<String> nodes = zk.getChildren(path, false);
			if (nodes != null) {
				for (String node : nodes) {
					delete(path + "/" + node, zk);
				}
			}
			zk.delete(path, -1);
			logger.info("删除节点完成:[" + path + "]");
		}
	}

	/**
	 * 关闭一个会话
	 * 
	 */
	public static void close(ZooKeeper zk) {
		if (zk != null) {
			try {
				zk.close();
			} catch (InterruptedException e) {
				logger.error("关闭zookeeper异常", e);
			}
		}
	}

	/**
	 * 获取zookeeper对应路径中的对象
	 * 
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static Object get(String path, ZooKeeper zk) throws KeeperException, InterruptedException {
		if (!exist(path, zk)) {
			return null;
		}
		// 获取节点上的数据
		Object result = null;
		byte[] data = zk.getData(path, false, null);
		if (data != null) {
			result = SerializeUtil.unserialize(data);
		}
		return result;
	}

	/**
	 * 创建一个永久的节点
	 * 
	 * @param path
	 * @param zk
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static String createGroupNode(String path, ZooKeeper zk) throws KeeperException, InterruptedException {
		return createGroupNode(path, zk, CreateMode.PERSISTENT);
	}

	/**
	 * 创建一个组节点
	 * 
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static String createGroupNode(String path, ZooKeeper zk, CreateMode mode) throws KeeperException, InterruptedException {
		// 检查节点是否存在
		if (exist(path, zk)) {
			logger.info("节点已经存在:[" + path + "]");
			return path;
		}
		createParentPath(path, zk);
		// 创建组节点
		try {
			String createPath = zk.create(path, null, Ids.OPEN_ACL_UNSAFE, mode);
			logger.info("创建节点完成:[" + createPath + "]");
			return createPath;
		} catch (KeeperException e) {
			logger.warn("创建节点失败:" + e.getMessage());
			return path;
		}
	}

	/**
	 * 创建节点并保存数据
	 * 
	 * @param path
	 * @param data
	 * @param zk
	 * @param mode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static String createGroupNodeAndData(String path, Object data, ZooKeeper zk, CreateMode mode) throws KeeperException, InterruptedException {
		// 检查节点是否存在
		if (exist(path, zk)) {
			logger.info("节点已经存在:[" + path + "]");
			saveData(path, data, zk);
			logger.info("保存数据成功");
			return path;
		}
		createParentPath(path, zk);
		// 创建组节点
		String createPath = zk.create(path, SerializeUtil.serialize(data), Ids.OPEN_ACL_UNSAFE, mode);
		logger.info("创建节点完成:[" + createPath + "]并且保存数据成功");
		return createPath;
	}

	/**
	 * 创建节点并保存数据
	 * 
	 * @param path
	 * @param data
	 * @param zk
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static String createGroupNodeAndData(String path, Object data, ZooKeeper zk) throws KeeperException, InterruptedException {
		return createGroupNodeAndData(path, data, zk, CreateMode.PERSISTENT);
	}

	/**
	 * 创建所有的父节点
	 * 
	 * @param path
	 * @param zk
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private static void createParentPath(String path, ZooKeeper zk) throws KeeperException, InterruptedException {
		int index = path.lastIndexOf("/");
		if (index > 0) {
			String parentPath = path.substring(0, index);
			if (zk.exists(parentPath, false) == null) {
				createGroupNode(parentPath, zk, CreateMode.PERSISTENT);
			}
		}
	}

	/**
	 * 设置值
	 * 
	 * @param path
	 * @param value
	 * @param zk
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static void saveData(String path, Object value, ZooKeeper zk) throws KeeperException, InterruptedException {
		byte[] data = SerializeUtil.serialize(value);
		zk.setData(path, data, -1);
	}

	/**
	 * 获取锁
	 * 
	 * @param path
	 *            锁路径(每个锁的这个值唯一)
	 * @param zk
	 * @return
	 */
	public static boolean tryLock(String path, ZooKeeper zk) {
		try {
			List<String> children = null;
			if (exist(path, zk)) {
				children = zk.getChildren(path, false);
				if (CollectionUtils.isNotEmpty(children)) {
					logger.info(path + "下有子节点，获取锁失败");
					return false;
				}
			} else {
				createGroupNode(path, zk);
			}
			String lockPath = createGroupNode(path + "/" + lockFlag, zk, CreateMode.EPHEMERAL_SEQUENTIAL);
			children = zk.getChildren(path, false);
			if (children.size() > 1) {
				Collections.sort(children);
				String minChild = path + "/" + children.get(0);
				if (!lockPath.equals(minChild)) {
					logger.info(path + "下有多个子节点，最小的不是" + lockPath + ",而是" + minChild + "获取锁失败");
					return false;
				}
			}
			saveData(path, System.currentTimeMillis(), zk);
			logger.info(lockPath + "获取锁成功");
			return true;
		} catch (Exception e) {
			logger.error("获取锁失败", e);
			return false;
		}
	}

	/**
	 * 获取锁
	 * 
	 * @param path
	 *            锁路径(每个锁的这个值唯一)
	 * @param zk
	 * @param timeout
	 *            锁超时时间，单位毫秒
	 * @return
	 */
	public static boolean tryLock(String path, ZooKeeper zk, Long timeout) throws KeeperException, InterruptedException {
		if (timeout == null || timeout <= 0) {
			return tryLock(path, zk);
		}
		if (!exist(path, zk)) {
			return tryLock(path, zk);
		}
		List<String> children = zk.getChildren(path, false);
		if (CollectionUtils.isEmpty(children)) {
			return tryLock(path, zk);
		}
		Long lockTime = (Long) get(path, zk);
		long now = System.currentTimeMillis();
		if (now - lockTime < timeout) {
			return false;
		}
		for (String child : children) {
			delete(path + "/" + child, zk);
		}
		return tryLock(path, zk);
	}

	/**
	 * 释放锁
	 * 
	 * @param path
	 * @param zk
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static void releaseLock(String path, ZooKeeper zk) throws KeeperException, InterruptedException {
		delete(path, zk);
		logger.info("释放锁" + path + "成功");
	}

	/**
	 * 处理路径，生成zookeeper的标准路径
	 * 
	 * @param path
	 * @return
	 */
	public static String dealPath(String path) {
		String sp = "/";
		if (!path.startsWith(sp)) {
			path = sp + path;
		}
		if (path.endsWith(sp)) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	/**
	 * 监听节点的孩子节点是否有变化(返回当前所有的孩子节点的名称列表)
	 * 
	 * @param path
	 *            节点路径
	 * @param zk
	 * @param w
	 *            监听对象
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static List<String> listenChildChange(String path, ZooKeeper zk, Watcher w) throws KeeperException, InterruptedException {
		return zk.getChildren(path, w);
	}

	/**
	 * 监听节点的变化(新增，删除，修改数据)
	 * 
	 * @param path
	 *            路径
	 * @param zk
	 * @param w
	 *            监听对象
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static void listenPath(String path, ZooKeeper zk, Watcher w) throws KeeperException, InterruptedException {
		zk.exists(path, w);
	}

	/**
	 * 判断节点是否存在
	 * 
	 * @param path
	 *            节点路径
	 * @param zk
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static boolean exist(String path, ZooKeeper zk) throws KeeperException, InterruptedException {
		return zk.exists(path, false) != null;
	}
}
