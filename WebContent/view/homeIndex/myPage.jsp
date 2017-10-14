<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%String path = request.getContextPath();
	String port ="80".equals((request.getServerPort()+""))?"":(":"+request.getServerPort()+"");
	String basePath = request.getScheme()+"://"+request.getServerName()+port+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
<title>我的</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/card.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/homeIndex/myPage.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<style>
.detail1{
	background-color:#FFFFFF;
	text-align:left;
	padding:10px;
	padding-left:20px;
	BORDER-TOP: 1px solid #EEEEEE; 
}
.detail5{
	background-color:#FFFFFF;
	text-align:right;
	padding-right:20px;
	BORDER-TOP: 1px solid #EEEEEE; 
}
</style>

<script language="JavaScript" type="text/JavaScript">
	wx.config({
	    debug: false,
	    appId: '${appId}',
	    timestamp: '${signMap.timestamp}',
	    nonceStr: '${signMap.nonceStr}',
	    signature: '${signMap.signature}',
	    jsApiList: ['closeWindow']
	});
	wx.ready(function () {
		//调用微信扫一扫功能，获取二维码中的台位号
		$("#closeWindow").click(function (){
			wx.closeWindow();
		});
		closeLayer();
	});
	
	wx.error(function (res) {
		alert(res.errMsg);
	});
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	$(function(){
		InitLayer();
		/* 
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
			$("body").text("请使用微信浏览器打开");
			return;
		}
		*/
	});
	function openPage(baseUrl){
		InitLayer();
		var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=" +
				baseUrl + 
				"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
	
		window.setTimeout(function(){
			location.href = url;
		},100);
	}
	function gotoPage(tr,url){
		$(tr).find('td').css('background-color','#eeeeee');
		window.setTimeout(function(){
			InitLayer();
			window.setTimeout(function(){
				location.href = url;
			}, 100);
		},500);
	}
