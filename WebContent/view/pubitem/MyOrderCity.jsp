<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.choice.test.utils.Commons"%>
<%String path=request.getContextPath(); %>
<html> 
　<head> 
　 <title>我的预定</title> 
   <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
　 <link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
   <link rel="stylesheet" href="<%=path %>/css/validate.css" />
   <link rel="stylesheet" href="<%=path %>/css/default/global.css"/>
　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
   <script src="<%=path %>/js/validate.js"></script>
   <script src="<%=path %>/js/layer.js"></script>
   <style type="text/css">
   		#two{
   			border: solid #DCD7D6 1px;
   			border-radius: 5px;
   		}
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
   			font-family:Verdana, "楷体", Arial,Sans;
		}
   </style>
</head>
<body>
	<div data-role="page" class="page">
		<div id="top" class="top"><%=Commons.wx_order_title %></div>
		<div id="bkgMain" class="main">
			<div id="bkgulMain">
				<div id="two">
					<table>
						<c:forEach var="orders" items="${listNet_Orders }">
							<tr style="border-bottom: solid #DCD7D6 1px;" onclick="InitLayer()">
								<td>
									<div style="float: left;padding: 11px 0 0 6px;"></div>
									&nbsp;<div style="float: left;padding-left:4px;">
									<a style="color:#000;font-size:18px;font-weight: normal;" data-ajax="false"  onclick="InitLayer()"  href="<%=path %>/pubitem/getOrderMenus.do?firmid=${orders.firmid }&openid=${orders.openid }&dat=${orders.dat }&dateTime=${orders.datmins }">${orders.firmdes }&nbsp;<br>${orders.dat }&nbsp;&nbsp;&nbsp;${orders.datmins }</a></div>
									<div style="float: right;padding: 11px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
		    </div>
	    </div>
   </div>
   <script type="text/javascript">
	    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		    WeixinJSBridge.call('hideToolbar');
		    WeixinJSBridge.call('hideOptionMenu');
		});
		$(function(){
			var ua = navigator.userAgent.toLowerCase();
// 			if(ua.match(/MicroMessenger/i)!="micromessenger") {
// 				$("body").text("请使用微信浏览器打开");
// 				return;
// 			}
			
			$("#two tr").each(function(){
				$(this).click(function(){
					$(this).find("a").click(function(){
					});
					var link1=$(this).find("a").attr("href");
					window.location.href=link1;
				});
			});
		});
   </script>
</body>
</html>