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
<link rel="stylesheet" href="<%=path %>/css/validate.css" />
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/validate.js"></script>
<script src="<%=path %>/js/layer.js"></script>
<style type="text/css">
#page{
	width:100%;
	height:100%;
	text-shadow:0px 0px 0px;
	background:#0f7ad8 repeat;
}
#page1{
	width:100%;
	min-height:100%;
	height:auto;
	margin:0 auto;
	background: #F9F8F6;
	background-size:cover;
}

/*内容样式*/
#main{
	width:100%;
	height:100%;
}
#firm_info{
	width:90%;
	margin:0px auto;
}
#firm_info table{
	width:100%;
	font-size:18px;
	font-weight:bold;
	color:#117ad6;
	position:relative;
	top:20px;
}
#tele{
	color:#117ad6;
}
</style>
<title>确认订位</title>
</head>
<body>
<div data-role="page" id="page">
<div id="page1">
	<!-- <div id="top"></div> -->
	<div id="main">
		<div id="firm_info">
			<form action="<%=path%>/pubitem/createOrder.do" method="post" data-ajax="false" id="form">
			<input type="hidden" value="${firm.firmid }" name="firmid" id="firmid"/>
			<input type="hidden" value="${firm.addr }" name="addr" />
			<input type="hidden" value="${sft }" name="sft" id="sft"/>
			<input type="hidden" value="${dat }" name="dat" id="dat"/>
			<input type="hidden" value="${pax }" name="realPax" id="realPax" />
			<input type="hidden" value="${openid }" name="openid" />
			<input type="hidden" value="${roomtyp }" name="roomtyp" id="roomtyp"  />
			<input type="hidden" value="${des }" name="tables" id="tbl" />
			<input type="hidden" value="${roomId }" name="resvtblid" id="roomId" />
			<table>
			<col style="width:30%" />
			<col style="width:70%" />
				<tr>
					<td align="left">店名：</td>
					<td>${firm.firmdes }</td>
				</tr>
				<tr>
					<td align="left">日期：</td>
					<td>${dat }&nbsp;
					<c:if test="${sft=='1' }">午市</c:if>
					<c:if test="${sft=='2' }">晚市</c:if>
					</td>
				</tr>
				<tr>
					<td align="left">时间：</td>
					<td>
						<select name="datmins">
							<c:if test="${sft=='1' }">
								<option value="11:00">11:00</option>
								<option value="11:30">11:30</option>
								<option value="12:00">12:00</option>
								<option value="12:30">12:30</option>
								<option value="13:00">13:00</option>
							</c:if>
							<c:if test="${sft=='2' }">
								<option value="17:30">17:30</option>
								<option value="18:00">18:00</option>
								<option value="18:30">18:30</option>
								<option value="19:00">19:00</option>
								<option value="19:30">19:30</option>
								<option value="20:00">20:00</option>
							</c:if>
						</select>
					</td>
				</tr>
				<tr>
					<td align="left">人数：</td>
					<td>
						<input name="pax" id="pax"  type="text" value="${pax }"/>
					</td>
				</tr>
				<tr>
					<td align="left">手机：</td>
					<td>
						<input name="contact" id="tele"  type="text"/>
					</td>
				</tr>
				<tr>
					<td align="left">验证码：</td>
					<td >
						<input type="text"  name="valicode" id="valicode" value=""/>
						<input type="button" style="font-size:11;"  data-inline="true" id="sendSms" value="获取验证码">
						<input type="hidden" id="rannum" value="111111">
					</td>
				</tr>
				<tr>
					<td align="left">地址：</td>
					<td>
						${firm.addr }
					</td>
				</tr>
				<tr>
					<td align="left">电话：</td>
					<td>
						<div style="float:left;padding:6px 0 0 0;">
							${firm.tele }
						</div>
						<div style="float:left;padding:3px 0 0 5px;">
							<a href="tel:${firm.tele }"><img height="25" width="25" src="<%=path %>/image/dianhua.png" /></a>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<textarea name="remark" id="remark" style="color:#ccc;">您还有什么需求，请在此留言！</textarea>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<br/>
		<div class="next_button_div" id="next_button_div" style="width:90%;margin:0px auto;">
		    <a href="#" data-role="button" id="next-button" data-ajax="false">下&nbsp;&nbsp;一&nbsp;&nbsp;步</a>
		</div>
		<br/>
   </div>