</script>
<style type="text/css">
img{
	vertical-align:middle;
}
.preImg{
	height: 20px;
	width:20px;
}
</style>
</head>
<body>
	<div data-role="page" data-theme="d" id="pageone" style="margin: 0; list-style: none; padding: 0;">
		<div data-role="content" style="width: 100%; overflow: hidden; margin: 0; list-style: none; padding: 0;">
			<div id="logo" style="background: url(<%=path%>/image/takewelcome.png)  no-repeat right; background-size: 100% 100%; height:200px;">
				<div style="height:  110px;color: white;">
					<table class="memberInfo">
						<tbody>
							<tr>
								<td rowspan="2" style="padding-top: 40px;width: 38%;"><img style="width: 60px; height: 60px;" src="${headimg }" /></td>
								<td style="padding-top: 40px; text-align: left;">${memList[2] }</td>
							</tr>
							<tr>
								<td style=" text-align: left;">${memList[3] }</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="clickImg" >
					<table>
						<tr>
							<%-- <td class="buttonImg">
								<a onclick="openPage('<%=basePath%>/bookDesk/findMenuOrders.do?pk_group=${pk_group}');">
									<img src="<c:url value='/image/wechat/dingdan_up.png'/>" />
								</a><br/>
								我的订单 	
							</td> --%>
							<td class="buttonImg">
                                <a onclick="openPage('<%=basePath%>/bookDesk/findOrders.do?pk_group=${pk_group}&code=${code}');" data-ajax="false">
                                	<img src="<c:url value='/image/wechat/yuding_d.png'/>" />
                                </a><br/>
								订位单 	
							</td><td class="buttonImg">
                                <a onclick="openPage('<%=basePath%>/bookDesk/findMenuOrders.do?pk_group=${pk_group}&code=${code}');" data-ajax="false">
                                	<img src="<c:url value='/image/wechat/yuding_d.png'/>" />
                                </a><br/>
								点菜单 	
							</td>
	<!-- 						<td class="buttonImg"> -->
	<%-- 							<a onclick="openPage('${baseUrl}');"> --%>
	<%-- 								<img src="<c:url value='/image/wechat/dengwei_d.png'/>" /> --%>
	<!-- 							</a><br/> -->
	<!-- 							点菜订单 -->
	<!-- 						</td> -->
							<td class="buttonImg">
								<a onclick="openPage('<%=basePath%>/myCard/cardInfo.do');">
									<img src="<c:url value='/image/wechat/huiyuan_d.png'/>" />
								</a><br/>
								我的会员
							</td>
							<td class="buttonImg">
                                <a onclick="openPage('<%=basePath%>/waitSeat/myWaitInfo.do?pk_group=${pk_group}&openId=${openid }&code=${code}');" data-ajax="false">
                                	<img src="<c:url value='/image/wechat/yuding_d.png'/>" />
                                </a><br/>
								我的等位
							</td>
	<!-- 						<td class="buttonImg"> -->
	<%-- 							<a onclick="openPage('${baseUrl}');"> --%>
	<%-- 								<img src="<c:url value='/image/wechat/maidan_d.png'/>" /> --%>
	<!-- 							</a><br/> -->
	<!-- 							外卖订单 -->
	<!-- 						</td> -->
						</tr>
					</table>
				</div>
			</div>
			<div class="account">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr onclick="">
						<td class="detail">
							<img class="preImg" src="<%=path %>/image/wechat/rmb.png">
							卡内余额
						</td>
						<td class="detail6">${memList[7] }</td>
					</tr>
					<tr onclick="javaScript:gotoPage(this,'<%=path %>/myCard/myCardVoucher.do?openid=${openid }&pk_group=${pk_group }');">
						<td class="detail1">
							<img class="preImg" src="<%=path %>/image/wechat/quan.png">
							代金券
						</td>
						<td class="detail5"><img class="imgRarrow" src="<%=path %>/image/wechat/r.png"></td>
					</tr>
					<tr onclick="">
						<td class="detail1">
							<img class="preImg" src="<%=path %>/image/wechat/jin.png">
							现金红包
						</td>
						<td class="detail5"><img class="imgRarrow"  src="<%=path %>/image/wechat/r.png"></td>
					</tr>
					<tr onclick="">
						<td class="detail1">
							<img class="preImg" src="<%=path %>/image/wechat/dui.png">
							兑换券
						</td>
						<td class="detail5"><img class="imgRarrow" src="<%=path %>/image/wechat/r.png"></td>
					</tr>
				</table>
			</div>
			<div class="account" style="padding-top:0px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr>
						<td class="detail">
							${vcompanyname }
						</td>
						<td class="detail6">
							<a href="tel:${vcompanytel }">
								<img style="height: 34px;" src="<%=path %>/image/wechat/bohao.png">
							</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	<%-- <div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a">
					<a id="closeWindow" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
			</tr>
		</table>
  	</div> --%>
		<!-- content -->
		<!--<div id="amtDiv" >
			${memList[7] }
		</div>
		<div id="couponDiv">
			<c:forEach items="${listCoupon }" var="coupon" varStatus="status">
				<div id="${coupon. vvouchercode}" class="cashCoupDiv" 
					onclick="addCoupon('${coupon. vvouchercode}','${coupon.zkmoney}','${coupon.paycode}','${coupon.payname}')"
				 	style="background:url(<%=path%>/image/wechat/${coupon.pic })  no-repeat right; background-size: 100% 100%;">
					<div style="float: left;width: 25%;color: ${coupon.fontcolor};"><h1>立减${coupon.zkmoney}元</h1></div>
					<div style="float: left; margin-top: 10px;color: white;">
						<span style="font-size: 30px;">${coupon. name}</span><br>
						<span style="font-size: 15px;">有效期至：${coupon.yxdate }</span>
					</div>
				</div>
			</c:forEach>
		</div>-->
	</div>
	<!-- page -->
</body>
</html>