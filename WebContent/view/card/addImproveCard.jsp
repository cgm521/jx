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
<link rel="stylesheet" href="<%=path %>/css/default/global.css" />
<link rel="stylesheet" href="<%=path %>/css/default/card/addCard.css" />
　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
   <script src="<%=path %>/js/validate.js"></script>
   <script type="text/javascript" src="<%=path%>/js/validate.js"></script>
   <script src="<%=path %>/js/layer.js"></script>
   <%@include file="/view/dining/jAlerts.jsp"%>
</head>
<body>
	<div data-role="page" class="page">
		<div id="top" class="firstPage_top"><div><%=Commons.wx_title %></div></div>
		<div class="main" style="background:#e4e4e4;height:95%;overflow:hidden;">
			<div id="bkgimgHead">完善会员卡信息</div>
				<div style="margin:-1px 0 0 0;">
				<div id="bkgimg2">
		<%-- 			<div style="width:4%;"><img width="3%" src="<%=path %>/image/regLeft.png"></div> --%>
		<%-- 			<img src="<%=path %>/image/regTop.png" width="100%"> --%>
					<div id="inform">
					    <form action="" method="post" id="queryFrom" data-ajax="false">
					   		<div>
								<span><img src="<%=path %>/image/zhuce_01.png" height="13">&nbsp;会员姓名：</span>
								<span><input type="text" data-role="none" name="name" id="name" onkeyup="changeNum(this)" value="" /></span>
							</div>
							<img src="<%=path %>/image/regFenGX.png" width="100%" style="margin:0 0 0 -3%;">
							<div>
								<span><img src="<%=path %>/image/zhuce_02.png" height="13"></span>&nbsp;手机号码：
								<input type="text" data-role="none" name="tele" id="tele" value="" />
							</div>
							<img src="<%=path %>/image/regFenGX.png" width="100%" style="margin:0 0 0 -3%;">
							<div>
								<span><img src="<%=path %>/image/zhuce_02.png" height="13"></span>&nbsp;性别：
								<input type="text" data-role="none" name="tele" id="tele" value="" />
							</div>
							<img src="<%=path %>/image/regFenGX.png" width="100%" style="margin:0 0 0 -3%;">
							<div>
								<span><img src="<%=path %>/image/zhuce_02.png" height="13"></span>&nbsp;生日：
								<input type="text" data-role="none" name="tele" id="tele" value="" />
							</div>
							<img src="<%=path %>/image/regFenGX.png" width="100%" style="margin:0 0 0 -3%;">
							<div>
								<span><img src="<%=path %>/image/zhuce_02.png" height="13"></span>&nbsp;详细地址：
								<input type="text" data-role="none" name="tele" id="tele" value="" />
							</div>
							<img src="<%=path %>/image/regFenGX.png" width="100%" style="margin:0 0 0 -3%;">
							<div>
								<span><img src="<%=path %>/image/zhuce_02.png" height="13"></span>&nbsp;邮箱：
								<input type="text" data-role="none" name="tele" id="tele" value="" />
							</div>
							<img src="<%=path %>/image/regFenGX.png" width="100%" style="margin:0 0 0 -3%;">
							<div>
								<span><img src="<%=path %>/image/zhuce_03.png" height="13"></span>&nbsp;验证码：&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="text" data-role="none" name="valicode" id="valicode" value="" />
								<input type="button" style="font-size:12;" data-role="none" id="sendSms" value="获取验证码">
								<input type="hidden" data-role="none" name="openid" id="openid" value="${openid }" />
								<input type="hidden" id="rannum" value="111111">
							</div>
							<img src="<%=path %>/image/regFenGX.png" width="100%" style="margin:0 0 0 -3%;">
							<div>
								<span><img src="<%=path %>/image/huiyuanka_19.png" height="13"></span>&nbsp;卡类型：&nbsp;&nbsp;&nbsp;&nbsp;
				      			<select data-role="none" name="typDes"  id="typDes" >
				      				<c:forEach items="${listCardTyp }" var="cardTyp">
				      					<option value="${cardTyp.id }">${cardTyp.nam }</option>
				      				</c:forEach>
				      			</select>
							</div>
							<img src="<%=path %>/image/regFenGX.png" width="100%" style="margin:0 0 0 -3%;">
							<div class="next_button_div" style="width:50%;margin:15% auto;">
								<a data-role="button" id="save">下一步</a>
								<!-- <input type="button" data-role="none" id="save" value="下一步"> -->
							</div>
					   </form>
		<%-- 			   <img src="<%=path %>/image/regDown.png" width="100%"> --%>
				   </div>
		<%-- 		   <div style="width:4%;"><img src="<%=path %>/image/regRight.png"></div> --%>
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
			/**必须使用微信打开**/
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i)!="micromessenger") {
				$("body").text("请使用微信浏览器打开");
				return;
			}
