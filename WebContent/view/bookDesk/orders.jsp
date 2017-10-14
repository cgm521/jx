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
<link type="text/css" href='<c:url value="/css/wechat/orders.css" />' rel="stylesheet" />
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
$(document).on("pageinit","#pageone",function(){
	$("#complete").hide();
});
var sftsJs = {};
<c:if test="${not empty sfts}">
	<c:forEach items="${sfts}" var="se">
		sftsJs["${se.key}"] ="${se.value}";
	</c:forEach>
</c:if>
function loadOrders(selectType,orderState){
	if($(selectType).hasClass("selectType")){
		return;
	}
	var nowObj = null;
	if($("#complete").is(":hidden")){
		$("#complete").show();
		$("#helf").hide();
		nowObj = $("#complete");
	}else{
		$("#helf").show();
		$("#complete").hide();
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
			data:{"pk_group":pk_group,"openid":openid,"orderType":"1","orderState":orderState},
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
					}else{
						sftname = sftsJs[item.sft];
					}
					
					if(item.state=="3"){
						stateValue = "已取消";
					}else{
						stateValue = "已完成";
					}
					
					htmlStr = htmlStr
								+ "<div class='oneOrder' onclick=\"javaScript:orderDetails(this,'"+item.id+"','"+item.firmid+"');\" style=\"border:1px solid #ddd;\">"
								+ "<table width='100%' border='0' cellspacing='0' cellpadding='0' align='center' style=\"width: 96%;margin: 0px auto;\">"
								+ "<tr>"
								+ "<td style='text-align:left;padding-left:10px;' colspan='2'>"
								+ "<p style='text-align:left;font-size:110%; margin-bottom: 5px;font-weight: bolder;'>"+item.firmdes+"</p>"
								+ "</td>"
								+ "</tr>"
								+ "<tr>"
								+ "<td style='text-align:left;padding-left:10px;padding-bottom:6px;border-bottom: 1px solid #ddd;'>"
								+ "<span style=\"font-family: Arial;\">"
								+ item.orderTimes.substr(0,16)
								+ "</span>"
								+ "</td>"
								+ "<td style='padding-right:10px;padding-bottom:10px;text-align:right;vertical-align:bottom;border-bottom: 1px solid #ddd;'>"
								+ "<span style='font-size:80%;'>&nbsp;￥</span><span style='color:red;font-size:150%;font-family: Arial;'>" + item.money + "</span>"
								+ "</td>"
								+ "</tr>"
								+ "<tr>"
								+ "<td class='three-left'>"
								+ "<div style='color:#656565;font-size:80%;padding-bottom:5px'>"
								+ "预定/"+item.roomtype+"/"+((item.arrtime==null||item.arrtime=="")?item.dat:item.arrtime);
					if("${empty isCnSft || isCnSft ne 'Y'}" == "TRUE"){
						htmlStr = htmlStr			
								+"/"+sftname //+item.roompax+"人"
					}else{
						htmlStr = htmlStr			
							+"/"+item.datmins //+item.roompax+"人"
					}
					htmlStr = htmlStr			
								+ "</div>"
								+ "<div style='color:#656565;font-size:80%;'>订单编号:&nbsp;<span style='font-family: Arial;'>"+item.resv+"</span></div>"
								+ "</td>"
								+ "<td class='three-right-menu'><span class='greenState'>"
								+ stateValue
								+  "</span></td>"
								+ "</tr>"
								+ "</table>"
								+ "</div>";
	        	});
				nowObj.html(htmlStr);//填充新内容
				closeLayer();
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				closeLayer();
				//alert(XMLHttpRequest.readyState);
			},
		});
	}
}
function orderDetails(div,orderid,firmid){
	var pk_group = $("#pk_group").val();
	var openid = $("#openid").val();
	$(div).css("background-color","#eeeeee");
	window.setTimeout(function(){
		InitLayer();
		var url = "<c:url value='/bookDesk/orderDetail.do?pk_group=' />"+pk_group+"&openid="+openid
			+"&orderid="+orderid+"&firmid="+firmid+"&orderType=1";
		window.setTimeout(function(){
			$("#isLoadhelf").val(0);
			$("#isLoadcomp").val(0);
			location.href = url;
		},100);
	},500);
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
					<div class="noOrder">未查询到订单</div>
				</c:when>
				<c:otherwise>
				<c:forEach items="${listNet_Orders}" var="o">
				<div class="oneOrder"  data-ajax="false" onclick="javaScript:orderDetails(this,'${o.id}','${o.firmid}');"  style="border:1px solid #ddd;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 96%;margin: 0px auto;">
							<tr>
								<td style="text-align:left;padding-left:10px;" colspan="2">
									<p style="text-align:left;font-size:110%; margin-bottom: 5px;font-weight: bolder;">
										${o.firmdes}
									</p>
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
									<div style="color:#656565;font-size:80%;padding-bottom:5px">预定/${o.roomtype}/<c:choose><c:when test="${empty o.arrtime}">${o.dat}</c:when><c:otherwise>${o.arrtime}</c:otherwise></c:choose><c:choose><c:when test="${empty isCnSft || isCnSft ne 'Y'}">/${sfts[o.sft]}</c:when><c:otherwise>/${o.datmins}</c:otherwise></c:choose></div>
									<div style="color:#656565;font-size:80%;">订单编号:&nbsp;<span style="font-family: Arial;">${o.resv}</span></div>
								</td>
								<td class="three-right-menu">
									<span class="greenState">
										<c:choose>
											<c:when test="${o.state eq '0'}">未提交</c:when>
											<c:when test="${o.state eq '1'}">已受理</c:when>
											<c:otherwise>进行中</c:otherwise>
										</c:choose>
									</span>
								</td>
							</tr>
						</table>
				</div>
				</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
		<div id="complete">
			&nbsp;
		</div>
	</div><!-- content -->
</div><!-- page -->
</body>
</html>