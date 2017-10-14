<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<link rel="stylesheet" href="<%=path %>/css/default/global.css"/>
<link rel="stylesheet" href="<%=path %>/css/default/pubitem/choiceFirm.css"/>
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/layer.js"></script>
<title>选择门店</title>
</head>
<body style="overflow-y:scroll;">
<div data-role="page" id="page">
<div id="page1">
	<!-- <div id="top"></div> -->
	<div id="main">
		<div class="bookdiv">
		<div class="bookleft">
			<table id="table0" cellpadding="0" cellspacing="0">
				<c:forEach items="${mapFirm}" var="letter">
				<c:forEach items="${letter.value}" var="firm" varStatus="status">
					<tr>
						<td valign="top" 
							<c:if test="${status.first }">
								id="${letter.key }"
							</c:if>
							>
							<c:if test="${status.first }">
								${letter.key }
							</c:if>
						</td>
						<td class="td1" id="${firm.firmid }">
							<p>${firm.firmdes }</p>
						</td>
						<td class="td2" id="${firm.firmid }">
							<table id="table1">
								<tr>
								<c:forEach items="${firm.listStoreTable }" var="table" varStatus="status">
									<c:if test="${table.roomtyp=='大厅' }">
										<th>${table.pax }人桌</th>
									</c:if>
									<c:if test="${table.roomtyp=='包间' }">
										<th>包间</th>
									</c:if>
									<td><span style="color:red;">${table.num }</span></td>
									<c:if test="${status.count%2==0 }">
										</tr><tr>
									</c:if>
									<c:if test="${status.last }">
										</tr>
									</c:if>
								</c:forEach> 
							</table>
						</td>
					</tr>
				</c:forEach>
				</c:forEach>
			</table>
		</div>
		<div class="bookright">
			<ul>
				<li>A</li>
				<li>B</li>
				<li>C</li>
				<li>D</li>
				<li>E</li>
				<li>F</li>
				<li>G</li>
				<li>H</li>
				<li>I</li>
				<li>J</li>
				<li>K</li>
				<li>L</li>
				<li>M</li>
				<li>N</li>
				<li>O</li>
				<li>P</li>
				<li>Q</li>
				<li>R</li>
				<li>S</li>
				<li>T</li>
				<li>U</li>
				<li>V</li>
				<li>W</li>
				<li>X</li>
				<li>Y</li>
				<li>Z</li>
			</ul>
		</div>
		</div>
	</div>
</div>
</div>
<script type="text/javascript">
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
		InitPage();
	});
	//动态初始化组件高度
	function InitPage(){
		var bodyHeight = $(document.body).height();
		var mainHeight = $("#page").height();
		$("#page1").height(mainHeight);
		$(".bookright").css("right",bodyHeight*0.01);
		$(".bookright").find("ul").height(bodyHeight);
		$(".bookright").find("ul li").height(bodyHeight/26);
	}
	//提交参数
	$(".td1,.td2").click(function(){
		InitLayer();
		var firmid = $(this).attr('id');
		var href = "<%=path%>/pubitem/choiceDesk.do?firmId="+firmid+"&dat=${dat}&sft=${sft}&openid=${openid}";
		window.location.href = href;
	});
	//注册字母查询功能
	$(".bookright").find("ul li").click(function(){
		var Letter = $(this).text();
		var Label = $("#"+Letter).offset();
		if(Label==null||typeof(Label) == 'undefine'){
			return;
		}
		var offsetTop = Label.top;
		//字体变换
		$(this).css("font-size","15px");
		var a = $(this);
		var time =setTimeout(function() {
			a.css("font-size","12px");
		},
		300);
		//alert(offsetTop);
		//$(window).scrollTop(offsetTop);
		//滚动条滚动
		$('body').animate({'scrollTop':offsetTop},1000);
		//修正魅族字母组跑位
		//$(".bookright").css({'position':'fixed','top':'0px'});
		
	});
</script>
</body>
</html>