// 			/* alertMsg($(document.body).width());//浏览器当前窗口文档body的宽度 */
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
					error:['不能空']
				},{
					type:'text',
					validateObj:'tele',
					validateType:['mobile'],
					error:['手机号码格式不正确']
				}]
			});
			$("#save").click(function(){
	 			if(validate._submitValidate()){
	 				InitLayer();
	 				var rannum=$("#rannum").val();
	 				var valicode=$("#valicode").val();
	 				if(rannum == valicode){
	 					$("#valicode").css("background", "#0F3");
	 					var card={};
	 					card["tele"]=$("#tele").val();
	 					card["openid"]=$("#openid").val();
	 					$.post("<%=path %>/card/listCardValidate.do",card,function(data){
	 						if(data==1){
	 							alertMsg("您已经是微信会员,请点击左上角的按钮返回");
	 							clearTimeout(timeID);
	 							$("#sendSms").val("获取验证码");
	 							$("input").attr("disabled","disabled");
	 							closeLayer();
	 						}else{
			 					$.post("<%=path %>/card/getTele.do",card,function(data){
			 				 		if("OK"==data.substring(0, 2)){
				 		 				if(confirm("您已经是会员用户，是否绑定微信会员")){
				 		 					$("#queryFrom").attr("action","<%=path%>/card/saveWXCard.do?cardNo="+data.substring(2,data.length));
					 		 				clearTimeout(timeID);
				 							$("#sendSms").val("获取验证码");
					 		 				$("#queryFrom").submit();
					 		 				$("#sendSms").attr("disabled","disabled");
				 							$("#save").attr("disabled","disabled");
		// 		 		 					card["cardNo"]=data.substring(2,data.length);
		// 		 		 					card["openid"]=$("#openid").val();
		<%-- 		 							$.post("<%=path %>/card/saveWXCard.do",card,function(data){ --%>
		// 		 		 						if(data !=null){
		// 		 		 							alertMsg("会员号"+data+"绑定手机成功");
		// 		 		 							clearTimeout(timeID);
		// 		 		 							$("#sendSms").val("获取验证码");
		// 		 		 							$("input").attr("disabled","disabled");
		// 		 		 							var witchid=$("#openid").val();
		<%-- 		 		 							$("#queryFrom").attr("action","<%=path %>/card/listCard.do"); --%>
		// 		 		 							$("#queryFrom").submit();
		<%-- 		 		 							$("#queryFrom").attr("action","<%=path%>/card/saveCard.do"); --%>
		// 		 		 						}else{
		// 		 		 							alertMsg("绑定手机失败");
		// 		 		 						}
		// 		 		 					});
				 		 				}else{
				 		 					$("#queryFrom").attr("action","<%=path%>/card/saveCard.do");
					 		 				clearTimeout(timeID);
				 							$("#sendSms").val("获取验证码");
					 		 				$("#queryFrom").submit();
					 		 				$("#sendSms").attr("disabled","disabled");
				 							$("#save").attr("disabled","disabled");
				 		 				}
				 		 			}else{
				 		 				
				 		 				$("#queryFrom").attr("action","<%=path%>/card/saveCard.do");
				 		 				clearTimeout(timeID);
			 							$("#sendSms").val("获取验证码");
				 		 				$("#queryFrom").submit();
				 		 				$("#sendSms").attr("disabled","disabled");
			 							$("#save").attr("disabled","disabled");
				 		 				
		// 		 		 				card["name"]=$("#name").val();
		// 	 		 					card["typDes"]=$("#typDes").val();
		// 	 		 					card["openid"]=$("#openid").val();
		// 	 		 						if(data!=null){
		// 	 		 							alertMsg("注册成功，您的卡号为："+data);
		// 	 		 							clearTimeout(timeID);
		// 	 		 							$("#sendSms").val("获取验证码");
		// 	 		 							$("input").attr("disabled","disabled");
		// 	 		 						}else{
		// 	 		 							alertMsg("注册失败");
		// 	 		 						}
		// 	 		 					});
				 		 			}
		 						});
	 						}
	 					});
	 				}else{
	 					closeLayer();
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