<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%	String path=request.getContextPath(); 
	String port ="80".equals((request.getServerPort()+""))?"":(":"+request.getServerPort()+"");
	String basePath = request.getScheme()+"://"+request.getServerName()+port+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>电子券</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/myCardVoucher.css" />' rel="stylesheet" />
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
			"<%=basePath %>/myCard/voucherDetail.do?id=${voucher.id}_${voucher.cardId}&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		
		wx.onMenuShareAppMessage({
		    title: '转赠电子券', // 分享标题
		    desc: '赠给你一张电子券，点击领取。', // 分享描述
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
	
	wx.error(function (res) {
		alertMsg(res.errMsg);
	});

	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('showToolbar');
	    WeixinJSBridge.call('showOptionMenu');
	});
	$(function(){
		if("${isMember}" == "N") {
			alertMsg("您还不是会员，请先注册会员再领券！");
			var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=" +
				"<%=basePath %>/myCard/cardInfo.do?id=${voucher.id}&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
				
			window.location.href = url;
		}
		/* var ua = navigator.userAgent.toLowerCase();
	 	if(ua.match(/MicroMessenger/i)!="micromessenger") {
	 		$("body").text("请使用微信浏览器打开");
	 		return;
		} */
		
		/* WeixinJSBridge.on('menu:share:appmessage',
				function(argv) {
			WeixinJSBridge.invoke('shareTimeline',{
				  "img_url":"",
				  //”img_width”:”640″,
				  //”img_height”:”640″,
				  "link":"http://www.baidu.com",
				  "desc":"test",
				  "title":"t"
			   },function(res){
		  			WeixinJSBridge.log(res.err_msg);
		  			alert(res.err_msg);
		  		// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
		  		//因此微信团队建议，当收到ok返回时，向商户后台询问是否收到交易成功的通知，若收到通知，前端展示交易成功的界面；若此时未收到通知，商户后台主动调用查询订单接口，查询订单的当前状态，并反馈给前端展示相应的界面。
		  		});
			}); */
		
		$(".giveFriends").bind("click",function (){
			var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=" +
				"<%=basePath %>/myCard/voucherDetail.do?id=${voucher.id}_${voucher.cardId}&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
			
			WeixinJSBridge.invoke('sendAppMessage',{
				"appid":"${appId}",                                              //appid 设置空就好了。
				"img_url":"",                                   //分享时所带的图片路径
				"img_width":"120",                            //图片宽度
				"img_height":"120",                            //图片高度
				"link":url,                                               //分享附带链接地址
				"desc":"转赠",                            //分享内容介绍
				"title":"转赠"
			}, function(res){
				/*** 回调函数，最好设置为空 ***/
				alert(res.err_msg);
			});
		});
		
		$(".accept").bind("click",function (){
			InitLayer();
			$.post("<c:url value='/myCard/acceptVoucher.do?' />", {"id":"${voucher.id}", "cardId":"${acceptCardId}"}, function(data){
				if(data == "ok") {
					alertMsg("转赠成功！");
					var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=" +
					"<%=basePath %>/myCard/cardInfo.do?id=${voucher.id}&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
				
					window.setTimeout(function(){
						location.href = url;
					},500);
				} else {
					alertMsg('转赠失败！');
				}
			});
		});
	});
</script>
<style type="text/css">
.oneOrder{
	margin-top:10px;
	background-color:#ffffff;
	width:90%;
	margin-left:5%;
	margin-right:5%;
	border:1px solid #ddd;
}
</style>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0px; padding-top:0px; background-color:#F7F7F7;"><!--页面主体-->
		<input type="hidden" id="pk_group" value="${pk_group}">
		<input type="hidden" id="openid" value="${openid}">
		
		<div class="oneOrder" data-ajax="false" style="border:1px solid #ddd;float:left;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 96%;margin: 0px auto;">
				<tr>
					<td style="text-align:left;padding-left:10px;" colspan="2">
						<p style="text-align:left;font-size:110%; font-weight: bolder;">
							${voucher.typdes }
						</p>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="oneOrder" data-ajax="false" style="border:1px solid #ddd;float:left;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 96%;margin: 0px auto;">
				<tr height="50px">
					<td style="text-align:left;padding-left:10px;border-bottom: 1px solid #ddd;">
						<span style="font-family: Arial;">电子券号：</span>
					</td>
					<td style="padding-right:10px;text-align:right;vertical-align:middle;border-bottom: 1px solid #ddd;">
						<span style="font-size:80%; font-family: Arial;">&nbsp;${voucher.code}</span>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="oneOrder" data-ajax="false" style="border:1px solid #ddd;float:left;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 96%;margin: 0px auto;">
				<tr height="50px">
					<td style="text-align:left;padding-left:10px;border-bottom: 1px solid #ddd;">
						<span style="font-family: Arial;">使用期限：</span>
					</td>
					<td style="padding-right:10px;text-align:right;vertical-align:middle;border-bottom: 1px solid #ddd;">
						<span style="font-size:80%; font-family: Arial;">&nbsp;${voucher.bdate}/${voucher.edate}</span>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="oneOrder" data-ajax="false" style="border:1px solid #ddd;float:left;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 96%;margin: 0px auto;">
				<tr height="50px">
					<td style="text-align:left;padding-left:10px;border-bottom: 1px solid #ddd;">
						<span style="font-family: Arial;">适用门店：</span>
					</td>
					<td style="padding-right:10px;text-align:right;vertical-align:middle;border-bottom: 1px solid #ddd;">
						<c:choose>
							<c:when test="${voucher.rest ne null && voucher.rest ne '' }">
								<span style="font-family: Arial;">&nbsp;${voucher.rest }</span>
							</c:when>
							<c:otherwise>
								<span style="font-family: Arial;">&nbsp;所有</span>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="oneOrder" data-ajax="false" style="border:1px solid #ddd;float:left;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 96%;margin: 0px auto;">
				<tr height="50px">
					<td style="text-align:left;padding-left:10px;border-bottom: 1px solid #ddd;">
						<span style="font-family: Arial;">使用说明：</span>
					</td>
					<td style="padding-right:10px;text-align:right;vertical-align:middle;border-bottom: 1px solid #ddd;">
						<span style="font-size:80%; font-family: Arial;">&nbsp;${voucher.memo }</span>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="ui-a">
					<a href="<c:url value='/myCard/cardInfo.do?pk_group=${pk_group}&openid=${openid }' />" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
				<c:choose>
					<c:when test="${showAcceptBtn eq 'Y' }">
						<td style="background-color:#F39801; color:white; width:25%; text-align:center;" class="accept">
							接受
						</td>
					</c:when>
					<c:when test="${showTransBtn eq 'Y' }">
						<td style="background-color:#F39801; color:white; width:25%; text-align:center; display:none;" class="giveFriends">
							赠送给好友
						</td>
					</c:when>
				</c:choose>
				<%-- <c:if test="${showAcceptBtn eq 'Y' }">
					<td style="background-color:#F39801; color:white; width:25%; text-align:center;" class="accept">
						接受
					</td>
				</c:if> --%>
			</tr>
		</table>
  	</div>
</div>
</body>
</html>