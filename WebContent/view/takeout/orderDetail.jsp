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
<title>我的外卖单</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/orderDetail.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/takeout/address.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/validate.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jweixin-1.0.0.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/qqmap.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script language="JavaScript" type="text/JavaScript">
var tableId= "";
// 手机型号 0：安卓；1：苹果；2：winphone
var phoneType = "0";
var orderState = "${orderDetail.state}";

var myScrollCoupon;
var myScrollSft; 
function loaded() {
	setTimeout(function(){
		myScrollCoupon = new iScroll("listCouponDiv",{hScrollbar:false,vScrollbar:false});
		myScrollSft = new iScroll("wrapperSfts",{hScrollbar:false,vScrollbar:false});
	},100);
} 
window.addEventListener("load",loaded,false);

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
	
	if("${pageFrom}" == "orderDetail" || "${orderDetail.state}"!="0") {
		//$("#cancelBtn").hide();
		//$("#scanTable").text("");
		$("#scanTable").text("菜品评价");
		//$("#scanTable").css("background-color", "#000000");
		//$("#scanTable").removeClass("scanToOrders");
		$("#headDiv").removeClass("scanToOrders");
		$("#headDiv").find("span").eq(0).text("祝您用餐愉快");
	}
			
	if("${hasAskCheckOut}" == "Y") {
		// 如果已经点击过我要结帐，隐藏加菜按钮
		$("#addMenuBtn").hide();
		$("#selectCouponBtnTr").hide();
		$(".orderStateImg").css("margin-top","245px");
	}
	
	// 根据配置文件判断是否展示开发票选项
	if('<%=Commons.getConfig().getProperty("showInvoiceTitle") %>' == 'N') {
		$("#invoiceTitleDiv").hide();
		//$("#invoiceTitleDiv").prev().hide();
	}
	
	if("${mustAddAddress}"=="Y"){//没有地址先显示pagetwo
		$("#gotoPagetwo").click();
		isAddNew();
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
	var url = "<c:url value='/bookDesk/cancelOrder.do?pk_group=' />" + pk_group + "&orderid=" + ordersid + "&orderType=2&firmid=${orderDetail.firmid}&state=${orderDetail.state}&vtransactionid=${orderDetail.vtransactionid}&paymoney=${orderDetail.paymoney}";
	/* window.setTimeout(function(){
		location.href = url;
	},100); */

	$.post(url, {}, function(data){
		window.location.href = "<c:url value='/takeout/orderDetail.do?pk_group=' />" + pk_group 
				+ "&orderid=" + ordersid + "&openid=${openid}";
	});
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
	InitLayer();
	$("#addMenuBtn").hide();
	$("#selectCouponBtnTr").hide();
	$(".orderStateImg").css("margin-top","245px");
	location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&" + 
			"redirect_uri=<%=basePath %>/wxPay/toCheckout.do?" + "couponStr=" + couponStr + "&id=${orderDetail.id}&vcode=${firm.firmCode}" + 
			"&resv=${orderDetail.resv}&firmid=${orderDetail.firmid}&pax=${orderDetail.pax}&dat=${orderDetail.dat}" + 
			"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
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
	url += "&pageFrom=takeout"
	
	InitLayer();
	window.setTimeout(function(){
		location.href = url;
	},100);
}

