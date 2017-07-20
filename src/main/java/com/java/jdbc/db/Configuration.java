package com.java.jdbc.db;

import java.sql.Connection;

import javax.sql.DataSource;

import com.java.jdbc.db.dialect.Dialect;



public class Configuration {

	private final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	
	String name;
	DataSource dataSource;
	
	Dialect dialect;
	boolean showSql;
	boolean devMode;
	int transactionLevel;
/*	IContainerFactory containerFactory;
	ICache cache;*/
	
	/**
	 * Constructor with full parameters
	 * @param name the name of the config
	 * @param dataSource the dataSource
	 * @param dialect the dialect
	 * @param showSql the showSql
	 * @param devMode the devMode
	 * @param transactionLevel the transaction level
	 * @param containerFactory the containerFactory
	 * @param cache the cache
	 */
	public Configuration(String name, DataSource dataSource, Dialect dialect, boolean showSql, boolean devMode, int transactionLevel) {
		if (isBlank(name))
			throw new IllegalArgumentException("Config name can not be blank");
		if (dataSource == null)
			throw new IllegalArgumentException("DataSource can not be null");
		if (dialect == null)
			throw new IllegalArgumentException("Dialect can not be null");
		
		this.name = name.trim();
		this.dataSource = dataSource;
		this.dialect = dialect;
		this.showSql = showSql;
		this.devMode = devMode;
		this.transactionLevel = transactionLevel;
	}
	
	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim());
	}
	public Configuration() {
		
	}
	

	
	public String getName() {
		return name;
	}
	
	public Dialect getDialect() {
		System.out.println("Mysql");
		return dialect;
	}
	
	
	public int getTransactionLevel() {
		return transactionLevel;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	
	public boolean isShowSql() {
		return showSql;
	}
	
	public boolean isDevMode() {
		return devMode;
	}
	
	// --------
	
	/**
	 * Support transaction with Transaction interceptor
	 */
	public final void setThreadLocalConnection(Connection connection) {
		threadLocal.set(connection);
	}
	
	public final void removeThreadLocalConnection() {
		threadLocal.remove();
	}
	
	/**
	 * Get Connection. Support transaction if Connection in ThreadLocal
	 */
/*	public final Connection getConnection() throws SQLException {
		Connection conn = threadLocal.get();
		if (conn != null)
			return conn;
		return showSql ? new SqlReporter(dataSource.getConnection()).getConnection() : dataSource.getConnection();
	}*/
	
	/**
	 * Helps to implement nested transaction.
	 * Tx.intercept(...) and Db.tx(...) need this method to detected if it in nested transaction.
	 */
	public final Connection getThreadLocalConnection() {
		return threadLocal.get();
	}
	
	/**
	 * Return true if current thread in transaction.
	 */
	public final boolean isInTransaction() {
		return threadLocal.get() != null;
	}
	
}

