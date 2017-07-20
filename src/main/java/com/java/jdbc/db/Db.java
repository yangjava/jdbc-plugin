package com.java.jdbc.db;

import java.util.List;


/**
 * 核心类,用于SQL查询
 * @author:杨京京
 * @QQ:1280025885
 */
public class Db {

	public static <T> T selectOne(Class<T> Class, String sql, Object... params) {
        return DbHelper.query(Class, sql, params);
    }
	
	public static <T> T selectOne(Class<T> Class) {
        return null;
    }
	
    public static <T> List<T> selectList(Class<T> entityClass) {
        return null;
    }
    
    
    
}
