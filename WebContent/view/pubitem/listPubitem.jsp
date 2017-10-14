<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%String path=request.getContextPath(); %>
<html>
	<head>
	   <title></title>
	   <meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8"> 
	　 <link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
	   <link rel="stylesheet" href="<%=path %>/css/validate.css" />
	   <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
	　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
	   <script src="<%=path %>/js/validate.js"></script>
	    <style type="text/css">
	   		#mainLeft ul li{
	   			border:1px solid #CCC;
	   			background-color:#EDF;
	   		}
	   		#mainRight{
	   			width:70%;
	   			height:455px;
	   			border:1px solid #CCC;
	   			margin:0 0 0 29%;
	   			folat:right;
	   			overflow-y:scroll;
	   		}
	   		#myOrder1{
	   			width:70px;
	   			height:40px;
	   			border:1px solid transparent;
	   			background-image:url(<%=path %>/image/dcbtn_2.png);
	   			background-color:write;
	   			background-repeat:no-repeat;
	   			background-position: center;
	   			background-color: transparent;
	   			text-align:center;
	   			float:right; 
	   			position:absolute; 
	   			right:5px;
	   		}
		</style>
	</head>
	<body>
		<div>
			<div id="mainLeft" style="width:30%;height:100%;margin:0 0 0 -3%;float:left;">
    			<ul id="protypes" style="line-height:25x;list-style-type:none; " data-role="none">
    				<c:forEach var="protype" items="${listProjectType}">
    					<li><a href="#" style="text-decoration:none;" id="one"><input type="hidden" id="protypeId" value="${protype.id}" />${protype.des }</a></li>
    				</c:forEach>
    			</ul>
			</div>
			<div id="mainRight">
			    <table id="lltable">
					
				</table>
			</div>
			<form action="<%=path %>/pubitem/sessionOrder.do" method="post" id="queryForm">
				<input type="hidden" id="id" name="id" value="${id }" />
				<input type="hidden" id="pk_pub" name="pubitem" value=""/>
				<input type="button" id="myOrder1" data-role="none" value="">
			</form>
		</div>
		<script type="text/javascript">
			$(document).ready(function(){
				var ua = navigator.userAgent.toLowerCase();
				if(ua.match(/MicroMessenger/i)!="micromessenger") {
					$("body").text("请使用微信浏览器打开");
					return;
				}
				
				$("#myOrder1").click(function(){
					$("#myOrder1").css("background-image","url('<%=path %>/image/dcbtn_01.png')");
					var orderid=$("#pk_pub").val();
					if (orderid == '' || orderid == null || orderid == undefined) {
						$("#myOrder1").css("background-image","url('<%=path %>/image/dcbtn_01.png')");
						alert("您还没有选菜");			
						$("#myOrder1").css("background-image","url('<%=path %>/image/dcbtn_1.png')");
					}else{
						$("#myOrder1").css("background-image","url('<%=path %>/image/dcbtn_01.png')");
						$("#queryForm").submit();
					}
				});
				$("ul li").each(function(){
					$(this).click(function(){
						$("#mainLeft ul li").css("background-color","#EDF");
						$(this).css("background-color","#CCA");
						var protyp={};
						var protypid=$(this).find("input").val();
						$.post("<%=path %>/pubitem/listPubitemDtl.do?projecttyp="+protypid,protyp,function(data){
							 $("#lltable").find("tr").remove();
							for(var i=0;i<data.length;i++){
								$("#lltable").append("<tr>"+
								"<td style='display:none;'>"+data[i].pubitem+"</td>"+
								"<td style='display:none;'>"+data[i].pitcode+"</td>"+
								"<td style='display:none;'>"+data[i].pinit+"</td>"+
								"<td>"+
									"<ul style='cursor: pointer;list-style-type:none;line-height:20px;'>"+
										"<li style='float:left;'><img width='60' height='60' src='<%=path %>/image/kaoruzhu.png' id='img_"+data[i].pitcode+"'></li>"+
										"<li style='padding:0 0 0 10px;float:left;'>"+
											"<span>"+data[i].pdes+"</span><br>"+
											"<span>"+data[i].price+"&nbsp;元</span><br>"+
											"<a id='btnOrder_"+data[i].pitcode+"' onclick='btnOrder("+data[i].pitcode+")'>我要点餐</a>"+
										"</li>"+
									"</ul>"+
								"</td>"+
							"</tr>");
							}
						});
					});
				});
			});
			function btnOrder(code){
				var orderid=$("#pk_pub").val();
				if (orderid == '' || orderid == null || orderid == undefined) {
					$("#pk_pub").val(code);
				}else{
					var ord=orderid+","+code;
					$("#pk_pub").val(ord);
				}
				var a=$("#img_"+code);
				var b = createPic(a);
				$("body").append(b);
				var offset = $("#myOrder1");
				//点击我要点餐按钮时，对应图片执行动画效果
				b.animate({
					right:offset.right+10,
					top:offset.top+5,
					width:"20px",
					height:"20px"
				},1000).animate({
					right:offset.right+10,
					top:offset.top-20,
					width:"20px",
					height:"20px"
				},250).animate({
					right:offset.right+10,
					top:offset.top+5,
					width:"20px",
					height:"20px"
				},250,function(){
					b.remove();
				});
			}
			//创建动画图片的方法
			function createPic(oldPic){
				var offset = oldPic.offset();
				var left = offset.left;
				var top = offset.top;
				var pic = $("<img src="+oldPic.attr("src")+" style='width:140px;height:90px'></img>");
				pic.css({
				    position: "absolute",
				    left: left,
				    top: top
				});
				return pic;
			}
		</script>
	</body>
</html>