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
   <link rel="stylesheet" href="<%=path %>/css/default/global.css" />
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
		<div class="card_top">
			<img width="25" height="25" src="<%=path %>/image/yue.png"/>&nbsp;&nbsp;电子券
		</div>
		<div class="main">
			<div style="padding:1% 0 0 1%;width:98%;">
				<table>
					<c:forEach var="voucher" items="${listVoucher}" varStatus="status">
						<tr id="ids" style="border-bottom: solid #DCD7D6 1px;" onclick="xialaChage(this)">
							<td><div style="float: left;padding: 1px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_05.png"></div>&nbsp;<div style="float: left;padding-left:4px;"><a style="color:#000;font-weight: normal;" data-ajax="false" href="#">${voucher.typdes }</a></div><div style="float: right;padding: 1px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouDown.png"></div></td>
						</tr>
						<tr style="display:none;" id="tr_${status.index+1}">
							<td id="td_${status.index+1}"><input type="hidden" id="" value="${status.index+1}" />
								<ul>
									<li>编号：${voucher.code }</li>
									<li>使用分店：${voucher.firmname }</li>
									<li>开始日期：${voucher.bdate }</li>
									<li>结束日期：${voucher.edate }</li>
									<c:choose>
										<c:when test="${voucher.sta eq 1 }">
											<li>状态：未领用</li>
										</c:when>
										<c:when test="${voucher.sta eq 2 }">
											<li>状态：已领用</li>
										</c:when>
										<c:when test="${voucher.sta eq 3 }">
											<li>状态：已使用</li>
										</c:when>
										<c:when test="${voucher.sta eq 4 }">
											<li>状态：已过期</li>
										</c:when>
										<c:when test="${voucher.sta eq 5 }">
											<li>状态：已作废</li>
										</c:when>
									</c:choose>
								</ul>
							</td>
						</tr>
					</c:forEach>
				</table>
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
		function xialaChage(obj){
			$(obj).next("tr").toggle();
			
		}
   </script>
</body>
</html>