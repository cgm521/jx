<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>订位</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/bookDeskOrder.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/css/validate.css'/>" />
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
	
	$("#overlay-screen").bind("touchmove",function(){
		return false;//去除jquery自带遮罩滑动行为
	});	
	$("#leftSft").bind("touchmove",function(){
		return false;//去除jquery自带遮罩滑动行为
	});
	
	$("textarea").focus(function(){
		$("textarea").val('');
	});
	
	$("#sendSms").css({"font-size":"12","height":"38px","background-color":"#ffb400","border":"0","border-radius":"5px","color":"#FFFFFF"});
	
	
	
	var wait=60;
	//短信验证码
	function time() {
		if (wait == 0) {
			$("#sendSms").removeAttr("disabled"); 
			$("#sendSms").val("获取验证码");
			wait = 60;
		} else {
			$("#sendSms").attr('disabled',"true");
			$("#sendSms").val(wait + "秒可重发");
			wait--;
			timeID=setTimeout(function() {
				time();
			},
			1000);
		}
	}
	
	//发送验证码点击事件
	$("#sendSms").click(function(){
		var validate1 = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'phoneNo',
				validateType:['mobile'],
				error:['手机号码格式不正确']
			},{
				type:'text',
				validateObj:'callName',
				validateType:['canNull'],
				param:['F'],
				error:['不为空']
			}]
		});
			if(validate1._submitValidate()){
				time();
				var card={};
				card["tele"]=$("#phoneNo").val();
				/*
				$.post("<c:url value='/card/sendSms.do'/>",card,function(data){
					$("#rannum").val(data);
				});
				*/
			}
	});
});
var myScroll; 
function loaded() { 
    myScroll = new iScroll('wrapper',{hScrollbar:false,vScrollbar:false}); 
} 
document.addEventListener('DOMContentLoaded', loaded, false); 
	//选择台位
	function popClick(){
		$("#storeTable").popup("open");
	}
	//选定台位
	function selectTable(roomtyp, pax, num){
		if(num<=0){
			return;
		}
		var temp = "";
		/*
		if("大厅" == roomtyp){
			temp = pax + "人桌";
		}else{
			temp = pax + "人" + roomtyp;
		}
		*/
		temp = roomtyp;
		$("#pax").val(pax);
		$("#realPax").val(pax);
		$("#roomtyp").val(roomtyp);
		$("#tableTyp").val(temp);
		$("#storeTable").popup("close");//关闭选择框
	}
	//注册下一步点击事件
	function confirmOrder(){
		var realPax = $("#realPax").val();
		var roomtyp = $("#roomtyp").val();
		var maxValue = $(document.getElementById("max"+roomtyp+realPax)).val();
		var minValue = $(document.getElementById("min"+roomtyp+realPax)).val();
		
		var validate = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'phoneNo',
				validateType:['canNull'],
				param:['F'],
				error:['手机号码不能为空']
			},{
				type:'text',
				validateObj:'phoneNo',
				validateType:['mobile'],
				error:['手机号码格式不正确']
			},{
				type:'text',
				validateObj:'callName',
				validateType:['canNull'],
				param:['F'],
				error:['不为空']
			},{
				type:'text',
				validateObj:'tableTyp',
				validateType:['canNull'],
				param:['F'],
				error:['不为空']
			},{
				type:'text',
				validateObj:'pax',
				validateType:['canNull','num','minValue','maxValue'],
				param:['F','F',minValue,maxValue],
				error:['就餐人数必填','必须为数字','就餐人数不能少于'+minValue,'就餐人数不能大于'+maxValue]
			},{
				type:'text',
				validateObj:'valicode',
				validateType:['canNull'],
				param:['F'],
				error:['不为空']
			}]
		});
		//校验
		if(validate._submitValidate()){
			InitLayer();
			var rannum=$("#rannum").val();
			var valicode=$("#valicode").val();
			if(rannum == valicode){
				var remark= $("#remark").val();
				if(remark=='您还有什么需求，请在此留言！' || remark=='' || remark==null){
					$("#remark").val('');
				}
				$(".bottDiv").hide();
				$("#order").submit();
			}else{
				closeLayer();
				alert("验证码错误");
			}
			
		}else{
			var phoneNo = $("#phoneNo");
			var phoneNoValue = phoneNo.val();
			var isMobile=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;
			if(phoneNoValue==null || phoneNoValue=="" || !isMobile.test(phoneNoValue)){ //如果用户输入的值不同时满足手机号和座机号的正则
				var t = phoneNo.offset().top;
			    $(window).scrollTop(t);
			}
			var valicode = $("#valicode");
			if(valicode.val()==null || valicode.val()==""){ //如果用户输入的值不同时满足手机号和座机号的正则
				var t = valicode.offset().top;
			    $(window).scrollTop(t);
			}
		}
	}
	function selectDateAndSft(){
		myScroll.scrollTo(0,0,100);
		$("#overlay").popup("open");
	}
	//更改日期
	function changeDate(td_on){
		if($(td_on).hasClass("td-left-on")){
			return;
		}
		$(".td-left-on").attr("class","td-left-lev");
		$(td_on).attr("class","td-left-on");
	}
	//更改餐次
	function changeSft(sftCode,sftName){
		$("#sft").val(sftCode);
		
		var dateE = $(".td-left-on").find("span").text();
		
		$("#dat").val(dateE.substr(0,10));
		
		var content = dateE.substr(0,10) + "  " + sftName;
		$("#orderDate").val(content);
		
		$("#overlay").popup("close");//关闭选择框
	}
