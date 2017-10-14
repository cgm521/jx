package com.choice.wechat.util;

import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

/**
 * 日志写入类
 * @author wqq
 * <br>2013-12-24
 * <br>
 */
public class LogUtil {
	
	public static final String CALLWEBSERVICE="callwebservice";//调用webservice
	public static final String BUSINESS="business";//业务处理
	public static final String JSON="json";//JSON
	public static final String DATABASE="database";//数据库
	public static final String EXTERNALINTERFACECALL="ExternalInterfaceCall";//外部接口调用
	public static final String TENPAY="tenPay";//微信支付
	public static final String TENPAYREFUND="tenPayRefund";//微信退款
	public static final String MQSENDORDER="mqSendOrder";//往mq中发送订单对象
	public static String PATH = LogUtil.class.getResource("").getPath().substring(1).replace("WEB-INF/classes/com/choice/wechat/util/", "");
	private static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	
	private static SimpleDateFormat YYYY_MM_DD_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 日志文件书写
	 * @param writeStr
	 */
	public static void writeToTxt(String logtype,String content) {
		try {
			//如果是linux系统
			 if("/".equals(File.separator)){
				 PATH = "/"+PATH;
			}
			File dir = new File(PATH+"businesslog/"+YYYY_MM_DD.format(new Date()));
		    if(!dir.exists()){
		    	dir.mkdirs();
		    }
			String filename = PATH+"businesslog/"+YYYY_MM_DD.format(new Date())+"/"+logtype+".log";
			File file = new File(filename);
		    if(!file.exists()){
		    	file.createNewFile();//不存在则创建
		    }
		    
            FileWriter writer = new FileWriter(filename, true);
            content = URLDecoder.decode(content, "UTF-8");
            writer.write(YYYY_MM_DD_HH_mm_ss.format(new Date())+" : "+content+"\r\n");
            writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}   
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String ss = URLEncoder.encode("中国人", "UTF-8");
		String dd = URLDecoder.decode(ss,"UTF-8");
		System.out.println(ss);
		System.out.println(dd);
	}
}
