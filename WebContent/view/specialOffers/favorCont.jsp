<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.choice.test.utils.Commons"%>
<%String path=request.getContextPath(); %>
<html> 
　<head> 
　 <title></title> 
　 <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/> 
　 <link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
   <link rel="stylesheet" href="<%=path %>/css/validate.css" />
　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
   <script src="<%=path %>/js/validate.js"></script>
   <style type="text/css">
		.tablist-content {
		    width: 60%;
		    display: inline-block;
		    vertical-align: top;
		    margin-left: 5%;
		}
   		#bkgMain{
   			width:96%;
   			height:100%;
   			margin:auto;
   			background-color:white;
   		}
   		table{
   			width:100%;
			border-collapse: collapse; 
			border: none; 
		}
   </style>
</head>
<body>
	<div data-role="page" style="background-image:url(<%=path %>/image/regHuise.png);width:100%;margin:auto;">
		<div data-role="header">
			<h1>优惠信息</h1>
		</div>
		<div id="bkgMain">
				<table>
					<c:forEach var="webmsg" items="${listWebMsg}" varStatus="status">
						<tr id="ids" style="border-top: solid #DCD7D6 1px;">
							<td>
								<table>
									<tr style="text-align:center">
										<td style="width:100%;height:40%;"><img width="100%" height="220px" width="15" height="15" src="${webmsg.wurl }"></td>
									</tr>
									<tr>
										<td style="text-align:center;"><h4>${webmsg.title }</h4></td>
									</tr>
									<tr>
										<td style="text-align:left;">${webmsg.wcontent }</td>
									</tr>
									<tr>
										<td style="text-align:left;">
											<ul style="list-style:none;">本优惠信息适用于<span style="color:red;">
											<c:forEach var="firm" items="${listFirm}" varStatus="status">
												<li>${firm.firmdes }</li>
											</c:forEach></span>
											</ul>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</c:forEach>
				</table>
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