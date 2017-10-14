<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><fmt:message key="titleMyWaitInfo" /></title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<link type="text/css" href='<c:url value="/css/wechat/successpage.css" />' rel="stylesheet" />
<link rel="stylesheet" href="<c:url value='/css/validate.css'/>" />
<link rel="stylesheet" href="<c:url value='/css/wechat/waitSeat.css'/>" />
<%@include file="/view/dining/jAlerts.jsp"%>
<style type="text/css">
	.wrapText{
		color:#FFFFFF;
		float:left; 
		margin-top: 5px; 
		width: 75%;
		white-space: nowrap; 
		line-height:25px;
		white-space:nowrap; 
		text-overflow:ellipsis; 
		-o-text-overflow:ellipsis; 
		overflow:hidden;
		text-align: left;
	}
	
	.firm{
	width:100%;
	text-align:center;
	padding-top:10px;
	padding-bottom:10px;
	border-radius:5px;
}
.topRadius td{
	border-radius:5px 5px 0px 0px;
}
.detail{
	background-color:#FFFFFF;
	text-align:left;
	padding:10px 10px 10px 0px;
}
</style>
<script language="JavaScript" type="text/JavaScript">
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	
	$(function(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
// 			$("body").text("请使用微信浏览器打开");
// 			return;
		}
		
		if('${isInQueue}' != "Y") {
			// 未取号，隐藏取消等位
			$("#takeSpan").parent().css("background-color","#000000");
			$("#takeSpan").hide();
		}
		
		// 定时刷新，请求BOH获取当前排队信息
		window.setInterval(function(){
			if('${isInQueue}' == "Y") {
				InitLayer();
				$.ajax({
					url:"<c:url value='/waitSeat/getQueueInfo.do'/>",
					type:"POST",
					dataType:"json",
					data:{"openId":"${openId}","firmCode":"${firm.firmCode }","pk_store":"${firm.firmid }","type":"user"},
					success:function(data){
						if(typeof data.REC != "undefined" && data.REC != "undefined") {
							$("#myNumSpan").text(data.REC + "(" + data.TBLNAME + ")");
							$("#nowNumSpan").text(data.CALLINGNO);
							$("#leftNumSpan").text(data.TBLNUM);
							$("#timeSpan").text(data.WTIME);
							closeLayer();
						} else {
							window.location.href = "<%=path %>/waitSeat/myWaitInfo.do?code=${code}&pk_group=${pk_group}&openId=${openId}";
						}
					},
					error:function(ajax){
						closeLayer();
					}
				});
				cnt = cnt + 1;
			}
		}, 10000);
	});
	
	// 弹出提示框，询问是否取消等位
	function showConfirm() {
		$("#confirmInfoDiv").popup("open");
	}
	
	// 不取消，关闭弹出框
	function cancel() {
		$("#confirmInfoDiv").popup("close");
	}
	
	// 取消等位
	function cancelWait() {
		var pk_store = "${firm.firmCode }";
		var myNum = $("#myNumSpan").text();
		InitLayer();
		$.ajax({
			url:"<c:url value='/waitSeat/cancelQueue.do'/>",
			type:"POST",
			dataType:"json",
			data:{"pk_store":pk_store, "openId":"${openId}","myNum":myNum,"pk_group":"${pk_group}","firmid":"${firm.firmid}","lineno":"${userObj.TBL }"},
			success:function(data){
				alertMsg('<fmt:message key="msgCancelWaitSuccess" />');
				window.location.href = "<%=path %>/waitSeat/myWaitInfo.do?code=${code}&pk_group=${pk_group}&openId=${openId}";
				closeLayer();
			},
			error:function(ajax){
				alertMsg('<fmt:message key="msgCancelWaitFail" />');
				closeLayer();
			}
		});
		
		$("#confirmInfoDiv").popup("close");
	}
	function getWaitNo(){
		InitLayer();
		var url = "<c:url value='/dining/listFirmFromCity.do?pk_group=${pk_group}&openid=${openId}&nextType=3' />";
		window.setTimeout(function(){
			location.href = url;
		},100);
	}
