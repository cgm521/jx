<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="viewport" content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta name="description" content="">

<title>幸运大转盘抽奖</title>
<link href="./css/activity-style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
</head>

<body class="activity-lottery-winning">
<div class="main">
<script type="text/javascript">
//var loadingObj = new loading(document.getElementById('loading'),{radius:20,circleLineWidth:8});   
//    loadingObj.show();   
</script>
 <div id="outercont">
<div id="outer-cont">
<div id="outer"><img src="./css/activity-lottery-1.png" width="310px"></div>
</div>
<div id="inner-cont">
<div id="inner"><img src="./css/activity-lottery-2.png"></div>
</div>
</div>
<div class="content">
<div class="boxcontent boxyellow" id="result" style="display:none">
<div class="box">
<div class="title-orange"><span>恭喜你中奖了</span></div>
<div class="Detail">
            <a class="ui-link" href="" id="opendialog" style="display: none;" data-rel="dialog"></a>
<p>你中了：<span class="red" id="prizetype">一等奖</span></p>
<p>你的兑奖SN码：<span class="red" id="sncode"></span></p>
<p class="red">本次兑奖码已经关联你的微信号，请关注公众号发送的奖品领取成功信息!</p>
               
<p>
<c:choose>
	<c:when test="${empty clientID}"><input name="" class="px" id="tel" type="text" placeholder="输入您的手机号码"></c:when>
	<c:otherwise><input name="" id="tel" type="hidden"></c:otherwise>
</c:choose>
<input type="hidden" value="${openid}" name="openid" id="openid"/>
<input type="hidden" value="${clientID}" name="clientID" id="clientID"/>
</p>
<p>
<input class="pxbtn" id="save-btn" name="提 交" type="button" value="提 交">
</p>
</div>
</div>
</div>
<div class="boxcontent boxyellow">
<div class="box">
<div class="title-green"><span>奖项设置：</span></div>
<div class="Detail">
<c:forEach items="${gifts}" var="t">
<p>
	<c:choose>
		<c:when test="${t.lucky==1}">
			一等奖：
		</c:when>
		<c:when test="${t.lucky==2}">
			二等奖：
		</c:when>
		<c:when test="${t.lucky==3}">
			三等奖：
		</c:when>
	</c:choose>
	${t.luckyName}。奖品数量：${t.quantity}
</p>
</c:forEach>
</div>
</div>
</div>
<div class="boxcontent boxyellow">
<div class="box">
<div class="title-green">活动说明：</div>
<div class="Detail">
<p>本次活动每人可以转 3 次 </p>
               <p> 我们的中奖率高达33.3%！！ </p>
</div>
</div>
</div>
</div>

</div>
<script src="./css/jquery.js" type="text/javascript"></script> 
<script type="text/javascript">
$(function(){
	window.requestAnimFrame=(function(){
		return window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||window.oRequestAnimationFrame||window.msRequestAnimationFrame||function(callback){
					window.setTimeout(callback,1000/60)
				}
	})();
	var totalDeg=360*3+0;
	var steps=[];
	var lostDeg=[36,66,96,156,186,216,276,306,336];
	var prizeDeg=[6,126,246];
	var prize,sncode;
	var count=0;
	var now=0;
	var a=0.01;
	var outter,inner,timer,running=false;
	function countSteps(){
		var t=Math.sqrt(2*totalDeg/a);
		var v=a*t;
		for(var i=0;i<t;i++){
			steps.push((2*v*i-a*i*i)/2)
		}
		steps.push(totalDeg)
	}
	function step(){
		outter.style.webkitTransform='rotate('+steps[now++]+'deg)';
		outter.style.MozTransform='rotate('+steps[now++]+'deg)';
		if(now<steps.length){
			requestAnimFrame(step)
		}else{
			running=false;
			setTimeout(function(){
				if(prize!=null){
					$("#sncode").text(sncode);
					var type="";
					if(prize==1){
						type="一等奖"
					}else if(prize==2){
						type="二等奖"
					}else if(prize==3){
						type="三等奖"
					}
					$("#prizetype").text(type);
					$("#result").slideToggle(500);
					$("#outercont").slideUp(500)
				}else{
					alert("谢谢您的参与，下次再接再厉")
				}
			},200)
		}
	}
	function start(deg){
		deg=deg||lostDeg[parseInt(lostDeg.length*Math.random())];
		running=true;
		clearInterval(timer);
		totalDeg=360*5+deg;
		steps=[];
		now=0;
		countSteps();
		requestAnimFrame(step)
	}
	window.start=start;
	outter=document.getElementById('outer');
	inner=document.getElementById('inner');
	i=10;
	$("#inner").click(function(){
		if(running)return;
		if(count>=3){
			alert("您已经抽了 3 次奖。");
			return
		}
		if(prize!=null){
			alert("亲，你不能再参加本次活动了喔！下次再来吧~");
			return
		}
		/* */
		$.ajax({
			url:"running.do",
			dataType:"json",
			data:{openid:"${openid}",ac:"activityuser",tid:"5",t:Math.random()},
			method:"post",
			beforeSend:function(){
				running=true;
				timer=setInterval(function(){
					i+=5;
					outter.style.webkitTransform='rotate('+i+'deg)';
					outter.style.MozTransform='rotate('+i+'deg)'
				},1)
			},
			success:function(data){
				if(data.error=="invalid"){
					alert("您已经抽了 3 次奖。");
					count=3;
					clearInterval(timer);
					return
				}
				if(data.error=="getsn"){
					alert('本次活动你已经中过奖，本次只显示你上次抽奖结果!兑奖SN码为:'+data.sn);
					count=3;
					clearInterval(timer);
					prize=data.prizetype;
					sncode=data.sn;
					start(prizeDeg[data.prizetype-1]);
					return;
				}
				if(data.success){
					prize=data.prizetype;
					sncode=data.sn;
					start(prizeDeg[data.prizetype-1])
				}else{
					prize=null;
					start()
				}
				running=false;
				count++
			},
			error:function(){
				prize=null;
				start();
				running=false;
				count++
			},
			timeout:4000
		})
		
	})
});
$("#save-btn").bind("click",function(){
	var btn=$(this);
	var tel=$("#tel").val();
	
	var clientID1 = $("#clientID").val()
	if(clientID1==null || clientID1==""){
		if(tel==''){
			alert("请输入手机号码");
			return;
		}
		var regu=/^[1][0-9]{10}$/;
		var re=new RegExp(regu);
		if(!re.test(tel)){
			alert("请输入正确手机号码");
			return
		}
	}
	
	InitLayer();
	var submitData={code:$("#sncode").text(),tel:tel,action:"setTel",clientID:"${clientID}",openid:"${openid}",pk_group:"${pk_group}"};
	$.post('setTel.do',submitData,function(data){
		if(data.success==true){
			alert("提交成功，谢谢您的参与");
			$("#result").slideToggle(500);
			closeLayer();
			return;
		}else{
			alert("奖品赠送失败，请稍后再试");
			return;
		}
	},"json");
});
</script>
</body></html>