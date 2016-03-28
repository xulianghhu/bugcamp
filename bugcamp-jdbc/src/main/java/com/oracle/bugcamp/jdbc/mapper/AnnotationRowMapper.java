package com.oracle.bugcamp.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationRowMapper<T> implements RowMapper<T> {

	private static final Map<Class<?>, Object> cache = new ConcurrentHashMap<Class<?>, Object>();

	final Class<T> typeParameterClass;

	public AnnotationRowMapper(Class<T> typeParameterClass) {
		this.typeParameterClass = typeParameterClass;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
