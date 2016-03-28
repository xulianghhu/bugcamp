package com.oracle.bugcamp.jdbc.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility for reflection usage.
 *
 * @company Oracle
 * @author liangx
 * @date Sep 28, 2015
 */
public class ReflectionUtil {

	public static Object getProperty(Object obj, String propertyName) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] descriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				if (descriptor.getReadMethod() != null
						&& descriptor.getName().equals(propertyName)) {
					return descriptor.getReadMethod().invoke(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setProperty(Object obj, String propertyName,
			Object propertyValue) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] descriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				if (descriptor.getWriteMethod() != null
						&& descriptor.getName().equals(propertyName)) {
					descriptor.getWriteMethod().invoke(obj, propertyValue);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static <T> T convertRow2Bean(ResultSet rs, Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] descriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				Method writeMethod = descriptor.getWriteMethod();
				String propertyName = descriptor.getName();
				if (writeMethod != null) {
					try {
						Object propertyValue = rs.getObject(propertyName);
						if (null != propertyValue) {
//							System.out.println(propertyName + " => " + propertyValue.getClass());
							if (propertyValue.getClass() == BigDecimal.class) {
								writeMethod.invoke(instance, ((BigDecimal)propertyValue).longValue());
							} else {
								writeMethod.invoke(instance, propertyValue);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
}
