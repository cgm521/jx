<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>在线充值</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/myCardCharge.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/myCardVoucher.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/validate.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
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
		
		$("#payBtn").on("tap",function(){
			if($("#chargeMoney").val() == '') {
				alertMsg("请输入充值金额");
				return;
			}
			
			if($("#chargeMoney").val() <= 0) {
				alertMsg("充值金额必须大于0");
				return;
			}
			var validate1 = new Validate({
				validateItem:[{
					type:'text',
					validateObj:'empNoRecommend',
					validateType:['num'],
					param:['F'],
					error:['只能为数字']
				}]
			});
			if(!validate1._submitValidate()){
				return;
			}
			callpay();
		});
	});
	
	// 当微信内置浏览器完成内部初始化后会触发WeixinJSBridgeReady事件。
    //公众号支付
	function callpay(){
		var para={};
		para["amt"] = $("#chargeMoney").val() * 100;
		para["appid"] = "${appId}";
		para["orderid"] = orderId;
		para["outTradeNo"] = orderId;
		para["firmName"] = "会员卡充值";
		para["empNoRecommend"]=$("#empNoRecommend").val();
		$.ajaxSetup({async:false});
		$.post("<c:url value='/wxPay/enterPay.do?' />",para,function(data){
			if($.trim(data)){
				var obj = eval('(' + data + ')');
				if(parseInt(obj.agent)<5){
					alertMsg("您的微信版本低于5.0无法使用微信支付");
					return;
				}
				WeixinJSBridge.invoke('getBrandWCPayRequest',{
		  			"appId" :"${appId}", //公众号id，由商户传入
		  			"timeStamp" : obj.timeStamp, //时间戳
		  			"nonceStr" : obj.nonceStr, //随机串
		  			"package" : obj.package,//扩展包
					"signType" : obj.signType, //微信签名方式:1.sha1
					"paySign" : obj.paySign //微信签名
		  		},function(res){
		  			WeixinJSBridge.log(res.err_msg);  
		      		if(res.err_msg == "get_brand_wcpay_request:ok"){
						// 调用充值方法
						window.location.href = "<%=path %>/view/myCard/chargeSuccess.jsp";
		          		return "1";
				    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
				        alertMsg("用户取消支付");  
				        InitLayer();
						$.ajax({
							url:"<c:url value='/myCard/resetOrderId.do'/>",
							type:"POST",
							dataType:"json",
							data:{},
							success:function(data){
								orderId = data;
								closeLayer();
							},
							error:function(ajax){
								closeLayer();
							}
						});
			          	return "2";
				    }else{  
				    	alertMsg("支付失败,失败原因：err_code="+res.err_code+" err_desc="+res.err_desc+" err_msg="+res.err_msg);  
						InitLayer();
						$.ajax({
							url:"<c:url value='/myCard/resetOrderId.do'/>",
							type:"POST",
							dataType:"json",
							data:{},
							success:function(data){
								orderId = data;
								closeLayer();
							},
							error:function(ajax){
								closeLayer();
							}
						});
			          	return "3";
				    }
		  		// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
		  		//因此微信团队建议，当收到ok返回时，向商户后台询问是否收到交易成功的通知，若收到通知，前端展示交易成功的界面；若此时未收到通知，商户后台主动调用查询订单接口，查询订单的当前状态，并反馈给前端展示相应的界面。
		  		}); 
			}else{
				alertMsg("获取预支付信息出错，支付失败。");
				return;
			}
		});
	}
	
	function changeMoney() {
		$("#canGet").text("0");
		$("#payMoney").text("0");
		if($("#chargeMoney").val() == '') {
			alertMsg("请输入充值金额");
			return;
		}
		
		if($("#chargeMoney").val() <= 0) {
			alertMsg("充值金额必须大于0");
			return;
		}
		
		// 计算赠送金额
		var chargeMoney = $("#chargeMoney").val();
		InitLayer();
		$.ajax({
			url:"<c:url value='/myCard/calcGiftAmt.do'/>",
			type:"POST",
			dataType:"json",
			data:{"chargeMoney":chargeMoney,"cardId":"${card.cardId}"},
			success:function(data){
				if(data >= 0) {
					$("#canGet").text(parseFloat($("#chargeMoney").val()) + parseFloat(data));
					$("#giftAmt").val(data);
				}
				closeLayer();
			},
			error:function(ajax){
				alertMsg("赠送金额计算失败，请稍后再试！");
				closeLayer();
			}
		});
		
		
		$("#payMoney").text($("#chargeMoney").val());
	}
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
		<input type="hidden" id="appid" name="appid" value="${APPID }"/>
		<input type="hidden" id="package" name="package" value="${PACKEGEVALUE }"/>
		<input type="hidden" id="sign" name="sign" value="${SIGN }"/>
		<input type="hidden" id="timestamp" name="timestamp" value="${TIMESTAMP }"/>
		<input type="hidden" id="nonce" name="nonce" value="${NONCE }"/>
		<input type="hidden" id="signtype" name="signtype" value="${SIGNTYPE }"/>
		<input type="hidden" id="giftAmt" name="giftAmt" value="${giftAmt }"/>
		<div class="header">
			<img src="<c:url value='/image/wechat/laba.png'/>" /><span>会员卡余额：<span style="font-family:Arial;">${card.zAmt }</span></span>
		</div>
		
		<div class="tdTitle">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span>充值金额</span></div>
			</div>
		</div>
		<div class="tdContent">
			<table width="100%" height="60px" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="td_left" width="30%">
						<input type="number" data-role="none" class="inputNum" name="chargeMoney" id="chargeMoney" min="0" value="100" onchange="javascript:changeMoney()" />
					</td>
					<td width="5%"></td>
					<td class="td_right" width="60%" style="text-align:center;">
						<div class="btn" data-role='none' style="width:90%" >实际到帐<span id="canGet" style="font-family:Arial;">${canGet }</span>元</div>
					</td>
					<td width="5%"></td>
				</tr>
			</table>
		</div>
		<!-- 选择支付方式 -->
		<div class="tdTitle">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span>支付方式</span></div>
			</div>
		</div>
		<div class="tdContent">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr onclick="changePayType(this, '1')">
					<td class="td_left" style="width:50px;">
						<img src="<c:url value='/image/wechat/wechat.png'/>" width="45px" height="45px" />
					</td>
					<td align="left">
						<p>
							<span class="payTypeName">微信支付</span><br />
							<span class="payTypeInfo">微信官方支付，方便快捷</span>
						</p>
					</td>
					<td class="pay-right">
						<img id="wxPay" src="<c:url value='/image/wechat/right.png'/>"/>
					</td>
				</tr>
			</table>
		</div>
		<div style="border-top:1px solid #E5E5E5; height:45px; width:90%; text-align:right; margin:auto;">
			<span style="line-height:45px; font-size:120%">付款金额：</span>
			<span id="payMoney" style="line-height:45px; font-size:130%; color:#FFB400; font-family:Arial;">100</span>
		</div>
		<div style="display:none" class="tdTitle">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span>推荐人员工号</span></div>
			</div>
		</div>
		<div style="display:none" class="tdContent">
			<table width="100%" height="60px" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="td_right" width="30%" style="text-align:center;">		
						<input  class="inputNum" data-role="none"  name="empNoRecommend" id="empNoRecommend" value="" placeholder="可以为空"/>
					</td>
					<td width=""></td>
				</tr>
			</table>
		</div>
		<div>
			<table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="colBtn">
						<div id="payBtn" class="btn" data-role='none'>确认支付</div> 
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="ui-a">
					<a href="<c:url value='/myCard/cardInfo.do?pk_group=${card.pk_group}&openid=${card.openid }' />" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
			</tr>
		</table>
  	</div>
</div>
</body>
</html>