</script>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<form id="order" method="POST" data-ajax="false" action="<c:url value='/bookDesk/confirmOrder.do' />">
	<input type="hidden" name="openid" id="openid" value="${openid}">
	<input type="hidden" name="pk_group" id="pk_group" value="${pk_group}">
	<input type="hidden" name="dat" id="dat" value="${dat}">
	<input type="hidden" name="sft" id="sft" value="${sft}">
	<input type="hidden" name="firmid" id="firmid" value="${firm.firmid}">
	<input type="hidden" name="roomtyp" id="roomtyp" value="${defaultRoomTyp}">
	<input type="hidden" name="realPax" id="realPax" value="${defaultPax}">
	<input type="hidden" name="clientID" id="clientID" value="${clientID}">
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div class="header">
			<div class="space">&nbsp;</div>
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right">
				<img alt="" src="<c:url value='/image/wechat/home.png'/>"/>&nbsp;<span class="home">${firm.firmdes}</span>
				</div>
			</div>
			<div class="space">&nbsp;</div>
		</div>
		<div class="orderBack">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span><fmt:message key="orderInfo" /></span></div>
			</div>
		</div>
		<c:set var="orderDateValue" value=""/>
		<c:choose>
			<c:when test="${sft eq '0'}"><c:set var="orderDateValue" value="全天"/></c:when>
			<c:when test="${sft eq '1'}"><c:set var="orderDateValue" value="午市"/></c:when>
			<c:when test="${sft eq '2'}"><c:set var="orderDateValue" value="晚市"/></c:when>
		</c:choose>
		<div class="orderContent">
			<table width="100%" border="0" cellspacing="0" cellpadding="4px;" align="center">
				<tr>
					<td class="td_left1"><fmt:message key="date" /></td>
					<td class="td_right">
					<input type="text" data-role="none" data-rel="popup" class="inputContentJiantou" id="orderDate" value="${dat}&nbsp;&nbsp;${orderDateValue}" readonly="readonly" onclick="selectDateAndSft();">
					<div id="overlay" data-role="popup" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
						<ul data-role="listview" data-inset="false">
							<li data-role="list-divider" style="text-align:center;"><fmt:message key="selectBookdate" /></li>
						</ul>
						<div id="sftList">
							<div id="wrapper"><!-- 日期 -->
								<c:set var="numDate" value="1" />
								<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" id="scroller">
									<c:forEach items="${listDate}" var="d">
									<tr>
									<c:choose>
										<c:when test="${numDate gt 1}">
										<td class="td-left-lev" onClick="changeDate(this);">
											<span>${d}</span>
										</td>
										</c:when>	
										<c:otherwise>
											<td class="td-left-on" onClick="changeDate(this);">
												<span>${d}</span>
											</td>
										</c:otherwise>
									</c:choose>
									</tr>
									<c:set var="numDate" value="${numDate+1}" />
									</c:forEach>
									<tr><td>&nbsp;</td></tr>
								</table>
							</div>
							<div id="leftSft"><!-- 餐次 -->
									<c:set var="numSft" value="0"/>	
									<c:forEach items="${listSft}" var="d">
									<c:choose>
									<c:when test="${numSft gt 0}">
										<div class="td-right-lev" onClick="changeSft('${listSft[numSft].code}','${listSft[numSft].name}')">
											<img src="<c:url value='/image/wechat/wanshi_d.png'/>" width='61' height='60'>
											<p>${listSft[numSft].name}</p>
										</div>
									</c:when>	
									<c:otherwise>
										<div class="td-right-on" onClick="changeSft('${listSft[numSft].code}','${listSft[numSft].name}')">
											<img src="<c:url value='/image/wechat/wushi_up.png'/>" width='61' height='60'>
											<p>${listSft[numSft].name}</p>
										</div>
									</c:otherwise>
									</c:choose>
									<c:set var="numSft" value="${numSft+1}"/>	
									</c:forEach>
							</div>
						</div>
					</div>
					</td>
				</tr>
				<tr>
					<td class="td_left1"><fmt:message key="desk" /></td>
					<td class="td_right">
						<input type="text" data-role="none" class="inputContentJiantou"  id="tableTyp" value="${defaultRoomTyp}" readonly="readonly" onclick="popClick();">
						<div id="storeTable" data-role="popup" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
							<ul data-role="listview" data-inset="false">
								<li data-role="list-divider" style="text-align:center;"><fmt:message key="selectRoomtyp" /></li>
								<c:forEach items="${listStoreTable}" var="v">
									<li style="background-color: #f7f7f7;">
										<div class="father_div" onclick="selectTable('${v.roomtyp}','${v.pax}','${v.num}');">
											<div class="table_left">
												&nbsp;&nbsp;
												<span <c:if test="${v.num <= '0'}">style="color:#717171;"</c:if>>
												<!--  
												<c:choose>
													<c:when test="${v.roomtyp eq '大厅'}">
														${v.pax}人桌
													</c:when>
													<c:otherwise>
														${v.pax}人${v.roomtyp}
													</c:otherwise>
												</c:choose>
												-->
												${v.roomtyp}
												</span>
												&nbsp;&nbsp;
											</div>
											<div class="table_right">
											<c:choose>
												<c:when test="${v.num <= '0'}">
													<img src="<c:url value='/image/wechat/bukexuan.png'/>" width="30px;" height="30px;">
												</c:when>
												<c:otherwise>
													剩余${v.num}&nbsp;&nbsp;
												</c:otherwise>
											</c:choose>
											<input type="hidden" value="${v.maxpax}" id="max${v.roomtyp}${v.pax}">
											<input type="hidden" value="${v.minpax}" id="min${v.roomtyp}${v.pax}">
											</div>
										</div>
									</li>
								</c:forEach>
							</ul>
						</div>
					</td>
				</tr>
				<tr>
					<td class="td_left1"><fmt:message key="pax" /></td>
					<td class="td_right">
						<input type="tel" data-role="none" class="inputContentJiantou" name="pax" id="pax" value="${defaultPax}">
					</td>
				</tr>
				<tr>
					<td class="td_left"><fmt:message key="orderMoney" /></td>
					<td class="orderMoney">￥&nbsp;0.00</td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
			</table>
		</div>
		<div class="orderBack">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span><fmt:message key="payTyp" /></span></div>
			</div>
		</div>
		<div class="orderContent">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="pay-left">
						<p>
							<span class="nonsupport"><fmt:message key="nonsupport" /></span><br />
							<span class="message"><fmt:message key="nonsupport_message" /></span>
						</p>
					</td>
					<td class="pay-right">
						<img src="<c:url value='/image/wechat/right.png'/>"/>
					</td>
				</tr>
			</table>
		</div>
		<div class="orderBack">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span><fmt:message key="contactWay" /></span></div>
			</div>
		</div>
		<div class="orderContent">
			<table width="100%" border="0" cellspacing="0" cellpadding="4px;" align="center">
				<tr>
					<td class="td_left"><fmt:message key="phone" /></td>
					<td class="td_right" colspan="2"><input type="tel" data-role="none" class="inputContent" name="contact" id="phoneNo" value="${tele}" ></td><!-- <c:if test="${not empty tele}">readonly="readonly"</c:if> -->
				</tr>
				<tr>
					<td class="td_left"><fmt:message key="name" /></td>
					<td class="td_right" colspan="2"><input type="text" data-role="none" class="inputContent" name="name" id="callName" value="${WeChatUser.nickname}"></td>
				</tr>
				<tr>
					<td class="td_left">验证码</td>
					<td width="40%">
						<input type="tel" data-role="none" class="inputContent" name="valicode" id="valicode" value="111111">
					</td>
					<td width="30%" style="padding-right:4%;">
						<input type="button" data-role="none"  id="sendSms" value="获取验证码">
						<input type="hidden" id="rannum" value="111111">
					</td>
				</tr>
			</table>
		</div>
		<div class="orderContent">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td style="padding-left:5%;width:90%;" colspan="3">
						<textarea name="remark" data-role="none" id="remark" rows="4" style="background-color: #FFFFFF;border:1px solid #EEEEEE;border-radius:4px;width:95%;color:#717171;">您还有什么需求，请在此留言！</textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="orderSpace">
			&nbsp;
		</div>
	</div><!-- content -->
	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false"><!--  data-role="footer" data-position="inline" data-fullscreen="true" class="footer" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a"><a href="<c:url value='/bookDesk/listFirmFromCity.do?pk_group=${pk_group}&openid=${openid}' />" data-ajax="false"><img align="middle" src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;</a></td><!-- <img src="<c:url value='/image/wechat/back.png'/>"/> -->
				<td id="ui-b">￥&nbsp;0.00</div>
				<td id="ui-c" onclick="JavaScript:confirmOrder();"><span><fmt:message key="submitOrder" /></span></div>
			</tr>
		</table>
  	</div>
  	</form>
</div><!-- page -->
</body>
</html>