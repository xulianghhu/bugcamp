package com.oracle.bugcamp.jdbc;

/**
 * The JDBC configuration should be provided by user. It is recommended to
 * register this class as a Spring bean.
 * 
 * @company Oracle
 * @author liangx
 * @date Sep 23, 2015
 */
public class JdbcConfiguration {

	private String url;
	private String username;
	private String password;
	private String driverClass;
	private int initialPoolSize;
	private int corePoolSize;
	private int maxPoolSize;
	private int retryTimes;
	private int aquireIncrement;
	private int limitIdleTime;
	private String testSql;
	private int testPeriod;
	private boolean testBeforeUse;
	private boolean showSql;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public int getInitialPoolSize() {
		return initialPoolSize;
	}

	public void setInitialPoolSize(int initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public int getAquireIncrement() {
		return aquireIncrement;
	}

	public void setAquireIncrement(int aquireIncrement) {
		this.aquireIncrement = aquireIncrement;
	}

	public int getLimitIdleTime() {
		return limitIdleTime;
	}

	public void setLimitIdleTime(int limitIdleTime) {
		this.limitIdleTime = limitIdleTime;
	}

	public String getTestSql() {
		return testSql;
	}

	public void setTestSql(String testSql) {
		this.testSql = testSql;
	}

	public int getTestPeriod() {
		return testPeriod;
	}

	public void setTestPeriod(int testPeriod) {
		this.testPeriod = testPeriod;
	}

	public boolean isTestBeforeUse() {
		return testBeforeUse;
	}

	public void setTestBeforeUse(boolean testBeforeUse) {
		this.testBeforeUse = testBeforeUse;
	}

	public boolean isShowSql() {
		return showSql;
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

}
