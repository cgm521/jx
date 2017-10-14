<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%String path=request.getContextPath(); %>
<html> 
<head> 
<title>微商城</title> 
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<link rel="stylesheet" href="<%=path %>/css/validate.css" />
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/validate.js"></script>
<script src="<%=path %>/js/layer.js"></script>
<script type="text/javascript">
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
</script>
</head>
<body>
<script type="text/javascript">
	//辰森世纪微商城地址
	var url = "http://mp.weixin.qq.com/bizmall/mallshelf?id=&t=mall/list&biz=MjM5MDc2NzgwMw==&shelf_id=1&showwxpaytitle=1#wechat_redirect";
	window.setTimeout(function(){
		location.href = url;
	},100);
</script>
</body>
</html>