</script>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div role="main" class="ui-content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
	<c:choose>
		<c:when test="${isInQueue eq 'Y' }">
			<div class="firm" style="background-color:#F4A21A;text-align:center; margin-bottom:10px;border-radius: 0px;">
				<table width="90%" class="dataTable">
					<tr class="topRadius">
						<td class="detail"  style="padding: 0px;background-color: #F4A21A;">
							<div style="float:left;width:80%;font-size:110%;font-weight: bolder;margin-top: 6px;">
								${firm.firmdes}
							</div>
							<c:if test="${firm.addr ne null && firm.addr ne '' }">
								<div onclick="alertMsg('${firm.addr}');" style="float:left;width:10%;margin-top: 3px;">
									<img style="width:30px;height:30px;" src="<c:url value='/image/wechat/local2.png'/>"/>
								</div>
							</c:if>	
							<c:if test="${firm.tele ne null && firm.tele ne '' }">
								<div style="float:left;width:10%;">
									<a href="tel:${firm.tele }"><img style="width:30px;height:30px;" src="<c:url value='/image/wechat/dianhua1.png'/>"/></a>
								</div>
							</c:if>
						</td>
					</tr>
					<tr style="border-radius:5px 5px 0px 0px;">
					<td class="detail" style="padding: 0px;background-color: #F4A21A;"><span style="font-family: Arial;">${waitDate }</span>
						<span style="line-height:45px; font-size:100%;">
							<c:choose>
								<c:when test="${sft eq '2' }">
									<fmt:message key="dinnerSession" />
								</c:when>
								<c:otherwise>
									<fmt:message key="lunchSession" />
								</c:otherwise>
							</c:choose>
						</span></td>
					</tr>
				</table>
			</div>
		</c:when>
		<c:otherwise>
			<div class="top">
				<div class="contentMessage">
					<span style="font-size: 120%;">您当前没有等位信息</span>
				</div>
				<div class="father_div">
					<div>
						<div class="content">等位号：无</div>
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
		<div id="userWaitInfo" style="background-color:#FFFFFF; text-align:center;">
			<c:choose>
				<c:when test="${isInQueue eq 'Y' }">
			<table id="userWaitInfoTable" class="dataTable">
				<tr>
					<td class="colLabel" style="border-bottom: 1px solid #ddd;"><fmt:message key='labelMyWaitNumber' /></td>
					<td class="colData redWord"  style="border-bottom: 1px solid #ddd;"><span id="myNumSpan" style="font-family:Arial;">${userObj.REC }(${userObj.TBLNAME })</span></td>
				</tr>
				<tr>
					<td class="colLabel"  style="border-bottom: 1px solid #ddd;"><fmt:message key='labelNowCalledNum' /></td>
					<td class="colData redWord"  style="border-bottom: 1px solid #ddd;"><span id="nowNumSpan" style="font-family:Arial;">${userObj.CALLINGNO }</span></td>
				</tr>
				<tr>
					<td class="colLabel"  style="border-bottom: 1px solid #ddd;"><fmt:message key='labelWaitTables' /></td>
					<td class="colData"  style="border-bottom: 1px solid #ddd;"><span id="leftNumSpan" style="font-family:Arial;">${userObj.TBLNUM }</span><fmt:message key='labelTableUnit' /></td>
				</tr>
				<tr>
					<td class="colLabel"  style="border-bottom: 1px solid #ddd;"><fmt:message key='labelExpectWaitTime' /></td>
					<td class="colData"  style="border-bottom: 1px solid #ddd;">
						<fmt:message key='labelExpect' /><span id="timeSpan" style="font-family:Arial;">${userObj.WTIME }</span><fmt:message key='labelWaitMinute' />
					</td>
				</tr>
			</table>
			<div id="confirmInfo" >
            	
        	</div>
				</c:when>
				<c:otherwise>
					<div class="bottonDiv" style="margin-top: 60px;">
						<p>
							<input type="button" value="马上取号" data-role='none' data-ajax="false" onclick="javaScript:getWaitNo();"> 
						</p>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div><!-- content -->
	<div class="bottDiv" data-role="footer" data-position="fixed" data-update-page-padding="true" data-tap-toggle="false"> <!-- data-tap-toggle="false" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="ui-a">
					<%-- <a href="#pageone" data-rel="back">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a> --%>
				</td>
				<td class="ui-c"><span id="takeSpan" style="font-size:130%" onclick="JavaScript:showConfirm();"><fmt:message key="btnCancelWait" /></span></div>
			</tr>
		</table>
		<div data-role="popup" id="confirmInfoDiv" style="position:fixed; top:30%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false"  data-history="false">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr height="45px">
					<td align="center"><span style="font-size:120%"><fmt:message key='msgConfirmCancelWait' /></span></td>
				</tr>
				<tr height="50px">
					<td class="popBtnCol">
						<input type="button" value="<fmt:message key='btnCancel' />" data-role='none' data-ajax="false" onclick="javaScript:cancel()" /> 
						<input type="button" value="<fmt:message key='btnOK' />" data-role='none' data-ajax="false" onclick="javaScript:cancelWait();" /> 
					</td>
				</tr>
			</table>
		</div>
  	</div>
</div><!-- page -->
</body>
</html>