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
<title>门店列表</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/dining/listFirm.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jweixin-1.0.0.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script language="JavaScript" type="text/JavaScript">
/*去除安卓返回及刷新按钮*/
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});

$(function(){
	/* 
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)!="micromessenger") {
		$("body").text("请使用微信浏览器打开");
		return;
	}
	*/
	$("#city-screen").bind("vmouseover",function(){
		cancelSelect();
	});
	$("#sort-screen").bind("vmouseover",function(){
		cancelSelect();
	});
	$("#brands-screen").bind("vmouseover",function(){
		cancelSelect();
	});
	$("#city-screen").bind("touchmove",function(){
		return false;//去除jquery自带遮罩滑动行为
	});	
	$("#sort-screen").bind("touchmove",function(){
		return false;//去除jquery自带遮罩滑动行为
	});	
	$("#brands-screen").bind("touchmove",function(){
		return false;//去除jquery自带遮罩滑动行为
	});
	
});
var myScrollBrands; 
var myScrollCity; 
var myScrollSort;
var myScrollStreet; 
function loaded() { 
    setTimeout(function(){
		myScrollBrands = new iScroll("wrapperBrands",{hScrollbar:false,vScrollbar:false});
		myScrollCity = new iScroll("wrapperCity",{hScrollbar:false,vScrollbar:false});
		myScrollSort = new iScroll("wrapperSort",{hScrollbar:false,vScrollbar:false});
		myScrollStreet = new iScroll("wrapperStreet",{hScrollbar:false,vScrollbar:false});
		},100);
		
	var height = $("#header").height();
	$(".mypopup").css({"top":height});
} 
window.addEventListener("load",loaded,false); 
function selectPopup(popupid){
	var popupstate = $("#popupstate").val();
	if(popupstate==1){
		cancelSelect();
		return;
	}
	$(document.getElementById(popupid)).popup("open");
	$(document.getElementById(popupid+"TD")).removeClass("headerTD_UP");
	$(document.getElementById(popupid+"TD")).addClass("headerTD_DOWN");
	if(popupid=="brands"){
		$(".fenge_left1").toggle();
	}else if(popupid=="sort"){
		$(".fenge_left2").toggle();
	}else{
		$(".fenge_left1").toggle();
		$(".fenge_left2").toggle();
	}
	$("#popupstate").val("1");
}
function cancelSelect(){
	var popupstate = $("#popupstate").val();
	if(popupstate==0){
		return;
	}
	$(".mypopup").popup("close");
	$(".headerTD").removeClass("headerTD_DOWN");
	$(".headerTD").addClass("headerTD_UP");
	$(".fenge_left1").show();
	$(".fenge_left2").show();
	$("#popupstate").val("0");
}
function changeBrands(brands,brandVcode,brandName){
	var oldbrandVcode = $("#brandVcode").val();
	
	if(oldbrandVcode == brandVcode){
		return;
	}
	$("#brandVcode").val(brandVcode);
	$("#brandsTD").find(".headerSelect").text(brandName);
	
	var selectB = $("#scrollerBrands").find(".brandsSelect");
	selectB.removeClass("brandsSelect");
	selectB.next(".brandsRight").find("img").attr("src", "<c:url value='/image/wechat/noselect.png'/>");
	$(brands).find(".brandsLeft").addClass("brandsSelect");
	$(brands).find("img").attr("src", "<c:url value='/image/wechat/select.png'/>");
	/************ ajax切换门店列表内容 **************/
	cancelSelect();
	reWriteStore();
}
function changeCity(city,pk_city,cityName){
	var oldCity = $("#pk_city").val();
	if(oldCity == pk_city){
		return;
	}
	
	$("#pk_city").val(pk_city);
	$("#cityTD").find(".headerSelect").text(cityName);
	
	var selectC = $("#wrapperCity").find(".wrapperCityTD_DOWN");
	selectC.removeClass("wrapperCityTD_DOWN");
	selectC.addClass("wrapperCityTD_UP");
	
	$(city).find("td").removeClass("wrapperCityTD_UP");
	$(city).find("td").addClass("wrapperCityTD_DOWN");
	/************ ajax切换区域列表内容内容 **************/
	getStreet(pk_city);
}
function changeStreet(street,streetCode){
	var oldCode = $("#streetCode").val();
	if(oldCode == streetCode){
		return;
	}
	$("#streetCode").val(streetCode);
	var selectA = $("#wrapperStreet").find(".brandsSelect");
	selectA.removeClass("brandsSelect");
	selectA.next(".brandsRight1").find("img").attr("src", "<c:url value='/image/wechat/noselect.png'/>");
	$(street).find(".brandsLeft1").addClass("brandsSelect");
	$(street).find("img").attr("src", "<c:url value='/image/wechat/select.png'/>");
	/************ ajax切换门店列表内容 **************/
	cancelSelect();
	reWriteStore();
}
function changeSort(sort,sortCode,sortName){
	var oldCode = $("#sortCode").val();
	
	if(oldCode == sortCode){
		return;
	}
	$("#sortCode").val(sortCode);
	$("#sortTD").find(".headerSelect").text(sortName);
	
	var selectB = $("#scrollerSort").find(".brandsSelect");
	selectB.removeClass("brandsSelect");
	selectB.next(".brandsRight").find("img").attr("src", "<c:url value='/image/wechat/noselect.png'/>");
	$(sort).find(".brandsLeft").addClass("brandsSelect");
	$(sort).find("img").attr("src", "<c:url value='/image/wechat/select.png'/>");
	/************ ajax切换门店列表内容 **************/
	cancelSelect();
	reWriteStore();
}
function openQueryPopup(){
	cancelSelect();
	promptMsg('店铺名称',function(r){
		$("#remarmemo11").val(r);
		commitRemarmemo();
	});
	//$("#storeNamePopup").popup("open");
	//$.alerts.dialogClass = "style_1"; // set custom style class
    //jPrompt("店铺名称", '','',function (r) {});
	
}
function commitRemarmemo(){
	//$("#storeNamePopup").popup("close");
	/************ ajax切换门店列表内容 **************/
	reWriteStore();
	$("#remarmemo11").val('');
}
function cancelRemarmemo(){
	$("#storeNamePopup").popup("close");
	$("#remarmemo11").val('');
}
function reWriteStore(){
	var pk_group = $("#pk_group").val();
	var pk_city =$("#pk_city").val(); 
	var brands = $("#brandVcode").val(); 
	var sort = $("#sortCode").val(); 
	var street = $("#streetCode").val();
	var storeName = $("#remarmemo11").val();
	var openid = $("#openid").val();
	var address = $("#address").val();
	
	InitLayer();
	$.ajax({
		url:"<c:url value='/dining/choiceFirm.do'/>",
		type:"POST",
		dataType:"json",
		data:{"pk_group":pk_group,"pk_city":pk_city,"brands":brands,"sort":sort,"street":street,"storeName":storeName,"openid":openid, "address":address},
		success:function(json){
			//alertMsgMsg(JSON.stringify(json));
			//$("#listFirm").html("");
			var addrValue="&nbsp;";
			var imgValue="";
			var htmlStr = "";
			$.each(json,function(i,item){
				addrValue="&nbsp;";
				if(item.addr!=null && item.addr!=""){
					addrValue = item.addr;
				}
				if(item.wbigpic==null || ""==item.wbigpic){
					imgValue = "<img src=\""+"<c:url value='/image/wechat/1102.jpg'/>"+"\"/>";
				}else{
					imgValue = "<img src=\"" + "<%=Commons.vpiceure %>" + item.wbigpic + "\"/>";
				}
				htmlStr = htmlStr
							+ "<tr onclick= \"JavaScript:gotoFirm(this,'"+ item.firmid +"','" + item.deskState + "','" + item.waitTime +"');\">"
							+ "<td class='store_bottom'>"
							+ "<table>"
							+ "<tr class='store_tr1'>"
							+ "<td class='store_td_l'>"
							+ imgValue
							+ "</td>"
							+ "<td class='store_td_r'>"
							+ "<table>"
							+ "<tr>"
							+ "<td>"
							+ "<div class='firmdes'><span>" + item.firmdes + "\&nbsp;</span>"
							+ "<img src=\""+ "<c:url value='/image/wechat/ding.png'/>" +"\"/>&nbsp;";
				var kk = 0;
				var quanName = "";
				$.each(item.actmList,function(j,test){
					for(var key in test){  
						if(key=='1'){
							htmlStr = htmlStr + "<img src=\""+ "<c:url value='/image/wechat/tuan.png'/>" +"\"/>&nbsp;";
						}else if(key=='2'){
							htmlStr = htmlStr + "<img src=\""+ "<c:url value='/image/wechat/fen.png'/>" +"\"/>&nbsp;";
						}else if(key=='3'){
							htmlStr = htmlStr + "<img src=\""+ "<c:url value='/image/wechat/quan.png'/>" +"\"/>&nbsp;";
							kk=1;
							quanName = test[key];
						}
					}  
				});
				htmlStr = htmlStr
							+ "</div>"
							+ "<div class='deskState'>"
							+ "<span>台位状态:</span>";
				var deskState = item.deskState;
				if(deskState=='2'){
					htmlStr = htmlStr + "<img src=\""+ "<c:url value='/image/wechat/jinzhang.png'/>" +"\"/>&nbsp;";
				}else if(deskState=='1'){
					htmlStr = htmlStr + "<img src=\""+ "<c:url value='/image/wechat/shizhong.png'/>" +"\"/>&nbsp;";
				}else if(deskState=='0'){
					htmlStr = htmlStr + "<img src=\""+ "<c:url value='/image/wechat/kongxian.png'/>" +"\"/>&nbsp;";
				}
				htmlStr = htmlStr
							//+ "&nbsp;&nbsp;预计等位："
							//+ "50分钟"
							+ "</div>"
							+ "</td>"
							+ "<td width='66px'>";
				if(!(item.addr==null||item.addr=='') || !(item.position==null||item.position=='')){
					var posi = "";
					if(item.position!=null && item.position!=''){
						posi = item.position;
					}
					htmlStr = htmlStr 
							+ "<div class='daohang' onclick=\"gotoMap(event,'" + item.firmdes + "','" + item.addr + "','" + posi + "');\">"
                 			+ "<img src=\"<c:url value='/image/wechat/genwozou.png'/>\" height='22px' width='61px'/>"
                 			+ "</div>";
				}			
				htmlStr = htmlStr       
                         	+ "</td>"
                         	+ "</tr>"
                         	+ "<tr>"
                         	+ "<td colspan='2'>"
							+ "<div class='addr'>" 
							+ "<div class='addrLeft'>"
							+ addrValue 
							+ "</div>"
							+ "<div class='addrRight'";
				if(!(item.addr==null||item.addr=='') || !(item.position==null||item.position=='')){
					var posi = "";
					if(item.position!=null && item.position!=''){
						posi = item.position;
					}
					htmlStr = htmlStr 
							+ "onclick=\"gotoMap(event,'" + item.firmdes + "','" + item.addr + "','" + posi + "');\"";
				}		
				htmlStr = htmlStr 
							+ ">";
				if(item.distanceText == null || item.distanceText==''){
					htmlStr = htmlStr + "&nbsp;"
				}else{
					htmlStr = htmlStr + item.distanceText;
				}
				htmlStr = htmlStr
							+ "</div>"
							+ "</div>"
							+ "</td>"
                            + "</tr>"
							+ "</table>"
							+ "</td>"
							+ "</tr>";
				if(kk==1){			
					htmlStr = htmlStr
							+ "<tr>"
							+ "<td class='store_td_l'>&nbsp;</td>"
							+ "<td class='store_td_r'>"
							+ "<div class='addr favou'>"
							+ "<img src=\""+ "<c:url value='/image/wechat/quan.png'/>" +"\"/>"
							+ "<span>&nbsp;" + quanName + "</span>"
							+ "</div>"
							+ "</td>"
							+ "</tr>";
				}
				
				htmlStr = htmlStr
						+ "<tr>"
						+ "<td colspan='2'>"
						+ "<span style='font-size:60%; color:red;'>";
				
				$.each(item.listActm, function(j,test){
					for(var key in test){
						if(key == 'vname') {
							htmlStr = htmlStr + test[key] + "&nbsp;&nbsp;";
							break;
						}
					}  
				});
				
				htmlStr = htmlStr 
						+ "</span>"
						+ "</td>"
						+ "</tr>";
				
				htmlStr = htmlStr
							+ "</table>"
							+ "</td>"
							+ "</tr>";
        	});
			htmlStr = htmlStr
						+ "<tr height='110px'>"
						+ "<td>"
						+ "&nbsp;"
						+ "</td>" 
						+ "</tr>";
			$("#listFirm").html(htmlStr);//填充新内容
			closeLayer();
			setPageOneHeight();
			myScrollStreet.refresh();
		},
		error:function(ajax){
			closeLayer();
			alertMsg("网络链接不稳定，请稍后重试");
		}
	});
}
function getStreet(pk_city){
	InitLayer();
	var htmlStr = "";
	
	if(pk_city==null || pk_city==""){
		$("#wrapperStreet").find("table").html(htmlStr);//填充新内容
		cancelSelect();
		$("#streetCode").val('');
		closeLayer();
		reWriteStore();
		return;
	}
	$.ajax({
		url:"<c:url value='/dining/getStreet.do'/>",
		type:"POST",
		dataType:"json",
		data:{"pk_city":pk_city},
		success:function(json){
			//alertMsgMsg(JSON.stringify(json));
			//$("#listFirm").html("");
			htmlStr = htmlStr
					+ "<tr onclick=\"changeStreet(this,'');\">"
					+ "<td class='brandsSelect brandsLeft1'>全部</td>"
					+ "<td class='brandsRight1'><img src=\""
					+ "<c:url value='/image/wechat/select.png'/>"
					+ "\"/></td>" 
					+ "</tr>";
			var imgsrc = "<c:url value='/image/wechat/noselect.png'/>";
			$.each(json,function(i, item) {
					htmlStr = htmlStr
						+ "<tr onclick=\"changeStreet(this,'"+ item.pk_street + "');\">"
						+ "<td class='brandsLeft1'>" + item.streetName + "</td>"
						+ "<td class='brandsRight1'><img src=\""+ imgsrc + "\"></td>"
						+ "</tr>";
			});
			$("#wrapperStreet").find("table").html(htmlStr);//填充新内容
			//setPageOneHeight();
			closeLayer();
			if(json==null || jQuery.isEmptyObject(json)){
				cancelSelect();
			}
			$("#streetCode").val('');
			reWriteStore();
			},
			error : function(ajax) {
					closeLayer();
					alertMsg("获取街道信息失败");
			}
		});
	}
	function setPageOneHeight() {
		/**
		 *多数据页面转到少数据页面时，pageone后有一块空白
		 *初始页面是少数据页面时没有此种情况
		 */
		$('#pageone').css("height", "");
		var ph = parseInt($("#pageone").height());
		var wh = parseInt($(window).height());
		if (ph < wh) {
			$('#pageone').css("height", "100%");
		}
	}
	//转向订单页面
	function gotoFirm(tr,firmid,deskState,waitTime) {
		if (firmid == null || firmid == "") {
			return;
		}
		
		$(tr).css("background-color","#eeeeee");
		
		var nextType = $("#nextType").val();//点击门店后跳转类型
		var openid = $("#openid").val();
		var pk_group = $("#pk_group").val();
		
		window.setTimeout(function(){
			InitLayer();
			window.setTimeout(function(){
				clearSelect();
				if(nextType=='1'){//订位
					location.href = "<c:url value='/dining/bookDesk.do?openid='/>"+ openid + "&pk_group=" + pk_group +"&firmid=" + firmid;
				}else if(nextType=='2'){//点餐
					location.href = "<c:url value='/bookMeal/gotoMenu.do?openid='/>"+ openid + "&pk_group=" + pk_group +"&firmid=" + firmid;
				}else if(nextType=='3'){//等位
					location.href = "<c:url value='/waitSeat/firmWaitInfo.do?openId='/>"+ openid + "&pk_group=" + pk_group +"&pk_store=" + firmid;
				}else if(nextType=='4'){//外卖
					location.href = "<c:url value='/bookMeal/gotoMenu.do?openid='/>"+ openid + "&pk_group=" + pk_group +"&firmid=" + firmid+"&type=takeout";
				}else{//门店详情
					$("#firmid").val(firmid);
					$("#deskState").val(deskState);
					$("#waitTime").val(waitTime);
					$("#cityName").val($(".wrapperCityTD_DOWN").text());
					$('#order').submit();
				}
			},100);
		},500);
	}
	
	function clearSelect(){//解决物理返回时值是jquery设置的值
		$("#pk_city").val($("#init_city").val());
		$("#brandVcode").val("${brands }");
		$("#streetCode").val("${street }");
		$("#sortCode").val("${sort }");
	}
	
	function gotoMap(e,firmdes,addr,position){
		//导航图片点击
		var e=e||window.event; 
		e.stopPropagation();//阻止冒泡
		
		var address = $("#address").val();//定位地址
		var LatLng = $("#LatLngMap").val();//定位地址
		
		if(LatLng==null || ""==LatLng){
			alertMsg("无法获取您当前的位置");
			return;
		}
		
		if(address==null || ""==address){
			alertMsg("无法获取您当前的位置");
			return;
		}
		
		clearSelect();
		
		$("#cityMap").val($(".wrapperCityTD_DOWN").text());
		$("#firmNameMap").val(firmdes);//门店名称
		$("#addr").val(addr);//门店地址
		$("#position").val(position);//门店地址
		InitLayer();
		window.setTimeout(function(){
			$("#map").submit();
		},100);
	}
	
	// 根据输入地址，查询附近门店
	function searchAddress() {
		var address = $("#inputAddress").val();
		if(null == address || '' == address) {
			alertMsg("请输入地址信息！");
		} else {
			$("#address").val(address);
			reWriteStore();
		}
	}
