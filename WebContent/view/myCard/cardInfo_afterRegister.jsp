<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons" %>
<%String path=request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<title>吉祥馄饨_会员导航1</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/cardInfo.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<style>

</style>
<script type="text/JavaScript">
/*去除安卓返回及刷新按钮*/
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});
$(function(){
	var ua = navigator.userAgent.toLowerCase();
	/* if(ua.match(/MicroMessenger/i)!="micromessenger") {
		$("body").text("请使用微信浏览器打开");
		return;
	} */
});
$(document).ready(function(){ 
	alertMyselfShareCode()
})
function alertMyselfShareCode(){
	alertMsg("您的推荐码为：${card.myselfShareCode}");
}
function addCard(pk_group, openid){
	InitLayer();
	var url = "<c:url value='/myCard/addCard.do?pk_group=' />" + pk_group + "&openid=" + openid;
	location.href = url;
}
//查询会员卡积分记录
function showIntegration(pk_group, openid, cardNo){
	InitLayer();
	var url = "<c:url value='/myCard/showIntegration.do?pk_group=' />" + pk_group + "&openid=" + openid + "&cardNo=" + cardNo+"&type=all";
	location.href = url;
}

// 页面跳转
function openPage(tr,url){
	$(tr).find("td").css("background-color","#eeeeee");
	window.setTimeout(function(){
		InitLayer();
		window.setTimeout(function(){
			location.href = url;
		}, 100);
	},500);
}
//显示积分页面
function showJf_or_CardPage(){
	document.title="吉祥馄饨_积分"; 
	$(document.body).attr("class", "jxht_body_score");
	var html="<div class=\"jxht_score_menubox\">   ";
		html+="<a href='' class='jxht_score_a1'>当前积分：<c:out value='${card.ttlFen }' default='0'></c:out></a>";
		html+=" <a onclick=\"showIntegration('${pk_group}','${openid}','${card.cardNo}')\" class='jxht_score_a2'>积分明细查询</a>";
		html+="<a href='' class='jxht_score_a3'>积分商城</a></div>";

	$(document.body).html(html);//JQ方式
	
}
</script>
</head>
<body class="jxht_body_menu">
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<!--右上角手机编号-->
		<div class="jxht_mbox_1" tyle="font-size:20%;">NO:${card.tele}</div>
		<div class="jxht_menu_a1" onclick="showJf_or_CardPage()">我的积分</div>
		<div class="jxht_menu_a2" onclick="window.location.href='../view/myCard/myQRCode.jsp?openid=${openid }&pk_group=${pk_group }&qrordr=${card.qrordr}&unionid=${unionid}&myselfShareCode=${card.myselfShareCode }'">绝密档案</div>
		<div class="jxht_menu_a3" onclick="javaScript:openPage(this,'<%=path %>/myCard/myCardVoucher.do?openid=${openid }&pk_group=${pk_group }')">我的优惠券</div>
		<div class="jxht_menu_a4" onclick="showIntegration('${pk_group}','${openid}','${card.cardNo}')">会员权益</div>
		<!--会员信息-->
		<div class="jxht_menuhy_1" style="display:">会员等级：${card.typDes}</div>
		<div class="jxht_menuhy_2" style="display:none">晋级：需消费100大洋</div>
		
		<!--条形码-->
		<div class="jxht_menu_txm"><img src="<%=Commons.vcardPic.replace("qrcode", "barcode") %>${card.qrordr}.png" width="100%" height="100%" /></div>
		
		<!--活动图片-->
		<div class="jxht_menu_hd">
			<h3 class="jxht_menu_hdtit">热门活动</h3>
		    <a class="jxht_menu_hdlink">吉祥馄饨会员专享福利</a>
		</div>
	</div>	
</div>	
</body>
</html>