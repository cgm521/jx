<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>我要领券</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/orderDetail.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/validate.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jweixin-1.0.0.js'/>"></script>
<style type="text/css">
.button1{
	text-align:center;
}
 .button1 input{
	padding:10px;
	width:45%;
	/* background-color:#F7F7F7;
	border:0;
	border-radius:5px; */
	color:#356893;
	text-align:center;
}
 
</style>
<script language="JavaScript" type="text/JavaScript">
// 未到店，展示订单信息
function goClose() {
	//gotoDetail("0", "0");
	wx.closeWindow();
}
$(document).ready(function(){  
    $("#showConfirm").click();
}); 
function gotoRegister() {
	location.href = "<c:url value='/myCard/cardInfo.do?openid=' />"+$("#openid").val();
}

</script>

</head>
<body>

<!-- -----------------------------------------------参与我要领券活动的结果------------------------------------------------------ -->
	<a href="#confirm" id="showConfirm" data-rel="popup" data-position-to="window"></a>
	<div data-role="popup" id="confirm" style="position:fixed; top:20%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  
		data-shadow="false" data-position-to="origin" data-corners="false" data-swipe-close="false" data-dismissible="false" data-history="false">
		<input type="hidden" />
		<input type="hidden" />
		<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr height="75px">
				<td align="center">
					<span style="font-size:120%">${res}</span>
					<br />
					<span style="color:#999999">欢迎参加我要领券活动</span>
				</td>
			</tr>
			<c:if test="${res!='请先注册会员'}">
			<tr height="75px">
				<td class="button1">
					<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="确定" data-role='none' data-ajax="false" onclick="goClose()" /> 
				</td>
			</tr>
			
			</c:if>
			<c:if test="${res=='请先注册会员'}">
				<tr height="75px">
					<td class="button1">
						<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="去注册" data-role='none' data-ajax="false" onclick="gotoRegister()" /> 	
						<input type="hidden" data-role="none" name="openid" id="openid" value="${openid}" />
					</td>
			   </tr>
			</c:if>
			
		</table>
	</div>

</body>
</html>