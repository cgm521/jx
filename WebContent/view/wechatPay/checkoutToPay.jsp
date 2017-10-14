<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%String path = request.getContextPath();
	String port ="80".equals((request.getServerPort()+""))?"":(":"+request.getServerPort()+"");
	String basePath = request.getScheme()+"://"+request.getServerName()+port+path;
%>
<html> 
<head> 
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>我要结账</title> 
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<link type="text/css" href='<c:url value="/css/wechat/orderDetail.css" />' rel="stylesheet" />
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	wx.config({
	    debug: false,
	    appId: '${appId}',
	    timestamp: '${signMap.timestamp}',
	    nonceStr: '${signMap.nonceStr}',
	    signature: '${signMap.signature}',
	    jsApiList: ['scanQRCode']
	});
</script>
<style type="text/css">
		.layer{	
			width:100%;
			height:100%;
			padding:50% 0;
			text-align:center;
			filter:ALPHA(opacity=70);
			opacity:0.7;
			background:#000;
			z-index:1101;
			position:fixed;
			top:0px;
			color:#ccc;
			margin-top:-18px;
			text-shadow:0px 0px 0px;
		}
		.wait{
			background:url(../image/wechat/waitforpay.png) no-repeat right;
			background-size:100% 100%;
			width: 278px;
			height: 241px;
		  	margin: 100px auto;
		  	position: relative;
		  	z-index:9999;
		}
		.nameDiv{
			width:50%;
			text-align: left;
			float: left;
		}
		.numDiv{
			width:23%;
			text-align: right;
			float: left;
		}
		.spaceMeaage{
			text-align:left;
			padding-left:5%;
			padding-top:5px;
			padding-bottom:5px;
			font-size:120%;
			display: none;
		}
</style>
</head>
<body>
	<div>
		<div style="display: none;width: 50%;float: left;">账单金额：</div><div style="width: 46%;float: left;text-align: right;display: none;"><span id="totalmoney">0.00</span></div>
		<div>正在等待门店端返回结账数据，请稍候。。。</div>
		<div id="zengsongDiv" style="display: none;">
			<div class="spaceMeaage">赠送</div>
		</div>
		<div id="tuicaiDiv" style="display: none;">
			<div class="spaceMeaage">退菜</div>
		</div>
		<div id="zhekouDiv" style="display: none;">
			<div class="spaceMeaage">折扣</div>
		</div>
		<form id="confirmForm" action="<c:url value='/wxPay/toConfirmPage.do'/>" method="post">
			<input type="hidden" id="id" name="id" value="${orders.id}">
			<input type="hidden" id="resv" name="resv" value="${orders.resv}">
			<input type="hidden" id="firmid" name="firmid" value="${orders.firmid}">
		</form>
	</div>
	<div class='layer'></div>
	<div class="wait"></div>
	<script type="text/javascript">
		//调用后台方法获取门店推送的结账信息
// 		InitLayer("正在等待门店端返回结账数据，请稍候。。。");
		//执行检测方法
		 setTimeout("getOrderState()",100);
		function getOrderState(){
			$.ajax({
					url:"<c:url value='/wxPay/getOrderState.do'/>?id=${orders.id}&resv=${orders.resv}",
					type:"POST",
// 					async:false,
					dataType:"json",
					success:function(data){
// 						$(".layer").hide();
// 						$(".wait").hide();
						if(data.PAYMONEY>0){//如果金额大于0 跳转到支付页面
// 							$("#confirmForm").submit();
							var url = "<%=basePath %>/wxPay/toConfirmPage.do?id=${orders.id}&firmid=${orders.firmid}&resv=${orders.resv}&code=${code}&billstate=6";
							window.setTimeout(function(){
								location.href = url;
							},100);
							return;
							/*var url = "<%=basePath %>/wxPay/toOrderPay.do?pk_group=${pk_group}&firmid=${orders.firmid}&resv=${orders.resv}&sumprice="+data.PAYMONEY+"&pax=${orders.pax}&id=${orders.id}&response_type=code&scope=snsapi_base&state=123&code=${code}&billstate=6";
// 							 		alert(url);
							window.setTimeout(function(){
								location.href = url;
							},100);*/
						}else if(data.PAYMONEY==0){//如果金额等于0 直接更新订单状态，并通知用户订单完成
							$.ajax({
								url:"<c:url value='/wxPay/updateOrdr.do'/>?id=${orders.id}&resv=${orders.resv}&openid=${orders.openid}&state=6",
								type:"POST",
								dataType:"json",
								success:function(data){
									if(data=="ok"){
										alertMsg("账单支付成功。");
										window.setTimeout(function(){
											wx.closeWindow();
										},2000);
										
									}else{
										alertMsg("账单更新状态失败。");
									}
								}
							});
						}else{
							getOrderState();
						}
					},
					error:function(data){
// 						alertMsg("等待超时，请重试。错误原因："+data);
						alertMsg("出现错误，请重试。");
					}
			});
		}
	</script>
</body>
</html>
