package com.choice.wechat.util;


import java.io.DataInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.choice.test.utils.Commons;

public class CallWebService {
	
	private final transient static Log log = LogFactory.getLog(CallWebService.class);

	public static String ADDRESS=Commons.CTFUrl;// 地址
	public static String NAMESPACE;// 域名空间
	/**
	 * 企业号
	 */
	public static final String SALT = "";
	

	public CallWebService(String addressName) {
//		ADDRESS = ConfigFileUtil.getConfigFileValue("sysconfig.properties", addressName);
		NAMESPACE = "http://webservice.choice.com/";
	}

	/**
	 * 调用webservice
	 * @param method 方法
	 * @param json 参数值
	 * @param paramnames 参数名
	 * @return
	 * @throws Exception
	 */
	public static String callWebService(String method, Object[] paramArray, String... paramnames) {
		Service service = new Service();
		String result = null;
		try {
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(ADDRESS));
			QName qn = new QName(NAMESPACE, method);
			call.setOperationName(qn);
			if(null!=paramnames){
				for (String param : paramnames) {
					call.addParameter(param, XMLType.XSD_STRING, ParameterMode.IN);
				}
			}
			call.setReturnType(XMLType.XSD_STRING);
			result = (String) call.invoke(paramArray);
		} catch (Exception e) {
			e.printStackTrace();
//			LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用接口【" + method + "】错误【" + e.getMessage() + "】");
		}
		return result;
	}
	
	/**
	 * http调用webservice
	 * @param method
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public static String httpCallWebService(String method,String params) {
//		System.out.println("startCall=============================================="+DateFormat.getTs());
		String result = "";
		String queryUrl = Commons.CTFUrl+method;
		System.out.println("============================"+queryUrl+"?"+params);
		try {
			queryUrl = queryUrl.replace(" ", "%20");
			URL url = new URL(queryUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(0);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-Type",	"application/x-www-form-urlencoded");
//			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			conn.setDefaultUseCaches(false);
			conn.setDoOutput(true);
			PrintWriter out = new PrintWriter(conn.getOutputStream());
			out.print(params);//传入参数
			out.close();

			conn.connect();
			DataInputStream dis = new DataInputStream(conn.getInputStream());
//			System.out.println("endCall=============================================="+DateFormat.getTs());
			byte []buf = new byte[1024 * 1024 * 2];
			int len=0;
			StringBuffer sb = new StringBuffer("");
			while((len = dis.read(buf)) != -1) {
				sb.append(new String(buf,0,len,"UTF-8"));
			}
			conn.disconnect();
			result = sb.toString();
//			System.out.println("======================="+result);
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return result;
	}
	

		/**
		 * http调用webservice 调用助手接口 GET
		 * @param method
		 * @param paramName
		 * @param paramValue
		 * @return
		 */
		public static String httpCallWebServiceGet(String method,String paramName) throws Exception{
			String result = "";
			
			
			String queryUrl = ADDRESS+method+"?"+paramName;
			try {
				queryUrl = queryUrl.replace(" ", "%20");
				URL url = new URL(queryUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.connect();
				DataInputStream dis = new DataInputStream(conn.getInputStream());
				byte []buf = new byte[1024 * 1024 * 2];
				int len=0;
				StringBuffer sb = new StringBuffer("");
				while((len = dis.read(buf)) != -1) {
					sb.append(new String(buf,0,len,"UTF-8"));
				}
				conn.disconnect();
				result = sb.toString();
				String returncode = getXmlValue(result);
				if(!returncode.equals("1")){
//					LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "返回值【"+returncode+"】调用URL【"+queryUrl+"】");
				}
				return returncode;
			}catch(Exception e) {
//				LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用接口【" + method + "】错误【" + e.getMessage() + "】调用URL【"+queryUrl+"】");
				throw new Exception(e);
			}
		}
		/**
		 * 获取接口返回的数据
		 * @param data
		 * @return
		 */
		 public static String getXmlValue(String data){
			   String result = "";
				 //创建一个新的字符串
		        StringReader read = new StringReader(data);
		        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		        InputSource source = new InputSource(read);
		        //创建一个新的SAXBuilder
		        SAXBuilder sbil = new SAXBuilder();
		        try {
		            //通过输入源构造一个Document
		            Document doc = sbil.build(source);
		            //取的根元素
		            Element root = doc.getRootElement();
		            //得到根元素所有子元素的集合
		            List<?> jiedian = root.getChildren();
		            Element et = null;
		            for(int i=0;i<jiedian.size();i++){
		                et = (Element) jiedian.get(i);//循环依次得到子元素
		                List<?> sun = et.getChildren();
		                for(int j=0;j<sun.size();j++){
			                et = (Element) sun.get(j);//循环依次得到子元素
			                result = et.getChildText("return").toString();
		                }
		            }
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
		        return result;
		}
		/**
		 * post调用
		 * @param queryUrl
		 * @param parameters
		 * @return
		 */
		public static String httpUrlConnection(String queryUrl,String parameters) {
			String result = "";
			try {
				queryUrl = queryUrl.replace(" ", "%20");
				URL url = new URL(queryUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(0);
				conn.setInstanceFollowRedirects(true);
				conn.setRequestProperty("Content-Type",	"application/x-www-form-urlencoded");
				conn.setDefaultUseCaches(false);
				conn.setDoOutput(true);
				PrintWriter out = new PrintWriter(conn.getOutputStream());
				out.print(parameters);//传入参数
				out.close();
	
				conn.connect();
				DataInputStream dis = new DataInputStream(conn.getInputStream());
				byte []buf = new byte[1024 * 1024 * 2];
				int len=0;
				StringBuffer sb = new StringBuffer("");
				while((len = dis.read(buf)) != -1) {
					sb.append(new String(buf,0,len,"GBK"));
				}
				conn.disconnect();
				result = sb.toString();
			}catch(Exception e) {
				e.printStackTrace();
				log.error(e);
			}
			return result;
		}

	public static void main(String[] args) throws Exception {
		String json = CallWebService.httpCallWebServiceGet("CRMWebService/queryCardByCardNo", "queryCardNo=90017");
		System.out.println(json);
	}
}
