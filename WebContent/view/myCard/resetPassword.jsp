<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%	String pk_group = request.getParameter("pk_group");
	String openid = request.getParameter("openid");
	String cardId = request.getParameter("cardId");
	String tele = request.getParameter("tele");
	String path = request.getContextPath(); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>设置密码</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/myCardVoucher.css" />' rel="stylesheet" />
<link rel="stylesheet" href="<c:url value='/css/validate.css'/>" />
<link rel="stylesheet" href="<c:url value='/css/card.css'/>" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script language="JavaScript" type="text/JavaScript">
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    /* WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu'); */
	});
	
	function backHome() {
		var url = "<%=path %>/myCard/cardInfo.do?pk_group=<%=pk_group %>&openid=<%=openid %>";
		window.location.href = url;
	}
	
	function setPassword(){
		var validate = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'oldPassword',
				validateType:['canNull'],
				param:['F'],
				error:['不能为空']
			},{
				type:'text',
				validateObj:'newPassword',
				validateType:['canNull'],
				param:['F'],
				error:['不能为空']
			},{
				type:'text',
				validateObj:'confirmPassword',
				validateType:['canNull', 'accordance'],
				param:['F', $('#newPassword')],
				error:['不能为空', '两次输入不一致']
			}]
		});
		//校验通过
		if(validate._submitValidate()){
			var cardId = "<%=cardId %>";
			var oldPassword = $("#oldPassword").val();
			var passwd = $("#newPassword").val();
			InitLayer();
			
			// 校验原密码是否正确
			$.ajax({
				url:"<c:url value='/myCard/verifyPass.do'/>",
				type:"POST",
				dataType:"json",
				data:{"cardId":cardId, "passwd":oldPassword},
				success:function(data){
					if(data == "1") {
						// 原密码正确
						$.ajax({
							url:"<c:url value='/myCard/setPassword.do'/>",
							type:"POST",
							dataType:"json",
							data:{"cardId":cardId, "passwd":passwd},
							success:function(data){
								if(data == "1") {
									closeLayer();
									alertMsg("更新密码成功");
									backHome();
								} else {
									closeLayer();
									alertMsg("更新密码失败");
								}
							},
							error : function(ajax) {
								closeLayer();
								alertMsg("更新密码失败");
							}
						});
					} else {
						closeLayer();
						alertMsg("原密码输入错误");
					}
				},
				error : function(ajax) {
					closeLayer();
					alertMsg("原密码输入错误");
				}
			});
		}
	}
	
	function resetPassword() {
		var cardId = "<%=cardId %>";
		var tele = "<%=tele %>";
		
		confirmMsg("新密码将会发送到您注册的手机号。确认重置？",function(){
			$.ajax({
				url:"<c:url value='/myCard/resetPassword.do'/>",
				type:"POST",
				dataType:"json",
				data:{"cardId":cardId, "tele":tele},
				success:function(data){
					closeLayer();
					if(data == "OK") {
						// 重置成功
						alertMsg("密码重置成功，请查收短信");
					} else {
						alertMsg("密码重置失败，请稍后重试");
					}
				},
				error : function(ajax) {
					closeLayer();
					alertMsg("密码重置失败，请稍后重试");
				}
			});
		});
	}
</script>
<style type="text/css">
.oneOrder{
	margin-top:10px;
	background-color:#ffffff;
	width:90%;
	margin-left:5%;
	margin-right:5%;
	border:1px solid #ddd;
}
.detail2{
	background-color:#f7f7f7;
	text-align:center;
	padding:10px;
}
.detail2 p input{
	padding:10px;
	width:50%;
	background-color:#ffb400;
	border:0;
	border-radius:5px;
	color:#FFFFFF;
	text-align:center;
}
</style>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0px; padding-top:0px; background-color:#F7F7F7;"><!--页面主体-->
		<input type="hidden" id="pk_group" value="${pk_group}">
		<input type="hidden" id="openid" value="${openid}">
		
		<div class="oneOrder" data-ajax="false" style="border:1px solid #ddd;float:left;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 96%;margin: 0px auto;">
				<tr>
					<td style="text-align:left;padding-left:10px;">
						原密码
					</td>
					<td>
						<input name="oldPassword" id="oldPassword" type="password" />
					</td>
				</tr>
				<tr>
					<td style="text-align:left;padding-left:10px;">
						新密码
					</td>
					<td>
						<input name="newPassword" id="newPassword" type="password" />
					</td>
				</tr>
				<tr>
					<td style="text-align:left;padding-left:10px;">
						确认密码
					</td>
					<td>
						<input name="confirmPassword" id="confirmPassword" type="password" />
					</td>
				</tr>
				<tr height="30px">
					<td class="detail2" colspan="2">
						<p><input type="button" value="确认" data-role='none' data-ajax="false" onclick="setPassword()" style="background-color:#FFB400; color:#FFFFFF" /></p> 
					</td>
				</tr>
				<tr height="30px" onclick="resetPassword()">
					<td class="detail2" colspan="2">
						<span style="color:blue;">忘记密码？点击此处重置密码</span>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="ui-a">
					<a href="javascript:backHome()" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
			</tr>
		</table>
  	</div>
</div>
</body>
</html>