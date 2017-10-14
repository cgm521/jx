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
<link rel="stylesheet" href="<%=path %>/css/default/pubitem/listOrderDtl.css"/>
　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
   <script src="<%=path %>/js/validate.js"></script>
   <script src="<%=path %>/js/layer.js"></script>
   <style type="text/css">
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
	<div id="bkgimg1" class="top">
		<div style="float:left;width:60%;"><%=Commons.wx_ordermenu_title %></div>
		<div style="float:right;width:38%;">
			<div style="float:left;width:45%;height:100%;text-align:right"><a data-role="button" style="margin:0.2em;padding:0.4em;display:block;" onclick="clearDtl()">清空</a></div>
			<div style="float:right;width:45%;height:100%;text-align:left;"><a data-ajax="false" href="<%=path %>/pubitem/listPubitem.do?id=${orderid}&openid=${openid}&firmid=${firmid}" data-role="button" style="margin:0.2em;padding:0.4em;display:block;" onclick="InitLayer()">加菜</a></div>
		</div>
		<%-- <div style="margin:0 0 0 -26%;">
			<div id="top"><%=Commons.wx_ordermenu_title %></div>
		</div>
		<div style="position:absolute;right:5px;top:4px;">
			<img height="30px" style="border: solid #DCD7D6 transparent;" onclick="clearDtl()" src="<%=path %>/image/clear.png"/>
			<a data-ajax="false" href="<%=path %>/pubitem/listPubitem.do?id=${orderid}&openid=${openid}&firmid=${firmid}">
				<img height="30px" onclick="InitLayer()" style="border: solid #DCD7D6 transparent;" src="<%=path %>/image/jiacai.png"/>
			</a>
		</div> --%>
	</div>
	<div id="one" class="ui-body-d tablist-content" style="width:95%;background-color:transparent;border-radius: 5px;margin:auto;">
   		<span>到店用餐时，请向服务员出示您的订单号</span><br>
   		<span>订单号：<span id="" style="color:red;">${resv }</span></span>
	</div><br>
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
					   	 	<td>删除</td>
				   	 	</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach var="orderDtl" items="${listNet_OrderDtl}">
							<tr style="border-top: solid #DCD7D6 1px;">
								<td style="display:none;">${orderDtl.id }</td>
								<td>${orderDtl.foodsname }</td>
								<td>${orderDtl.price }</td>
						   	 	<td>${orderDtl.foodnum }</td>
						   	 	<td>${orderDtl.totalprice }</td>
						   	 	<td><a onclick="deleteOrderDtl(this)"><img width="30" height="30" src="<%=path %>/image/chahao.png" /></a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
	    </div>
    </div>
    <br>
    <br>
	   	<div id="commitdiv">
			<div id="commit-left">合计：<span id="totalMoney" style="color:red;"></span>元</div>
			<div id="commit-right" onclick="flagDel()"><div style="margin:0.2em 0px 0.2em 1.5em;">提交订单</div></div>
			<a  id="a1" href="#" data-rel="dialog"></a>
		</div>
   </div>
   <div data-role="page" id="commiteSuccess" data-overlay-theme="e">
	  <div data-role="header">
	    <h1>温馨提示</h1>
	  </div>
	  <div data-role="content" id="conttent">
	  	<table id="dlagTbl" style="border: solid #DCD7D6 1px;">
	  	</table>
	  	<a id="enterMyorderBtn" href="#" data-role="button" data-ajax="false" onclick="delOrderDtl()">确定</a>
	  	<a href="#" id="clo" style="display:none;" data-role="button"  data-rel="back" data-theme="a">关闭</a>
	  </div>
   </div> 
   <script type="text/javascript">
	    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		    WeixinJSBridge.call('hideToolbar');
		    WeixinJSBridge.call('hideOptionMenu');
		});
		$(function(){
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i)!="micromessenger") {
				$("body").text("请使用微信浏览器打开");
				return;
			}
			
			var num=0;
			$("#tbody tr").each(function(){
				var money=$(this).find("td").eq(4).text();
				num=num*1+money*1;
			});
			$("#totalMoney").text(num);
		});
		function deleteOrderDtl(e){
			var val = $(e).closest('tr').find('td:eq(0)').text();
			var val1 = $(e).closest('tr').find('td:eq(1)').text();
			var val2 = $(e).closest('tr').find('td:eq(2)').text();
			var val3 = $(e).closest('tr').find('td:eq(3)').text();
			var val4 = $(e).closest('tr').find('td:eq(4)').text();
			$("#dlagTbl").append("<tr style='border-top: solid #DCD7D6 1px;'>"+
									"<td style='display:none;'>"+val+"</td>"+
									"<td>"+val1+"×"+val3+"</td>"+
								"</tr>");
			$(e).closest('tr').remove();
			var total=$(e).closest('tr').find('td:eq(4)').text();
			var totalSum=$("#totalMoney").text();
			$("#totalMoney").text(totalSum*1-total*1);
		};
		function delOrderDtl(){
			InitLayer();
			var id=null;
			var dtl={};
			$("#dlagTbl tr").each(function(i){
				var val = $(this).find('td:eq(0)').text();
				if(id==null){
					id=val;
				}else{
					id=id+","+val;
				}
			});
			$.post("<%=path%>/pubitem/deleteOrderDtl.do?id="+id,dtl,function(data){ 
				$("#clo").click();
				closeLayer();
			});
		};
		function clearDtl(){
			InitLayer();
			$("#tbody tr").each(function(i){
				var val = $(this).find('td:eq(0)').text();
				var val1 = $(this).find('td:eq(1)').text();
				var val3 = $(this).find('td:eq(3)').text();
				$("#dlagTbl").append("<tr style='border-top: solid #DCD7D6 1px;'>"+
										"<td style='display:none;'>"+val+"</td>"+
										"<td>"+val1+"×"+val3+"</td>"+
									"</tr>");
				var total=$(this).find('td:eq(4)').text();
				var totalSum=$("#totalMoney").text();
				$("#totalMoney").text(totalSum*1-total*1);
			});
			$("#tbody tr").remove();
			closeLayer();
		}
		function flagDel(){
			var len=$("#dlagTbl tr").length;
			if(len>0){
				$("#a1").attr("href","#commiteSuccess");
				$("#a1").click();
			}
		}
   </script>
</body>
</html>