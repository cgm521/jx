<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons" %>
<%	
	String openid = request.getParameter("openid");
	String path = request.getContextPath(); 
	String myselfShareCode=request.getParameter("myselfShareCode");
	String pk_group = request.getParameter("pk_group");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>吉祥馄饨_推荐码</title>
<!--<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" /> -->
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script language="JavaScript" type="text/JavaScript">
	wx.config({
	    debug: false,
	    appId: '${appId}',
	    timestamp: '${signMap.timestamp}',
	    nonceStr: '${signMap.nonceStr}',
	    signature: '${signMap.signature}',
	    jsApiList: ['onMenuShareAppMessage']
	});
	
	wx.ready(function() {
		wx.hideMenuItems({
		    menuList: [
		    	'menuItem:share:timeline',
		    	'menuItem:share:qq',
		    	'menuItem:share:weiboApp',
		    	'menuItem:favorite',
		    	'menuItem:editTag',
		    	'menuItem:copyUrl'
		    ] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
		});
		
		var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=" +
			"<%=path %>/myCard/voucherDetail.do?id=${voucher.id}_${voucher.cardId}&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		
		wx.onMenuShareAppMessage({
		    title: '转赠电子券', // 分享标题
		    desc: '您的好友邀请您注册会员', // 分享描述
		    link: url, //分享链接
		    imgUrl: '', // 分享图标
		    type: 'link', // 分享类型,music、video或link，不填默认为link
		    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
		    success: function () { 
		        // 用户确认分享后执行的回调函数
		    },
		    cancel: function () { 
		        // 用户取消分享后执行的回调函数
		    }
		});
	});
	
	

	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('showToolbar');
	    WeixinJSBridge.call('showOptionMenu');
	});

	function backHome() {
		var url = "../myCard/myQRCode.jsp?pk_group=<%=pk_group %>&openid=<%=openid %>&myselfShareCode=<%=myselfShareCode%>";
		window.location.href = url;
	}	
		
		
		
		
	
</script>
<style type="text/css">
/*推荐码-start--------------------------------------------------------------------------*/
.jxht_body_tjm{
	background:url(../../image/wechat/jxht_tjmbg.jpg) center bottom no-repeat;
	background-size:cover;
}
.jxht_tjm_num{
	margin:1rem auto;
	padding:0.5rem 0;
	width:60%;
	text-align:center;
	font-size:1rem;
	background:#231916;
	color:#fff;
	border-radius: 0.2rem;
}
.jxht_tjmcodes{
	margin:0 auto;
	width:40%;
}
.jxht_tjmline{
	margin:1rem auto;
	width:60%;
	height:1px;
	border-top:1px dashed #666;
}
.jxht_tjm_txt{
	margin:2rem 0 0 0 ;
	padding:1rem 1rem 0 2rem;
	width:90%;
	height:15rem;
	text-align:left;
	font-size:0.9rem;
	line-height:1.3rem;
	background:url(../../image/wechat/jxht_tjm_txtbg.png) no-repeat;
	background-size:100% auto;
}
.jxht_tjm_t1{
	color:#D72E22;
}
.jxht_tjm_back{
	position:absolute;
	bottom:1.2rem;
	left:1rem;
	width:1.6rem;
	height:1.2rem;
	display:block;
	background:url(../../image/wechat/jxht_arr_right_black.png) center center no-repeat;
	background-size:auto 90%;
	border-radius: 0.2rem;
}
/*推荐码-end--------------------------------------------------------------------------*/

</style>
</head>
<body class="jxht_body_tjm">
<div ><!--页面层容器-->
	<!--推荐码-->
	<div class="jxht_tjm_num">我的分享推荐码：<%=myselfShareCode %></div>
	<!--二维码-->
	<div class="jxht_tjmcodes"><img src="../../image/wechat/jxht_codes.png" width="100%" /></div>
	<!--虚线条-->
	<div class="jxht_tjmline"></div>
	<!--底部文字说明-->
	<div class="jxht_tjm_txt">注册会员时，输入我的推荐码，<br>将获得<span class="jxht_tjm_t1">奖励</span>哟~<br>点击微信右上角的按钮，<br>分享给朋友吧！</div>
	<!--返回按钮-->
	<a href="javascript:backHome()" class="jxht_tjm_back"></a>
</div>	
</body>
</html>