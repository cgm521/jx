package com.choice.test.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.choice.wechat.util.LogUtil;

public class SendSMSByWxt {
	private static Logger log = LogManager.getLogger(SendSMSByWxt.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpClient httpClient = new HttpClient(); // �����������
		httpClient.getHostConfiguration().setHost("61.156.157.248", 8889, "http"); // ���������ַ���˿ڼ�Э��
		try {
			PostMethod method = new UTF8PostMethod("/api.php"); // �½�һ��UTF-8�����POST����
			method.addParameter("username", "yourUsername"); // ���Ͳ������û���
			method.addParameter("password", "yourPassword"); // ���Ͳ���������
			method.addParameter("action", "surplus"); // ���Ͳ�������������ѯ���
			httpClient.executeMethod(method); // ��������
			String result = request(method); // ��ȡ��������
			method.releaseConnection(); // �ͷ����ӣ���ֹ��������������
			log.info(result);
			// TODO:��������
		} catch (Exception e) {
			log.error("��ѯ���ʱ����" + e.getMessage());
		}
	}
	
	/**
	 * ���Ͷ���
	 * @param tele
	 * @param content
	 */
	public static void sendSMS(String tele, String content) {
		HttpClient httpClient = new HttpClient(); // �����������
		httpClient.getHostConfiguration().setHost("61.156.157.209", 8889, "http"); // ���������ַ���˿ڼ�Э��
		try {
			PostMethod method = new UTF8PostMethod("/api.php"); // �½�һ��UTF-8�����POST����
			method.addParameter("username", Commons.softwareSerialNo); // ���Ͳ������û���
			method.addParameter("password", Commons.key); // ���Ͳ���������
			method.addParameter("action", "send"); // ���Ͳ��������������Ͷ���
			method.addParameter("receive_number", tele); // ���պ���
			method.addParameter("message_content", content); // ���պ���
			httpClient.executeMethod(method); // ��������
			String result = request(method); // ��ȡ��������
			method.releaseConnection(); // �ͷ����ӣ���ֹ��������������
			log.info(result);
			LogUtil.writeToTxt("SendSMSByWxt", "���Ͷ��ţ��ֻ��š�" + tele + "�����������ݡ�" + content + "�������ͽ����" + result + "��");
			// TODO:��������
		} catch (Exception e) {
			log.error("���Ͷ���ʱ��������" + e.getMessage());
			LogUtil.writeToTxt("SendSMSByWxt", "���Ͷ��ţ��ֻ��š�" + tele + "�����������ݡ�" + content + "�������ͽ����ʧ�ܡ���ʧ��ԭ��" + e.getMessage() + "��");
		}
	}

	/**
	 * ���������utf-8����
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
	 * �������ж�ȡ�ַ���
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
			log.error("��ȡ���ؽ������" + e.getMessage());
		}
		return "";
	}
}
