<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.choice.test.utils.Commons"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
<title><%=Commons.wx_title %></title>
<link type="text/css"
	href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />'
	rel="stylesheet" />
<link type="text/css"
	href='<c:url value="/css/wechat/dining/homePage.css" />'
	rel="stylesheet" />
<script type="text/javascript"
	src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jweixin-1.0.0.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script language="JavaScript" type="text/JavaScript">
/*去除安卓返回及刷新按钮*/
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});
$(function(){
	/* */
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)!="micromessenger") {
		$("body").text("请使用微信浏览器打开");
		return;
	}
	
	/* var x = navigator;
	var w = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
	var h = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
	alertMsg("<b>屏幕分辨率:</b>"+screen.width + "X" + screen.height+"<b>内部窗口</b>:" + w + "X" + h); */
	
});
function listFirm(a,baseUrl){
	InitLayer();
	var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+"<%=Commons.appId %>"+"&redirect_uri=" +
			baseUrl + "/listFirm.do" +
			"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
	var img = $(a).find("img");
	var imgUrl = img[0].src;
	img.attr("src",imgUrl.replace("_up","_d"));
	location.href = url;
}

function toMyCard(a,baseUrl){
	baseUrl = baseUrl.replace("dining", "");
	baseUrl += "/myCard/cardInfo.do";
	InitLayer();
	var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+"<%=Commons.appId %>"+"&redirect_uri=" +
			baseUrl + "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";

	var img = $(a).find("img");
	var imgUrl = img[0].src;
	img.attr("src",imgUrl.replace("_up","_d"));
	window.setTimeout(function(){
		location.href = url;
	},100);
}

function scanCodeToMenu() {
	wx.scanQRCode({
	    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    success: function (res) {
	   		tableId = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
	   		alertMsg(tableId);
		}
	});
}
</script>
</head>
<body>
	<div data-role="page" data-theme="d" id="pageone"
		style="margin: 0; list-style: none; padding: 0;">
		<div data-role="content"
			style="width: 100%; overflow: hidden; margin: 0; list-style: none; padding: 0;">
			<div id="logo">
				<table>
					<tr>
						<td><img width="100%"
							src="<c:url value='/image/wechat/top.jpg'/>" /></td>
					</tr>
				</table>
			</div>
			<div class="clickImg">
				<table>
					<tr>
						<td class="buttonImg">
							<a onclick="listFirm(this,'<%=Commons.getConfig().getProperty("baseurl") %>');">
								<img src="<c:url value='/image/wechat/zhaocanting_up.png'/>" />
							</a></br>
							找餐厅
						</td>
						<%-- <td class="buttonImg">
							<a onclick="listFirm(this,'${baseUrl}');">
								<img src="<c:url value='/image/wechat/dengwei_up.png'/>" />
							</a></br>
							等位
						</td> --%>
						<td class="buttonImg">
							<a onclick="toMyCard(this,'<%=Commons.getConfig().getProperty("baseurl") %>');">
								<img src="<c:url value='/image/wechat/huiyuan_up.png'/>" />
							</a></br>
							会员
						</td>
						<td class="buttonImg">
							<a onclick="listFirm(this,'<%=Commons.getConfig().getProperty("baseurl") %>');">
								<img src="<c:url value='/image/wechat/waimai_up.png'/>" />
							</a></br>
							外卖
						</td>
						<%-- <td class="buttonImg">
							<a class="scanToOrders" onclick="">
								<img src="<c:url value='/image/wechat/waimai_up.png'/>" />
							</a></br>
							扫码点餐
						</td> --%>
					</tr>
				</table>
			</div>
			<div id="storeup">
				<table>
					<tr>
						<td>
							<img width="100%" src="<c:url value='/image/wechat/middle.jpg'/>" />
						</td>
					</tr>
				</table>
			</div>
			<div class="clickImg">
				<div class="actmTitle">
					<image src="<c:url value='/image/wechat/yuding.png'/>" />
					<span>&nbsp;活动盘点</span>
				</div>
				<div style="padding-top: 10px;">
					<table>
						<tr>
							<td class="actmImgLeft">
								<a href="<%=Commons.getConfig().getProperty("actmpicurl") %>" data-ajax="false"><img width="100%" src="<c:url value='/image/wechat/g1.png'/>" /></a>
							</td>
							<td class="actmImgRight">
								<a href="<%=Commons.getConfig().getProperty("actmpicurl") %>" data-ajax="false"><img width="100%" src="<c:url value='/image/wechat/g2.png'/>" /></a>
							</td>
						</tr>
						<tr>
							<td class="actmImgLeft">
								<a href="<%=Commons.getConfig().getProperty("actmpicurl") %>" data-ajax="false"><img width="100%" src="<c:url value='/image/wechat/g3.jpg'/>" /></a>
							</td>
							<td class="actmImgRight">
								<a href="<%=Commons.getConfig().getProperty("actmpicurl") %>" data-ajax="false"><img width="100%" src="<c:url value='/image/wechat/g4.png'/>" /></a>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<!-- content -->
	</div>
	<!-- page -->
</body>
</html>