<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.choice.test.utils.Commons" %>
<%String path=request.getContextPath(); %>
<html> 
　<head> 
　 <title></title> 
   <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
　 <link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
   <link rel="stylesheet" href="<%=path %>/css/validate.css" />
   <link rel="stylesheet" href="<%=path %>/css/default/global.css" />
   <link rel="stylesheet" href="<%=path %>/css/default/card/listCard.css" />
　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
   <script src="<%=path %>/js/validate.js"></script>
   <script src="<%=path %>/js/layer.js"></script>
</head>
<body>
	<div data-role="page" class="page">
	<c:forEach items="${ listCard }" var="card">
		<!-- <c:if test="${card.runNum !=null and card.runNum !=''}">
			<div id="header">
				<ul id="cardhead">
					<li>
						<img src="<%=Commons.vcardPic %>${card.qrordr}.png" width="75" height="75">
					</li>
					<li style="margin:4% 0 0 3%;"><span>${card.typDes}<br/>卡号：${card.cardNo }</span></li>
				</ul>
			</div>
		</c:if> -->
		<div id="header">
			<div style="padding-top:130px; padding-left:80px;">
				<span style="font-size:120%;">${card.typDes}<br/>卡号：${card.cardNo }</span>
			</div>
		</div>
		<div id="bkgMain">
			<div id="bkgulMain">
				<div id="two">
					<table>
						<tr>
							<td><div style="float: left;padding: 1px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_05.png"></div>&nbsp;<div style="float: left;padding-left:4px;"><a style="color:#000;font-weight: normal;" data-ajax="false" href="<%=path %>/card/cardZAmt.do?zamt=${card.zAmt }">余额：${card.zAmt }元&nbsp;</a></div><div style="float: right;padding: 1px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div></td>
						</tr>
						<tr style="border-top: solid #DCD7D6 1px;">
							<td><div style="float: left;padding: 1px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_05.png"></div>&nbsp;<div style="float: left;padding-left:4px;"><a style="color:#000;font-weight: normal;" data-ajax="false" href="<%=path %>/card/cardTtlFen.do?ttlFen=${card.ttlFen }">积分：${card.ttlFen }&nbsp;分</a></div><div style="float: right;padding: 1px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div></td>
						</tr>
						<tr style="border-top: solid #DCD7D6 1px;">
							<td><div style="float: left;padding: 1px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_05.png"></div>&nbsp;<div style="float: left;padding-left:4px;"><a style="color:#000;font-weight: normal;" data-ajax="false" href="<%=path %>/card/cardVoucher.do?openid=${openid }">电子券：</a></div><div style="float: right;padding: 1px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div></td>
						</tr>
					</table>
				</div>
				<br>
				<div id="three">
					<table>
						<tr>
							<td>
								<div style="float: left;padding: 1px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_11.png"></div>&nbsp;
								<div style="float: left;padding-left:4px;">
									<a style="color:#000;font-weight: normal;" data-ajax="false" id="zx" href="">会员专享特权：</a>
								</div>
								<div style="float: right;padding: 1px 10px 0 0px;"><img id="zhuanxiang_jt" width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div>
							</td>
						</tr>
					</table>
					<div style="width:100%;background-color:#F1F0EF;" id="zhuanxiang">
						<div style="height:100%;padding:0px 0 0 0;margin:-2% 0 0 0;">${exclusprivle }</div>
					</div>
				</div>
				<br>
				<div id="fore">
					<table>
						<tr>
							<td><div style="float: left;padding: 1px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_13.png"></div>&nbsp;<div style="float: left;padding-left:4px;"><a style="color:#000;font-weight: normal;" data-ajax="false" href="<%=path %>/card/listChargeRecord.do?openid=${openid }&cardNo=${card.cardNo }">充值记录：</a></div><div style="float: right;padding: 1px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div></td>
						</tr>
						<tr style="border-top: solid #DCD7D6 1px;">
							<td><div style="float: left;padding: 1px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_13.png"></div>&nbsp;<div style="float: left;padding-left:4px;"><a style="color:#000;font-weight: normal;" data-ajax="false" href="<%=path %>/card/listConsumeRecord.do?openid=${openid }&cardNo=${card.cardNo }">消费记录：</a></div><div style="float: right;padding: 1px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div></td>
						</tr>
					</table>
				</div>
				<br>
				<div id="firve">
					<table>
						<tr id="tr1">
							<td><div style="float: left;padding: 1px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_19.png"></div>&nbsp;<div style="float: left;padding-left:4px;"><a style="color:#000;font-weight: normal;" data-ajax="false" href="<%=path %>/card/cardExplain.do">会员卡说明：</a></div><div style="float: right;padding: 1px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div></td>
						</tr>
						<tr id="tr2" style="border-top: solid #DCD7D6 1px;">
							<td><div style="float: left;margin: -2px 0 0 6px;"><img style="margin-top: 5px;" width="15" height="15" src="<%=path %>/image/zhuce_02.png"></div>&nbsp;<div style="float: left;padding-left:4px;"><a style="color:#000;font-weight: normal;" data-ajax="false" href="<%=path %>/card/updateTeleHtml.do?openid=${openid }">手机：${card.tele }</a></div><div style="float: right;padding: 1px 10px 0 0px;"><img width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div></td>
						</tr>
						<tr id="tr3" style="border-top: solid #DCD7D6 1px;">
							<td>
								<div style="float: left;margin: -2px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_19.png"></div>&nbsp;
								<div style="float: left;padding-left:4px;">
									<a style="color:#000;font-weight: normal;" data-ajax="false" href="#">会员中心电话：${cardtele }</a>
								</div>
							</td>
						</tr>
						<tr  id="tr4" style="border-top: solid #DCD7D6 1px;">
							<td>
								<div style="float: left;margin: -2px 0 0 6px;"><img width="15" height="15" src="<%=path %>/image/huiyuanka_21.png"></div>&nbsp;
								<div style="float: left;padding-left:4px;">
									<a style="color:#000;font-weight: normal;" data-ajax="false" id="firm">地址：</a>
								</div>
								<div style="float: right;padding: 1px 10px 0 0px;"><img id="firm_jt" width="15" height="15" src="<%=path %>/image/jiantouLeft.png"></div></td>
						</tr>
					</table>
					<div style="width:100%;background-color:#F1F0EF;" id="fendian">
						<div style="height:100%;padding:0px 0 0 0;margin:-2% 0 0 0;">${store }</div>
					</div>
				</div>
		    </div>
		    <br/>
	    </div>
   </c:forEach>
   </div>
   <script type="text/javascript">
	    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		    WeixinJSBridge.call('hideToolbar');
		    WeixinJSBridge.call('hideOptionMenu');
		});
		$(function(){
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i)=="micromessenger") {
		 	} else {
				$("body").text("请使用微信浏览器打开");
			}
			
			$("#zhuanxiang").hide();
			$("#fendian").hide();
			$("#zx").click(function(){
				$("#zhuanxiang").toggle();
			});
			$("#firm").click(function(){
				$("#fendian").toggle();
			});
			$("#two tr").each(function(){
				$(this).click(function(){
					$(this).find("a").click(function(){
					});
					var link1=$(this).find("a").attr("href");
					window.location.href=link1;
				});
			});
			$("#three tr").each(function(){
				$(this).click(function(){
					if($("#zhuanxiang_jt").attr("src").indexOf('Left') == -1){//不是向右的图标
						$("#zhuanxiang_jt").attr("src","<%=path %>/image/jiantouLeft.png");
					}else{
						$("#zhuanxiang_jt").attr("src","<%=path %>/image/jiantouDown.png");
					}
					$("#zhuanxiang").toggle();
				});
			});
			$("#fore tr").each(function(){
				$(this).click(function(){
					$(this).find("a").click(function(){
					});
					var link1=$(this).find("a").attr("href");
					window.location.href=link1;
				});
			});
			$("#tr1").click(function(){
				$("#tr1").find("a").click(function(){
				});
				var link1=$(this).find("a").attr("href");
				window.location.href=link1;
			});
			$("#tr2").click(function(){
				$("#tr2").find("a").click(function(){
				});
				var link1=$(this).find("a").attr("href");
				window.location.href=link1;
			});
			$("#tr3").click(function(){
				$("#tr3").find("a").click(function(){
				});
				var link1=$(this).find("a").attr("href");
				window.location.href=link1;
			});
			$("#tr4").click(function(){
				if($("#firm_jt").attr("src").indexOf('Left') == -1){//不是向右的图标
					$("#firm_jt").attr("src","<%=path %>/image/jiantouLeft.png");
				}else{
					$("#firm_jt").attr("src","<%=path %>/image/jiantouDown.png");
				}
				$("#fendian").toggle();
			});
		});
   </script>
</body>
</html>