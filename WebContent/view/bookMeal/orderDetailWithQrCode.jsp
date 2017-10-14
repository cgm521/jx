<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons"%>
<%String path = request.getContextPath();
	String port ="80".equals((request.getServerPort()+""))?"":(":"+request.getServerPort()+"");
	String basePath = request.getScheme()+"://"+request.getServerName()+port+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>我的点菜单</title>
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
var tableId= "";
// 手机型号 0：安卓；1：苹果；2：winphone
var phoneType = "0";
var orderState = "${orderDetail.state}";

var myScrollCoupon;
function loaded() {
	setTimeout(function(){
		myScrollCoupon = new iScroll("listCouponDiv",{hScrollbar:false,vScrollbar:false});
		},100);
} 
window.addEventListener("load",loaded,false);

wx.config({
    debug: false,
    appId: '${appId}',
    timestamp: '${signMap.timestamp}',
    nonceStr: '${signMap.nonceStr}',
    signature: '${signMap.signature}',
    jsApiList: ['scanQRCode']
});
wx.ready(function () {
	//调用微信扫一扫功能，获取二维码中的台位号
	$(".scanToOrders").bind("click",function (){
		if(orderState=="1" || orderState=="2"){//其他状态的不能扫码下单
   			if("${company.vafterpay}"=="Y" && orderState=="1"){//需要先支付才能再下单，调用支付界面
   				confirmMsg("该账单需要先支付才能下单。\n确认支付？",function(){
   		   				var url = "<%=basePath %>/wxPay/toOrderPay.do?pk_group=${pk_group}&firmid=${orderDetail.firmid}&resv=${orderDetail.resv}&sumprice=${totalPrice}&pax=${orderDetail.pax}&id=${orderDetail.id}&response_type=code&scope=snsapi_base&state=123&code=${code}&openScan=1&billstate=2";
   						window.setTimeout(function(){
   							location.href = url;
   						},500);
   				});
   				
   			}else{
   				$("#confirmOrderBut").click();
        		/* if(phoneType == "1") {
        			$.ajaxSetup({async:false});
	            	$.post("<c:url value='/bookMeal/setScanEventValue.do?' />", {"openid":"${openid}"}, function(data){
	    				wx.scanQRCode({
	    				    needResult: 0, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    				    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    				    success: function (res) {
	    				    	//wx.closeWindow();
	    				    	InitLayer();
	    				    	var tableStr = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
	    		            	
	    		            	$.post("<c:url value='/bookMeal/getTableId.do?' />", {"openid":"${openid}"}, function(data){
	    		            		alertMsg(data);
	    		            		var dataArray = data.split("_");
	    		            		alertMsg(dataArray[0] + ":" + "${orderDetail.firmid}");
	    				   			if(dataArray[0] != "${orderDetail.firmid}"){
	    				   				alertMsg("扫码门店与订单门店不符，不能下单！");
	    				   				return;
	    				   			}
	    				   			tableId = dataArray[1];
	    					   		if(tableId != null && tableId != ""){
	    					   			$("#confirmOrderBut").click();
	    					   		}
	    		            	});
	    		        		closeLayer();
	    					}
	    				});
	            	});
        		} else {
    				wx.scanQRCode({
    				    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
    				    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
    				    success: function (res) {
    				    	InitLayer();
    				    	var tableStr = res.resultStr; // 当needResult 为 1 时，扫码返回的结果

    		            	$.ajaxSetup({async:false});
    		            	$.post("<c:url value='/bookMeal/setScanEventValue.do?' />", {"openid":"${openid}"}, function(data){
    		            	});
    		            	
    		            	$.post("<c:url value='/bookMeal/getTableId.do?' />", {"openid":"${openid}"}, function(data){
    		            		var dataArray = data.split("_");
    				   			if(dataArray[0] != "${orderDetail.firmid}"){
    				   				alertMsg("扫码门店与订单门店不符，不能下单！");
    				   				return;
    				   			}
    				   			tableId = dataArray[1];
    					   		if(tableId != null && tableId != ""){
    					   			$("#confirmOrderBut").click();
    					   		}
    		            	});
    		        		closeLayer();
    					}
    				});
        		} */
   			}
		}else{
			alertMsg("此单不支持扫码下单。");
			return;
		}
	});
});

