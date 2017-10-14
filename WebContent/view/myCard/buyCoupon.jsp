<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%String path=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>购买电子券</title>
<link type="text/css" href='<c:url value="/css/wechat/jquery.mobile-1.4.5.min.css" />' rel="stylesheet" />
<link rel="stylesheet" href="<%=path %>/css/default/global.css" />
<script type="text/javascript" src="<c:url value='/js/jquery-1.11.0.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/wechat/jquery.mobile-1.4.5.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/layer.js'/>"></script>
<%@include file="/view/dining/jAlerts.jsp"%>
<style type="text/css">
	.tabContent{
		background-color:#EEEEEE;
		text-align:center;
		height:100%;
	}
	.tabContent table tr{
		height:100%;
	}
	.selectType{
		border-bottom:solid 2px #FFB400;
	}
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
		background-color:white;
		width:94%;
		height:120px;
		vertical-align:middle;
		margin:10px 3% 0 3%;
	}
	 h1 {
	    width: 1em;
	    margin-left: 15%;
	    font-size: 13px;
	  }
	.lmoney{
		font-size:150%;
		color:#FFFFFF;
	}
	.lunit{
		font-size:100%;
		color:#FFFFFF;
	}
	.ldiv{
		float:left;
		width:30%;
		margin-top:30px;
		margin-left:10px;
		color:#FFFFFF;
	}
	.rdiv{
		float:left; 
		margin-top:10px; 
		color:#FFFFFF;
	}
	.minus{
		float:right;
		width:45px;
		height:100%;
		text-align:center;
		padding:0px;
	}
	.number{
		float:right;
		width:10px;
		height:45px;
		text-align:center;
		font-size:12px;
		line-height:30px;
		color:#FFFFFF;
	}
	.plus{
		float:right;
		width:45px;
		height:100%;
		text-align:center;
		padding:0px;
	}
	.bottDiv{
		width:100%;
		border:0px;
		margin:0px;
		padding:0px;
		margin:0px;
		height:8%;
	}
	.bottDiv table tr{
		height:100%;
	}
	.ui-a{
		border: none;
		text-align: left;
		width:10%;
		padding-left:10px;
	}
	.ui-a img{
		border:0;
		padding:0;
		height:20px;
		width:20px;
		vertical-align:middle;
	}
	.ui-b{
		width:20%;
		padding-right:10px;
	} 
	.ui-c{
		border: none;
		font-weight: bold;
		width:45%;
		height:100%;
		text-align: center;
	}
	.ui-c span{
		font-size:26px; 
		color:#FFFFFF;
	}
	.ui-d{
		border: none;
		font-weight: bold;
		width:25%;
		height:100%;
		text-align: center;
		background-color:#ffb400;
	}
	.ui-d span{
		font-size:110%; 
		color:#FFFFFF;
	}
	.cartDiv{
		width:60px; 
		float:right; 
		height:100%; 
		padding:0 0 0 2%; 
		background:url(../image/wechat/shoppingCart.png) no-repeat right; 
		background-size:38px 38px;
	}
	.cartNumDiv{
		width:20px; 
		height:20px; 
		text-align:center; 
		display:inline-block; 
		background:url(../image/wechat/dishNum.png); 
		background-size:100% 100%; 
		color:white; 
		clear:both; 
		float:right; 
		margin-right:-2px;
	}
	.cai22{
		width:100%;
		height:auto;
		text-align:left;
		/* color:#ff4747; */
		font-size:10px;
	}
	.minus1{
		float:right;
		width:30%;
		height:100%;
		text-align:center;
		padding:0px;
	}
	.number1{
		float:right;
		width:34px;
		height:36px;
		text-align:center;
		color:#7a7a7a;
		line-height:45px;
	}
	.plus1{
		float:right;
		width:30%;
		height:100%;
		text-align:center;
		padding:0px;
	}
	.detailSpan{
		font-size:75%; 
		color:#FFFFFF; 
		margin-left:5px;
	}
