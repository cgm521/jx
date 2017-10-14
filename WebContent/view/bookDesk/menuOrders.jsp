<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path = request.getContextPath();
	String port ="80".equals((request.getServerPort()+""))?"":(":"+request.getServerPort()+"");
	String basePath = request.getScheme()+"://"+request.getServerName()+port+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<title>我的点菜单</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/orders.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<style>
	.evaState{
		background-color:#FFB400;
		color:#FFFFFF;
		padding:5px;
		border-radius:5px;
		font-size:90%;
	}
</style>
<script language="JavaScript" type="text/JavaScript">
/*去除安卓返回及刷新按钮*/
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});
var leftDelFlag = false; //是否处于删除状态 true-是 false-否
$(function(){
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)!="micromessenger") {
// 		$("body").text("请使用微信浏览器打开");
// 		return;
	}
});
$(document).on("pageinit","#pageone",function(){
	$("#complete").hide();
});
function loadOrders(selectType,orderState){
	if($(selectType).hasClass("selectType")){
		return;
	}
	var nowObj = null;
	if($("#complete").is(":hidden")){
		$("#complete").show();
		$("#blankDiv").hide();
		$("#helf").hide();
		nowObj = $("#complete");
	}else{
		$("#helf").show();
		$("#complete").hide();
		$("#blankDiv").show();
		nowObj = $("#helf");
	}
	
	$(".selectType").attr("class","noselectType");
	$(selectType).attr("class","selectType");
	
	var isLoadhelf = $("#isLoadhelf").val();
	var isLoadcomp = $("#isLoadcomp").val();
	if(isLoadhelf=='0' || isLoadcomp=='0'){
		InitLayer();
		
		$("#isLoadhelf").val(1);
		$("#isLoadcomp").val(1);
		
		var pk_group = $("#pk_group").val();
		var openid = $("#openid").val();
		
		var htmlStr = "";
		var sftname = "";
		var stateValue = "";
		$.ajax({
			url:"<c:url value='/bookDesk/getOrders.do' />",
			type:"POST",
			dataType:"json",
			data:{"pk_group":pk_group,"openid":openid,"orderType":"0","orderState":orderState},
			success:function(json){
				
				if(json.length<1){
					htmlStr="<div class='noOrder'>未查询到订单</div>";
					nowObj.html(htmlStr);//填充新内容
					closeLayer();
					return false;
				}
				$.each(json,function(i,item){
					if(item.sft=="0"){
						sftname = "全天";
					}else if(item.sft=="1"){
						sftname = "午市";
					}else{
						sftname = "晚市";
					}
					
					if(item.state=="3"){
						stateValue = "<span class='redState'>已取消</span>";
					}else{
						stateValue = "<span class='greenState'>已完成</span>";
					}
					htmlStr = htmlStr
// 								+ "<div class='oneOrder'  style=\"border:1px solid #ddd;float:left;\"  id=\""+item.id+"\">"
								+ "<div class='oneOrder'  id=\""+item.id+"\" onclick=\"javaScript:orderDetails(this,'"+item.id+"','"+item.firmid+"');\" style=\"border:1px solid #ddd;float:left;\">"
								+ "<table width='100%' border='0' cellspacing='0' cellpadding='0' align='center' style=\"width: 96%;margin: 0px auto;\">"
								+ "<tr>"
								+ "<td style='text-align:left;padding-left:10px;'>"
								+ "<p style='text-align:left;font-size:110%; margin-bottom: 5px;font-weight: bolder;'>"+item.firmdes+"</p>"
								+ "</td>"
								+ "<td style='padding-right:10px;padding-bottom:10px;text-align:right;vertical-align:bottom;' "
								+ "colspan='2' onclick='javascript:toEvaluation(\"" + item.id + "\")'>";
					
					if(item.evaluationId != null && item.evaluationId != "") {
						htmlStr = htmlStr + "<span class='evaState'>已评价</span>";
					} else {
						htmlStr = htmlStr + "<span class='evaState'>未评价</span>";
					}
								
					htmlStr = htmlStr
								+ "</tr>"
								+ "<tr>"
								+ "<td style='text-align:left;padding-left:10px;padding-bottom:6px;border-bottom: 1px solid #ddd;'>"
								+ "<span style=\"font-family: Arial;\">"
								+ item.orderTimes.substr(0,16)
								+ "</span>"
								+ "</td>"
								+ "<td style='padding-right:10px;padding-bottom:10px;text-align:right;vertical-align:bottom;border-bottom: 1px solid #ddd;'>"
								+ "<span style='font-size:80%;'>&nbsp;￥</span><span style='color:red;font-size:150%;font-family: Arial;	'>" + item.money +"</span>"
								+ "</td>"
								+ "</tr>"
								+ "<tr>"
								+ "<td class='three-left'>"
								+ "<div style='color:#656565;font-size:80%;padding-bottom:5px;'>";
								
					if(item.isfeast == '0') {
						htmlStr = htmlStr + "订单类型:堂食";
					} else if(item.isfeast == '2') {
						htmlStr = htmlStr + "订单类型:外卖";
					} else {
						htmlStr = htmlStr + "订单类型:堂食";
					}
								
					htmlStr = htmlStr
								+ "</div><div style='color:#656565;font-size:80%;'>订单编号:&nbsp;<span style=\"font-family: Arial;\">"+item.resv+"</span></div>"
								+ "</td>"
								+ "<td class='three-right'>"
								+ stateValue
								+ "</td>"
								+ "</tr>"
								+ "</table>"
								+ "</div>"
								+"<div class=\"oneOrder orderDel\" id=\""+item.id+"Del\" data-ajax=\"false\" onclick=\"javaScript:deleteDetails(this,'"+item.id+"','"+item.firmid+"');\" style=\"border:1px solid #ddd;background-color: red;width:60px;float: left;display:none;margin-left: 0px;margin-right: 0px;\">"
								+"	<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"width: 96%;margin: 0px auto;height:143px;\">"
								+"			<tr>"
								+"				<td style=\"text-align:left;padding-left:10px;padding-bottom:6px;\">"
								+"					&nbsp;"
								+"				</td>"
								+"			</tr>"
								+"			<tr>"
								+"				<td style=\"text-align:left;\">"
								+"					<p style=\"text-align:center;font-size:110%; margin-bottom: 5px;font-weight: bolder;\">"
								+"						删除"
								+"					</p>"
								+"				</td>"
								+"			</tr>"
								+"			<tr>"
								+"				<td class=\"three-left\">"
								+"					&nbsp;"
								+"				</td>"
								+"			</tr>"
								+"		</table>"
								+"</div>";								
	        	});
				if(htmlStr){
					htmlStr += "<div>&nbsp;</div>";
				}
				$("#blankDiv").hide();
				nowObj.html(htmlStr);//填充新内容
				closeLayer();
				$(function() {
					$(".oneOrder").bind("swipeleft",function(){
						leftDelFlag = true;
						$(this).css("margin-left","-35px");
						$(this).css("margin-right","0px");
						$("#"+$(this).attr("id")+"Del").show();
					});
					$(".oneOrder").bind("swiperight",function(){
						leftDelFlag = false;
						$(this).css("margin-left","5%");
						$(this).css("margin-right","5%");
						$("#"+$(this).attr("id")+"Del").hide();
					});
				});
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				closeLayer();
				alertMsg("网络不稳定，请稍后再试");
			},
		});
	}
}
function orderDetails(div,orderid,firmid){
	if(leftDelFlag){
		return;
	}
	var pk_group = $("#pk_group").val();
	var openid = $("#openid").val();
	$(div).css("background-color","#eeeeee");
	window.setTimeout(function(){
		InitLayer();
		var url = "<c:url value='/bookMeal/orderDetail.do?pk_group=' />"+pk_group+"&openid="+openid
			+"&orderid="+orderid+"&firmid="+firmid+"&orderType=0&pageFrom=list&code=${code}";
// 	var url = "<c:url value='/wxPay/toBuyOrderForPay.do?pk_group=' />"+pk_group+"&openid="+openid
// 		+"&orderid="+orderid+"&firmid="+firmid+"&orderType=0&pageFrom=list&code=${code}";
// 	var url = "<c:url value='/wxPay/toOrderPay.do?pk_group=${pk_group}' />&resv=15041515494035&sumprice=1&firmid=33e5cdb180974293b31e&pax=2&id=9ad9cb8b8f6841d389a92604cd09ba5a&response_type=code&scope=snsapi_base&state=123&code=${code}";
		window.setTimeout(function(){
			$("#isLoadhelf").val(0);
			$("#isLoadcomp").val(0);
			location.href = url;
		},100);
	},500);
}
function payTest(){
	var url = "<%=basePath %>/wxPay/toOrderPay.do?resv=15041515494036&sumprice=1&firmid=33e5cdb180974293b31e&pax=2&id=9ad9cb8b8f6841d389a92604cd09ba5b&response_type=code&scope=snsapi_base&state=123&code=${code}";
	alertMsg(url);
		window.setTimeout(function(){
			location.href = url;
		},100);
}
$(function() {
	$(".oneOrder").bind("swipeleft",function(){
		$(".oneOrder").css({"margin-left":"5%","margin-right":"5%"});
		$(".orderDel").css({"margin-left":"0","margin-right":"0"});
		$(".orderDel").hide();
		leftDelFlag = true;
		$(this).css("margin-left","-35px");
		$(this).css("margin-right","0px");
		$("#"+$(this).attr("id")+"Del").show();
	});
	$(".oneOrder").bind("swiperight",function(){
		$(".oneOrder").css({"margin-left":"5%","margin-right":"5%"});
		$(".orderDel").css({"margin-left":"0","margin-right":"0"});
		$(".orderDel").hide();
		leftDelFlag = false;
		$(this).css("margin-left","5%");
		$(this).css("margin-right","5%");
		$("#"+$(this).attr("id")+"Del").hide();
	});
	$("body").click(function(){
		$(".oneOrder").css({"margin-left":"5%","margin-right":"5%"});
		$(".orderDel").css({"margin-left":"0","margin-right":"0"});
		$(".orderDel").hide();
		leftDelFlag = false;
	});
});
//删除订单
function deleteDetails(div,orderid,firmid){
	InitLayer();
	var pk_group = $("#pk_group").val();
	var openid = $("#openid").val();
	//加延时是为了能让蒙板显示
	window.setTimeout(function(){
		$.ajax({
			url:"<c:url value='/bookDesk/deleteDetails.do' />",
			type:"POST",
			async:false,
			dataType:"json",
			data:{"pk_group":pk_group,"openid":openid,"id":orderid,"firmid":firmid},
			success:function(json){
				$("#"+orderid).remove();
				$("#"+orderid+"Del").remove();
				closeLayer();
			},
			error:function(json){
				alertMsg(json);
				closeLayer();
			}
		});
	},100);
}

