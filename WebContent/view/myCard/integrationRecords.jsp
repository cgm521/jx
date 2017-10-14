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
<title>积分</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/card.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/integrationRecords.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script language="JavaScript" type="text/JavaScript">
/*去除安卓返回及刷新按钮*/
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});
// $(function(){
// 	var ua = navigator.userAgent.toLowerCase();
// 	if(ua.match(/MicroMessenger/i)!="micromessenger") {
// 		$("body").text("请使用微信浏览器打开");
// 		return;
// 	}
// });
//查询会员卡积分记录
function showIntegration(pk_group, openid, cardNo, type){
	InitLayer();
	var url = "<c:url value='/myCard/showIntegration.do?pk_group=' />" + pk_group + "&openid=" + openid + "&cardNo=" + cardNo + "&type=" + type;
	location.href = url;
}
</script>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;" id="mainDiv"><!--页面主体-->
		<div class="account" style="padding-bottom:0px;  padding-top: 0px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr style="background-color:#FFB400;height: 60px;">
					<td class="detail3" width="33%" onclick="showIntegration('${pk_group}','${openid}','${card.cardNo}','all')">
						<div style="margin-top: 12px;">
							全部剩余<br/>
							<c:out value="${integrationRecodeSum.sy }" default="0"></c:out>
						</div>
						<div class="bottomDiv" <c:if test="${type!='all'}">style="background-color: #FFB400; "</c:if>></div>
					</td>
					<td class="detail3" width="33%"  onclick="showIntegration('${pk_group}','${openid}','${card.cardNo}','in')">
						<div style="margin-top: 12px;">
							获得<br/>
							<c:out value="${integrationRecodeSum.in }" default="0"></c:out>
							</div>
						<div class="bottomDiv" <c:if test="${type!='in'}">style="background-color: #FFB400;"</c:if>></div>
					</td>
					<td class="detail4" onclick="showIntegration('${pk_group}','${openid}','${card.cardNo}','out')">
						<div style="margin-top: 12px;">
							支出<br/>
							<c:out value="${integrationRecodeSum.out }" default="0"></c:out>
						</div>
						<div class="bottomDiv" <c:if test="${type!='out'}">style="background-color: #FFB400;"</c:if>></div>
					</td>
				</tr>
			</table>
		</div>
		<c:forEach items="${listFen}" var="fen" varStatus="status">
			<div class="fenDiv">
				<div class="dateDiv">${fen.operdate }</div>
				<div class="infoDiv" >
					<div  class="restDiv">${fen.firmdes }</div>
					<div class="resonDiv">${fen.rsn }</div>
				</div>
				<div class="recordsDiv" <c:if test="${fen.payfen<0}">style="color:red;"</c:if>><c:if test="${fen.payfen>0}">+</c:if>${fen.payfen }</div>
			</div>
		</c:forEach>
		</div>
		<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td id="ui-a">
						<a href="<c:url value='/myCard/cardInfo.do?pk_group=${pk_group}&openid=${openid }' />" data-ajax="false">
							<img src="<c:url value='/image/wechat/back.png'/>"/>&nbsp;
						</a>
					</td>
				</tr>
			</table>
	  	</div>
	</div>
<script language="JavaScript" type="text/JavaScript">
		//循环设置中间div的宽度
		$(".fenDiv").each(function(){
			$(this).find(".infoDiv").width($(window.document).width()*0.9-$(this).find(".dateDiv").width()-$(this).find(".recordsDiv").width());
		});
		//计算表头选中标志位置
		$(".bottomDiv").css("margin-left",$(window.document).width()/3*0.05);
</script>
</body>
</html>