.tdTitle{
	background-color:#E5E5E5;
	font-weight:bold;
	text-align:left;
	width:100%;
	height:40px;
	border:0;
	margin:0;
}
.tdContent{
	width:100%;
	border:0;
	margin:0;
	padding:0;
	overflow:hidden;
}
.space{
	width:100%;
	border:0;
	margin:0;
	height: 10px;
}
.father_div{
	overflow:hidden;
}
.td_left{
	width:30%;
	text-align: left;
	padding-left:5%;
}
.td_right{
	width:70%;
	/*padding-right:20px;*/
}
.space-left{
	float:left;
	width:5%;
}
.store_right{
	float:left;
	width:95%;
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
.payTypeName{
	font-size:110%;
}
.payTypeInfo{
	font-size:75%;
	color:#656565;
}
.orderMoney{
	text-align: right;
	padding-right:20px;
	color:#FF0000;
	font-size:120%; 
}
</style>
<script language="JavaScript" type="text/JavaScript">
	/*去除安卓返回及刷新按钮*/
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
	
	var orderId = "${orderId}";
	
	$(function(){
		/* var ua = navigator.userAgent.toLowerCase();
	 	if(ua.match(/MicroMessenger/i)!="micromessenger") {
	 		$("body").text("请使用微信浏览器打开");
	 		return;
		} */
		
		// 点击选好了
		$("#mymenudiv-right-b").click(function(){
			if(parseInt($("#menu-count1").text()) == 0){//如果没点菜，则提示没有点菜
				$("#pop").text("对不起，您还没有选择电子券。");
				$("#dhk").click();
			}else{//跳转到我的菜单
				$("#dhk2").click();
			}
		});
		
		/*******************************确认提交订单********************************************************/
		$("#commitBtnDiv").on("tap",function(){
			if(parseInt($("#menu-count1").text()) == 0){//如果没点菜，则提示没有点菜
				$("#pop").text("对不起，您还没有选择电子券。");
				$("#dhk").click();
			} else {
				// 判断积分是否不足
				if(parseInt($("#menu-point2").text()) > parseInt('${cardPoint}')) {
					alertMsg("积分不足。您目前拥有积分：${cardPoint}");
					return;
				}
				//保存订单
				if(parseInt($("#menu-price2").text() * 100) > 0) {
					// 金额大于0，调用微信支付
					callpay();
				} else {
					saveOrder("2", "0");
				}
			}
		});
	});
	
	//公众号支付
	
	function callpay(){
		var para={};
		para["amt"] = $("#menu-price2").text() * 100;
		para["appid"] = "${appId}";
		para["orderid"] = orderId;
		para["outTradeNo"] = orderId;
		para["firmName"] = "购买电子券";
		para["cardNo"] = '${cardNo}';//会员卡号
		para["sumPoint"] = $("#menu-point2").text();
		para["openid"] = $("#openid").val();
		para["pk_group"] = '${pk_group}';
		var couponids = "";//电子券id
		var couponnums = "";//电子券数量
		var vnum = 0;
		for(var i=0;i<=$("#mymenu-table").find("tr").length-1;i++){
			var couponnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text();//数量
			var vtype = $("#mymenu-table").find("tr").eq(i).attr("vtype");//1:现金 2:积分
			var vcode = $("#mymenu-table").find("tr").eq(i).attr("couponId");//电子券id
			if(i==$("#mymenu-table").find("tr").length-1){
				couponids += vcode;
				couponnums += couponnum;
			}else{
				couponids += vcode+",";
				couponnums += couponnum +",";
			}
			if("2"==vtype){
				vnum++;
			}
			
		}
		para["couponids"] = couponids;
		para["couponnums"] = couponnums;
		para["vnum"] = vnum;
		$.ajaxSetup({async:false});
		//微信预支付
		$.post("<c:url value='/wxPay/enterPay.do?' />",para,function(data){
			if($.trim(data)){
				var obj = eval('(' + data + ')');
				if(parseInt(obj.agent)<5){
					alertMsg("您的微信版本低于5.0无法使用微信支付");
					return;
				}
				//确定支付
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
						// 保存订单
						//saveOrder("2", "0");
		          		return "1";
				    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
				        alertMsg("用户取消支付");  
				        InitLayer();
						$.ajax({
							url:"<c:url value='/myCard/resetOrderId.do'/>",
							type:"POST",
							dataType:"json",
							data:{},
							success:function(data){
								orderId = data;
								closeLayer();
							},
							error:function(ajax){
								closeLayer();
							}
						});
			          	return "2";
				    }else{  
				    	alertMsg("支付失败,失败原因：err_code="+res.err_code+" err_desc="+res.err_desc+" err_msg="+res.err_msg);
				        InitLayer();
						$.ajax({
							url:"<c:url value='/myCard/resetOrderId.do'/>",
							type:"POST",
							dataType:"json",
							data:{},
							success:function(data){
								orderId = data;
								closeLayer();
							},
							error:function(ajax){
								closeLayer();
							}
						});
			          	return "3";
				    }
		  		// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
		  		//因此微信团队建议，当收到ok返回时，向商户后台询问是否收到交易成功的通知，若收到通知，前端展示交易成功的界面；若此时未收到通知，商户后台主动调用查询订单接口，查询订单的当前状态，并反馈给前端展示相应的界面。
		  		}); 
			}else{
				alertMsg("获取预支付信息出错，支付失败。");
				return;
			}
		});
	}
	
	function saveOrder(state, payType) {
		var order = {};
		order["state"] = state;
		order["paymentType"] = payType;
		order["openid"] = $("#openid").val();
		order["sumPrice"] = $("#menu-price2").text();
		order["sumPoint"] = $("#menu-point2").text();
		order["pk_group"] = '${pk_group}';
		order["custId"] = '${cardId}';
		//遍历   commit-menu
		for(var i=0;i<=$("#mymenu-table").find("tr").length-1;i++){
			var couponid = $("#mymenu-table").find("tr").eq(i).attr("couponId");//电子券id
			var couponname = $("#mymenu-table").find("tr").eq(i).attr("name");//电子券名称
			var couponnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text();//数量
			var price = $("#mymenu-table").find("tr").eq(i).attr("price");//单价
			var vtype = $("#mymenu-table").find("tr").eq(i).attr("vtype");//1:现金 2:积分
			var vcode = $("#mymenu-table").find("tr").eq(i).attr("vcode");//电子券编码
			
			var totalprice = parseInt(couponnum) * parseFloat(price);//单个电子券总额
			order["listNetGoodsOrderDtl["+i+"].goodsid"]=couponid;
			order["listNetGoodsOrderDtl["+i+"].goodsname"]=couponname;
			order["listNetGoodsOrderDtl["+i+"].goodsnum"]=couponnum;
			order["listNetGoodsOrderDtl["+i+"].price"]=price;
			order["listNetGoodsOrderDtl["+i+"].totalprice"]=totalprice;
			order["listNetGoodsOrderDtl["+i+"].payType"]=vtype;
			order["listNetGoodsOrderDtl["+i+"].couponCode"]=vcode;
		}
		InitLayer();
		$.post("<%=path %>/myCard/saveGoodsOrder.do?cardNo=${cardNo}", order, function(data){
			closeLayer();
			if(data == "1") {
				alertMsg("订单提交成功");
				InitLayer();
				window.location.href = "<%=path %>/myCard/cardInfo.do?pk_group=${pk_group}&openid=${openid }";
			} else {
				alertMsg("订单提交失败");
			}
		});
	}
	
	function loadVoucher(obj, divId){
		hideAll();
		$("#" + divId).show();
		
		$(".selectType").attr("class","noselectType");
		$(obj).attr("class","selectType");
	}
	
	function hideAll(){
		$("#byCash").hide();
		$("#byPoint").hide();
	}
	
	/* 减少电子券 */
	function minusClick(obj){
		InitLayer();
		var sl = parseInt($(obj).prev().text());
		sl -= 1;
		$(obj).prev().text(sl);
		
		var couponId = $(obj).attr("a");
		
		if(sl == 0){
			$(obj).hide();
			$(obj).prev().hide();
			$("#list"+couponId).remove();//下个页面table删掉这个菜品
			listMymenu();//遍历我的菜单，为总数量和总价格赋值。
			closeLayer();
			return;
		}
		$("#list"+couponId).children().last().children(".number1").text(sl);//下个页面table中数量变化
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
	}
	
	/* 增加电子券 */
	function plusClick(obj,flag){
		InitLayer();
		//dh1(obj, "shoppingCart"); //动画效果
		$(obj).nextAll().show();
		var sl = parseInt($(obj).next().text());
		sl += 1;
		$(obj).next().text(sl);
		
		var couponId = $(obj).attr("a");
		var pdes = $(obj).attr("b");
		var price = $(obj).attr("c");
		var vtype = $(obj).attr("d");
		var vcode = $(obj).attr("e");
		if($("#list" + couponId).length > 0 ){//如果存在这个菜品了
			$("#list" + couponId).children().last().children(".number1").text(sl);
			listMymenu();//遍历我的菜单，为总数量和总价格赋值。
			closeLayer();
			return;
		}
		
		var unit = "元";
		if(vtype != "1") {
			unit = "积分";
		}
		
		var menudata = "<tr width='100%' height='50px' id='list" + couponId + "' couponId='" + couponId + "' name='" + pdes + "' price='" + price + "' vtype='" + vtype + "' vcode='" + vcode + "'>"
		+"<td style='width:0px;' hidden='hidden'><input type='hidden'/></td>"
		+"<td style='width:30%;'><span style='font-size:16px;'>" + pdes + "</span></td>"
		+"<td style='width:30%;'>"
		+"<div class='cai22'><span style='color:#FFB400; font-size:15px;'>" + price + "</span><span style='color:#FFB400; font-size:13px;'>" + unit + "/张</span></div>"
		+"<div id='div" + couponId + "' style='width:100%;text-align:left;display;none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
		+"</td><td style='width:40%;'><div class='plus1' onclick='plusClick1(this)' a='"+couponId+"' b='"+pdes+"' c='"+price+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/></div>"
		+"<div class='number1' style='display:;'>"+sl+"</div>"
		+"<div class='minus1' style='display:;' onclick='minusClick1(this)' a='"+couponId+"' b='"+pdes+"' c='"+price+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
		+"</td></tr>";
		$("#mymenu-table").append(menudata);
		listMymenu(); //遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
	}
	
	/** 提交订单页面减少电子券 */
	function minusClick1(obj){
		InitLayer();
		var sl = parseInt($(obj).prev().text());
		sl -= 1;
		$(obj).prev().text(sl);
		var couponId = $(obj).attr("a");
		
		$("#num_" + couponId).text(sl);
		
		if(sl == 0){
			$("#num_" + couponId).next().hide();
			$("#num_" + couponId).hide();
			
			//将整条菜品删除
			$("#list" + couponId).remove();
			listMymenu();//遍历我的菜单，为总数量和总价格赋值。
			closeLayer();
			return;
		}
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
	}

	/** 提交订单页面增加电子券 */
	function plusClick1(obj){
		closeLayer();
		$(obj).nextAll().show();
		var sl = parseInt($(obj).next().text());
		sl += 1;
		$(obj).next().text(sl);
		$("#num_" + $(obj).attr("a")).text(sl);
		
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
	}
	
	/*增加效果*/
	function dh1(obj, targetId){
		$("#yddiv").remove();
		var left = $(obj).offset().left;
		var top = $(obj).offset().top;
		var top1 = $("#" + targetId).offset().top;
		var div = "<div id='yddiv' style='background:#FFB400;position:fixed;z-index:99;width:10px;height:10px;top:"+top+"px;left:"+left+"px'></div>";
		$("#menupage").append(div);
		$("#yddiv").animate({top:top1+$("#" + targetId).height()-35, left:"43%"}, 500).animate({opacity:"0"},500);
	}
	
	/** 遍历我的订单，给总数量和总价格赋值 */
	function listMymenu(){
		//遍历   mymenu-table
		var menuCount = 0;
		var menuPrice = 0;
		var menuPoint = 0;
		for(var i=0;i<=$("#mymenu-table").find("tr").length-1;i++){
			var foodnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text(); //数量
			var price = $("#mymenu-table").find("tr").eq(i).attr("price"); //单价
			var vtype = $("#mymenu-table").find("tr").eq(i).attr("vtype"); //类型
			foodnum = parseInt(foodnum);
			var totalprice = parseInt(foodnum) * parseFloat(price); //单个电子券总额
			if(vtype == "1") {
				// 现金购买
				menuPrice += totalprice;
			} else {
				// 积分兑换
				menuPoint += totalprice;
			}
			menuCount += foodnum;
		}
		$("#menu-count2").text(menuCount);//本页面和下个页面底部数量和价格变化
		$("#menu-price2").text(menuPrice);
		$("#menu-point2").text(menuPoint);
		$("#menu-price3").text(menuPrice);
		$("#menu-point3").text(menuPoint);
		
		conPriSyn();
	}
	/****前一个页面同步后一个页面的数量和价格*****/
	function conPriSyn(){
		$("#menu-count1").text($("#menu-count2").text());//本页面和下个页面底部数量和价格变化
		$("#menu-price1").text($("#menu-price2").text());
		$("#menu-point1").text($("#menu-point2").text());
		$("#menu-price3").text($("#menu-price2").text());
		$("#menu-point3").text($("#menu-point2").text());
	}
	
	/**返回电子券选择页面*/
	function returnLastPage(){
		$("#dhk3").click();
	}
	
	/** 展示电子券使用详情 */
	function showDetail(obj) {
		$(obj).parent().next().toggle();
	}
