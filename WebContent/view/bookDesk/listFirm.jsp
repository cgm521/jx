<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"  content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
<title>店铺列表</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/bookDesk.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script language="JavaScript" type="text/JavaScript">
/*去除安卓返回及刷新按钮*/
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});
$(function(){
	/* */
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)!="micromessenger") {
		$("body").text("请使用微信浏览器打开");
		return;
	}
	
	$("#overlay-screen").bind("touchmove",function(){
		return false;//去除jquery自带遮罩滑动行为
	});	
	$("#leftSft").bind("touchmove",function(){
		return false;//去除jquery自带遮罩滑动行为
	});
});

var myScroll; 
function loaded() { 
    //myScroll = new iScroll('wrapper',{hScrollbar:false,vScrollbar:false});
    setTimeout(function(){myscroll=new iScroll("wrapper",{hScrollbar:false,vScrollbar:false});},100);
} 
window.addEventListener("load",loaded,false); 
	//更改城市
	function changCity(pk_city,vname){
		$("#popupArrow").popup("close");//关闭选择框
		
		var pk_group = $("#pk_group").val();
		//var pk_city = $("#pk_city").val();
		var dat = $("#dat").val();
		var sft = $("#sft").val();
		var oldCity = $("#pk_city").val();
		
		if(oldCity == pk_city){
			$("#popupArrow").popup("close");//关闭选择框
			return;
		}
		
		$("#pk_city").val(pk_city);
		$("#cityName").text(vname);
		
		var htmlStr = "";
		var imgValue = "";
		InitLayer();
		$.ajax({
			url:"<c:url value='/bookDesk/choiceFirm.do'/>",
			type:"POST",
			dataType:"json",
			data:{"pk_group":pk_group,"pk_city":pk_city,"dat":dat,"sft":sft},
			success:function(json){
				//alert(JSON.stringify(json));
				//$("#listFirm").html("");
				var addrValue="&nbsp;";
				$.each(json,function(i,item){
					addrValue="&nbsp;";
					if(item.addr!=null && item.addr!=""){
						addrValue = item.addr;
					}
					if(item.wbigpic==null || ""==item.wbigpic){
						imgValue = "<img src=\""+"<c:url value='/image/wechat/1102.jpg'/>"+"\"/>";
					}else{
						imgValue = "<img src=\"" + "<%=Commons.vpiceure %>" + item.wbigpic + "\"/>"
					}
					htmlStr = htmlStr
								+ "<tr class='store_tr1' onclick= \"JavaScript:gotoOrder('"+ item.firmid +"');\">"
								+ "<td class='store_td1'>"
								+ imgValue
								+ "</td>"
								+ "<td class='store_td2'>"
								+ "<div class='firmdes'>" + item.firmdes + "</div>"
								+ "<div class='addr'>" + addrValue + "</div>"
								+ "</td>"
								+ "</tr>"
								+ "<tr class='store_tr2'>"
								+ "<td colspan='2'>"
								+ "<div class='desk'>"
								+ "<div class='space-left'>&nbsp;</div>"
								+ "<div class='deskMessage'>"
								+ "	<span>";
					$.each(item.listStoreTable,function(j,test){
						/*
						if("大厅" == test.roomtyp){
							htmlStr = htmlStr + test.pax + "人桌&nbsp;" + test.num + "&nbsp;";
						}else{
							htmlStr = htmlStr + test.pax + "人" + test.roomtyp + "&nbsp;" + test.num + "&nbsp;";
						}
						*/
						htmlStr = htmlStr + test.roomtyp + "&nbsp;" + test.num + "&nbsp;&nbsp;&nbsp;";
					});
					htmlStr = htmlStr + "&nbsp;</span>"
								+ "</div>"
								+ "<div style='float:left;width:15%;padding-top:10px;padding-bottom:10px;'>"
								+ "<span style='background-color:#00db37;width:100%;color:#FFFFFF;border-radius:5px;padding:4px;font-size:90%;' onclick=\"JavaScript:gotoMap('"
								+ item.firmdes +"','" + item.addr + "','" + item.position +"');\">到这去</span>"
								+ "</div>"
								+ "</div>"
								+ "<div class='space'>&nbsp;</div>"
								+ "</td>"
								+ "</tr>";
	        	});
				$("#listFirm").html(htmlStr);//填充新内容
				closeLayer();
				setPageOneHeight();
			},
			error:function(ajax){
				closeLayer();
				alert("error");
			}
		});
	}
	//更改日期
	function changeDate(td_on){
		if($(td_on).hasClass("td-left-on")){
			return;
		}
		$(".td-left-on").attr("class","td-left-lev");
		$(td_on).attr("class","td-left-on");
	}
	//更改餐次
	function changeSft(sftCode,sftName){
		var oldDat = $("#dat").val();
		var oldSft =  $("#sft").val();
		
		$("#sft").val(sftCode);
		
		var dateE = $(".td-left-on").find("span").text();
		
		if(oldDat==dateE.substr(0,10) && oldSft==sftCode){
			return;
		}
		
		$("#dat").val(dateE.substr(0,10));
		
		var content = dateE.substr(0,10) + "&nbsp;" + sftName;
		$("#sft_a").html(content);
		
		$("#overlay").popup("close");//关闭选择框
		
		var pk_city = $("#pk_city").val();
		var pk_group = $("#pk_group").val();
		//var pk_city = $("#pk_city").val();
		var dat = $("#dat").val();
		var sft = $("#sft").val();
		
		var htmlStr = "";
		var imgValue = "";
		InitLayer();
		$.ajax({
			url:"<c:url value='/bookDesk/choiceFirm.do'/>",
			type:"POST",
			dataType:"json",
			data:{"pk_group":pk_group,"pk_city":pk_city,"dat":dat,"sft":sft},
			success:function(json){
				//alert(JSON.stringify(json));
				//$("#listFirm").html("");
				var addrValue="&nbsp;";
				$.each(json,function(i,item){
					addrValue="&nbsp;";
					if(item.addr!=null && item.addr!=""){
						addrValue = item.addr;
					}
					if(item.wbigpic==null || ""==item.wbigpic){
						imgValue = "<img src=\""+"<c:url value='/image/wechat/1102.jpg'/>"+"\"/>";
					}else{
						imgValue = "<img src=\"" + "<%=Commons.vpiceure %>" + item.wbigpic + "\"/>"
					}
					htmlStr = htmlStr
								+ "<tr class='store_tr1' onclick= \"JavaScript:gotoOrder('"+ item.firmid +"');\">"
								+ "<td class='store_td1'>"
								+ imgValue
								+ "</td>"
								+ "<td class='store_td2'>"
								+ "<div class='firmdes'>" + item.firmdes + "</div>"
								+ "<div class='addr'>" + addrValue + "</div>"
								+ "</td>"
								+ "</tr>"
								+ "<tr class='store_tr2'>"
								+ "<td colspan='2'>"
								+ "<div class='desk'>"
								+ "<div class='space-left'>&nbsp;</div>"
								+ "<div class='deskMessage'>"
								+ "	<span>";
					$.each(item.listStoreTable,function(j,test){
						/*
						if("大厅" == test.roomtyp){
							htmlStr = htmlStr + test.pax + "人桌&nbsp;" + test.num + "&nbsp;";
						}else{
							htmlStr = htmlStr + test.pax + "人" + test.roomtyp + "&nbsp;" + test.num + "&nbsp;";
						}
						*/
						htmlStr = htmlStr + test.roomtyp + "&nbsp;" + test.num + "&nbsp;&nbsp;&nbsp;";
					});
					htmlStr = htmlStr + "&nbsp;</span>"
								+ "</div>"
								+ "<div style='float:left;width:15%;padding-top:10px;padding-bottom:10px;'>"
								+ "<span style='background-color:#00db37;width:100%;color:#FFFFFF;border-radius:5px;padding:5px;font-size:90%;' onclick=\"JavaScript:gotoMap('"
								+ item.firmdes +"','" + item.addr + "','" + item.position +"');\">到这去</span>"
								+ "</div>"
								+ "</div>"
								+ "<div class='space'>&nbsp;</div>"
								+ "</td>"
								+ "</tr>";
	        	});
				$("#listFirm").html(htmlStr);//填充新内容
				closeLayer();
				setPageOneHeight();
			},
			error:function(ajax){
				closeLayer();
				alert("error");
			}
		});
		
	}
	
	//转向订单页面
	function gotoOrder(firmid) {
		if (firmid == null || firmid == "") {
			return;
		}
		InitLayer();
		window.setTimeout(function(){
			$("#firmid").val(firmid);
			$('#order').submit();
		},100);
	}
	function openSelectDate(){
		//alert("ph:"+ph+"\nwh:"+wh+"\ndh:"+dh);
		//if(ph < wh){
		//	$('#pageone').css("height","100%") ;
		//}
		//setTimeout(function(){ myScroll.refresh(); }, 0);
		$("#overlay").popup("open");
	}
	function setPageOneHeight(){
		/**
		*多数据页面转到少数据页面时，pageone后有一块空白
		*初始页面是少数据页面时没有此种情况
		*/
		$('#pageone').css("height","");
		var ph = parseInt($("#pageone").height());
		var wh = parseInt($(window).height());
		if(ph < wh){
			$('#pageone').css("height","100%");
		}
	}
	function gotoMap(firmName,addr,position){
		var address = $("#address").val();//定位地址
		var LatLng = $("#LatLng").val();//定位地址
		
		if(LatLng==null || ""==LatLng){
			alert("无法获取您当前的位置");
			return;
		}
		
		if(address==null || ""==address){
			alert("无法获取您当前的位置");
			return;
		}
		$("#cityMap").val($("#cityName").text());
		$("#firmNameMap").val(firmName);
		$("#addr").val(addr);//门店地址
		$("#position").val(position);//门店地址
		InitLayer();
		window.setTimeout(function(){
			$('#map').submit();
		},100);
	}
