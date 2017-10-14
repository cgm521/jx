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
<title>门店详情</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/dining/firmDetail.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.SuperSlide.2.1.1.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
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
	
	
	jQuery(".picScroll-left").slide({mainCell:".bd ul",autoPage:true,effect:"left",autoPlay:false,vis:4,trigger:"click"});
});
function storeup(firmid){
	var storeupvinit = parseInt($("#storeupvinit").val());
	var openid = $("#openid").val();
	var pk_group = $("#pk_group").val();
	
	$.ajax({
		url:"<c:url value='/dining/storeUp.do'/>",
		type:"POST",
		dataType:"html",
		data:{"openid":openid,"firmid":firmid,"pk_group":pk_group,"storeupvinit":storeupvinit},
		success:function(html){
			if(html=="N"){
				alertMsg("收藏失败");
				return false;
			}
			if(storeupvinit==-1){
				$(".firmdesRight").find("img").attr("src", "<c:url value='/image/wechat/shoucang.png'/>");
				$("#storeupvinit").val('0');
			}
			else if(storeupvinit==0){
				$(".firmdesRight").find("img").attr("src", "<c:url value='/image/wechat/notshoucang.png'/>");
				$("#storeupvinit").val('-1');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			//alertMsg(XMLHttpRequest);
			//alertMsg(XMLHttpRequest.readyState);
			//alertMsg(errorThrown);
		},
	});
}
function gotoMap(){
	var address = $("#address").val();//定位地址
	var LatLng = $("#LatLng").val();//定位地址
	
	if(LatLng==null || ""==LatLng){
		alertMsg("无法获取您当前的位置");
		return;
	}
	/*
	if(address==null || ""==address){
		alertMsg("无法获取您当前的位置");
		return;
	}
	*/
	InitLayer();
	window.setTimeout(function(){
		$('#map').submit();
	},100);
}
/*
function setImgSize(){
	var documentWidth = $(document).width();
	var imgWidth = documentWidth;
	var imgHeight = parseInt(documentWidth/3);
	$(".Actmimage").find("img").css({"width":imgWidth,"height":imgHeight});
}
*/
function changeBackImg(a,url){
	InitLayer();
	var img = $(a).find("img");
	var imgUrl = img[0].src;
	img.attr("src",imgUrl.replace("_up","_d"));
	
	window.setTimeout(function(){
		location.href = url;
	},100);
}
</script>
</head>
<body>
<div data-role="page" data-theme="d" id="pageone">
	<form id="map" method="POST" data-ajax="false" action="<c:url value='/dining/gotoMap.do' />">
		<input type="hidden" name="openid" id="openid" value="${openid}">
		<input type="hidden" name="pk_group" id="pk_group" value="${pk_group}">
		<input type="hidden" name="LatLng" id="LatLng" value="${LatLng}">
		<input type="hidden" name="address" id="address" value="${address}">
		<input type="hidden" name="firmName" id="firmName" value="${firm.firmdes}">
		<input type="hidden" name="addr" id="addr" value="${firm.addr}">
		<input type="hidden" name="position" id="position" value="${firm.position}">
		<input type="hidden" name="city" id="city" value="${city}">
	</form>
	<input type="hidden" name="storeupvinit" id="storeupvinit" value="${firm.storeupvinit}">
	<div data-role="content" style="width:100%;overflow:hidden;margin:0;list-style:none;padding:0;">
    	<div id="selectStore">
			<table>
				<tr class="store_tr1"">
					<td class="store_td1">
						<c:choose>
							<c:when test="${not empty firm.wbigpic}">
								<img src="<%=Commons.vpiceure %>${firm.wbigpic}"/>
							</c:when>
							<c:otherwise>
								<img src="<c:url value='/image/wechat/1102.jpg'/>"/>
							</c:otherwise>
						</c:choose>
					</td>
					<td class="store_td2">
                    	<div>
                            <div class="firmdesLeft">
  								<span>${firm.firmdes}</span>
  								<img src="<c:url value='/image/wechat/ding.png'/>"/>&nbsp;<!-- 团购 -->
                            	<c:forEach items="${firm.actmList}" var="actm">
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
                            <div class="firmdesRight">
                            	<c:choose>
                            		<c:when test="${firm.storeupvinit eq '0'}">
                            			<img src="<c:url value='/image/wechat/shoucang.png'/>" onclick="storeup('${firm.firmid}');"/>
                            		</c:when>
                            		<c:otherwise>
                            			<img src="<c:url value='/image/wechat/notshoucang.png'/>" onclick="storeup('${firm.firmid}');"/>
                            		</c:otherwise>
                            	</c:choose>
                            </div>
                            <div class="deskState">
                            	<span>台位状态:</span>
                            	<c:choose>
                            		<c:when test="${firm.deskState == '2'}">
                            			<img src="<c:url value='/image/wechat/jinzhang.png'/>"/>
                            		</c:when>
                            		<c:when test="${firm.deskState == '1'}">
                            			<img src="<c:url value='/image/wechat/shizhong.png'/>"/>
                            		</c:when>
                            		<c:when test="${firm.deskState == '0'}">
                            			<img src="<c:url value='/image/wechat/kongxian.png'/>"/>
                            		</c:when>
                            	</c:choose>
                            	<!-- &nbsp;&nbsp;预计等位:50分钟 -->
                            </div>
							<div class="addr">
                            	营业时间：${firm.topentim}&nbsp;~&nbsp;${firm.tclosetim}
                            </div>
                         </div>
					</td>
				</tr>
                <tr>
                	<td colspan="2" class="imglist">
                    	<table>
                        	<tr class="picScroll-left" >
                            	<td width="10%"><a href="javascript:;" class="prev buttonImgRL"><img src="<c:url value='/image/wechat/l.png'/>"/></br></a>&nbsp;</td>
                            	<td class="bd">
                            		<ul class="picList">
                            			<li class="buttonImg">
                                			<a target="_blank" onclick="changeBackImg(this,'<c:url value='/dining/bookDesk.do?openid=${openid}&pk_group=${pk_group}&firmid=${firm.firmid}'/>');" data-ajax="false">
                                				<img src="<c:url value='/image/wechat/yuding_up.png'/>"/></br>
                                	 		</a>
                                	 		预定
                                		</li>
                                		<li class="buttonImg">
                                			<a onclick="changeBackImg(this,'<c:url value='/bookMeal/gotoMenu.do?openid=${openid}&pk_group=${pk_group}&firmid=${firm.firmid}'/>');"  data-ajax="false">
                                				<img src="<c:url value='/image/wechat/diancan_up.png'/>"/></br>
                                	 		</a>
                                   	 		点餐
                                		</li>
                                		<li class="buttonImg">
                                			<a onclick="changeBackImg(this,'<c:url value='/waitSeat/firmWaitInfo.do?openId=${openid}&pk_group=${pk_group}&pk_store=${firm.firmid}'/>');" data-ajax="false">
                                				<img src="<c:url value='/image/wechat/dengwei_up.png'/>"/></br>
                                	 		</a>
                                   			 等位
                                		</li>
                                		<%-- <li class="buttonImg">
                                			<img src="<c:url value='/image/wechat/huiyuan_d.png'/>"/></br>
                                   	 		会员
                                		</li> --%>
                                		<li class="buttonImg">
                                			<a onclick="changeBackImg(this,'<c:url value='/wxPay/toBuyOrderForPay.do?openid=${openid}&pk_group=${pk_group}&firmid=${firm.firmid}'/>');" data-ajax="false">
                                				<img src="<c:url value='/image/wechat/maidan_up.png'/>"/></br>
                                	 		</a>
                                  			买单
                                		</li>
                                		<%-- --%>
                                		<li class="buttonImg">
                                			<a target="_blank" onclick="changeBackImg(this,'<c:url value='/dining/bookDesk.do?openid=${openid}&pk_group=${pk_group}&firmid=${firm.firmid}'/>');" data-ajax="false">
                                				<img src="<c:url value='/image/wechat/yuding_up.png'/>"/></br>
                                	 		</a>
                                	 		预定
                                		</li>
                                		<li class="buttonImg">
                                			<a onclick="changeBackImg(this,'<c:url value='/bookMeal/gotoMenu.do?openid=${openid}&pk_group=${pk_group}&firmid=${firm.firmid}'/>');"  data-ajax="false">
                                				<img src="<c:url value='/image/wechat/diancan_up.png'/>"/></br>
                                	 		</a>
                                   	 		点餐
                                		</li>
                                		<li class="buttonImg">
                                			<a onclick="changeBackImg(this,'<c:url value='/waitSeat/firmWaitInfo.do?openId=${openid}&pk_group=${pk_group}&pk_store=${firm.firmid}'/>');" data-ajax="false">
                                				<img src="<c:url value='/image/wechat/dengwei_up.png'/>"/></br>
                                	 		</a>
                                   			 等位
                                		</li>
                                		<%-- <li class="buttonImg">
                                			<img src="<c:url value='/image/wechat/huiyuan_d.png'/>"/></br>
                                   	 		会员
                                		</li> --%>
                                		<li class="buttonImg">
                                			<a onclick="changeBackImg(this,'<c:url value='/wxPay/toBuyOrderForPay.do?openid=${openid}&pk_group=${pk_group}&firmid=${firm.firmid}'/>');" data-ajax="false">
                                				<img src="<c:url value='/image/wechat/maidan_up.png'/>"/></br>
                                	 		</a>
                                  			买单
                                		</li>
                            		</ul>
                            	</td>
                            	<td width="10%"><a href="javascript:;" class="next buttonImgRL"><img src="<c:url value='/image/wechat/r.png'/>"/></br></a>&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
			</table>
		</div>
        <div class="firmMessage messageBorder">
        	<table>
            	<tr>
                	<td class="messageLeft messageBorder">
                    	${firm.addr}
                    </td>
                    <td class="messageRight messageBorder">
                    	<img src="<c:url value='/image/wechat/daohang.png'/>" onclick="JavaScript:gotoMap();"/>
                    </td>
                </tr>
                <tr>
                	<td class="messageLeft">
                    	商家电话：${firm.tele}
                    </td>
                    <td class="messageRight">
                    	<a href="tel:${firm.tele}"><img src="<c:url value='/image/wechat/dianhua.png'/>"/></a>
                    </td>
                </tr>
            </table>
        </div>
        <div class="firmMessage">
        <table>
        	<tr>
            	<td class="messageLeft">
                    	优惠活动
                </td>
                <td class="messageRight">
                   <img src="<c:url value='/image/wechat/arrow_right.png'/>"/>
                </td>
            </tr>
            <tr>
            	<td colspan="2" class="Actmimage">
                	<a href="<%=Commons.getConfig().getProperty("actmpicurl") %>" data-ajax="false"><img width="100%" src="<c:url value='/image/wechat/huodong.png'/>"/></a>
                </td>
            </tr>
             <tr>
            	<td colspan="2" class="Actmimage">
                	<a href="<%=Commons.getConfig().getProperty("actmpicurl") %>" data-ajax="false"><img width="100%" src="<c:url value='/image/wechat/huodong_2.png'/>"/></a>
                </td>
            </tr>
        </table>
        </div>
   </div>
</div><!-- page -->
</body>
</html>