</script>

</head>
<body>
<div data-role="page" id="listCoupon" class="page" data-theme="d"><!--页面层容器-->
	<div style="border:0; height:5%;">
    	<div class="tabContent">
      		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="selectType" onclick="javaScript:loadVoucher(this,'byCash');">现金购买</td>
					<td class="noselectType" onclick="javaScript:loadVoucher(this,'byPoint');">积分兑换</td>
				</tr>
			</table>
    	</div>
  	</div>
  	
	<div style="width:100%; height:87%; overflow:auto; padding-bottom:-1px;"><!--页面主体-->
		<input type="hidden" id="pk_group" value="${pk_group}">
		<input type="hidden" id="openid" value="${openid}">
		<div id="byCash">
			<c:forEach items="${cashCouponList }" var="coupon" varStatus="status">
				<div id="${coupon.pk_id}" class="cashCoupDiv" 
				 	style="background:url(<%=path%>/image/wechat/${coupon.vpic }) no-repeat center; background-size: 100% 100%;">
					<div class="ldiv"><span class="lmoney">${coupon.nmoney}</span><span class="lunit">元</span></div>
					<div class="rdiv">
						<span>${coupon.vname }</span><br>
						<span>有效期：${coupon.validMonth }个月</span><br>
						<c:choose>
							<c:when test="${coupon.vtype ne '1' }">
								<span>价格：${coupon.nprice }积分</span>
							</c:when>
							<c:otherwise>
								<span>价格：${coupon.nprice }元</span>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="plus" style="padding-right:8px;" onclick="plusClick(this,0);" a="${coupon.pk_id}" b="${coupon.vname }" c="${coupon.nprice }" d="${coupon.vtype }" e="${coupon.vcode }">
						<img src="<%=path %>/image/wechat/plus.png" width="45px" height="45px"/>
					</div>
					<div class="number" id="num_${coupon.pk_id }" style="display:none; padding-top:7px;">0</div>
					<div class="minus" style="display:none;" onclick="minusClick(this);" a="${coupon.pk_id}" b="${coupon.vname }" c="${coupon.nprice }" d="${coupon.vtype }">
						<img src="<%=path %>/image/wechat/minus.png" width="45px" height="45px"/>
					</div>
					<div style="clear:left"></div>
					<div style="padding-top:15px; margin-left:10px; color:#FFFFFF;" onclick="showDetail(this)">使用详情</div>
				</div>
				<div style="display:none; background-color:#C9C9C9; width:94%; margin:auto;">
					<span class="detailSpan">${coupon.vmemo }</span>
					<c:if test="${coupon.limitFirm ne null && coupon.limitFirm ne '' }">
						<br><span class="detailSpan">适用门店：${coupon.limitFirm }</span>
					</c:if>
				</div>
			</c:forEach>
		</div>
		<div id="byPoint" style="display:none;">
			<c:forEach items="${pointCouponList }" var="coupon" varStatus="status">
				<div id="${coupon.pk_id}" class="cashCoupDiv" 
				 	style="background:url(<%=path%>/image/wechat/${coupon.vpic }) no-repeat center; background-size: 100% 100%;">
					<div class="ldiv"><span class="lmoney">${coupon.nmoney}</span><span class="lunit">元</span></div>
					<div class="rdiv">
						<span>${coupon.vname }</span><br>
						<span>有效期：${coupon.validMonth }个月</span><br>
						<c:choose>
							<c:when test="${coupon.vtype ne '1' }">
								<span>价格：${coupon.nprice }积分</span>
							</c:when>
							<c:otherwise>
								<span>价格：${coupon.nprice }元</span>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="plus" style="padding-right:8px;" onclick="plusClick(this,0);" a="${coupon.pk_id}" b="${coupon.vname }" c="${coupon.nprice }" d="${coupon.vtype }" e="${coupon.vcode }">
						<img src="<%=path %>/image/wechat/plus.png" width="45px" height="45px"/>
					</div>
					<div class="number" id="num_${coupon.pk_id }" style="display:none; padding-top:7px;">0</div>
					<div class="minus" style="display:none;" onclick="minusClick(this);" a="${coupon.pk_id}" b="${coupon.vname }" c="${coupon.nprice }" d="${coupon.vtype }">
						<img src="<%=path %>/image/wechat/minus.png" width="45px" height="45px"/>
					</div>
					<div style="clear:left"></div>
					<div style="padding-top:15px; margin-left:10px; color:#FFFFFF;" onclick="showDetail(this)">使用详情</div>
				</div>
				<div style="display:none; background-color:#C9C9C9; width:94%; margin:auto;">
					<span class="detailSpan">${coupon.vmemo }</span>
					<c:if test="${coupon.limitFirm ne null && coupon.limitFirm ne '' }">
						<br><span class="detailSpan">适用门店：${coupon.limitFirm }</span>
					</c:if>
				</div>
			</c:forEach>
		</div>
	</div>
	
	<div class="bottDiv" style="width:100%; height:8%; background-color:#272727; margin-top:2px;" >
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="ui-a">
					<a href="<c:url value='/myCard/cardInfo.do?pk_group=${pk_group}&openid=${openid }' />" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
				<td id="shoppingCart" class="ui-b">
					<div class="cartDiv" onclick="JavaScript:showcartmenu()">
						<span id="menu-count1" class="cartNumDiv">0</span>
					</div>
				</td>
				<td class="ui-c">
					<span>￥</span>
					<span id="menu-price1">0.00</span>
					<span>&nbsp;|&nbsp;</span>
					<span id="menu-point1">0</span>
					<span style="font-size:60%">积分</span>
				</td>
				<td id="mymenudiv-right-b" class="ui-d">
					<span>选好了</span>
				</td>
			</tr>
		</table>
  	</div>
	<a id="dhk" href="#pagetwo" data-rel="dialog" data-transition="pop"></a> <!-- 没有选择弹出框 -->
	<a id="dhk2" href="#mymenu" data-transition="slideup"></a>               <!-- 进入提交页面  -->
	<a id="dhk3" href="#listCoupon" data-transition="slideup"></a>           <!-- 进入电子券选择页面  -->