wx.error(function (res) {
	alertMsg(res.errMsg);
});
/*去除安卓返回及刷新按钮*/
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});
$(function(){
	var ua = navigator.userAgent.toLowerCase();
// 	if(ua.match(/MicroMessenger/i)!="micromessenger") {
// 		$("body").text("请使用微信浏览器打开");
// 		return;
// 	}
	if(ua.indexOf('android') > -1 || ua.indexOf('linux') > -1) {
		// 安卓手机
		phoneType = "0";
	} else if(ua.indexOf('iphone') > -1) {
		// 苹果手机
		phoneType = "1";
	} else if(ua.indexOf('windows phone') > -1) {
		// winphone手机
		phoneType = "2";
	}
	
	if("${pageFrom}" == "orderDetail" || ("${orderDetail.bookDeskOrderID}"!=null && "${orderDetail.bookDeskOrderID}"!="")) {
		$("#cancelBtn").hide();
		$("#scanTable").text("");
		$("#scanTable").css("background-color", "#000000");
		$("#scanTable").removeClass("scanToOrders");
		$("#headDiv").removeClass("scanToOrders");
		$("#headDiv").find("span").eq(0).text("祝您用餐愉快");
		if("${deskOrderState}" == "1" || "${deskOrderState}" == "2"){
			$("#buttDivTot").hide();
		}
	}
	
	// 通过已到店进入该页面，直接调用扫码下单
	var inStoreType = "${inStoreType}";
	if(inStoreType == "1") {
		$("#scanTable").click();
	}
	//如果是支付后跳转到订单明细页面并且打开扫码功能标志为1，自动打开扫码功能
	if("${openScan}"=="1"){
		$(".scanToOrders").trigger("click");
	}
	
	// 已下单或扫码点餐MQ推送成功，隐藏下单按钮
	if((orderState != "1" && orderState != "2") || "${scanType}" == "1") {
		$("#scanTable").text("");
		$("#scanTable").css("background-color", "#000000");
		$("#scanTable").removeClass("scanToOrders");
		$("#headDiv").removeClass("scanToOrders");
		$("#headDiv").find("span").eq(0).text("订单已提交，祝您用餐愉快");
	}
	
	// 已下单到POS时，显示选择电子券按钮
	if(orderState == "7" && "${fn:length(listCoupon)}" > 0) {
		$("#selectCouponBtnTr").show();
		$(".orderStateImg").css("margin-top","160px");
	}
	
	if("${hasAskCheckOut}" == "Y") {
		// 如果已经点击过我要结帐，隐藏加菜按钮
		$("#addMenuBtn").hide();
		$("#selectCouponBtnTr").hide();
		$(".orderStateImg").css("margin-top","105px");
	}
});

