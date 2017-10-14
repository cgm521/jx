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
<link rel="stylesheet" href="<%=path %>/css/default/pubitem/OrderFirst.css" />
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/jquery-calendar.js"></script>
<script src="<%=path %>/js/layer.js"></script>
<title>在线订位</title>
</head>
<body>
<div data-role="page" id="page" class="page">
	<div class="firstPage_top"><div><%=Commons.wx_title %></div></div>
	<div class="main">
		<div style="width:94%;height:auto;margin:0 auto;">
		<div class="btnsDiv">
				<label id='city_label'><span style="float:left;">城</span><span style="float:right">市</span></label>
		        <select id="city" data-icon="">
		         <option value="" disabled="disabled" selected="selected">选择城市</option>
		         <c:forEach items="${listCity }" var="city">
		         	<option value="${city.sno }">${city.des }</option>
		         </c:forEach>
		         <!-- 
		         <option value="jn">济南</option>
		         <option value="bj">北京</option>
		         <option value="sh">上海</option>
		         <option value="tj">天津</option>
		         <option value="gz">广州</option>
		         <option value="sz">深圳</option>
		         <option value="nj">南京</option> -->
		        </select>
		        <!-- 日历隐藏参数 -->
		        <input type="hidden" id="calendar" />
		        <label id='dat_label'>预订日期</label>
		        <select id="date1" data-icon="">
		         	<option value="" disabled="disabled" selected="selected">选择日期</option>
		        </select>
		        <label id='sb_label'><span style="float:left;">市</span><span style="float:right">别</span></label>
		        <select id="sb" data-icon="">
		         <option value="" disabled="disabled" selected="selected">选择市别</option>
		         <%-- <c:forEach items="${listSft }" var="sft" >
		         	<option value="${sft.code }">${sft.name }</option>
		         </c:forEach> --%>
		         <option value="1">午市</option>
		         <option value="2">夜市</option>
		        </select>
	    </div>
	    <div class="next_button_div" id="next_button_div">
		    <a href="#" data-role="button" id="next-button" data-ajax="false">下&nbsp;&nbsp;一&nbsp;&nbsp;步</a>
		</div>
		<br/>
	</div>
	</div>
</div>
<a href="#dialogPage" id="dialogbtn" data-rel="dialog"></a>
<input type="hidden" name="id" id="orderid" value="${orderid}"/>
<input type="hidden" name="openid" id="openid" value="${openid}"/>
<input type="hidden" name="firmid" id="firmid" value="${firmid}"/>
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
	//去除安卓底部后退及刷新按钮
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	
	$(function(){
		//控制只能用微信浏览器打开
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
			$("body").text("请使用微信浏览器打开");
			return;
		}
		//动态定义按钮高度
		$(".ui-select:eq(0)").attr("style","margin-top:15px");
		var bodyWidth = $(document.body).width()*0.90*0.90;
		var btHeight = 60; //bodyWidth*14/70;
		$("#city-button").height(btHeight);
		$("#date1-button").height(btHeight);
		$("#sb-button").height(btHeight);
		//$("#next-button").height(bodyWidth*7.5/52);
		//动态定义标签位置
		$("#city_label").width($("#dat_label").width());
		$("#sb_label").width($("#dat_label").width());
		$("#city_label").css({'top':btHeight/2-14,'left':30+$(document.body).width()*0.01});
		$("#dat_label").css({'top':btHeight*1.5-13,'left':30+$(document.body).width()*0.01});
		$("#sb_label").css({'top':btHeight*2.5-12,'left':30+$(document.body).width()*0.01});
		
		//处理时间按钮
		$("#date1").selectmenu('disable');
		$('#date1').css('display', 'none').parent('div').css({'filter': 'Alpha(Opacity=100)','opacity':'1'}); 
		$('select').selectmenu('refresh'); 
		
		//调用时间日期插件并回传参数
		$('#date1').parent().parent().click(function(){
			$("#calendar").calendar();
		});
	});
	function InitDate(){
		for(var i=0;i<10;i++){
			var uom = new Date(new Date()-0+i*86400000);  
			uom = uom.getFullYear() + "-" + (uom.getMonth()+1) + "-" + uom.getDate();  
			var html = "<option >"+uom +"</option>";
			$("#date1").append(html);
		} 
	}
	//提交参数
	$("#next-button").click(function(){
		var city = $("#city").val();
		var date1 = $("#calendar").val();
		var date = $("#calendar").val();
		var sft = $("#sb").val();
		
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
		/* var preDate=new Date(); 
		var hour = preDate.getHours();
		preDate.setHours(hour, preDate.getMinutes(), preDate.getSeconds(), 0);
		
		var nowDate = preDate;
		if(date.getFullYear() == nowDate.getFullYear() && date.getMonth() == nowDate.getMonth() && date.getDate() == nowDate.getDate()){
			if(((nowDate.getHours()+'-'+nowDate.getMinutes())>'10-30') && sft==1){
				$("#hint").empty().text("抱歉，当天午市在线预订截止上午10:30。");
				$("#dialogbtn").click();
				return;
			}
			if(((nowDate.getHours()+'-'+nowDate.getMinutes())>'16-30') && sft==2){
				$("#hint").empty().text("抱歉，当天夜市在线预订截止下午4:30。");
				$("#dialogbtn").click();
				return;
			}
		} */
		
		if(city==null||city==''){
			$("#hint").empty().text("请选择城市");
			$("#dialogbtn").click();
			return;
		}

		if(date1==null||date1==''){
			$("#hint").empty().text("请选择预订日期");
			$("#dialogbtn").click();
			return;
		}
		if(sft==null||sft==''){
			$("#hint").empty().text("请选择市别");
			$("#dialogbtn").click();
			return;
		}
		InitLayer();//遮罩
		var href = "<%=path%>/pubitem/choiceFirm.do?city="+city+"&dat="+date1+"&sft="+sft+"&openid=${openid}";
		$(this).attr("href",href);
	});
	
</script>
</body>
</html>