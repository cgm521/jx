/**
 * @Description:
 * @Title:PayUtil.java
 * @Package:com.wxap.util
 * @Author:dwh
 * @Date:2014-12-9 下午1:11:28
 * @Version:V1.0
 */
package com.wxap.util;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.choice.test.utils.Commons;
import com.wxap.RequestHandler;

/**
 * @author Iceleaf
 * 微信支付签名辅助工具类
 */
public class PayUtil {
	//订单详情经过第一次加密后的串
	private static String packageValueInfo="";
	private static String timestamp="";
	private static String nonceStr="";
	/**
	 * 订单详情第一次加密码
	 * @Description:
	 * @Title:packageValueInfo
	 * @Author:dwh
	 * @Date:2014-12-10 下午3:39:49 	
	 * @param request 
	 * @param response
	 * @param goodsInfo 商品信息（主要是商品描述和商品的价格）
	 * @return
	 * @throws Exception
	 */
	public static String packageValueInfo(HttpServletRequest request,  
            HttpServletResponse response, TreeMap<String,String> goodsInfo)throws Exception{
		//微信支付服务器签名支付请求请求类 
		RequestHandler reqHandler = new RequestHandler(request, response);
//		TenpayHttpClient httpClient = new TenpayHttpClient();
//		TreeMap<String, String> outParams = new TreeMap<String, String>();
		 //初始化 
		reqHandler.init();
		//reqHandler.init(APP_ID, APP_SECRET, PARTNER_KEY, APP_KEY);
//	 	reqHandler.init(APP_ID,APP_SECRET, APP_KEY,PARTNER_KEY);
		reqHandler.init(Commons.appId,Commons.secret, Commons.app_key,Commons.partner_key);
		
		//当前时间 yyyyMMddHHmmss
		String currTime = TenpayUtil.getCurrTime();
		//8位日期
		String strTime = currTime.substring(8, currTime.length());
		//四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		//10位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		//订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
		String out_trade_no = strReq;
		//获取提交的商品价格
//		String order_price = request.getParameter("order_price");
		//获取提交的商品名称
//		String product_name = request.getParameter("product_name");
//		System.out.println();
//		System.out.println(Commons.notify_url);
//		System.out.println(out_trade_no);
//		System.out.println(Commons.partner);
//		System.out.println();
		//设置package订单参数
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams = goodsInfo;
		packageParams.put("bank_type", "WX");  //支付类型   
//		packageParams.put("body", "assss"); //商品描述   
		packageParams.put("fee_type","1"); 	  //银行币种
		packageParams.put("input_charset", "GBK"); //字符集    
		packageParams.put("notify_url", Commons.notify_url); //通知地址  
		packageParams.put("out_trade_no", out_trade_no); //商户订单号  
		packageParams.put("partner", Commons.partner); //设置商户号
//		packageParams.put("total_fee", "1"); //商品总金额,以分为单位
		packageParams.put("spbill_create_ip",  "192.168.1.1"); //订单生成的机器IP，指用户浏览器端IP
		
		//获取package包
		String packageValue = reqHandler.genPackage(packageParams);
		packageValueInfo=packageValue;
		return packageValue;
	}
	
	
	
	public static String getSign()throws Exception{
		String packageValue = packageValueInfo;
		nonceStr = Sha1Util.getNonceStr();
		timestamp = Sha1Util.getTimeStamp();
		//设置支付参数
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		
		
//	 	signParams.put("appid", APP_ID);  
//		System.out.println(Commons.appId);
		signParams.put("appid", Commons.appId); 
		signParams.put("noncestr", nonceStr);  
		signParams.put("package", packageValue);  
		signParams.put("timestamp", timestamp);  
		signParams.put("appkey", Commons.app_key);  
//	 	System.out.println("["+APP_ID+"]["+noncestr+"]["+packageValue+"]["+timestamp);
//		System.out.println("["+Commons.appId+"]["+noncestr+"]["+packageValue+"]["+timestamp);
		//生成支付签名，要采用URLENCODER的原始值进行SHA1算法！
		String sign = Sha1Util.createSHA1Sign(signParams);
		
		//增加非参与签名的额外参数
		signParams.put("paySign", sign);
		signParams.put("signType", "SHA1");
		return sign;
	}
	
	/**
	 * 获取当前时间
	 * @Description:
	 * @Title:getTimestamp
	 * @Author:dwh
	 * @Date:2014-12-10 下午5:19:21 	
	 * @return
	 */
	public static String getTimestamp(){
//		String timestamp = Sha1Util.getTimeStamp();
		return timestamp;
	}
	
	/**
	 * 获取随机字符串
	 * @Description:
	 * @Title:getNonceStr
	 * @Author:dwh
	 * @Date:2014-12-10 下午5:19:55 	
	 * @return
	 */
	public static String getNonceStr() {
//		String nonceStr = Sha1Util.getNonceStr();
		return nonceStr;
	}
	
	
	
}
