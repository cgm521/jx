package com.choice.test.utils;

import java.io.BufferedReader;  
 import java.io.InputStream;  
 import java.io.InputStreamReader;  
 import java.io.OutputStreamWriter;  
 import java.net.HttpURLConnection;  
 import java.net.URL;  
 
 public class caidan_bak {  
     public static void createMenu(String params,String accessToken) {
         StringBuffer bufferRes = new StringBuffer();
         try {
        	 //https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN
        	 //https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN
//        	 http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE
        	 //测试测试环境url
//        	 URL realUrl = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxd6c7035d76ad8ab0&secret=aab813fdf22f20707e3f4d5cf5be1dbf");
//        	 URL realUrl = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxd6c7035d76ad8ab0&secret=aab813fdf22f20707e3f4d5cf5be1dbf");
        	 URL realUrl = new URL("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken);
             HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
             // 连接超时  ​​
             conn.setConnectTimeout(25000);
             // 读取超时 --服务器响应比较慢，增大时间
             conn.setReadTimeout(25000);
             HttpURLConnection.setFollowRedirects(true);
             // 请求方式
             conn.setRequestMethod("GET");
             conn.setDoOutput(true);
             conn.setDoInput(true);
             conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");  
             conn.setRequestProperty("Referer", "https://api.weixin.qq.com/");  
             conn.connect();  
             // 获取URLConnection对象对应的输出流
             OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
             // 发送请求参数
             //out.write(URLEncoder.encode(params,"UTF-8"));
             out.write(params);
             out.flush();
             out.close();
             InputStream in = conn.getInputStream();
             BufferedReader read = new BufferedReader(new InputStreamReader(in,"UTF-8"));
             String valueString = null;
             while ((valueString=read.readLine())!=null){
                 bufferRes.append(valueString);
             }
             System.out.println(bufferRes.toString());
             in.close();
             if (conn != null) {
                 // 关闭连接
                 conn.disconnect();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }//,{\"type\":\"view\",\"name\":\"刮刮乐\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://115.238.164.148/ChoiceWeChat/games/toScratch.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}
     public static void main(String[] args) {
    	 String s = "{\"button\":[{\"name\":\"精彩优惠\",\"sub_button\":[{\"type\":\"view\",\"name\":\"优惠信息\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://115.238.164.148/ChoiceWeChat/pubitem/findFavorArea.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}]}," +
	 							  "{\"name\":\"预订点餐\",\"sub_button\":[{\"type\":\"view\",\"name\":\"预定桌台\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://115.238.164.148/ChoiceWeChat/pubitem/listOrderFirst.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
											 							",{\"type\":\"view\",\"name\":\"在线点餐\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://115.238.164.148/ChoiceWeChat/pubitem/onlineBook.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
											 							",{\"type\":\"view\",\"name\":\"我的预定\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://115.238.164.148/ChoiceWeChat/pubitem/getOrderFirm.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}]}," +
								  "{\"type\":\"view\",\"name\":\"会员卡\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://115.238.164.148/ChoiceWeChat/card/addCard.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
					 "]}";

//    	 String s = "{\"button\":[{\"name\":\"活动细则\",\"sub_button\":[{\"type\":\"click\",\"name\":\"会展须知\",\"key\":\"1\"}" +
//																	   ",{\"type\":\"click\",\"name\":\"日程安排\",\"key\":\"2\"}]}," +
//																		"{\"type\":\"click\",\"name\":\"分项活动\",\"key\":\"3\"},"
//							   + "{\"name\":\"会员服务\",\"sub_button\":[{\"type\":\"click\",\"name\":\"组织架构\",\"key\":\"4\"}" +
//																	   ",{\"type\":\"click\",\"name\":\"联系我们\",\"key\":\"5\"}]}";
    	 
         String accessToken = "nuqOCWWSkT9NedqqqgyN7LzXY1YNnRn-8M5au3aQwQQcd429OsLcs1swQlvmHJftYhwQG79qKkAL4npy0g05X1mXhAu7sTNv4bf-Z5qsdcU";// 你自己的token  
         createMenu(s,accessToken);
     }
 }