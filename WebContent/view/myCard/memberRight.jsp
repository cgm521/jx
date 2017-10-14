<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员特权</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/myCardCharge.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/myCardVoucher.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script language="JavaScript" type="text/JavaScript">
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	
	var orderId = "${orderId}";
	
	$(function(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
			$("body").text("请使用微信浏览器打开");
			return;
		}
	});
</script>
<style type="text/css">
.header{
	width:100%;
	color:#FFB400;
	text-align:center;
	padding-bottom:10px;
	padding-top:10px;
	font-size:120%;
}
.header img{
	width:20px;
	height:20px;
}
.tdTitle{
	background-color:#E5E5E5;
	font-weight:bold;
	text-align:left;
	width:100%;
	height:40px;
	border:0;
	margin:0;
}
.tdContent{
	width:100%;
	border:0;
	margin:0;
	padding:0;
	overflow:hidden;
}
.space{
	width:100%;
	border:0;
	margin:0;
	height: 10px;
}
.father_div{
	overflow:hidden;
}
.td_left{
	width:30%;
	text-align: left;
	padding-left:5%;
}
.td_right{
	width:70%;
	/*padding-right:20px;*/
}
.space-left{
	float:left;
	width:5%;
}
.store_right{
	float:left;
	width:95%;
}
.pay-right{
	width:40%;
	text-align:right;
	padding-right:5%;
	vertical-align:middle;
}
.pay-right img{
	width:50px;
	height:50px;
}
.payTypeName{
	font-size:110%;
}
.payTypeInfo{
	font-size:75%;
	color:#656565;
}
.colBtn{
	text-align:center;
	padding:10px;
}
.btn{
 	background-color:#FFB400;
	border: 1px solid #EEEEEE;
	border-radius: 4px;
	color:#FFFFFF;
 	height: 35px; 
 	width: 100%;
	line-height: 2;
	float: center;
}
.inputNum{
	background-color: #FFFFFF;
	border:1px solid #EEEEEE;
	border-radius:4px;
	height:35px;
	width:95%;
	-webkit-appearance:none !important;
    margin: 0; 
    font-family:Arial;
}
</style>
</head>
<body>
<div data-role="page" class="page" data-theme="d">
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;">
		<div class="tdTitle">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span>您可以享受到的会员特权包含：</span></div>
			</div>
		</div>
		<div class="tdContent">
			<table width="100%" height="60px" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="td_left" width="30%">
						${memberRight.memberRight }
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="ui-a">
					<a href="<c:url value='/myCard/cardInfo.do?pk_group=${card.pk_group}&openid=${openid }' />" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
			</tr>
		</table>
  	</div>
</div>
</body>
</html>