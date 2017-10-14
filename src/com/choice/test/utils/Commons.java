package com.choice.test.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Commons {
	/*********************************微信公共账号特殊ID**************************************/
	/**
	 * 应用id
	 */
	public static String appId = Commons.getConfig().getProperty("appId");
	/**
	 * 应用密钥
	 */
	public static String secret = Commons.getConfig().getProperty("secret");
	/**
	 * 支付key
	 */
	public static String app_key = Commons.getConfig().getProperty("app_key");
	/**
	 * 财付通商户号
	 */
	public static String partner = Commons.getConfig().getProperty("partner");
	/**
	 * 财务通密钥
	 */
	public static String partner_key = Commons.getConfig().getProperty("partner_key");
	/**
	 * 机构号，由云收银系统分配
	 */
	public static String inscd = Commons.getConfig().getProperty("inscd");
	/**
	 * 商户ID，由云收银系统分配
	 */
	public static String mchntid = Commons.getConfig().getProperty("mchntid");
	public static String terminalid = Commons.getConfig().getProperty("terminalid");
	/**
	 * 迅联云支付加密用的秘钥
	 */
	public static String xulian_sign_key = Commons.getConfig().getProperty("xulian_sign_key");
	/**
	 * 调用成功之后跳转页面的url
	 */
	public static String notify_url = Commons.getConfig().getProperty("notify_url");
	
	/**
	 * 购买电子券调用成功之后跳转页面的url
	 */
	public static String coupon_notify_url = Commons.getConfig().getProperty("coupon_notify_url");
	
	/***********************************webService接口*******************************************/
	public static String CRMwebService=Commons.getConfig().getProperty("CRMwebService");
	public static String BOHwebService=Commons.getConfig().getProperty("BOHwebService");
	
	/*************************************公司主页信息**********************************/
	//关注欢迎信息
	public static String vcontent =Commons.getConfig().getProperty("vcontent");
	//公司主页欢迎信息
	public static String vtitle =Commons.getConfig().getProperty("vtitle");
	//公司主页地址
	public static String vcomUrl =Commons.getConfig().getProperty("vcomUrl");
	//公司主页图片地址
	public static String vcomPic =Commons.getConfig().getProperty("vcomPic");

	/****************************************会员卡***************************************/
	//注册界面图片
	public static String vregestPic=Commons.getConfig().getProperty("vregestPic");
	//查询会员二维码显示地址
	public static String vcardPic=Commons.getConfig().getProperty("vcardPic");
	
	//微信自动回复
	public static String vmessage=Commons.getConfig().getProperty("vmessage");
	//微信扫描二维码点菜配置路径
	public static String vtakeOrdrurl=Commons.getConfig().getProperty("vtakeOrdrurl");
	public static String vwifi=Commons.getConfig().getProperty("vwifi");
	public static String vtakeOrdr=Commons.getConfig().getProperty("vtakeOrdr");
	
	/*****************************************微信页面公共标题*******************************/
	public static String wx_title=Commons.getConfig().getProperty("wx_title");
	public static String wx_order_title=Commons.getConfig().getProperty("wx_order_title");
	public static String wx_orderdtl_title=Commons.getConfig().getProperty("wx_orderdtl_title");
	public static String wx_ordermenu_title=Commons.getConfig().getProperty("wx_ordermenu_title");
	public static String wx_button_value=Commons.getConfig().getProperty("wx_button_value");
	
	/*****************************************短信平台配置*******************************/
	public static String softwareSerialNo=Commons.getConfig().getProperty("softwareSerialNo");
	public static String key=Commons.getConfig().getProperty("key");
	
	/*****************************************微信配置是否绑定赠送*******************************/
	public static String isGift=Commons.getConfig().getProperty("isGift");
	public static String inAmt=Commons.getConfig().getProperty("inAmt");
	public static String firmId=Commons.getConfig().getProperty("firmId");
	public static String payMentCode=Commons.getConfig().getProperty("payMentCode");
	public static String payMent=Commons.getConfig().getProperty("payMent");
	
	/*****************************************微信菜品优惠信息图片*******************************/
	public static String vpiceure=Commons.getConfig().getProperty("vpiceure");
	/*****************************************外送的webService地址*******************************/
	public static String CALLwebService=Commons.getConfig().getProperty("CALLwebService");
	
	/*****************************************微信菜品优惠信息图片*******************************/
	public static String memCachedIp = Commons.getConfig().getProperty("memcachedIP");
	public static String memCachedPort = Commons.getConfig().getProperty("memcachedPort");
	
	/*****************************************公司名称*******************************/
	public static String vcompanyname = Commons.getConfig().getProperty("vcompanyname");
	
	/*****************************************公司名称*******************************/
	public static String baidumap = Commons.getConfig().getProperty("baidumap");
	
	/*****************************************CTF访问路径*******************************/
	public static String CTFUrl = Commons.getConfig().getProperty("CTFUrl");
	
	/*****************************************总部BOH访问路径 *******************************/
	public static String BOHUrl = Commons.getConfig().getProperty("BOHUrl");
	
	/*****************************************公众号支付生成预支付号统一连接*******************************/
	public static String unified_order_url = Commons.getConfig().getProperty("unified_order_url");
	/*****************************************账单支付需要的字段*******************************/
	public static String vwxpaycode = Commons.getConfig().getProperty("vwxpaycode");
	public static String vwxpayname = Commons.getConfig().getProperty("vwxpayname");
	public static String vcardpaycode = Commons.getConfig().getProperty("vcardpaycode");
	public static String vcardpayname = Commons.getConfig().getProperty("vcardpayname");
	/*****************************************数据库标志*******************************/
	public static String databaseType = Commons.getConfig().getProperty("databaseType");

	/*****************************************MQ发送地址*******************************/
	public static String broker_url = Commons.getConfig().getProperty("broker_url");

	/*****************************************平均每桌等待时长(分钟)*******************************/
	public static String avgWaitMinutes = Commons.getConfig().getProperty("avgWaitMinutes");
	
	/*****************************************我要结账提示语*******************************/
	public static String checkoutmsg = Commons.getConfig().getProperty("checkoutmsg");
	
	/*****************************************财付通证书路径*******************************/
	public static String certPath = Commons.getConfig().getProperty("certPath");
	
	/*****************************************发送mq消息的有效时间*******************************/
	public static int mqEffectiveTime = Integer.parseInt(Commons.getConfig().getProperty("mqEffectiveTime"));
	
	/*****************************************进入微信墙发送图片存放路径*******************************/
	public static String wallImgPath = Commons.getConfig().getProperty("wallImgPath");
	
	/*****************************************接收mq消息队列名*******************************/
	public static String reciveMqQueueName = Commons.getConfig().getProperty("reciveMqQueueName");
	
	/*****************************************公司电话*******************************/
	public static String vcompanytel = Commons.getConfig().getProperty("vcompanytel");
	
	/*****************************************是否需要台位配置 0-不需要 1-需要*******************************/
	public static String needSite = Commons.getConfig().getProperty("needSite");
	
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
		System.out.println(Commons.appId);
		System.out.println(Commons.secret);
		System.out.println(Commons.BOHUrl);
		System.out.println(Commons.inAmt);
		System.out.println(Commons.baidumap);
		System.out.println(Commons.wx_title);
	}
}