</script>
</head>
<body>
<div data-role="page" data-theme="d" id="pageone" style="margin:0;list-style:none;padding:0;">
	<form id="order" method="POST" data-ajax="false" action="<c:url value='/dining/gotoFirm.do' />">
		<input type="hidden" name="openid" id="openid" value="${openid}">
		<input type="hidden" name="pk_group" id="pk_group" value="${pk_group}">
		<input type="hidden" name="LatLng" id="LatLng" value="${LatLng}">
		<input type="hidden" name="firmid" id="firmid" value="">
		<input type="hidden" name="deskState" id="deskState" value="">
		<input type="hidden" name="waitTime" id="waitTime" value="">
		<input type="hidden" name="cityName" id="cityName" value="">
	</form>
	<form id="map"  method="POST" data-ajax="false" action="<c:url value='/bookDesk/gotoMap.do' />">
		<input type="hidden" name="openid" id="openidMap" value="${openid}">
		<input type="hidden" name="pk_group" id="pk_groupMap" value="${pk_group}">
		<input type="hidden" name="address" id="address" value="${address}"><!-- 用户订位地址  -->
		<input type="hidden" name="city" id="cityMap" value="">
		<input type="hidden" name="firmName" id="firmNameMap" value="">
		<input type="hidden" name="addr" id="addr" value=""><!-- 门店地址 -->
		<input type="hidden" name="LatLng" id="LatLngMap" value="${LatLng}"><!-- 用户订位坐标  -->
		<input type="hidden" name="position" id="position" value=""><!-- 门店坐标 -->
	</form>
	<input type="hidden" name="pk_city" id="pk_city" value="${userCity.pk_city}">
	<input type="hidden" name="init_city" id="init_city" value="${userCity.pk_city}">
	<input type="hidden" name="brands" id="brandVcode" value="${brands }">
	<input type="hidden" name="street" id="streetCode" value="${street }">
	<input type="hidden" name="sort" id="sortCode" value="${sort }">
	<input type="hidden" id="remarmemo11"  value=""><!-- 查找店铺名称  -->
	<input type="hidden" id="now_city" value="" /><!-- jssdk定位成功后返回设置 -->
	<input type="hidden" id="now_cityName" value="" /><!-- jssdk定位成功后返回设置 -->
	<input type="hidden" id="nextType" value="${nextType}"/><!-- 点击门店后，跳转类型 -->
	<div style="width:100%;">
		<table>
			<tr>
				<td width="10%"></td>
				<td width="80%">
					<div style="width:100%; margin-left:auto; margin-right:auto;">
						<span><input placeholder="请输入地址..." type="text" id="inputAddress" value="${address }" style="border:1px solid #C6996E; border-radius:4px; background-color:#FFF2D4; color:#555555" /></span>
					</div>
				</td>
				<td width="10%">
					<span id="gotoSearch" onclick="javascript:searchAddress();">
						<img src="<c:url value='/image/huiyuanka_21.png'/>" width="33px" height="30px"/>
					</span>
				</td>
			</tr>
		</table>
	</div>
	<div data-role="header" id="header" style="border:0;z-index:1100;">
    	<table>
        	<tr>
            	<td id="brandsTD" class="headerTD headerTD_UP headerTD_width1" onclick="javascript:selectPopup('brands');">
                	<div class="headerSelect">全部品牌</div>
                    <image class="fenge fenge_left1" src="<c:url value='/image/wechat/fenge.png'/>">
                </td>
                <td id="cityTD" class="headerTD headerTD_UP headerTD_width1" onclick="javascript:selectPopup('city');">
                	<div class="headerSelect">
                		<c:choose>
                        	<c:when test="${empty userCity || empty userCity.pk_city}">
	                        	全部
                        	</c:when>
                        	<c:otherwise>
                        		${userCity.vname}
                        	</c:otherwise>
                        </c:choose>
                	</div>
                    <image class="fenge fenge_left2" src="<c:url value='/image/wechat/fenge.png'/>">
                </td>
                <td id="sortTD" class="headerTD headerTD_UP headerTD_width2" onclick="javascript:selectPopup('sort');">
                	<div class="headerSelect">
                		<c:choose>
							<c:when test="${sort eq '2'}">
								距离优先
							</c:when>
							<c:otherwise>
								综合排序
							</c:otherwise>
						</c:choose>
                	</div>
                </td>
            </tr>
        </table>
        <input type="hidden" id="popupstate" value="0">
        <div id="brands" class="mypopup" data-role="popup" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false"><!-- 品牌 -->
        	<div id="wrapperBrands" class="mypopupScroller">
				<table id="scrollerBrands">
					<tr onclick="javascript:changeBrands(this,'','品牌');">
						<c:choose>
							<c:when test="${empty brands}">
								<td class="brandsSelect brandsLeft">全部品牌</td>
								<td class="brandsRight"><img src="<c:url value='/image/wechat/select.png'/>"/></td>
							</c:when>
							<c:otherwise>
								<td class="brandsLeft">全部品牌</td>
								<td class="brandsRight"><img src="<c:url value='/image/wechat/noselect.png'/>"/></td>
							</c:otherwise>
						</c:choose>
					</tr>
					<c:forEach items="${brandsList}" var="b">
					<tr onclick="changeBrands(this,'${b.pk_brand}','${b.vname}');">
						<c:choose>
							<c:when test="${brands eq b.pk_brand}">
								<td class="brandsSelect brandsLeft">${b.vname}</td>
								<td class="brandsRight"><img src="<c:url value='/image/wechat/select.png'/>"/></td>
							</c:when>
							<c:otherwise>
								<td class="brandsLeft">${b.vname}</td>
								<td class="brandsRight"><img src="<c:url value='/image/wechat/noselect.png'/>"/></td>
							</c:otherwise>
						</c:choose>
					</tr>
					</c:forEach>
				</table>
			</div><!-- wrapper -->
            <div class="cancelSelect" onclick="cancelSelect();">
                <img src="<c:url value='/image/wechat/sanjiao_up.png'/>"/>
            </div>
        </div><!-- brands -->
        <div id="city" class="mypopup" data-role="popup" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false"><!-- 城市 -->
        	<div class="mypopupScroller">
                    	<div id="wrapperCity"><!-- 城市 -->
                        	<table>
                        		<c:choose>
                        			<c:when test="${empty userCity || empty userCity.pk_city}">
                        			<tr id="TRALL" onclick="changeCity(this,'','全部');">
                        				<td class="wrapperCityTD_DOWN">全部</td>
                        			</tr>
                        			</c:when>
                        			<c:otherwise>
                        			<tr id="TRALL" onclick="changeCity(this,'','全部');">
                        				<td class="wrapperCityTD_UP">全部</td>
                        			</tr>
                        			</c:otherwise>
                        		</c:choose>
                        		
                        		<c:forEach items="${listCity}" var="c">
                            	<tr id="TR${c.pk_city}" onclick="changeCity(this,'${c.pk_city}','${c.vname}');">
                            		<c:choose>
                            			<c:when test="${c.pk_city == userCity.pk_city}">
                            				<td class="wrapperCityTD_DOWN">${c.vname}</td>
                            			</c:when>
                            			<c:otherwise>
                            				<td class="wrapperCityTD_UP">${c.vname}</td>
                            			</c:otherwise>
                            		</c:choose>
                                </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <div id="wrapperStreet" class="mypopupScroller"><!-- 区域 -->
                        	<table>
                        		<tr onclick="changeStreet(this,'');">
									<c:choose>
										<c:when test="${empty street}">
										<td class="brandsSelect brandsLeft1">全部</td>
										<td class="brandsRight1"><img src="<c:url value='/image/wechat/select.png'/>"/></td>
										</c:when>
										<c:otherwise>
										<td class="brandsLeft1">全部</td>
										<td class="brandsRight1"><img src="<c:url value='/image/wechat/noselect.png'/>"/></td>
										</c:otherwise>
									</c:choose>
								</tr>
								<c:forEach items="${streetsList}" var="Astreet">
								<tr onclick="changeStreet(this,'${Astreet.pk_street}');">
									<c:choose>
										<c:when test="${street eq Astreet.pk_street}">
										<td class="brandsSelect brandsLeft1">${Astreet.streetName}</td>
										<td class="brandsRight1"><img src="<c:url value='/image/wechat/select.png'/>"/></td>
										</c:when>
										<c:otherwise>
										<td class="brandsLeft1">${Astreet.streetName}</td>
										<td class="brandsRight1"><img src="<c:url value='/image/wechat/noselect.png'/>"/></td>
										</c:otherwise>
									</c:choose>
								</tr>
								</c:forEach>
                            </table>
                        </div>
			</div><!-- wrapper -->
            <div class="cancelSelect" onclick="cancelSelect();">
                <img src="<c:url value='/image/wechat/sanjiao_up.png'/>"/>
            </div>
        </div><!-- city -->
        <div id="sort" data-role="popup" class="mypopup" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false"><!-- 排序 -->
        	<div id="wrapperSort" class="mypopupScroller">
				<table id="scrollerSort">
					<tr onclick="changeSort(this,'','综合排名')">
						<c:choose>
							<c:when test="${empty sort}">
								<td class="brandsSelect brandsLeft">综合排序</td>
								<td class="brandsRight"><img src="<c:url value='/image/wechat/select.png'/>"/></td>
							</c:when>
							<c:otherwise>
								<td class="brandsLeft">综合排序</td>
								<td class="brandsRight"><img src="<c:url value='/image/wechat/noselect.png'/>"/></td>
							</c:otherwise>
						</c:choose>
					</tr>
					<!--  
                    <tr onclick="changeSort(this,'1')">
						<td class="brandsLeft">预订优先</td>
                        <td class="brandsRight"><img src="<c:url value='/image/wechat/noselect.png'/>"/></td>
					</tr>
					-->
                    <tr onclick="changeSort(this,'2','距离优先')">
                    	<c:choose>
							<c:when test="${sort eq '2'}">
								<td class="brandsSelect brandsLeft">距离优先</td>
								<td class="brandsRight"><img src="<c:url value='/image/wechat/select.png'/>"/></td>
							</c:when>
							<c:otherwise>
								<td class="brandsLeft">距离优先</td>
								<td class="brandsRight"><img src="<c:url value='/image/wechat/noselect.png'/>"/></td>
							</c:otherwise>
						</c:choose>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>
				</table>
			</div><!-- wrapper -->
            <div class="cancelSelect" onclick="cancelSelect();">
                <img src="<c:url value='/image/wechat/sanjiao_up.png'/>"/>
            </div>
        </div><!-- sort -->
	</div><!-- header -->
    <div data-role="main" style="background-color:#FFFFFF;overflow:hidden;">
    	<div id="selectStore">
			<table id="listFirm">
				<c:forEach items="${listFirm}" var="t">
				<tr onclick= "JavaScript:gotoFirm(this,'${t.firmid}','${t.deskState}','${t.waitTime}');">
					<td class="store_bottom">
					<table>
						<tr class="store_tr1">
							<td class="store_td_l">
							<c:choose>
							<c:when test="${not empty t.wbigpic}">
								<img src="<%=Commons.vpiceure %>${t.wbigpic}"/>
							</c:when>
							<c:otherwise>
								<img src="<c:url value='/image/wechat/1102.jpg'/>"/>
							</c:otherwise>
							</c:choose>
							</td>
							<td class="store_td_r">
							<table>
							<tr>
								<td>
								<div class="firmdes">
                            		<span>${t.firmdes}&nbsp;</span>
                            		<img src="<c:url value='/image/wechat/ding.png'/>"/>&nbsp;
                            		<c:forEach items="${t.actmList}" var="actm">
                            			<c:forEach items="${actm}" var="entry">
                            			<c:choose>
                            				<c:when test="${entry.key eq '1'}">
                            					<img src="<c:url value='/image/wechat/tuan.png'/>"/>&nbsp;<!-- 团购 -->
                            				</c:when>
                            				<c:when test="${entry.key eq '2'}">
                            					<img src="<c:url value='/image/wechat/fen.png'/>"/>&nbsp;<!-- 积分 -->
                            				</c:when>
                            				<c:when test="${entry.key eq '3'}">
                            					<img src="<c:url value='/image/wechat/quan.png'/>"/>&nbsp;<!-- 券 -->
                            				</c:when>
                            			</c:choose>
                            			</c:forEach>
                            		</c:forEach>
                             	</div>
                             	<div class="deskState">
                            		<span>台位状态:</span>
                            		<c:choose>
                            		<c:when test="${t.deskState == '2'}">
                            			<img src="<c:url value='/image/wechat/jinzhang.png'/>"/>
                            		</c:when>
                            		<c:when test="${t.deskState == '1'}">
                            			<img src="<c:url value='/image/wechat/shizhong.png'/>"/>
                            		</c:when>
                            		<c:when test="${t.deskState == '0'}">
                            			<img src="<c:url value='/image/wechat/kongxian.png'/>"/>
                            		</c:when>
                            		</c:choose>
                            		<!-- &nbsp;&nbsp;预计等位:50分钟 -->
                            	</div>
                            	</td>
                            	<td width="66px">
                            	<c:if test="${not empty t.addr || not empty t.position}">
                            	<div class="daohang" onclick="gotoMap(event,'${t.firmdes}','${t.addr}','${t.position}');">
                             		<img src="<c:url value='/image/wechat/genwozou.png'/>" height="22px" width="61px"/>
                             	</div>
                             	</c:if>
                             	</td>
                             	</tr>
                             <tr>
                             	<td colspan="2">
								<div class="addr">
                            		<div class="addrLeft">${t.addr}</div>
                               		<div class="addrRight" 
	                               		<c:if test="${not empty t.addr || not empty t.position}">
	                               			onclick="gotoMap(event,'${t.firmdes}','${t.addr}','${t.position}');"
	                               		</c:if>
                               		>
                               			${t.distanceText}
                               		</div>
                            	</div>
                            	</td>
                            </tr>
                            <tr>
                            	<td colspan="2">
                            		<span style="font-size:60%; color:red;">
	                            		<c:forEach items="${t.listActm}" var="item">
	                            			${item.vname }&nbsp;&nbsp;
	                            		</c:forEach>
                            		</span>
                            	</td>
                            </tr>
							</table>
						</td>
					</tr>
					<c:forEach items="${t.actmList}" var="actmm">
                     	<c:forEach items="${actmm}" var="entry">
                        	<c:if test="${entry.key eq '3'}">
                         	<tr>
                         	<td class="store_td_l">
                         		&nbsp;
                         	</td>
                         	<td class="store_td_r">
                            <div class="addr favou">
                            	<img src="<c:url value='/image/wechat/quan.png'/>"/><span>&nbsp;${entry.value}</span>
                            </div>
                            </td>
                          </tr>
                      		</c:if>
                      	</c:forEach>
                	</c:forEach>
					</table>
					</td>
				</tr>
				</c:forEach>
				<tr height="110px">
					<td>
					&nbsp;
					</td>
				</tr>
			</table>
		</div>
    </div><!-- main -->
 	<div class="bottDiv" data-role="footer" data-position="fixed" data-fullscreen="true" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a">
					<!--  
					<c:choose>
						<c:when test="${empty nextType} ">
							<a href="<c:url value='/dining/homePage.do'/>" data-ajax="false">
						</c:when>
						<c:otherwise>
							<a href="<c:url value='/dining/listFirmFromCity.do'/>" data-ajax="false">
						</c:otherwise>
					</c:choose>
						<img align="middle" src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
					-->
					&nbsp;
				</td><!-- <img src="<c:url value='/image/wechat/back.png'/>"/> -->
				<td id="ui-b">&nbsp;</td>
				<td id="ui-c" onclick="JavaScript:openQueryPopup();"><span>查找店铺</span></td>
			</tr>
		</table>
  	</div><!-- footer -->
</div><!-- page -->
</body>
</html>