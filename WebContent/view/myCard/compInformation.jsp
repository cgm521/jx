<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<meta name="format-detection" content="telphone=no"/>
<meta name="description" content="辰森世纪" />
<meta name="keywords" content="辰森世纪" />
<title>吉祥馄饨_个人资料</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min_my.css" />' rel="stylesheet" /> 
<link type="text/css" href='<c:url value="/css/wechat/dining/listFirm.css" />' rel="stylesheet" /> 
<link type="text/css" href='<c:url value="/css/wechat/compInformation.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/My97DatePicker/WdatePicker.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jweixin-1.0.0.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/css/validate.css'/>" />
<!--<link rel="stylesheet" href="<c:url value='/css/wechat/waitSeat.css'/>" />-->
<%@include file="/view/dining/jAlerts.jsp"%>
<style type="text/css">
.tdTitle{
	font-weight:;
	text-align:left;
	width:100%;
	height:40px;
	border:0;
	margin:0;
	margin-top:0px;
	padding-top:15px;
}
/*用户资料-start--------------------------------------------------------------------------*/
.jxht_body_user{
	background:url(../image/wechat/jxht_user.jpg)  no-repeat ;;
	background-size:100%  auto; 
}
.jxht_user_mid{
	position:absolute;
	top:13rem;
	bottom:2.8rem;
	left:0;
	right:0;
	overflow:auto;
}
.jxht_user_inputbox{
	margin:0 auto;
	font-size:0.9rem;
	color:#666;
}
.jxht_user_inputbox td{
	padding:0.3rem 0 0.1rem;
	vertical-align:middle;
}
.jxht_user_inputbox td input{
	padding:0.2rem 0.2rem 0.1rem;
	width:90%;
	font-size:1rem;
	color:#333;
	border: 1px solid rgba(0,0,0,.3);
	background: #fff;
	border-radius: 0.2rem;
}
.jxht_user_inputbox td input[type=radio]{
	width:auto;
}
	
.jxht_user_bqbox{
	margin:0.5rem auto 0;
	font-size:0.8rem;
	color:#666;
	background:#f3f3f3;
}
.jxht_user_bqbox td{
	padding:0.5rem;
}
.jxht_user_bqbox td span{
	display:inline-block;
	margin:0.1rem;
	padding:0.1rem 0.2rem;
	border: 1px solid rgba(0,0,0,0);
	border-radius: 0.1rem;
}
.jxht_user_bqbox td span.jxht_user_on{
	border: 1px solid rgba(202,52,36,0.7);
	background:#fff;
}	
	
.jxht_bottom{
	position:absolute;
	bottom:0;
	left:0;
	right:0;
	height:2.6rem;
	background:#231815;
}
.jxht_btn_sure{
	display:inline-block;
	float:right;
	padding:0rem 2rem;
	color:#ffffff;
	font-size:1.2rem;
	line-height:2.6rem;
	background:#CA3424;
}
.jxht_rightbtn{
	display:inline-block;
	float:left;
	height:2.6rem;
	width:3.2rem;
	background:url(../image/wechat/jxht_arr_right.png) center center no-repeat;
	background-size:auto 40%;
}
/*用户资料-end--------------------------------------------------------------------------*/
.ui-overlay-a, .ui-page-theme-a, .ui-page-theme-a .ui-panel-wrapper{
	background:url(../image/wechat/jxht_user.jpg) 50% 0 no-repeat;
	background-size:contain;
}


