package com.oracle.bugcamp.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.bugcamp.jdbc.pool.CheckPoolHook;
import com.oracle.bugcamp.jdbc.pool.ConnectionPool;
import com.oracle.bugcamp.jdbc.util.ConcurrentUtil;

public class ConnectionFactory {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

	private JdbcConfiguration jdbcConfiguration;

	private ConnectionPool pool;

	public ConnectionFactory(JdbcConfiguration jdbcConfiguration) {
		this.jdbcConfiguration = jdbcConfiguration;
		initConnectionFactory();
	}

	private void initConnectionFactory() {
		logger.info("Initialize connection factory.");
		pool = new ConnectionPool(jdbcConfiguration);
		pool.initPool();
		CheckPoolHook.startHook(pool);
		ConcurrentUtil.await();
	}

	public ConnectionProxy getConnectionProxy() {
		return pool.borrowFromPool(null, null);
	}

	public ConnectionProxy getConnectionProxy(String username, String password) {
		return pool.borrowFromPool(username, password);
	}

	public ConnectionPool getConnectionPool() {
		return pool;
	}
}