function showConfirm(){
	$('#cancelLink').click();
}
//取消订单
function cancelOrders(pk_group,ordersid){
	if(orderState=="3"){
		alertMsg("该账单已经取消，不能取消。");
		return;
	}
	$("#cancelDiv").popup("close");
	InitLayer();
	var url = "<c:url value='/bookDesk/cancelOrder.do?pk_group=' />" + pk_group + "&orderid=" + ordersid + "&orderType=0&firmid=${orderDetail.firmid}&state=${orderDetail.state}&vtransactionid=${orderDetail.vtransactionid}&paymoney=${orderDetail.paymoney}";
	window.setTimeout(function(){
		location.href = url;
	},100);
}
//关闭取消订单提示框
function noCancel(){
	$("#cancelDiv").popup("close");
}
//关闭下单提示框
function noConfirm(){
	$("#confirmSingleDiv").popup("close");
}
//下单操作ajax提交
function confirmOrder(pk_group, ordersid, firmCode){
	// 校验就餐人数
	var validate1 = new Validate({
		validateItem:[{
			type:'text',
			validateObj:'totalPerson',
			validateType:['num1'],
			param:['F'],
			error:['就餐人数输入错误']
		}]
	});
	if(!validate1._submitValidate()){
		return;
	}
	
	$("#confirmSingleDiv").popup("close");//隐藏提示框
	/* window.setTimeout(function(){
		$("#confirmSingleDiv").popup("close");//隐藏提示框
	},500); */
	//$.ajaxSetup({async:false});
	var pax = $("#totalPerson").val();
// 	alert("openid=${openid},pk_group="+pk_group+",orderid="+ordersid+",firmCode="+firmCode+",pax="+pax);
	$.post("<c:url value='/bookMeal/setScanEventValue.do?' />", {"openid":"${openid}", "pk_group":pk_group, "orderid":ordersid, "firmid":"${orderDetail.firmid}", "firmCode":firmCode, "pax":pax}, function(data){
		wx.scanQRCode();
		//wx.closeWindow();
	});
	InitLayer();
	window.setTimeout(function(){
		wx.closeWindow();
		closeLayer();
	},5000);
	/* $.ajaxSetup({async:false});
	$.post("<c:url value='/bookDesk/commitOrdr.do?' />",{"pk_group":pk_group,"id":ordersid,"tables":tableId,"firmid":"${orderDetail.firmid}"},function(data){
		if(data=="1"){
			alertMsg("下单成功。");
			//修改订单状态标志及值
			$(".orderStateImg img").attr("src","<c:url value='/image/wechat/yixiadan.png'/>");
			orderState = 7;
			$("#cancelBut").remove();//删除取消订单按钮的tr
		}else if(data=="0"){
			alertMsg("没有要更新的数据");
		}else{
			// 下单失败，状态更新为1
			//$.post("<c:url value='/bookMeal/updateOrderState.do?' />",{"pk_group":pk_group, "orderid":ordersid, "state":"1"},function(updateRet){
				
			//});
			//alertMsg("下单失败，失败原因："+data);
			alertMsg("下单失败，请稍后再试");
		}
		closeLayer();
	}); */
}
var couponStr = "${orderDetail.id}:";
//选择电子券页面
function toSelectCouponPage() {
	$("#selectCouponDiv").popup("open");
}
//确认选择电子券
function confirmSelect() {
	couponStr = "${orderDetail.id}:";
	// 判断所选电子券
	$("[name='selectCoupon']").each(function(){
		if($(this).is(":visible")) {
			couponStr += $(this).parent().attr("pushval") + ";";
		}
	});
	
	// 判断所选电子券张数
	var cnt = 0;
	$("[name='selectCoupon']").each(function(){
		if($(this).is(":visible")) {
			cnt += 1;
		}
	});
	$("#selectCouponHead").text("您已经选择" + cnt + "张电子券");
	
	$("#selectCouponDiv").popup("close");
}
// 不使用电子券
function cancelSelect() {
	couponStr = "${orderDetail.id}:";
	$("#selectCouponDiv").popup("close");
	$("#selectCouponHead").text("您已经选择0张电子券");
}
//选择电子券
function toCheckout(){
	if("${not empty groupactms}"=="true"){
		$("#gotoPagetwo").click();
	}else{
		$.ajax({
			url:"<c:url value='/wxPay/resetPayMoney.do'/>?orderid=${orderDetail.id}",
			type:"POST",
			dataType:"json",
			success:function(res){
				InitLayer();
				$("#addMenuBtn").hide();
				$("#selectCouponBtnTr").hide();
				$(".orderStateImg").css("margin-top","105px");
				location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&" + 
					"redirect_uri=<%=basePath %>/wxPay/toCheckout.do?" + "couponStr=" + couponStr + "&id=${orderDetail.id}&vcode=${firm.firmCode}" + 
					"&resv=${orderDetail.resv}&firmid=${orderDetail.firmid}&pax=${orderDetail.pax}&dat=${orderDetail.dat}" + 
					"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
			},
			error:function(ajax){
				//alertMsg(ajax);
				InitLayer();
				$("#addMenuBtn").hide();
				$("#selectCouponBtnTr").hide();
				$(".orderStateImg").css("margin-top","105px");
				location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&" + 
					"redirect_uri=<%=basePath %>/wxPay/toCheckout.do?" + "couponStr=" + couponStr + "&id=${orderDetail.id}&vcode=${firm.firmCode}" + 
					"&resv=${orderDetail.resv}&firmid=${orderDetail.firmid}&pax=${orderDetail.pax}&dat=${orderDetail.dat}" + 
					"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
			}
		});
	}
}
//延时调用的方法
function ysFunction(){
	//发送mq消息，并返回状态
	$.ajax({
		url:"<c:url value='/wxPay/toCheckout.do'/>?id=${orderDetail.id}&resv=${orderDetail.resv}&vcode=${firm.firmCode}&dat=${orderDetail.dat}",
		type:"POST",
// 		async:false,
		dataType:"json",
		success:function(res){
			//alert(res);
			if(res=="ok"){
// 				InitLayer("正在等待门店端返回结账数据，请稍候。。。");
				//定时执行检测方法
// 				 setInterval(getOrderState,5000);
				getOrderState();
			}
		},
		error:function(ajax){
			alertMsg(ajax);
			closeLayer();
		}
	});
	
}
//检测订单折扣状态及需支付金额
function getOrderState(){
// 	alert("id=${orderDetail.id}&resv=${orderDetail.resv}&vcode=${firm.firmCode}");
	$.ajax({
			url:"<c:url value='/wxPay/getOrderState.do'/>?id=${orderDetail.id}&resv=${orderDetail.resv}&vcode=${firm.firmCode}",
			type:"POST",
// 			async:false,
			dataType:"json",
			success:function(res){
// 				alert(res);
				if(res!=""){
// 					InitLayer("正在等待门店端返回结账数据，请稍候。。。");
					var url = "<%=basePath %>/wxPay/toOrderPay.do?pk_group=${pk_group}&firmid=${orderDetail.firmid}&resv=${orderDetail.resv}&sumprice="+res+"&pax=${orderDetail.pax}&id=${orderDetail.id}&response_type=code&scope=snsapi_base&state=123&code=${code}&billstate=6";
// 					alert(url);
					window.setTimeout(function(){location.href = url;},100);
				}
				closeLayer();
			},
			error:function(ajax){
				alertMsg(ajax);
				getOrderState();
				closeLayer();
			}
	});
}
// 未下单前，修改菜品
function modifyMenu() {
	var url = "<%=path %>/bookMeal/gotoMenu.do?openid=${openid}&pk_group=${pk_group}&firmid=${orderDetail.firmid}&orderid=${orderDetail.id}&type=modify";
	/********修改菜品返回按钮设置********/
	if("${pageFrom}" == "orderDetail" || ("${orderDetail.bookDeskOrderID}"!=null && "${orderDetail.bookDeskOrderID}"!="")) {
		url += "&pageFrom=${pageFrom}"
	}
	InitLayer();
	window.setTimeout(function(){
		location.href = url;
	},100);
}

