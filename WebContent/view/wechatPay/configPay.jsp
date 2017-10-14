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
#configPay{
	width:100%;height:100%;background:#efefef;margin:0px;padding:0px;text-shadow:0px 0px 0px;
}
#configPay-top{
	width:100%; 
	height:10%;
	background:url('<%=path %>/image/vippay.png') no-repeat;
	background-size: 105% 100%;
}
#configPay-main{
	width:95%;height:auto;
	border:1px solid #c1c1c1;
	border-radius:10px;
	background:#fff;
	margin:2% auto;
}
#configPay-table{
	width:100%;
	height:auto;
	border-spacing:0px;
	padding:1% 2%;
}
#configPay-table tr{
	width:100%;
	height:50px;
}
#configPay-table tr th{
	text-align:right;
	padding:2% 0px 2% 0px;
	border-bottom:1px dotted #c1c1c1;
}
#configPay-table tr td{
	text-align:left;
	padding:2% 3% 2% 0px;
	border-bottom:1px dotted #c1c1c1;
}
#configPay-table tr td input{
	width:100%;
}
#yespay{
	width:98%; 
	height:8%;
	margin:10% auto;
	background:url('<%=path %>/image/yespay.png') no-repeat;
	background-size: 100% 90%;
}
</style>
</head>
<body>
<div data-role="page" id="configPay">
<div id="configPay-top"></div>
	<div id="configPay-main">
		<table id="configPay-table" >
			<tbody>
				<tr>
					<td  colspan="2" style='color:red;text-align:center;padding:5%;font-size:24px;border:0px;'>请输入您的会员卡信息</td>
				</tr>
				<tr>
					<th style='width:20%;'><label for='name'>卡&nbsp;&nbsp;&nbsp;&nbsp;号：</label></th>
					<td style='width:80%;'><span><input type="text" data-role="none" name="name" id="name" onkeyup="changeNum(this)" value="" /></span></td>
				</tr>
				<tr>
					<th><label for='pass'>密&nbsp;&nbsp;&nbsp;&nbsp;码：</label></th>
					<td><span><input type="password" data-role="none" name="pass" id="pass" value="" /></span></td>
				</tr>
				<tr>
					<th><label for='tele'>手机号：</label></th>
					<td><span><input type="text" data-role="none" name="tele" id="tele" value="" /></span></td>
				</tr>
				<tr>
					<th style='border:0px;'><label for='valicode'>验证码：</label></th>
					<td style='border:0px;'>
						<input type="text" data-role="none" name="valicode" id="valicode" value="" style='width:55%;'/>
						<input type="button" style="font-size:14px;width:40%;" data-role="none" id="sendSms" value="获取验证码"/>
						<input type="hidden" data-role="none" name="openid" id="openid" value="${openid }" />
						<input type="hidden" id="rannum" value="" />
					</td>
				</tr>
			</tbody>
		</table>
    </div>
    <div id="yespay"></div>
  </div>
<form action="<%=path %>/pubitem/yespay.do" method="post" data-ajax="false">
<input type="hidden" id="orderid" name="orderid" value="${orders.id }" />
<input type="hidden" id="resv" name="resv" value="${orders.resv }" />
<input type="hidden" id="firmid" name="firmid" value="${orders.firmid }" />
<input type="hidden" id="firmdes" name="firmdes" value="${orders.firmdes }" />
<input type="hidden" id="dat" name="dat" value="${orders.dat }" />
<input type="hidden" id="pax" name="pax" value="${orders.pax }" />
<input type="hidden" id="sft" name="sft" value="${orders.sft }" />
<input type="hidden" id="tables" name="tables" value="${orders.tables }" />
<input type="hidden" id="contact" name="contact" value="${orders.contact }" />
<input type="hidden" id="addr" name="addr" value="${orders.addr }" />
<input type="hidden" id="rannum" name="rannum" value="${orders.rannum }" />
<input type="hidden" id="pubitem" name="pubitem" value="${orders.pubitem }" />
<input type="hidden" id="openid" name="openid" value="${orders.openid }" />
<input type="hidden" id="datmins" name="datmins" value="${orders.datmins }" />
<input type="hidden" id="remark" name="remark" value="${orders.remark }" />
<input type="hidden" id="tele" name="tele" value="${orders.tele }" />
<input type="hidden" id="isfeast" name="isfeast" value="${orders.isfeast }" />
<input type="hidden" id="listNetOrderDtl" name="listNetOrderDtl" value="${orders.listNetOrderDtl }" />

<input type="hidden" id="cardNo" name="cardNo" value="${cardNo }" />
<input type="hidden" id="outAmt" name="outAmt" value="${outAmt }" />
<input type="hidden" id="firmId" name="firmId" value="${firmId }" />
<input type="hidden" id="date" name="date" value="${date }" />
<input type="hidden" id=invoiceAmt name="invoiceAmt" value="${invoiceAmt }" />
<input type="hidden" id="empName" name="empName" value="${empName }" />
</form>
  <script type="text/javascript">
     	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		    WeixinJSBridge.call('hideToolbar');
		    WeixinJSBridge.call('hideOptionMenu');
		});
		$(function(){
			var wait=60;
			var timeID;
			function time() {
				if (wait == 0) {
					$("#sendSms").removeAttr("disabled");
					$("#sendSms").val("获取验证码");
					wait = 60;
				} else {
					$("#sendSms").attr("disabled",true);
					$("#sendSms").val("重新发送(" + wait + ")");
					wait--;
					timeID=setTimeout(function() {
						time();
					},
					1000);
				}
			}
			var validate = new Validate({
				validateItem:[{
					type:'text',
					validateObj:'name',
					validateType:['canNull','maxLength'],
					param:['F',10],
					error:['会员姓名不能为空','姓名最大值为10']
				},{
					type:'text',
					validateObj:'tele',
					validateType:['canNull'],
					param:['F'],
					error:['手机号码不能为空']
				},{
					type:'text',
					validateObj:'valicode',
					validateType:['canNull'],
					param:['F'],
					error:['验证码不能为空']
				},{
					type:'text',
					validateObj:'tele',
					validateType:['mobile'],
					error:['手机号码格式不正确']
				}]
			});
			$("#save").click(function(){
	 			if(validate._submitValidate()){
	 				var rannum=$("#rannum").val();
	 				var valicode=$("#valicode").val();
	 				if(rannum == valicode){
	 					
	 				}else{
	 					$("#valicode").css("background", "#F33");
	 				}
	 			}
			});
			$("#sendSms").click(function(){
				var validate1 = new Validate({
					validateItem:[{
						type:'text',
						validateObj:'name',
						validateType:['canNull'],
						param:['F'],
						error:['会员姓名不能为空']
					},{
						type:'text',
						validateObj:'tele',
						validateType:['canNull'],
						param:['F'],
						error:['手机号码不能为空']
					},{
						type:'text',
						validateObj:'tele',
						validateType:['mobile'],
						error:['手机号码格式不正确']
					}]
				});
	 			if(validate1._submitValidate()){
	 				time();
	 				var card={};
	 				card["tele"]=$("#tele").val();
	 				$.post("<%=path %>/card/sendSms.do",card,function(data){
	 					$("#rannum").val(data);
	 				});
	 			}
			});
		});
		function changeNum(e){
			var num=$(e).val();
 			if(num.length>10){
 				$(e).val(num.substr(0,10));
 			}
		}
   </script>
</body>
</html>
