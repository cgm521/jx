<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>店铺列表</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/bookDesk.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script language="JavaScript" type="text/JavaScript">
	/*去除安卓返回及刷新按钮*/
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
	});
	
	//更改城市
	function changCity(pk_city,vname){
		var pk_group = $("#pk_group").val();
		var oldCity = $("#pk_city").val();
		var openid = $("#openid").val();
		
		$("#city_close").click();//关闭选择框
		
		if(oldCity == pk_city){
			return;
		}
		
		$("#pk_city").val(pk_city);
		$("#cityName").text(vname);
		
		var htmlStr = "";
		InitLayer();
		
		$.ajax({
			url:"<c:url value='/bookMeal/choiceFirm.do'/>",
			type:"POST",
			dataType:"json",
			data:{"pk_group":pk_group,"pk_city":pk_city,"openid":openid},
			success:function(json){
				$.each(json,function(i,item){
					var addrValue="&nbsp;";
					if(item.addr!=null && item.addr!=""){
						addrValue = item.addr + addrValue;
					}
					htmlStr = htmlStr
								+ "<tr class='store_tr1'>"
								+ "<td class='store_td1' style='border-bottom:1px dashed gray' onclick= \"JavaScript:gotoMenu('"+ item.firmid +"');\">";
					if(item.wbigpic != null && item.wbigpic != "") {
						htmlStr = htmlStr + "<img src='<%=Commons.vpiceure %>"+item.wbigpic + "'/>";
					} else {
						htmlStr = htmlStr + "<img src='<%=path %>/image/wechat/selectCity.png'/>";
					}
					htmlStr = htmlStr
								+ "</td>"
								+ "<td class='store_td2' style='border-bottom:1px dashed gray' onclick= \"JavaScript:gotoMenu('"+ item.firmid +"');\">"
								+ "<span class='firmdes'>" + item.firmdes + "</span><br/>"
								+ "<span class='addr'>" + addrValue + "</span>"
								+ "</td>"
								+ "</tr>"
	        	});
				$("#listFirm").html(htmlStr);//填充新内容
				closeLayer();
			},
			error:function(ajax){
				closeLayer();
			}
		});
	}
	
	//转向菜单页面
	function gotoMenu(firmid){
		if(firmid==null || firmid==""){
			return;
		}
		
		InitLayer();
		window.setTimeout(function(){
			$("#firmid").val(firmid);
			$('#menu').submit();
		},100);
	}
</script>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div role="main" class="ui-content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<form id="menu" method="POST" data-ajax="false" action="<c:url value='/bookMeal/gotoMenu.do' />">
			<input type="hidden" name="openid" id="openid" value="${openid}">
			<input type="hidden" name="pk_group" id="pk_group" value="${pk_group}">
			<input type="hidden" name="pk_city" id="pk_city" value="${userCity.pk_city}">
			<input type="hidden" name="code" id="code" value="${code}">
			<input type="hidden" name="firmid" id="firmid" value="">
			<input type="hidden" name="numfirmid" id="numfirmid" value="">
		</form>
		<div style="width:100%; height: 60px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr height="45px">
						<td class="td_left" id="cityName">${userCity.vname}</td>
						<td class="td_right">
							<div data-role="popup" id="popupArrow" data-arrow="true" data-shadow="false" data-theme="a" data-history="false">
							<a href="#" data-rel="back" id="city_close" style="visibility:hidden;" class="ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right">close</a>
							<ul data-role="listview" data-inset="true" style="min-width:100px;">
								<!-- <li data-role="list-divider"><fmt:message key="changeCity" /></li> -->
								<c:forEach items="${listCity}" var="t" varStatus="stat">
									<c:if test="${stat.count == 1 }">
										<li data-icon="false">
											<a href="javaScript:changCity('${t.pk_city}','${t.vname}');" style="text-align:center; background-color:#FFFFFF; border-top:none">
												<span style="font-weight:normal; color:#7C7C7C">${t.vname}</span>
											</a>
										</li>
									</c:if>
									<c:if test="${stat.count > 1 }">
										<li data-icon="false">
											<a href="javaScript:changCity('${t.pk_city}','${t.vname}');" style="text-align:center; background-color:#FFFFFF; border-top:1px dashed #D5D5D5">
												<span style="font-weight:normal; color:#7C7C7C">${t.vname}</span>
											</a>
										</li>
									</c:if>
								</c:forEach>
							</ul>
							</div>
						<a href="#popupArrow" data-rel="popup" data-transition="slideup"><img src="<c:url value='/image/wechat/selectCity.png'/>" alt="切换城市"/></a>
						</td>
					</tr>
				</table>
		</div>
		<div id="selectSpace">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="td_left"><fmt:message key="canSelectStore" /></td>
				<td class="td_right">&nbsp;</td>
			</tr>
			</table>
		</div>
		<div id="selectStore">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" id="listFirm">
				<c:forEach items="${listFirm}" var="t">
					<tr class="store_tr1" style="height:80px;">
						<td class="store_td1" style="border-bottom:1px dashed gray" onclick= "JavaScript:gotoMenu('${t.firmid}');">
							<c:choose>
								<c:when test="${t.wbigpic ne null && t.wbigpic ne '' }">
									<img src="<%=Commons.vpiceure %>${t.wbigpic}"/>
								</c:when>
								<c:otherwise>
									<img src="<c:url value='/image/wechat/selectCity.png'/>" />
								</c:otherwise>
							</c:choose>
						</td>
						<td class="store_td2" style="border-bottom:1px dashed gray" onclick= "JavaScript:gotoMenu('${t.firmid}');">
							<span class="firmdes">${t.firmdes}</span><br/>
							<span class="addr">${t.addr}&nbsp;</span>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
　　</div><!-- content -->
</div><!-- page -->
</body>
</html>