package com.oracle.bugcamp.jdbc.util;

import java.util.concurrent.CountDownLatch;

import com.oracle.bugcamp.jdbc.JdbcException;

public class ConcurrentUtil {

	private static CountDownLatch startLatch = new CountDownLatch(1);

	public static void countDownStartLatch() {
		if (startLatch != null) {
			startLatch.countDown();
		}
	}

	public static void await() {
		try {
			if (startLatch != null) {
				startLatch.await();
				startLatch = null;
			}
		} catch (InterruptedException e) {
			throw new JdbcException(e);
		}
	}
}
