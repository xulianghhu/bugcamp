package com.oracle.bugcamp.jdbc.util;

import java.sql.Driver;

import com.oracle.bugcamp.jdbc.JdbcException;

/**
 * Utility for JDBC
 *
 * @company Oracle
 * @author liangx
 * @date Sep 23, 2015
 */
public class JdbcUtil {
	
	public static Driver createDriver(String driverClassName) throws JdbcException {
		return createDriver(null, driverClassName);
	}

	public static Driver createDriver(ClassLoader classLoader, String driverClassName) throws JdbcException {
		Class<?> clazz = null;
		if (classLoader != null) {
			try {
				clazz = classLoader.loadClass(driverClassName);
			} catch (ClassNotFoundException e) {
				throw new JdbcException("Driver class not found!", e);
			}
		}
		if (clazz == null) {
			try {
				ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
				if (contextLoader != null) {
					clazz = contextLoader.loadClass(driverClassName);
				}
			} catch (ClassNotFoundException e) {
				throw new JdbcException("Driver class not found!", e);
			}
		}
		if (clazz == null) {
			try {
				clazz = Class.forName(driverClassName);
			} catch (ClassNotFoundException e) {
				throw new JdbcException("Driver class not found!", e);
			}
		}
		try {
			return (Driver) clazz.newInstance();
		} catch (Exception e) {
			throw new JdbcException("Fail to create driver instance!", e);
		}
	}
}
