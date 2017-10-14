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
});
function cancelOrders(pk_group,ordersid){
	$("#cancelDiv").popup("close");
	InitLayer();
	var url = "<c:url value='/bookDesk/cancelOrder.do?pk_group=' />"+pk_group+"&orderid="+ordersid+"&state="+$("#orderStateNow").val();
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
function gotoMeal(){
	
	var url = "<c:url value='/bookMeal/orderDetail.do?openid=${orderDetail.openid}&firmid=${orderDetail.firmid}"+
			"&orderid=${orderDetail.bookDeskOrderID}&bookDeskOrderID=${orderDetail.id}&orderType=0&pageFrom=orderDetail&deskOrderState=' />"+$("#orderStateNow").val();
	InitLayer();
	window.setTimeout(function(){
		location.href = url;
	},100);
}
function xiadan(){
	confirmMsg('订单确认后不能修改，如需修改联系门店或者取消订单重新预订',function(){
		InitLayer();
		$.ajax({
			url:"<c:url value='/dining/pushToPOS.do'/>",
			type:"POST",
			dataType:"html",
			data:{"orderid":"${orderDetail.bookDeskOrderID}","deskOrderid":"${orderDetail.id}"},
			success:function(html){
				if(html == "1"){
					$("#xiandanButton").hide();
					$("#orderStateNow").val("1");
					//$(".orderStateImg").css("margin-top","0");
					$(".orderStateImg").find("img").attr("src", "<c:url value='/image/wechat/yishouli.png'/>"); ;
				}else{
					alertMsg("下单失败");
				}
				closeLayer();
			},
			error:function(ajax){
				closeLayer();
			}
		});
	});
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
					<td class="detail" colspan="2">
						<input type="hidden" id="orderStateNow" value="${orderDetail.state}"/>
						<div style="float:left;width:80%;font-size:110%;font-weight: bolder;margin-top: 6px;">
							${orderDetail.firmdes}
						</div>
						<c:if test="${orderDetail.addr ne null && orderDetail.addr ne '' }">
							<div onclick="alertMsg('${orderDetail.addr}');" style="float:left;width:10%;">
								<img src="<c:url value='/image/wechat/local.png'/>"/>
							</div>
						</c:if>
						<c:if test="${orderDetail.tele ne null && orderDetail.tele ne '' }">
							<div style="float:left;width:10%;">
								<a href="tel:${orderDetail.tele }"><img style="width:30px;height:30px;" src="<c:url value='/image/wechat/dianhua.png'/>"/></a>
							</div>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="detail" colspan="2" style="padding-top: 5px;padding-bottom: 0px;"><span style="font-family: Arial;color:#656565;font-size: 100%;">${orderDetail.orderTimes}</span></td>
				</tr>
				<tr>
					<td class="detail" style="width:40%;padding-top: 5px;padding-bottom: 0px;">
						预订桌位:&nbsp;<span style="color: red;font-size: 150%;font-family: Arial;">${orderDetail.tablesname}</span>
					</td>
					
						<c:choose>
								<c:when test="${not empty orderDetail.money && orderDetail.money ne '0.00'}">
									<td class="detail" style="width:50%;text-align: right;vertical-align: bottom;padding-top: 5px;padding-bottom: 0px;" onclick="gotoMeal();">
										<span style="font-size:12px;">金额￥</span><span style="font-family: Arial;font-size: 180%;color: red;">
										${orderDetail.money}
										</span></br>
										<span style="font-size:12px;color:#656565;">点击金额查看点菜单</span>
									</td>
								</c:when>
								<c:otherwise>
									<td class="detail" style="width:50%;text-align: right;vertical-align: bottom;padding-top: 5px;padding-bottom: 0px;">
										<span style="font-size:12px;">金额￥</span><span style="font-family: Arial;font-size: 180%;color: red;">
										0.00
										</span>
									</td>
								</c:otherwise>
						</c:choose>
				</tr>
			</table>
		</div>
		<div class="orderStateImg" <c:if test="${orderDetail.state >= '2' && orderDetail.state ne '7'}">style="margin-top:0px;"</c:if>>
			<c:choose>
				<c:when test="${orderDetail.state eq '0'}">
					<img src="<c:url value='/image/wechat/weitijiao.png'/>"/>
				</c:when>
				<c:when test="${orderDetail.state eq '1' or orderDetail.state eq '2'}">
					<img src="<c:url value='/image/wechat/yishouli.png'/>"/>
				</c:when>
				<c:when test="${orderDetail.state eq '3'}">
					<img src="<c:url value='/image/wechat/yiquxiao.png'/>"/>
				</c:when>
				<c:when test="${orderDetail.state == 7 }">
					<img src="<c:url value='/image/wechat/yixiadan.png'/>"/>
				</c:when>
				<c:otherwise>
					<img src="<c:url value='/image/wechat/yiwancheng.png'/>"/>
				</c:otherwise>
			</c:choose>
		</div>
		<c:if test="${orderDetail.state lt '2' || orderDetail.state eq '7'}">
		<div id="allbutton" class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="bottomRadius">
					<td class="detail2">
						<p>
							<input type="button" style="width:40%;" value="取消订单" data-role='none' data-ajax="false" onclick="javaScript:showConfirm()">
							<c:if test="${orderDetail.state eq '0'}">
								<input type="button" id="xiandanButton" style="width:40%;" value="确认预订" data-role='none' data-ajax="false" onclick="javaScript:xiadan()">
							</c:if>
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
					</td>
				</tr>
			</table>
		</div>
		</c:if>
		<div class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="detail" colspan="2" style="font-size: 14px;">
						<div style="height:8px;"></div>
						<div>订单编号:&nbsp;<span style="color:#656565;font-size: 100%;font-family: Arial;">${orderDetail.resv}</span></div>
						<div style="margin-top: 2px;">订单类型:&nbsp;<span style="vertical-align: bottom;font-family: Arial;font-size: 100%;color:#656565;">预定/${orderDetail.pax}人${orderDetail.roomtype}/<c:choose><c:when test="${empty orderDetail.arrtime}">${orderDetail.dat}</c:when><c:otherwise>${orderDetail.arrtime}</c:otherwise></c:choose>&nbsp;${orderDetail.datmins}/${sfts[orderDetail.sft]}</span></div>
						<div>联系姓名:&nbsp;<span style="vertical-align: bottom;font-family: Arial;font-size: 100%;color:#656565;">${orderDetail.name}</span></div>
						<div style="margin-top: 2px;">联系电话:&nbsp;<span style="vertical-align: bottom;font-family: Arial;font-size: 100%;color:#656565;">${orderDetail.contact}</span></div>
					</td>
				</tr>
			</table>
		</div>
		<div class="orderSpace">
			&nbsp;
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-tap-toggle="false"><!--  data-role="footer" data-position="inline" data-fullscreen="true" class="footer" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a"><a href="<c:url value='/bookDesk/findOrders.do?pk_group=${pk_group}&openid=${orderDetail.openid}' />" data-ajax="false"><img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;</a></td><!-- <img src="<c:url value='/image/wechat/back.png'/>"/> -->
			</tr>
		</table>
  	</div>
</div><!-- page -->
</body>
</html>