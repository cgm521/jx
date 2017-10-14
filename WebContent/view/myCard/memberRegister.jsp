<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>吉祥馄饨_注册</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1.0, maximum-scale=1.0,user-scalable=no "/>
<meta name="description" content="辰森世纪" />
<meta name="keywords" content="辰森世纪" />
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/jxht_main.css" />' rel="stylesheet" />
<%-- <link type="text/css" href='<c:url value="/css/wechat/memberRegister.css" />' rel="stylesheet" /> --%>
<link type="text/css" href='<c:url value="/css/jxhd_validate.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/iscroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jweixin-1.0.0.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script type="text/JavaScript">
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
	
	var wait=60;
	var timeID;
	function time() {
		if (wait == 0) {
			$("#sendSms").removeAttr("disabled");
			$("#sendSms").css("background-color", "#FFB400");
			$("#sendSms").text("点击发送验证码");
			wait = 60;
		} else {
			$("#sendSms").attr("disabled",true);
			$("#sendSms").css("background-color", "#D5D5D5");
			$("#sendSms").text("重新发送(" + wait + ")");
			wait--;
			timeID=setTimeout(function() {
				time();
			},
			1000);
		}
	}
	
	// 发送验证码
	$("#sendSms").click(function(){
		var tel = $("#tele").val();
		var flag_tel = true;
		if(tel == null || tel == ""){
			$("#tele").attr("placeholder","手机号不能为空！");
			$("#tele").css("border","1px solid #EE2E52");
			flag_tel = false;
		}else if(!(/^1[34578]\d{9}$/.test(tel))){
			$("#tele").attr("placeholder","手机号格式不正确！");
			$("#tele").css("border","1px solid #EE2E52");
			$("#tele").val("");
			flag_tel = false;
		}
		
		if(flag_tel){
			time();
			var card={};
			card["tele"]=$("#tele").val();
			$.post("<%=path %>/card/sendSms.do",card,function(data){
				$("#rannum").val(data);
			});
		}
	});
	
	$("#tele").click(
			function(){
				$("#tele").css("border","none");
				$("#tele").attr("placeholder","请输入手机号");
			});
	
	$("#valicode").click(
			function(){
				$("#valicode").css("border","none");
				$("#valicode").attr("placeholder","请输入验证码");
			})
	
	
	// 绑定
	$("#bindBtn").click(function(){
			//$("#bindBtn").hide();
			var othersShareCode=$("#othersShareCode").val();
			var valicode=$("#valicode").val();
			var tel = $("#tele").val();
			var flag_valicode = true;
			var flag_tel = true;
			if(tel == null || tel == ""){
				$("#tele").attr("placeholder","手机号不能为空！");
				$("#tele").css("border","1px solid #EE2E52");
				flag_tel = false;
			}else if(!(/^1[34578]\d{9}$/.test(tel))){
				$("#tele").attr("placeholder","手机号格式不正确！");
				$("#tele").css("border","1px solid #EE2E52");
				$("#tele").val("");
				flag_tel = false;
			}
			if(valicode == null || valicode == ""){
				$("#valicode").attr("placeholder","不能为空！");
				$("#valicode").css("border","1px solid #EE2E52");
				flag_valicode = false;
			}
			
			
			if(flag_tel && flag_valicode && verifyShareCode(othersShareCode)){
				
				//InitLayer();
				var rannum=$("#rannum").val();	
				if(rannum == valicode){
					$("#valicode").css("background", "#0F3");
					var card={};
					card["tele"]=tel;
					//card["openid"]=$("#openid").val();这个openid必须要注释掉
 					card["chlb"]="会员";
					$.post("<%=path %>/myCard/verifyMemberExist.do", card, function(listCard){
						if(listCard.length!=0) {
							showSelectCardItem(listCard);
							
// 							var url = "<c:url value='/myCard/saveWXCard.do?pk_group=' />" + $("#pk_group").val() + "&name=" + $("#name").val()
// 									+ "&openid=" + $("#openid").val();
// 							location.href = url;
						} else {
							//var vname = encodeURI(encodeURI($('#name').val()));
							card["openid"]=$("#openid").val();
							var vname = $('#name').val();
							card["name"]="";
							card["pk_group"]=$("#pk_group").val();
		 					card["chlb"]="会员";
		 					card["firmId"] = "${firmCode}";
							card["passwd"]=$("#pwd").val();
							card["othersShareCode"]=$("#othersShareCode").val();
		 					card["typ"]="<%=Commons.getConfig().getProperty("registerCardTyp") %>";
		 					/*
							var url = "<c:url value='/myCard/saveWXMember.do?pk_group=' />" + $("#pk_group").val() + "&name=" + vname
									+ "&openid=" + $("#openid").val()+ "&tele=" + $("#tele").val();
							location.href = url;
							*/
							$.post("<c:url value='/myCard/saveWXMember.do' />", card, function(){
								location.href = "<c:url value='/myCard/cardInfo_afterRegister.do?openid=' />"+$("#openid").val();
							});
						}
					});
					
				}else{
					//closeLayer();
				}
			}else{
				//closeLayer();
 			}
	});
});
<%--  绑定实体卡代码
var myScrollSelectCard; 