</div>

</div>
<script type="text/javascript">
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	var paxs = $("#realPax").val();
	var timeID;
	$(function(){
	 	var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
			$("body").text("请使用微信浏览器打开");
			return;
		}
		
		//var bodyWidth = $(document.body).width()*0.90*0.98;
		//$("#next-button").height(bodyWidth*7.5/52);
		
		$("#valicode").parent().css({"width":"34%","float":"left"});
		$("#sendSms").parent().css({"float":"right","margin-right":"0","color":"#117ad6"});
		
		
		var wait=60;
		//短信验证码
		function time() {
			if (wait == 0) {
				$("#sendSms").button("enable");
				$("#sendSms").val("获取验证码").button( "refresh" );
				wait = 60;
			} else {
				$("#sendSms").button( "disable" );
				$("#sendSms").val(wait + "秒可重发").button( "refresh" );
				wait--;
				timeID=setTimeout(function() {
					time();
				},
				1000);
			}
		}
		
		//发送验证码点击事件
		$("#sendSms").click(function(){
			var validate1 = new Validate({
				validateItem:[{
					type:'text',
					validateObj:'tele',
					validateType:['canNull'],
					param:['F'],
					error:['手机号码不能为空']
				},{
					type:'text',
					validateObj:'tele',
					validateType:['mobile'],
					error:['手机号码格式不正确']
				}]
			});
				if(validate1._submitValidate()){
					time();
					var card={};
					card["tele"]=$("#tele").val();
					$.post("<%=path %>/card/sendSms.do",card,function(data){
						$("#rannum").val(data);
					});
				}
		});
	});
	//注册下一步点击事件
	$("#next-button").click(function(){
		<%-- $(this).css('background',"url('<%=path %>/image/button02.png') no-repeat;");
		setTimeout(function(){
			$(this).css('background',"url('<%=path %>/image/next_step.png') no-repeat;");
		},300); --%>
		var validate = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'tele',
				validateType:['canNull'],
				param:['F'],
				error:['手机号码不能为空']
			},{
				type:'text',
				validateObj:'tele',
				validateType:['mobile'],
				error:['手机号码格式不正确']
			},{
				type:'text',
				validateObj:'valicode',
				validateType:['canNull'],
				param:['F'],
				error:['不为空']
			},{
				type:'text',
				validateObj:'pax',
				validateType:['canNull','num','minValue','maxValue'],
				param:['F','F',0,paxs],
				error:['就餐人数必填','必须为数字','就餐人数最小为1','就餐人数最大不能超过'+paxs]
			}]
		});
		//校验
		if(validate._submitValidate()){
			var rannum=$("#rannum").val();
			var valicode=$("#valicode").val();
			if(rannum == valicode){
				var remark= $("#remark").val();
				if(remark=='您还有什么需求，请在此留言！'){
					$("#remark").val('');
				}
				InitLayer();
				//校验是否被预定
				<%-- var card={};
				var roomId = $("#roomId").val();
				card["sft"]=$("#sft").val();//餐次
				card["dat"]=$("#dat").val();//就餐日期
				card["firmid"]=$("#firmid").val();//餐次
				card["pax"] = $("#realPax").val();//真正的桌台
				card["firmdes"] = $("#roomtyp").val();//借用来放桌台类型，防止乱码
				$.post("<%=path %>/pubitem/isOrder.do?roomId="+roomId,card,function(data){
					alert(data);
					if(data == "1"){//you
						alert("继续");
						$("#form").submit();
					}else{
						alert("对不起，已经被预定。");
					}
				}); --%>
				$("#form").submit();
			}else{
				alert("验证码错误！");
			}
		}
	});
	//处理验证码
	$("#valicode").focus(function(){
		$("#valicode").css("background", "#fff");
	});
	//处理备注
	$("#remark").focus(function(){
		if($(this).val()=='您还有什么需求，请在此留言！'){
			$(this).val('');
			$(this).attr("style","color:#117ad6;font-weight:bold;");
		}
	});

</script>
</body>
</html>