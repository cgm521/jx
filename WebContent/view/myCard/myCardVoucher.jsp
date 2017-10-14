<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>电子券</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/myCardVoucher.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
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
	});
	
	function loadVoucher(obj, divId){
		hideAll();
		$("#" + divId).show();
		
		$(".selectType").attr("class","noselectType");
		$(obj).attr("class","selectType");
	}
	
	function hideAll(){
		$("#noUse").hide();
		$("#haveUsed").hide();
		$("#haveExpired").hide();
	}
	
	/** 展示电子券使用详情 */
	function showDetail(obj) {
		$(obj).parent().next().toggle();
	}
	
	/** 跳转到电子券详情页面 */
	function showVoucherDetail(id) {
		window.setTimeout(function(){
			InitLayer();
			var url = "<c:url value='/myCard/voucherDetail.do?id=' />" + id +"&openid=${openid}";
			window.setTimeout(function(){
				location.href = url;
			},100);
		},500);
	}
</script>
<style type="text/css">
	.tdSmall span{
		font-family: Arial;
	}
	.cashCoupDiv{
		background-color:white;
		width:94%;
		height:120px;
		vertical-align:middle;
		margin:10px 3% 0 3%;
	}
	h1 {
	    width: 1em;
	    margin-left: 15%;
	    font-size: 13px;
	  }
	.lmoney{
		font-size:150%;
		color:#FFFFFF;
	}
	.lunit{
		font-size:100%;
		color:#FFFFFF;
	}
	.ldiv{
		float:left;
		width:20%;
		margin-top:30px;
		margin-left:10px;
		color:#FFFFFF;
	}
	.rdiv{
		float:left;
		margin-top:10px; 
		color:#FFFFFF;
	}
	.rdiv span{
		font-size:90%;
	}
	.detailInfo{
		padding-top:25px; 
		margin-left:10px; 
		color:#FFFFFF;
	}
