<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>在线点餐</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script language="JavaScript" type="text/JavaScript">
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
$(document).on("pageinit","#pageone",function(){
	InitLayer();
	var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc1437f5d60f3755e&redirect_uri=" +
			"http://115.238.164.148" +
			"<c:url value='/sceneMeal/gotoMenu.do?scene_id='/>"+ $("#scene_id").val() +
			"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
	window.setTimeout(function(){
		location.href = url;
	},100);
});
</script>
</head>
<body>
<div data-role="page" data-theme="d" id="pageone"><!--页面层容器-->
	<input type="hidden" id="scene_id" value="${scene_id}">
</div><!-- page -->
</body>
</html>