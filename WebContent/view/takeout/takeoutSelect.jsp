<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>外卖</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>

<style type="text/css">
body{
	color:#3d3d3d;
	background-color:#f7f7f7;
}
.content{
	margin-top:0;
	list-style:none;
	padding:0;
}
.header{
	width:100%;
	background-color:#f7f7f7;
	color:#FFFFFF;
	height:200px;
    margin:-29px 0 0 0;
    background-image:url(../image/wechat/takeout_1.png);
    background-repeat:no-repeat;
    background-position: center;
    text-align:center;
	font-size:80%;
}
</style>
<script language="JavaScript" type="text/javascript">
/*去除安卓返回及刷新按钮*/
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});
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

</script>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div class="header" style="width:300px;height:185px; background-size:100% 100%;margin: 15px auto;">
			<div id="noDiv" style="padding-left:8px;">
				<div style="float:right; padding:75px 50% 0 0px; list-style-type:none;">
					<span style="color: #000;font-size: 14px;font-weight: bolder;color: black;">NO.${card.tele }</span>
				</div>
			</div>
		</div>
		<div class="account" style="padding-bottom:0px;width:320px;height:200px;margin: 5px auto;">
			<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr style="background-color:;color:#111010;height:50px;font-size:100%;">
					<td><div style="float:left; padding:0 0 10px 20px;"></div></td>
					<td><div style="float:right; padding:0 20px 10px 0px;"></div></td>
				</tr>
				<tr style="background-color:#FFB400 ;">
					<td class="" width="50%" style="background-color:#f3dee1;color:#111010" onclick="javaScript:openPage(this,'<%=path %>/dining/listFirm.do?nextType=4&openID=${openid }')">外卖下单<br/>
					</td>
					<td class="" width="50%" style="background-color:#f8eeef;color:#111010" onclick="javaScript:openPage(this,'<%=path %>/bookDesk/findTakeOutOrders.do')">我的外卖单<br/>
					</td>	
				</tr>
			</table>
		</div>	
	</div>	
</div>	
</body>
</html>