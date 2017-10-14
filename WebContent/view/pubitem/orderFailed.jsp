<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<link rel="stylesheet" href="<%=path %>/css/global.css"/>
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<style type="text/css">

</style>
<title>提示信息</title>
</head>
<body>
<form id="okForm" action="<%=path %>/pubitem/choiceDesk.do" method="post" data-ajax="false">
<div data-role="page" id="page1" data-overlay-theme="e">
	<div data-role="header">
  	</div>
	  <div data-role="content" style="margin:auto;text-align:center;">
	    <p id="hint" style="text-aling:center;">${msg }</p>
		<a href="#" data-role="button" id="ok">确定</a>
	    <input type="hidden" name="dat" id="dat" value="${dat}"/>
	   <input type="hidden" name="openid" id="openid" value="${openid}"/>
	   <input type="hidden" name="firmId" id="firmId" value="${firmid}"/>
	   <input type="hidden" name="sft" id="sft" value="${sft}"/>
	  </div>
</div>
</form>
<script type="text/javascript">
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
		
	
		$("#ok").click(function(){
			$("#okForm").submit();
		});
	
	});
</script>
</body>
</html>