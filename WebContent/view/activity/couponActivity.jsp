<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>优惠券分享</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/orderDetail.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/validate.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jweixin-1.0.0.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script language="JavaScript" type="text/JavaScript">
	function getCoupon() {
		var haveGot = "${haveGot}";
		var haveRegister = "${haveRegister}";
		if(haveGot == "Y") {
			alertMsg("您已领取过该优惠券");
		} else {
			if(haveRegister == "Y") {
				saveCoupon();
			} else {
				$('#contact').click();
			}
		}
	}
	
	function cancelContact() {
		$("#contactDiv").popup("close");
	}
	
	function confirmSave() {
		// 提交前验证
		var validate = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'name',
				validateType:['canNull'],
				param:['F'],
				error:['姓名不能为空']
			},{
				type:'text',
				validateObj:'tele',
				validateType:['canNull','mobile'],
				param:['F','F'],
				error:['不能为空','格式不正确']
			}]
		});
		
		if(validate._submitValidate()){
			saveCoupon();
		}
	}
	
	function saveCoupon() {
		var tel = $("#tele").val();
		
		InitLayer();
		var submitData={code:"", tel:tel, action:"setTel", clientID:"${clientID}", openid:"${openid}", pk_group:"${pk_group}"};
		$.post('setTel.do',submitData,function(data){
			if(data.success==true){
				alert("提交成功，谢谢您的参与");
				$("#result").slideToggle(500);
				closeLayer();
				return;
			}else{
				alert("奖品赠送失败，请稍后再试");
				return;
			}
		},"json");
	}
</script>
<style type="text/css">
.header{
	width:100%;
	background-color:#f7f7f7;
	color:#FFFFFF;
	height:200px;
    margin:-29px 0 0 0;
    background-image:url(../image/activity/couponTitle.png);
    background-repeat:no-repeat;
    background-position: center;
    text-align:center;
	font-size:80%;
}
.header img{
	width:20px;
	height:20px;
}
.buttonDiv{
	height:30px;
	width:90%;
	margin-top:10px;
	margin-left:auto;
	margin-right:auto;
}
.buttonStyle{
	height:30px;
	background-color:#FFB400;
	color:#FFFFFF;
	width:100%;
	border:none;
}
</style>
</head>
<body>
<div data-role="page" data-theme="d" id="pageone"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;height:100%; width:100%"><!--页面主体-->
		<div class="header" style="width:100%;height:400px; background-size:100% 100%;margin: 15px auto;">
			<div id="noDiv" style="padding-left:8px;">
				<div style="float:right; padding:145px 20% 0 0px;; list-style-type:none;">
					<span style="color: #000;font-size: 45px;font-weight: bolder;color: black;">聪少15元尝鲜券</span>
				</div>
			</div>
		</div>
		<div class="buttonDiv">
			<input type="button" class="buttonStyle" value="领取优惠券" data-role='none' data-ajax="false" onclick="getCoupon()" />
		</div>
		<div class="buttonDiv">
			<input type="button" class="buttonStyle" value="分享给好友" data-role='none' data-ajax="false" onclick="" /> 
		</div>
	</div>
  	<a href="#contactDiv" id="contact" data-rel="popup" data-position-to="window"></a>
	<div data-role="popup" id="contactDiv" style="position:fixed; top:30%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
		<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr height="60px">
				<td width="40%" align="right" style="padding-right:10px">
					姓名：
				</td>
				<td width="60%">
					<input id="name" style="border:1px solid #D5D5D5;border-radius:4px;height:35px;width:75%;-webkit-appearance: none;" data-role="none"/>
				</td>
			</tr>
			<tr height="60px">
				<td width="40%" align="right" style="padding-right:10px">
					手机号：
				</td>
				<td width="60%">
					<input id="tele" type="tel" value="${card.tele }" style="border:1px solid #D5D5D5;border-radius:4px;height:35px;width:75%;-webkit-appearance: none;" data-role="none"/>
				</td>
			</tr>
			<tr height="65px">
				<td class="popTd" colspan="2">
					<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="取消" data-role='none' data-ajax="false" onclick="cancelContact()" /> 
					<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="确定" data-role='none' data-ajax="false" onclick="confirmSave()" /> 
				</td>
			</tr>
		</table>
	</div> 
  	<div data-role="popup" id="selectCouponDiv" style="position:fixed; top:0px; left:0px; width:100%; height:100%;" 
  		data-dismissible="false" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false">
		<%-- <c:forEach items="${listCoupon }" var="coupon" varStatus="status">
			<div id="${coupon. code}" class="cashCoupDiv" 
				onclick="addCoupon('${coupon. code}','${coupon.zkmoney}','${coupon.paycode}','${coupon.payname}')"
			 	style="background:url(<%=path%>/image/wechat/${coupon.pic })  no-repeat right; background-size: 100% 100%;">
				<div style="float: left;width: 25%;color: ${coupon.fontcolor};"><h1>立减${coupon.zkmoney}元</h1></div>
				<div style="float: left; margin-top: 10px;color: white;">
					<span style="font-size: 30px;">${coupon. name}</span><br>
					<span style="font-size: 15px;">有效期至：${coupon.yxdate }</span>
				</div>
			</div>
		</c:forEach> --%>
		<div class="header" style="text-shadow:none">
			<span>您有以下电子券可以使用</span>
		</div>
		<div id="listCouponDiv" style="height:80%">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<c:set var="listCouponSize" value="${fn:length(listCoupon)}"/>
				<c:forEach items="${listCoupon }" var="coupon" varStatus="status">
					<tr height="85px">
						<td>
							<div id="${coupon.code}" class="cashCoupDiv" onclick="selectCoupon(this);" pushval="${coupon.typId}_${coupon.amt}_${coupon.actmCode}_${coupon.code}"
							 	style="background:url(<%=path%>/image/wechat/${coupon.pic })  no-repeat right; background-size: 100% 100%;">
								<div style="float: left;width: 25%;color: ${coupon.fontColor};"><h1>立减${coupon.amt}元</h1></div>
								<div style="float: left; margin-top: 10px;color: white;">
									<span style="font-size: 130%;">${coupon.typdes}</span><br>
									<span style="font-size: 90%;">有效期至：${coupon.edate }</span>
								</div>
								<div name="selectCoupon" style="padding-top:18px; display:none" class="selectImgDiv">
									<img id="wxPay" style="width:50px; height:50px; text-align:right; vertical-align:middle;" src="<c:url value='/image/wechat/right.png'/>"/>
								</div>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div style="position:fixed; bottom:10px; width:100%">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="popTd">
						<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="不使用电子券" data-role='none' data-ajax="false" onclick="javaScript:cancelSelect();" /> 
						<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="确认选择" data-role='none' data-ajax="false" onclick="javaScript:confirmSelect();" /> 
					</td>
				</tr>
			</table>
		</div>
	</div> 
</div><!-- page -->
</body>
</html>