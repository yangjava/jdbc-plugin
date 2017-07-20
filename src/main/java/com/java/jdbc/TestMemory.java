package com.java.jdbc;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import com.java.jdbc.db.DbHelper;
import com.java.jdbc.handler.BeanListHandler;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestMemory {
    
	Memory memory=MemoryFactory.getInstance();
	@Test
	public void testMemory(){
		String  sql="select * from  user where id =? ";
		List<User> query = memory.query(sql, new BeanListHandler<User>(User.class), "1");
		System.out.println(query);
	}
	
	@Test
	public void testFindUserById() throws SQLException{
		QueryRunner queryRunner=new QueryRunner(new ComboPooledDataSource("mySource"));
		String sql="select * from user where id = ?";
		User user=(User)queryRunner.query(sql, new BeanHandler<User>(User.class),"1");
		System.out.println(user);
	}
	
	@Test
	public void testDbHelper() throws SQLException{
		String sql="select * from user where id = ?";
		User user=DbHelper.query(User.class,sql, "1");
		System.out.println(user);
	}
	
	
	
}
