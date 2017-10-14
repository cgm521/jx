<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<% 

//调试模式
 boolean DEBUG_ = true;
 String PARTNER		= "1219295101" ;	//财付通商户号
 String PARTNER_KEY	= "lapargay20140704lacafe20140705ll";	//财付通密钥
 String APP_ID		= "wxc1437f5d60f3755e";	//appid
 String APP_SECRET	= "4d879044188e5c2e262e4ebcbbd2c12e";	//appsecret
 String APP_KEY		= "nhO7AR1Xk96RgRgIXxmqWeECWbSSZtky3YfgapDPpkFJKV9CZmC19Gqzts1MElFvl4k1y52BKlwd1n5dLTOVuk07dkXyJbzneGPwxudrpmq1VZ0D6tx1v4F02RYCP5tb";	//paysignkey 128位字符串(非appkey)
 
 String NOTIFY_URL	= "http://115.238.164.148/view/wxpay/payNotifyUrl.jsp";  //支付完成后的回调处理页面,*替换成notify_url.asp所在路径
 String LOGING_DIR	= "";  //日志保存路径
%>

		