<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path = request.getContextPath();
	String port ="80".equals((request.getServerPort()+""))?"":(":"+request.getServerPort()+"");
	String basePath = request.getScheme()+"://"+request.getServerName()+port+path;
%>
<!DOCTYPE html >
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>账单确认</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/orderDetail.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/validate.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script type="text/JavaScript">

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
	$("#payDiv").click(function(){
		InitLayer();
		var url = "<%=basePath %>/wxPay/toOrderPay.do?pk_group=${pk_group}&firmid=${orderDetail.firmid}&resv=${orderDetail.resv}&sumprice=${paymoney}&pax=${orderDetail.pax}" +
			"&id=${orderDetail.id}&response_type=code&scope=snsapi_base&state=123&code=${code}&billstate=6&outTradeNo=${orderDetail.outTradeNo}";
//			 		alert(url);
		window.setTimeout(function(){
			location.href = url;
		},100);
	});
});
</script>
<style type="text/css">
	.detail{
		padding-top: 5px;
		padding-bottom: 0px;
	}
	.numClass{
		font-family: Arial;
	}
</style>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
		<c:set var="tuicaiListSize" value="${fn:length(tuicaiList)}"/>
		<c:set var="zhekouListSize" value="${fn:length(zhekouList)}"/>
		<c:set var="otherInfoSize" value="${fn:length(otherInfo)}"/>
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div id="headDiv" class="header scanToOrders">
			<img src="<c:url value='/image/wechat/laba.png'/>" /><span>请查看账单明细及优惠信息，确认无误后支付。</span>
		</div>
		<div class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="table-layout:fixed;">
				<tr class="topRadius">
					<td class="detail" colspan="2" style="padding-bottom:0px;">
						<div style="float:left;width:80%;font-size:110%;font-weight: bolder;margin-top: 6px;">
							${orderDetail.firmdes}
						</div>
						<div onclick="alertMsg('${orderDetail.addr}');" style="float:left;width:10%;">
							<img src="<c:url value='/image/wechat/local.png'/>"/>
						</div>
						<c:if test="${orderDetail.tele ne null && orderDetail.tele ne '' }">
							<div style="float:left;width:10%;">
								<a href="tel:${orderDetail.tele }"><img style="width:30px;height:30px;" src="<c:url value='/image/wechat/dianhua.png'/>"/></a>
							</div>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="detail" style="padding-top: 5px;padding-bottom: 0px;"><span style="font-family: Arial;color:#656565;font-size: 100%;">${orderDetail.orderTimes}</span></td>
					<td class="detail" style="width:30%;text-align: right;vertical-align: bottom;padding-top: 5px;padding-bottom: 0px;" rowspan="2"><span style="font-size:12px;">账单金额￥</span><span style="font-family: Arial;font-size: 180%;color: red;">${totalPrice }</span></td>
				</tr>
				<tr>
					<td class="detail" style="width:70%;padding-top: 5px;padding-bottom: 0px;">就餐桌位:&nbsp;<span style="color: red;font-size: 150%;font-family: Arial;">${orderDetail.tablesname}</span></td>
				</tr>
				<tr>
					<td class="detail1" colspan="2" style="font-size: 14px;">
						<div style="height:8px;"></div>
						<div>订单编号:&nbsp;<span style="color:#656565;font-size: 100%;font-family: Arial;">${orderDetail.resv}</span></div>
						<div style="margin-top: 2px;">订单类型:&nbsp;堂食</div>
						<div>就餐人数:&nbsp;<span style="vertical-align: bottom;font-family: Arial;font-size: 100%;color:#656565;">${orderDetail.pax}</span></div>
						<div style="margin-top: 2px;">备注:&nbsp;${orderDetail.remark}</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="spaceMeaage" style="padding-left: 10px;">
			我的菜单，共<span style="font-family: Arial;">${orderDtlSize }</span>条点菜记录
		</div>
		<div class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="background-color:#FFFFFF">
				<c:forEach items="${orderDtl }" var="food" varStatus="stat">
					<tr>
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;font-size:13px;font-family: Arial;"width="10%" >${stat.count }</td>
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align:left;"width="60%" >
							<div><span style='width:100%; text-align:left; font-size:14px;'>${food.foodsname }</span></div>
							<div><span style='width:100%; color:gray; text-align:left; font-size:12px;font-family: Arial;'>￥${food.price }</span></div>
							<c:if test="${fn:length(food.listDishAddItem) > 0.0 }">
								<div><span style='width:100%; color:gray; text-align:left; font-size:12px;'>附加项:
									<c:forEach items="${food.listDishAddItem }" var="addItem">
										${addItem.redefineName }
										<c:if test="${addItem.nprice > 0.0 }">
											<span style="color:red">(${addItem.nprice }元)</span>
										</c:if>
										&nbsp;
									</c:forEach>
								</span></div>
							</c:if>
							<c:if test="${fn:length(food.listDishProdAdd) > 0 }">
								<div><span style='width:100%; color:gray; text-align:left; font-size:12px;'>附加产品:
									<c:forEach items="${food.listDishProdAdd }" var="addItem">
										${addItem.prodAddName }
										<c:if test="${addItem.nprice > 0.0 }">
											<span style="color:red">(${addItem.nprice }元)</span>
										</c:if>
										&nbsp;
									</c:forEach>
								</span></div>
							</c:if>
							<c:if test="${food.remark ne null && food.remark ne '' }">
								<div><span style='width:100%; color:gray; text-align:left; font-size:12px;'>备注:${food.remark }</span></div>
							</c:if>
						</td>
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;"width="10%">
							<span style="color:#ffb400; font-size:14px;font-family: Arial;">×${food.foodnum }</span>
						</td>
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align: right;"width="20%">
							<span style="color:#ffb400; font-size:14px;font-family: Arial;">￥${food.totalprice }</span>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div class="spaceMeaage" style="padding-left: 10px;">
			退菜信息
		</div>
		<c:choose>
			<c:when test="${tuicaiListSize<=0 }">
					<div class="order">
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="background-color:#FFFFFF">
						<tr height="30px">
							<td class="detail">未查询到数据</td>
						</tr>
					</table>
					</div>
			</c:when>
			<c:otherwise>
				<div class="order">
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="background-color:#FFFFFF">
						<c:forEach items="${tuicaiList }" var="tuicai" varStatus="stat">
							<tr height="30px">
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;font-size:13px;font-family: Arial;"width="10%" >${stat.count }</td>
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align:left;"width="60%" >
									<div><span style='width:100%; text-align:left; font-size:14px;'>${tuicai.vpname }</span></div>
									<div><span style='width:100%; color:gray; text-align:left; font-size:12px;' class="numClass">￥${tuicai.price }</span></div>
								</td>
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;"width="10%">
									<span style="color:#ffb400; font-size:14px;font-family: Arial;">×${tuicai.nzcnt }</span>
								</td>
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align: right;"width="20%">
									<span style="color:#ffb400; font-size:14px;font-family: Arial;">￥${tuicai.nzmoney }</span>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</c:otherwise>
		</c:choose>
		<div class="spaceMeaage" style="padding-left: 10px;">
			优惠信息
		</div>
		<c:choose>
			<c:when test="${zhekouListSize<=0 }">
					<div class="order">
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="background-color:#FFFFFF">
						<tr height="30px">
							<td class="detail">未查询到数据</td>
						</tr>
					</table>
					</div>
			</c:when>
			<c:otherwise>
				<div class="order">
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="background-color:#FFFFFF">
						<c:forEach items="${zhekouList }" var="zhekou" varStatus="stat">
							<tr>
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;font-size:13px;font-family: Arial;"width="10%" >${stat.count }</td>
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align:left;" width="60%">
									<div><span style='width:100%; text-align:left; font-size:14px;'>${zhekou.vsettlementname }</span></div>
								</td>
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;"width="10%">
									<span style="color:#ffb400; font-size:14px;font-family: Arial;">×${zhekou.nsettlementcount }</span>
								</td>
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align: right;"width="20%">
									<span style="color:#ffb400; font-size:14px;font-family: Arial;">￥${zhekou.nrefundamt }</span>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</c:otherwise>
		</c:choose>
		<div class="spaceMeaage" style="padding-left: 10px;">
			其他信息
		</div>
		<c:choose>
			<c:when test="${otherInfoSize<=0 && empty groupInfo}">
					<div class="order">
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="background-color:#FFFFFF">
						<tr height="30px">
							<td class="detail">未查询到数据</td>
						</tr>
					</table>
					</div>
			</c:when>
			<c:otherwise>
				<div class="order">
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="background-color:#FFFFFF">
						<c:forEach items="${otherInfo }" var="otherinf" varStatus="stat">
							<tr>
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;font-size:13px;font-family: Arial;"width="10%" >${stat.count }</td>
								<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align:left;" width="90%">
									<div><span style='width:100%; text-align:left; font-size:14px;'>${otherinf.vcouponname } ${otherinf.verrmsg }</span></div>
								</td>
							</tr>
						</c:forEach>
						<c:forEach items="${groupInfo }" var="groupinf" varStatus="stat">
						<tr>
							<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;font-size:13px;font-family: Arial;"width="10%" >${stat.count }</td>
							<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align:left;" width="90%">
								<div style="word-wrap: break-word; word-break: normal">
									<span style='width:100%; text-align:left; font-size:14px;'>
										${groupinf.couponname } ${groupinf.nderatenum }元券&nbsp;&nbsp;
										<c:choose>
											<c:when test=" ${groupinf.state eq 'Y'}">可用</c:when>
											<c:otherwise>不可用</c:otherwise>
										</c:choose>
									</span>
								</div>
							</td>
						</tr>
						</c:forEach>
					</table>
				</div>
			</c:otherwise>
		</c:choose>
		<div class="orderSpace">
			&nbsp;
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-role="popup" data-position="fixed" data-fullscreen="true" data-tap-toggle="false" data-history="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a" style="width:25%;">
					<a href="<c:url value='/bookDesk/findMenuOrders.do?pk_group=${pk_group}&openid=${openid }' />" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
				<td style="background-color: black;color: white;width:50%;text-align: right;">
					应付：<span class="numClass" style="color: red;font-size: 160%;">${paymoney}&nbsp;&nbsp;&nbsp;</span>
				</td>
				<td style="background-color: #F39801;color: white;width:25%;text-align: center;" id="payDiv">
					支付
				</td>
			</tr>
		</table>
  	</div>
</div><!-- page -->
</body>
</html>