// 下单后，增加菜品
function addMenu() {
// 	alert("1");
	var url = "<%=path %>/bookMeal/gotoMenu.do?openid=${openid}&pk_group=${pk_group}&firmid=${orderDetail.firmid}&orderid=${orderDetail.id}&type=add&pageFrom=takeout";
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

//更换地址
function changAddress(name,tele,address){
	$("#message_contentName").text(name);
	$("#message_tele").text(tele);
	$("#message_address").text(address);
}

function dingwei(){
	if("${baidu_address}"==null || "${baidu_address}"==""){
		alertMsg("获取坐标失败，请手动添加地址");
	}else{
		$("#new_address").val("${baidu_address}");
	}
}

function changSelectMeal(type,tr){
	var td = $(tr).find("td:eq(0)");
	if(td.hasClass("mealLeftSelect")){
		return;
	}
	$("#payType").val(type);
	var selectB = $(".time").find(".mealLeftSelect");
	selectB.removeClass("mealLeftSelect");
	selectB.addClass("mealLeft");
	selectB.next(".mealRight").find("img").attr("src", "<c:url value='/image/wechat/noselect.png'/>");
	td.addClass("mealLeftSelect");
	td.removeClass("mealLeft");
	td.next(".mealRight").find("img").attr("src", "<c:url value='/image/wechat/select.png'/>");

	if(type=='0'){//在线支付时显示电子券
		//$("#selectCouponBtnTr").show();
		$(".orderStateImg").css("margin-top","245px");
		$("#message_pay").text("在线支付");
		$("#invoiceTitleDiv").hide();
	}else{
		$("#selectCouponBtnTr").hide();
		$("#message_pay").text("货到付款");
		// 根据配置文件判断是否展示开发票选项
		if('<%=Commons.getConfig().getProperty("showInvoiceTitle") %>' == 'Y') {
			$("#invoiceTitleDiv").show();
		}
	}
}

// 初始化地图
var center = new qq.maps.LatLng(39.916527,116.397128);
var map = new qq.maps.Map(document.getElementById('container'),{
    center: center,
    zoom: 13
});

//是否在配送范围内标识
var checkRes = false;
var path, polygon = null;

function checkPosition() {
	var address = $("#new_address").val();
	if(address == '' || address == null) {
		address = $("#message_address").text()
	}
	
	//如果有多个配送区域，需要循环每个配送区域，判断是否在配送区域内
	<c:forEach items="${listStoreRange}" varStatus="ss" var="sr">
		if(checkRes == false) {
			path = [
					<c:forEach items="${sr.listRangeCoordi}" varStatus="rs" var="rc">
						new qq.maps.LatLng('${rc.vlat}', '${rc.vlng}'),
					</c:forEach>
			];
			
			polygon = new qq.maps.Polygon({
				path:path,
				strokeColor: '#000000',
				strokeWeight: 5,
				fillColor: '#111111',
				map: map
			});
			 
			//通过getLocation();方法获取位置信息值
			new qq.maps.Geocoder({
			    complete : function(result){
					// 判断坐标点是否在外送范围内
					checkRes = polygon.getBounds().contains(result.detail.location);
			    },
			    //若服务请求失败，则运行以下函数
			    error: function() {
			    }
			}).getLocation(address);
		}
	</c:forEach>
}

function xiadan(){
	if(orderState!="1" && orderState!="2"){
		//进入评价页面
		var uri = "<%=path %>/evaluate/takeOutEvaluate.do?openid=${openid}&pk_group=${pk_group}&ordersId=${orderDetail.id}";
		window.location.href = uri;
		return;
	}
	
	//校验地址是否在配送范围内
	checkPosition();
	
	window.setTimeout(function(){
		if(checkRes == false) {
			alertMsg("您选择的地址不在门店配送范围！");
			return;
		}
		
		if("${empty time_list}"=="true"){
			alertMsg("不在送餐时间段内");
			return;
		}
		if($("#visopeninvoice").attr("data-cacheval")=="false" && $("#vinvoicetitle").val()==""){
			alertMsg("请填写发票抬头！");
			return;
		}
		InitLayer();
		window.setTimeout(function(){
		var payType = $("#payType").val();
		var para={};
		para["openid"] = "${openid}";
		para["orderid"] = "${orderDetail.id}";
		para["addr"] = $("#message_address").text().replace(/(^\s*)|(\s*$)/g, "");
		para["name"] =  $("#message_contentName").text().replace(/(^\s*)|(\s*$)/g, "");
		para["tele"] =  $("#message_tele").text().replace(/(^\s*)|(\s*$)/g, "");
		para["payType"] = payType;//支付方式
		para["datmin"] = $("#datmin").val();//送餐时间
		para["vinvoicetitle"] = $("#vinvoicetitle").val();//发票抬头
		para["visopeninvoice"]=($("#visopeninvoice").attr("data-cacheval")=="false"?"Y":"N");//是否开发票
		para["firmid"] = "${orderDetail.firmid}";
		para["sumprice"] = "${payMoney}";
		if(payType == '0'){//在线支付
			if(orderState=="0"){//未下单
				$.ajaxSetup({async:false});
				$.post("<c:url value='/takeout/saveTakeOutOrders.do' />", para, function(data){
					if($.trim(data)){
						if(data=="TRUE"){
							var url = "<%=basePath %>/wxPay/toOrderPay.do?pk_group=${pk_group}&firmid=${orderDetail.firmid}&resv=${orderDetail.resv}&sumprice=${payMoney}&pax=${orderDetail.pax}&id=${orderDetail.id}&isfeast=2&code=${code}&openScan=1&billstate=2";
							location.href = url;
						}else{
							alertMsg("下单失败，请稍后重试");
						}
					}
				});
			}
		}else if(payType == '1'){//货到付款
			if(orderState=="0"){//0 未下单
				InitLayer();
				$.ajaxSetup({async:false});
				$.post("<c:url value='/takeout/saveAndSendOrders.do' />", para, function(data){
					if($.trim(data)){
						if(data=="TRUE"){
							var url = "<%=basePath %>/takeout/orderDetail.do?pk_group=${pk_group}&firmid=${orderDetail.firmid}&openid=${openid}&orderid=${orderDetail.id}";
							location.href = url;
						}else{
							alertMsg("下单失败，请稍后重试");
						}
					}
				});
				return;
			}
		}
		},100);
	}, 500);
}

function changeSftPopup(td,time){
	if($(td).hasClass("popupSelect")){
		return;
	}
	$("#scrollerSfts").find(".popupSelect").addClass("popupNoSelect");
	$("#scrollerSfts").find(".popupSelect").removeClass("popupSelect");
	$(td).removeClass("popupNoSelect");
	$(td).addClass("popupSelect");
}

function confirmSelectDatMin(){
	var time = $("#scrollerSfts").find(".popupSelect").text();
	var result = time.replace(/(^\s*)|(\s*$)/g, "");
	if(result.indexOf(":")<0){
		$("#datmin").val("${default_time}");
	}else{
		$("#datmin").val(result);
	}
	$("#time_value").text(result);
	$('#sfts').popup('close');
}
//修改发票抬头输入框的编辑状态
function changInput(){
	if($("#visopeninvoice").attr("data-cacheval")=="false"){
		$("#vinvoicetitle").removeAttr("readonly");
		$(".ui-input-text").css("border-color","black");
	}else{
		$("#vinvoicetitle").attr("readonly","readonly");
		$(".ui-input-text").css("border-color","#F7F7F7");
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
<div id="container" style="display:none;"></div>
<div data-role="page" id="pageone" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div id="headDiv" class="header scanToOrders" style="background-color:#d83322">
			<img src="<c:url value='/image/wechat/laba.png'/>" /><span>欢迎使用外卖业务</span>
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
					<td class="detail" style="padding-top: 5px;padding-bottom: 0px;"><span style="font-family: Arial;color:#656565;font-size: 100%;">${orderDetail.orderTimes}</span></td>
					<td class="detail" style="width:30%;text-align: right;vertical-align: bottom;padding-top: 5px;padding-bottom: 0px;" rowspan="2">
						<span>账单金额￥</span>
						<span style="font-family: Arial; padding-right:10px;">${totalPrice }</span>
					</td>
				</tr>
				<tr>
					<td class="detail" style="width:70%;padding-top: 5px;padding-bottom: 0px;">订单类型:&nbsp;外卖</td>
				</tr>
				<tr>
					<td class="detail" colspan="2" style="width:70%;padding-top: 5px;padding-bottom: 0px;">优惠信息</td>
				</tr>
				<c:forEach items="${listActm}" var="item" varStatus="stat">
					<tr>
						<td class="detail"><span style="padding-left:20px; font-size:80%; color:red;">${item.vname }</span></td>
						<td class="detail"></td>
					</tr>
				</c:forEach>
				<tr>
					<td class="detail" style="padding-top: 5px;padding-bottom: 0px;"></td>
					<td class="detail" style="width:30%;text-align: right;vertical-align: bottom;padding-top: 5px;padding-bottom: 0px;">
						<span style="font-size:12px;">应付金额￥</span>
						<span style="font-family: Arial;font-size: 150%;color: red; padding-right:10px;">${payMoney }</span>
					</td>
				</tr>
			</table>
		</div>
		<c:if test="${orderDetail.state le '2'}">
		<div class="order" id="buttDivTot">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="bottomRadius" id="cancelBut">
					<td class="detail2" style="padding: 0px;" id="buttonDivtd" colspan="2">
 						<c:if test="${orderDetail.state le '2' and orderDetail.state != '7'}">
							<p>
								<c:if test="${orderDetail.state ne '7'}">
									<input type="button" id="cancelBtn" style="width:40%;background-color:#D83322;" value="取消订单" data-role='none' data-ajax="false" onclick="javaScript:showConfirm()">
								</c:if>
								<c:if test="${orderDetail.state eq '0'}">
									<input type="button" style="width:40%;	background-color:#D83322;" value="修改菜品" data-role='none' data-ajax="false" onclick="javaScript:modifyMenu()">
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
 						</c:if>
 						<c:if test="${orderDetail.state eq '7'}">
							<p>
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
		</c:if>
		<c:if test="${orderDetail.state eq '0'}">
		 <div class="time">
		 	<input type="hidden" name="payType" id="payType" value="0" />
        	<table>
        		<tr id="selectCouponBtnTr" onclick="javaScript:toSelectCouponPage()" style="display:none">
					<td class="detail" style="border-bottom:1px solid #EEEEEE;">
						选择电子券&nbsp;&nbsp;<span id="selectCouponHead" style="color:red; font-size:70%;">您已经选择0张电子券</span>
					</td>
					<td class="detail6" style="border-bottom:1px solid #EEEEEE;">
						<img width="15" height="15" src="<%=path %>/image/wechat/arrow_right.png">
					</td>
				</tr>
        		<tr height="50px" onclick="changSelectMeal('0',this);">
        			<td class="mealLeftSelect  timeTop" >
        				在线支付
        			</td>
        			<td class="mealRight  timeTop">
        				<img src="<c:url value='/image/wechat/select.png'/>"/>
        			</td>
        		</tr>
        		<tr height="50px" onclick="changSelectMeal('1',this);"style="display:none">
        			<td class="mealLeft">
        				货到付款
        			</td>
        			<td class="mealRight">
        				<img src="<c:url value='/image/wechat/noselect.png'/>"/>
        			</td>
        		</tr>
        	</table>
        </div>
        </c:if>
        <div class="orderStateImg" style="margin-top: 245px;">
        	<c:if test="${orderDetail.state == 0}">
				<img src="<c:url value='/image/wechat/weitijiao.png'/>"/>
			</c:if>
			<c:if test="${orderDetail.state == 1 or orderDetail.state == 2}">
				<img src="<c:url value='/image/wechat/yixiadan.png'/>"/>
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
        <div class="order"><!-- 外卖信息 -->
			<table style="background-color:#FFFFFF">
				<tr style="border-bottom:1px solid #eeeeee;" <c:if test="${orderDetail.state eq 0}">onclick="$('#gotoPagetwo').click();"</c:if>>
					<td class="detail">
                    	我的外卖信息<span style="color:#d83322">(点我更改外送地址)</span>
               		</td>
                	<td class="detail6">
                   		<img width="15" height="15" src="<%=path %>/image/wechat/arrow_right.png">
                	</td>
				</tr>
				<c:choose>
					<c:when test="${orderDetail.state == 0}">
						<tr>
							<td colspan="2" class="messageLeft">姓名：<span id="message_contentName">${address_list[0].contentName}</span></td>
						</tr>
						<tr>
							<td colspan="2" class="messageLeft">电话：<span id="message_tele">${address_list[0].tele}</span></td>
						</tr>
						<tr>
							<td colspan="2" class="messageLeft">地址：<span id="message_address">${address_list[0].address}</span></td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="2" class="messageLeft">姓名：<span id="message_contentName">${orderDetail.name}</span></td>
						</tr>
						<tr>
							<td colspan="2" class="messageLeft">电话：<span id="message_tele">${orderDetail.contact}</span></td>
						</tr>
						<tr>
							<td colspan="2" class="messageLeft">地址：<span id="message_address">${orderDetail.addr}</span></td>
						</tr>
						<tr>
							<td colspan="2" class="messageLeft">
								送餐时间：<span id="message_time">
										<c:choose>
											<c:when test="${fn:endsWith(orderDetail.datmins,'00') or fn:endsWith(orderDetail.datmins,'30')}">${orderDetail.datmins}</c:when>
											<c:otherwise>立即配送</c:otherwise>
										</c:choose>
										
									</span>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="messageLeft">
								支付方式：<span id="message_pay">
										<c:choose>
											<c:when test="${orderDetail.payment == '2'}">在线支付</c:when>
											<c:otherwise>货到付款</c:otherwise>
										</c:choose>
									</span>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
		</div>
		<div class="order" id="invoiceTitleDiv" style="display:none;">
			<table style="background-color:#FFFFFF">
				<tr style="border-bottom:1px solid #eeeeee;" <c:if test="${orderDetail.state eq 0}">onclick="$('#gotoPagetwo').click();"</c:if>>
					<td class="detail">
                    	发票信息
               		</td>
                	<td class="detail6">
                   		&nbsp;
                	</td>
				</tr>
				<tr>
					<td colspan="2" class="messageLeft">
						<input type="checkbox" id="visopeninvoice" value="Y" onclick="changInput();" />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="messageLeft">
						发票抬头：<input type="text" id="vinvoicetitle" name="vinvoicetitle" value="${vinvoicetitle}" readonly="readonly" class="inputContent" data-role="none" style="border-color: #656565;width:80%;"/>
					</td>
				</tr>
			</table>
		</div>
		<c:if test="${orderDetail.state eq 0}">
		<div class="order"><!-- 外卖信息 -->
			<table style="background-color:#FFFFFF">
				<tr style="border-bottom:1px solid #eeeeee;" onclick="$('#sfts').popup('open');">
					<td class="detail">
                    	送餐时间&nbsp;<span id="time_value" style="color:#ddd;font-size:90%;">立即配送</span>
                    	<input type="hidden" id="datmin" name="datmin" value="${default_time}"/>
               		</td>
                	<td class="detail6">
                   		<img width="15" height="15" src="<%=path %>/image/wechat/arrow_right.png">
                	</td>
				</tr>
			</table>
		</div>
		<div id="sfts" class="mypopup" data-role="popup" data-corners="true" data-shadow="false" data-overlay-theme="b" data-history="false"><!-- 选择sft -->
        		<div class="popupTitle">
               		<table>
            			<tr>
            				<td>选择送餐时间</td>
            			</tr>
            		</table>
                </div>
        		<div id="wrapperSfts" class="mypopupScroller">
					<table id="scrollerSfts">
                    	<tr><td>&nbsp;</td></tr>
						<c:forEach items="${time_list}" var="s" varStatus="stat">
							<tr>
							<c:choose>
								<c:when test="${default_time eq s}">
									<td id="TD_SFT_${stat.count}" class="popupSelect" onclick="changeSftPopup(this,'${s}');">
								</c:when>
								<c:otherwise>
									<td id="TD_SFT_${stat.count}" class="popupNoSelect" onclick="changeSftPopup(this,'${s}');">
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${stat.count>1}">
									${s}
								</c:when>
								<c:otherwise>立即配送</c:otherwise>
							</c:choose>
								</td>
							</tr>
						</c:forEach>
                        <tr><td>&nbsp;</td></tr>
					</table>
				</div><!-- wrapper -->
				<div class="popupComplate">
                   	<table>
            			<tr>
            				<td>
            					<input type="button" style="background-color:#D83322; color:#FFFFFF" value="取消" data-role='none' data-ajax="false" onclick="javaScript:$('#sfts').popup('close');" /> 
								<input type="button" style="background-color:#D83322; color:#FFFFFF" value="确认" data-role='none' data-ajax="false" onclick="javaScript:confirmSelectDatMin();" /> 
            				</td>
            			</tr>
            		</table>
                </div>
        	</div><!-- sfts -->
        </c:if>
		<div class="spaceMeaage" style="padding-left: 10px;">
		<c:set var="orderDtlListSize" value="${fn:length(orderDtl)}"/>
			我的菜单，共<span style="font-family: Arial;">${orderDtlListSize }</span>条点菜记录
		</div>
		<div class="order">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="background-color:#FFFFFF">
				<c:forEach items="${orderDtl }" var="food" varStatus="stat">
					<tr height="30px">
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;font-size:13px;font-family: Arial;"  width="10%">${stat.count }</td>
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align:left;" width="60%" >
							<div><span style='width:100%; text-align:left; font-size:14px;'>${food.foodsname }</span></div>
							<div><span style='width:100%; color:gray; text-align:left; font-size:12px;font-family: Arial;'>￥${food.price }</span></div>
							<c:if test="${fn:length(food.listDishAddItem) > 0 }">
								<div><span style='width:100%; color:gray; text-align:left; font-size:12px;'>附加项:
									<c:forEach items="${food.listDishAddItem }" var="addItem">
										${addItem.redefineName }
										<c:if test="${addItem.nprice > 0.0 }">
											<span style="color:red">(${addItem.nprice }元)</span>
										</c:if>&nbsp;
									</c:forEach>
								</span></div>
							</c:if>
							<c:if test="${fn:length(food.listDishProdAdd) > 0 }">
								<div><span style='width:100%; color:gray; text-align:left; font-size:12px;'>附加产品:
									<c:forEach items="${food.listDishProdAdd }" var="addItem">
										${addItem.prodAddName }
										<c:if test="${addItem.nprice > 0.0 }">
											<span style="color:red">(${addItem.nprice }元)</span>
										</c:if>&nbsp;
									</c:forEach>
								</span></div>
							</c:if>
							<c:if test="${food.remark ne null && food.remark ne '' }">
								<div><span style='width:100%; color:gray; text-align:left; font-size:12px;'>备注:${food.remark }</span></div>
							</c:if>
						</td>
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;"width="10%">
							<span style="color:#D83322; font-size:14px;font-family: Arial;">×${food.foodnum }</span>
						</td>
						<td class="detail"  style="padding-top: 5px;padding-bottom: 0px;text-align: right;"width="20%">
							<span style="color:#D83322; font-size:14px;font-family: Arial;">￥${food.totalprice }</span>
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
					<a id="gotoPagetwo" href="#pagetwo"/>
					<c:choose>
						<c:when test="${pageFrom eq 'orderDetail' }">
							<a href="<c:url value='/bookDesk/orderDetail.do?pk_group=${pk_group}&openid=${openid }&orderid=${orderDetail.bookDeskOrderID }&firmid=${orderDetail.firmid }&orderType=1' />" data-ajax="false">
								<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
							</a>
						</c:when>
						<c:otherwise>
							<a href="<c:url value='/bookDesk/findTakeOutOrders.do?pk_group=${pk_group}&openid=${openid }' />" data-ajax="false">
								<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
							</a>
						</c:otherwise>
					</c:choose>
				</td>
				<td style="background-color: #D83322;color: white;width:25%;text-align: center;" class="scanToOrders" id="scanTable" onclick="xiadan();">
					确定下单
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
						<input type="button" style="background-color:#D83322; color:#FFFFFF" value="不使用电子券" data-role='none' data-ajax="false" onclick="javaScript:cancelSelect();" /> 
						<input type="button" style="background-color:#D83322; color:#FFFFFF" value="确认选择" data-role='none' data-ajax="false" onclick="javaScript:confirmSelect();" /> 
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
		$(".orderStateImg").css("margin-top","43px");
	}
	function isAddNew(){
		if($("#addnew").is(":visible")){
			$("#isAddNew").val('0');
			var tr = $(".dingwei_white_nopadding").find("div:eq(0)");
			var select_tr = $(".address_detail_color_select").length;
			if(tr.length>0 && select_tr<=0){
				tr.removeClass("address_detail_color");
				tr.addClass("address_detail_color_select");
				$("#address_id").val(tr.attr("id").substring(8));
			}
		}else{
			$("#isAddNew").val('1');
			$(".address_detail_color_select").addClass("address_detail_color");
			$(".address_detail_color_select").removeClass("address_detail_color_select");
		}
		$("#addnew").toggle();
	}
	function selectAddress(index){
		var tr = $("#address_"+index);
		if(tr.hasClass("address_detail_color_select")){
			return;
		}
		$(".address_detail_color_select").find("img").attr("src","<c:url value='/image/wechat/noselect.png'/>");
		tr.find("img").attr("src","<c:url value='/image/wechat/xuanze.png'/>");
		
		$(".address_detail_color_select").addClass("address_detail_color");
		$(".address_detail_color_select").removeClass("address_detail_color_select");
		tr.removeClass("address_detail_color");
		tr.addClass("address_detail_color_select");
		$("#address_id").val(index);
		if($("#addnew").is(":visible")){
			isAddNew();
		}
	}
	function saveAddress(){
		var isAddNew = $("#isAddNew").val();
		if(isAddNew=='1'){//新增
			var validate = new Validate({
				validateItem:[{
					type:'text',
					validateObj:'new_phoneNo',
					validateType:['canNull'],
					param:['F'],
					error:['手机号码不能为空']
				},{
					type:'text',
					validateObj:'new_phoneNo',
					validateType:['mobile'],
					error:['手机号码格式不正确']
				},{
					type:'text',
					validateObj:'new_name',
					validateType:['canNull'],
					param:['F'],
					error:['不能为空']
				},{
					type:'text',
					validateObj:'new_address',
					validateType:['canNull'],
					param:['F'],
					error:['不能为空']
				},{
					type:'text',
					validateObj:'new_address',
					validateType:['maxLength'],
					param:['100'],
					error:['最长为100个字符']
				}]
			});
			//校验
			if(validate._submitValidate()){
				var name = $("#new_name").val();
				var phoneNo = $("#new_phoneNo").val();
				var address = $("#new_address").val();
				
				InitLayer();
				$.ajax({
					url:"<c:url value='/takeout/saveAddress.do'/>",
					type:"POST",
					dataType:"html",
					data:{"name":name,"phoneNo":phoneNo,"address":address,"openid":"${openid}"},
					success:function(html){
						if(html == "FALSE"){
							alertMsg("地址保存失败，请稍后重试");
							return false;
						}
						$("#address_id").val(html);
						$("#isAddNew").val('0');
						$("#addnew").hide();
						$("#noHaveAddress").hide();
						//$(".address_detail_color_select").addClass("address_detail_color");
						//$(".address_detail_color_select").removeClass("address_detail_color_select");
						
						var htmlStr = "<div class='address_detail address_detail_color_select' id='address_"+html+"' onclick=\"selectAddress('"+html+"');\">"
									+ "<table><tr><td class='td_left left_width'>"
									+ "<div><span>"+name+"</span>&nbsp<span>"+phoneNo+"</span></div>"
									+ "<div class='font_small'>"+address+"</div></td>"
									+"<td class='td_right right_width'><img src='"+"<c:url value='/image/wechat/xuanze.png'/>"+"'/></td>"
									+"</tr></table></div>";
						
						$(".dingwei_white_nopadding").append(htmlStr);//填充新内容
						changAddress(name,phoneNo,address);
						$("#new_name").val('');
						$("#new_phoneNo").val('');
						$("#new_address").val('');
						closeLayer();
						$("#ui-a-a").click();
					},
					error:function(ajax){
						closeLayer();
						alertMsg("error");
					}
				});
			}
		}else{
			var tr = $(".address_detail_color_select");
			if(tr.length <= 0){
				alertMsg("请先选择地址或新增");
				return;
			}
			var name = tr.find("span:eq(0)").text();
			var tele = tr.find("span:eq(1)").text();
			var address = tr.find(".font_small").text();
			changAddress(name,tele,address);
			$("#ui-a-a").click();
		}
	}
</script>
<div data-role="page" id="pagetwo" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="width:100%;overflow:hidden;margin:0;list-style:none;padding:0;">
    	<div class="dingwei_white font_red">
			<table>
				<tr onclick="dingwei();">
					<td class="td_left">
						定位当前位置
					</td>
					<td class="td_right">
						<img src="<c:url value='/image/wechat/dingwei.png'/>"/>
					</td>
				</tr>
             </table>
		</div>
		<div class="dingwei_white div_top" onclick="isAddNew();">
			<table>
				<tr>
					<td class="td_left">
						新增送餐信息
						<input type="hidden" id="isAddNew" value="0"/>
					</td>
					<td class="td_right">
						<img src="<c:url value='/image/wechat/tianjia.png'/>"/>
					</td>
				</tr>
			</table>
		</div>
		<div id="addnew" style="background-color:#FFFFFF;display:none;">
			<table>
			
							<tr>
								<td class="timeTopLeft timeTop">姓名</td>
                       			<td id="bookRoomTyp" class="timeTopRight timeTop">
                        			<input type="text" name="new_name" id="new_name" value="" class="inputContent" data-role="none" style="border-color: #fff;"/>
                        		</td>
							</tr>
							<tr>
								<td class="timeTopLeft timeTop">电话</td>
                       			<td id="bookRoomTyp" class="timeTopRight timeTop">
                        			<input type="tel" name="new_phoneNo" id="new_phoneNo" value="" class="inputContent" data-role="none" style="border-color: #fff;"/>
                        		</td>
							</tr>
							<tr>
								<td class="timeTopLeft timeTop">地址</td>
                       			<td id="bookRoomTyp" class="timeTopRight timeTop">
                        			<input type="text" name="new_address" id="new_address" value="" class="inputContent" data-role="none" placeholder="可点击上方的“定位当前位置”获取" style="border-color: #fff;"/>
                        		</td>
							</tr>
			</table>
		</div>
		<div class="history_address">
			历史送餐信息
		</div>
        <div class="dingwei_white_nopadding">
        	<c:set var="index" value="0"/>
        	<input type="hidden" id="address_id" value="${address_list[0].pk_id}"/>
        	<c:forEach items="${address_list}" var="a">
        		<c:choose>
        			<c:when test="${index > 0}">
        				<div class="address_detail address_detail_color" id="address_${a.pk_id}" onclick="selectAddress('${a.pk_id}');">
        					<table>
        						<tr>
        							<td class="td_left left_width">
        								<div><span>${a.contentName}</span>&nbsp<span>${a.tele}</span></div>
        								<div class="font_small">${a.address}</div>
        							</td>
        							<td class="td_right right_width"><img src="<c:url value='/image/wechat/noselect.png'/>"/></td>
        						</tr>
        					</table>
        				</div>
        			</c:when>
        			<c:otherwise>
        				<div class="address_detail address_detail_color_select" id="address_${a.pk_id}" onclick="selectAddress('${a.pk_id}');">
        					<table>
        						<tr>
        							<td class="td_left left_width">
        								<div><span>${a.contentName}</span>&nbsp<span>${a.tele}</span></div>
        								<div class="font_small">${a.address}</div>
        							</td>
        							<td class="td_right right_width"><img src="<c:url value='/image/wechat/xuanze.png'/>"/></td>
        						</tr>
        					</table>
        				</div>
        			</c:otherwise>
        		</c:choose>
        		<c:set var="index" value="${index+1}"/>
        	</c:forEach>
        	<c:if test="${index<=0}">
        		<span id="noHaveAddress" style="color:#ddd;font-size:80%;padding:10px;text-align:center;">还未添加过地址</span>
        	</c:if>
   </div>
   <div class="bottDiv" data-role="footer" data-position="fixed" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a"><a href="#pageone" id="ui-a-a"><img align="middle" src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;</a></td>
				<td id="ui-b">&nbsp;</td>
				<td id="ui-c" onclick="JavaScript:saveAddress();"><span>确定</span></td>
			</tr>
		</table>
  	</div><!-- footer -->
</body>
</html>