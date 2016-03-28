package com.oracle.bugcamp.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class JdbcDataSourceTest {

	@Autowired
	private JdbcDataSource dataSource;
	
	@Test
	public void testGetConnection() throws SQLException {
		Connection commonConnection = dataSource.getConnection();
		System.out.println(commonConnection.getSchema());
		
		Connection specifiedConnection = dataSource.getConnection("liangx", "Xl000111");
		System.out.println(specifiedConnection.getSchema());
	}
}
