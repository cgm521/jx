<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><fmt:message key="waitSeatTitle" /></title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/css/validate.css'/>" />
<link rel="stylesheet" href="<c:url value='/css/wechat/waitSeat.css'/>" />
<%@include file="/view/dining/jAlerts.jsp"%>
<style type="text/css">

</style>
<script language="JavaScript" type="text/JavaScript">
	var myScrollPerson; 
	var myScrollSeatType; 
	function loaded() { 
	setTimeout(function(){
		myScrollPerson = new iScroll("wrapperPersons",{hScrollbar:false,vScrollbar:false});
		myScrollSeatType = new iScroll("wrapperSeatTypes",{hScrollbar:false,vScrollbar:false});
		},100);
	} 
	window.addEventListener("load",loaded,false);
	
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	
	// 在当前门店已取号
	var isInQueue = "${isInQueue}";
	// 已取号(不限当前门店)
	var haveNumber = "${haveNumber}";
	var cnt = 0;
	var displayDiv = "wait"; // 显示等位信息
	var timeID;
	
	$(function(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
// 			$("body").text("请使用微信浏览器打开");
// 			return;
		}
		
		// 定时刷新，请求BOH获取当前排队信息
		timer();
		
		if(haveNumber == "Y") {
			// 已取号，不可重复取号
			$("#takeSpan").parent().css("background-color","#000000");
			$("#takeSpan").hide();
		}
	});
	
	function timer() {
		// 定时刷新，请求BOH获取当前排队信息
		timeID = window.setInterval(function(){
			if(isInQueue == "Y") {
				InitLayer();
				$.ajax({
					url:"<c:url value='/waitSeat/getQueueInfo.do'/>",
					type:"POST",
					dataType:"json",
					data:{"openId":"${openId}","firmCode":"${firm.firmCode}","pk_store":"${firm.firmid}","type":"firm"},
					success:function(data){
						var contentHtml = "";
						$.each(data,function(i,item){
							contentHtml = contentHtml + "<tr height='40px'><td width='28%'>" + data[i].des + "<span style='color:#999999; font-family:Arial;'>(" 
									+ data[i].minpax + "-" + data[i].maxpax + "<fmt:message key='personUnit' />)</span></td>";
							if(data[i].myNumber != null && data[i].myNumber != "" && parseInt(data[i].myNumber) >= 0) {
								contentHtml += "<td width='28%'><fmt:message key='labelMyNumber' />:<span style='color:red; font-family:Arial;'>" + data[i].myNumber + "</span></td>"
									+ "<td width='43%'><fmt:message key='calledNumber' />:<span style='color:red; font-family:Arial;'>" + data[i].calldeNumber + "</span></td></tr>";
							} else {
								/* contentHtml += "<td width='33%'><fmt:message key='takedNumber' />:<span style='color:#999999; font-family:Arial;'>" + data[i].takedNumber + "</span></td>"
									+ "<td width='33%'><fmt:message key='calledNumber' />:<span style='color:#999999; font-family:Arial;'>" + data[i].calldeNumber + "</span></td></tr>"; */
								contentHtml += "<td width='28%'><fmt:message key='labelWaitTables' />:<span style='color:#999999; font-family:Arial;'>" + data[i].waitTblNum + "</span></td>"
								+ "<td width='43%'><fmt:message key='labelExpectWaitTime' />:<span style='color:#999999; font-family:Arial;'>" + data[i].waitTime + "</span></td></tr>";
							}
						});
						$("#waitInfoTable").find("tr").remove();
						$("#waitInfoTable").append(contentHtml);
						closeLayer();
					},
					error:function(ajax){
						closeLayer();
					}
				});
				cnt = cnt + 1;
			}
		}, 20000);
	}
	
	// 取号
	function takeNumber(firmid) {
		// 显示用户信息
		if(displayDiv == "wait") {
			// 微信获取用户性别为2(女)时，改变性别div的样式
			if("${user.sex}" == "2") {
				$("#sexM").attr("class", "sexNotSelected");
				$("#sexF").attr("class", "sexSelected");
			}
			$("#waitInfo").hide();
			$("#userInfo").show();
			$("#takeSpan").text("<fmt:message key='btnOK' />");
			displayDiv = "user";
			clearTimeout(timeID);
			return;
		}
		
		var validate = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'phoneNo',
				validateType:['mobile'],
				error:['<fmt:message key="msgPhoneFormatError" />']
			},{
				type:'text',
				validateObj:'callName',
				validateType:['canNull'],
				param:['F'],
				error:['<fmt:message key="msgCannotNull" />']
			}]
		});
		
		if(!validate._submitValidate()){
			return;
		}
		
		// 预订人数
		var personNum = $("#totalPerson").text();
		var lineno = $("#lineno").val();
		if(parseInt(personNum)>=2){
			if(lineno==null || lineno==""){
				alertMsg("请选择台位类型");
				return;
			}
		}
		// 手机号码
		var tele = $("#phoneNo").val();
		// 预订人称呼
		var name = $("#").val();
		if($("#sexM").attr("class") == "sexSelected") {
			name = name + '<fmt:message key="sexGentleman" />';
		} else {
			name = name + '<fmt:message key="sexWomen" />';
		}
		
		InitLayer();
		$.ajax({
			url:"<c:url value='/waitSeat/takeNumber.do'/>",
			type:"POST",
			dataType:"json",
			data:{"firmId":firmid, "personNum":personNum, "name":name, "tele":tele, "openId":"${openId}","pk_firm":"${firm.firmid}","pk_group":"${pk_group}","lineno":lineno},
			success:function(data){
				if(data.myNum.indexOf("ERROR") > 0) {
					var errorCode = data.myNum.substring(data.myNum.indexOf("ERROR") + 6, data.myNum.length - 1);
					var errorMsg = '<fmt:message key="msgGetNumberFail" />';
					if(errorCode == "9005") {
						errorMsg += "!失败原因：人数输入错误，没有相符的桌位，请修改人数";
					}
					alertMsg(errorMsg);
					closeLayer();
				} else {
					isInQueue = "Y";
					$("#myWaitNumberSpan").text(data.myNum);
					$("#beforeMeSpan").text(data.beforeMe);
					$("#numberInfoDiv").popup("open");
					// 定时刷新，请求BOH获取当前排队信息
					//timer();
					closeLayer();
				}
			},
			error:function(ajax){
				alertMsg('<fmt:message key="msgGetNumberFail" />');
				closeLayer();
			}
		});
	}
	
	// 取消排队
	function cancelQueue(obj, firmid) {
		InitLayer();
		$.ajax({
			url:"<c:url value='/bookMeal/cancelQueue.do'/>",
			type:"POST",
			dataType:"json",
			data:{"firmId":firmid},
			success:function(data){
				var parentObj = $(obj).parent();
				$(obj).remove();
				parentObj.append('<input type="button" value="取号" data-role="none" data-ajax="false" onclick="javaScript:showPopup(\'' + firmid + '\'); event.cancelBubble=true;">');
				$("#tr_" + firmid).html('');
				$("#tr_" + firmid).hide();
				closeLayer();
			},
			error:function(ajax){
				closeLayer();
			}
		});
	}

	function showPopup(firmid){
		$("#numfirmid").val(firmid);
		$('#takeNumberLink').click();
	}
	
	function cancel(){
		$("#takeNumberDiv").popup("close");
	}
	
	// 修改性别选项
	function changeSelected(obj, type) {
		if($(obj).attr("class") == "sexSelected") {
			// 当前已经是已选中状态，返回
			return;
		}
		$(obj).attr("class", "sexSelected");
		// 另外一个修改为未选中
		if(type == "M") {
			$("#sexF").attr("class", "sexNotSelected");
		} else {
			$("#sexM").attr("class", "sexNotSelected");
		}
	}
	
	// 弹出选择人数窗口
	function showSelectPerson() {
		// 默认选择两人
		if($("#scrollerPersons .popupSelect").length == 0) {
			$("#scrollerPersons").find("tr").eq(2).find("td").addClass("popupSelect");
		}
		$("#selectPersonDiv").popup("open");
	}
	
	// 选择人数
	function changePersonNum(obj) {
		$("#scrollerPersons .popupSelect").removeClass("popupSelect");
		$(obj).addClass("popupSelect");
	}
	
	// 确认选择，将选择的人数回填到人数展示框
	function popupComplate() {
		$("#totalPerson").text($("#scrollerPersons .popupSelect").find("span").text());
		
		var personNum = parseInt($("#totalPerson").text());
		if(personNum<2){
			$("#scrollerSeatTypes .popupSelect").addClass("popupNoSelect");
			$("#scrollerSeatTypes .popupSelect").removeClass("popupSelect");
			$("#seatType").val('系统自动分配');
			$("#lineno").val('');
		}else{
			$("#scrollerSeatTypes .popupSelect").addClass("popupNoSelect");
			$("#scrollerSeatTypes .popupSelect").removeClass("popupSelect");
			$("#seatType").val('');
			$("#lineno").val('');
			$("#seatType").attr('placeholder','请选择台位类型');
		}
		$("#selectPersonDiv").popup("close");
	}
	
	// 弹出选择等位类型窗口
	function showSelectSeatType() {
		$("#selectSeatTypeDiv").popup("open");
	}
	
	// 选择类型
	function changeSeatType(obj,vcode,vname,minPax,maxPax) {
		var personNum = parseInt($("#totalPerson").text());
		var min = parseInt(minPax);
		var max = parseInt(maxPax);
		if(personNum>=min && personNum<=max){
			$("#scrollerSeatTypes .popupSelect").addClass("popupNoSelect");
			$("#scrollerSeatTypes .popupSelect").removeClass("popupSelect");
			$(obj).addClass("popupSelect");
			$("#seatType").val(vname);
			$("#lineno").val(vcode);
		}else{
			alertMsg("当前选择的人数，不在此台位范围内");
			return;
		}
	}
	
	// 确认选择，将选择的类型回填到展示框
	function popupComplateSeatType() {
		//$("#totalPerson").text($("#scrollerPersons .popupSelect").find("span").text());
		$("#selectSeatTypeDiv").popup("close");
	}
	
	// 确认我的等位号,取号功能隐藏
	function confirmNumber() {
		$("#numberInfoDiv").popup("close");
		window.location.href = "<%=path %>/waitSeat/myWaitInfo.do?openId=${openId}&pk_group=${pk_group}&code=${code}";
		
		/* $("#userInfo").hide();
		$("#waitInfo").show();
		$("#takeSpan").parent().css("background-color","#000000");
		$("#takeSpan").hide();
		$("#numberInfoDiv").popup("close");
		$(".bottDiv").show(); */
	}
	
	// 取消我的等位号
	function cancelNumber() {
		InitLayer();
		$.ajax({
			url:"<c:url value='/waitSeat/cancelQueue.do'/>",
			type:"POST",
			dataType:"json",
			data:{"firmId":firmid},
			success:function(data){
				alertMsg('<fmt:message key="msgCancelWaitSuccess" />');
				$("#userInfo").hide();
				$("#waitInfo").show();
				$("#takeSpan").text("<fmt:message key='getNumber' />");
				displayDiv = "wait";
				closeLayer();
			},
			error:function(ajax){
				alertMsg('<fmt:message key="msgCancelWaitFail" />');
				closeLayer();
			}
		});
		
		$("#numberInfoDiv").popup("close");
	}