// 下单后，增加菜品
function addMenu() {
// 	alert("1");
	var url = "<%=path %>/bookMeal/gotoMenu.do?openid=${openid}&pk_group=${pk_group}&firmid=${orderDetail.firmid}&orderid=${orderDetail.id}&type=add";
	InitLayer();
	window.setTimeout(function(){
		location.href = url;
	},100);
}
function backmoney(){
	var url = "<%=path %>/bookDesk/cancelOrder.do?vtransactionid=${orderDetail.vtransactionid}&paymoney=${orderDetail.paymoney}&firmid=${orderDetail.firmid}&orderid=${orderDetail.id}&state=${orderDetail.state}";
	InitLayer();
	window.setTimeout(function(){
		location.href = url;
	},100);
}

// 反选电子券
function selectCoupon(obj) {
	var imgObj = $(obj).children().eq(2);
	if(imgObj.is(":visible")) {
		imgObj.hide();
	} else {
		imgObj.show();
	}
}
</script>
<style type="text/css">
	.cashCoupDiv{
		background-color: white;
		border: 1px  solid;
		width: 90%;
		height:80px;
		vertical-align:middle;
		margin: 5px 5%;
	}
	h1 {
	    width: 1em;
	    margin-left: 15%;
	    font-size: 13px;
	  }
