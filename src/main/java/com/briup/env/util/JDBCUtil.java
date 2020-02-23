package com.briup.env.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtil {
	
	private static String driverClass;
	private static String url;
	private static String user;
	private static String password;
	
	static {
		//init 
		//简单的值一般存放在  xxxx.properties 资源文件 内容都是key=value形式
		Properties pp = new Properties();
		InputStream is =null;		
		try {
			//is = new FileInputStream("src/jdbc.properties");
			is = JDBCUtil.class.getResourceAsStream("/jdbc.properties");
			//is = JDBCUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
			pp.load(is);
			driverClass = pp.getProperty("jdbc.driverClass");
			url = pp.getProperty("jdbc.url");
			user = pp.getProperty("jdbc.user");
			password = pp.getProperty("jdbc.password");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public static void main(String[] args) {
		System.out.println(driverClass);
		System.out.println(url);
		System.out.println(user);
		System.out.println(password);
		
	}*/
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName(driverClass);
		Connection connection = DriverManager.getConnection(url,user,password);
		System.out.println(connection);
		return connection;
	}
	
	public static void close(ResultSet rs,Statement stmt,Connection conn)
	{
		try {
			if(rs==null)
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(stmt==null)
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(conn==null)
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void close(Statement stmt,Connection conn)
	{
		close(null, stmt, conn);
	}
	
	public static void executeDMLByStmt(String sql) throws ClassNotFoundException
	{
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			stmt.execute(sql);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				close(stmt, conn);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	public static void executeDMLByPs(String sql,IPSwork work) throws ClassNotFoundException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			work.doWork(ps);
			ps.execute();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				close(ps, conn);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	public static Object executeSelectByStmt(String sql,IRswork work)
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();
		    rs = stmt.executeQuery(sql);
		    Object o = work.doWork(rs);
		    return o;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
			close(rs, stmt, conn);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
}
