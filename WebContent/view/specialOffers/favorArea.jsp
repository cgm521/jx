<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
		#two{
   			border: solid #DCD7D6 1px;
   			border-radius: 5px;
   		}
   		#bkgMain{
   			width:100%;
   			height:1000px;
   			margin:auto;
   			background-color:white;
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
	<div data-role="page" style="background-color:#DCD7D6;">
		<div data-role="header">
			<h1>优惠区域</h1>
		</div>
		<div id="bkgMain">
			<div id="bkgulMain" style="padding:1% 0 0 1%;width:98%;">
				<div id="two">
					<table>
						<c:forEach var="favorarea" items="${listFavorArea}" varStatus="status">
							<tr id="ids" style="border-top: solid #DCD7D6 1px;" onclick="xialaChage(this)">
								<td><div style="float: left;padding: 1px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_21.png"></div>&nbsp;<div style="float: left;padding-left:4px;"><a style="color:#000;font-weight: normal;" data-ajax="false" href="<%=path %>/pubitem/findWebMsg.do?favorAreaId=${favorarea.id}&firmId=${favorarea.firmid}">${favorarea.area }</a></div><div style="float: right;padding: 1px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div></td>
							</tr>
							<tr style="display:none;" id="tr_${status.index+1}">
								<td id="td_${status.index+1}"><input type="hidden" id="" value="${status.index+1}" />
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
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