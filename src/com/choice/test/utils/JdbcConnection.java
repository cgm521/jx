package com.choice.test.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
/**
 * jdbc数据库链接工具类
 * @author 孙胜彬
 */
public class JdbcConnection {
	//连接CRM数据库
	public Connection getCRMConnection(){
		Connection conn = null;
		//获取数据库连接文件
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//设置连接数据库连接参数
		String driver = properties.getProperty("jdbc.driver_crm");
		String url = properties.getProperty("jdbc.url_crm");
		String uname =properties.getProperty("jdbc.username_crm");
		String password1 =properties.getProperty("jdbc.password_crm");
		try{
			Class.forName(driver); 
			conn=DriverManager.getConnection(url, uname, password1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	//连接TELE数据库
	public Connection getTELEConnection(){
		Connection conn = null;
		//获取数据库连接文件
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//设置连接数据库连接参数
		String driver = properties.getProperty("jdbc.driver_tele");
		String url = properties.getProperty("jdbc.url_tele");
		String uname =properties.getProperty("jdbc.username_tele");
		String password1 =properties.getProperty("jdbc.password_tele");
		
		try{
			Class.forName(driver); 
			conn=DriverManager.getConnection(url, uname, password1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	//连接OSP数据库
	public Connection getOSPConnection(){
		Connection conn = null;
		//获取数据库连接文件
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//设置连接数据库连接参数
		String driver = properties.getProperty("jdbc.driver_osp");
		String url = properties.getProperty("jdbc.url_osp");
		String uname =properties.getProperty("jdbc.username_osp");
		String password1 =properties.getProperty("jdbc.password_osp");
		
		try{
			Class.forName(driver); 
			conn=DriverManager.getConnection(url, uname, password1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	//关闭数据库连接
	public void closeAll(Connection conn,PreparedStatement ps,ResultSet rs){
		if(rs!=null){
			try{
				rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(ps !=null){
			try{
				ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(conn!=null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public String getSocketServerIP() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("webservice-socket-ipconfig.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String ip = properties.getProperty("ip");
		String port = properties.getProperty("port");
		return ip+"@"+port;
	}
	
}
