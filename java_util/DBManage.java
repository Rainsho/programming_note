package com.rainsho.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManage {
	static String user = "sa";
	static String pswd = "root";

	// 连接MSSQL
	static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static String url = "jdbc:sqlserver://localhost:1433;databasename=[DB_name]";

	// 连接MySQL
	// static String driver = "com.mysql.jdbc.Driver";
	// static String url = "jdbc:mysql://localhost:3306/[DB_name]";
	// static String ssl = "?useSSL=false&characterEncoding=utf8";
	// 连接Oracle
	// static String driver = "oracle.jdbc.driver.OracleDriver";
	// static String url = "jdbc:oracle:thin:@localhost:1521:[DB_name]";

	static Connection con = null;
	static PreparedStatement pst = null;
	static ResultSet rs = null;

	private static Connection getConnect() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pswd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public static ResultSet getResultSet(String sql) {
		con = getConnect();
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet getResultSet(String sql, Object[] params) {
		con = getConnect();
		try {
			pst = con.prepareStatement(sql);
			for (int i = 0; i < params.length; i++)
				pst.setObject(i + 1, params[i]);
			rs = pst.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static int modifyEntiy(String sql, Object[] params) {
		int num = 0;
		con = getConnect();
		try {
			pst = con.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				pst.setObject(i + 1, params[i]);
			}
			num = pst.executeUpdate();
			closeAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}

	public static void closeAll() {
		if (pst != null) {
			try {
				pst.close();
				pst = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}