.detail6{
	background-color:#FFFFFF;
	text-align:right;
	padding-right:10px;
}
</style>
</head>
<body>
<div data-role="page" data-theme="d" id="pageone"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div id="headDiv" class="header">
			<img src="<c:url value='/image/wechat/laba.png'/>" /><span>请联系服务员扫描二维码下单</span>
		</div>
		<div class="firm">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="table-layout:fixed;">
				<tr class="topRadius">
					<td class="detail" colspan="2">
						<div style="float:left;width:80%;font-size:110%;font-weight: bolder;margin-top: 6px;">
							${orderDetail.firmdes}
						</div>
						<div onclick="alertMsg('${orderDetail.addr}');" style="float:left;width:10%;">
							<img src="<c:url value='/image/wechat/local.png'/>"/>
						</div>
						<c:if test="${orderDetail.tele ne null && orderDetail.tele ne '' }">
							<div style="float:right;width:10%; ">
								<a href="tel:${orderDetail.tele }"><img style="width:30px;height:30px;" src="<c:url value='/image/wechat/dianhua.png'/>"/></a>
							</div>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="detail" style="width:70%;padding-top:5px;padding-bottom:0px;">
						<span style="font-family: Arial;color:#656565;font-size: 100%;">${orderDetail.orderTimes}</span>
					</td>
					<td class="detail" style="padding-top:5px;padding-bottom:0px;text-align:right;padding-right:25px" rowspan="4">
						<img style="width:100px; height:100px;" src='<%=Commons.getConfig().getProperty("vcardPic") %>${orderDetail.resv}.png'>
					</td>
				</tr>
				<tr>
					<td class="detail" style="width:70%;padding-top:5px;padding-bottom:0px;" colspan="2">
						<div>订单编号:&nbsp;<span style="color:#656565;font-size: 100%;font-family: Arial;">${orderDetail.resv}</span></div>
					</td>
				</tr>
				<tr>
					<td class="detail" style="width:70%;padding-top:5px;padding-bottom:0px;" colspan="2">
						<div>就餐桌位:&nbsp;<span style="color: red;font-size: 150%;font-family: Arial;">${orderDetail.tablesname}</span></div>
					</td>
				</tr>
				<tr>
					<td class="detail" style="width:70%;vertical-align:bottom;padding-top:5px;padding-bottom:0px;" colspan="2">
						<div>账单金额:&nbsp;￥<span style="font-family: Arial;font-size: 180%;color: red; padding-right:10px;">${totalPrice }</span></div>
					</td>
				</tr>
			</table>
		</div>
		<c:if test="${pageFrom ne 'orderDetail' && (orderDetail.bookDeskOrderID eq null || orderDetail.bookDeskOrderID eq '')}">
		<div class="orderStateImg" style="margin-top: 125px;">
			<c:if test="${orderDetail.state == 1 or orderDetail.state == 2}">
				<img src="<c:url value='/image/wechat/weixiadan.png'/>"/>
			</c:if>
			<c:if test="${orderDetail.state == 3 }">
				<img src="<c:url value='/image/wechat/yiquxiao.png'/>"/>
			</c:if>
			<c:if test="${orderDetail.state == 6 }">
				<img src="<c:url value='/image/wechat/yiwancheng.png'/>"/>
			</c:if>
			<c:if test="${orderDetail.state == 7 }">
				<img src="<c:url value='/image/wechat/yixiadan.png'/>"/>
			</c:if>
		</div>
		</c:if>
		<div class="order" id="buttDivTot">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr id="selectCouponBtnTr" onclick="javaScript:toSelectCouponPage()" style="display:none">
					<td class="detail" style="border-bottom:1px solid #EEEEEE;">
						选择电子券&nbsp;&nbsp;<span id="selectCouponHead" style="color:red; font-size:70%;">您已经选择0张电子券</span>
					</td>
					<td class="detail6" style="border-bottom:1px solid #EEEEEE;">
						<img width="15" height="15" src="<%=path %>/image/wechat/arrow_right.png">
					</td>
				</tr>
				<tr class="bottomRadius" id="cancelBut">
					<td class="detail2" style="padding: 0px;" id="buttonDivtd" colspan="2">
 						<c:if test="${orderDetail.state lt '2' and orderDetail.state != '7'}">
							<p>
								<input type="button" id="cancelBtn" style="width:40%;" value="取消订单" data-role='none' data-ajax="false" onclick="javaScript:showConfirm()">
								<input type="button" style="width:40%" value="修改菜品" data-role='none' data-ajax="false" onclick="javaScript:modifyMenu()">
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
 						<c:if test="${orderDetail.state eq '7'}">
							<p>
								<a id="gotoPagetwo" href="#pagetwo"/>
								<input type="button" style="width:40%;" value="加菜" data-role='none' data-ajax="false" onclick="javaScript:addMenu()" id="addMenuBtn">
								<input type="button" style="width:40%;" value="我要结账" data-role='none' data-ajax="false" onclick="javaScript:toCheckout()">
							</p>
 						</c:if>
					</td>
 						<!--<c:if test="${orderDetail.state eq '6' && orderDetail.vtransactionid!=''}">
							<p>
								<input type="button" style="width:40%;" value="退款" data-role='none' data-ajax="false" onclick="javaScript:backmoney()" id="addMenuBtn">
							</p>
 						</c:if>-->
				</tr>
			</table>
		</div>
		<div class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="detail" colspan="2" style="font-size: 14px;">
						<div style="height:8px;"></div>
						<div style="margin-top: 2px;">订单类型:&nbsp;堂食</div>
						<div>就餐人数:&nbsp;<span style="vertical-align: bottom;font-family: Arial;font-size: 100%;color:#656565;">${orderDetail.pax}</span></div>
						<div style="margin-top: 2px;">备注:&nbsp;${orderDetail.remark}</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="spaceMeaage" style="padding-left: 10px;">
		<c:set var="orderDtlListSize" value="${fn:length(orderDtl)}"/>
			我的菜单，共<span style="font-family: Arial;">${orderDtlListSize }</span>条点菜记录
		</div>
		<div class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="background-color:#FFFFFF">
				<c:forEach items="${orderDtl }" var="food" varStatus="stat">
					<tr height="30px">
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;font-size:13px;font-family: Arial;"  width="10%">${stat.count }</td>
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align:left;" width="52%" >
							<div><span style='width:100%; text-align:left; font-size:14px;'>${food.foodsname }</span></div>
							<div><span style='width:100%; color:gray; text-align:left; font-size:12px;font-family: Arial;'>￥${food.price }</span></div>
							<c:if test="${fn:length(food.listDishAddItem) > 0 }">
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
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;"width="18%">
							<span style="color:#ffb400; font-size:14px;font-family: Arial;">×${food.foodnum }${food.unit}</span>
						</td>
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align: right;"width="20%">
							<span style="color:#ffb400; font-size:14px;font-family: Arial;">￥${food.totalprice }</span>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div class="orderSpace">
			&nbsp;
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-role="popup" data-position="fixed" data-tap-toggle="false" data-history="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a" style="width:75%;">
					<c:choose>
						<c:when test="${pageFrom eq 'orderDetail' }">
							<a href="<c:url value='/bookDesk/orderDetail.do?pk_group=${pk_group}&openid=${openid }&orderid=${orderDetail.bookDeskOrderID }&firmid=${orderDetail.firmid }&orderType=1' />" data-ajax="false">
								<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
							</a>
						</c:when>
						<c:otherwise>
							<a href="<c:url value='/bookDesk/findMenuOrders.do?pk_group=${pk_group}&openid=${openid }' />" data-ajax="false">
								<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
							</a>
						</c:otherwise>
					</c:choose>
				</td>
				<!-- <td style="background-color: #F39801;color: white;width:25%;text-align: center;" class="scanToOrders" id="scanTable">
					扫码下单
				</td> -->
			</tr>
		</table>
  	</div>
  	<a href="#confirmSingleDiv" id="confirmOrderBut" data-rel="popup" data-position-to="window"></a>
	<div data-role="popup" id="confirmSingleDiv" style="position:fixed; top:30%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
		<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr height="60px">
				<td width="40%" align="right" style="padding-right:10px">
					就餐人数
				</td>
				<td width="60%">
					<input id="totalPerson" type="number" min="1" max="99" oninput="this.value=this.value.slice(0,2);" value="${orderDetail.pax }"
					style="border:1px solid #D5D5D5;border-radius:4px;height:35px;width:75%;-webkit-appearance: none;" data-role="none"/>
				</td>
			</tr>
			<tr height="45px">
				<td align="center" colspan="2">
					<span style="color:#999999">下单后，订单将不可取消</span>
				</td>
			</tr>
			<tr height="65px">
				<td class="popTd" colspan="2">
					<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="取消" data-role='none' data-ajax="false" onclick="noConfirm()" /> 
					<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="确定" data-role='none' data-ajax="false" onclick="javaScript:confirmOrder('${pk_group}','${orderDetail.id}','${firm.firmCode}');" /> 
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
									<span style="font-size: 30px;">${coupon.typdes}</span><br>
									<span style="font-size: 15px;">有效期至：${coupon.edate }</span>
								</div>
								<div name="selectCoupon" style="padding-top:18px;" class="selectImgDiv">
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
<script type="text/javascript">
	//如果没有可以电子券，把选择电子券行去掉
	if("${listCouponSize}"=="0"){
		$("#selectCouponBtnTr").remove();
	}
	//如果订单操作按钮为空
	if(!$.trim($("#buttonDivtd").html())){
		$("#buttDivTot").hide();
		$(".orderStateImg").css("margin-top","23px");
	}
	function selectGroup(td,grouptyp,vname){
		var groupNum = $("#groupNum").val();
		var url = "<img src='"+"<c:url value='/image/wechat/xuanze.png'/>"+"'/>";
		if(grouptyp=="0"){
			if($(td).hasClass("fontcolor")){
				return;
			}
			$(".grouptd_center").removeClass("fontcolor");
			$(".grouptd_center").next().html("&nbsp;");
			$(td).addClass("fontcolor");
			$(td).next(".grouptd_right").html(url);
			$("#groupType").val(grouptyp);
		}else{
			var groupType = $(td).find("input").val();
			//groupType = groupType.replace(".","_");//后台已替换
			
			var groupOneDiv = $("#"+groupType);
			
			if(groupOneDiv!=null && groupOneDiv.length>0){
				$(".grouptd_center").removeClass("fontcolor");
				$(".grouptd_center").next().html("&nbsp;");
				addStyle();//为已选择的所有团购券添加样式
				
				$("#groupType").val(groupType);//记录当前选择的券
				$("#groupName").html("您选择了"+vname+",请输入验证码");
				
				$(".groupOneDiv").hide();
				groupOneDiv.show();
			}else{
				if(groupNum>=5){
					alertMsg("您最多可以选择使用5张团购券");
					return;
				}
				$(".grouptd_center").removeClass("fontcolor");
				$(".grouptd_center").next().html("&nbsp;");
				
				$("#groupType").val(groupType);//记录当前选择的券
				
				$("#groupName").html("您选择了"+vname+",请输入验证码");
				
				$(".groupOneDiv").hide();
				var html = "<div id='"+groupType+"' class='groupOneDiv'>"
					+ "<table><tr height='50px;'><td style='width:85%;padding-left:1%;'>"
					+ "<input type='type' value='' placeholder='请输入券验证码' name='v_"+groupType+"' /></td>"
					+ "<td style='font-size:12px;color:#D5D5D5;' onclick='writeTR(this);'>继续<br/>输入</td></tr></table></div>";
					
				$("#groupCodeDiv").prepend(html);
				$("#span_"+groupType).text("(1张)");
				$("#groupNum").val(parseInt(groupNum)+1);
				
				addStyle();//为已选择的所有团购券添加样式
			}
			
			$("#groupCode").popup("open");
		}
	}
	function addStyle(){
		var url = "<img src='"+"<c:url value='/image/wechat/xuanze.png'/>"+"'/>";
		
		var isHave = false;
		$(".groupTable tr td:nth-child(2)").each(function(){
	        var text = $(this).find("span").text().replace(/(^\s*)|(\s*$)/g,'');
	        if(text.indexOf("张)")>=0){
	        	isHave = true;
	        	$(this).addClass("fontcolor");
	        	$(this).next().html(url);
			}
		});
		if(!isHave){
			var td = $(".groupTable tr:eq(0) td:eq(1)");
			td.addClass("fontcolor");
			td.next().html(url);
		}
	}
	function complateGroup(){
		// 校验
		var groupType = $("#groupType").val();
		var isPass = true;
		$("#"+groupType).find("input").each(function(a,e){
			var code = $(e).val();
			if(code==null || code==""){
				isPass = false;
				//创建错误显示信息
		    	var msgDiv = $('<div class="validateMsg"></div>'); 
		    	msgDiv.css({
		    		'top': $(e).position().top + 3,
		    		'left': $(e).position().left,
		    		'height': $(e).height()-2,
		    		'width': $(e).width()-4,
		    		'line-height': $(e).height()+'px'
		    	}).html("请填写验证码")
		    	.insertAfter($(e))
		    	.bind('click.validate',function(){
		    		$(this).prev().focus();
		    		$(this).siblings('.validateMsg').remove();
		    		$(this).remove();
		    	});
			}
		});
		
		if(!isPass){
			return false;
		}
		$("#groupCode").popup("close");
	}
	function clearGroup(){
		var groupType = $("#groupType").val();
		var table = $("#"+groupType).find("table");
		var leng = table.find("tr").length;
		var groupNum = $("#groupNum").val();//总张数
		
		$("#span_"+groupType).text("");
		$("#groupNum").val(parseInt(groupNum)-leng);
		$("#"+groupType).remove();
		var td = $("#span_"+groupType).parent();
		td.removeClass("fontcolor");
		td.next().html("&nbsp;");
		addStyle();
		
		$("#groupCode").popup("close");
	}
	function writeTR(td){
		var groupType = $("#groupType").val();
		var table = $("#"+groupType).find("table");
		var leng = table.find("tr").length;
		var groupNum = $("#groupNum").val();//总张数
		if(groupNum <= 4){
			var cont = " onclick='writeTR(this);'>继续<br/>输入";
			//if(groupNum==4){
			//	cont = " onclick='deleteTR(this);'>删除";
			//}
			var html = "<tr height='50px;'><td style='width:85%;padding-left:1%;'>"
				+ "<input type='type' value='' placeholder='请输入券验证码' name='v_"+groupType+"' /></td>"
				+ "<td style='font-size:12px;color:#D5D5D5;'"+ cont +"</td></tr>";
			table.append(html);
			$(td).html("删除");
			$(td).attr("onclick", "deleteTR(this)");
			
			$("#span_"+groupType).text("("+(leng+1)+"张)");
			$("#groupNum").val(parseInt(groupNum)+1);
		}else{
			alertMsg("您最多可以选择使用5张团购券");
			return;
		}
	}
	function deleteTR(td){
		var groupType = $("#groupType").val();
		var table = $("#"+groupType).find("table");
		var leng = table.find("tr").length;
		var groupNum = $("#groupNum").val();
		
		var tr = $(td).parent();
		tr.remove();
		
		if(groupNum==5){
			table.find('td:last').html("继续<br/>输入");
			table.find('td:last').attr("onclick", "writeTR(this)");
		}
		
		$("#span_"+groupType).text("("+(leng-1)+"张)");
		$("#groupNum").val(parseInt(groupNum)-1);
	}
	//选择我要结账
	function toCheckoutGroup(){
		InitLayer();
		var groupType = $("#groupType").val();
		
		var codeData = "";
		$(".groupOneDiv").find("input").each(function(a,e){
			if(groupType!="0"){//0为不使用团购券
				codeData = codeData + e.name + "=" + e.value + ",";
			}
		});
		if(codeData!=null && codeData.length>0){
			codeData = ":" + codeData.substr(0,codeData.length-1);//与电子券信息区分开
		}

		//把团购券组装到电子券信息里
		$.ajax({
			url:"<c:url value='/wxPay/resetPayMoney.do'/>?orderid=${orderDetail.id}",
			type:"POST",
			dataType:"json",
			success:function(res){
				$("#addMenuBtn").hide();
				$("#selectCouponBtnTr").hide();
				$(".orderStateImg").css("margin-top","105px");
				location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&" + 
					"redirect_uri=<%=basePath %>/wxPay/toCheckout.do?" + "couponStr=" + couponStr + codeData + "&id=${orderDetail.id}&vcode=${firm.firmCode}" + 
					"&resv=${orderDetail.resv}&firmid=${orderDetail.firmid}&pax=${orderDetail.pax}&dat=${orderDetail.dat}" + 
					"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
			},
			error:function(ajax){
				//alertMsg(ajax);
				$("#addMenuBtn").hide();
				$("#selectCouponBtnTr").hide();
				$(".orderStateImg").css("margin-top","105px");
				location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&" + 
					"redirect_uri=<%=basePath %>/wxPay/toCheckout.do?" + "couponStr=" + couponStr + codeData + "&id=${orderDetail.id}&vcode=${firm.firmCode}" + 
					"&resv=${orderDetail.resv}&firmid=${orderDetail.firmid}&pax=${orderDetail.pax}&dat=${orderDetail.dat}" + 
					"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
			}
		});
	}
