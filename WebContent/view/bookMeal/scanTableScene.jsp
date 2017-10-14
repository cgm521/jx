<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>扫码下单</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script language="JavaScript" type="text/JavaScript">
	wx.config({
	    debug: false,
	    appId: '${appId}',
	    timestamp: '${signMap.timestamp}',
	    nonceStr: '${signMap.nonceStr}',
	    signature: '${signMap.signature}',
	    jsApiList: ['scanQRCode']
	});
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	$(function(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
			$("body").text("请使用微信浏览器打开");
			return;
		}
	});

	wx.ready(function () {
		//调用微信扫一扫功能，获取二维码中的台位号
		$("#saoyisao").click(function (){
			wx.scanQRCode({
			    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
			    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
			    success: function (res) {
			   		var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
			   		$("#scene_id").val(result);
				}
			});
		});
	});

	wx.error(function (res) {
		alertMsg(res.errMsg);
	});
</script>
</head>
<body>
<div data-role="page" data-theme="d" id="pageone"><!--页面层容器-->
	<input type="text" id="scene_id" value="${tableId}">
	<input type="button" value="扫一扫" id="saoyisao" >
</div><!-- page -->
</body>
</html>