</style>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="header" style="border:0;">
    	<div class="tabContent">
      		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="selectType"  onclick="javaScript:loadVoucher(this,'noUse');">未使用</td>
					<td class="noselectType" onclick="javaScript:loadVoucher(this,'haveUsed');">已使用</td>
					<td class="noselectType" onclick="javaScript:loadVoucher(this,'haveExpired');">已过期</td>
				</tr>
			</table>
    	</div>
  	</div>
  	
	<div data-role="content" style="margin-top:0px; padding-top:0px; background-color:#F7F7F7;"><!--页面主体-->
		<input type="hidden" id="pk_group" value="${pk_group}">
		<input type="hidden" id="openid" value="${openid}">
		<div id="noUse">
			<c:choose>
				<c:when test="${fn:length(noUseVoucher) <= 0}">
					<div class="noData">未查询到电子券</div>
				</c:when>
				<c:otherwise>
					<c:forEach items="${noUseVoucher }" var="voucher">
						<div id="${coupon.pk_id}" class="cashCoupDiv" onclick="showVoucherDetail('${voucher.id }')"
						 	style="background:url(<%=path%>/image/wechat/coupon_limit10.png) no-repeat center; background-size: 100% 100%;">
							<div class="ldiv">
								<c:choose>
									<c:when test="${voucher.istyle eq 1 }">
										<span class="lmoney">${voucher.amt}</span><span class="lunit">元</span>
									</c:when>
									<c:otherwise>
										<span class="lmoney">${voucher.discrate}</span><span class="lunit">折</span>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="rdiv">
								<span>${voucher.typdes }</span><br>
								<span>电子券号：</span><span style="font-family: Arial;">${voucher.code}</span><br>
								<span>使用期限：</span><span style="font-family: Arial;">${voucher.bdate}/${voucher.edate}</span>
							</div>
							<div style="clear:left"></div>
							<div class="detailInfo" onclick="showDetail(this); event.cancelBubble=true;">使用详情</div>
						</div>
						<div style="display:none; background-color:#C9C9C9; width:94%; margin:auto;">
							<c:choose>
								<c:when test="${voucher.rest ne null && voucher.rest ne '' }">
									<span class="detailSpan">适用门店：${voucher.rest }</span>
								</c:when>
								<c:otherwise>
									<span class="detailSpan">适用门店：所有</span>
								</c:otherwise>
							</c:choose>
							<br>
							<span class="detailSpan">使用说明：${voucher.memo }</span>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
		<div id="haveUsed" style="display:none">
			<c:choose>
				<c:when test="${fn:length(haveUsedVoucher) <= 0}">
					<div class="noData">未查询到电子券</div>
				</c:when>
				<c:otherwise>
					<c:forEach items="${haveUsedVoucher }" var="voucher">
						<div id="${coupon.pk_id}" class="cashCoupDiv" 
						 	style="background:url(<%=path%>/image/wechat/coupon_haveUsed.png) no-repeat center; background-size: 100% 100%;">
							<div class="ldiv" >
								<c:choose>
									<c:when test="${voucher.istyle eq 1 }">
										<span  style="color:#ddb9b1"class="lmoney">${voucher.amt}</span><span style="color:#ddb9b1"class="lunit">元</span>
									</c:when>
									<c:otherwise>
										<span style="color:#ddb9b1"class="lmoney">${voucher.discrate}</span><span style="color:#ddb9b1"class="lunit">折</span>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="rdiv" style="color:#ddb9b1">
								<span>${voucher.typdes }</span><br>
								<span>电子券号：</span><span style="font-family: Arial;">${voucher.code}</span><br>
								<span>使用期限：</span><span style="font-family: Arial;">${voucher.bdate}/${voucher.edate}</span>
							</div>
							<div style="clear:left"></div>
							<div class="detailInfo"style="color:#ddb9b1" onclick="showDetail(this)">使用详情</div>
						</div>
						<div style="display:none; background-color:#C9C9C9; width:94%; margin:auto;">
							<c:choose>
								<c:when test="${voucher.rest ne null && voucher.rest ne '' }">
									<span class="detailSpan">适用门店：${voucher.rest }</span>
								</c:when>
								<c:otherwise>
									<span class="detailSpan">适用门店：所有</span>
								</c:otherwise>
							</c:choose>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
		<div id="haveExpired" style="display:none">
			<c:choose>
				<c:when test="${fn:length(haveExpiredVoucher) <= 0}">
					<div class="noData">未查询到电子券</div>
				</c:when>
				<c:otherwise>
					<c:forEach items="${haveExpiredVoucher }" var="voucher">
						<div id="${coupon.pk_id}" class="cashCoupDiv" 
						 	style="background:url(<%=path%>/image/wechat/coupon_haveExpired.png) no-repeat center; background-size: 100% 100%;">
							<div class="ldiv">
								<c:choose>
									<c:when test="${voucher.istyle eq 1 }">
										<span class="lmoney">${voucher.amt}</span><span class="lunit">元</span>
									</c:when>
									<c:otherwise>
										<span class="lmoney">${voucher.discrate}</span><span class="lunit">折</span>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="rdiv">
								<span>${voucher.typdes }</span><br>
								<span>电子券号：</span><span style="font-family: Arial;">${voucher.code}</span><br>
								<span>使用期限：</span><span style="font-family: Arial;">${voucher.bdate}/${voucher.edate}</span>
							</div>
							<div style="clear:left"></div>
							<div class="detailInfo" onclick="showDetail(this)">使用详情</div>
						</div>
						<div style="display:none; background-color:#C9C9C9; width:94%; margin:auto;">
							<c:choose>
								<c:when test="${voucher.rest ne null && voucher.rest ne '' }">
									<span class="detailSpan">适用门店：${voucher.rest }</span>
								</c:when>
								<c:otherwise>
									<span class="detailSpan">适用门店：所有</span>
								</c:otherwise>
							</c:choose>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="ui-a">
					<a href="" >
						<img src="<c:url value='/image/wechat/jixianghuiyuanhuodong.png'/>"/>&nbsp;
					</a>
				</td>
			</tr>
		</table>
  	</div>
</div>
	
</body>
</html>