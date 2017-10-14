<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link type="text/css" href="<c:url value='/css/wechat/jquery.mobile-1.4.5.min.css'/>" rel="stylesheet" />
<link type="text/css" href="<c:url value='/css/wechat/dining/bookDesk.css'/>" rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/My97DatePicker/WdatePicker.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/css/validate.css'/>" />
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
});

	$(document).on("pagehide","#pagetwo",function(){
		var button = $("#popup_ok");
		if(button !=null){
			button.click();
		}
	});
	
	var myScrollPerson; 
	var myScrollSft; 
	function loaded() { 
    setTimeout(function(){
		myScrollPerson = new iScroll("wrapperPersons",{hScrollbar:false,vScrollbar:false});
		myScrollSft = new iScroll("wrapperSfts",{hScrollbar:false,vScrollbar:false});
		
		$(".timeTopRightText").text(setDateText("${nowIndex}"));
		
		},100);
	} 
	window.addEventListener("load",loaded,false); 
	function selectPopup(type){
		if(type=='1'){
			$("#persons").popup("open");
		}else if(type=='0'){
			$("#sfts").popup("open");
		}
	}
	function popupComplate(){
		$(".mypopup").popup('close');
	}
	function changeNormalDate(type,dat){
		var NormalDate = $("#NormalDate").val();
		if(NormalDate == type){
			return;
		}
		
		$("#NormalDate").val(type);
		$("#dat").val(dat);
		$("#datmins").val('');
		
		$(".headerDateSelect").addClass("headerDateNoSelect");
		$(".headerDateSelect").removeClass("headerDateSelect");
		
		$(document.getElementById("TD"+type)).removeClass("headerDateNoSelect");
		$(document.getElementById("TD"+type)).addClass("headerDateSelect");
		
		$(".headerDate").removeClass("fenge");
		if(type=='0'){
			$(document.getElementById("TD1")).addClass("fenge");
			$(document.getElementById("TD2")).removeClass("fenge");
			
			var nowIndex = parseInt($("#nowIndex").val());
			$(".timeSpan").removeClass("canotSelect");
			$(".timeSpan").removeClass("selected");
			$(".timeSpan").removeClass("canSelect");
			var length = $(".timeSpan").length;
			for(var i=1;i<=length;i++){
				if(i<nowIndex){
					$(document.getElementById("T"+i)).find(".timeSpan").addClass("canotSelect");
				}else if(i==nowIndex){
					$(document.getElementById("T"+i)).find(".timeSpan").addClass("selected");
					$("#datmins").val(iGetInnerText("T"+i));
					var sft = setSft(i);
					if(sft=='0'){
						$("#dateAndpersonL").find("span").text("请选择餐次");
					}else{
						$("#dateAndpersonL").find("span").text(sftNameMapJs[sft]);
					}
					$(".timeTopRightText").text(setDateText(i));
				}else{
					$(document.getElementById("T"+i)).find(".timeSpan").addClass("canSelect");
				}
			}
			initPopup();
		}else if(type=='1'){
			/*$(document.getElementById("TD2")).addClass("fenge");*/
			$(".timeSpan").removeClass("canotSelect");
			$(".timeSpan").removeClass("selected");
			$(".timeSpan").addClass("canSelect");
			$("#T1").find(".timeSpan").removeClass("canSelect");
			$("#T1").find(".timeSpan").addClass("selected");
			$("#datmins").val(iGetInnerText("T1"));
			var sft = setSft(1);
			$("#dateAndpersonL").find("span").text(sftNameMapJs[sft]);
			$(".timeTopRightText").text("预计到店时间：明天 "+$("#datmins").val());
			initPopup();
		}else if(type=='2'){
			$(document.getElementById("TD0")).addClass("fenge");
			$(document.getElementById("TD2")).addClass("fenge");
			$(".timeSpan").removeClass("canotSelect");
			$(".timeSpan").removeClass("selected");
			$(".timeSpan").addClass("canSelect");
			$("#T1").find(".timeSpan").removeClass("canSelect");
			$("#T1").find(".timeSpan").addClass("selected");
			$("#datmins").val(iGetInnerText("T1"));
			var sft = setSft(1);
			$("#dateAndpersonL").find("span").text(sftNameMapJs[sft]);
			$(".timeTopRightText").text("预计到店时间：后天 "+$("#datmins").val());
			initPopup();
		}
		
		setTable();
	}
	function selectDate(td){
		var wdateWidth = $("iframe").width(202);
		var wdateHeight = $("iframe").height(217);
		//alertMsg(documentWidth);
		//var pos = documentWidth -  wdateWidth - 20;
		//alertMsg(pos);
		WdatePicker({skin:'twoer',el:'calendar',minDate:'%y-%M-\#{%d+3}',maxDate:'%y-%M-\#{%d+30}',onpicked:setDate})
	}
	function setDate(){
		$("#NormalDate").val('-1');
		$(".headerDateSelect").toggleClass("headerDateNoSelect");
		$(".headerDateSelect").toggleClass("headerDateSelect");
		
		$("#dat").val($("#calendar").val());
		
		$(document.getElementById("TD0")).addClass("fenge");
		$(document.getElementById("TD1")).addClass("fenge");
		$(document.getElementById("TD2")).removeClass("fenge");
		
		$(".timeSpan").removeClass("canotSelect");
		$(".timeSpan").removeClass("selected");
		$(".timeSpan").addClass("canSelect");
		$("#T1").find(".timeSpan").removeClass("canSelect");
		$("#T1").find(".timeSpan").addClass("selected");
		$("#datmins").val(iGetInnerText("T1"));
		var sft = setSft(1);
		$("#dateAndpersonL").find("span").text(sftNameMapJs[sft]);
		$(".timeTopRightText").text("预计到店时间："+$('#dat').val()+" "+$("#datmins").val());
		initPopup();
		
		setTable();
	}
	function changeTime(span,index){
		var o = $(span).find(".timeSpan");
		if(o.hasClass("selected") || o.hasClass("canotSelect")){
			return;
		}
		$(".selected").addClass("canSelect");
		$(".selected").removeClass("selected");
		$(span).find(".timeSpan").addClass("selected");
		$(span).find(".timeSpan").removeClass("canSelect");
		$("#datmins").val(iGetInnerText("T"+index));
		
		var oldSft = $("#sft").val();
		var sft = setSft(index);
		if(sft=='0'){
			$("#dateAndpersonL").find("span").text("请选择餐次");
		}else{
			$("#dateAndpersonL").find("span").text(sftNameMapJs[sft]);
		}
		
		var td = $("#TD_SFT_"+sft);
		if(!td.hasClass("popupSelect")){
			$("#scrollerSfts").find(".popupSelect").addClass("popupNoSelect");
			$("#scrollerSfts").find(".popupSelect").removeClass("popupSelect");
			td.removeClass("popupNoSelect");
			td.addClass("popupSelect");
			
			//$("#dateAndpersonL").find("span").text("选择餐次");
		}
		
		$(".timeTopRightText").text(setDateText(index));
		
		if(oldSft != sft){
			setTable();
		}
	}
	function setDateText(index){
		var text = "预计到店时间：";
		var NormalDate = $("#NormalDate").val();
		
		if("${empty nextDayIndex}" == "true"){
			if(NormalDate=='0'){
				text += "今天 ";
			}else if(NormalDate=='1'){
				text += "明天 ";
			}else if(NormalDate=='2'){
				text += "后天 ";
			}else if(NormalDate=='-1'){
				text += $("#dat").val() + " ";
			}
		}else{//如果有跨日的情况
			var nextDayIndex = parseInt("${nextDayIndex}");
			var selectIndex = parseInt(index);
			if(selectIndex >= nextDayIndex){//选择的时间是第二天的时间
				var dat = $("#dat").val();
				if(dat!=null && dat!=""){
					text += getNextDate(dat) + " ";
				}
			}else{
				if(NormalDate=='0'){
					text += "今天 ";
				}else if(NormalDate=='1'){
					text += "明天 ";
				}else if(NormalDate=='2'){
					text += "后天 ";
				}else if(NormalDate=='-1'){
					text += $("#dat").val() + " ";
				}
			}
		}
		return text += $("#datmins").val();
	}
	function getNextDate(dat){
		var data = new Date(dat);
		var nextDay = new Date(data.getTime() + 1 * 24 * 60 * 60 * 1000);
		var month = (nextDay.getMonth() + 1);
		var day = nextDay.getDate();
		if(month<=9){
			month = "0" + month;
		}
		if(day<=9){
			day = "0" + day;
		}
		return nextDay.getFullYear() + "-" + month + "-" + day;
	}
	function changeSftPopup(start,end,sft,td){
		if($(td).hasClass("popupSelect")){
			return;
		}
		var NormalDate = $("#NormalDate").val();
		var nowIndex = parseInt($("#nowIndex").val());
		var length = $(".timeSpan").length;
		start =  parseInt(start);
		end =  parseInt(end);
		if(start==0){
			start = 1;
		}
		if(end==0){
			end = length;
		}
		
		if(sft=='0'){
			$("#dateAndpersonL").find("span").text("请选择餐次");
		}else{
			$("#dateAndpersonL").find("span").text(sftNameMapJs[sft]);
		}
		
		$("#scrollerSfts").find(".popupSelect").addClass("popupNoSelect");
		$("#scrollerSfts").find(".popupSelect").removeClass("popupSelect");
		$(td).removeClass("popupNoSelect");
		$(td).addClass("popupSelect");
		
		$(".timeSpan").removeClass("canotSelect");
		$(".timeSpan").removeClass("selected");
		$(".timeSpan").removeClass("canSelect");
		if(NormalDate == "0" && start<nowIndex){
			start = nowIndex;
		}
		$("#datmins").val('');
		
		for(var i=1;i<=length;i++){
			if(i>=start && i<=end){
				if(i==start){
					$(document.getElementById("T"+i)).find(".timeSpan").addClass("selected");
					$("#datmins").val(iGetInnerText("T"+i));
					setSft(i);
					$(".timeTopRightText").text(setDateText(i));
				}else{
					$(document.getElementById("T"+i)).find(".timeSpan").addClass("canSelect");
				}
			}else{
				$(document.getElementById("T"+i)).find(".timeSpan").addClass("canotSelect");
			}
		}
		
		setTable();
	}
	function changePaxPopup(num,pax,min,max,td,roomtyp,roomtypid){
		/*
		if($(td).hasClass("popupSelect")){
			return;
		}
		*/
		var text = "预定类型：";
		if(num=='0'){
			alertMsg("对应桌位已经被抢光啦。。。");
			return;
		}else if(num=='-1'){
			$("#dateAndpersonR").find("span").text("选择人数");
			text += roomtyp + '${defaultMax_Min}' + "人";
		}else{
			$("#dateAndpersonR").find("span").text(min+"人");
			text += roomtyp + min + "-" + max + "人";
		}
		
		$("#realPax").val(min);
		$("#pax").val(min);
		$("#roomtyp").val(roomtypid);
		$("#scrollerPersons").find(".popupSelect").addClass("popupNoSelect");
		$("#scrollerPersons").find(".popupSelect").removeClass("popupSelect");
		if(!$(td).hasClass("popupSelect")){
			$(td).removeClass("popupNoSelect");
			$(td).addClass("popupSelect");
		}
		$("#bookRoomTyp").text(text);
		popupComplate();
		
		promptMsg_num("请输入人数（范围:"+min+"-"+max+"人）",function(r){
			if(!isNaN(r)){
				var new_pax = parseInt(r);
				if(new_pax>=parseInt(min) && new_pax<=parseInt(max)){
					$("#realPax").val(r);
					$("#pax").val(r);
					$("#dateAndpersonR").find("span").text(r+"人");
				}else{
					alertMsg("人数输入有误");
				}
			}else{
				alertMsg("人数输入有误");
			}
		});
	}
	
	var seMapJs = {};
	<c:if test="${not empty seMap}">
		<c:forEach items="${seMap}" var="se">
			seMapJs["${se.key}"] = parseInt("${se.value}");
		</c:forEach>
	</c:if>
	
	var sftNameMapJs = {};
	<c:if test="${not empty sftNameMap}">
		<c:forEach items="${sftNameMap}" var="se">
		sftNameMapJs["${se.key}"] ="${se.value}";
		</c:forEach>
	</c:if>
	
	function setSft(index){
		var temp = parseInt(index);
		var sft = '0';
		for(var key in sftNameMapJs){
			if(temp>=seMapJs["Start_"+key] && temp<=seMapJs["End_"+key]){
				$("#sft").val(key);
				sft = key;
			}
		}
		return sft;
	}
	function initPopup(){
		var sft = $("#sft").val();
		var pax = $("#pax").val();
		
		var td = $("#TD_SFT_"+sft);
		if(!td.hasClass("popupSelect")){
			$("#scrollerSfts").find(".popupSelect").addClass("popupNoSelect");
			$("#scrollerSfts").find(".popupSelect").removeClass("popupSelect");
			td.removeClass("popupNoSelect");
			td.addClass("popupSelect");
			
			//$("#dateAndpersonL").find("span").text("选择餐次");
		}
		
		var roomtyp = $("#roomtyp").val();
		td = $("#TD_PAX_"+roomtyp+"_0");
		if(!td.hasClass("popupSelect")){
			$("#scrollerPersons").find(".popupSelect").addClass("popupNoSelect");
			$("#scrollerPersons").find(".popupSelect").removeClass("popupSelect");
			td.removeClass("popupNoSelect");
			td.addClass("popupSelect");
			
			$("#dateAndpersonR").find("span").text(pax+"人");
		}
	}
	function iGetInnerText(id) {
		var testStr = $(document.getElementById(id)).text();
        var resultStr = testStr.replace(/(^\s*)|(\s*$)/g, "");//去掉空格
        return resultStr;
    }
	//更改桌位
	function setTable(){
		var pk_group = $("#pk_group").val();
		var firmid = $("#firmid").val();
		var dat = $("#dat").val();
		var sft = $("#sft").val();
		
		var key = true;
		var htmlStr = "";
		InitLayer();
		$.ajax({
			url:"<c:url value='/dining/getDeskFormFirm.do'/>",
			type:"POST",
			dataType:"json",
			data:{"pk_group":pk_group,"firmid":firmid,"dat":dat,"sft":sft},
			success:function(json){
				//alertMsg(JSON.stringify(json));
				//$("#listFirm").html("");
				htmlStr = htmlStr + "<tr><td>&nbsp;</td></tr>";
				$.each(json,function(i,item){
					var tdClass="popupNoSelect"
					if(key){
						if(item.num>0){
							key = false;
							var text = "预定类型："+item.roomtyp+item.minpax+"-"+item.maxpax+"人";
							$("#bookRoomTyp").html(text);
							$("#realPax").val(item.minpax);
							$("#pax").val(item.minpax);
							$("#roomtyp").val(item.id);
							$("#dateAndpersonR").find("span").text(item.minpax+"人");
							
							tdClass = "popupSelect";
						}
					}
					htmlStr = htmlStr
								+ "<tr>"
								+ "<td id='TD_PAX_"+item.id+"_"+ item.pax +"' class='" + tdClass + "' onclick= \"JavaScript:changePaxPopup('"+item.num+"','"+item.pax+"','"+item.minpax+"','"+item.maxpax+"',this,'"+item.roomtyp+"','"+item.id+"');\">"
								+ item.roomtyp ;
					if(item.pax>0){
						htmlStr = htmlStr + "(" + item.pax + "人)";
						
					}
					htmlStr = htmlStr
								+ "</td>"
								+ "</tr>";
	        	});
				if(key){
					htmlStr = htmlStr + "<tr><td>台位已经被预订光了，请选择其他餐次或日期</td></tr>";
					$("#dateAndpersonR").find("span").text("选择人数");
					$("#bookRoomTyp").html("");
					$("#realPax").val("");
					$("#pax").val("");
					$("#roomtyp").val("${defaultRoomTypID}");
				}
				$("#scrollerPersons").html(htmlStr);//填充新内容
				closeLayer();
			},
			error:function(ajax){
				closeLayer();
				alertMsg("error");
			}
		});
	}
	function gotoDetail(){
		var order={};
		order["openid"] = $("#openid").val();
		order["dat"] = $("#dat").val();
		order["sft"] = $("#sft").val();
		InitLayer();
		$.post("<c:url value='/dining/queryLimit.do'/>",order,function(data){
			closeLayer();
			if(data=="N0"){
				alertMsg("在预订日期您还有未完成的订单，请先完成或取消订单后再进行预订");
				return false;
			}else if(data=="N1"){
				alertMsg("在预订日期餐次您还有未完成的订单，请先完成或取消订单后再进行预订");
				return false;
			}else if(data=="Y"){
				var datmins = $("#datmins").val();
				if(datmins==null || datmins==''){
					alertMsg("请选择到店时间");
					return;
				}
				
				var pax = $("#pax").val();
				if(pax==null || pax==''){
					alertMsg("请选择就餐人数");
					return;
				}
				
				var sft = $("#sft").val();
				if(sft==null || sft==''){
					alertMsg("请选择餐次");
					return;
				}
				
				$("#pagetwoRoomtyp").text($("#bookRoomTyp").text());
				
				/*
				var dat = $("#dat").val();
				var NormalDate = $("#NormalDate").val();
				var text = dat + " " + datmins + " ";
				if(NormalDate=='0'){
					text += "今天";
				}else if(NormalDate=='1'){
					text += "明天";
				}else if(NormalDate=='2'){
					text += "后天";
				}
				*/
				var text = $(".timeTopRightText").text().replace(/(^\s*)|(\s*$)/g, "").replace("预计到店时间：","");
				$("#pagetwo").find(".timeTopRightDetail").text(text);
				if(text.indexOf("天")>=0){
					$("#arrtime").val($("#dat").val());
				}else{
					$("#arrtime").val(text.substr(0,10));
				}
				
				var startTime = "";
				var endTime = "";
				text = sftNameMapJs[sft];
				startTime = iGetInnerText("T"+seMapJs["Start_"+sft]);
				endTime = iGetInnerText("T"+seMapJs["End_"+sft]);
				$("#sftName").text(text);
				$("#timeStart").text(startTime);
				$("#timeEnd").text(endTime);
				
				$("#gotoPagetwo").click();
			}
		});
	}
	function changSelectMeal(type,tr){
		var td = $(tr).find("td:eq(0)");
		if(td.hasClass("mealLeftSelect")){
			return;
		}
		$("#bookMeal").val(type);
		var selectB = $(".time").find(".mealLeftSelect");
		selectB.removeClass("mealLeftSelect");
		selectB.addClass("mealLeft");
		selectB.next(".mealRight").find("img").attr("src", "<c:url value='/image/wechat/noselect.png'/>");
		td.addClass("mealLeftSelect");
		td.removeClass("mealLeft");
		td.next(".mealRight").find("img").attr("src", "<c:url value='/image/wechat/select.png'/>");
	}
	//注册下一步点击事件
	function confirmOrder(){
		var realPax = $("#realPax").val();
		var roomtyp = $("#roomtyp").val();
		var validate = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'phoneNo',
				validateType:['canNull'],
				param:['F'],
				error:['手机号码不能为空']
			},{
				type:'text',
				validateObj:'phoneNo',
				validateType:['mobile'],
				error:['手机号码格式不正确']
			},{
				type:'text',
				validateObj:'callName',
				validateType:['canNull'],
				param:['F'],
				error:['不为空']
			}
			/*
			,{
				type:'text',
				validateObj:'pax',
				validateType:['canNull','num','minValue','maxValue'],
				param:['F','F',minValue,maxValue],
				error:['就餐人数必填','必须为数字','就餐人数不能少于'+minValue,'就餐人数不能大于'+maxValue]
			},{
				type:'text',
				validateObj:'valicode',
				validateType:['canNull'],
				param:['F'],
				error:['不为空']
			}
			*/
			]
		});
		//校验
		if(validate._submitValidate()){
			InitLayer();
			var rannum=$("#rannum").val();
			//var valicode=$("#valicode").val();
			//if(rannum == valicode){
				var remark= $("#remark").val();
				if(remark=='请输入备注(可不填)' || remark=='' || remark==null){
					$("#remark").val('');
				}
				$(".bottDiv").hide();
				$("#order").submit();
			//}else{
			//	closeLayer();
			//	alertMsg("验证码错误");
			//}
			
		}else{
			var phoneNo = $("#phoneNo");
			var phoneNoValue = phoneNo.val();
			var isMobile=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;
			if(phoneNoValue==null || phoneNoValue=="" || !isMobile.test(phoneNoValue)){ //如果用户输入的值不同时满足手机号和座机号的正则
				var t = phoneNo.offset().top;
			    $(window).scrollTop(t);
			}
		}
	}
	
	function changeSex(td,type){
		$(".sexSpan").removeClass("selected");
		$(".sexSpan").removeClass("canSelect");
		$(td).find(".sexSpan").addClass("selected");
		if(type=="1"){
			$("#sexTD2").find(".sexSpan").addClass("canSelect");
			$("#food").val("先生");
		}else if(type=="2"){
			$("#sexTD1").find(".sexSpan").addClass("canSelect");
			$("#food").val("女士");
		}
	}