</script>
<div data-role="page" id="pagetwo" data-theme="d">
	<div data-role="content" style="width:100%;overflow:hidden;margin:0;list-style:none;padding:0;">
		<div id="headDiv" class="header">
			<span>如果您有团购券,请选择团购种类,并输入验证码</span>
		</div>
		<c:if test="${not empty groupactms}">
		<div class="groupContent">
			<input type="hidden" id="groupType" value="0"/> 
			<input type="hidden" id="groupNum" value="0"/>
			<table class="groupTable">
				<tr>
					<td  class="grouptd_left"><img src="<c:url value='/image/wechat/tuangou.jpg'/>"/></td>
					<td  class="grouptd_center fontcolor" onclick="selectGroup(this,'0','no');">
						不使用团购券
					</td>
					<td  class="grouptd_right">
						<img src="<c:url value='/image/wechat/xuanze.png'/>"/>
					</td>
				</tr>
				<c:forEach items="${groupactms}" var="a">
				<tr>
					<td class="grouptd_left">
						<c:choose>
							<c:when test="${a.couponcode eq '1'}">
								<img src="<c:url value='/image/wechat/meituan.jpg'/>"/>
							</c:when>
							<c:when test="${a.couponcode eq '2'}">
								<img src="<c:url value='/image/wechat/nuomi.jpg'/>"/>
							</c:when>
							<c:when test="${a.couponcode eq '3'}">
								<img src="<c:url value='/image/wechat/dazhong.jpg'/>"/>
							</c:when>
						</c:choose>
					</td>
					<td class="grouptd_center" onclick="selectGroup(this,'${a.couponcode}','${a.vname}');">
						${a.vname}<span id="span_${a.pk_actm}_${a.vcode}_${a.couponcode}_${a.nDerateNum}" class="fontcolor"></span>
						<input type="hidden" value="${a.pk_actm}_${a.vcode}_${a.couponcode}_${a.nDerateNum}"/>
					</td>
					<td class="grouptd_right">
						&nbsp;
					</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		<div data-role="popup" id="groupCode" style="position:fixed; top:0px; left:0px; width:100%; height:100%;border:none;" 
  			data-dismissible="false" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false">
		<div class="header" style="text-shadow:none">
			<span id="groupName">&nbsp;</span>
		</div>
		<div id="groupCodeDiv" style="height:80%;margin-top:20px;">
			<div class="complate">
				<input type="button" data-role='none' data-ajax="false" value="清除" onclick="clearGroup();"/>
				<input type="button" data-role='none' data-ajax="false" value="完成" onclick="complateGroup();"/>
			</div>
		</div>
	</div> 
		</c:if>
	</div><!-- content -->
	<div class="bottDiv" data-role="footer" data-position="fixed" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a"><a href="#pageone" id="ui-a-a"><img align="middle" src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;</a></td>
				<td id="ui-b">&nbsp;</td>
				<td id="ui-c" onclick="JavaScript:toCheckoutGroup();"><span>我要结帐</span></td>
			</tr>
		</table>
  	</div><!-- footer -->
</div><!-- pagetwo -->
</body>
</html>