<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ page import="com.wxap.ResponseHandler"%>
<%@ page import="com.wxap.RequestHandler"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.wxap.client.TenpayHttpClient"%>
<%@ page import="java.util.SortedMap"%>
<%@ page import="com.wxap.util.Sha1Util"%>
<%@ page import="com.wxap.util.TenpayUtil"%>
<%@ page import="com.wxap.util.MD5Util"%>
<%@ page import="java.io.BufferedWriter"%>
<%@ page import="java.io.BufferedOutputStream"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="com.google.gson.Gson"%>
<%@ include file="config.jsp"%>

<%

//=================================
//jsapi�ӿ�
//=================================
//��ʼ��

	RequestHandler reqHandler = new RequestHandler(request, response);
	TenpayHttpClient httpClient = new TenpayHttpClient();
	
	TreeMap<String, String> outParams = new TreeMap<String, String>();
	 //��ʼ�� 
	reqHandler.init();
	//reqHandler.init(APP_ID, APP_SECRET, PARTNER_KEY, APP_KEY);
	reqHandler.init(APP_ID,APP_SECRET, APP_KEY,PARTNER_KEY);  
	
	//��ǰʱ�� yyyyMMddHHmmss
	String currTime = TenpayUtil.getCurrTime();
	//8λ����
	String strTime = currTime.substring(8, currTime.length());
	//��λ�����
	String strRandom = TenpayUtil.buildRandom(4) + "";
	//10λ���к�,�������е�����
	String strReq = strTime + strRandom;
	//�����ţ��˴���ʱ�����������ɣ��̻������Լ����������ֻҪ����ȫ��Ψһ����
	String out_trade_no = strReq;
	
	//��ȡ�ύ����Ʒ�۸�
	String order_price = request.getParameter("order_price");
	//��ȡ�ύ����Ʒ����
	String product_name = request.getParameter("product_name");
	System.out.println();
	System.out.println(NOTIFY_URL);
	System.out.println(out_trade_no);
	System.out.println(PARTNER);
	System.out.println();
	//����package��������
	SortedMap<String, String> packageParams = new TreeMap<String, String>();
	packageParams.put("bank_type", "WX");  //֧������   
	packageParams.put("body", "assss"); //��Ʒ����   
	packageParams.put("fee_type","1"); 	  //���б���
	packageParams.put("input_charset", "GBK"); //�ַ���    
	packageParams.put("notify_url", NOTIFY_URL); //֪ͨ��ַ  
	packageParams.put("out_trade_no", out_trade_no); //�̻�������  
	packageParams.put("partner", PARTNER); //�����̻���
	packageParams.put("total_fee", "1"); //��Ʒ�ܽ��,�Է�Ϊ��λ
	packageParams.put("spbill_create_ip",  "192.168.1.1"); //�������ɵĻ���IP��ָ�û��������IP
	
	//��ȡpackage��
	String packageValue = reqHandler.genPackage(packageParams);
	String noncestr = Sha1Util.getNonceStr();
	String timestamp = Sha1Util.getTimeStamp();
	
	//����֧������
	SortedMap<String, String> signParams = new TreeMap<String, String>();
	
	
	signParams.put("appid", APP_ID);  
	signParams.put("noncestr", noncestr);  
	signParams.put("package", packageValue);  
	signParams.put("timestamp", timestamp);  
	signParams.put("appkey", APP_KEY);  
	System.out.println("["+APP_ID+"]["+noncestr+"]["+packageValue+"]["+timestamp);
	//����֧��ǩ����Ҫ����URLENCODER��ԭʼֵ����SHA1�㷨��
	String sign = Sha1Util.createSHA1Sign(signParams);
	
	//���ӷǲ���ǩ���Ķ������
	signParams.put("paySign", sign);
	signParams.put("signType", "SHA1");
		
		
	
%>
<html>
	<head>
		<script language="javascript">
		function callpay(){
			 WeixinJSBridge.invoke('getBrandWCPayRequest',{
	  		 		"appId" : "<%= APP_ID %>",
  		 			"timeStamp" : "<%= timestamp %>",
  		 		 	"nonceStr" : "<%= noncestr %>",
  		 		  	"package" : "<%= packageValue %>",
  		 		  	"signType" : "SHA1", 
  		 		  	"paySign" : "<%= sign %>" 
	   		 },function(res){
						
						WeixinJSBridge.log(res.err_msg);
						alert(res.err_msg);
			            if(res.err_msg == "get_brand_wcpay_request:ok"){  
			                alert("΢��֧���ɹ�");  
			            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
			                alert("�û�ȡ��֧��");  
			            }else{  
			                alert("֧��ʧ��");  
			            }  
			})
		}
		</script>
	</head>
  <body>
    <button type="button" onclick="callpay()" ><h1>����֧��</h1></button>
  </body>
</html>