</style>
<script language="JavaScript" type="text/JavaScript">
	var myScrollCity;
	var myScrollStreet;
	function loaded() {
	    setTimeout(function(){
			myScrollCity = new iScroll("wrapperCity",{hScrollbar:false,vScrollbar:false});
			myScrollStreet = new iScroll("wrapperStreet",{hScrollbar:false,vScrollbar:false});
		},100);
			
		var height = $("#header").height();
		$(".mypopup").css({"top":height});
	}
	window.addEventListener("load", loaded, false);
	
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	$(function(){
		/* */
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
// 			$("body").text("请使用微信浏览器打开");
// 			return;
		}
	});
	
	function gotoDetail(){
		var validate = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'callName',
				validateType:['canNull'],
				param:['F'],
				error:['姓名不能为空']
			},{
				type:'text',
				validateObj:'bridate',
				validateType:['canNull'],
				param:['F'],
				error:['出生年月不能为空']
			},/*{
				type:'text',
				validateObj:'mail',
				validateType:['canNull'],
				param:['F'],
				error:['邮箱不能为空']
			},{
				type:'text',
				validateObj:'mail',
				validateType:['email','maxLength'],
				param:['F','60'],
				error:['邮箱格式不正确','长度不能超过60']
			},*/{
				type:'text',
				validateObj:'addr',
				validateType:['maxLength'],
				param:['30'],
				error:['长度不能超过30']
			}]
		});
		//校验
		if(validate._submitValidate()){
			var bridate = $("#bridate").val();
			var select_date = new Date(Date.parse(bridate));
			var max_date = new Date();
			max_date.setDate(max_date.getDate()-1);
			var min_date = new Date("1930/01/01");
			if(select_date<max_date && select_date>min_date){
				InitLayer();
				$("#order").submit();
			}else{
				alertMsg("出生日期不在可允许范围内");
				return;
			}
		}
	}
	function selectDate(){
		var wdateWidth = $("iframe").width(202);
		WdatePicker({skin:'twoer'});
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
			$("#sex").val('男');
		} else {
			$("#sexM").attr("class", "sexNotSelected");
			$("#sex").val('女');
		}
		
	}
	
	//修改标签或者兴趣爱好
	function changeLabelFavoriteSelected(obj,type){
		var sno=$(obj).attr("name");
		if($(obj).attr("class") == "jxht_user_on") {
			$(obj).attr("class", "");
			deleteSno(type,sno);		
		}else{
			$(obj).attr("class", "jxht_user_on");
			addSno(type,sno);
		}
	}
	function addSno(type,sno){
		if(type=="BQ"){
				if($("#label").val()==""){
					$("#label").val(sno);
				}else{
					var snoArray=$("#label").val().split(",");
					snoArray.push(sno);
					$("#label").val(snoArray.join());
				}
			}else{
				if($("#favorite").val()==""){
					$("#favorite").val(sno)
				}else{
					var snoArray=$("#favorite").val().split(",");
					snoArray.push(sno);
					$("#favorite").val(snoArray.join(","));
				}			
			}
	}
	function deleteSno(type,sno){
		if(type=="BQ"){
				var snoArray=$("#label").val().split(",");	
				arrayRemoveByValue(snoArray,sno);
				$("#label").val(snoArray.join(","));
			}else{
				var snoArray=$("#favorite").val().split(",");	
				arrayRemoveByValue(snoArray,sno);
				$("#favorite").val(snoArray.join(","));
			}
	}
	//从数组中删除指定值元素
	function arrayRemoveByValue(arr, val) {
		for(var i=0; i<arr.length; i++) {
			if(arr[i] == val) {
				arr.splice(i, 1);
				break;
			}
		}
	}
	// 弹出选择城市窗口
	function showSelectCity() {
		// 默认选择第一个
		if($("#scrollerCitys .popupSelect").length == 0) {
			$("#scrollerCitys").find("tr").eq(1).find("td").addClass("popupSelect");
		}
		$("#selectCityDiv").popup("open");
	}
	
	var tempCity;
	var tempCityName;
	// 选择城市
	function changeCity(city, pk_city, cityName) {
		var oldCity = $("#pk_city").val();
		if(oldCity == pk_city){
			return;
		}
		
		tempCity = pk_city;
		tempCityName = cityName;
		
		var selectC = $("#wrapperCity").find(".wrapperCityTD_DOWN");
		selectC.removeClass("wrapperCityTD_DOWN");
		selectC.addClass("wrapperCityTD_UP");
		
		$(city).find("td").removeClass("wrapperCityTD_UP");
		$(city).find("td").addClass("wrapperCityTD_DOWN");
		/************ ajax切换区域列表内容内容 **************/
		getFirm(pk_city);
	}
	
	// 确认选择，将选择的人数回填到人数展示框
	function popupComplate() {
		$("#city").val($("#scrollerCitys .popupSelect").find("span").text());
		$("#selectCityDiv").popup("close");
	}
	
	// 弹出选择门店窗口
	function showSelectFirm() {
		// 默认选择第一个
		if($("#scrollerFirms .popupSelect").length == 0) {
			$("#scrollerFirms").find("tr").eq(1).find("td").addClass("popupSelect");
		}
		$("#selectFirmDiv").popup("open");
	}
	
	// 选择城市
	function changeFirm(obj, firmCode, firmName) {
		var oldCode = $("#firmCode").val();
		if(oldCode == firmCode){
			return;
		}
		$("#firmCode").val(firmCode);
		$("#firmName").val(firmName);
		$("#pk_city").val(tempCity);
		$("#city").val(tempCityName);
		var selectA = $("#wrapperStreet").find(".brandsSelect");
		selectA.removeClass("brandsSelect");
		selectA.next(".brandsRight1").find("img").attr("src", "<c:url value='/image/wechat/noselect.png'/>");
		$(obj).find(".brandsLeft1").addClass("brandsSelect");
		$(obj).find("img").attr("src", "<c:url value='/image/wechat/select.png'/>");
	}
	
	// 确认选择，将选择的人数回填到人数展示框
	function popupFirmComplate() {
		$("#firmName").val($("#scrollerFirms .popupSelect").find("span").text());
		$("#firmCode").val($("#scrollerFirms .popupSelect").find("input").eq(0).val());
		$("#selectFirmDiv").popup("close");
	}
	
	function selectPopup(popupid){
		var popupstate = $("#popupstate").val();
		if(popupstate==1){
			cancelSelect();
			return;
		}
		$(document.getElementById(popupid)).popup("open");
		$(document.getElementById(popupid+"TD")).removeClass("headerTD_UP");
		$(document.getElementById(popupid+"TD")).addClass("headerTD_DOWN");
		
		$(".fenge_left1").toggle();
		$(".fenge_left2").toggle();
		
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
	
	function getFirm(pk_city){
		InitLayer();
		var htmlStr = "";
		
		$.ajax({
			url:"<c:url value='/waitSeat/getFirmByCity.do'/>",
			type:"POST",
			dataType:"json",
			data:{"pk_city":pk_city},
			success:function(json){
				var imgsrc = "<c:url value='/image/wechat/noselect.png'/>";
				var imgSelectSrc = "<c:url value='/image/wechat/select.png'/>";
				var cardFirmName = "${card.firmName}";
				$.each(json,function(i, item) {
						htmlStr = htmlStr
							+ "<tr onclick=\"changeFirm(this,'"+ item.firmCode + "','"+ item.firmdes + "');\">";
						
						if(cardFirmName == item.firmdes) {
							htmlStr = htmlStr + "<td class='brandsSelect brandsLeft1' style='width:80%'>" + item.firmdes + "</td>"
								+ "<td class='brandsRight1' style='width:20%'><img src=\""+ imgSelectSrc + "\"></td>";
						} else {
							htmlStr = htmlStr + "<td class='brandsLeft1' style='width:80%'>" + item.firmdes + "</td>"
							+ "<td class='brandsRight1' style='width:20%'><img src=\""+ imgsrc + "\"></td>";
						}
						
						htmlStr = htmlStr + "</tr>";
				});
				
				$("#wrapperStreet").find("table").html(htmlStr);//填充新内容
				closeLayer();
				/* if(json==null || jQuery.isEmptyObject(json)){
					cancelSelect();
				} */
				$("#streetCode").val('');
			},
			error : function(ajax) {
				closeLayer();
				alertMsg("获取门店信息失败");
			}
		});
	}
	//如果生日日期已经存在，如果点击就弹提示框  
	function verifyBirthdate(){
		alertMsg("出生日期只能设置一次")
	}
	
	function backHome() {
		var url ='../view/myCard/myQRCode.jsp?openid=${openid }&pk_group=${pk_group }&qrordr=${card.qrordr}&unionid=${unionid}&myselfShareCode=${card.myselfShareCode }'
		window.location.href = url;
	}
</script>
<style type="text/css">
	.ui-input-text{
		border-style:hidden;
	}
	.radio-inline-custom{position:relative;padding-left:15px; display:inline-block; margin:0.5rem auto;color:#333}
	.radio-inline-custom .ui-radio{position:absolute; top:3px; left:-3px; }
</style>
</head>
<body class="jxht_body_user" >
<!--中间内容-->
<div class="jxht_user_mid">
	<form id="order" method="POST" action="<c:url value='/myCard/saveInformation.do'/>" data-ajax="false">
		<input type="hidden" value="${openid}" name="openid"/>
		<input type="hidden" value="${unionid}" name="unionid"/>
		<input type="hidden" value="${card.cardNo}" name="cardNo"/>
		<input type="hidden" value="${pk_group}" name="pk_group"/>
		<input type="hidden" name="pk_city" id="pk_city" value="${pk_city}">	
				<table style="width:70%;" border="0" cellspacing="0" cellpadding="0" class="jxht_user_inputbox"">
					<tr style=" height:18px;">
						<td style="text-align:left;" width="38%" class="detail_1 line">
							<span style="font-size:14px" >  姓名</span>
						</td>
						<td class="detail_r line">
							<input type="text" name="name" id="callName" value="${card.name}" class="inputContent" data-role="none" style="border-color: #FFF;" placeholder="请输入真实姓名"/>
						</td>
					</tr>
					<tr  style=" height:18px;">
						<td style="text-align:left;"  width="38%" class="detail_1 line">
							<span style="font-size:14px" >  性别</span>
						</td>
						<td style="text-align:left;" class="detail_r line">
							
								<c:choose>
									<c:when test="${card.sex eq '男'}">
										<div class="radio-inline-custom"><input name="sex" type="radio" value="男" checked="checked"  >&nbsp;男士&nbsp;&nbsp;</div>
										<div class="radio-inline-custom"><input name="sex" type="radio" value="女" >&nbsp;女士	</div></td>
									</c:when>
									<c:when test="${card.sex eq '女'}">
										<div class="radio-inline-custom"><input name="sex" type="radio" value="男" >&nbsp;男士&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
										<div class="radio-inline-custom"><input name="sex" type="radio" value="女" checked="checked">&nbsp;女士</div></td>
									</c:when>
									<c:otherwise>
										<div class="radio-inline-custom"><input name="sex" type="radio" value="男">&nbsp;男士&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
										<div class="radio-inline-custom"><input name="sex" type="radio" value="女">&nbsp;女士</div></td>
									</c:otherwise>
								</c:choose>
						</td>
					</tr>
					<tr style=" height:18px;" >
						<td style="text-align:left;" class="detail_1 line">
							<span style="font-size:14px" >  出生年月</span>
						</td>
						<td class="detail_r line" style="font-family: Arial;" >
						 	<c:choose>
						 		<c:when test="${card.bridate == ''}">
						 		 	<input  type="date" name="bridate" id="bridate" value="${card.bridate}" class="inputContent"  style=" border-color: #FFF;border: 0;  background-color: #FFF;" placeholder="请选择日期" onclick="verifyBirthdate()"/>
						 		</c:when>
						 		<c:otherwise>
						 		 	<input type="date" readonly="readonly" name="bridate" id="bridate" value="${card.bridate}" class="inputContent"  style="border-color: #FFF;border: 0;  background-color: #FFF;" placeholder="请选择日期" onclick="verifyBirthdate()" />
								</c:otherwise>
						 	</c:choose>
							<!--  <input name="bridate" id="bridate" onClick="selectDate();" value="${card.bridate}" class="inputContent" data-role="none" readonly="readonly"/> -->
						</td>
					</tr>
					<tr style="display:none">
						<td class="detail_l line">
							电子邮箱
						</td>
						<td class="detail_r line" style="font-family: Arial;">
							<input type="text" name="mail" id="mail" value="${card.mail}" class="inputContent" data-role="none" style="border-color: #FFF;" placeholder="请输入电子邮箱"/>
						</td>
					</tr>
					<tr style=" height:18px;" >
						<td style="text-align:left;" class="detail_1 line">
							<span style="font-size:14px" >  常驻城市</span>
						</td>
						<td class="detail_r line" style="font-family: Arial;";>
							<input type="text" name="city" id="city" value="${card.city}" class="inputContent" data-role="none" style="border-color: #FFF;" placeholder="请输入常驻城市"/>
						</td>
					</tr>
					<tr style="display:none">
						<td class="detail_1 line">
							入会门店
						</td>
						<td class="detail_r line" style="font-family: Arial;" onclick="javascript:selectPopup('selectCity');">
							<input type="text" name="firmName" id="firmName" value="${card.firmName}" class="inputContent" data-role="none" style="border-color: #FFF;" placeholder="请选择入会门店"/>
							<input style="display:none" name="firmCode" id="firmCode" />
						</td>
					</tr>
					<tr style=" height:18px;">
						<td  style="text-align:left;" class="detail_1 line">
							<span style="font-size:14px" >  联系地址</span>
						</td>
						<td class="detail_r line" style="font-family: Arial;">
							<input type="text" name="addr" id="addr" value="${card.addr}" class="inputContent" data-role="none" style="border-color: #FFF;" placeholder="请输入联系地址"/>
						</td>
					</tr>
					<tr style=" height:18px;" >
						<td style="text-align:left;"class="detail_1 line">
						<span style="font-size:14px" >  我的推荐码</span>
						</td>
						<td class="detail_r line" style="font-family: Arial;">
							<input type="text" readonly="readonly" value="${card.myselfShareCode}" class="inputContent" data-role="none" style="border-color: #FFF;" placeholder/>
						</td>
					</tr>
				</table>
				 <!--标签选择区域-->
				<table width="90%" border="0" cellspacing="0" cellpadding="0" class="jxht_user_bqbox">
				  <tr style=" height:30px;">
					<td style="text-align:left;" bgcolor="#ddd"><b>标签：</b>
				  
				 
						<c:forEach items="${listLabelFavorite}" var="LabelFavorite" varStatus="status">	
						<c:choose>
							<c:when test="${fn:contains(card.label,LabelFavorite.sno)&&LabelFavorite.code=='BQ'}">
									
									<span onclick="javaScript:changeLabelFavoriteSelected(this, 'BQ')"  class="jxht_user_on" name="${LabelFavorite.sno}">	${LabelFavorite.des} </span>
					
							</c:when>							
							<c:when test="${LabelFavorite.code=='BQ'}">
									
									<span onclick="javaScript:changeLabelFavoriteSelected(this, 'BQ')" class="" name="${LabelFavorite.sno}">	${LabelFavorite.des} </span>
							</c:when>	
						 </c:choose>		
					</c:forEach>	</td>										
				  </tr>
				  <tr style=" height:30px;">
					<td style="text-align:left;" bgcolor="#ddd"><b>兴趣：</b>
				
				 
					
						<c:forEach items="${listLabelFavorite}" var="LabelFavorite" varStatus="status">	
						<c:choose>
							<c:when test="${fn:contains(card.favorite,LabelFavorite.sno)&&LabelFavorite.code=='XQ'}">
									
									<span onclick="javaScript:changeLabelFavoriteSelected(this, 'XQ')" class="jxht_user_on" name="${LabelFavorite.sno}">${LabelFavorite.des}</span>
							</c:when>
							<c:when test="${LabelFavorite.code=='XQ'}">
									
									<span onclick="javaScript:changeLabelFavoriteSelected(this, 'XQ')" class="" name="${LabelFavorite.sno}">${LabelFavorite.des}</span>
							</c:when>		
						 </c:choose>		
					</c:forEach> </td>	
				  </tr>
					<input type="hidden" id="label" name="label" value="${card.label}"/>
					<input type="hidden" id="favorite" name="favorite" value="${card.favorite}"/>	
				</table><!--标签选择区域-->
			
	</form>
</div><!--中间内容-->
<!--底部确定-->
<div class="jxht_bottom">
	<a  class="jxht_rightbtn" onclick="backHome();"></a>
	<a  class="jxht_btn_sure" style="color:#ffffff;" onclick="gotoDetail();">确定</a>
    
</div> 
<!--底部确定
<div class="bottDiv" data-role="footer" data-position="fixed" data-tap-toggle="false">
	<a id="ui-a" href="<c:url value='/myCard/cardInfo.do?openid=${openid}&pk_group=${pk_group}'/>" class="jxht_rightbtn"></a>
	
	<a id="ui-c" class="jxht_btn_sure" onclick="gotoDetail();">确定</a>
</div><!-- footer -->
</body>
</html>