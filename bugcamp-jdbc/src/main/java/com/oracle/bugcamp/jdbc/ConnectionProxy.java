package com.oracle.bugcamp.jdbc;

import java.sql.Connection;

import com.oracle.bugcamp.jdbc.pool.ConnectionPool;

/**
 * 
 *
 * @company Oracle
 * @author liangx
 * @date Sep 23, 2015
 */
public class ConnectionProxy {

	/**
	 * this connection username for the specified user
	 */
	private String username;

	private final ConnectionPool pool;

	private final Connection connection;

	public ConnectionProxy(ConnectionPool pool, Connection connection) {
		this(pool, connection, null);
	}

	public ConnectionProxy(ConnectionPool pool, Connection connection,
			String username) {
		this.pool = pool;
		this.connection = connection;
		this.username = username;
	}

	public void close() {
		pool.returnToPool(this);
	}

	public String getUsername() {
		return username;
	}

	public ConnectionPool getPool() {
		return pool;
	}

	public Connection getConnection() {
		return connection;
	}

}