</div>

<!----------------------------------------------- 没有选择电子券弹出框 -------------------------------------------------->
<div data-role="page" id="pagetwo" data-overlay-theme="e">
  <div data-role="header">
    <h1>温馨提示</h1>
  </div>
  <div data-role="content">
    <p id="pop"></p>
    <a href="#listCoupon" data-role="button" data-transition="slide">确定</a>
  </div>
</div> 

<!------------------------------------------------ 提交页面 --------------------------------------------------------->
<div data-role="page" id="mymenu" class="page">
	<div style="width:100%; height:92%;">
		<div class="tdTitle">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span>付款信息</span></div>
			</div>
		</div>
		<div class="tdContent">
			<table width="100%" height="50px" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="td_left">订单金额</td>
					<td class="orderMoney">
						<span style="font-size:25px;">￥</span>
						<span id="menu-price3" style="font-size:25px;">0.00</span>
					</td>
				</tr>
			</table>
		</div>
		<div class="tdContent">
			<table width="100%" height="50px" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="td_left">消费积分</td>
					<td class="orderMoney">
						<span id="menu-point3" style="font-size:25px;">0.00</span>
					</td>
				</tr>
			</table>
		</div>
		<div class="tdTitle">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span>支付方式</span></div>
			</div>
		</div>
		<div class="tdContent">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr onclick="changePayType(this, '1')">
					<td class="td_left" style="width:50px;">
						<img src="<c:url value='/image/wechat/wechat.png'/>" width="45px" height="45px" />
					</td>
					<td align="left">
						<p>
							<span class="payTypeName">微信支付</span><br />
							<span class="payTypeInfo">微信官方支付，方便快捷</span>
						</p>
					</td>
					<td class="pay-right">
						<img id="wxPay" src="<c:url value='/image/wechat/right.png'/>"/>
					</td>
				</tr>
			</table>
		</div>
		<div class="tdTitle">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span>购买明细</span></div>
			</div>
		</div>
		<div id="mymenu-main" style="margin:0 0 0 0;">
			<table id="mymenu-table">
			</table>
		</div>
	</div>
	<div class="commitdiv" style="height:8%; background-color:#272727; padding-top:1px;" >
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:-1px;">
			<tr>
				<td class="ui-a">
					<a href="javascript:returnLastPage();" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
				<td class="ui-b">
					<div class="cartDiv">
						<span id="menu-count2" class="cartNumDiv">0</span>
					</div>
				</td>
				<td class="ui-c">
					<span>￥</span>
					<span id="menu-price2">0.00</span>
					<span>&nbsp;|&nbsp;</span>
					<span id="menu-point2">0</span>
					<span style="font-size:60%">积分</span>
				</td>
				<td id="commitBtnDiv" class="ui-d">
					<span>提交订单</span>
				</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>