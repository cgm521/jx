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
		<img width="25" height="25" src="<%=path %>/image/yue.png"/>&nbsp;&nbsp;积分余额：<span style="color:red;">${ttlFen}分</span>
	</div>
	<div class="main">
		<div style="margin:3% 0 0 10px;padding:1px 0 0 0;"><h3>积分规则</h3></div>
		<div style="width:95%;">
			${jifenrules }
		</div>
	   </div>
	  </div>
	<%-- <div data-role="page" style="background-color:#DCD7D6;">
		<div style="padding:0 0 0 18%;">
			<h2><div style="float:left;margin:3px 0 0 0;"><img width="25" height="25" src="<%=path %>/image/yue.png"></div><div style="padding:0 0 0 10%;">积分余额：<span style="color:red;">${ttlFen}分</span></div></h2>
		</div>
		<div style="background-color:white;width:100%;height:1000px;margin:-2% 0 0 0;font-family:Verdana, '黑体', Arial,Sans;">
			<div style="margin:3% 0 0 10px;padding:1px 0 0 0;"><h3>积分规则</h3></div>
			<div style="width:95%;">
				${jifenrules }
			</div>
	    </div>
	</div> --%>
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