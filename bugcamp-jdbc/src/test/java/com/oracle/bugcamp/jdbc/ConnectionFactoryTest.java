package com.oracle.bugcamp.jdbc;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class ConnectionFactoryTest {

	@Autowired
	private ConnectionFactory connectionFactory;

	@Test
	public void testGetConnection() throws SQLException {
//		for (int i = 0; i < 10; i++) {
//			ConnectionProxy connectionProxy = connectionFactory.getConnectionProxy();
//			System.out.print(connectionProxy.getConnection());
//			System.out.print("-------");
//			System.out.print(connectionFactory.getConnectionPool().getPoolSize());
//			System.out.print("-------");
//			System.out.println(connectionFactory.getConnectionPool().getPeakSize());
//			// run sql here ...
//			connectionProxy.close();
//		}
		
		for (int i = 0; i < 10; i++) {
			ConnectionProxy connectionProxy = connectionFactory.getConnectionProxy("liangx", "Xl000111");
			System.out.print(connectionProxy.getConnection());
			System.out.print("-------");
			System.out.print(connectionFactory.getConnectionPool().getPoolSize());
			System.out.print("-------");
			System.out.println(connectionFactory.getConnectionPool().getPeakSize());
			// run sql here ...
			connectionProxy.close();
		}
	}
}