</script>
<style type="text/css">
	
.dateAndperson span{
	display:block; 
	text-align:center;
	padding-top:8px;
	padding-bottom:8px;
	color:#ffffff;
	background-color:#ffb400;
	border:1px solid #ffb400;
	border-radius:4px;
	font-weight:bold;
	font-size: 120%;
}
</style>
<title>
<c:choose>
	<c:when test="${not empty firmdes}">${firmdes}</c:when>
	<c:otherwise>门店预定</c:otherwise>
</c:choose>
</title>
</head>
<body style="width:100%;overflow:hidden;margin:0;list-style:none;padding:0;">
<form id="order" method="POST" data-ajax="false" action="<c:url value='/dining/confirmOrder.do' />">
<div data-role="page" data-theme="d" id="pageone">
		<input type="hidden" id="openid" name="openid" value="${openid}">
		<input type="hidden" id="pk_group" name="pk_group" value="${pk_group}">
		<input type="hidden" id="firmid" name="firmid" value="${firmid}">
		<input type="hidden" id="firmdes" name="firmdes" value="${firmdes}">
		<input type="hidden" id="dat" name="dat" value="${dat}">
		<input type="hidden" id="arrtime" name="arrtime" value="${dat}">
		<input type="hidden" id="sft" name="sft" value="${sft}">
		<input type="hidden" id="datmins" name="datmins" value="${datmins}">
		<input type="hidden" name="roomtyp" id="roomtyp" value="${defaultRoomTypID}">
		<input type="hidden" name="realPax" id="realPax" value="${defaultPax}">
		<input type="hidden" name="pax" id="pax" value="${defaultPax}">
		<input type="hidden" name="clientID" id="clientID" value="${clientID}">
		<input type="hidden" name="bookMeal" id="bookMeal" value="0">
		
	<input type="hidden" id="NormalDate" value="0">
	<input type="hidden" id="nowIndex" value="${nowIndex}">
    <div data-role="header" style="border:0;">
    	<div>
			<table>
            	<tr>
                	<td id="TD0" class="headerDate headerDateSelect" onclick="changeNormalDate('0','${datMap.today}');"><!-- 默认今天  -->
                    	<span class="dateSpan">今天</span><br/>
                        <span style="font-size:80%;font-family: Arial;">${weekMap.today}</span>
                    </td>
                    <td id="TD1" class="headerDate headerDateNoSelect fenge" onclick="changeNormalDate('1','${datMap.tomorrow}');">
                    	<span  class="dateSpan">明天</span><br/>
                         <span style="font-size:80%;font-family: Arial;">${weekMap.tomorrow}</span>
                    </td>
                    <td id="TD2" class="headerDate headerDateNoSelect" onclick="changeNormalDate('2','${datMap.houtian}');">
                    	<span  class="dateSpan">后天</span><br/>
                        <span style="font-size:80%;font-family: Arial;">${weekMap.houtian}</span>
                    </td>
                    <td id="TD3" class="headerDateFrame" onclick="selectDate();">
                    	<img src="<c:url value='/image/wechat/riqi.png'/>"/>
                    	<input type='hidden' id="calendar" value=""/>
                    </td>
                </tr>
            </table>
		</div>
        <div style="margin-top: 10px;">
        	<table>
            	<tr>
                	<td id="dateAndpersonL" class="dateAndperson" onclick="selectPopup('0');">
                    	<span>
                    		<c:choose>
                    			<c:when test="${not empty sftNameMap[sft]}">${sftNameMap[sft]}</c:when>
                    			<c:otherwise>选择餐次</c:otherwise>
                    		</c:choose>
                    	</span>
                    </td>
                    <td id="dateAndpersonR" class="dateAndperson" onclick="selectPopup('1');">
                    	<span>
                    	<c:choose>
                    		<c:when test="${not empty defaultPax}">${defaultPax}人</c:when>
                    		<c:otherwise>选择人数</c:otherwise>
                    	</c:choose>
                    	</span>
                    </td>
                </tr>
            </table>
            <div id="persons" class="mypopup" data-role="popup" data-dismissible="false" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false"><!-- 选择人数 -->
            	<div class="popupTitle">
            		<table>
            			<tr>
            				<td>选择预定人数</td>
            			</tr>
            		</table>
                </div>
        		<div id="wrapperPersons" class="mypopupScroller">
					<table id="scrollerPersons">
                    	<tr><td>&nbsp;</td></tr>
                    	<c:set var="index" value="0"/>
						<c:forEach items="${listStoreTable}" var="s">
							<tr>
								<c:choose>
									<c:when test="${defaultPax eq s.minpax && defaultRoomTypID eq s.id}">
										<td id="TD_PAX_${s.id}_${s.pax}" class="popupSelect" onclick="changePaxPopup('${s.num}','${s.pax}','${s.minpax}','${s.maxpax}',this,'${s.roomtyp}','${s.id}');">
									</c:when>
									<c:otherwise>
										<td id="TD_PAX_${s.id}_${s.pax}" class="popupNoSelect" onclick="changePaxPopup('${s.num}','${s.pax}','${s.minpax}','${s.maxpax}',this,'${s.roomtyp}','${s.id}');">
									</c:otherwise>
								</c:choose>
									${s.roomtyp}
									<c:if test="${s.pax > 0}">(${s.pax}人)</c:if>
								</td>
							</tr>
							<c:set var="index" value="${index + 1}"/>
						</c:forEach>
						<c:if test="${index lt 1}"><tr><td>台位已经被预订光了，请选择其他餐次或日期</td></tr></c:if>
                        <tr><td>&nbsp;</td></tr>
					</table>
				</div><!-- wrapper -->
				<div class="popupComplate" onclick="popupComplate();">
					<table>
            			<tr>
            				<td>完成</td>
            			</tr>
            		</table>
                </div>
        	</div><!-- person -->
        	<div id="sfts" class="mypopup" data-role="popup" data-dismissible="false" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false"><!-- 选择sft -->
            	<div class="popupTitle">
               		<table>
            			<tr>
            				<td>选择预定时间段</td>
            			</tr>
            		</table>
                </div>
        		<div id="wrapperSfts" class="mypopupScroller">
					<table id="scrollerSfts">
                    	<tr><td>&nbsp;</td></tr>
						<c:forEach items="${sftMap}" var="s">
							<c:set var="Start_" value="${'Start_'}${s.key}"/>
							<c:set var="End_" value="${'End_'}${s.key}"/>
							<tr>
							<c:choose>
								<c:when test="${s.key eq sft}">
									<td id="TD_SFT_${s.key}" class="popupSelect" onclick="changeSftPopup('${seMap[Start_]}','${seMap[End_]}','${s.key}',this);">
								</c:when>
								<c:otherwise>
									<td id="TD_SFT_${s.key}" class="popupNoSelect" onclick="changeSftPopup('${seMap[Start_]}','${seMap[End_]}','${s.key}',this);">
								</c:otherwise>
							</c:choose>
									${s.value}
								</td>
							</tr>
						</c:forEach>
                        <tr><td>&nbsp;</td></tr>
					</table>
				</div><!-- wrapper -->
				<div class="popupComplate" onclick="popupComplate();">
                   	<table>
            			<tr>
            				<td>完成</td>
            			</tr>
            		</table>
                </div>
        	</div><!-- sfts -->
        </div>
    </div>
    <div data-role="content" style="width:100%;overflow:hidden;margin:0;list-style:none;padding:0;">
    	<div class="time">
        	<div class="timeTop">
            	<table>
                	<tr>
                    	<td class="timeTopLeft"><image src="<c:url value='/image/wechat/yuding_sj.png'/>"/></td>
                        <td class="timeTopRight">
                        	<span class="timeTopRightText">预计到店时间：今天 ${datmins}</span>
                        	<div style="display:none;font-family: Arial;" id="bookRoomTyp">预定类型：${defaultRoomTyp}${defaultMax_Min}人</div>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="timeBottom">
            	<table>
            		<c:set value="1" var="index"/>
            		<c:forEach items="${allTime}" var="t" varStatus="stat">
            			<c:if test="${index % 4 == 1}">
            				<tr>
            			</c:if>
            			<td id="T${t.key}" class="timeBottomTD" onclick="changeTime(this,'${t.key}');">
            				<c:choose>
            					<c:when test="${nowIndex > stat.count}">
            						<span class="timeSpan canotSelect">
                            	</c:when>
                            	<c:when test="${nowIndex == stat.count}">
            						<span class="timeSpan selected">
                            	</c:when>
                            	<c:otherwise>
                            		<span class="timeSpan canSelect">
                            	</c:otherwise>
            				</c:choose>
            						${t.value}
                            	</span>
            			</td>
            			<c:if test="${index % 4 == 0}">
            				<c:set value="0" var="index"/>
            				</tr>
            			</c:if>
            			<c:set value="${index + 1}" var="index"/>
            		</c:forEach>
            		<c:if test="${index > 1}">
            			<c:forEach begin="${index}" end="4">
            				<td class="timeBottomTD">
                        		&nbsp;
                       		</td>
            			</c:forEach>
            			</tr>
            		</c:if>
                </table>
                <div class="space">
    				&nbsp;
    			</div>
            </div>
        </div>
    </div>
    <div class="bottDiv" data-role="footer" data-position="fixed"  data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a">
					<c:choose>
						<c:when test="${empty nextType}">
							<a href="<c:url value='/dining/gotoFirm.do?pk_group=${pk_group}&firmid=${firmid}&openid=${openid}' />" data-ajax="false">
						</c:when>
						<c:otherwise>
							<a href="<c:url value='/dining/listFirmFromCity.do?openid=${openid}&pk_group=${pk_group}'/>" data-ajax="false">
						</c:otherwise>
					</c:choose>
						<img align="middle" src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td><!-- <img src="<c:url value='/image/wechat/back.png'/>"/> -->
				<td id="ui-b">&nbsp;</td>
				<td id="ui-c">
					<a id="gotoPagetwo" href="#pagetwo"/>
					<a onclick="gotoDetail();">预订</a>
				</td>
			</tr>
		</table>
  	</div><!-- footer -->
