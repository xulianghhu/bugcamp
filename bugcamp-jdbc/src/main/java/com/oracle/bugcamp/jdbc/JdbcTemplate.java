package com.oracle.bugcamp.jdbc;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.bugcamp.jdbc.mapper.NamedParameterStatement;
import com.oracle.bugcamp.jdbc.mapper.ResultSetExtractor;
import com.oracle.bugcamp.jdbc.mapper.RowMapper;
import com.oracle.bugcamp.jdbc.util.ReflectionUtil;

/**
 * This is a JDBC template, it not likes the <code>JdbcTemplate</code> in
 * Spring. Every time you should new a instance for this class rather than
 * inject it as a singleton, because it may get different connections from pool
 * according to different users. So do not register this class as a Spring bean.
 * I provide two constructors to initialize this template, one for common user
 * and one for specified user.
 * <p>
 * There is no significant difference between this template and JDBC API, they
 * both use SQL to query from database. This template only provide some
 * convenient methods to convert {@link ResultSet} to java bean by
 * {@link RowMapper}. It is not elegant enough but lightweight and handy.
 *
 * @company Oracle
 * @author liangx
 * @date Sep 25, 2015
 */
public class JdbcTemplate {

	private static final Logger logger = LoggerFactory
			.getLogger(JdbcTemplate.class);

	private final ConnectionFactory connectionFactory;
	private String username = null;
	private String password = null;

	public JdbcTemplate(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public JdbcTemplate(ConnectionFactory connectionFactory, String username,
			String password) {
		this.connectionFactory = connectionFactory;
		this.username = username;
		this.password = password;
	}

	private ConnectionProxy getConnectionProxy() {
		return connectionFactory.getConnectionProxy(username, password);
	}

	public <T> List<T> query(String sql, Map<String, Object> params,
			Class<T> clazz) {
		List<T> result = new ArrayList<T>();

		logger.debug("Executing SQL query [" + sql + "]");
		ConnectionProxy connectionProxy = getConnectionProxy();
		Connection connection = connectionProxy.getConnection();
		NamedParameterStatement p;
		try {
			p = new NamedParameterStatement(connection, sql);
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				p.setObject(entry.getKey(), entry.getValue());
			}
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				result.add(ReflectionUtil.convertRow2Bean(rs, clazz));
			}
		} catch (Exception e) {
			logger.error("query error", e);
		} finally {
			connectionProxy.close();
		}
		return result;
	}

	public <T> List<T> query(String sql, RowMapper<T> rowMapper) {

		return null;
	}

	public <T> T query(final String sql, final ResultSetExtractor<T> rse) {
		logger.debug("Executing SQL query [" + sql + "]");

		return null;
	}

}