function toEvaluation(orderid) {
	var pk_group = $("#pk_group").val();
	var openid = $("#openid").val();
	//进入评价页面
	var uri = "<%=path %>/evaluate/takeOutEvaluate.do?openid=" + openid 
			+ "&pk_group=" + pk_group + "&ordersId=" + orderid;
	window.location.href = uri;
}
</script>
</head>
<body>
<div data-role="page" data-theme="d" id="pageone"><!--页面层容器-->
	<div data-role="header" style="border:0;">
    	<div class="stateContent">
      		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="selectType" onclick="javaScript:loadOrders(this,'0');">进行中</td>
					<td class="noselectType" onclick="javaScript:loadOrders(this,'1');">已结单</td>
				</tr>
			</table>
    	</div>
  	</div>
	<div data-role="content" style="margin-top:0;padding:0;"><!--页面主体-->
		<input type="hidden" id="pk_group" value="${pk_group}">
		<input type="hidden" id="openid" value="${openid}">
		<input type="hidden" id="isLoadhelf" value="1">
		<input type="hidden" id="isLoadcomp" value="0">
		<c:set var="orderSize" value="${fn:length(listNet_Orders)}"/>
		<div id="helf">
			<c:choose>
				<c:when test="${orderSize <= 0}">
					<div class="noOrder">未查询到订单
					</div>
				</c:when>
				<c:otherwise>
				<c:forEach items="${listNet_Orders}" var="o">
