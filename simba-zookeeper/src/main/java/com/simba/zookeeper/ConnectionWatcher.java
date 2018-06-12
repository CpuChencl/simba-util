package com.simba.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 自定义zookeeper监听器
 * 
 * @author caozj
 *
 */
public class ConnectionWatcher implements Watcher {

	private static final int SESSION_TIMEOUT = 60 * 1000;
	private CountDownLatch signal = new CountDownLatch(1);
	private Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 连接服务
	 * 
	 */
	public ZooKeeper connection(String servers) {
		ZooKeeper zk;
		try {
			zk = new ZooKeeper(servers, SESSION_TIMEOUT, this);
			signal.await();
			return zk;
		} catch (IOException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 连接服务
	 * 
	 * @param servers
	 * @param sessionTimeout
	 * @return
	 */
	public ZooKeeper connection(String servers, int sessionTimeout) {
		ZooKeeper zk;
		try {
			zk = new ZooKeeper(servers, sessionTimeout, this);
			signal.await();
			return zk;
		} catch (IOException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 事件监听，可以继承这个类，重载这个方法，实现自己的监听事件,但是实现的方法中第一句必须调用此父类方法
	 */
	public void process(WatchedEvent event) {
		KeeperState state = event.getState();
		if (state == KeeperState.SyncConnected) {
			signal.countDown();
		}
	}

}
