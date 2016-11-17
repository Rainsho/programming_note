package com.rainsho.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtil { // 获取SqlSession工具类
	// 单例SqlSessionFactory对象
	private final static SqlSessionFactory sqlSessionFactory;
	static { // 静态语句块，仅执行一次
		String resource = "mybatis-config.xml";
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource); // 读取配置文件
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		// 创建SqlSessionFactory对象
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
	}

	// 返回单例SqlSessionFactory对象
	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}