<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%String path=request.getContextPath(); %>
<html>
	<head>
	   <title>我的点菜</title>
	   <meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8"> 
	　 <link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
	   <link rel="stylesheet" href="<%=path %>/css/validate.css" />
	　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
	　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
	   <script src="<%=path %>/js/validate.js"></script>
	   <style type="text/css">
			body { 
	   			font:11px/22px Verdana, Geneva, sans-serif; 
	   			background:#fff; color:#333; 
	   			padding:0; 
	   			margin:0;
	   		}
	   		
		</style>
	</head>
	<body>
		<input type="button" id="btnSub" name="btnSub" value="保存订单" />
		<div>
			<div id="main">
				<div id="mainLeft" style="width:100%;height:100%;margin:0 0 0 0;float:left;">
					<table>
						<thead>
							<tr>
								<td style="display:none;">菜品主键</td>
								<td style="display:none;">订单主键</td>
								<td>菜品名称</td>
								<td>菜品数量</td>
								<td>菜品单价</td>
								<td>菜品总金额</td>
							</tr>
						</thead>
					</table>
					<table id="tBody">
						<tbody>
							<c:forEach var="project" items="${listProject}" varStatus="status">
								<tr>
									<td style="display:none;">${project.pubitem }</td>
									<td style="display:none;">${id }</td>
									<td>${project.pdes }</td>
									<td><input type="text" id="num" size="10" value="1" onkeyup="changeNum(this)" /></td>
									<td>${project.price }</td>
									<td>${project.price }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			$(function(){
				var ua = navigator.userAgent.toLowerCase();
				if(ua.match(/MicroMessenger/i)!="micromessenger") {
					$("body").text("请使用微信浏览器打开");
					return;
				}
				
				$("#btnSub").click(function(){
					var order={};
					var flag=true;
					$("#tBody").find('tr').each(function(i){
						var num1=$(this).find('td').eq(3).find('input').val();
						if(num1==0 || num1==null || !num1.match("^[0-9]*[0-9][0-9]*$")){
							alert("第"+(i+1)+"行的数量不正确");
							flag=false;
							return;
						}
						order["listNetOrderDtl["+i+"].foodsid"]=$(this).find('td').eq(0).text();
						order["listNetOrderDtl["+i+"].ordersid"]=$(this).find('td').eq(1).text();
						order["listNetOrderDtl["+i+"].foodsname"]=$(this).find('td').eq(2).text();
						order["listNetOrderDtl["+i+"].foodnum"]=$(this).find('td').eq(3).find('input').val();
						order["listNetOrderDtl["+i+"].price"]=$(this).find('td').eq(4).text();
						order["listNetOrderDtl["+i+"].totalprice"]=$(this).find('td').eq(5).text();
					});
					if(!flag){
						return;	
					}else{
						$.post("<%=path %>/pubitem/saveOrderDtl.do",order,function(data){
							if(data==1){
								alert("点菜成功");
								$("#btnSub").attr("disabled","disabled");
							}else{
								alert("点菜失败");
							}
						});
					}
				});
			});
			function changeNum(e){
				var num=$(e).val();
				var price=$(e).closest('td').next().text();
					if(num.match("^[0-9]*[0-9][0-9]*$")){
						$(e).closest('td').next().next().text(num*price);
					}else{
						alert("数量只能为大于0的数字");
						
					}
			}
		</script>
	</body>
</html>