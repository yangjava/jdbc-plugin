package com.java.jdbc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.jdbc.handler.ResultSetHandler;

public class DataAccessor {

	private static final Logger logger = LoggerFactory.getLogger(DataAccessor.class);
    
	public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh,
			Object... params) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		T result = null;
		try {
			stmt = conn.prepareStatement(sql);
			this.fillStatement(stmt, params);
			rs = stmt.executeQuery();
			result = rsh.handle(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
		}
		return result;
	}
	
	
	private void fillStatement(PreparedStatement stmt, Object... params) {
		if (params == null)
			return;
		try {
			for (int i = 0; i < params.length; i++) {
			stmt.setObject(i + 1, params[i]);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
