<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path = request.getContextPath();
	String port ="80".equals((request.getServerPort()+""))?"":(":"+request.getServerPort()+"");
	String basePath = request.getScheme()+"://"+request.getServerName()+port+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>买单</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/maidan.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/validate.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script language="JavaScript" type="text/JavaScript">
	var result= "";
	wx.config({
	    debug: false,
	    appId: '${appId}',
	    timestamp: '${signMap.timestamp}',
	    nonceStr: '${signMap.nonceStr}',
	    signature: '${signMap.signature}',
	    jsApiList: ['scanQRCode']
	});
	wx.ready(function () {
		//调用微信扫一扫功能，获取二维码中的台位号
		$("#scanPay").click(function (){
			wx.scanQRCode({
			    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
			    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
			    success: function (res) {
			   		result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
			   		if(result != null && result != ""){//跳转到支付页面
			   			openPayPage(result);
			   		}
				}
			});
		});
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
		window.history.go(1);//禁用后退按钮功能，点击后自动回到上个页面
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
			$("body").text("请使用微信浏览器打开");
			return;
		}
	});
	//输入付款金额买单
	function payForOrder(){
		var validate = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'paymoney',
				validateType:['num2','handler'],
				handler:function(){
					if($("#paymoney").val()<=0){
						return false;
					}else{
						return true;
					}
				},
				param:['F','F'],
				error:['必须为大于0的数字','必须为大于0的数字']
			}]
		});
		
		if(validate._submitValidate()){
			//跳转到支付页面
			var card={};
			card["tele"]=$("#tele").val();
			$.post("<%=path %>/card/sendSms.do",card,function(data){
				$("#rannum").val(data);
			});
		}
	}
	//打开我的点菜单
	function openMyOrdrs(){
		InitLayer();
		var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=<%=basePath %>/bookDesk/findMenuOrders.do?pk_group=${pk_group}&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		window.setTimeout(function(){
			location.href = url;
		},500);
	}
	//打开支付页面
	function openPayPage(result){
		var url = "<%=basePath %>/wxPay/toOrderPay.do?pk_group=${pk_group}&firmid=${firmid}&"+result+"&response_type=code&scope=snsapi_base&state=123&code=${code}&billstate=6";
<%-- 		var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=<%=basePath %>/wxPay/toOrderPay.do?pk_group=${pk_group}"+result+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect"; --%>
// 		alert(url);
		window.setTimeout(function(){
			location.href = url;
		},500);
	}
</script>
<style type="text/css">
</style>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div class="firm" id="scanPay">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="topRadius">
					<td class="detail"  rowspan="2">
						<div style="float:left;"><img src="<c:url value='/image/wechat/maidan_03.png'/>" /></div>
						<div style="float:left;margin-left: 10px;">
							<span style="font-size: 120%;">扫码买单</span><br>
							<span style="padding-top: 5px;line-height: 25px;font-size: 80%;">扫码小票或者店铺支付二维码</span>
						</div>
						<div style="float:right;text-align: right;"><img src="<c:url value='/image/wechat/maidan_06.png'/>" /></div>
					</td>
				</tr>
			</table>
		</div>
		<div class="spaceMeaage" style="padding: 0px;">
		</div>
		<div class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="topRadius">
					<td class="detail">输入买单金额</td>
				</tr>
				<tr class="bottomRadius">
					<td class="detail2" style="text-align: left;">
						<input type="number"  data-role="none" id="paymoney" name="paymoney" class="inputContent" >
						<input type="button" value="确认支付" data-role='none' data-ajax="false" onclick="javaScript:payForOrder()">
					</td>
				</tr>
			</table>
		</div>
		<div class="orderSpace">
			&nbsp;
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a" style="width:75%;">
					<a href="${pageFrom}" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
				<td style="background-color: #F39801;color: white;width:25%;text-align: center;" id="orderHistory" onclick="openMyOrdrs()">
					历史订单
				</td>
			</tr>
		</table>
  	</div>
</div><!-- page -->
</body>
</html>