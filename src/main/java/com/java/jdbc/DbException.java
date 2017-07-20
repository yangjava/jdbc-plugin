package com.java.jdbc;

public class DbException extends RuntimeException{
	
	
	private static final long serialVersionUID = 342820722361408621L;
	
	public DbException(String message) {
		super(message);
	}
	
	public DbException(Throwable cause) {
		super(cause);
	}
	
	public DbException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
