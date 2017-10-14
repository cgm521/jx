package com.choice.test.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.choice.wechat.util.WeChatUtil;
 
 public class caidan {  
     public static void createMenu(String params,String accessToken) {
         StringBuffer bufferRes = new StringBuffer();
         try {
        	 //https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN
        	 //https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN
//        	 http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE
        	 //测试测试环境url
        	// URL realUrl = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxd6c7035d76ad8ab0&secret=aab813fdf22f20707e3f4d5cf5be1dbf");
        	//啦啦咖啡
//        	 URL realUrl = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxc1437f5d60f3755e&secret=4d879044188e5c2e262e4ebcbbd2c12e");
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
     }//,{\"type\":\"view\",\"name\":\"刮刮乐\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://183.62.205.180/ChoiceWeChat/games/toScratch.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}
     public static void main(String[] args) {
//    	 String s = "{\"button\":[{\"type\":\"view\",\"name\":\"微商城\",\"url\":\"http://weigou.qq.com/wkd/modulepage/2918381097/930079/0/0\"}," +
//	 							  "{\"type\":\"view\",\"name\":\"订单查询\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc1437f5d60f3755e&redirect_uri=http://183.62.205.180/ChoiceWeChat/pubitem/getOrderFirm.do&response_type=codescope=snsapi_basestate=123#wechat_redirect\"}," +
//	 							  "{\"type\":\"view\",\"name\":\"会员卡\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://183.62.205.180/ChoiceWeChat/card/addCard.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
//					 "]}";

//    	 String s = "{\"button\":[{\"name\":\"精彩优惠\",\"sub_button\":[{\"type\":\"view\",\"name\":\"优惠信息\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://183.62.205.180/ChoiceWeChat/pubitem/findFavorArea.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}]}," +
//				  "{\"name\":\"预订点餐\",\"sub_button\":[{\"type\":\"view\",\"name\":\"预定桌台\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://183.62.205.180/ChoiceWeChat/pubitem/listOrderFirst.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
//							 							",{\"type\":\"view\",\"name\":\"在线点餐\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://183.62.205.180/ChoiceWeChat/pubitem/onlineBook.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
//							 							",{\"type\":\"view\",\"name\":\"我的预定\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd6c7035d76ad8ab0&redirect_uri=http://183.62.205.180/ChoiceWeChat/pubitem/getOrderFirm.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}]}," +
//							 							  "{\"name\":\"会员服务\",\"sub_button\":[{\"type\":\"view\",\"name\":\"会员卡\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=http://pagekitezgl.pagekite.me/ChoiceWeChat/card/addCard.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
//							 							",{\"type\":\"view\",\"name\":\"我的会员卡\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=http://pagekitezgl.pagekite.me/ChoiceWeChat/myCard/cardInfo.do?pk_group=15579ECB5B8348SFB621&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}]}" +
//				  "]}";
    	 
//    	 String s = "{\"button\":[{\"name\":\"活动细则\",\"sub_button\":[{\"type\":\"click\",\"name\":\"会展须知\",\"key\":\"1\"}" +
//																	   ",{\"type\":\"click\",\"name\":\"日程安排\",\"key\":\"2\"}]}," +
//																		"{\"type\":\"click\",\"name\":\"分项活动\",\"key\":\"3\"},"
//							   + "{\"name\":\"会员服务\",\"sub_button\":[{\"type\":\"click\",\"name\":\"组织架构\",\"key\":\"4\"}" +
//																	   ",{\"type\":\"click\",\"name\":\"联系我们\",\"key\":\"5\"}]}";
    	 String s = "{\"button\":[{\"name\":\"精彩优惠\",\"sub_button\":[{\"type\":\"view\",\"name\":\"优惠信息\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=http://183.62.205.180/ChoiceWeChat/pubitem/findFavorArea.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}]}," +
    			 			"{\"name\":\"预订点餐\",\"sub_button\":[{\"type\":\"view\",\"name\":\"预定桌台\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=https://pagekitezgl.pagekite.me/ChoiceWeChat/bookDesk/listFirm.do?pk_group=15579ECB5B8348SFB621&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
							 							",{\"type\":\"view\",\"name\":\"在线点餐\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=https://pagekitezgl.pagekite.me/ChoiceWeChat/bookMeal/listFirm.do?pk_group=15579ECB5B8348SFB621&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
							 							",{\"type\":\"view\",\"name\":\"我的点菜单\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=http://pagekitezgl.pagekite.me/ChoiceWeChat/bookDesk/findMenuOrders.do?pk_group=15579ECB5B8348SFB621&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
							 							",{\"type\":\"view\",\"name\":\"订位\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=http://pagekitezgl.pagekite.me/ChoiceWeChat/bookDesk/listFirm.do?pk_group=15579ECB5B8348SFB621&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
//							 							",{\"type\":\"view\",\"name\":\"扫码下单\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=http://pagekitezgl.pagekite.me/ChoiceWeChat/sceneMeal/openScanSceneForUpdateOrder.do?pk_group=15579ECB5B8348SFB621&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
							 							",{\"type\":\"view\",\"name\":\"我的预定\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=http://183.62.205.180/ChoiceWeChat/pubitem/getOrderFirm.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}]}," +
				  "{\"name\":\"会员服务\",\"sub_button\":[{\"type\":\"view\",\"name\":\"会员卡\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=http://pagekitezgl.pagekite.me/ChoiceWeChat/card/addCard.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}" +
							 							",{\"type\":\"view\",\"name\":\"我的会员卡\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb677527b5459000f&redirect_uri=http://pagekitezgl.pagekite.me/ChoiceWeChat/myCard/cardInfo.do?pk_group=15579ECB5B8348SFB621&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}]}," +
				  "]}";
//    	 System.out.println(s);
         String accessToken = "8M68spFKGq2_dOzcjWVbbv8Ioe5cieNkRyWcHN7jk--tTO0xhCsgkYHVeaqJIFeSwM17S2DiWLuGeNBvN5VvXEN0TaZcmnDk1UI9e_t3woQ";// 你自己的token  
         createMenu(s,accessToken);
//         System.out.println(WeChatUtil.getAccessToken("wxb677527b5459000f", "96e58aa64fa69a2f6a88c48686950114").getToken());
         
//         System.out.println(getGson());
         
//         createMenu(getGson(),accessToken);
         
     }
     
     
     
    public static String getGson(){
    	
    	 JSONObject jobj = new JSONObject();
    	 JSONArray jarray = new JSONArray();
    	 
    	 JSONObject  obj = new JSONObject();
    	 obj.put("type", "view");
    	 obj.put("name", "微商城");
    	 obj.put("url", "http://weigou.qq.com/wkd/modulepage/2918381097/930079/0/0");
    	 jarray.add(obj);
    	 
    	 obj = new JSONObject();
    	 obj.put("type", "view");
    	 obj.put("name", "订单查询");
    	 obj.put("url", "http://183.62.205.180/ChoiceWeChat/pubitem/getOrderFirm.do");
    	 jarray.add(obj);
    	 
    	 
    	 obj = new JSONObject();
    	 obj.put("type", "view");
    	 obj.put("name", "会员卡");
    	 obj.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc1437f5d60f3755e&redirect_uri=http://183.62.205.180/ChoiceWeChat/pubitem/addCard.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
    	 jarray.add(obj);
    	 
    	 
    	 jobj.put("button", jarray);
    	 return jobj.toString();
    }
 }