</script>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<div role="main" class="ui-content" style="margin-top:0;list-style:none;padding:0;"><!--页面主体-->
		<div id="storeInfo" style="padding-top:20px; padding-bottom:10px; background-color:#FFFFFF">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td width="80%" align="left" style="vertical-align:middle; padding-left: 10px;">
						<span style="float:left; text-align:left; font-size:110%;font-weight: bolder;">${firm.firmdes }</span>
					</td>
					<td width="20%" rowspan="2">
						<a href="tel:${firm.tele }"><img style="width:35px;height:35px;" src="<c:url value='/image/wechat/tele.png'/>"/></a>
					</td>
				</tr>
				<tr>
					<td width="60%" align="left" style="vertical-align:middle; padding-left: 10px;">
						<span style="float:left; text-align:left; font-size:80%; color:#999999">${firm.addr }</span>
					</td>
				</tr>
			</table>
		</div>
		<div id="waitInfo" style="margin-top:10px; background-color:#F7F7F7;;">
			<div style="height:45px;background-color: #F4A21A;;">
				<div style="margin: 0px auto;width: 100px;">
					<span style="line-height:45px; font-size:110%;font-weight: bolder;">
						<c:choose>
							<c:when test="${sft eq '2' }">
								<fmt:message key="dinnerSession" />
							</c:when>
							<c:otherwise>
								<fmt:message key="lunchSession" />
							</c:otherwise>
						</c:choose>
						<fmt:message key="waitSeat" />
					</span>
				</div>
			</div>
			<div style="margin-top: 10px;background-color: #FFFFFF;">
				<table id="waitInfoTable">
					<c:forEach items="${tableList }" var="table">
						<tr height="40px">
							<td width="40%" style="text-align: left;padding-left: 10px;">${table.des }
								<span style='color:#999999; font-family:Arial;'>(${table.minpax }-${table.maxpax }<fmt:message key='personUnit' />)
								</span>
							</td>
							<c:choose>
								<c:when test="${table.myNumber ne null && table.myNumber ne '' }">
									<td width="30%"><fmt:message key='labelMyNumber' />:<span style='color:red; font-family:Arial;'>${table.myNumber }</span></td>
									<td width="30%"><fmt:message key='calledNumber' />:<span style='color:red; font-family:Arial;'>${table.calldeNumber }</span></td>
								</c:when>
								<c:otherwise>
									<%-- <td width="30%"><fmt:message key='takedNumber' />:<span style='color:#999999; font-family:Arial;'>${table.takedNumber }</span></td>
									<td width="25%"><fmt:message key='calledNumber' />:<span style='color:#999999; font-family:Arial;'>${table.calldeNumber }</span></td> --%>
									<td width="25%"><fmt:message key='labelWaitTables' />:<span style='color:#999999; font-family:Arial;'>${table.waitTblNum }</span></td>
									<td width="40%"><fmt:message key='labelExpectWaitTime' />:<span style='color:#999999; font-family:Arial;'>${table.waitTime }</span></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<c:if test="${isInQueue ne 'Y' && haveNumber eq 'Y' && userObj.PK_STORE ne firm.firmid }">
			<div class="infoDiv">您已经在${userObj.FIRMNAME }等位</div>
		</c:if>
		<div id="userInfo" style="margin-top:10px; background-color:#FFFFFF; display:none;">
			<table id="userInfoTable">
				<tr style="height:60px; border-bottom:1px solid #EEEEEE;" onclick="javascript:showSelectPerson();">
					<td width="15%" style="text-align:right;">人数:</td>
					<td width="85%" style="text-align:right;" colspan="2">
						<span id="totalPerson" style="font-family:Arial;color: red;font-size: 180%;">2</span>
						<div style="float:right; padding-right:15px;margin-top: 5px;">
							<img src="<c:url value='/image/wechat/r.png'/>" width="20px" height="20px" />
						</div>
					</td>
				</tr>
				<tr style="height:50px; border-bottom:1px solid #EEEEEE;">
					<td width="15%" style="text-align:right">姓名:</td>
					<td width="35%">
						<input type="text" data-role="none" class="inputContent" name="callName" id="callName" value="${user.nickname}" style="border-color: #FFF;color: grey;" placeholder="请输入姓名">
					</td>
					<td width="50%">
						<div id="sexF" class="sexNotSelected" onclick="javaScript:changeSelected(this, 'F')"><fmt:message key='sexWomen' /></div>
						<div id="sexM" class="sexSelected" onclick="javaScript:changeSelected(this, 'M')"><fmt:message key='sexGentleman' /></div>
					</td>
				</tr>
				<tr style="height:50px;">
					<td width="15%" style="text-align:right;">电话:</td>
					<td width="85%" colspan="2">
						<input type="tel" data-role="none" class="inputContent" name="phoneNo" id="phoneNo" value="" style="border-color: #FFF;color: grey;" width="200px" placeholder="请输入电话">
					</td>
				</tr>
				<tr style="height:50px;" >
					<td width="15%" style="text-align:right;">类型:</td>
					<td width="85%" colspan="2">
						<input type="text" readonly="readonly" onclick="javascript:showSelectSeatType();" data-role="none" class="inputContent" name="seatType" id="seatType" style="border-color: #FFF;color: grey;" width="200px" value="${defaultSeatType}" placeholder="">
						<input type="hidden" value="${defaultLineno}" id="lineno" name="lineno">
					</td>
				</tr>
			</table>
			<div id="selectPersonDiv" class="mypopup" data-role="popup" data-dismissible="false" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false"><!-- 选择人数 -->
            	<div class="popupTitle">
               		<fmt:message key='personNumber' />
                </div>
        		<div id="wrapperPersons" class="mypopupScroller">
					<table id="scrollerPersons">
                    	<tr><td>&nbsp;</td></tr>
						<c:forEach begin="1" end="${maxPerson }" step="1" varStatus="sta">
							<tr>
								<td class="popupNoSelect" onclick="changePersonNum(this);">
									<span style="font-family:Arial;">${sta.index}</span>
								</td>
							</tr>
						</c:forEach>
                        <tr><td>&nbsp;</td></tr>
					</table>
				</div><!-- wrapper -->
				<div class="popupComplate" onclick="popupComplate();" style="bottom:0px; text-shadow:none;">
                   	<fmt:message key='btnOK' />
                </div>
        	</div><!-- person -->
        	<div id="selectSeatTypeDiv" class="mypopup" data-role="popup" data-dismissible="false" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false"><!-- 选择人数 -->
            	<div class="popupTitle">
               		台位类型
                </div>
        		<div id="wrapperSeatTypes" class="mypopupScroller">
					<table id="scrollerSeatTypes">
                    	<tr><td>&nbsp;</td></tr>
                    	<c:if test="${not empty seatTypeList}">
                    		<c:forEach items="${seatTypeList}" var="seat">
							<tr>
								<c:choose>
									<c:when test="${seat['vcode'] eq defaultLineno}">
										<td class="popupSelect" onclick="changeSeatType(this,'${seat['vcode']}','${seat['vname']}','${seat['minpax']}','${seat['maxpax']}');">
									</c:when>
									<c:otherwise><td class="popupNoSelect" onclick="changeSeatType(this,'${seat['vcode']}','${seat['vname']}','${seat['minpax']}','${seat['maxpax']}');"></c:otherwise>
								</c:choose>
									<span style="font-family:Arial;">${seat['vname']}(${seat['minpax']}-${seat['maxpax']}<fmt:message key='personUnit' />)</span>
									<input type="hidden" id="${seat['vcode']}_min" value="${seat['minpax']}"/>
									<input type="hidden" id="${seat['vcode']}_max" value="${seat['maxpax']}"/>
								</td>
							</tr>
							</c:forEach>
                    	</c:if>
                        <tr><td>&nbsp;</td></tr>
					</table>
				</div><!-- wrapper -->
				<div class="popupComplate" onclick="popupComplateSeatType();" style="bottom:0px; text-shadow:none;">
                   	<fmt:message key='btnOK' />
                </div>
        	</div><!-- seatType -->
		</div>
	</div><!-- content -->
	<div class="bottDiv" data-role="footer" data-position="fixed" data-update-page-padding="true" data-tap-toggle="false"> <!-- data-tap-toggle="false" -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
			<!--  
				<td class="ui-a">
					<a href="<c:url value='/dining/gotoFirm.do?firmid=${firm.firmid }&openid=${openId }' />" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
			-->
				<td class="ui-a">
					<c:choose>
						<c:when test="${empty nextType}">
							<a href="<c:url value='/dining/gotoFirm.do?pk_group=${pk_group}&firmid=${firm.firmid}&openid=${openId}' />" data-ajax="false">
						</c:when>
						<c:otherwise>
							<a href="<c:url value='/dining/listFirmFromCity.do?pk_group=${pk_group}&openid=${openId}'/>" data-ajax="false">
						</c:otherwise>
					</c:choose>
						<img align="middle" src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
				<td class="ui-c"><span id="takeSpan" style="font-size:130%" onclick="JavaScript:takeNumber('${firm.firmCode }');"><fmt:message key="getNumber" /></span></div>
			</tr>
		</table>
		<div data-role="popup" id="numberInfoDiv" style="position:fixed; top:30%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr height="45px">
					<td align="center"><span style="font-size:120%"><fmt:message key='msgMyWaitNumber' /></span></td>
				</tr>
				<tr height="45px">
					<td align="center"><span id="myWaitNumberSpan" style="color:red; font-size:150%"></span></td>
				</tr>
				<tr height="35px" style="display:none;">
					<td align="center">
						<span style="color:#999999"><fmt:message key='msgBeforeMe' /><span id="beforeMeSpan"></span><fmt:message key='msgWaitCustomerNumber' /></span>
					</td>
				</tr>
				<tr height="50px">
					<td class="popBtnCol">
						<%-- <input type="button" value="<fmt:message key='btnCancel' />" data-role='none' data-ajax="false" onclick="javaScript:cancelNumber()" /> --%> 
						<input type="button" value="<fmt:message key='btnOK' />" data-role='none' data-ajax="false" onclick="javaScript:confirmNumber();" /> 
					</td>
				</tr>
			</table>
		</div>
  	</div>
</div><!-- page -->
</body>
</html>