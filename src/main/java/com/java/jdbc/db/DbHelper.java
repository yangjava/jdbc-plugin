package com.java.jdbc.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.java.jdbc.db.dialect.MySqlDialect;
import com.java.jdbc.handler.BeanHandler;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DbHelper {
 
	
	
	static Configuration configuration = new Configuration();
	
	private static ComboPooledDataSource ds = null;
	
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
    
    private static final DataAccessor dataAccessor = new DataAccessor();
    
    private static final String databaseType =null;
    
    static{
        try{
            ds = new ComboPooledDataSource("mySource");
            configuration.getDialect();
        }catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
    * @Method: getConnection
    * @Description: 从数据源中获取数据库连接
    * @return Connection
    * @throws SQLException
    */ 
    public static Connection getConnection() {
//        //从当前线程中获取Connection
//        Connection conn = threadLocal.get();
//        if(conn==null){
//            //从数据源中获取数据库连接
//            conn = getDataSource().getConnection();
//            //将conn绑定到当前线程
//            threadLocal.set(conn);
//        }
//        return conn;
        Connection conn;
        try {
            // 先从 ThreadLocal 中获取 Connection
            conn = threadLocal.get();
            if (conn == null) {
                // 若不存在，则从 DataSource 中获取 Connection
                conn = getDataSource().getConnection();
                // 将 Connection 放入 ThreadLocal 中
                if (conn != null) {
                	threadLocal.set(conn);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
    
    /**
    * @Method: startTransaction
    * @Description: 开启事务
    *
    */ 
    public static void startTransaction(){
        try{
            Connection conn =  threadLocal.get();
            if(conn==null){
                conn = getConnection();
                 //把 conn绑定到当前线程上
                threadLocal.set(conn);
            }
            //开启事务
            conn.setAutoCommit(false);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
    * @Method: rollback
    * @Description:回滚事务
    *
    */ 
    public static void rollback(){
        try{
            //从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if(conn!=null){
                //回滚事务
                conn.rollback();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
    * @Method: commit
    * @Description:提交事务
    *
    */ 
    public static void commit(){
        try{
            //从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if(conn!=null){
                //提交事务
                conn.commit();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
    * @Method: close
    * @Description:关闭数据库连接(注意，并不是真的关闭，而是把连接还给数据库连接池)
    *
    */ 
    public static void close(){
        try{
            //从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if(conn!=null){
                conn.close();
                 //解除当前线程上绑定conn
                threadLocal.remove();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
    * @Method: getDataSource
    * @Description: 获取数据源
    * @return DataSource
    */ 
    public static DataSource getDataSource(){
        //从数据源中获取数据库连接
        return ds;
    }
    
    
    public static <T> T query(Class<T> entityClass, String sql, Object... params)  {
    	configuration.getDialect();
		return dataAccessor.query(getConnection(), sql, new BeanHandler<T>(entityClass), params);
    }
    
    

    
/*	public <T> T query( String sql, ResultSetHandler<T> rsh,
			Object... params) {
		Connection conn;
		try {
			conn = getConnection();
			return query(conn, sql, rsh, params);
		} catch (SQLException e) {
		}
		return null;
	}
	
	public <T> T query( String sql, Class<T> entityClass,
			Object... params) {
		Connection conn;
		try {
			conn = getConnection();
			return query(conn, sql, new BeanHandler<T>(entityClass), params);
		} catch (SQLException e) {
		}
		return null;
	}
	
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
			printSQL(sql, params);
			throw new RuntimeException(e);
		} finally {
			close(rs, stmt, conn);
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
	
	
	
	
	private void close(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			close(stmt, conn);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void close(Statement stmt, Connection conn) {
		try {
			if (stmt != null) {
				stmt.close();
			}
			close(conn);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void close(Connection conn) {
		try {
			if (conn != null && conn.getAutoCommit()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}*/
	
	
	
	
	private void printSQL(final String sql, final Object[] params){
		if (!match(sql, params)) {
			System.out.println(sql);
			return;
		}
	}
	
	
	private boolean match(String sql, Object[] params) {
		Matcher m = Pattern.compile("(\\?)").matcher(sql);
		int count = 0;
		while (m.find()) {
			count++;
		}
		return count == params.length;
	}
	
	
	
	
	
	
	
}
