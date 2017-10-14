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
<title>会员卡信息</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/card.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/validate.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
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
	
	// 发送验证码
	$("#sendSms").click(function(){
		var validate1 = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'tele',
				validateType:['canNull'],
				param:['F'],
				error:['手机号码不能为空']
			},{
				type:'text',
				validateObj:'tele',
				validateType:['mobile'],
				error:['手机号码格式不正确']
			}]
		});
		
		if(validate1._submitValidate()){
			time();
			var card={};
			card["tele"]=$("#tele").val();
			$.post("<%=path %>/card/sendSms.do",card,function(data){
				$("#rannum").val(data);
			});
		}
	});
	
	// 提交前验证
	var validate = new Validate({
		validateItem:[{
			type:'text',
			validateObj:'cardNo',
			validateType:['canNull'],
			param:['F'],
			error:['会员卡号不能为空']
		},{
			type:'text',
			validateObj:'tele',
			validateType:['canNull'],
			param:['F'],
			error:['手机号码不能为空']
		},{
			type:'text',
			validateObj:'valicode',
			validateType:['canNull'],
			param:['F'],
			error:['不能空']
		},{
			type:'text',
			validateObj:'tele',
			validateType:['mobile'],
			error:['手机号码格式不正确']
		}]
	});
	
	// 绑定
	$("#bindBtn").click(function(){
			if(validate._submitValidate()){
				InitLayer();
				var rannum=$("#rannum").val();
				var valicode=$("#valicode").val();
				if(rannum == valicode){
					$("#valicode").css("background", "#0F3");
					var card={};
					card["tele"]=$("#tele").val();
					card["openid"]=$("#openid").val();
					card['cardNo'] = $("#cardNo").val();
					$.post("<%=path %>/myCard/verifyCardExist.do", card, function(data){
						if(data == 1) {
							var url = "<c:url value='/myCard/saveWXCard.do?pk_group=' />" + $("#pk_group").val() + "&cardNo=" + $("#cardNo").val()
									+ "&openid=" + $("#openid").val();
							location.href = url;
						} else {
							alertMsg("会员卡不存在，请确认会员卡号是否正确！");
							closeLayer();
						}
					});
				}else{
					closeLayer();
				}
			}else{
				closeLayer();
 			}
	});
});

</script>
<style type="text/css">
.td_left{
	width:30%;
	text-align: left;
	padding-left:2%;
}
.td_right{
	width:70%;
}
.inputContent{
	background-color: #FFFFFF;
	border:1px solid #EEEEEE;
	border-radius:4px;
	height:35px;
	width:95%;
}
</style>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div class="header" style="height:250px; background-size:60% 70%;">
			<ul style="padding:25% 20% 0 0;">
				<li style="margin:4% 0 0 3%;"><span style="font-size:120%">NO.${card.cardNo }</span></li>
				<li style="float:right; padding:30px 30px 0 0; list-style-type:none;"><span>使用说明>></span></li>
			</ul>
		</div>
		<div class="account">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="topRadius">
					<td class="td_left">
						会员卡号
					</td>
					<td class="td_right" colspan="2">
						<input type="text" data-role="none" class="inputContent" name="cardNo" id="cardNo" value="" length="160">
					</td>
				</tr>
				<tr>
					<td class="td_left">
						联系电话
					</td>
					<td>
						<input type="text" data-role="none" class="inputContent" name="tele" id="tele" value="">
					</td>
					<td>
						<input type="button" data-role="none" id="sendSms" value="点击发送验证码">
						<input type="hidden" data-role="none" name="openid" id="openid" value="${openid }" />
						<input type="hidden" data-role="none" name="pk_group" id="pk_group" value="${pk_group }" />
						<input type="hidden" id="rannum" value="111111">
					</td>
				</tr>
				<tr>
					<td class="td_left">
						验证码
					</td>
					<td class="td_right">
						<input type="text" data-role="none" class="inputContent" name="valicode" id="valicode" value="">
					</td>
				</tr>
			</table>
		</div>
		<div>
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="bottomRadius">
					<td class="detail2">
						<p>
							<input type="button" id="bindBtn" value="绑定" data-role='none' data-ajax="false" /> 
						</p>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a">
					<a href="<c:url value='/myCard/cardInfo.do?pk_group=${pk_group}' />" data-ajax="false">
						<img src="<c:url value='/image/wechat/back.png'/>"/>&nbsp;
					</a>
				</td>
			</tr>
		</table>
  	</div>
</div>
</body>
</html>