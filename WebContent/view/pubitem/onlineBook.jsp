<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@page import="com.choice.test.utils.Commons"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<link rel="stylesheet" href="<%=path %>/css/jquery-calendar.css" />
<link rel="stylesheet" href="<%=path %>/css/default/global.css" />
<link rel="stylesheet" href="<%=path %>/css/default/pubitem/onlineBook.css" />
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/layer.js"></script>
<script src="<%=path %>/js/jquery-calendar.js"></script>
<title>自助点餐</title>
</head>
<body>
<div data-role="page" id="page" class="page">
<div class="firstPage_top"><div><%=Commons.wx_title %></div></div>
<div class="main">
	<div style="width:94%;height:auto;margin:0 auto;">
	<form id="nextForm" action="<%=path %>/pubitem/listPubitem.do" method="post" data-ajax="false">
		<input type="hidden" id="addr" name="addr" value=""/>
		<input type="hidden" name="openid" value="${openid }"/>
		<div id="btnsDiv">
				<div id="city_label">
					<div id='citylabelleft'></div>
					<div id='citylabelright'><span style='float:left;'>城</span><span style="float:right;">市</span></div>
				</div>
		        <select id="city" data-icon="" onchange="changeCity(this)">
		         <option value="" disabled="disabled" selected="selected">选择城市</option>
		         <c:forEach items="${listCity }" var="city">
		         	<option value="${city.sno }">${city.des }</option>
		         </c:forEach>
		        </select>
		        <div id="store_label">
					<div id='storylabelleft'></div>
					<div id='storylabelright'><span style='float:left;'>门</span><span style="float:right;">店</span></div>
				</div>
		      <select name="firmid" id="store" data-icon="" onchange="storeChange(this)">
		         <option value="" disabled="disabled" selected="selected">选择门店</option>
		        </select>
		     	 <!-- 日历隐藏参数 -->
		        <input name="dat" type="hidden" id="calendar" />
		        <div id="dat_label">
					<div id='datlabelleft'></div>
					<div id='datlabelright'>预定日期</div>
				</div>
		        <select id="date1" data-icon="">
		         	<option value="" disabled="disabled">选择日期</option>
		        </select>
		        <div id="sb_label">
					<div id='sblabelleft'></div>
					<div id='sblabelright'><span style='float:left;'>市</span><span style="float:right;">别</span></div>
				</div>
		        <select name="sft" id="sb" data-icon="" onchange="sbChange(this)">
			         <option value="" disabled="disabled" selected="selected">选择市别</option>
			         <option value="1">午市</option>
			         <option value="2">夜市</option>
		        </select>
		        <div id="time_label">
					<div id='timelabelleft'></div>
					<div id='timelabelright'><span style='float:left;'>时</span><span style="float:right;">间</span></div>
				</div>
		        <select name="datmins" id="time" data-icon="">
			         <option value="" disabled="disabled" selected="selected">选择时间</option>
		        </select>
	    </div>
	    <div class="next_button_div" id="next_button_div">
		    <a href="#" data-role="button">下&nbsp;&nbsp;一&nbsp;&nbsp;步</a>
		</div>
		</form>
		<br/>
	</div>
</div>
</div>
<a href="#dialogPage" id="dialogbtn" data-rel="dialog"></a>
<div data-role="page" id="dialogPage" data-overlay-theme="e">
  <div data-role="header">
    <h1>温馨提示</h1>
  </div>
  <div data-role="content">
    <p id="hint" style="text-aling:center;"></p>
    <a href="#page" data-role="button">确定</a>
  </div>
