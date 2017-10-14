<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>充值记录</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/default/global.css" />' rel="stylesheet" />
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
	 	/* if(ua.match(/MicroMessenger/i)!="micromessenger") {
	 		$("body").text("请使用微信浏览器打开");
	 		return;
	 	} */
	});
	
	function changeDate(dateStr) {
		$("#overlay").popup("close");
		if(dateStr == $("#chargeDate").val()){
			return;
		}
		InitLayer();
		window.setTimeout(function(){
			window.location.href = "<%=path %>/myCard/listChargeRecord.do?cardNo=${cardNo }&openid=${openid }&pk_group=${pk_group }&chargeDate=" + dateStr;
		},100);
	}
</script>

<style type="text/css">
</style>

</head>
<body>
<div data-role="page" data-theme="d" class="page"><!--页面层容器-->
	<div data-role="header" style="border:0;">
    	<div class="account" style="padding-bottom:0px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr style="background-color:#FFB400; height:60px;">
					<td width="35%" class="headTxt">
						<a href="#overlay" data-rel="popup" id="sft_a" class="headTxt" style="border-right:0px;font-family:Arial;">${headDate }
						<img src="<%=path %>/image/wechat/drop_arrow.png" width="30px" height="30px" style="vertical-align:middle" /></a>
						<div id="overlay" data-role="popup" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
							<ul data-role="listview" data-inset="false">
								<li data-role="list-divider" style="text-align:center;">选择日期</li>
							</ul>
							<div id="sftList" style="font-family:Arial;">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
								<c:catch var="error_Message">
									<c:forEach items="${listDate}" var="date">
										<tr>
											<td class="td-left-lev" onClick="changeDate('${date}');">
												<span>${date}</span>
											</td>
										</tr>
									<c:set var="numDate" value="${numDate+1}"/>
									</c:forEach>
								</c:catch>
			    				</table>
							</div>
					</div>
					</td>
					<td width="65%" class="headTxt">充值总额</br>${totalAmt }</td>
				</tr>
			</table>
		</div>
  	</div>
  	
	<div data-role="content" style="margin-top:0px; padding-top:0px; background-color:#F7F7F7;"><!--页面主体-->
		<input type="hidden" id="pk_group" value="${pk_group}">
		<input type="hidden" id="openid" value="${openid}">
		<input type="hidden" id="chargeDate" value="${chargeDate}">
		<div id="chargeRecord">
			<c:choose>
				<c:when test="${fn:length(listChargeRecord) <= 0}">
					<div class="noData">未查询到充值记录</div>
				</c:when>
				<c:otherwise>
					<c:forEach items="${listChargeRecord }" var="record">
						<c:if test="${record.rmbamt != 0.0 }">
						<div class="dataDiv"  data-ajax="false">
							<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
								<tr height="30px">
									<td style="font-size:120%;text-align: left;" colspan="3">
										&nbsp;&nbsp;&nbsp;${record.firmdes}
									</td>
								</tr>
								<tr height="30px">
									<td align="center" style="width:70%; font-size:100%;font-family:Arial;text-align: left;" colspan="2">
										&nbsp;&nbsp;&nbsp;${record.tim}
									</td>
									<c:if test="${fn:indexOf(record.rmbamt, '-') >= 0 }">
										<td align="center" style="width:30%; font-size:120%; color:red;font-family:Arial;text-align: right;">
											${record.rmbamt }&nbsp;&nbsp;&nbsp;
										</td>
									</c:if>
									<c:if test="${fn:indexOf(record.rmbamt, '-') < 0 }">
										<td align="center" style="width:30%; font-size:120%; color:green;font-family:Arial;text-align: right;">
											${record.rmbamt }&nbsp;&nbsp;&nbsp;
										</td>
									</c:if>
								</tr>
								<tr>
									<td class="tdSmall" colspan="3" style="text-align: left;">
										${record.payment}
									</td>
								</tr>
							</table>
						</div>
						</c:if>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="ui-a">
					<a href="<c:url value='/myCard/cardInfo.do?pk_group=${pk_group}&openid=${openid }' />" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
			</tr>
		</table>
  	</div>
</div>
</body>
</html>