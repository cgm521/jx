<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>订单完成</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/successpage.css" />' rel="stylesheet" />
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

function goHome(pk_group){
	InitLayer();
	var url = "<c:url value='/bookMeal/listFirm.do?pk_group=' />"+pk_group;
	window.setTimeout(function(){
		location.href = url;
	},100);
}

function viewDetail(){
	InitLayer();
	window.setTimeout(function(){
		$("#detailForm").submit();
	},100);
}

</script>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div class="top">
			<div class="contentMessage">
				<img src="<c:url value='/image/wechat/right1.png'/>"/>
				<span>订单已提交</span>
			</div>
			<div class="father_div">
				<div>
					<div class="content">订单状态:已受理</div>
				</div>
			</div>
			<div class="contentMessage">
				<p>
					我们稍后将通过微信消息通知您<br/>
					订单受理情况请稍后
				</p>
			</div>
		</div><!--  top -->
		<div class="bottonDiv">
			<form id="detailForm" data-ajax="false" action="<c:url value='/bookMeal/orderDetail.do' />">
				<input type="hidden" id="openid" name="openid" value="${openid}">
				<input type="hidden" id="orderid" name="orderid" value="${orderid}">
				<input type="hidden" id="firmid" name="firmid" value="${firmid}">
				<input type="hidden" id="pk_group" name="pk_group"  value="${pk_group}">
				<p>
					<input type="button" value="查看订单详情" data-role='none' onclick="javaScript:viewDetail();" /> 
				</p>
				<p>
					<input type="button" value="返回首页" class="button" data-role='none' data-ajax="false" onclick="javaScript:goHome('${pk_group}');">
				</p>
			</form>
		</div><!-- -->
	</div>
</div>
</body>
</html>