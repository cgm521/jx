<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.choice.test.utils.Commons"%>
<%String path=request.getContextPath(); %>
<html> 
　<head> 
　 <title></title> 
   <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
　 <link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
   <link rel="stylesheet" href="<%=path %>/css/validate.css" />
      <link rel="stylesheet" href="<%=path %>/css/default/global.css"/>
　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
   <script src="<%=path %>/js/validate.js"></script>
   <script src="<%=path %>/js/layer.js"></script>
   <style type="text/css">
  		
   		#orderTable{
   			width:100%;
			border-collapse: collapse; 
			border: none;
			margin:0px auto;
			height:auto;
		}
		#orderTable tr{
			height:35px;
		}
		#orderTable td a{
			font-size:16px/22px;
			text-decoration:none;
		}
		#bkgMain{
   			width:98%;
   			height:auto;
   			margin:auto;
   			background-color:transparent;
   			border-radius: 5px;
   		}
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
   </style>
</head>
<body>
	<div data-role="page" class="page">
		<div id="top" class="top"><%=Commons.wx_orderdtl_title %></div>
		<div id="bkgMain" class="main" style="height:auto;">
				<table id="orderTable">
					<c:forEach var="orders" items="${listNet_Orders }" >
						<tr style="border-bottom: solid #DCD7D6 1px;">
							<td>
								&nbsp;<div style="width:95%;height:auto;margin:0px auto;">
<!-- 									<a style="color:#000;font-weight: normal;" data-ajax="false" href=""> -->
									<table style="width:100%;height:auto;border:1px;margin:0px auto;">
										<tr style="font-size:18px;">
											<td style="width:35%;">订单号：</td>
											<td>${orders.resv }</td>
											<td style="display:none;">${orders.id }</td>
											<td style="display:none;">${orders.resv }</td>
										</tr>
										
										<tr style="font-size:18px;">
											<td style="width:35%;">订单状态:</td>
											
											<td><c:choose>
													<c:when test="${orders.state=='2'}">已支付</c:when>
													<c:otherwise>未支付</c:otherwise>
												</c:choose>
											</td>
										</tr>
										
										<tr style="font-size:18px;">
											<td style="width:35%;">门店名称：</td>
											<td>${orders.firmdes }</td>
											<td style="display:none;">${orders.id }</td>
											<td style="display:none;">${orders.resv }</td>
										</tr>
										<tr style="font-size:18px;">
											<td style="width:35%;">预定时间：</td>
											<td>${orders.dat }&nbsp;&nbsp;&nbsp;${orders.datmins }</td>
										</tr>
										<tr style="font-size:18px;">
											<td style="width:35%;">门店地址：</td>
											<td>${orders.addr }</td>
										</tr>
										<tr style="font-size:18px;">
											<td style="width:35%;">门店电话：</td>
											<td><div style="float:left;padding:6px 0 0 0;">${orders.tele }</div><div style="float:left;padding:3px 0 0 5px;"><a href="tel:${orders.tele }"><img height="25" width="25" src="<%=path %>/image/dianhua.png" /></a></div></td>
										</tr>
										<c:if test="${orders.pax !=null  && orders.pax !=''}">
										<tr style="font-size:18px;">
											<td style="width:35%;">预定人数：</td>
											<td>${orders.pax }人</td>
										</tr>
										</c:if>
										<c:if test="${orders.tables !=null && orders.tables !=''}">
										<tr style="font-size:18px;">
											<td style="width:35%;">台位：</td>
											<td>${orders.tables }</td>
										</tr>
										</c:if>
										<c:if test="${orders.remark !=null && orders.remark !=''}">
										<tr style="font-size:18px;">
											<td style="width:35%;">备注：</td>
											<td>${orders.remark }</td>
										</tr>
										</c:if>
										
										<tr>
										<td colspan="2">
										<div id="bkgMain">
										<div id="bkgulMain">
											<div id="two">
												<table id="tbl">
													<thead>
														<tr>
															<td>菜品名称</td>
															<td>单价</td>
													   	 	<td>数量</td>
													   	 	<td>总额</td>
<!-- 													   	 	<td>删除</td> -->
												   	 	</tr>
													</thead>
													<tbody id="tbody">
														<c:forEach var="orderDtl" items="${orders.listNetOrderDtl}">
															<tr style="border-top: solid #DCD7D6 1px;">
																<td style="display:none;">${orderDtl.id }</td>
																<td>${orderDtl.foodsname }</td>
																<td>${orderDtl.price }</td>
														   	 	<td>${orderDtl.foodnum }</td>
														   	 	<td>${orderDtl.totalprice }</td>
<%-- 														   	 	<td><a onclick="deleteOrderDtl(this)"><img width="30" height="30" src="<%=path %>/image/chahao.png" /></a></td> --%>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
									    </div>
								    </div>
										</td>
										</tr>
										<tr>
											<td colspan="2">
												<div style="float:center;width:35%;height:100%;text-align:right;margin:0 0 0 30%;">
													<a data-role="button" style="margin:0.2em;padding:0.5em;display:block;" onclick="deleteOrder(this)">取消预订</a>
												</div>
<!-- 												<div style="float:center;width:35%;height:100%;text-align:right;margin:0 0 0 30%;"> -->
<!-- 													<a data-role="button" style="margin:0.2em;padding:0.5em;display:block;" onclick="deleteOrder(this)">微信支付</a> -->
<!-- 												</div> -->
<!-- 												<div style="float:center;width:35%;height:100%;text-align:right;margin:0 0 0 30%;"> -->
<!-- 													<a data-role="button" style="margin:0.2em;padding:0.5em;display:block;" onclick="deleteOrder(this)">现场付</a> -->
<!-- 												</div> -->
<!-- 												<div style="float:right;width:35%;height:100%;text-align:left;margin:0 14% 0 0;"> -->
<%-- 													<a href="<%=path %>/pubitem/getOrderDtlMenus.do?orderid=${orders.id }&resv=${orders.resv}&openid=${orders.openid}&firmid=${orders.firmid}" data-role="button" data-ajax="false" onclick="InitLayer()" style="margin:0.2em;padding:0.5em;display:block;">查看详情</a> --%>
<!-- 												</div> -->
											</td>
										</tr>
									</table>
								</div>
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
// 			if(ua.match(/MicroMessenger/i)!="micromessenger") {
// 				$("body").text("请使用微信浏览器打开");
// 				return;
// 			}
// 			$("#two tr").each(function(){
// 				$(this).click(function(){
// 					$(this).find("a").click(function(){
// 					});
// 					var link1=$(this).find("a").attr("href");
// 					window.location.href=link1;
// 				});
// 			});
		});
		function deleteOrder(e){
// 			$(e).closest('tr').unbind("click");
			var val = $(e).closest('tr').siblings().first().find('td:eq(2)').text();
			var obj = $(e).closest('tr').siblings().first().find('td:eq(3)').text();
			InitLayer();
			$.post("<%=path%>/pubitem/deleteOrder.do?resv="+obj+"&id="+val,val,function(date){
				$(e).closest('table').parents('tr').remove();
				closeLayer();
			});
		};
   </script>
</body>
</html>