</script>
</head>
<body>
<div data-role="page" data-theme="d" id="pageone"><!--页面层容器-->
	<div data-role="content" style="margin:0;list-style:none;padding:0;"><!--页面主体-->
		<form id="order" method="POST" data-ajax="false" action="<c:url value='/bookDesk/gotoOrder.do' />">
			<input type="hidden" name="openid" id="openid" value="${openid}">
			<input type="hidden" name="pk_group" id="pk_group" value="${pk_group}">
			<input type="hidden" name="pk_city" id="pk_city" value="${userCity.pk_city}">
			<input type="hidden" name="dat" id="dat" value="${dat}">
			<input type="hidden" name="sft" id="sft" value="${sft.code}">
			<input type="hidden" name="firmid" id="firmid" value="">
		</form>
		<form id="map"  method="POST" data-ajax="false" action="<c:url value='/bookDesk/gotoMap.do' />">
			<input type="hidden" name="openid" id="openidMap" value="${openid}">
			<input type="hidden" name="pk_group" id="pk_groupMap" value="${pk_group}">
			<input type="hidden" name="address" id="address" value="${address}">
			<input type="hidden" name="city" id="cityMap" value="">
			<input type="hidden" name="firmName" id="firmNameMap" value="">
			<input type="hidden" name="addr" id="addr" value="">
			<input type="hidden" name="LatLng" id="LatLng" value="${LatLng}">
			<input type="hidden" name="position" id="position" value="">
		</form>
		<div id="header">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr>
						<td class="td_left" id="cityName">${userCity.vname}</td>
						<td class="td_right">
							<div data-role="popup" id="popupArrow" data-arrow="true" data-shadow="false" data-theme="a" data-history="false">
							<ul data-role="listview" data-inset="true" style="min-width:100px;">
								<c:forEach items="${listCity}" var="t" varStatus="stat">
									<c:if test="${stat.count == 1 }">
										<li data-icon="false">
											<a href="javaScript:changCity('${t.pk_city}','${t.vname}');" style="text-align:center; background-color:#FFFFFF; border:none">
												<span style="font-weight:normal; color:#7C7C7C">${t.vname}</span>
											</a>
										</li>
									</c:if>
									<c:if test="${stat.count > 1 }">
										<li data-icon="false">
											<a href="javaScript:changCity('${t.pk_city}','${t.vname}');" style="text-align:center; background-color:#FFFFFF; border:none;border-top:1px dashed #D5D5D5">
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
		<div id="storeDate">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="td_left_store"><fmt:message key="bookdesk_date" /></td>
				<td class="td_right_store">
					<a href="JavaScript:openSelectDate();" id="sft_a">${dat}&nbsp;${sft.name}</a>
					<div id="overlay" data-role="popup" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
						<ul data-role="listview" data-inset="false">
							<li data-role="list-divider" style="text-align:center;"><fmt:message key="selectBookdate" /></li>
						</ul>
						<div id="sftList">
							<div id="wrapper"><!-- 日期 -->
								<c:set var="numDate" value="1" />
								<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" id="scroller">
									<c:forEach items="${listDate}" var="d">
									<tr>
									<c:choose>
										<c:when test="${numDate gt 1}">
										<td class="td-left-lev" onClick="changeDate(this);">
											<span>${d}</span>
										</td>
										</c:when>	
										<c:otherwise>
											<td class="td-left-on" onClick="changeDate(this);">
												<span>${d}</span>
											</td>
										</c:otherwise>
									</c:choose>
									</tr>
									<c:set var="numDate" value="${numDate+1}" />
									</c:forEach>
									<tr><td>&nbsp;</td></tr>
								</table>
							</div>
							<div id="leftSft"><!-- 餐次 -->
									<c:set var="numSft" value="0"/>	
									<c:forEach items="${listSft}" var="d">
									<c:choose>
									<c:when test="${numSft gt 0}">
										<div class="td-right-lev" onClick="changeSft('${listSft[numSft].code}','${listSft[numSft].name}')">
											<img src="<c:url value='/image/wechat/wanshi_d.png'/>" width='61' height='60'>
											<p>${listSft[numSft].name}</p>
										</div>
									</c:when>	
									<c:otherwise>
										<div class="td-right-on" onClick="changeSft('${listSft[numSft].code}','${listSft[numSft].name}')">
											<img src="<c:url value='/image/wechat/wushi_up.png'/>" width='61' height='60'>
											<p>${listSft[numSft].name}</p>
										</div>
									</c:otherwise>
									</c:choose>
									<c:set var="numSft" value="${numSft+1}"/>	
									</c:forEach>
							</div>
						</div>
					</div>
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
					<tr class="store_tr1" onclick= "JavaScript:gotoOrder('${t.firmid}');">
						<td class="store_td1">
							<c:choose>
								<c:when test="${not empty t.wbigpic}">
									<img src="<%=Commons.vpiceure %>${t.wbigpic}"/>
								</c:when>
								<c:otherwise>
									<img src="<c:url value='/image/wechat/1102.jpg'/>"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="store_td2">
							<div>
								<div class="firmdes">${t.firmdes}</div>
								<div class="addr">${t.addr}&nbsp;&nbsp;${distanceMap[t.firmid]}</div>
							</div>
						</td>
					</tr>
					<tr class="store_tr2">
						<td colspan='2'>
							<div class="desk">
								<div class="space-left">
									&nbsp;
								</div>
								<div class="deskMessage">
									<span>
									<c:forEach items="${t.listStoreTable}" var="v">
										<!--  
										<c:choose>
											<c:when test="${v.roomtyp eq '大厅'}">
												${v.pax}人桌&nbsp;${v.num}&nbsp;
											</c:when>
											<c:otherwise>
												${v.pax}人${v.roomtyp}&nbsp;${v.num}&nbsp;
											</c:otherwise>
										</c:choose>
										-->
										${v.roomtyp}&nbsp;${v.num}&nbsp;&nbsp;&nbsp;
									</c:forEach>
									&nbsp;
									</span>
								</div>
								<div style="float:left;width:15%;padding-top:10px;padding-bottom:10px;">
									<span style="background-color:#00db37;width:100%;color:#FFFFFF;border-radius:5px;padding:5px;font-size:90%;" onclick="JavaScript:gotoMap('${t.firmdes}','${t.addr}','${t.position}');">到这去</span>
								</div>
							</div>
							<div class="space">&nbsp;</div>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
　　</div><!-- content -->
</div><!-- page -->
</body>
</html>