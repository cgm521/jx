package com.choice.wechat.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.choice.test.utils.Commons;
import com.choice.test.utils.JdbcConnection;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.JsapiTicket;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.service.common.company.CompanyService;
import com.choice.wxc.util.MySSLProtocolSocketFactory;
import com.wxap.util.HttpClientUtil;
/**
 * 公众平台通用接口工具类
 * 
 * @date 2015-02-06
 */
@Component
public class WeChatUtil {
	private static Logger log = Logger.getLogger(WeChatUtil.class);
	//private static Map<String, AccessToken> accessTokenMap = new HashMap<String, AccessToken>();//微信基础支持的AccessToken，不同于网页授权高级接口的
	//private static Map<String, JsapiTicket> jsapiTicketMap = new HashMap<String, JsapiTicket>();// 调用微信JS接口的临时票据
	
	@Autowired
	private CompanyService companyService;

	private static WeChatUtil weChatUtil;
	
	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}
	
	@PostConstruct
	public void init() {
		weChatUtil = this;
		weChatUtil.companyService = this.companyService;
	}
	
	// 储存用户信息
	public static Map<String, String> userInfoMap = new HashMap<String, String>();

	public static Map<String, String> getUserInfoMap() {
		return userInfoMap;
	}

	public static void setUserInfoMap(Map<String, String> userInfoMap) {
		WeChatUtil.userInfoMap = userInfoMap;
	}
	
	public static Map<String, JSONObject> preOrderInfoMap = new HashMap<String, JSONObject>();

	public static Map<String, JSONObject> getPreOrderInfoMap() {
		return preOrderInfoMap;
	}

	public static void setPreOrderInfoMap(Map<String, JSONObject> preOrderInfoMap) {
		WeChatUtil.preOrderInfoMap = preOrderInfoMap;
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}
	
	/**
	 * 发起Http请求
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return
	 */
	public static String httpRequestReturnString(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	// 菜单创建（POST） 限100（次/天）
	public final static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单
	 * 
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	/*
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败 errcode:{} errmsg:{}");
			}
		}

		return result;
	}
	*/
	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	private static Object lock = new Object();//锁资源
	
	private static boolean isWantGetToken(String appid){
		boolean key = false;//是否重新取AccessToken true 重新获取  false 不需要重新获取
		
		//AccessToken accessToken = accessTokenMap.get(appid);
		AccessToken accessToken = (AccessToken)MemCachedUtil.getObject("access_token" + appid);
		if(accessToken != null){
			long time = new Date().getTime();
			if((accessToken.getTime() + 1000*accessToken.getExpiresIn() - time) < 600000){//有效期前十分钟重新获取,一般有效期2个小时
				key = true;
			}
		}else{
			key = true;
		}
		return key;
	}
	
	/**
	 * 获取access_token
	 * 
	 * 同步方法，后期如果有分布式的多个应用需求时可以改为数据库存储AccessToken
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		
		if(isWantGetToken(appid)){
			synchronized (lock) {
				if(isWantGetToken(appid)){//进入同步块中再次判断，以防上个线程获取成功后，这个线程再次获取
					long time = new Date().getTime();
					String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
					JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
					// 如果请求成功
					if (null != jsonObject) {
						try {
							AccessToken accessToken = new AccessToken();
							accessToken.setToken(jsonObject.getString("access_token"));
							accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
							accessToken.setTime(time);
							//accessTokenMap.put(appid, accessToken);
							MemCachedUtil.setObject("access_token" + appid, accessToken);
						} catch (JSONException e) {
							// 获取token失败
							log.error("获取token失败 errcode:{} errmsg:{}");
						}
					}
				}
			}
		}
		//return accessTokenMap.get(appid);
		return (AccessToken)MemCachedUtil.getObject("access_token" + appid);
	}
	
	public final static String oauth2Url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	/**
	 * 
	 * @param appId
	 * @param secret
	 * @param code
	 * @return
	 * @throws Exception
	 * 网页授权获取openId,access_token
	 */
	public static String[] getOAuth2(String appId, String secret, String code)
			throws Exception {
		String [] oauth2 = {"",""};
		try{
			String requestUrl = oauth2Url.replace("APPID", appId).replace("SECRET", secret).replace("CODE", code);
			JSONObject json = WeChatUtil.httpRequest(requestUrl, "POST", null);
			oauth2[0] = json.getString("openid");
			oauth2[1] = json.getString("access_token");
		}catch(Exception e){
			//e.printStackTrace();
			log.error("获取用户信息失败 errmsg:{" + e.getMessage() + "}; appid:{" + appId + "}; secret:{" + secret + "}; code:{" + code + "}");
		}
		return oauth2;
	}
	
	public final static String weChatUserUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * 
	 * @param openid
	 * @param access_token 微信基础支持的AccessToken，不同于网页授权高级接口的
	 * @return
	 * @throws Exception
	 * 获取用户基本信息 非授权方式
	 */
	public static WeChatUser getWeChatUser( String openid, String access_token)
			throws Exception {
		WeChatUser user = new WeChatUser();
		JSONObject json;
		try{
			String requestUrl = weChatUserUrl.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
			json = WeChatUtil.httpRequest(requestUrl, "GET", null);
		}catch(Exception e){
			e.printStackTrace();
			//失败，重新获取AccessToken
			//AccessToken accessToken = accessTokenMap.get(Commons.appId);
			AccessToken accessToken = (AccessToken)MemCachedUtil.getObject("access_token" + Commons.appId);
			if(accessToken != null){
				//设置为失效
				accessToken.setTime(accessToken.getTime() + 1000 * accessToken.getExpiresIn());
			}
			
			//重新获取AccessToken
			AccessToken token = getAccessToken(Commons.appId, Commons.secret);
			
			//获取用户信息
			String requestUrl = weChatUserUrl.replace("ACCESS_TOKEN", token.getToken()).replace("OPENID", openid);
			json = WeChatUtil.httpRequest(requestUrl, "GET", null);
		}
		if(null != json) {
			user.setCity(json.containsKey("city") ? json.getString("city") : "");
			user.setCountry(json.containsKey("country") ? json.getString("country") : "");
			user.setGroupid(json.containsKey("groupid") ? json.getInt("groupid") : 0);
			user.setHeadimgurl(json.containsKey("headimgurl") ? json.getString("headimgurl") : "");
			user.setLanguage(json.containsKey("language") ? json.getString("language") : "");
			user.setNickname(json.containsKey("nickname") ? json.getString("nickname") : "");
			user.setOpenid(json.containsKey("openid") ? json.getString("openid") : "");
			user.setProvince(json.containsKey("province") ? json.getString("province") : "");
			user.setRemark(json.containsKey("remark") ? json.getString("remark") : "");
			user.setSex(json.containsKey("sex") ? json.getInt("sex") : 0);
			user.setSubscribe(json.containsKey("subscribe") ? json.getInt("subscribe") : 0);
			user.setSubscribe_time(json.containsKey("subscribe_time") ? json.getInt("subscribe_time") : 0);
			//user.setUnionid(json.getString(""));
			//user = (WeChatUser) JSONObject.toBean(json, WeChatUser.class);  
		}
		return user;
	}
	
	private static Object ticketlock = new Object();//锁资源
	
	private static boolean isWantGetTicket(String appid){
		boolean key = false;//是否重新取  true 重新获取  false 不需要重新获取
		
		//JsapiTicket ticket = jsapiTicketMap.get(appid);
		JsapiTicket ticket =(JsapiTicket)MemCachedUtil.getObject("jsapi_ticket" + appid);
		
		if(ticket != null){
			long time = new Date().getTime();
			if((ticket.getTime() + 1000*ticket.getExpiresIn() - time) < 5000){//有效期前五秒重新获取,一般有效期2个小时
				key = true;
			}
		}else{
			key = true;
		}
		return key;
	}
	/**
	 * 调用微信JS接口的临时票据获取方法
	 */
	public final static String jsapiUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	public static JsapiTicket getJsapiTicket(String appid, String access_token){
		if(isWantGetTicket(appid)){
			synchronized (ticketlock) {
				if(isWantGetTicket(appid)){//进入同步块中再次判断，以防上个线程获取成功后，这个线程再次获取
					long time = new Date().getTime();
					String requestUrl = jsapiUrl.replace("ACCESS_TOKEN", access_token);
					JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
					// 如果请求成功
					if (null != jsonObject) {
						try {
							JsapiTicket ticket = new JsapiTicket();
							ticket.setTicket(jsonObject.getString("ticket"));
							ticket.setExpiresIn(jsonObject.getInt("expires_in"));
							ticket.setTime(time);
							//jsapiTicketMap.put(appid, ticket);
							MemCachedUtil.setObject("jsapi_ticket" + appid, ticket);
						} catch (JSONException e) {
							//e.printStackTrace();
							// 获取token失败
							log.error("获取ticket失败 errcode:{} errmsg:{" + e.getMessage() + "}");
						}
					}
				}
			}
		}
		
		//return jsapiTicketMap.get(appid);
		return (JsapiTicket)MemCachedUtil.getObject("jsapi_ticket" + appid);
	}
	
	/**
	 * 获取永久素材
	 */
	public final static String getMaterialUrl = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN";
	public static JSONObject getMaterial(String access_token, String media_id){
		if(StringUtils.hasText(access_token) && StringUtils.hasText(media_id)){
			String requestUrl = getMaterialUrl.replace("ACCESS_TOKEN", access_token);
			JSONObject json = new JSONObject();
			json.put("media_id", media_id);
			
			JSONObject result = httpRequest(requestUrl, "POST", json.toString());
			return result;
		}
		return null;
	}
	
	/**
	 * 获取素材信息列表
	 * @param access_token
	 * @param type 素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
	 * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count 返回素材的数量，取值在1到20之间
	 */
	public final static String  getMaterialListUrl ="https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";
	public static JSONObject getMaterialList(String access_token, String type, String offset, String count){
		if(StringUtils.hasText(access_token)){
			String requestUrl = getMaterialListUrl.replace("ACCESS_TOKEN", access_token);
			if(StringUtils.hasText(type) && StringUtils.hasText(offset) && StringUtils.hasText(count)){
				JSONObject json = new JSONObject();
				json.put("type", type);
				json.put("offset", offset);
				json.put("count", count);
				
				JSONObject result = httpRequest(requestUrl, "POST", json.toString());
				
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 获取企业配置信息
	 * @param pkGroup
	 * @return
	 */
	public static Company getCompanyInfo(String pkGroup) {
		if(pkGroup==null || "".equals(pkGroup)){
			return null;
		}
		Company info = new Company();
		String key = "company_config_" + pkGroup;
		if(MemCachedUtil.getObject(key) != null) {
			info = (Company)MemCachedUtil.getObject(key);
		} else {
			try {
				info = weChatUtil.companyService.findCompanyById(pkGroup);
				MemCachedUtil.setObject(key, info);
			} catch (Exception ex) {
				log.error("获取企业配置信息错误！");
				ex.printStackTrace();
			}
		}
		
		return info;
	}
	/**
	 * @Description：将请求参数转换为xml格式的string
     * @param parameters  请求参数
     * @author ZGL
     * @Date 2015-04-08 16:20:51
	 * @return
	 */
    public static String getRequestXml(SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }
    
    /**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static String httpRequestNew(String requestUrl, String requestMethod, String outputStr) {
		 try {
	            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
	            TrustManager[] tm = { new MyX509TrustManager() };
	            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	            sslContext.init(null, tm, new java.security.SecureRandom());
	            // 从上述SSLContext对象中得到SSLSocketFactory对象
	            SSLSocketFactory ssf = sslContext.getSocketFactory();
	            URL url = new URL(requestUrl);
	            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	            conn.setSSLSocketFactory(ssf);
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setUseCaches(false);
	            // 设置请求方式（GET/POST）
	            conn.setRequestMethod(requestMethod);
	            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
	            // 当outputStr不为null时向输出流写数据
	            if (null != outputStr) {
	                OutputStream outputStream = conn.getOutputStream();
	                // 注意编码格式
	                outputStream.write(outputStr.getBytes("UTF-8"));
	                outputStream.close();
	            }
	            // 从输入流读取返回内容
	            InputStream inputStream = conn.getInputStream();
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String str = null;
	            StringBuffer buffer = new StringBuffer();
	            while ((str = bufferedReader.readLine()) != null) {
	                buffer.append(str);
	            }
	            // 释放资源
	            bufferedReader.close();
	            inputStreamReader.close();
	            inputStream.close();
	            inputStream = null;
	            conn.disconnect();
	            return buffer.toString();
	        } catch (ConnectException ce) {
	            log.error("连接超时：{}", ce);
	        } catch (Exception e) {
	            log.error("https请求异常：{}", e);
	        }
	        return null;
	}
	/**
	 * 数字与字符类型的数字相加
	 * @param t
	 * @param obj
	 * @return
	 */
	public static Double stringPlusDouble(Object pl,Object obj){
		Double t = 0.00;
		try{
			String model="#0.00";//数字格式
			DecimalFormat df = new DecimalFormat(model);
			df.setRoundingMode(RoundingMode.HALF_UP);//设置五入 默认为HALF_EVEN 2，4，6，8的不进位
			if(ValueCheck.IsNotEmpty(pl)){
				t = Double.parseDouble(pl.toString());
			}
			if(ValueCheck.IsNotEmpty(obj)){
				t = t+Double.parseDouble(obj.toString());
			}
			t=Double.parseDouble(df.format(t));
		}catch(Exception e){
			log.error(e);
		}
		return t;
	}
	/**
	 * 数字与字符类型的数字相减
	 * @param t
	 * @param obj
	 * @return
	 */
	public static Double subtractNum(Object pl,Object obj){
		Double t = 0.00;
		try{
			String model="#0.00";//数字格式
			DecimalFormat df = new DecimalFormat(model);
			df.setRoundingMode(RoundingMode.HALF_UP);//设置五入 默认为HALF_EVEN 2，4，6，8的不进位
			if(ValueCheck.IsNotEmpty(pl)){
				t = Double.parseDouble(pl.toString());
			}
			if(ValueCheck.IsNotEmpty(obj)){
				t = t-Double.parseDouble(obj.toString());
			}
			t=Double.parseDouble(df.format(t));
		}catch(Exception e){
			log.error(e);
		}
		return t;
	}
	/**
	 * 数字与字符类型的数字相除
	 * @param t
	 * @param obj
	 * @return
	 */
	public static String dividedNum(Object pl,Object obj,int len){
		Double t = 0.00;
		try{
			if(ValueCheck.IsNotEmpty(pl)){
				t = Double.parseDouble(pl.toString());
			}
			if(ValueCheck.IsNotEmpty(obj)){
				if(Double.parseDouble(pl.toString())==0){
					return formatDoubleLength(t.toString(),len);
				}
				//计算
				if(!obj.equals("0")){
					t = t/Double.parseDouble(obj.toString());
				}
			}
		}catch(Exception e){
			log.error(e);
		}
		return formatDoubleLength(t.toString(),len);
	}
	/**
	 * 数字与字符类型的数字相乘
	 * @param t
	 * @param obj
	 * @return
	 */
	public static String multipliedNum(Object pl,Object obj,int len){
		Double t = 0.00;
		try{
			if(ValueCheck.IsNotEmpty(pl)){
				t = Double.parseDouble(pl.toString());
			}
			if(ValueCheck.IsNotEmpty(obj)){
				//计算
				t = t * Double.parseDouble(obj.toString());
			}else{
				t = 0.00;
			}
		}catch(Exception e){
			log.error(e);
		}
		return formatDoubleLength(t.toString(),len);
	}
	/**
	 * 格式化数字
	 * @param num  需要格式化的数字
	 * @param len  格式化的长度:<0 为默认2;  >=0 为格式化的小数位数
	 * @return
	 */
	public static String formatDoubleLength(String num,int len){
		Double formatNum = 0.00;
		if(ValueCheck.IsEmpty(num)){
			num = "0";
		}
		try{//如果输入的字符是数字则转换，不是直接返回该字符串
			formatNum=Double.valueOf(num);
		}catch (Exception e) {
			log.error(e);
			return num;
		}
		String result = "";//存放最终格式化后的数字
		String model="#0";//数字格式
		int length=2;//数字格式的小数位数
		if(len>=0){//判断如果输入的长度>=0则将长度修改<0则去系统配置的长度参数名：formatLen
			length = len;
		}else{
			length=2	;
		}
		if(length>1){
			model+=".";
		}
		//循环小数位数格式
		for(int i=0;i<length;i++){
			model+="0";
		}
		DecimalFormat df = new DecimalFormat(model);
		df.setRoundingMode(RoundingMode.HALF_UP);//设置五入 默认为HALF_EVEN 2，4，6，8的不进位
		result = df.format(formatNum);
		return result;
	}
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public  static Map<String,Object> TransformMap(Map map){
		Map<String,Object> resMap = new HashMap<String,Object>();
		Map<String, String[]> maps = map;  
        Set<Entry<String, String[]>> set = map.entrySet();  
        Iterator<Entry<String, String[]>> it = set.iterator();  
        while (it.hasNext()) {  
            Entry<String, String[]> entry = it.next();  
            resMap.put(entry.getKey(), entry.getValue()[0]);
        }  
		return resMap;
		
	}
	 /**
	  * 获取客户端ip
	  * @param request
	  * @return
	  */
	public static String getIpAddr(HttpServletRequest request) { 
	     String ip = request.getHeader("x-forwarded-for"); 
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	         ip = request.getHeader("Proxy-Client-IP"); 
	     } 
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	         ip = request.getHeader("WL-Proxy-Client-IP"); 
	     } 
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	         ip = request.getRemoteAddr(); 
	     } 
	     // 返回一串ip值的话，取第一个
	     if(ip.contains(",")){
	    	 ip = ip.substring(0, ip.indexOf(","));
	     }
	     if("0:0:0:0:0:0:0:1".equals(ip) || ip.indexOf("0:0:0:0:0:0:0:1")>=0){
	    	 ip = "127.0.0.1";
	     }
	     if(ip.indexOf("::ffff:")>-1){
	    	 ip = ip.substring("::ffff:".length());
	     }
	     return ip; 
	 }
	/**
	 * 将map中的键值对ASCII 码 从小到大排序（字典序）
	 * @param signParams
	 * @return
	 * @throws Exception
	 */
	public static String orderParams(SortedMap<Object, Object> signParams) throws Exception {
		StringBuffer sb = new StringBuffer();
		Set es = signParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if(ValueCheck.IsNotEmpty(v)){
				sb.append(k + "=" + v + "&");
			}
			//要采用URLENCODER的原始值！
		}
		String params = sb.substring(0, sb.lastIndexOf("&"));
		System.out.println("=========="+params);
		return params;
	}

	/**
	 * 格式化list集合中的数字（将空值格式化为0.00）
	 * @param listResult
	 * @param cols
	 * @param len
	 * @return
	 * @author zgl
	 * @Date 2014-03-03 10:06:35
	 */
	public static List<Map<String,Object>> formatNumForListResult(List<Map<String,Object>> listResult,String cols,int len){
		String num = "0";
		if(len>0){
			num +=".";
			for(int i=0;i<len;i++){
				num += "0";
			}
		}
		if(listResult != null && listResult.size()>0){
			String[] colsList = cols.split(",");
			for(Map<String,Object> map : listResult){
				//循环map并取里面的值
				for(String col : colsList){
					map.put(col,null==map.get(col)?num:formatDoubleLength(map.get(col).toString(),len));
				}
			}
		}
		return listResult;
	}
    
    public static String executeHttpMethod(String url, JSONObject json) {
		try {
			HttpURLConnection conn = HttpClientUtil.getHttpURLConnection(url);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(0);
			conn.setInstanceFollowRedirects(true);			
			conn.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			conn.setDefaultUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8"); // utf-8编码
			out.write(json.toString());//传递参数
			out.flush();
			out.close();
			DataInputStream dis = new DataInputStream(conn.getInputStream());
			byte []buf = new byte[1024 * 1024 * 2];
			int len=0;
			StringBuffer sb = new StringBuffer("");
			while((len = dis.read(buf)) != -1) {
				sb.append(new String(buf,0,len,"UTF-8"));
			}
			conn.disconnect();
			String resultStr = sb.toString();
			
			return resultStr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 使用辰森短信平台发送验证码
	 * @param tele
	 * @param content
	 * @return 0:成功；-1：失败
	 */
	public static int sendSMSbyPlatform(String tele, String content) {
		Connection conn = null;
		Statement st = null;
		int result = 0;
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "INSERT INTO SENDSMS (SMSID,TELNO,TELMEMO,FLAG) VALUES (SEQ_SENDSMS.nextval, '" + tele + "', '" + content + "', 0)";
			result = st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result > 0 ? 0 : -1;
	}
	/**
     * 新增永久素材
     * @param file  表单名称media
     * @param token access_token
     * @param type  type只支持四种类型素材(video/image/voice/thumb)
     * @param des 上传视频时的json
     */
	private static final String UPLOAD_MEDIA = "http://api.weixin.qq.com/cgi-bin/material/add_material";
    public static JSONObject uploadMedia(File file, String token, String type,String des) {
        if(file==null||token==null||type==null){
        	log.info("上传文件为空,请检查!");
            return null;
        }

        if(!file.exists()){
            log.info("上传文件不存在,请检查!");
//        	System.out.println("上传文件不存在,请检查!");
            return null;
        }

        String url = UPLOAD_MEDIA;
        JSONObject jsonObject = null;
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Connection", "Keep-Alive");
        post.setRequestHeader("Cache-Control", "no-cache");
        FilePart media = null;
        HttpClient httpClient = new HttpClient();
        //信任任何类型的证书
        Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443); 
        Protocol.registerProtocol("https", myhttps);
        
        try {
            media = new FilePart("media", file);
            Part[] parts;
            if("video".equals(type)){
            	System.out.println("description="+des);
            	parts = new Part[] { new StringPart("access_token", token),
                        new StringPart("type", type),new StringPart("description", des), media };
            }else{
            	parts = new Part[] { new StringPart("access_token", token),
                        new StringPart("type", type), media };
            }
            MultipartRequestEntity entity = new MultipartRequestEntity(parts,
                    post.getParams());
            post.setRequestEntity(entity);
            int status = httpClient.executeMethod(post);
            if (status == HttpStatus.SC_OK) {
                String text = post.getResponseBodyAsString();
                log.info("上传多媒体文件"+type+",返回结果："+text);
//                System.out.println("上传第一次媒体文件："+text);
                jsonObject = JSONObject.fromObject(text);
            } else {
                log.info("upload Media failure status is:" + status);
            }
        } catch (FileNotFoundException execption) {
            log.error(execption);
        } catch (HttpException execption) {
            log.error(execption);
        } catch (IOException execption) {
            log.error(execption);
        }
        return jsonObject;
    }
}