<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="format-detection" content="telephone=no" />
<title>我的订位单</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/orderDetail.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script language="JavaScript" type="text/JavaScript">
/*去除安卓返回及刷新按钮
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
*/
function cancelOrders(pk_group,ordersid){
	$("#cancelDiv").popup("close");
	InitLayer();
	var url = "<c:url value='/bookDesk/cancelOrder.do?pk_group=' />"+pk_group+"&orderid="+ordersid;
	window.setTimeout(function(){
		location.href = url;
	},100);
}

function showConfirm(){
	$('#cancelLink').click();
}

function noCancel(){
	$("#cancelDiv").popup("close");
}
</script>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div class="header">
			<img src="<c:url value='/image/wechat/laba.png'/>" /><span>请您务必于预定时间前到达本店并使用手机进行签到</span>
		</div>
		<div class="firm">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="topRadius">
					<td class="detail"><img src="<c:url value='/image/wechat/home1.png'/>"/>&nbsp;${orderDetail.firmdes}</td>
				</tr>
				<tr class="bottomRadius">
					<td class="detail1">
						<div style="float:left;width:90%;"><img src="<c:url value='/image/wechat/local.png'/>"/>&nbsp;${orderDetail.addr}</div>
						<div style="float:right;width:10%;"><a href="tel:${orderDetail.tele}"><img style="width:30px;height:30px;" src="<c:url value='/image/wechat/dianhua.png'/>"/></a></div>
					</td>
				</tr>
			</table>
		</div>
		<div class="spaceMeaage">
			订单信息
		</div>
		<div class="orderStateImg">
			<c:choose>
				<c:when test="${orderDetail.state eq '1' or orderDetail.state eq '2'}">
					<img src="<c:url value='/image/wechat/yishouli.png'/>"/>
				</c:when>
				<c:when test="${orderDetail.state eq '3'}">
					<img src="<c:url value='/image/wechat/yiquxiao.png'/>"/>
				</c:when>
				<c:otherwise>
					<img src="<c:url value='/image/wechat/yiwancheng.png'/>"/>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="topRadius">
					<td class="detail">
						订单编号:&nbsp;&nbsp;${orderDetail.resv}
					</td>
				</tr>
				<tr>
					<td class="detail1">
						订单类型:&nbsp;&nbsp;预定/${orderDetail.roomtype}/${orderDetail.dat}/
						<c:choose>
							<c:when test="${orderDetail.sft eq '0'}">全天</c:when>
							<c:when test="${orderDetail.sft eq '1'}">午市</c:when>
							<c:when test="${orderDetail.sft eq '2'}">晚市</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="detail1">下单时间:&nbsp;&nbsp;${orderDetail.orderTimes}</td>
				</tr>
				<tr>
					<td class="detail1">预定人数:&nbsp;&nbsp;${orderDetail.pax} 人</td>
				</tr>
				<tr>
					<td class="detail1">订单金额:&nbsp;&nbsp;<span>￥ 0.00</span></td>
				</tr>
				<tr class="bottomRadius">
					<td class="detail2">
						<c:if test="${orderDetail.state lt '2'}">
						<p>
							<input type="button" value="取消订单" data-role='none' data-ajax="false" onclick="javaScript:showConfirm()">
							<a href="#cancelDiv" id="cancelLink" data-rel="popup" data-position-to="window"></a>
							<div data-role="popup" id="cancelDiv" style="position:fixed; top:30%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
								<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
									<tr height="50px">
										<td align="center">确定取消此订单吗？</td>
									</tr>
									<tr height="50px">
										<td class="popTd">
											<input type="button" value="取消" data-role='none' data-ajax="false" onclick="noCancel()" /> 
											<input type="button" value="确认" data-role='none' data-ajax="false" onclick="javaScript:cancelOrders('${pk_group}','${orderDetail.id}');" /> 
										</td>
									</tr>
								</table>
							</div> 
						</p>
						</c:if>
					</td>
				</tr>
			</table>
		</div>
		<div class="spaceMeaage">
			联系方式
		</div>
		<div class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="topRadius">
					<td class="detail">联系姓名:&nbsp;&nbsp;${orderDetail.name}</td>
				</tr>
				<tr class="bottomRadius">
					<td class="detail1">联系电话:&nbsp;&nbsp;${orderDetail.contact}</td>
				</tr>
			</table>
		</div>
		<div class="orderSpace">
			&nbsp;
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false"><!--  data-role="footer" data-position="inline" data-fullscreen="true" class="footer" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a"><a href="<c:url value='/bookDesk/findOrders.do?pk_group=${pk_group}' />" data-ajax="false"><img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;</a></td><!-- <img src="<c:url value='/image/wechat/back.png'/>"/> -->
			</tr>
		</table>
  	</div>
</div><!-- page -->
</body>
</html>