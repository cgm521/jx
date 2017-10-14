<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<link rel="stylesheet" href="<%=path %>/css/default/global.css"/>
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<style type="text/css">

</style>
<title>提示信息</title>
</head>
<body>
<form id="okForm" action="<%=path %>/pubitem/listPubitem.do" method="post" data-ajax="false">
<div data-role="page" id="page1" data-overlay-theme="e">
	<div data-role="header">
	    <!-- <h1>温馨提示</h1> -->
  	</div>
	  <div data-role="content" style="margin:auto;text-align:center;">
	    <p id="hint" style="text-aling:center;">订位成功是否继续点餐?</p>
	    <div style="width:100%;">
		    <div style="float:left;width:30%;margin-left:15%;"><a style="text-decoration:none;" href="#" data-role="button" id="ok">是</a></div>
		    <div style="float:right;width:30%;margin-right:15%;"><a style="text-decoration:none;" href="#" data-role="button" id="cancle">否</a></div>
	    </div>
	    <input type="hidden" name="id" id="orderid" value="${orderid}"/>
	   <input type="hidden" name="openid" id="openid" value="${openid}"/>
	   <input type="hidden" name="firmid" id="firmid" value="${firmid}"/>
	   <input type="hidden" name="remark" id="remark" value="${remark}"/>
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
		
		var id = $("#orderid").val();
		var openid = $("#openid").val();
		var firmid = $("#firmid").val();
		$("#ok").click(function(){
			$("#okForm").submit();
		});
		$("#cancle").click(function(){
			window.location.href="<%=path %>/pubitem/enterMyOrder.do?id="+id+"&openid="+openid+"&firmid="+firmid;
		});
		
	});
</script>
</body>
</html>