//展示要选择绑定的实体会员卡	
function showSelectCardItem(listCard) {
	//alertMsg($("#openid").val());//13287756251
	//var itemClass = "itemNoSelect";
	$("#selectCardTable").find("div").remove();
	var itemHtml = "<div id='appendDiv' style='height:100%;z-index:999;text-align:center;'>";
	    
		itemHtml = itemHtml+"<table style='margin-top:-10px' >"
		itemHtml = itemHtml+"<tr><td>&nbsp;</td></tr>"
		/* 遍历会员卡号 */
	for (var i = 0; i < listCard.length; i++) {
		
		itemHtml = itemHtml + "<tr ><td  class='itemNoSelect' onclick='javascript:chooseItem(this)'>" 
						+ "<span>"+listCard[i].cardNo+"</span>"
						+"</td></tr>"
						
	}
	
	itemHtml = itemHtml+"<tr><td>&nbsp;</td></tr>"
	itemHtml=itemHtml+"</table></div>"
	$("#selectCardTable").append(itemHtml);
	if(myScrollSelectCard == null){
		myScrollSelectCard = new iScroll("fixedDivSelectCard",{hScrollbar:false,vScrollbar:false});
	}else{
		myScrollSelectCard.refresh();
	} 
$(".itemNoSelect").first().click();
if(listCard.length==1){
	$("#goBinding").click();
}else{
	$("#selectCard").popup("open");
}


}
function chooseItem(obj){
$("#selectCardTable .itemSelected").attr("class", "itemNoSelect");
$(obj).attr("class","itemSelected")
var cardNo=$(obj).find("span").first().text();	
$("#goBinding").find("input").first().val(cardNo);	
}
function goBindingMember(obj){

var card={};	
	card["openid"]=$("#openid").val();
 	card["cardNo"]=$(obj).find("input").first().val();	
 //	card["othersShareCode"]=$("#othersShareCode").val();
		$.post("<c:url value='/myCard/saveWXCard.do' />", card, function(){
				location.href = "<c:url value='/myCard/cardInfo.do?openid=' />"+$("#openid").val();
							}); 
	$("#selectCard").popup("close");
	InitLayer("正在加载，请稍后...");
}
--%>
//判断推荐码是否正确
function verifyShareCode(othersShareCode){
var exist=false;
//如果没写推荐码，此时认为推荐码正确
if(othersShareCode==null||othersShareCode==""){
	return true;
}
var card={};
card["myselfShareCode"]=othersShareCode;
$.ajax({ 
      type : "post", 
      url : "<%=path %>/myCard/verifyShareCode.do", 
      data : card, 
      async : false, 
      success : function(listCard){ 
        if(listCard.length==0){
			var code=$("#othersShareCode").val();
			code=code+"推荐码错误";
			$("#othersShareCode").val(code);
		}else{
			exist=true
		}	
      } 
      }); 
return exist;
} 
</script>
</head>
<body class="jxht_body">

<!--注册页-->
	<div class="jxht_reg" id="all">
		<div  class="jxht_reg_inputbox" id="part">
		    	<div class="jxht_reg_inbox_1">
		    		<input id="tele" class="inputContent" type="text" data-role="none" placeholder="请输入手机号" >
		    	</div>
		        <div class="jxht_reg_inbox_2">
		        	<input type="text"  class="jxht_reg_inputyzm" data-role="none"  name="valicode" id="valicode" placeholder="请输入验证码" >
		        	 <div class="jxht_reg_yzm" id="sendSms" >获取验证码</div>
		        	<input type="hidden" id="rannum" value="1111" >
		        	<input type="hidden" data-role="none" name="openid" id="openid" value="${openid}" >
					<input type="hidden" data-role="none" name="pk_group" id="pk_group" value="${pk_group }" >
		        </div>
		        <div class="jxht_reg_inbox_3">
		        	<input class="jxht_reg_inputyqr"  data-role="none" id="othersShareCode" type="text" placeholder="邀请人推荐码" >
		            <div class="jxht_reg_yqr">(选填)</div>
		        </div>
		</div><!--输入区域-->
	    
	    <!--注册按钮-->
		<a id="bindBtn" class="jxht_reg_inputbtn">立即注册</a><!--注册按钮-->
	    <!-- 暂时先不考虑<!-- -----------------------------------微信会员注册时，选择要绑定的实体卡------------------------------------------------------ --
		<a href="#selectCard" id="showSelectCard" data-rel="popup" data-position-to="window"></a>
		<div id="selectCard" data-role="popup" style="position:fixed; bottom:0px; width:100%; left:0px;height:40%;" data-overlay-theme="b" data-theme="a"  
				data-shadow="false" data-position-to="origin" data-corners="false"   data-history="false">
			<div class="popupTitle">
	            		<table>
	            			<tr>
	            				<td>选择卡号</td>
	            			</tr>
	            		</table>
	                </div>
			<div id="fixedDivSelectCard" class="mypopupScroller">
				<table id="selectCardTable">
				</table>
			</div>
			<div id="goBinding"  class="popupComplate" onclick="goBindingMember(this);">
	            <table  >
	            	<tr><td><span style="padding:20px">绑定</span> </td></tr>
	            	<tr><td> <input type="hidden" value/></td></tr>
	            </table>		
	       </div>
			
		</div> -->
	</div><!--注册页-->
	
</body>
</html>