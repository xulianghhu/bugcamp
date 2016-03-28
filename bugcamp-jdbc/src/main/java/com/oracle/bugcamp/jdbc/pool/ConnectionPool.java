package com.oracle.bugcamp.jdbc.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.bugcamp.jdbc.ConnectionProxy;
import com.oracle.bugcamp.jdbc.JdbcConfiguration;
import com.oracle.bugcamp.jdbc.JdbcDataSource;
import com.oracle.bugcamp.jdbc.JdbcException;

/**
 * This connection pool contains two type of connections: <b>common
 * connection</b> and <b>specified connection</b>. The common connection use a
 * common user specified by {@link JdbcConfiguration}, normally they only have
 * read privilege. The specified connections use different users, generally the
 * sso account, to connect to oracle database, they have some additional write
 * privileges to their bugs.
 * <p>
 * The pool will provide the first connection in the store to user if they do
 * not provide username and password. This may be a specified connection belong
 * to other user, but it is no matter to use other's connection to read database
 * only.
 *
 * @company Oracle
 * @author liangx
 * @date Sep 23, 2015
 */
public class ConnectionPool {

	private static final Logger logger = LoggerFactory
			.getLogger(ConnectionPool.class);

	private ConnectionStore connectionStore = new ConnectionStore();

	private JdbcDataSource jdbcDataSource;

	private JdbcConfiguration jdbcConfiguration;

	private AtomicInteger poolSize = new AtomicInteger();

	private Lock lock = new ReentrantLock();

	/**
	 * record the peak of the connection in the pool.
	 */
	private int peakPoolSize;

	public ConnectionPool(JdbcConfiguration jdbcConfiguration) {
		this.jdbcConfiguration = jdbcConfiguration;
	}

	public ConnectionPool(JdbcDataSource jdbcDataSource) {
		this.jdbcDataSource = jdbcDataSource;
		this.jdbcConfiguration = jdbcDataSource.getJdbcConfiguration();
	}

	public void initPool() {
		logger.info("Initialize connection pool.");
		if (null == jdbcConfiguration) {
			throw new JdbcException("JDBC configuration not privided!");
		}
		jdbcDataSource = new JdbcDataSource(jdbcConfiguration);
		int initialPoolSize = jdbcConfiguration.getInitialPoolSize();
		fillPool(initialPoolSize);
	}

	/**
	 * Fill the pool with connections.
	 * 
	 * @param count
	 *            the connection count
	 */
	private void fillPool(int count) {
		for (int i = 0; i < count; i++) {
			ConnectionProxy connectionProxy = getConnection(null, null);
			connectionStore.add(connectionProxy);
			handlePeakPoolSize(i + 1);
		}
		poolSize.addAndGet(count);
	}

	/**
	 * Try to get connection from data source
	 * 
	 * @param isCommon
	 *            if true then get a common connection without specified user,
	 *            otherwise get a specified connection by username and password
	 * @param username
	 *            the specified user
	 * @param password
	 *            the password of the user
	 * @return {@link ConnectionProxy}
	 */
	private ConnectionProxy getConnection(String username, String password) {
		int retryTimes = jdbcConfiguration.getRetryTimes();
		int count = 0;
		Connection connection = null;
		do {
			try {
				if (null == username || null == password) {
					connection = jdbcDataSource.getConnection();
				} else {
					connection = jdbcDataSource.getConnection(username,
							password);
				}
			} catch (SQLException e) {
				logger.error("Fail to get connection - time " + count++, e);
				if (count > retryTimes) {
					logger.error("Cannot get connection.");
					throw new JdbcException(e);
				}
			}
		} while (connection == null);
		ConnectionProxy connectionProxy = new ConnectionProxy(this, connection,
				username);
		return connectionProxy;
	}

