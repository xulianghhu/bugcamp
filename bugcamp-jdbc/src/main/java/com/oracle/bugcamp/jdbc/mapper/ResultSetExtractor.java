package com.oracle.bugcamp.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetExtractor<T> {

	T extractData(ResultSet rs) throws SQLException;

}
