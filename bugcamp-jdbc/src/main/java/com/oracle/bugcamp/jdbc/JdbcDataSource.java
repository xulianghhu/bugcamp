package com.oracle.bugcamp.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.oracle.bugcamp.jdbc.util.JdbcUtil;

/**
 * 
 *
 * @company Oracle
 * @author liangx
 * @date Sep 23, 2015
 */
public class JdbcDataSource implements DataSource {

	private final String url;
	private final Driver driver;
	private final Properties commonProperties;
	private final JdbcConfiguration jdbcConfiguration;
	
	public JdbcDataSource(JdbcConfiguration configuration) {
		url = configuration.getUrl();
		driver = JdbcUtil.createDriver(configuration.getDriverClass());
		jdbcConfiguration = configuration;
		commonProperties = new Properties();
		commonProperties.put("user", configuration.getUsername());
		commonProperties.put("password", configuration.getPassword());
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return driver.connect(url, commonProperties);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		Properties properties = new Properties();
		properties.put("user", username);
		properties.put("password", password);
		Connection connection = this.driver.connect(this.url, properties);
		return connection;
	}

	public JdbcConfiguration getJdbcConfiguration() {
		return jdbcConfiguration;
	}

}
