<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>预定桌台</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script language="JavaScript" type="text/JavaScript">
function loaded() { 
    setTimeout(function(){InitLayer();},100);
} 
window.addEventListener("load",loaded,false); 

$(function(){
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)!="micromessenger") {
		$("body").text("请使用微信浏览器打开");
		return;
	}
	$.mobile.pageLoading(true);
});

wx.config({
    debug: false,
    appId: '${appId}',
    timestamp: '${signMap.timestamp}',
    nonceStr: '${signMap.nonceStr}',
    signature: '${signMap.signature}',
    jsApiList: ['getLocation']
});

wx.ready(function () {
	  wx.getLocation({
	    success: function (res) {
	      	$("#lat").val(res.latitude);
	    	$("#lng").val(res.longitude);
	    	gotoListFirm();
	    },
	    cancel: function (res) {
	      //alert('用户拒绝授权获取地理位置');
	      gotoListFirm();
	    }
	  });
});

wx.error(function (res) {
	alert(res.errMsg);
});

function gotoListFirm(){
	$("#one").attr("action","<c:url value='/bookDesk/listFirmFromCity.do'/>").submit();
}
</script>
</head>
<body>
<div data-role="page" data-theme="d" id="pageone"><!--页面层容器-->
	<form id="one" data-ajax="false" method="POST">
		<input type="hidden" id="pk_group" name="pk_group" value="${pk_group}">
		<input type="hidden" id="openid" name="openid" value="${openid}">
		<input type="hidden" id="lat" name="lat" value="">
		<input type="hidden" id="lng" name="lng" value="">
	</form>
</div><!-- page -->
</body>
</html>