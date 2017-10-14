<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%String path=request.getContextPath(); %>
<html> 
　<head> 
　 <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/> 
　 <link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<link rel="stylesheet" href="<%=path %>/css/validate.css" />
<link rel="stylesheet" href="<%=path %>/css/default/global.css" />
　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/validate.js"></script>
<script type="text/javascript" src="<%=path%>/js/validate.js"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<title>修改绑定手机</title> 
<style type="text/css">
	#table1{
		width:95%;
		height:auto;
		text-align:left;
		margin:15px auto;
	}
	#table1 tr{
		width:100%;
		height:auto;
	}
	#table1 tr td{
		border-bottom:1px dotted grey;
	}
</style>
</head>
<body>
<div data-role="page" class="page">
	<div class="top">修改绑定手机</div>
	<div class="main">
		<form action="" method="post" id="queryFrom">
		<table id="table1">
			<col width="30%"/>
			<col width="70%"/>
			<tr>
				<td>
					<span><img src="<%=path %>/image/zhuce_02.png" height="13"></span>&nbsp;手机号码：
				</td>
				<td>
					<input type="text" data-role="none" name="tele" id="tele" value="" style="width:95%;"/>
				</td>
			</tr>
			<tr>
				<td>
					<span><img src="<%=path %>/image/zhuce_03.png" height="13"></span>&nbsp;验证码：
				</td>
				<td>
					<input type="text" data-role="none" name="valicode" id="valicode" value="" style="width:50%"/>
					<input type="button" style="font-size:12;" data-role="none" id="sendSms" value="获取验证码" style="width:35%">
					<input type="hidden" data-role="none" name="openid" id="openid" value="${openid }" />
				</td>
			</tr>
		</table>
		<div class="next_button_div" style="width:60%;">
			<a data-role="button"  id="save">下一步</a>
		</div>
		</form>
    </div>
</div>
<form action="<%=path %>/card/listCard.do?openid=${openid}" method="post" id="return_form" data-ajax="false"></form>
<%-- 	<div id="bkgimg2">
		<div id="inform">
		    <form action="" method="post" id="queryFrom">
				<div class="ui-field-contain">
					<span><img src="<%=path %>/image/zhuce_02.png" height="13"></span>&nbsp;手机号码：
					<input type="text" data-role="none" name="tele" id="tele" value="" />
				</div>
				<div class="ui-field-contain">
					<span><img src="<%=path %>/image/zhuce_03.png" height="13"></span>&nbsp;验证码：&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="text" data-role="none" name="valicode" id="valicode" value="" />
					<input type="button" style="font-size:12;" data-role="none" id="sendSms" value="获取验证码">
					<input type="hidden" data-role="none" name="openid" id="openid" value="${openid }" />
				</div>
				<div style="padding:15% 0 0 30%;">
					<input type="button" data-role="none" id="save">
				</div>
		   </form>
	   </div>
   </div> --%>
   <input type="hidden" id="rannum" value="">
      <script type="text/javascript">
		$(function(){
			
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i)=="micromessenger") {
		 	} else {
				$("body").text("请使用微信浏览器打开");
			}
			
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
	 					$("#valicode").css("background", "#0F3");
	 					var card={};
	 					card["tele"]=$("#tele").val();
	 					$.post("<%=path %>/card/getTele.do",card,function(data){
	 				 		if("OK"==data.substring(0, 2)){
		 		 				alertMsg("当前手机号码已经绑定过微信会员,请更换手机号");
		 		 			}else{
		 		 				if(confirm("是否确定绑定新的手机号码")){
		 		 					card["openid"]=$("#openid").val();
		 							$.post("<%=path %>/card/updateTele.do",card,function(data){
		 		 						if(data !=null){
		 		 							alertMsg("绑定新手机号码成功");
		 		 							clearTimeout(timeID);
		 		 							$("#sendSms").val("获取验证码");
		 		 							$("input").attr("disabled","disabled");
		 		 							$("#return_form").submit();
		 		 						}else{
		 		 							alertMsg("绑定手机失败");
		 		 						}
		 		 					});
		 		 				}
		 		 			}
	 					});
	 				}else{
	 					$("#valicode").css("background", "#F33");
	 				}
	 			}
			});
			$("#sendSms").click(function(){
				var validate1 = new Validate({
					validateItem:[{
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
   </script>
</body>
</html>