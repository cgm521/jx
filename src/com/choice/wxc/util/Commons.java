package com.choice.wxc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Commons {
	/***********************************webService接口*******************************************/
	public static String CRMwebService=Commons.getConfig().getProperty("CRMwebService");
	public static String BOHwebService=Commons.getConfig().getProperty("BOHwebService");
	
	/*****************************************CTF访问路径*******************************/
	public static String CTFUrl = Commons.getConfig().getProperty("CTFUrl");
	
	/*****************************************总部BOH访问路径 *******************************/
	public static String BOHUrl = Commons.getConfig().getProperty("BOHUrl");
	
	/*****************************************数据库标志*******************************/
	public static String databaseType = Commons.getConfig().getProperty("databaseType");
	
	private static Properties properties = null;
	//获取config配置文件的内容
	public static Properties getConfig(){
		if(properties == null){
			//获取文件流
			InputStream inputStream = Commons.class.getClassLoader().getResourceAsStream("config.properties");
			BufferedReader bf = null;
			properties = new Properties();
			try {
				bf = new BufferedReader(new InputStreamReader(inputStream,"UTF-8")); 
				properties.load(bf);
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally{
				if(bf != null){
					try {
						bf.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(inputStream != null){
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return properties;
	}
	
	public static void main(String args[]){
	}
}
