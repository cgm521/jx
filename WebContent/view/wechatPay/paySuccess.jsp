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
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/validate.js"></script>
<script src="<%=path %>/js/layer.js"></script>
<style type="text/css">
body { 
	font-size:16px/22px; 
	background:#fff; color:#333; 
	font-weight: normal;
	font-family:Verdana, "微软雅黑", Arial,Sans;
	padding:0; 
	margin:0;
}
#paySuccess{
	width:100%;height:100%;background:#ededed;margin:0px;padding:0px;text-shadow:0px 0px 0px;
}
#paySuccess-top{
	width:100%; 
	height:10%;
	background:url('<%=path %>/image/paySuccess_top.png') no-repeat;
	background-size: 100% 100%;
}
#paySuccess-main{
	width:95%;height:90%;
	margin:2% auto;
}
#paySucMain1{
	width:70%;
	height:20%;
	margin:0px auto;
	background:url('<%=path %>/image/paySuccess1.png') no-repeat;
	background-size: 105% 100%;
}
#paySucMain2{
	width:100%;
	height:6%;
	background:url('<%=path %>/image/paySuccess2.png') no-repeat;
	background-size: 100% 95%;
}
#paySucMain-table{
	width:100%;
	height:auto;
	border-spacing:0px;
	padding:1% 2%;
	font-size:26px;
}
#paySucMain-table tr{
	width:100%;
	height:50px;
}
#paySucMain-table tr th{
	text-align:right;
	padding:2% 3% 2% 0px;
}
#paySucMain-table tr td{
	text-align:left;
	padding:2% 3% 2% 0px;
}
#finishPay{
	width:98%; 
	height:8%;
	margin:10% auto;
	background:url('<%=path %>/image/finishPay.png') no-repeat;
	background-size: 100% 90%;
}
</style>
</head>
<body>
<div data-role="page" id="paySuccess">
<div id="paySuccess-top"></div>
	<div id="paySuccess-main">
		<div id="paySucMain1"></div>
		<div id="paySucMain2"></div>
		<table id="paySucMain-table" >
			<tbody>
				<tr>
					<th>应付金额：</th>
					<td><span style='color:red;'>100.00</span>元</td>
				</tr>
				<tr>
					<th>已付金额：</th>
					<td><span style='color:red;'>100.00</span>元</td>
				</tr>
				<tr>
					<th>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额：</th>
					<td><span style='color:red;'>100.00</span>元</td>
				</tr>
			</tbody>
		</table>
		<div id="finishPay"></div>
   </div>
 </div>
<script type="text/javascript">
$(document).on("pageinit","#payMenuPage",function(){
	/*完成支付*/
	$("#finishPay").on("tap",function(){
		window.close();
	});
	
});
</script>
</body>
</html>
