package com.choice.wxc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class CodeHelper {
	
	/**
	 * 部门编号的每一分段长度，如为4则code将类似1234-5678
	 */
	public static int CODE_LENGTH = 4;
	
	public static String QrcodePic=getProjectPath();
	
	public static String createUUID(){
		return String.valueOf(UUID.randomUUID()).replaceAll("-", "");
	}
	public static String getProjectPath(){
	    String nowpath;             //当前tomcat的bin目录的路径 如 D:\java\software\apache-tomcat-6.0.14\bin   
		String tempdir;   
		nowpath =  CodeHelper.class.getClassLoader().getResource("../../").getPath();
//		nowpath=System.getProperty("user.dir");   
//		tempdir=nowpath.replace("bin", "webapps");  //把bin 文件夹变到 webapps文件里面    
		tempdir=nowpath+"qrcode"+File.separator;  //拼成D:\java\software\apache-tomcat-6.0.14\webapps\sz_pro    
	    return tempdir;
	}
	public static void main(String[] args){
		System.out.println();
//		System.out.println(new CodeHelper().getProjectPath());
	}
	/**
	 * 获取openID
	 * @param appId
	 * @param secret
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String getOpenID(String appId, String secret, String code)
			throws Exception {
		String openId = "";
		try{
			URL url = new URL(
						"https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+secret+"&code="+code+"&grant_type=authorization_code");
				InputStream in = url.openStream();
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != reader) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			String json = buffer.toString();
			openId = json.substring(json.lastIndexOf("openid\":\"") + 9,
					json.lastIndexOf("\",\"scope"));
		}catch(Exception e){
			e.printStackTrace();
			return "idkdfjdjwkdkdngngng";
		}
		return openId;
	}
}
