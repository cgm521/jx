<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons" %>
<%	String qrPath = request.getParameter("qrordr");
	String pk_group = request.getParameter("pk_group");
	String openid = request.getParameter("openid");
	String path = request.getContextPath(); 
	String unionid=request.getParameter("unionid");
	String myselfShareCode=request.getParameter("myselfShareCode");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>吉祥馄饨_完善资料导航</title>
<!--<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" /> -->
<!--<link type="text/css" href='<c:url value="/css/wechat/myCardVoucher.css" />' rel="stylesheet" /> -->
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script language="JavaScript" type="text/JavaScript">
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	
	function backHome() {
		var url = "<%=path %>/myCard/cardInfo.do?pk_group=<%=pk_group %>&openid=<%=openid %>";
		window.location.href = url;
	}
// 页面跳转
function openPage(tr,url){
	window.setTimeout(function(){
		InitLayer();
		window.setTimeout(function(){
			location.href = url;
		}, 100);
	},500);
}
</script>
<style type="text/css">
.jxht_body_perfect{
	padding-top:30%;
	background:#CA3424 url(../../image/wechat/jxht_wfbg.jpg) no-repeat;
	background-size:100% auto;
}
.jxht_perfect_btn{
	text-align:center;
	display:block;
	margin:2rem auto 0 auto;
	width:48%;
	padding: 0.4rem 0 0.3rem;
	border: 1px solid rgba(0,0,0,.1);
	background: #fff;
	border-radius: 0.2rem;
	box-shadow: 0rem 0.3rem 0rem rgba(0,0,0,1);
	font-size:1.2rem;
	line-height: 1.5;
}
.jxht_perfect_back{
	position:absolute;
	bottom:1rem;
	left:1rem;
	width:1.6rem;
	height:1.2rem;
	display:block;
	background:url(../../image/wechat/jxht_arr_right.png) center center no-repeat;
	background-size:auto 90%;
	border-radius: 0.2rem;
}
</style>
</head>
<body class="jxht_body_perfect" >
<div><!--页面层容器-->
				
	<div class="jxht_perfect_btn" style="text-align:center;"
		onclick="javaScript:openPage('','<%=path %>/myCard/compInformation.do?openid=<%=openid %>&pk_group=<%=pk_group %>&unionid=<%=unionid %>')">
		  完善资料
	</div>
			
	<div class="jxht_perfect_btn"  
		onclick="javaScript:openPage('','shareCode.jsp?openid=<%=openid %>&pk_group=<%=pk_group %>&myselfShareCode=<%=myselfShareCode %>')" >
		分享推荐码
	</div>
					
	<div class="jxht_perfect_back" onclick="javascript:backHome()">			
  	</div>
</div>
</body>
</html>