</div><!-- page -->
<div data-role="page" data-theme="d" id="pagetwo">
	<div data-role="content" style="width:100%;overflow:hidden;margin:0;list-style:none;padding:0;">
		<div style="background-color:#FFFFFF;">
        	<div>
            	<table>
               		<tr height="60px" class="detail" style="background-color: #fff;">
                    	<td colspan="2" class="timeTopLeft">
						<div style="float:left;font-size:110%;font-weight: bolder;margin-top: 6px;">
							${firm.firmdes}
						</div>
						<c:if test="${firm.tele ne null && firm.tele ne '' }">
							<div style="float:right;width:40px;margin-top: 3px;">
								<a href="tel:${firm.tele }"><img style="width:28px;height:28px;" src="<c:url value='/image/wechat/dianhua.png'/>"/></a>
							</div>
						</c:if>
						<c:if test="${firm.addr ne null && firm.addr ne '' }">
							<div onclick="alertMsg('${firm.addr}');" style="float:right;width:40px;margin-top: 3px;">
								<img src="<c:url value='/image/wechat/local1.png'/>" style="width:30px;height:30px;"/>
							</div>
						</c:if>
<%--                     		${firmdes} --%>
                    		</td>
                	</tr>
                	<tr height="30px">
                    	<td class="timeTopLeft" colspan="2" id="pagetwoRoomtyp">
                        	${defaultRoomTyp}${defaultMax_Min}人  
                        </td>
                    </tr>
                    <tr height="30px">
                    	<td class="timeTopLeft timeTopRight timeTopRightDetail timeTop" colspan="2" style="padding-left: 10px;">
                        	今天 ${datmins}
                        </td>
                    </tr>
                    <tr height="60px">
                    	<td class="timeTopLeft timeTop" style="width:50px;">姓名：</td>
                    	<td class="timeTop">
                        	<table>
                            	<tr>
                                	<td class="timeTopRight fontblod">
                                    	<input type="text" name="name" id="callName" value="${WeChatUser.nickname}" class="inputContent fontblod" data-role="none" style="border-color: #fff;width:93%;"/>
                                    	<input type="hidden" id="food" name="food" value="先生"/>
                                    </td>
                                    <td class="sexTD" id="sexTD1" onclick="changeSex(this,'1');">
                                   		<c:choose>
                                   			<c:when test="${WeChatUser.sex == 1}">
                                   				<span class="sexSpan selected">
                                   			</c:when>
                                   			<c:otherwise>
                                   				<span class="sexSpan canSelect">
                                   			</c:otherwise>
                                   		</c:choose>
                                     	先生</span>
                                    </td>
                                    <td class="sexTD" id="sexTD2" onclick="changeSex(this,'2');">
                                    	<c:choose>
                                       		<c:when test="${WeChatUser.sex == 2}">
                                   				<span class="sexSpan selected">
                                   			</c:when>
                                   			<c:otherwise>
                                   				<span class="sexSpan canSelect">
                                   			</c:otherwise>
                                   		</c:choose>
                                   		女士</span>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr height="60px">
                    	<td class="timeTopLeft timeTop"  style="width:50px;">电话：</td>
                        <td id="bookRoomTyp" class="timeTopRight fontblod timeTop">
                        	<input type="tel" name="contact" id="phoneNo" value="${tele}" class="inputContent fontblod" data-role="none" style="border-color: #fff;"/>
                        </td>
                    </tr>
                     <tr height="60px">
                    	<td class="timeTopLeft" style="width:50px;">备注：</td>
                        <td id="bookRoomTyp" class="timeTopRight fontblod">
                        	<input type="text" name="remark" id="remark" class="inputContent timeTopRightDetail" placeholder="请输入备注(可不填)" value="" data-role="none" style="border-color: #fff;">
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="time">
        	<table>
        		<tr height="60px" onclick="changSelectMeal('0',this);">
        			<td class="mealLeftSelect  timeTop" >
        				只订座，不提前点餐
        			</td>
        			<td class="mealRight  timeTop">
        				<img src="<c:url value='/image/wechat/select.png'/>"/>
        			</td>
        		</tr>
        		<tr height="60px" onclick="changSelectMeal('1',this);">
        			<td class="mealLeft">
        				提前点菜，到店起菜
        			</td>
        			<td class="mealRight">
        				<img src="<c:url value='/image/wechat/noselect.png'/>"/>
        			</td>
        		</tr>
        	</table>
        </div>
        <div style="padding:10px;">
        	温馨提示
        </div>
        <div style="padding-left:10px;padding-right:10px;font-size:80%;">
        	本店<span id="sftName"></span>到店时间为<span id="timeStart" style="font-family: Arial;"></span>到<span id="timeEnd" style="font-family: Arial;"></span>,望广大顾客在规定时间内用餐。用餐时请到餐厅就餐，给您带来不便请谅解，谢谢配合。
        </div>
        <div class="space">
    		&nbsp;
    	</div>
	</div><!-- content -->
	<div class="bottDiv" data-role="footer" data-position="fixed" data-tap-toggle="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td id="ui-a"><a href="#pageone" data-rel="back"><img align="middle" src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;</a></td><!-- <img src="<c:url value='/image/wechat/back.png'/>"/> -->
				<td id="ui-b">&nbsp;</td>
				<td id="ui-c"><a onclick="confirmOrder();">预订</a></td>
			</tr>
		</table>
  	</div><!-- footer -->
</div><!-- page -->
</form>
</body>
</html>