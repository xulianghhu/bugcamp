package com.oracle.bugcamp.jdbc;

/**
 * Common runtime exception
 *
 * @company Oracle
 * @author liangx
 * @date Sep 24, 2015
 */
public class JdbcException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JdbcException() {
		super();
	}

	public JdbcException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JdbcException(String message, Throwable cause) {
		super(message, cause);
	}

	public JdbcException(String message) {
		super(message);
	}

	public JdbcException(Throwable cause) {
		super(cause);
	}

}
