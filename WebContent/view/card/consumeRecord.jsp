<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
   <style type="text/css">
   		table{
   			width:100%;
			border-collapse: collapse; 
			border: none; 
		}
		tr{
			height:45px;
		}
		td a{
			font-size:16px/22px;
			text-decoration:none;
   			font-family:"微软雅黑";
		}
   </style>
</head>
<body>
	<div data-role="page" class="page">
	<div class="card_top">消费记录</div>
	<div class="main" style="width:90%;margin:0 auto;">
			<table>
				<c:forEach var="consumeRecord" items="${listConsumeRecord }">
					<tr style="border-bottom: solid #DCD7D6 1px;">
						<td>
								<table>
									<tr style="font-size:18px;font-family:Verdana, '楷体', Arial,Sans;">
										<td style="width:35%;">消费金额：</td>
										<td><span style="color:red;">${consumeRecord.balaamt }</span>元</td>
									</tr>
									<tr style="font-size:18px;font-family:Verdana, '楷体', Arial,Sans;">
										<td>消费时间：</td>
										<td>${consumeRecord.tim }</td>
									</tr>
									<tr style="font-size:18px;font-family:Verdana, '楷体', Arial,Sans;">
										<td>消费门店：</td>
										<td>${consumeRecord.firmdes }</td>
									</tr>
								</table>
						</td>
					</tr>
				</c:forEach>
			</table>
	 </div>
   </div>
   <script type="text/javascript">
	    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		    WeixinJSBridge.call('hideToolbar');
		    WeixinJSBridge.call('hideOptionMenu');
		});
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