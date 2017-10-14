<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons"%>
<%String path = request.getContextPath();
	String port ="80".equals((request.getServerPort()+""))?"":(":"+request.getServerPort()+"");
	String basePath = request.getScheme()+"://"+request.getServerName()+port+path;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>支付</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link type="text/css" href='<c:url value="/css/wechat/maidan.css" />' rel="stylesheet" />
<link rel="stylesheet" href="<%=path %>/css/wechat/listMenu.css" />
<link type="text/css" href='<c:url value="/css/validate.css" />' rel="stylesheet" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="<c:url value='/js/validate.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<script language="JavaScript" type="text/JavaScript">
	var result= "";
	var cnt=0; //电子券选择数量大于1时不能再选择电子券
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	$(function(){
		window.history.go(1);//禁用后退按钮功能，点击后自动回到上个页面
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)!="micromessenger") {
			$("body").text("请使用微信浏览器打开");
			return;
		}
		
		// 根据配置文件判断是否展示开发票选项
		if('<%=Commons.getConfig().getProperty("showInvoiceTitle") %>' == 'N') {
			$("#invoiceTitleDiv").hide();
			$("#invoiceTitleDiv").prev().hide();
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
			var validate1 = new Validate({
				validateItem:[{
					type:'text',
					validateObj:'tele',
					validateType:['canNull','mobile'],
					param:['F','F'],
					error:['不能为空','格式不正确']
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
	wx.config({
	    debug: false,
	    appId: '${appId}',
	    timestamp: '${signMap.timestamp}',
	    nonceStr: '${signMap.nonceStr}',
	    signature: '${signMap.signature}',
	    jsApiList: ['scanQRCode']
	});
	
	//等待POS返回订单数据，支付金额不为NULL时，认为POS已返回数据
	function getOrderState() {
		$.ajax({
			url:"<c:url value='/wxPay/getOrderState.do'/>?id=${net_Orders.id}&resv=${net_Orders.resv}",
			type:"POST",
			dataType:"json",
			success:function(data){
				if(data.PAYMONEY > 0){//如果金额大于0 ，支付
					//微信支付
					if($("#paymentType").val()=="1"){
						var para={};
						para["discAmt"] = $("#xjqMoney").val()*100;
						para["amt"] = Number(data.PAYMONEY).toFixed(2)*100;
						para["appid"] = "${appId}";
						para["orderid"] = "${net_Orders.id}";
						para["outTradeNo"] = data.OUTTRADENO;
						para["code"] = "${code}";
						para["visopeninvoice"]=($("#visopeninvoice").attr("data-cacheval")=="false"?"Y":"N");//是否开发票
						para["vinvoicetitle"]=$("#vinvoicetitle").val();//发票抬头
						para["openid"] = "${openid}";
						para["firmName"] = "${firmName}";
						para["pk_firm"] = "${pk_firm}";
						$.ajaxSetup({async:false});
						window.setTimeout(function(){
							$.post("<c:url value='/wxPay/enterPay.do?' />",para,function(data){
								var obj = eval('(' + data + ')');
								if($.trim(obj.errMsg)==""){
									if(parseInt(obj.agent)<5){
										alertMsg("您的微信版本低于5.0无法使用微信支付");
										return;
									}
									WeixinJSBridge.invoke('getBrandWCPayRequest',{
							  			"appId" :"${appId}", //公众号id，由商户传入
							  			"timeStamp" : obj.timeStamp, //时间戳
							  			"nonceStr" : obj.nonceStr, //随机串
							  			"package" : obj.package,//扩展包
										"signType" : obj.signType, //微信签名方式:1.sha1
										"paySign" : obj.paySign //微信签名
							  		},function(res){
							  			WeixinJSBridge.log(res.err_msg);  
							      		if(res.err_msg == "get_brand_wcpay_request:ok"){
							          		alertMsg("微信支付成功");
							          		//调用微信订单查询接口，查询是否支付成功，支付成功后刷新界面
											var order={};
											order["appid"] = "${appId}";
											order["billno"] = $("#billNo").val();//订单号
											order["resv"] = '${net_Orders.resv}';//账单号
											order["poserial"] = "${net_Orders.id}";
											order["outTradeNo"] = data.OUTTRADENO;
											order["cardAmt"] = $("#finalMoney").val();//最终支付金额
											order["discAmt"] = $("#xjqMoney").val();//优惠金额
											order["billAmt"] = "${net_Orders.sumprice}";//账单金额
											order["cardNo"] = $("#cardNo").val();//会员卡号
											order["voucherCode"] = $("#voucherCode").val();//电子券号
											order["discpaycode"] = $("#paycode").val();//优惠对应的支付方式编码
											order["discpayname"] = $("#payname").val();//优惠对应的支付方式名称
											order["billstate"] = "${billstate}";//订单的状态
											order["openid"] = "${openid}";//20150507 song add
											order["isfeast"] = "${net_Orders.isfeast}";//20150710 song add
											order["visopeninvoice"]=($("#visopeninvoice").attr("data-cacheval")=="false"?"Y":"N");//是否开发票
											order["vinvoicetitle"]=$("#vinvoicetitle").val();//发票抬头
											order["firmid"] = "${firmId}";//门店编码
											order["pk_firm"] = "${pk_firm}";//门店主键
											order["vtransactionid"] = "${net_Orders.id}";//财付通流水号暂存账单主键，后台会自动处理
											
											$.post("<c:url value='/wxPay/insertPayInfoForTenpay.do?' />",order,function(data){
												if(data=="1"){
													wx.closeWindow();
												}else{
													alertMsg("账单支付信息保存失败，返回错误信息："+data);
												}
											});
							          		return "1";
									    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
									        alertMsg("支付取消");  
											closeLayer();
								          	return "2";
									    }else{  
									        alertMsg("支付失败,失败原因：err_code="+res.err_code+" err_desc="+res.err_desc+" err_msg="+res.err_msg);  
											closeLayer();
								          	return "3";
									    }
								  		// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
								  		//因此微信团队建议，当收到ok返回时，向商户后台询问是否收到交易成功的通知，若收到通知，前端展示交易成功的界面；若此时未收到通知，商户后台主动调用查询订单接口，查询订单的当前状态，并反馈给前端展示相应的界面。
							  		}); 
								}else{
									alertMsg("获取预支付信息出错，支付失败。错误原因："+obj.errMsg);
									closeLayer();
									return;
								}
							});
						},200);
					}else{//会员卡支付
						if(!$("#cardNo").val()){
							alertMsg("请先注册会员。");
							closeLayer();
							return;
						}
						var bill = {};
						bill["billno"] = $("#billNo").val();//订单号
						bill["pax"] = $("#pax").val();//人数
						bill["resv"] = '${net_Orders.resv}';//账单号
						bill["cardAmt"] = data.PAYMONEY;//卡结金额
						bill["discAmt"] = $("#xjqMoney").val();//优惠金额
						bill["cardNo"] = $("#cardNo").val();//会员卡号
						bill["billAmt"] = "${net_Orders.sumprice}";//账单金额
						bill["pk_group"] = "${pk_group}";//
						bill["firmid"] = "${firmId}";//门店编码
						bill["pk_firm"] = "${pk_firm}";//门店主键
						bill["poserial"] = "${net_Orders.id}";//流水号、反结算/取消支付时需要
						bill["voucherCode"] = $("#voucherCode").val();//电子券号
						bill["discpaycode"] = $("#paycode").val();//优惠对应的支付方式编码
						bill["discpayname"] = $("#payname").val();//优惠对应的支付方式名称
						bill["billstate"] = "${billstate}";//订单的状态
						bill["openid"] = "${openid}";//20150507 song add
						bill["isfeast"] = "${net_Orders.isfeast}";//20150706 song add
						bill["visopeninvoice"]=($("#visopeninvoice").attr("data-cacheval")=="false"?"Y":"N");//是否开发票
						bill["vinvoicetitle"]=$("#vinvoicetitle").val();//发票抬头
						bill["passwd"]="${passwd}";//会员密码
						$.ajaxSetup({async:false});
						window.setTimeout(function(){
							$.post("<c:url value='/wxPay/payWithCard.do?' />",bill,function(data){
								switch(data){
									case "1" : 
										alertMsg("支付成功。");
											wx.closeWindow();
										break;
									case "-5" : alertMsg("卡余额不足，支付失败。"); break;
									case "-6" : alertMsg("密码错误，支付失败。"); break;
									case "-3" : alertMsg("没有查询到卡号为【"+$("#cardNo").val()+"】的会员卡信息。"); break;
									case "-4" : alertMsg("已经支付，不能重复支付。"); break;
									case "-99" : alertMsg("电子券消费失败，撤销支付成功。"); break;
									case "-100" : alertMsg("更新账单状态失败，撤销支付成功。"); break;
									case "-101" : alertMsg("没有找到会员支付方式，支付失败。"); break;
									default : alertMsg("返回码："+ data + "，会员卡支付发生异常。");break;
								}
								closeLayer();
							});
						},200);
					}
				}else if(data.PAYMONEY == 0){//如果金额等于0 直接更新订单状态，并通知用户订单完成
					$.ajax({
						url:"<c:url value='/wxPay/updateOrdr.do'/>?id=${net_Orders.id}&resv=${net_Orders.resv}&openid=${net_Orders.openid}&state=6",
						type:"POST",
						dataType:"json",
						success:function(data){
							if(data=="ok"){
								alertMsg("账单支付成功。");
								window.setTimeout(function(){
									wx.closeWindow();
								},2000);
								
							}else{
								alertMsg("账单更新状态失败。");
							}
						}
					});
				}else{
					getOrderState();
				}
			},
			error:function(data){
				alertMsg("出现错误，请重试。");
			}
		});
	}
	function enterPay_before(){
		var isMember="${isMember}";
			if(isMember == 'N'){
			// 提交前验证
			var validate = new Validate({
				validateItem:[{
					type:'text',
					validateObj:'tele',
					validateType:['canNull','mobile'],
					param:['F','F'],
					error:['不能为空','格式不正确']
				},{
					type:'text',
					validateObj:'valicode',
					validateType:['canNull'],
					param:['F'],
					error:['不能空']
				}]
			});
			if(validate._submitValidate()){
				var rannum=$("#rannum").val();
				var valicode=$("#valicode").val();
				if(valicode==rannum){
					
					var card={};
					card["tele"]=$("#tele").val();
					card["openid"]="${openid}";
					card["firmId"]="${firmId}";
					$.post("<%=path %>/myCard/registerMember.do",card,function(data){
						
					});
					enterPay();
								
				}else{
					alertMsg("验证码不正确");
					$("#valicode").val("");
					
				}
				
			}
			
		}else{
			enterPay();
		}
	}
	//确认付款
	function enterPay(){
		if(Number($("#finalMoney").val()) < 0 ) {
			alertMsg("订单金额错误，请重新选择！");
		}
		
		InitLayer();
		if($("#visopeninvoice").attr("data-cacheval")=="false" && $("#vinvoicetitle").val()==""){
			alertMsg("请填写发票抬头！");
			closeLayer();
			return;
		}
		var mustPayBeforeOrder = '<%=Commons.getConfig().getProperty("mustPayBeforeOrder") %>';
		if(mustPayBeforeOrder != "Y"){//不需要先支付时，走我要结账流程，同步订单数据防止pos加减菜品导致账单数据不一致
				/**支付前重新获取订单数据，防止点击我要结帐后POS加菜*/
				//首先设置支付金额为NULL，
				$.ajax({
					url:"<c:url value='/wxPay/resetPayMoney.do'/>?orderid=${net_Orders.id}",
					type:"POST",
					dataType:"json",
					success:function(res){
						//推送我要结帐请求
						window.setTimeout(function(){
							var order={};
							order["id"] = "${net_Orders.id}";
							order["vcode"] = "${firmId}";
							order["resv"] = "${net_Orders.resv}";
							order["dat"] = "${net_Orders.dat}";
							$.post("<c:url value='/wxPay/sendPayRequestToPOS.do?' />", order, function(data){
								//等待POS返回数据
								getOrderState();
							});
						},200);
					},
					error:function(ajax){
						
					}
				});
			}else{
				//微信支付
				if($("#paymentType").val()=="1"){
					if(Number($("#finalMoney").val()) <= 0 ) {
						alertMsg("请选择其他支付方式！");
						closeLayer();
						return;
					}
					var para={};
					para["discAmt"] = $("#xjqMoney").val()*100;
					para["amt"] = Number($("#finalMoney").val()).toFixed(2)*100;
					para["appid"] = "${appId}";
					para["orderid"] = "${net_Orders.id}";
					para["outTradeNo"] = "${net_Orders.outTradeNo}";
					para["code"] = "${code}";
					para["visopeninvoice"]=($("#visopeninvoice").attr("data-cacheval")=="false"?"Y":"N");//是否开发票
					para["vinvoicetitle"]=$("#vinvoicetitle").val();//发票抬头
					para["openid"] = "${openid}";
					para["firmName"] = "${firmName}";
					// 优惠券信息
					para["actmCode"] = $("#vactmcode").val();
					para["voucherCode"] = $("#voucherCode").val();
					para["pk_firm"] = "${pk_firm}";
					$.ajaxSetup({async:false});
					window.setTimeout(function(){
						$.post("<c:url value='/wxPay/xunlian_enterPay.do?' />",para,function(redirect_uri){	
							//alert(redirect_uri);
							if(redirect_uri==""){
								alertMsg("支付失败，请重新支付");
								return;
							}			
							//var encodeuri = encodeURIComponent(redirect_uri);//如果加上此行，迅联支付接口将调用失败
							//alert(encodeuri);
							var payUrl = "http://sandbox.showmoney.cn/scanpay/unified?"+redirect_uri;
								//alert(payUrl);
								//测试http服务地址http://sandbox.showmoney.cn/scanpay/unified；正常http服务地址https://showmoney.cn/scanpay/unified
						window.location.href=payUrl;	
						
						});
					},200);
				}else{//会员卡支付
					if(!$("#cardNo").val()){
						alertMsg("请先注册会员。");
						closeLayer();
						return;
					}
					var bill = {};
					bill["billno"] = $("#billNo").val();//订单号
					bill["pax"] = $("#pax").val();//人数
					bill["resv"] = '${net_Orders.resv}';//账单号
					bill["cardAmt"] = Number($("#finalMoney").val()).toFixed(2);//卡结金额
					bill["discAmt"] = $("#xjqMoney").val();//优惠金额
					bill["cardNo"] = $("#cardNo").val();//会员卡号
					bill["billAmt"] = "${net_Orders.sumprice}";//账单金额
					bill["pk_group"] = "${pk_group}";//
					bill["firmid"] = "${firmId}";//门店编码
					bill["pk_firm"] = "${pk_firm}";//门店主键
					bill["poserial"] = "${net_Orders.id}";//流水号、反结算/取消支付时需要
					bill["voucherCode"] = $("#voucherCode").val();//电子券号
					bill["discpaycode"] = $("#paycode").val();//优惠对应的支付方式编码
					bill["discpayname"] = $("#payname").val();//优惠对应的支付方式名称
					bill["billstate"] = "${billstate}";//订单的状态
					bill["openid"] = "${openid}";//20150507 song add
					bill["isfeast"] = "${net_Orders.isfeast}";//20150706 song add
					bill["visopeninvoice"]=($("#visopeninvoice").attr("data-cacheval")=="false"?"Y":"N");//是否开发票
					bill["vinvoicetitle"]=$("#vinvoicetitle").val();//发票抬头
					bill["passwd"]="${passwd}";//会员密码
					
					// 优惠券信息
					bill["actmCode"] = $("#vactmcode").val();
					bill["orderid"] = "${net_Orders.id}";
					
					$.ajaxSetup({async:false});
					window.setTimeout(function(){
						$.post("<c:url value='/wxPay/payWithCard.do?' />",bill,function(data){
							switch(data){
								case "1" : 
									alertMsg("支付成功。");
										wx.closeWindow();
									break;
								case "-5" : alertMsg("卡余额不足，支付失败。"); break;
								case "-6" : alertMsg("密码错误，支付失败。"); break;
								case "-3" : alertMsg("没有查询到卡号为【"+$("#cardNo").val()+"】的会员卡信息。"); break;
								case "-4" : alertMsg("已经支付，不能重复支付。"); break;
								case "-99" : alertMsg("电子券消费失败，撤销支付成功。"); break;
								case "-100" : alertMsg("更新账单状态失败，撤销支付成功。"); break;
								case "-101" : alertMsg("没有找到会员支付方式，支付失败。"); break;
								default : alertMsg("返回码："+ data + "，会员卡支付发生异常。");break;
							}
							closeLayer();
						});
					},200);
				}
			}
	}
	
	//切换支付方式选择状态
	function changePayType(obj, payType) {
		$("#wxPay").hide();
		$("#afterPay").hide();
		$("#paymentType").val(payType);
		$(obj).find("img").show();
	}
	//选择电子券操作
	function addCoupon(id,money,paycode,payname){
		$("#selectCouponDiv").popup("close");
		
		if(cnt>1){
			alertMsg("只能使用一张电子券。");
			return;
		}
		
		$("[name='selectCoupon']").each(function(){
			if($(this).is(":visible")) {
				id = $(this).parent().attr("id");
				money = $(this).parent().attr("money");
				actmCode = $(this).parent().attr("actmCode");
				
				$("#vactmcode").val(actmCode);
				$("#voucherCode").val(id);
			}
		});
		
		//$("#xjqMoney").val(Number($("#xjqMoney").val())+Number(money));
		$("#xjqMoney").val(Number(money));
		//$("#"+id).hide();
		$("#xjqMoneySpan").html(Number($.trim($("#xjqMoney").val())).toFixed(2));
		$("#finalMoney").val(Number(Number($("#finalMoney").val())-Number($("#xjqMoney").val())).toFixed(2));
		var finalMoney = Number(Number($("#initMoney").val())-Number($("#xjqMoney").val())).toFixed(2);
		/* if(finalMoney < 0) {
			finalMoney = 0.00;
		} */
		$("#finalMoney").val(finalMoney);
		$("#totalMoney").html(Number($("#finalMoney").val()).toFixed(2));
		$(".xjDiv").css("display","block");
		$("#voucherCode").val(id);
		$("#paycode").val(paycode);
		$("#payname").val(payname);
		$("#selectCouponHead").text("您已经选择" + cnt + "张电子券");
		//记录已经用的电子券id
	}
	//点击取消电子券按钮
	function imgClick(){
		$(".xjDiv").css("display","none");
		$(".cashCoupDiv").css("display","block");
		$("#finalMoney").val(Number(Number($("#finalMoney").val())+Number($("#xjqMoney").val())).toFixed(2));
		//$("#finalMoney").val(Number($("#initMoney").val()).toFixed(2));
		$("#totalMoney").html(Number($("#finalMoney").val()).toFixed(2));
		$("#xjqMoney").val("0.00");
		$("#voucherCode").val("");
		cnt = 0;
		$("#selectCouponHead").text("您已经选择" + cnt + "张电子券");
		
		//取消所选电子券
		$("[name='selectCoupon']").each(function(){
			if($(this).is(":visible")) {
				$(this).hide();
			}
		});
	}
	//修改发票抬头输入框的编辑状态
	function changInput(){
		if($("#visopeninvoice").attr("data-cacheval")=="false"){
			$("#vinvoicetitle").removeAttr("readonly");
			$(".ui-input-text").css("border-color","black");
		}else{
			$("#vinvoicetitle").attr("readonly","readonly");
			$(".ui-input-text").css("border-color","#F7F7F7");
		}			
	}
	
	//选择电子券页面
	function toSelectCouponPage() {
		$("#selectCouponDiv").popup("open");
	}

	// 不使用电子券
	function cancelSelect() {
		$("[name='selectCoupon']").each(function(){
			if($(this).is(":visible")) {
				$(this).hide();
			}
		});
		cnt = 0;
		
		couponStr = "${orderDetail.id}:";
		$("#selectCouponDiv").popup("close");
		$("#selectCouponHead").text("您已经选择0张电子券");
	}
	
	// 反选电子券
	function selectCoupon(obj) {
		var imgObj = $(obj).children().eq(2);
		if(imgObj.is(":visible")) {
			cnt = cnt - 1;
			imgObj.hide();
		} else {
			if(cnt>=1){
				alertMsg("只能使用一张电子券。");
				return;
			}
			cnt = cnt + 1;
			imgObj.show();
		}
	}
</script>
<style type="text/css">
	
	.td_left{
		width:30%;
		text-align: left;
		padding-left:5%;
	}
	.pay-left{
		width:50%;
		text-align:left;
		padding-left:20px;
		vertical-align:middle;
	}
	
	.pay-right{
		width:40%;
		text-align:right;
		padding-right:5%;
		vertical-align:middle;
	}
	.pay-right img{
		width:50px;
		height:50px;
	}
	.csahcoupon{
		color: #B7B7B7;
		font-size: 80%;
	}
	.cashCoupDiv{
		background-color: white;
		border: 1px  solid;
		width: 90%;
		height:80px;
		vertical-align:middle;
		margin: 5px 5%;
	}
	 h1 {
	    width: 1em;
	    margin-left: 15%;
	    font-size: 13px;
	  }
	  .ui-checkbox{
	  	float: left;
	  }
	  .div1{
	  	  float: left;
		  margin-left: 40px;
		  margin-top: -4px;
	  }
	  .ui-input-text{
	  	float: left;
  		margin-top: -6px;
  		border-color:#F7F7F7;
	  }
	.numClass{
		font-family: Arial;
		font-size: 200%;
		color: red;
	}
	.popTd{
		text-align:center;
	}
	.popTd input{
		padding:10px;
		width:45%;
		background-color:#F7F7F7;
		border:0;
		border-radius:5px;
		color:#356893;
		text-align:center;
	}
.buttonDiv{
	background-color: #DCDCDC;
	border: 1px solid #EEEEEE;
	border-radius: 4px;
 	height: 30px; 
	font-family:微软雅黑;
	width: 36%;
	float: left;
	line-height: 30px;
	font-size: 10px;
	margin-left: 3%;
  	text-align: center;
  margin-top: 5px;
}
.inputContent{
	background-color: #FFFFFF;
	border:1px solid #EEEEEE;
	border-radius:4px;
 	height:30px; 
	width:90%;
}
</style>
</head>
<body>
<div data-role="page" data-theme="d"><!--页面层容器-->
	<input type="hidden" id="paymentType" name="paymentType" value="1" /><!-- 保存选择的支付方式 -->
	<input type="hidden" id="billNo" name="billNo" value="${net_Orders.resv }" /><!-- 账单号 -->
	<input type="hidden" id="pax" name="pax" value="${net_Orders.pax }" /><!-- 账单人数 -->
	<input type="hidden" id="cardNo" name="cardNo" value="${cardNo }" /><!-- 会员卡号-->
	<input type="hidden" id="firmId" name="firmId" value="${firmId }" /><!-- 门店编码 -->
	<input type="hidden" id="voucherCode" name="voucherCode" value="" /><!-- 电子券号 -->
	<input type="hidden" id="paycode" name="paycode" value="" /><!-- 支付方式编码 -->
	<input type="hidden" id="payname" name="payname" value="" /><!-- 支付方式名称 -->
	<input type="hidden" id="vactmcode" name="vactmcode" value="" /><!-- 支付方式名称 -->
	<div data-role="content" style="margin-top:0;list-style:none;padding:0;"><!-- 页面主体 -->
		<div class="firm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="detail" height="height: 90px;">
					<td class="td_left">
							<div class="nonsupport" style="margin-top: 14px;">应付金额：</div>
					</td>
					<td class="pay-right">
						<div class="numClass" style="margin-top:15px;">
							<input type="hidden" id="initMoney" value="${net_Orders.sumprice }">
							<div style=" float: right;">
							<div style="float:left;margin-top:5px;font-size: 23px; ">￥</div>
							<fmt:formatNumber value="${net_Orders.sumprice }" pattern="0.00"/></div>
						</div>
					</td>
				</tr>
				<tr class="detail" id="xjqTr">
					<td class="td_left">
						<p>
							<div class="xjDiv">
								<span class="nonsupport csahcoupon">现金券：</span>
							</div>
						</p>
					</td>
					<td class="pay-right csahcoupon">
						<p>
							<div class="xjDiv">
								<input type="hidden" id="xjqMoney" value="0" />
								-<span id="xjqMoneySpan">0.00</span>元<img id="xjqImg"  style="float: right; margin-top: -10px;width: 35px;height: 35px;" src="<c:url value='/image/wechat/jian_d.png'/>" onclick="imgClick()"/>
							</div>
						</p>
					</td>
				</tr>
				<tr class="detail" id="selectCouponBtnTr" onclick="javaScript:toSelectCouponPage()" style="height:35px;">
					<td class="td_left" style="width:60%; border-bottom:1px solid #EEEEEE;">
						<span style="font-size:80%;">选择电子券&nbsp;</span>
						<span id="selectCouponHead" style="color:red; font-size:70%;">您已经选择0张电子券</span>
					</td>
					<td class="pay-right" style="border-bottom:1px solid #EEEEEE;">
						<img style="width:20px; height:20px" src="<%=path %>/image/wechat/arrow_right.png">
					</td>
				</tr>
				<tr class="detail">
					<td class="td_left" style="border-top:1px dashed gray">
						<p>
							<span class="nonsupport">合计</span><br />
						</p>
					</td>
					<td class="pay-right" style="border-top:1px dashed gray">
						<span id="totalMoney" class="numClass">
							<fmt:formatNumber value="${net_Orders.sumprice }" pattern="0.00"/>
						</span>元
						<input type="hidden" id="finalMoney" value="<fmt:formatNumber value="${net_Orders.sumprice }" pattern="0.00"/>" />
					</td>
				</tr>
			</table>
		</div>
		<div class="spaceMeaage" style="padding: 0px;">
		</div>
		<div class="firm" style="padding-bottom:  2px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr onclick="changePayType(this, '1')" class="detail">
					<td class="td_left">
						<p>
							<img alt="" src="<%=path %>/image/wechat/wxpay1.png" style="width: 40px;height: 40px;">
							<div class="nonsupport" style="margin-top: -51px;margin-left: 46px;font-weight: bolder;">微信支付</div>
						</p>
					</td>
					<td class="pay-right">
						<img id="wxPay" src="<c:url value='/image/wechat/right.png'/>"/>
					</td>
				</tr>
			</table>
		</div>
		<c:if test="${isMember eq 'N' }">
			<div class="firm" style="padding-top: 0px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr >
						<td>
							<input type="tel"  class="inputContent" name="tele" id="tele" style="width: %;border-color: #FFF;" placeholder="请输入电话">
						</td>					
					</tr>
					<tr >
						<td>
							<input type="text"  class="inputContent" name="valicode" id="valicode" value="" style="width: %;border-color: #FFF;" placeholder="请输入验证码">	
							<div data-role="none" id="sendSms" class="buttonDiv" style="background-color:#d83322; color:#FFFFFF" >点击发送验证码</div>
							<input type="hidden" data-role="none" name="openid" id="openid" value="${openid}" />
							<input type="hidden" id="rannum" value="1111">					
						</td>					
					</tr>
				</table>
			</div>
		</c:if>
		<c:if test="${canUseCardPay eq 'Y' }">
			<div class="firm" style="padding-top: 0px;display:none">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr onclick="changePayType(this, '2')" class="detail">
						<td class="td_left">
							<p>
								<img alt="" src="<%=path %>/image/wechat/cardImg.png" style="width: 40px;height: 40px;">
								<div class="nonsupport" style="margin-top: -51px;margin-left: 46px;font-weight: bolder;">会员支付</div>
							</p>
						</td>
						<td class="pay-right" >
							<img id="afterPay" style="display:none;" src="<c:url value='/image/wechat/right.png'/>"/>
						</td>
					</tr>
				</table>
			</div>
		</c:if>
		<div class="spaceMeaage" style="padding: 0px;">
		</div>
		<div class="firm" id="invoiceTitleDiv" style="display:none">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr class="detail">
					<td class="td_left" style="width: 20px;" colspan="2">
						<p style="height: 6px;margin-left: -5px;" id="div11">
							<input type="checkbox" id="visopeninvoice" name="visopeninvoice" value="Y" onclick="changInput()" />
						</p>
					</td>
				</tr>
				<tr class="detail">
					<td class="td_left"  colspan="2">
						<p>
							<div class="nonsupport" style="margin-top: 10px;float: left;font-size:100%;">发票抬头：</div>
							<input type="text" id="vinvoicetitle" name="vinvoicetitle" value="${vinvoicetitle}" readonly="readonly"/>
						</p>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="bottDiv" data-role="footer" data-position="fixed"  data-tap-toggle="false"  style="min-height: 48px;position:fixed;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center"   height="100%">
			<tr>
				<td id="ui-a" style="width:75%;">
					<a href="${pageFrom }" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
				<td style="background-color: #d83322;color: white;width:25%;text-align: center;" id="enterWcPay" onclick="enterPay_before()">
					确认支付
				</td>
			</tr>
		</table>
  	</div>

		<!-- 选择电子券页面 -->
	  	<div data-role="popup" id="selectCouponDiv" style="position:fixed; top:0px; left:0px; width:100%; height:100%;" 
	  		data-dismissible="false" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false">
			<div style="text-shadow:none; width:100%; background-color:#d83322; color:#FFFFFF; text-align:center; padding-bottom:10px; padding-top:10px; font-size:80%;">
				<span>您有以下电子券可以使用</span>
			</div>
			<div style="position:fixed; bottom:30px; width:100%">
				<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr>
						<td class="popTd">
							<input type="button" style="background-color:#d83322; color:#FFFFFF" value="不使用电子券" 
								data-role='none' data-ajax="false" onclick="javaScript:cancelSelect();" /> 
							<input type="button" style="background-color:#d83322; color:#FFFFFF" value="确认选择" 
								data-role='none' data-ajax="false" onclick="addCoupon('${coupon.code}','${coupon.amt}','','')" /> 
						</td>
					</tr>
				</table>
			</div>
			<div id="listCouponDiv" style="height:80%; overflow-y:scroll;">
				<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="overflow:hidden;position:relative;">
					<c:set var="listCouponSize" value="${fn:length(listCoupon)}"/>
					<c:forEach items="${listCoupon }" var="coupon" varStatus="status">
						<tr height="85px">
							<td>
								<div id="${coupon.code}" class="cashCoupDiv" onclick="selectCoupon(this);" pushval="${coupon.typId}_${coupon.amt}_${coupon.actmCode}_${coupon.code}"
								 	actmCode="${coupon.actmCode}" money="${coupon.amt}" style="background:url(<%=path%>/image/wechat/${coupon.pic })  no-repeat right; background-size: 100% 100%;">
									<div style="float: left;width: 25%;color: ${coupon.fontColor};"><h1>立减${coupon.amt}元</h1></div>
									<div style="float: left; margin-top: 10px;color: white;">
										<span style="font-size: 130%;">${coupon.typdes}</span><br>
										<span style="font-size: 90%;">有效期至：${coupon.edate }</span>
									</div>
									<div name="selectCoupon" style="padding-top:18px; display:none" class="selectImgDiv">
										<img id="wxPay" style="width:50px; height:50px; text-align:right; vertical-align:middle;" src="<c:url value='/image/wechat/right.png'/>"/>
									</div>
								</div>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
</div><!-- page -->
<script type="text/javascript">
// 	$("#xjqTr").css("display","none");
	//手动添加开发票提示，防止布局错乱
	var div1 = "<div class=\"div1\"><span class=\"nonsupport\">开发票</span></div>";
	$("#div11").append(div1);
</script>
</body>
</html>