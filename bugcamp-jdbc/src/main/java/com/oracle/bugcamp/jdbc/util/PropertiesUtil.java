package com.oracle.bugcamp.jdbc.util;

import java.io.IOException;
import java.util.Properties;

/**
 * This is a utility to get value by key in <code>jdbc.properties</code>. It is
 * deprecated as I recommend to use Spring to load properties.
 *
 * @company Oracle
 * @author liangx
 * @date Sep 23, 2015
 */
@Deprecated
public class PropertiesUtil {

	private PropertiesUtil() {
	}

	private static class PropertiesHolder {

		private static Properties properties;

		static Properties getInstance() throws IOException {
			if (null == properties) {
				properties = new Properties();
				properties.load(PropertiesUtil.class.getClassLoader()
						.getResourceAsStream("jdbc.properties"));
			}
			return properties;
		}
	}

	public static String getValue(String key) {
		try {
			return PropertiesHolder.getInstance().getProperty(key);
		} catch (IOException e) {
			return "";
		}
	}

}
