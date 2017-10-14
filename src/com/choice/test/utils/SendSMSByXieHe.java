package com.choice.test.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.choice.wechat.util.LogUtil;

public class SendSMSByXieHe {
	private static Logger log = LogManager.getLogger(SendSMSByXieHe.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpClient httpClient = new HttpClient(); // 创建请求对象
		httpClient.getHostConfiguration().setHost("124.172.243.56", 8080, "http"); // 设置请求地址、端口及协议
		try {
			PostMethod method = new UTF8PostMethod("/sms/balance.jsp"); // 新建一个UTF-8编码的POST请求
			//PostMethod method = new UTF8PostMethod("/sms/sendsms.jsp"); // 新建一个UTF-8编码的POST请求
			method.addParameter("account", "ceshi@cesh"); // 发送参数：账号
			method.addParameter("passwd", "123456"); // 发送参数：密码
			//method.addParameter("action", "send"); // 发送参数：动作：发送短信
			//method.addParameter("mobiles", "15965316163"); // 接收号码
			//method.addParameter("content", "【辰森世纪】您本次的验证码是666666"); // 接收号码
			httpClient.executeMethod(method); // 发送请求
			String result = request(method); // 获取返回内容
			method.releaseConnection(); // 释放链接，防止请求过多堵塞网络
			log.info(result);
			// TODO:后续处理
		} catch (Exception e) {
			log.error("查询余额时错误：" + e.getMessage());
		}
	}
	
	/**
	 * 发送短信
	 * @param tele
	 * @param content
	 */
	public static void sendSMS(String tele, String content) {
		HttpClient httpClient = new HttpClient(); // 创建请求对象
		httpClient.getHostConfiguration().setHost("124.172.243.56", 8080, "http"); // 设置请求地址、端口及协议
		try {
			PostMethod method = new UTF8PostMethod("/sms/sendsms.jsp"); // 新建一个UTF-8编码的POST请求
			method.addParameter("account", Commons.softwareSerialNo); // 发送参数：账号
			method.addParameter("passwd", Commons.key); // 发送参数：密码
			//method.addParameter("action", "send"); // 发送参数：动作：发送短信
			method.addParameter("mobiles", tele); // 接收号码
			method.addParameter("content", content); // 接收号码
			httpClient.executeMethod(method); // 发送请求
			String result = request(method); // 获取返回内容
			method.releaseConnection(); // 释放链接，防止请求过多堵塞网络
			LogUtil.writeToTxt("SendSMSByXieHe", "发送短信，手机号【" + tele + "】，短信内容【" + content + "】，发送结果【" + result + "】");
			// TODO:后续处理
		} catch (Exception e) {
			LogUtil.writeToTxt("SendSMSByXieHe", "发送短信，手机号【" + tele + "】，短信内容【" + content + "】，发送结果【失败】，失败原因【" + e.getMessage() + "】");
		}
	}

	/**
	 * 对请求进行utf-8编码
	 */
	public static class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}

		public String getRequestCharSet() {
			return "UTF-8";
		}
	}

	/**
	 * 从请求中读取字符串
	 * 
	 * @param method
	 * @return
	 */
	private static String request(HttpMethod method) {
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(method
					.getResponseBodyAsStream(), "UTF-8"));
			String resultStr = "";
			String line = null;
			while ((line = br.readLine()) != null) {
				resultStr += line + "\r\n";
			}
			method.releaseConnection();
			resultStr = resultStr.replaceAll("\\s", "");
			return resultStr;
		} catch (Exception e) {
			log.error("读取返回结果错误：" + e.getMessage());
		}
		return "";
	}
}