<%-- 				<div class="oneOrder"  data-ajax="false" style="border:1px solid #ddd;float:left;" id="${o.id}"> --%>
				<div class="oneOrder"  id="${o.id}"  data-ajax="false" onclick="javaScript:orderDetails(this,'${o.id}','${o.firmid}');" style="border:1px solid #ddd;float:left;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 96%;margin: 0px auto;">
							<tr>
								<td style="text-align:left;padding-left:10px;">
									<p style="text-align:left;font-size:110%; margin-bottom: 5px;font-weight: bolder;">
										${o.firmdes}
									</p>
								</td>
								<td style="padding-right:10px;padding-bottom:10px;text-align:right;vertical-align:bottom;" 
									colspan="2" onclick="javascript:toEvaluation('${o.id}')">
									<c:choose>
										<c:when test="${o.evaluationId != null && o.evaluationId != ''}">
											<span class="evaState">已评价</span>
										</c:when>
										<c:otherwise>
											<span class="evaState">未评价</span>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td style="text-align:left;padding-left:10px;padding-bottom:6px;border-bottom: 1px solid #ddd;">
									<span style="font-family: Arial;">${fn:substring(o.orderTimes,0,16)}</span>
								</td>
								<td style="padding-right:10px;padding-bottom:10px;text-align:right;vertical-align:bottom;border-bottom: 1px solid #ddd;">
									<span style="font-size:80%;">&nbsp;￥</span><span style="color:red;font-size:150%;font-family: Arial;">${o.money}</span>
								</td>
							</tr>
							<tr>
								<td class="three-left">
									<div style="color:#656565;font-size:80%;padding-bottom:5px">
										<c:choose>
											<c:when test="${o.isfeast == 0}">
												订单类型:&nbsp;堂食
											</c:when>
											<c:when test="${o.isfeast == 2}">
												订单类型:&nbsp;外卖
											</c:when>
											<c:otherwise>
												订单类型:&nbsp;堂食
											</c:otherwise>
										</c:choose>
									</div>
									<div style="color:#656565;font-size:80%;">订单编号:&nbsp;<span style="font-family: Arial;">${o.resv}</span></div>
								</td>
								<td class="three-right-menu">
									<c:choose>
										<c:when test="${o.state == 1 or o.state == 2}">
											<span class="redState">未下单</span>
										</c:when>
										<c:when test="${o.state == 3}">
											<span class="redState">已取消</span>
										</c:when>
										<c:when test="${o.state == 6}">
											<span class="greenState">已完成</span>
										</c:when>
										<c:when test="${o.state == 7}">
											<span class="greenState">已下单</span>
										</c:when>
										<c:otherwise>
											<span class="redState">未受理</span>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</table>
				</div>
				<div class="oneOrder orderDel" id="${o.id}Del" data-ajax="false" onclick="javaScript:deleteDetails(this,'${o.id}','${o.firmid}');" style="border:1px solid #ddd;background-color: red;width:60px;float: left;display:none;margin-left: 0px;margin-right: 0px;">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 96%;margin: 0px auto;height:143px;">
							<tr>
								<td style="text-align:left;padding-left:10px;padding-bottom:6px;">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td style="text-align:left;">
									<p style="text-align:center;font-size:110%; margin-bottom: 5px;font-weight: bolder;">
										删除
									</p>
								</td>
							</tr>
							<tr>
								<td class="three-left">
									&nbsp;
								</td>
							</tr>
						</table>
				</div>
				</c:forEach>
				</c:otherwise>
			</c:choose>
<!-- 			<input type="button" name="pay" value="支付测试" onclick="payTest()"/> -->
		</div>
		<div id="blankDiv">
			&nbsp;
		</div>
		<div id="complete">
			&nbsp;
		</div>
	</div><!-- content -->
</div><!-- page -->
</body>
</html>