package com.oracle.bugcamp.jdbc.pool;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.oracle.bugcamp.jdbc.ConnectionProxy;

/**
 * This class use a {@link LinkedHashMap} to store connections. The priority of
 * a connection only depend on the add order (first in first out). User can also
 * get a connection from the map by username.
 *
 * @company Oracle
 * @author liangx
 * @date Sep 25, 2015
 */
public class ConnectionStore {

	private LinkedHashMap<String, ProxyNode> proxyMap = new LinkedHashMap<String, ProxyNode>();

	public void add(ConnectionProxy proxy) {
		proxyMap.put(proxy.getUsername() == null ? UUID.randomUUID().toString()
				: proxy.getUsername(), new ProxyNode(proxy));
	}

	public ConnectionProxy remove(String username) {
		if (proxyMap.size() == 0) {
			return null;
		}
		if (null == username) {
			username = proxyMap.entrySet().iterator().next().getKey();
		}
		if (null == proxyMap.get(username)) {
			return null;
		}
		return proxyMap.remove(username).element;
	}

	public ConnectionProxy removeIdle(long period) {
		if (proxyMap.size() == 0) {
			return null;
		}
		long now = System.currentTimeMillis();
		Map.Entry<String, ProxyNode> firstEntry = proxyMap.entrySet()
				.iterator().next();
		ProxyNode node = firstEntry.getValue();
		if (node == null || now - node.entryTime < period) {
			return null;
		}
		return proxyMap.remove(firstEntry.getKey()).element;
	}

	private final static class ProxyNode {
		ConnectionProxy element;

		long entryTime;

		ProxyNode(ConnectionProxy element) {
			this.element = element;
			this.entryTime = System.currentTimeMillis();
		}
	}
}
