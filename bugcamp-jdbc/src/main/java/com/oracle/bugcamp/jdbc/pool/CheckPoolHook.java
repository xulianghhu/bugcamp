package com.oracle.bugcamp.jdbc.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.bugcamp.jdbc.ConnectionProxy;
import com.oracle.bugcamp.jdbc.util.ConcurrentUtil;

/**
 * This is a daemon thread to monitor the connection pool: <li>remove idle
 * connection from the pool when pool size is more than core pool size<li>
 * remove invalid connection and create new one
 * 
 * @see ConnectionPool
 * @company Oracle
 * @author liangx
 * @date Sep 24, 2015
 */
public class CheckPoolHook implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(CheckPoolHook.class);

	private static ConnectionPool pool;

	/**
	 * This thread should have only one instance
	 */
	private CheckPoolHook() {
	}

	/**
	 * Start CheckPoolHook
	 */
	public static Thread startHook(ConnectionPool connectionPool) {
		pool = connectionPool;
		CheckPoolHook checkIdleHook = new CheckPoolHook();
		Thread thread = new Thread(checkIdleHook);
		thread.setName("CheckIdleHook");
		thread.setDaemon(true);
		thread.start();
		return thread;
	}

	@Override
	public void run() {
		logger.info("CheckIdleHook start running.");
		ConcurrentUtil.countDownStartLatch();
		this.dealIdle();
		this.dealInvalid();
	}

	/**
	 * Remove idle connections if necessary.
	 */
	private void dealIdle() {
		long idleTime = pool.getJdbcConfiguration().getLimitIdleTime() * 1000;
		while (pool.isNeedCollected()) {
			ConnectionProxy connectionProxy = pool.removeIdleFromPool(idleTime);
			if (connectionProxy == null) {
				break;
			}
			logger.debug("close the idle connection: " + connectionProxy.getUsername());
			// close the connection
			pool.closeConnection(connectionProxy);
			// decrement the pool size
			pool.decrementPoolSize();
		}
	}

	/**
	 * Remove and re-create invalid connection if necessary.
	 */
	private void dealInvalid() {
		long period = pool.getJdbcConfiguration().getTestPeriod();
		if (period == -1) {
			return;
		}
		while (true) {
			ConnectionProxy connectionProxy = pool.removeIdleFromPool(period);
			if (connectionProxy == null) {
				break;
			}
			boolean isValid = pool.testConnection(connectionProxy);
			// if connection is
			if (isValid) {
				pool.returnToPool(connectionProxy);
				continue;
			}
			// decrement the pool size
			pool.decrementPoolSize();
			// close the connection
			pool.closeConnection(connectionProxy);
			// fill a new connection into pool
			pool.incrementOneConnection();
		}
	}
}
