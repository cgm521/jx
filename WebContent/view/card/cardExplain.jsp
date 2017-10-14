<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%String path=request.getContextPath(); %>
<html> 
　<head> 
　 <title></title> 
　 <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/> 
　 <link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
   <link rel="stylesheet" href="<%=path %>/css/validate.css" />
   <link rel="stylesheet" href="<%=path %>/css/default/global.css" />
　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
   <script src="<%=path %>/js/validate.js"></script>
</head>
<body>
	<div data-role="page" class="page">
		<div class="card_top">
			<img width="25" height="25" src="<%=path %>/image/huiyuanka_19.png">&nbsp;&nbsp;会员卡说明
		</div>
		<div class="main">
			<div style="margin:3% 0 0 10px;padding:1px 0 0 0;">
				${cardexplan }
			</div>
	    </div>
	</div>
	<script type="text/javascript">
		$(function(){
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i)=="micromessenger") {
		 	} else {
				$("body").text("请使用微信浏览器打开");
			}
		});
	</script>
</body>
</html>