</div> 
<script type="text/javascript">
/**去除微信返回，刷新，分享**/
   document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});  
	$(function(){
		/**必须使用微信打开**/
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
			$("body").text("请使用微信浏览器打开");
			return;
		} 
		
 		//$(".ui-select:eq(0)").attr("style","margin-top:15px");
		/*var bodyWidth = $(document.body).width()*0.85;
		var btHeight = bodyWidth*14/75;
		$("#city-button").height(btHeight);
		$("#date1-button").height(btHeight);
		$("#sb-button").height(btHeight);
		$("#store-button").height(btHeight);
		$("#time-button").height(btHeight);
		//$("#next-button").height(bodyWidth*6/52); */

		/* var btHeight = $("#city-button").css("height");
		alert(btHeight);
		$("#city_label").css({'top':btHeight/2});
		$("#store_label").css({'top':btHeight/2+btHeight});
		$("#dat_label").css({'top':btHeight/2+btHeight*2});
		$("#sb_label").css({'top':btHeight/2+btHeight*3});
		$("#time_label").css({'top':btHeight/2+btHeight*4}); */
		
		$("#date1").selectmenu('disable');
		$('#date1').css('display', 'none').parent('div').css({'filter': 'Alpha(Opacity=100)','opacity':'1'}); 
		$('select').selectmenu('refresh'); 
		
		//调用时间日期插件并回传参数
		$('#date1').parent().parent().click(function(){
			$("#calendar").calendar();
		});
	});
	//提交参数
	$("#next_button_div").click(function(){
		var city = $("#city").val();
		var store = $("#store").val();
		var date = $("#calendar").val();
		var sft = $("#sb").val();
		var time = $("#time").val();
		
		if(city==null||city==''){
			$("#hint").empty().text("请选择城市。");
			$("#dialogbtn").click();
			return;
		}
		if(store==null||store==''){
			$("#hint").empty().text("请选择门店。");
			$("#dialogbtn").click();
			return;
		}
		if(date==null||date==''){
			$("#hint").empty().text("请选择日期。");
			$("#dialogbtn").click();
			return;
		}
		if(sft==null||sft==''){
			$("#hint").empty().text("请选择市别。");
			$("#dialogbtn").click();
			return;
		}
		if(time==null||time==''){
			$("#hint").empty().text("请选择时间。");
			$("#dialogbtn").click();
			return;
		}
		var currTime = new Date();
		date = new Date(Date.parse(date));
		if(date.getFullYear() == currTime.getFullYear() && date.getMonth() == currTime.getMonth() && date.getDate() == currTime.getDate()){
			var currHour = currTime.getHours();
			var currMinute = currTime.getMinutes();
			if(sft == '1'){//选择的是今天的午市
				if(currHour > 10 || (currHour == 10 && currMinute > 30)){
					$("#hint").empty().text("抱歉，当天午市在线预订截止上午10:30。");
					$("#dialogbtn").click();
					return;
				}
			}else if(sft == '2'){//选择的是今天的夜市
				if(currHour > 16 || (currHour == 16 && currMinute > 30)){
					$("#hint").empty().text("抱歉，当天夜市在线预订截止下午4:30。");
					$("#dialogbtn").click();
					return;
				}
			}
		}
		InitLayer();
		$("#nextForm").submit();
	});
	
	//城市变化
	function changeCity(obj){
		InitLayer();
		var store={};
		var area=$(obj).val();
		$("#store").empty();
		$("#store").append("<option value='' disabled='disabled'>选择门店</option>");
		$("#store").val("");
		$("#store-button > span").text("选择门店");
		$.post("<%=path %>/pubitem/getStore.do?area="+area,store,function(data){
			for(var i=0;i<data.length;i++){
				var html = "<option value='"+data[i].firmid+"' addr='"+data[i].addr+"'>"+data[i].firmdes+"</option>";
				$("#store").append(html);
			}
			closeLayer();
		});
	}
	
	//市别选择
	function sbChange(obj){
		$("#time").empty();
		$("#time").append("<option value='' disabled='disabled'>选择时间</option>");
		$("#time").val("");
		$("#time-button > span").text("选择时间");
		if($(obj).val() == "1"){//午市
			var date1 = new Date(11*60*60*1000);
			var date2 = new Date(14*60*60*1000);
			changeTime(date1,date2);
		}else if($(obj).val() == "2"){//夜市
			var date1 = new Date(16*60*60*1000);
			var date2 = new Date(18*60*60*1000);
			changeTime(date1,date2);
		}
	}
	
	//门店选择
	function storeChange(obj){
		var addr = $(obj).find("option:selected").attr("addr");
		$("#addr").val(addr);
	}
	
	/**初始化日期**/
	/* function InitDate(){
		for(var i=0;i<10;i++){
			var uom = new Date(new Date()-0+i*86400000);  
			uom = uom.getFullYear() + "-" + (uom.getMonth()+1) + "-" + uom.getDate();  
			var html = "<option >"+uom +"</option>";
			$("#date1").append(html);
		} 
	} */
	
	/*根据市别选择时间*/
	function changeTime(date1,date2){
		var i = 1;
		var date1time = date1.getTime();
		while(true){
			var hour = date1.getUTCHours();
			var minutes = date1.getUTCMinutes();
			if(minutes == 0){
				minutes = "00";
			}
			var time = hour+":"+minutes;
			$("#time").append("<option value='"+time+"'>"+time+"</option>");
			if(date1.getTime() == date2.getTime()){return;}
			date1 = new Date(date1time + 15*60*1000*i);
			i+= 1;
		}
	}
	
</script>
</body>
</html>