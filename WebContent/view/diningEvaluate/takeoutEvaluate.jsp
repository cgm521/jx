<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8">
		<meta name="format-detection" content="telephone=no" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>订单评价</title>
		<link type="text/css" href='<c:url value="/css/jquery.mobile-1.4.0.min.css" />' rel="stylesheet" />
		<link type="text/css" href='<c:url value="/css/validate.css" />' rel="stylesheet" />
		<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/raty/jquery.raty.js'/>"></script>
		<%@include file="/view/dining/jAlerts.jsp"%>
		<script language="JavaScript" type="text/JavaScript">
			$(function(){
				// 如果已经评价过，隐藏提交评价按钮
				if("${hasEvaluated}" == "true") {
					$("#bott-c").hide();
					$("#remark").val("${evaInfo.remark}");
					$('#remark').attr("disabled",true);
				}
				
				// 初始化评价星级
				$('#entiStar').raty({
					target : "#function-hint",
					targetType: 'hint',
					hints: ['非常差','差', '一般', '好', '非常好' ],
					targetText: '全五星',
					targetKeep: true,
					size : 60,
					score: "${evaInfo.entiretyPoint}",
					readOnly: ${hasEvaluated},
					click: function (score, evt) {
						$("#entiValue").val(score);
					}
				});
				
				$("[name='starDiv']").width("200");
			});
			
			function save() {
				var evaluateInfo = {};
				evaluateInfo["ordersId"] = "${ordersId}";
				evaluateInfo["entiretyPoint"] = $("#entiValue").val();
				evaluateInfo["remark"] = $("#remark").val();
				
				// 保存评价信息
				$.post("<%=path %>/evaluate/saveEvaluate.do", evaluateInfo, function(data){
					alertMsg("感谢您的评价！");
					var uri = "<%=path %>/evaluate/takeOutEvaluate.do?ordersId=${ordersId}";
					window.location.href = uri;
				});
			}
		</script>
		<style type="text/css">
.header{
	width:100%;
	background-color:#ffb400;
	color:#FFFFFF;
	text-align:center;
	padding-bottom:10px;
	padding-top:10px;
	font-size:80%;
}
.starDiv{
	width:200px;
	border:1px solid #ffb400;
}
.mainTr{
	text-align:center;
	height:40px;
	border:1px solid #ffb400;
}
.titleTd{
	background-color:#ffb400;
}
.spaceMeaage{
	text-align:left;
	padding-top:5px;
	padding-bottom:5px;
	font-size:120%;
	padding-left:10px;
}
.contentDiv{
	width:100%;
	border:0;
	margin:0;
	padding:0;
	overflow:hidden;
	padding-top:5px;
	background-color:#F7F7F7
}
.contentDiv table tr{
	height:30px;
}
.contentDiv * p{
	height:60px;
}
.textContent{
	margin-top:10px;
	background-color:#FFFFFF;
	border:1px solid #EEEEEE;
	border-radius:4px;
	width:100%;
	color:#717171;
}
.bottDiv{
	width:100%;
	border:0px;
	margin:0px;
	padding:0px;
	height:50px;
	position:fixed;
}
.bottDiv table tr{
	height:50px;
}
#bott-a{
	background-color: #000000;
	border: none;
	text-align: left;
	width:40%;
	padding-left:10px;
}
#bott-a img{
	border:0;
	padding:0;
	height:28px;
	width:26px;
}
#ui-b{
	background-color: #000000;
	border: none;
	width:30%;
} 
#bott-c{
	background-color:#ffb400;
	color:#FFFFFF;
	width:25%;
	text-align:center;
}
		</style>
	</head>
	<body>
		<div data-role="page" data-theme="d" id="pageone" style="background-color:#F7F7F7"><!--页面层容器-->
			<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
				<div id="headDiv" class="header">
					<span>我们的每一点进步，都来自于您的支持</span>
				</div>
				<div class="contentDiv" style="margin-top:20%;list-style:none;padding:0;">
					<table align="center" style="width:95%;">
						<tr style="height:40px">
							<td  style="text-align:-webkit-center;"><span id="function-hint"  style="font-style:'宋体';font-size:1.4em;font-weight:bold"></span></td>
						</td>
						<tr class="mainTr" style="height:40px">
							<td stlye="text-align: -webkit-center;  ">
								<div id="entiStar"  name="starDiv" style="margin:0 auto; -webkit-transform:scale(1.5,1.5);  -moz-transform:scale(1.5,1.5); -transform:scale(1.5,1.5);">
								</div></td>
							<input type="hidden" id="entiValue" value="5" />
						</tr>
					</table>
				</div>
				<div class="spaceMeaage">
				<!--	<span>给我们留言</span>-->
				</div>
				<div class="contentDiv">
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
						<tr>
							<td>
								<textarea id="remark" maxlength="200" data-role="none" rows="6" 
									placeholder="写下您对商家和配送的评价吧(最多输入50字)" class="textContent"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="bottDiv" data-role="footer" data-role="popup" data-position="fixed" data-tap-toggle="false" data-history="false">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr>
						<td id="bott-a" style="width:75%;">
						</td>
						<td id="bott-c" onclick="javascript:save();">
							提交评价
						</td>
					</tr>
				</table>
		  	</div>
		</div><!-- page -->
	</body>
</html>