	/**
	 * Borrow a common connection from the pool
	 */
	public ConnectionProxy borrowFromPool(String username, String password) {
		ConnectionProxy connectionProxy = null;
		for (;;) {
			this.lock.lock();
			try {
				do {
					connectionProxy = connectionStore.remove(username);
					// if could not get a common connection from pool, should
					// new increment ones
					if (connectionProxy == null) {

						if (username == null) {
							int maxIncrement = jdbcConfiguration
									.getMaxPoolSize() - this.poolSize.get();
							// if pool is full, shouldn't grow it
							if (maxIncrement == 0) {
								throw new JdbcException(
										"There is no more connection in the pool.");
							} else {
								this.fillPoolByAcquireIncrement();
							}
						} else {
							// for a specified user create new one
							ConnectionProxy proxy = getConnection(username,
									password);
							connectionStore.add(proxy);
						}
					}
				} while (connectionProxy == null);
			} finally {
				this.lock.unlock();
			}

			if (jdbcConfiguration.isTestBeforeUse()) {
				// test the connection before use
				boolean isValid = testConnection(connectionProxy);
				if (!isValid) {
					decrementPoolSize();
					closeConnection(connectionProxy);
					incrementOneConnection();
					continue;
				}
			}
			break;
		}
		return connectionProxy;
	}

	/**
	 * Return connection to the pool
	 */
	public void returnToPool(ConnectionProxy connectionProxy) {
		if (connectionProxy == null) {
			throw new NullPointerException();
		}
		this.lock.lock();
		try {
			connectionStore.add(connectionProxy);
		} finally {
			this.lock.unlock();
		}
	}

	private void fillPoolByAcquireIncrement() {
		int maxIncrement = jdbcConfiguration.getMaxPoolSize()
				- this.poolSize.get();
		// double check
		if (maxIncrement != 0) {
			int increment = jdbcConfiguration.getAquireIncrement();
			if (increment > maxIncrement) {
				increment = maxIncrement;
			}
			this.fillPool(increment);
		}
	}

	/**
	 * remove the idle connection from pool
	 * 
	 * @param period
	 * @return
	 */
	public ConnectionProxy removeIdleFromPool(long period) {
		this.lock.lock();
		try {
			return connectionStore.removeIdle(period);
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * Just test the connection
	 * 
	 * @param connectionProxy
	 * @return
	 */
	public boolean testConnection(ConnectionProxy connectionProxy) {
		PreparedStatement stmt = null;
		try {
			Connection connection = connectionProxy.getConnection();
			String testSql = jdbcConfiguration.getTestSql();
			stmt = connection.prepareStatement(testSql);
			stmt.execute();
		} catch (SQLException e) {
			logger.warn("Test connection failed.");
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error("", e);
				}
			}
		}
		return true;
	}

	/**
	 * close the connection
	 * 
	 * @param connectionProxy
	 */
	public void closeConnection(ConnectionProxy connectionProxy) {
		if (connectionProxy != null) {
			try {
				connectionProxy.getConnection().close();
			} catch (SQLException e) {
				logger.error("Cannot close connection", e);
			}
		}
	}

	/**
	 * increment one connection
	 */
	public void incrementOneConnection() {
		this.lock.lock();
		try {
			this.fillPool(1);
		} finally {
			this.lock.unlock();
		}
	}

	public void decrementPoolSize() {
		this.poolSize.decrementAndGet();
	}

	/**
	 * save the peak pool size
	 */
	private void handlePeakPoolSize(int poolNum) {
		int size = this.poolSize.get() + poolNum;
		if (size > this.peakPoolSize) {
			this.peakPoolSize = size;
		}
	}

	/**
	 * when the poolSize is more than corePoolSize in the configuration, this
	 * pool should be collected
	 * 
	 * @return
	 */
	public boolean isNeedCollected() {
		return this.poolSize.get() > jdbcConfiguration.getCorePoolSize();
	}

	public JdbcConfiguration getJdbcConfiguration() {
		return jdbcConfiguration;
	}

	public int getPoolSize() {
		return poolSize.get();
	}

	public int getPeakSize() {
		return peakPoolSize;
	}

}
