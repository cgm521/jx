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
					score: "${evaInfo.entiretyPoint}",
					readOnly: ${hasEvaluated},
					click: function (score, evt) {
						$("#entiValue").val(score);
					}
				});
				$('#tasteStar').raty({
					score: "${evaInfo.tastePoint}",
					readOnly: ${hasEvaluated},
					click: function (score, evt) {
						$("#tasteValue").val(score);
					}
				});
				$('#envStar').raty({
					score: "${evaInfo.envPoint}",
					readOnly: ${hasEvaluated},
					click: function (score, evt) {
						$("#envValue").val(score);
					}
				});
				$('#serviceStar').raty({
					score: "${evaInfo.servicePoint}",
					readOnly: ${hasEvaluated},
					click: function (score, evt) {
						$("#serviceValue").val(score);
					}
				});
				
				// 初始化菜品评价星级
				<c:forEach items="${orderDtl}" var="dtl">
					$('#${dtl.pkPubitem}_star').raty({
						score: "${dtl.point}",
						readOnly: ${hasEvaluated},
						click: function (score, evt) {
							$("#${dtl.pkPubitem}Value").val(score);
						}
					});
				</c:forEach>
				
				$("[name='starDiv']").width("200");
			});
			
			function save() {
				var evaluateInfo = {};
				evaluateInfo["ordersId"] = "${ordersId}";
				evaluateInfo["entiretyPoint"] = $("#entiValue").val();
				evaluateInfo["tastePoint"] = $("#tasteValue").val();
				evaluateInfo["envPoint"] = $("#envValue").val();
				evaluateInfo["servicePoint"] = $("#serviceValue").val();
				evaluateInfo["remark"] = $("#remark").val();
				
				<c:forEach items="${orderDtl}" var="dtl" varStatus="sta">
				    evaluateInfo["evaluateDtlList[${sta.index}].ordersId"] = "${ordersId}";
				    evaluateInfo["evaluateDtlList[${sta.index}].pkPubitem"] = "${dtl.pkPubitem}";
				    evaluateInfo["evaluateDtlList[${sta.index}].point"] = $("#${dtl.pkPubitem}Value").val();
				</c:forEach>
				
				// 保存评价信息
				$.post("<%=path %>/evaluate/saveEvaluate.do", evaluateInfo, function(data){
					alertMsg("感谢您的评价！");
					var uri = "<%=path %>/evaluate/toEvaluate.do?ordersId=${ordersId}";
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
		<div data-role="page" data-theme="d" id="pageone"><!--页面层容器-->
			<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
				<div id="headDiv" class="header">
					<span>我们的每一点进步，都来自于您的支持</span>
				</div>
				<div class="contentDiv">
					<table align="center" style="width:95%;">
						<tr class="mainTr">
							<td width="30%" class="titleTd"><span>总体评价：</span></td>
							<td width="70%"><div id="entiStar" name="starDiv"></div></td>
							<input type="hidden" id="entiValue" value="5" />
						</tr>
						<tr class="mainTr">
							<td width="30%" class="titleTd"><span>口味：</span></td>
							<td width="70%"><div id="tasteStar" name="starDiv"></div></td>
							<input type="hidden" id="tasteValue" value="5" />
						</tr>
						<tr class="mainTr">
							<td width="30%" class="titleTd"><span>环境：</span></td>
							<td width="70%"><div id="envStar" name="starDiv"></div></td>
							<input type="hidden" id="envValue" value="5" />
						</tr>
						<tr class="mainTr">
							<td width="30%" class="titleTd"><span>服务：</span></td>
							<td width="70%"><div id="serviceStar" name="starDiv"></div></td>
							<input type="hidden" id="serviceValue" value="5" />
						</tr>
					</table>
				</div>
				<div class="spaceMeaage">
					<span>给我们留言</span>
				</div>
				<div class="contentDiv">
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
						<tr>
							<td>
								<textarea id="remark" maxlength="200" data-role="none" rows="4" 
									placeholder="期待您的建议(最多输入50字)" class="textContent"></textarea>
							</td>
						</tr>
					</table>
				</div>
				<div class="spaceMeaage">
					<span>菜品评价</span>
				</div>
				<div class="contentDiv">
					<table align="center" style="width:95%;">
						<c:forEach items="${orderDtl }" var="dtl">
							<tr>
								<td width="40%"><span>${dtl.vname }：</span></td>
								<td width="60%"><div id="${dtl.pkPubitem }_star" name="starDiv"></div></td>
								<input type="hidden" id="${dtl.pkPubitem }Value" value="5" />
							</tr>
						</c:forEach>
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