<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.choice.test.utils.Commons"%>
<%@ page import="com.choice.test.utils.CodeHelper"%>
<%String path = request.getContextPath(); %>
<%String seq = CodeHelper.createUUID(); %>

<!DOCTYPE html>
<html>
<head>
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/layer.js"></script>
<script src="<%=path %>/js/wechat/iscroll.js"></script>
<script src="<%=path %>/js/validate.js"></script>
<script src="<%=path %>/js/wechat/jweixin-1.0.0.js"></script>
<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<link rel="stylesheet" href="<%=path %>/css/default/global.css" />
<link rel="stylesheet" href="<%=path %>/css/wechat/listMenu.css" />
<link rel="stylesheet" href="<%=path %>/css/validate.css" />
<%@include file="/view/dining/jAlerts.jsp"%>
<style>
textarea {
	-webkit-appearance: none;
}
.number1{
	float:right;
	width:34px;
	height:36px;
	text-align:right;
	color:#7a7a7a;
	line-height:45px;
	font-family:Arial;
}
.itemNoSelect{
	border:1px solid #FFB400; 
	float:left; 
	text-align:center; 
	margin:4px;
	color:#FFB400;
}
.itemSelected{
	border:1px solid #FFB400; 
	float:left; 
	text-align:center; 
	margin:4px;
	background-color:#FFB400;
	color:#FFFFFF;
}
.saveAddItemBtn{
	width:100%; 
	height:13%; 
	background-color:#FFB400; 
	text-align:center; 
	color:#FFFFFF; 
	margin-top:10px; 
	float:left;
}
.itemGroup{
	padding-top:5px;
	overflow:hidden;
}
.itemGroupSpan{
	width:90%; 
	color:#999999; 
	float:left; 
	padding-left:5px;
	border:1px solid #FFFFFF;
}
.itemGroupSpanError{
	width:90%; 
	color:#999999; 
	float:left; 
	padding-left:5px;
	border:1px solid red;
}
.cai1{
	height:auto;
	text-align:left;
	font-size:16px;
	color:#5A5B5A;
/*  	margin-bottom: -10px; */
	margin-left: 10px;
}
.cai2{
	width:100%;
	height:auto;
	text-align:left;
	color:#FFB400;
	font-size:90%;
	font-family:Arial;
/* 	margin-top: -5px; */
	margin-left: 10px;
}
.leftItemType{
	background-color:#E5E5E5; 
	height:45px; 
	color:#484848;
	border-style:none;
}
.leftItemType span{
	font-size:16px; 
	line-height:45px; 
	float:right; 
	padding-right:5px;
}
#menu-left{
	float:left;
	width:22%;
	height:100%;
	overflow:auto;
	background-color:#E5E5E5;
}
.number{
	float:right;
	width:20px;
	height:100%;
	text-align:center;
	font-size:15px;
	line-height:35px;
	color:#7a7a7a;
	font-family:Arial;
}
.minus{
	float:right;
	width:35px;
	height:100%;
	text-align:center;
	padding:0px;
}
.plus{
	float:right;
	width:35px;
	height:100%;
	text-align:center;
	padding:0px;
}
.mypopup{
	position:fixed;
	left:0;
	bottom:0;
	width:100%;
	height:60%;
	background-color: #ffffff;
	border:0;
}
.mypopupScroller { 
	position:relative; /* */
	z-index:100; 
	height:76%;        /* Desired element height */ 
	float:left;
	width:100%;
}
.popupTitle{
	border-bottom:1px solid #D5D5D5;
	text-align:center;
	height:9%;
	padding:5px;
}
.popupNoSelect{
	padding:5px;
	color:#D5D5D5;
	background-color:#FFFFFF;
	text-align:center;
}
.popupSelect{
	padding:5px;
	color:#FFB400;
	background-color:#EEEEEE;
	text-align:center;
}
.popupComplate{
	clear:both;
	background-color:#FFB400;
	text-align:center;
	height:40px;
	line-height:40px;
	color:#FFFFFF;
	font-size:120%;
}
.totalPrice{
	font-family:Arial;
	width:50px;
}
input[type="submit"],
input[type="reset"],
input[type="button"],
button {
-webkit-appearance: none;
}

#fixedDivCartMenu tr td{
	text-align:center;
	padding:1% 5%;
	width:50%;
	border-bottom:1px dashed #EEEEEE;
}
</style>

<script type="text/javascript">
var packageDataTr = "",packagePrice=0,clickPackageCnt=parseInt("${tcPubitemSeq}"),tempCntSeq=0,tempPageckSeq=parseInt("${tcPubitemSeq}"),packageIdTemp="";
var dishSeq = 0;
/*去掉iphone手机滑动默认行为*/
$('body').on('touchmove', function (event) {
    event.preventDefault();
});
/**去除微信返回，刷新，分享**/
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});

/* $(document).ready(function(){
	$("#dhk5").click();
	window.setTimeout(function(){
		$("#dhk3").click();
	}, 3000);
}) */

var showOrderRemark = '<%=Commons.getConfig().getProperty("showOrderRemark") %>';

$(function(){
	var phoneType="";
	var ua = navigator.userAgent.toLowerCase();
	if(ua.indexOf('android') > -1 || ua.indexOf('linux') > -1) {
		// 安卓手机
		phoneType = "0";
	} else if(ua.indexOf('iphone') > -1) {
		// 苹果手机
		phoneType = "1";
	} else if(ua.indexOf('windows phone') > -1) {
		// winphone手机
		phoneType = "2";
	}
	if(phoneType=="0"){
		$(".cartNumDiv").addClass("androidHeight");
		$(".itemNumDiv").addClass("androidHeight");
	}else{
		$(".cartNumDiv").addClass("iphoneHeight");
		$(".itemNumDiv").addClass("iphoneHeight");
	}
	if("${orders.remark}" != null && "${orders.remark}" != "") {
		$("#remark").val("${orders.remark}");
	}
	
	if(showOrderRemark != 'Y') {
		$("#orderRemark").hide();
	}
});

$(document).on("pagehide","#mymenu",function(){
	var button = $("#popup_ok");
	if(button !=null){
		button.click();
	}
});

wx.config({
    debug: false,
    appId: '${appId}',
    timestamp: '${TIMESTAMP}',
    nonceStr: '${NONCE}',
    signature: '${SIGN}',
    jsApiList: ['scanQRCode']
});

var myScrollMenu;
var myScrollMustAddItem; 
var myScrollMustAddSingle; 
var myScrollMustAddLast; 
var myScrollPerson; 
function loaded() {
setTimeout(function(){
	myScrollMenu = new iScroll("fixedDivCartMenu",{hScrollbar:false,vScrollbar:false});
	//myScrollMustAddItem = new iScroll("fixedDivMustAddItem",{hScrollbar:false,vScrollbar:false});
	//myScrollMustAddSingle = new iScroll("fixedDivMustAddSingle",{hScrollbar:false,vScrollbar:false});
	
	myScrollPerson = new iScroll("wrapperPersons",{hScrollbar:false,vScrollbar:false});
	},900);
	//保证body加载完成后再执行。否则不生效。
} 
window.addEventListener("load",loaded,false);

// 外送时，判断是否已到达起送价
var canCommit = 0;
$(document).on("pageinit","#menupage",function(){
	// 通过修改菜品进入时，左侧显示各类别已点数量
	for(var i=0;i<=$("#mymenu-table").find("tr").length-1;i++){
		var foodnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text();//菜品数量
		var grptyp = $("#mymenu-table").find("tr").eq(i).attr("grptyp");//菜品类别
		var iflag = $("#mymenu-table").find("tr").eq(i).attr("flag");//菜品标志 0-单点 1-套餐
		if(iflag == "0" || iflag == "1"){
			for(var j = 0; j < foodnum; j++) {
				showDishNumByItemType(grptyp, "add");
			}
		}
	}
	
	/**********************************************在线选餐页面********************************************************************/
	/*菜单类型点击*/
	//$("#menu-left table tbody tr td").on("tap",function(){
	$(".leftItemType").on("tap",function(){
		regainChoose();//其他的选中都恢复
		//$(this).css("background","#117ad6");
		$(this).css("background-color","#FFFFFF");
		$(this).css("color","#FFB400");
		$("#menu-right").scrollTop("0");
		//$(this).css("border","2px solid #FFA201");
		/*功能：分类查询菜品*/
		var firmid = $("#firmid").val();//门店id
		var protypid = $(this).attr("id");//菜品类型id
		var name = $.trim($(this).find("span").text());//用来判断套餐
		var pkGroup = $("#pk_group").val();//企业编码
		var openid = $("#openid").val();//open id
		var protyp={};
		var data = "";
		if(protypid == "packageDiv" && name == "套餐"){  //进入套餐
			//发送请求，查询此门店下的所有套餐
			InitLayer();
			showMenu(eval('${listPackage}'), 'tc');
		}else{//进入单菜
			var itemCode = $(this).attr("itemCode");
			<c:forEach items="${allItemListMap }" var="item">
				if('${item.key}' == itemCode) {
					data =  eval('${item.value}');
				}
			</c:forEach>
			showMenu(data, 'dc');
		}
		//window.location.href="<%=path %>/pubitem/testjsapi.do?paymoney=1";
	});
	
	/*******************************确认提交订单********************************************************/
	$("#commitBtnDiv").on("tap",function(){
		if(canCommit == 1) {
			// 未达到起送价
			alertMsg("未到达起送价，起送价为：${startPrice}");
			return;
		}
			
		if(parseInt($("#menu-count1").text()) == 0){//如果没点菜，则提示没有点菜
			$("#pop").text("对不起，您还没有点菜。");
			$("#dhk").click();
		} else {
			// 校验备注是否超长
			var validate1 = new Validate({
				validateItem:[{
					type:'text',
					validateObj:'remark',
					validateType:['maxLength', 'withOutSpecialChar'],
					param:['200',''],
					error:['备注长度不能超过200','不能包含特殊字符']
				}]
			});
			if(!validate1._submitValidate()){
				return;
			}
			// 如果是订位的点菜单，直接跳转到订单详情页面
			var bookDeskOrderId = "${orders.bookDeskOrderID}";
			if(bookDeskOrderId != null && bookDeskOrderId != "") {
				//保存订单
				saveResvItems("1");
			} else {
				//如果是扫码点餐，询问是否确定直接下单
				var tables = $("#tables").val();
				if(tables != null && tables != "") {
					if("add" == "${type}") {
						// 加菜时，隐藏就餐人数
						$("#personTr").hide();
					}
					$("#showPushOrderOrNot").click();
				} else {
					saveResvItems("1");
				}
			}
		}
	});
	
	// 当微信内置浏览器完成内部初始化后会触发WeixinJSBridgeReady事件。
    //公众号支付
	function callpay(){
		var para={};
		para["total_fee"] = $("#menu-price3").text() * 100;
		$.ajaxSetup({async:false});
		$.post("<c:url value='/weChatPay/getPackageInfo.do?' />", para, function(data){
			if($.trim(data)){
				WeixinJSBridge.invoke('getBrandWCPayRequest',{
		  			"appId" : data.APPID, //公众号id，由商户传入
		  			"timeStamp" : data.TIMESTAMP, //时间戳
		  			"nonceStr" : data.NONCE, //随机串
		  			"package" : data.PACKEGEVALUE,//扩展包
					"signType" : data.SIGNTYPE, //微信签名方式:1.sha1
					"paySign" : data.SIGN //微信签名
		  		},function(res){
		  			WeixinJSBridge.log(res.err_msg);
		      		if(res.err_msg == "get_brand_wcpay_request:ok"){  
		          		alertMsg("微信支付成功");
						saveResvItems("6");
				    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
				        alertMsg("用户取消支付");  
						saveResvItems("1");
				    }else{  
				        alertMsg("支付失败");  
						saveResvItems("1");
				    }
		  		// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
		  		//因此微信团队建议，当收到ok返回时，向商户后台询问是否收到交易成功的通知，若收到通知，前端展示交易成功的界面；若此时未收到通知，商户后台主动调用查询订单接口，查询订单的当前状态，并反馈给前端展示相应的界面。
		  		});
			}else{
				alertMsg("获取预支付信息出错，支付失败。");
				return;
			}
		});
		//注释不用
		/* var para={};
		para["amt"] = $("#menu-price3").text() * 100;
		//para["discAmt"] = $("#xjqMoney").val();
		para["appid"] = "${APPID}";
		para["orderid"] = "<%=seq %>";
		$.ajaxSetup({async:false});
		$.post("<c:url value='/wxPay/enterPay.do?' />",para,function(data){
			if($.trim(data)){
				var obj = eval('(' + data + ')');
				if(parseInt(obj.agent)<5){
					alertMsg("您的微信版本低于5.0无法使用微信支付");
					return;
				}
				WeixinJSBridge.invoke('getBrandWCPayRequest',{
		  			"appId" :"${APPID}", //公众号id，由商户传入
		  			"timeStamp" : obj.timeStamp, //时间戳
		  			"nonceStr" : obj.nonceStr, //随机串
		  			"package" : obj.package,//扩展包
					"signType" : obj.signType, //微信签名方式:1.sha1
					"paySign" : obj.paySign //微信签名
		  		},function(res){
		  			WeixinJSBridge.log(res.err_msg);  
		      		if(res.err_msg == "get_brand_wcpay_request:ok"){  
		          		alertMsg("微信支付成功");  
		          		return "1";
				    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
				        alertMsg("用户取消支付");  
			          	return "2";
				    }else{  
				        alertMsg("支付失败");  
	 					alertMsg(res.err_code+" err_desc="+res.err_desc+" err_msg="+res.err_msg); 
			          	return "3";
				    }
		  		// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
		  		//因此微信团队建议，当收到ok返回时，向商户后台询问是否收到交易成功的通知，若收到通知，前端展示交易成功的界面；若此时未收到通知，商户后台主动调用查询订单接口，查询订单的当前状态，并反馈给前端展示相应的界面。
		  		}); 
			}else{
				alertMsg("获取预支付信息出错，支付失败。");
				return;
			}
		}); */
		var packageVal = $("#package").val() + "&total_fee=" + $("#menu-price3").text() * 100;
		WeixinJSBridge.invoke('getBrandWCPayRequest',{
  			"appId" : $("#appid").val(), //公众号名称，由商户传入
  			"timeStamp" : $("#timestamp").val(), //时间戳
  			"nonceStr" : $("#nonce").val(), //随机串
  			"package" : packageVal,//扩展包
			"signType" : $("#signtype").val(), //微信签名方式:1.sha1
			"paySign" : $("#sign").val() //微信签名
  		},function(res){
  			WeixinJSBridge.log(res.err_msg);  
  			alertMsg(res.err_msg);
      		if(res.err_msg == "get_brand_wcpay_request:ok"){  
          		//alertMsg("微信支付成功");  
          		return "1";
		    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
		        //alertMsg("用户取消支付");  
	          	return "2";
		    }else{  
		        //alertMsg("支付失败");  
	          	return "3";
		    }
  		// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
  		//因此微信团队建议，当收到ok返回时，向商户后台询问是否收到交易成功的通知，若收到通知，前端展示交易成功的界面；若此时未收到通知，商户后台主动调用查询订单接口，查询订单的当前状态，并反馈给前端展示相应的界面。
  		});
	}
	
	/************************套餐返回*************************************/
	$("#taocan-return").on("tap",function(){
		$("#hidetaocanbtn").click();
	});
});

//保存订单
function saveResvItems(state){
	var order={};
	var orderid=$("#orderid").val();
	order["id"]=orderid;
	order["addr"] = $("#addr").val();
	order["openid"] = $("#openid").val();
	order["firmid"] = $("#firmid").val();
	order["dat"] = $("#dat").val();
	order["sft"] = $("#sft").val();
	order["datmins"] = $("#datmins").val();
	order["remark"] = $("#remark").val();
	order["stws"] = $("#stws option:selected").val();
	order["money"] = $("#menu-price2").text();
	order["state"] = state;
	order["resv"] = '${orders.resv}';
	order["pk_group"] = '${pk_group}';
	order["tables"] = $("#tables").val();
	order["vcode"] = "${firm.firmCode}";
	order["bookDeskOrderID"] = "${bookDeskOrderID}";
	if("${orders.bookDeskOrderID}" != null && "${orders.bookDeskOrderID}" != "") {
		order["bookDeskOrderID"] = "${orders.bookDeskOrderID}";
	}
	if(parseInt($("#totalPerson").val()) > 0) {
		order["pax"] = $("#totalPerson").val();
	}
	//遍历   commit-menu 菜品列表
	var dtlIndex = 0;
	for(var i=0;i<=$("#mymenu-table").find("tr").length-1;i++){
		var rearmemo=$("#mymenu-table").find("tr").eq(i).find("td").find("input").val();
		var foodsid = $("#mymenu-table").find("tr").eq(i).attr("foodsid");//菜品id
		var foodsname = $("#mymenu-table").find("tr").eq(i).attr("foodsname");//菜品名称
		var foodnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text();//菜品数量
		var price = $("#mymenu-table").find("tr").eq(i).attr("price");//单价
		var flag = $("#mymenu-table").find("tr").eq(i).attr("flag");//单菜还是套餐的标识
		var totalprice = parseInt(foodnum) * parseFloat(price);//单个菜品总额
		var grptyp = $("#mymenu-table").find("tr").eq(i).attr("grptyp");//菜品类别
		var pcode = $("#mymenu-table").find("tr").eq(i).attr("pcode");//菜品编码
		var unit = $("#mymenu-table").find("tr").eq(i).attr("unit");//菜品单位
		if(flag=="0" || flag=="1"){
			order["listNetOrderDtl["+dtlIndex+"].foodsid"]=foodsid;
			order["listNetOrderDtl["+dtlIndex+"].ordersid"]=orderid;
			order["listNetOrderDtl["+dtlIndex+"].foodsname"]=foodsname;
			order["listNetOrderDtl["+dtlIndex+"].foodnum"]=foodnum;
			order["listNetOrderDtl["+dtlIndex+"].price"]=price;
			order["listNetOrderDtl["+dtlIndex+"].totalprice"]=totalprice;
			order["listNetOrderDtl["+dtlIndex+"].ispackage"]=flag;
			order["listNetOrderDtl["+dtlIndex+"].remark"]=rearmemo;
			order["listNetOrderDtl["+dtlIndex+"].grptyp"]=grptyp;
			order["listNetOrderDtl["+dtlIndex+"].pcode"]=pcode;
			order["listNetOrderDtl["+dtlIndex+"].unit"]=unit;

			if(flag=="1"){
				var indexDtl = 0;
				$("#mymenu-table").find("tr").eq(i).find("tr").each(function(){
					if($(this).attr("flag")=="2"){
						var vmemo=$(this).find("td").find("input").val();
						order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].pk_pubitem"]=$(this).attr("foodsid");
						order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].ncnt"]=1;
						order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].nzcnt"]=0;
						order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].nprice"]=$(this).attr("price1");//套餐内菜品价格price为菜品标准价格
						order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].vremark"]=vmemo;
						order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].unit"]=$(this).attr("unit");
						var trId = $(this).attr("id");
						var indexDishAddItem = 0;
						for(var iitem = 0; iitem <= $(this).find("#addItemDataTable"+trId.substring(4)).find("tr").length-1; iitem++){
							var trObj = $(this).find("#addItemDataTable"+trId.substring(4)).find("tr").eq(iitem).find("td");
							for(var jitem = 0; jitem <= trObj.length - 1; jitem++) {
								var tdObj = trObj.eq(jitem);
								var pk_ProdcutReqAttAc = tdObj.find("input").eq(0).val();
								var pk_Redefine = tdObj.find("input").eq(1).val();
								var namt = tdObj.find("input").eq(3).val();
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishAddItem["+indexDishAddItem+"].pk_ordersId"]=orderid;
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishAddItem["+indexDishAddItem+"].pk_pubItem"] = $(this).attr("foodsid");
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishAddItem["+indexDishAddItem+"].pk_redefine"] = pk_Redefine;
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishAddItem["+indexDishAddItem+"].pk_prodcutReqAttAc"] = pk_ProdcutReqAttAc;
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishAddItem["+indexDishAddItem+"].nprice"] = namt;
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishAddItem["+indexDishAddItem+"].ncount"] = "1";
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishAddItem["+indexDishAddItem+"].pk_group"] = '${pk_group}';
								indexDishAddItem++;
							}
						}
						indexDishAddItem = 0;
						for(var iproduct = 0; iproduct <= $(this).find("#prodAddDataTable"+trId.substring(4)).find("tr").length-1; iproduct++){
							var trObj = $(this).find("#prodAddDataTable"+trId.substring(4)).find("tr").eq(iproduct).find("td");
							for(var jproduct = 0; jproduct <= trObj.length - 1; jproduct++) {
								var tdObj = trObj.eq(jproduct);
								var pk_prodReqAdd = tdObj.find("input").eq(0).val();
								var pk_prodAdd = tdObj.find("input").eq(1).val();
								var namt = tdObj.find("input").eq(3).val();
								var unit = tdObj.find("input").eq(6).val();

								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishProdAdd["+indexDishAddItem+"].pk_ordersId"]=orderid;
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishProdAdd["+indexDishAddItem+"].pk_pubitem"] = $(this).attr("foodsid");
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishProdAdd["+indexDishAddItem+"].pk_prodAdd"] = pk_prodAdd;
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishProdAdd["+indexDishAddItem+"].pk_prodReqAdd"] = pk_prodReqAdd;
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishProdAdd["+indexDishAddItem+"].nprice"] = namt;
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishProdAdd["+indexDishAddItem+"].ncount"] = "1";
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishProdAdd["+indexDishAddItem+"].pk_group"] = '${pk_group}';
								order["listNetOrderDtl["+dtlIndex+"].orderPackageDetailList["+indexDtl+"].listDishProdAdd["+indexDishAddItem+"].unit"] = unit;
								indexDishAddItem++;
							}
						}
						indexDtl++;
					}
				});
			}
			
			// 获取行的序列号，已找到与其对应的附加项及附加产品
			var trSeq = $("#mymenu-table").find("tr").eq(i).attr("dishidx");
			
			// 整理菜品的附加项
			var itemCnt = 0;
			for(var itemTrIdx = 0; itemTrIdx <= $("#addItemDataTable").find("tr").length-1; itemTrIdx++){
				var subTrSeq = $("#addItemDataTable").find("tr").eq(itemTrIdx).attr("dishidx");
				if(subTrSeq == "") {
					subTrSeq = $("#addItemDataTable").find("tr").eq(itemTrIdx).find("td").eq(0).find("input").eq(5).val();
				}
				if(subTrSeq == trSeq) {
					var trObj = $("#addItemDataTable").find("tr").eq(itemTrIdx).find("td");
					for(var j = 0; j <= trObj.length - 1; j++) {
						var tdObj = trObj.eq(j);
						var pk_ProdcutReqAttAc = tdObj.find("input").eq(0).val();
						var pk_Redefine = tdObj.find("input").eq(1).val();
						var pk_PubItem = tdObj.find("input").eq(2).val();
						var namt = tdObj.find("input").eq(3).val();
			 			order["listNetOrderDtl["+dtlIndex+"].listDishAddItem[" + itemCnt + "].pk_ordersId"] = orderid;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishAddItem[" + itemCnt + "].pk_pubItem"] = pk_PubItem;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishAddItem[" + itemCnt + "].pk_redefine"] = pk_Redefine;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishAddItem[" + itemCnt + "].pk_prodcutReqAttAc"] = pk_ProdcutReqAttAc;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishAddItem[" + itemCnt + "].nprice"] = namt;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishAddItem[" + itemCnt + "].ncount"] = "1";
			 			order["listNetOrderDtl["+dtlIndex+"].listDishAddItem[" + itemCnt + "].pk_group"] = '${pk_group}';
			 			itemCnt = parseInt(itemCnt) + 1;
					}
				}
			}
			
			//遍历   附加产品
			var addCnt = 0;
			for(var prodTrIdx = 0; prodTrIdx <= $("#prodAddDataTable").find("tr").length-1; prodTrIdx++){
				var subTrSeq = $("#prodAddDataTable").find("tr").eq(prodTrIdx).attr("dishidx");
				if(subTrSeq == "") {
					subTrSeq = $("#prodAddDataTable").find("tr").eq(prodTrIdx).find("td").eq(0).find("input").eq(5).val();
				}
				if(subTrSeq == trSeq) {
					var trObj = $("#prodAddDataTable").find("tr").eq(prodTrIdx).find("td");
					for(var j = 0; j <= trObj.length - 1; j++) {
						var tdObj = trObj.eq(j);
						var pk_prodReqAdd = tdObj.find("input").eq(0).val();
						var pk_prodAdd = tdObj.find("input").eq(1).val();
						var pk_PubItem = tdObj.find("input").eq(2).val();
						var namt = tdObj.find("input").eq(3).val();
						var unit = tdObj.find("input").eq(6).val();
			 			order["listNetOrderDtl["+dtlIndex+"].listDishProdAdd[" + addCnt + "].pk_ordersId"] = orderid;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishProdAdd[" + addCnt + "].pk_pubitem"] = pk_PubItem;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishProdAdd[" + addCnt + "].pk_prodAdd"] = pk_prodAdd;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishProdAdd[" + addCnt + "].pk_prodReqAdd"] = pk_prodReqAdd;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishProdAdd[" + addCnt + "].nprice"] = namt;
			 			order["listNetOrderDtl["+dtlIndex+"].listDishProdAdd[" + addCnt + "].ncount"] = "1";
			 			order["listNetOrderDtl["+dtlIndex+"].listDishProdAdd[" + addCnt + "].pk_group"] = '${pk_group}';
			 			order["listNetOrderDtl["+dtlIndex+"].listDishProdAdd[" + addCnt + "].unit"] = unit;
			 			addCnt = parseInt(addCnt) + 1;
					}
				}
			}
			
			dtlIndex++;
		}
	}
	//遍历   附加项
	/* var itemCnt = 0;
	for(var i = 0; i <= $("#addItemDataTable").find("tr").length-1; i++){
		var trObj = $("#addItemDataTable").find("tr").eq(i).find("td");
		for(var j = 0; j <= trObj.length - 1; j++) {
			var tdObj = trObj.eq(j);
			var pk_ProdcutReqAttAc = tdObj.find("input").eq(0).val();
			var pk_Redefine = tdObj.find("input").eq(1).val();
			var pk_PubItem = tdObj.find("input").eq(2).val();
			var namt = tdObj.find("input").eq(3).val();
 			order["listDishAddItem[" + itemCnt + "].pk_ordersId"] = orderid;
 			order["listDishAddItem[" + itemCnt + "].pk_pubItem"] = pk_PubItem;
 			order["listDishAddItem[" + itemCnt + "].pk_redefine"] = pk_Redefine;
 			order["listDishAddItem[" + itemCnt + "].pk_prodcutReqAttAc"] = pk_ProdcutReqAttAc;
 			order["listDishAddItem[" + itemCnt + "].nprice"] = namt;
 			order["listDishAddItem[" + itemCnt + "].ncount"] = "1";
 			order["listDishAddItem[" + itemCnt + "].pk_group"] = '${pk_group}';
 			itemCnt = parseInt(itemCnt) + 1;
		}
	} */
	//遍历   附加产品
	/* var addCnt = 0;
	for(var i = 0; i <= $("#prodAddDataTable").find("tr").length-1; i++){
		var trObj = $("#prodAddDataTable").find("tr").eq(i).find("td");
		for(var j = 0; j <= trObj.length - 1; j++) {
			var tdObj = trObj.eq(j);
			var pk_prodReqAdd = tdObj.find("input").eq(0).val();
			var pk_prodAdd = tdObj.find("input").eq(1).val();
			var pk_PubItem = tdObj.find("input").eq(2).val();
			var namt = tdObj.find("input").eq(3).val();

 			order["listDishProdAdd[" + addCnt + "].pk_ordersId"] = orderid;
 			order["listDishProdAdd[" + addCnt + "].pk_pubitem"] = pk_PubItem;
 			order["listDishProdAdd[" + addCnt + "].pk_prodAdd"] = pk_prodAdd;
 			order["listDishProdAdd[" + addCnt + "].pk_prodReqAdd"] = pk_prodReqAdd;
 			order["listDishProdAdd[" + addCnt + "].nprice"] = namt;
 			order["listDishProdAdd[" + addCnt + "].ncount"] = "1";
 			order["listDishProdAdd[" + addCnt + "].pk_group"] = '${pk_group}';
			order["listDishProdAdd[" + addCnt + "].unit"] = unit;
 			addCnt = parseInt(addCnt) + 1;
		}
	} */
	InitLayer();
	//保存订单
	if("add" == "${type}") {
		// 加菜处理
		$.post("<%=path %>/bookMeal/addMenu.do",order,function(data){
			/* $("#inStoreOrNot").find("input").eq(0).val(data);
			gotoDetail("0", "0"); */
			wx.closeWindow();
		});
	} else {
		if(orderid==null || orderid=="" || orderid==undefined){//如果没有订单ID的情况（直接预订点餐）
			$.post("<%=path %>/bookMeal/saveOrderOrDtl.do?type=${type}",order,function(data){
				//$(".layer").hide();
				if(data != null && data != ''){
					processAfterSaveOrder(data);
				}else{
					$("#commitfailbtn").click();
				}
			});
		}else{//如果有订单ID（点桌或者预订里面跳转来的）
			if("${pageFrom}" == "takeout"){//外卖单详情里修改菜品
				$.post("<%=path %>/bookMeal/saveOrderOrDtl.do?type=takeout",order,function(data){
					//$(".layer").hide();
					if(data!=null && data != ''){
						processAfterSaveOrder(data);
					}else{
						$("#commitfailbtn").click();
					}
				});
			}else{
				$.post("<%=path %>/bookMeal/saveOrderOrDtl.do?type=${type}",order,function(data){
					//$(".layer").hide();
					if(data!=null && data != ''){
						processAfterSaveOrder(data);
					}else{
						$("#commitfailbtn").click();
					}
				});
			}
		}
	}
}

// 检测下单次数，超过15次(15秒)无返回，检测失败
var preOrderCnt = 0;

function processAfterSaveOrder(data) {
	$("#inStoreOrNot").find("input").eq(0).val(data);
	// 如果已生成二维码，直接跳转到订单详情页面
	// 如果是订位的点菜单，直接跳转到订单详情页面
	var bookDeskOrderId = "${orders.bookDeskOrderID}";
	
	var isPass = "bookDesk" == "${type}" || "takeout" == "${type}" || "${pageFrom}" == "takeout";
	if(!isPass && (('<%=Commons.getConfig().getProperty("generateQrCode") %>' == 'Y') || (bookDeskOrderId != null && bookDeskOrderId != "" ))) {
		//保存成功,询问是否已到店
		//$("#showInStoreOrNot").click();
		// 跳转到明细页面
		gotoDetail("0", "0");
	} else {
		InitLayer();
		//如果是扫码下单，询问是否确定下单
		var tables = $("#tables").val();
		if(tables != null && tables != "") {
			// 如果需要先支付，跳转到支付界面
			if("${mustPayBeforeOrder}" == "Y") {
				//下单前是否需要检测可下单
				var checkStoreCanOrder = '<%=Commons.getConfig().getProperty("checkStoreCanOrder") %>';
				if(checkStoreCanOrder == "Y") {
					// 先推送一次下单消息，检测是否可下单
					var tables = $("#tables").val();
					var firmid = $("#firmid").val();
					$.ajaxSetup({async:false});
					$.post("<c:url value='/bookDesk/commitOrdr.do?' />",{"pk_group":"${pk_group}","id":data,"tables":tables,"firmid":firmid,"mqtype":"42"},function(commitRet){
						if(commitRet.code == "1"){
							preOrderCnt = 0;
							// 每隔3秒检测一次门店是否已返回消息，如返回成功，进入支付页面，否则提示错误信息
							setTimeout("getPreOrderInfo('" + commitRet.serialid + "')", 100);
						} else {
							alertMsg("下单失败，请稍后再试");
							wx.closeWindow();
						}
					});
				} else {
					//不检测，直接下单
					var tables1 = $("#tables").val();
					var firmid1 = $("#firmid").val();
					var sumprice1 = $("#menu-price2").text();
					var pax1 = $("#totalPerson").val();
					var payUri = "<%=path %>/wxPay/toOrderPay.do?firmid=" + firmid1 + "&openid=${openid}"
							+ "&resv=&sumprice=" + sumprice1 + "&pax=" + pax1 + "&id=" + data 
							+ "&response_type=code&scope=snsapi_base&state=123&code=${code}&openScan=1&billstate=2";
					window.setTimeout(function(){
						location.href = payUri;
					},500);
				}
			} else {
				$("#pushOrderOrNot").popup("close");
				var tables = $("#tables").val();
				var firmid = $("#firmid").val();
				var scanType = "1";
				$.ajaxSetup({async:false});
				$.post("<c:url value='/bookDesk/commitOrdr.do?' />",{"pk_group":"${pk_group}","id":data,"tables":tables,"firmid":firmid},function(commitRet){
					if(commitRet.code == "1"){
						//alertMsg("订单已提交，请稍后查询订单状态。");
						wx.closeWindow();
					}else if(commitRet.code == "0"){
						scanType = "0";
						//alertMsg("没有要更新的数据");
						wx.closeWindow();
					}else{
						scanType = "0";
						alertMsg("下单失败，请稍后再试");
						wx.closeWindow();
					}
					closeLayer();
				});
			}
			//gotoDetail("0", scanType);
		} else {
			if("bookDesk" == "${type}"){//如果是订位单跳转过来的下一步再跳转到订位单详情
				var url = "<c:url value='/bookDesk/orderDetail.do?orderid=${bookDeskOrderID}&openid='/>"+$("#openid").val()
					+"&firmid="+$("#firmid").val()+"&pk_group=${pk_group}&bookMealOrderID="+data;
				location.href = url;
			}else if("takeout" == "${type}"){//外卖订单
				var url = "<c:url value='/takeout/orderDetail.do?orderid="+data+"&openid='/>"+$("#openid").val()
					+"&firmid="+$("#firmid").val()+"&pk_group=${pk_group}&code=${code}";
				location.href = url;
			}else{
				if("${pageFrom}" == "takeout"){//外卖单详情里修改菜品
					var url = "<c:url value='/takeout/orderDetail.do?orderid="+data+"&openid='/>"+$("#openid").val()
						+"&firmid="+$("#firmid").val()+"&pk_group=${pk_group}&code=${code}";
					location.href = url;
					return;
				}
				//保存成功,询问是否已到店
				closeLayer();
				$("#showInStoreOrNot").click();
			}
		}
	}
}

function getPreOrderInfo(serialid){
	preOrderCnt = preOrderCnt + 1;
	$.ajax({
			url:"<c:url value='/bookDesk/getPreOrderInfo.do'/>?serialid=" + serialid,
			type:"POST",
			dataType:"json",
			success:function(data){
				if(data.state == "1"){
					var tables1 = $("#tables").val();
					var firmid1 = $("#firmid").val();
					var sumprice1 = $("#menu-price2").text();
					var pax1 = $("#totalPerson").val();
					var payUri = "<%=path %>/wxPay/toOrderPay.do?firmid=" + firmid1 + "&openid=${openid}"
							+ "&resv=&sumprice=" + sumprice1 + "&pax=" + pax1 + "&id=" + data 
							+ "&response_type=code&scope=snsapi_base&state=123&code=${code}&openScan=1&billstate=2";
					window.setTimeout(function(){
						location.href = payUri;
					},500);
				} else if(data.state == "0") {
					alertMsg(data.errmsg);
					closeLayer();
				} else if(preOrderCnt > 15) {
					alertMsg("连接门店系统超时，请联系服务员！");
					closeLayer();
				} else {
					//getPreOrderInfo(serialid);
					// 每隔1秒检测一次门店是否已返回消息，如返回成功，进入支付页面，否则提示错误信息
					setTimeout("getPreOrderInfo('" + serialid + "')", 1000);
				}
			},
			error:function(data){
				alertMsg("出现错误，请重试。");
				closeLayer();
			}
	});
}

// 已经选择的附加项数量
var totalCnt = 0;
// 附加项最大数量
var maxTotalCnt = '<%=Commons.getConfig().getProperty("maxAddItemCount") %>';

// 点击必选附加项改变样式并判断是否在最大最小范围内
function chooseItem(obj) {
	$(obj).parent().find("span").first().attr("class", "itemGroupSpan");
	// 当前点击的附加项的样式
	var itemClass = $(obj).attr("class");
	// 判断选择个数
	var nowCnt = $(obj).parent().find("input").first().val();
	var minCnt = $(obj).parent().find("input").eq(1).val();
	var maxCnt = $(obj).parent().find("input").eq(2).val();
	
	// 选择个数等于最小个数，并且点击的附加项为已选择状态，点击后会小于最小数量
	// 选择个数等于最大个数，并且点击的附加项为未选择状态，点击后会大于最大数量
	/* if((parseInt(nowCnt) == parseInt(minCnt) && itemClass == 'itemSelected') 
			|| (parseInt(nowCnt) == parseInt(maxCnt)  && itemClass == 'itemNoSelect')) {
		$(obj).parent().find("span").first().css("border", "1px solid red");
		setTimeout(function(){
			$(obj).parent().find("span").first().css("border", "none");
		}, 500);
		return;
	} */
	// 选择个数等于最大个数，并且点击的附加项为未选择状态，点击后会大于最大数量
	if(parseInt(nowCnt) == parseInt(maxCnt)  && itemClass == 'itemNoSelect') {
		$(obj).parent().find("span").first().attr("class", "itemGroupSpanError");
		setTimeout(function(){
			$(obj).parent().find("span").first().attr("class", "itemGroupSpan");
		}, 100);
		return;
	}
		
	// 点击必选附加项改变样式
	if(itemClass == 'itemNoSelect') {
		if(maxTotalCnt != null && maxTotalCnt != '' && totalCnt >= maxTotalCnt) {
			alertMsg("附加项最多可以选择" + maxTotalCnt + "个！");
			return;
		}
		totalCnt = totalCnt + 1;
		$(obj).attr("class", "itemSelected");
		$(obj).parent().find("input").first().val(parseInt(nowCnt) + 1);
	} else {
		totalCnt = totalCnt - 1;
		$(obj).attr("class", "itemNoSelect");
		$(obj).parent().find("input").first().val(parseInt(nowCnt) - 1);
	}
}

// 点击可选附加项
function chooseCanSelectItem(obj) {
	// 当前点击的附加项的样式
	var itemClass = $(obj).attr("class");
	// 点击必选附加项改变样式
	if(itemClass == 'itemNoSelect') {
		if(maxTotalCnt != null && maxTotalCnt != '' && totalCnt >= maxTotalCnt) {
			alertMsg("附加项最多可以选择" + maxTotalCnt + "个！");
			return;
		}
		totalCnt = totalCnt + 1;
		$(obj).attr("class", "itemSelected");
		$(obj).parent().find("input").first().val(parseInt(nowCnt) + 1);
	} else {
		totalCnt = totalCnt - 1;
		$(obj).attr("class", "itemNoSelect");
		$(obj).parent().find("input").first().val(parseInt(nowCnt) - 1);
	}
}

//菜品序列号，从1开始，每点一道菜增加1
var dishSeq = 1;
if(null != "${dishSeq }" && parseInt("${dishSeq }") > 1) {
	dishSeq = parseInt("${dishSeq }");
}

// 保存附加项
function saveAddItem(obj, type, pubitem, dishidx) {
	if(type == "last" || type=="tc") {
		// 校验备注是否超长
		var validate1 = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'remarmemo11',
				validateType:['maxLength','withOutSpecialChar'],
				param:['50',''],
				error:['备注长度不能超过50','不能包含特殊字符']
			}]
		});
		if(!validate1._submitValidate()){
			return;
		}
	}
	var hasError = "0";
	// 所有已选择的附加项
	var selectedItem = "附加项：";
	$(obj).parent().find("div").each(function(){
		var groupDiv = $(this);
		var itemKey;
		var pk_ProdcutReqAttAc = "";
		
		if(groupDiv.attr("class") == 'itemGroup' && groupDiv.attr("name") == 'additem') {
			// 必选附加项校验数量
			if(groupDiv.find("span").eq(0).attr("class") == 'itemGroupSpan') {
				var nowCnt = groupDiv.find("input").eq(0).val();
				var minCnt = groupDiv.find("input").eq(1).val();
				var maxCnt = groupDiv.find("input").eq(2).val();
				// 菜品主键
				var pk_PubItem = groupDiv.find("input").eq(3).val();
				pubitem = pk_PubItem;
				// 附加项组主键
				pk_ProdcutReqAttAc = groupDiv.find("input").eq(4).val();
				
				// 选择个数小于于最小个数，或者大于最大个数，返回
				if(parseInt(nowCnt) < parseInt(minCnt) || parseInt(nowCnt) > parseInt(maxCnt)) {
					hasError = "1";
					groupDiv.find("span").first().attr("class", "itemGroupSpanError");
					setTimeout(function(){
						groupDiv.find("span").first().attr("class", "itemGroupSpan");
					}, 100);
					return;
				}
				// 如果已经存在，删除
				itemKey = pk_PubItem + dishidx + "_" + pk_ProdcutReqAttAc;
			} else if(groupDiv.find("input").eq(0).val() != undefined) {
				// 可选附加项
				pubitem = groupDiv.find("input").eq(0).val();
				itemKey = pubitem + dishidx + "_canSelect";
			}
			$("#additem_" + itemKey).remove();
			
			var dataHtml = "<tr id='additem_" + itemKey + "' dishidx='" + dishidx + "'>";
			groupDiv.children("div").each(function(){
				if($(this).attr("class") == 'itemSelected') {
					// 附加项主键
					var pk_Redefine = $(this).find("input").eq(0).val();
					// 价格
					var namt = $(this).find("input").eq(1).val();
					dataHtml = dataHtml + "<td><input type='hidden' value='" + pk_ProdcutReqAttAc + "'>"
						+ "<input type='hidden' value='" + pk_Redefine + "'>"
						+ "<input type='hidden' value='" + pubitem + "'>"
						+ "<input type='hidden' value='" + namt + "'>"
						+ "<input type='hidden' value='" + $(this).find("input").eq(2).val() + "'>"
						+ "<input type='hidden' value='" + dishidx + "'></td>";
					// 附加项名称
					selectedItem += $(this).find("span").eq(0).html().replace("<br>", "").replace("70%", "90%") + " ";
				}
			});
			dataHtml = dataHtml + "</tr>";
			
			if(dataHtml.indexOf("<td>") > 0) {
				if(type=="tc"){
					$("#addItemDataTable"+pubitem).append(dataHtml);
				}else{
					$("#addItemDataTable").append(dataHtml);
				}
			}
		}
	});
	
	// 所有附加产品
	var prodAddItem = "附加产品：";
	$(obj).parent().find("div").each(function(){
		var groupDiv = $(this);
		var itemKey;
		var pk_prodReqAdd = "";
		if(groupDiv.attr("class") == 'itemGroup' && groupDiv.attr("name") == 'prodadd') {
			// 必选附加项校验数量
			if(groupDiv.find("span").eq(0).attr("class") == 'itemGroupSpan') {
				var nowCnt = groupDiv.find("input").eq(0).val();
				var minCnt = groupDiv.find("input").eq(1).val();
				var maxCnt = groupDiv.find("input").eq(2).val();
				// 菜品主键
				var pk_PubItem = groupDiv.find("input").eq(3).val();
				// 必选附加产品主键
				pk_prodReqAdd = groupDiv.find("input").eq(4).val();
				
				// 选择个数小于于最小个数，或者大于最大个数，返回
				if(parseInt(nowCnt) < parseInt(minCnt) || parseInt(nowCnt) > parseInt(maxCnt)) {
					hasError = "1";
					groupDiv.find("span").first().attr("class", "itemGroupSpanError");
					setTimeout(function(){
						groupDiv.find("span").first().attr("class", "itemGroupSpan");
					}, 100);
					return;
				}
				// 如果已经存在，删除
				itemKey = pk_PubItem + dishidx + "_" + pk_prodReqAdd;
			} else if(groupDiv.find("input").eq(0).val() != undefined) {
				// 附加产品
				pubitem = groupDiv.find("input").eq(0).val();
				itemKey = pubitem + dishidx + "_canSelect";
			}
			$("#prodadd_" + itemKey).remove();
			
			var dataHtml = "<tr id='prodadd_" + itemKey + "' dishidx='" + dishidx + "'>";
			groupDiv.children("div").each(function(){
				if($(this).attr("class") == 'itemSelected') {
					// 附加产品主键
					var pk_prodAdd = $(this).find("input").eq(0).val();
					// 价格
					var price = $(this).find("input").eq(1).val();
					// 名称
					var addName = $(this).find("input").eq(2).val();
					// 单位
					var unit = $(this).find("input").eq(3).val();
					dataHtml = dataHtml + "<td><input type='hidden' value='" + pk_prodReqAdd + "'>"
						+ "<input type='hidden' value='" + pk_prodAdd + "'>"
						+ "<input type='hidden' value='" + pubitem + "'>"
						+ "<input type='hidden' value='" + price + "'>"
						+ "<input type='hidden' value='" + addName + "'>"
						+ "<input type='hidden' value='" + dishidx + "'>"
						+ "<input type='hidden' value='" + unit + "'></td>";
					// 附加产品名称
					prodAddItem += $(this).find("span").eq(0).html().replace("<br>", "").replace("70%", "90%") + " ";
				}
			});
			dataHtml = dataHtml + "</tr>";
			
			if(dataHtml.indexOf("<td>") > 0) {
				if(type=="tc"){
					$("#prodAddDataTable"+pubitem).append(dataHtml);
				}else{
					$("#prodAddDataTable").append(dataHtml);
				}
			}
		}
	});
	
	if(hasError == "0") {
		// 没有错误，关闭必选附加项选择框
		$("#mustadditem" + type).popup("close");
		
		// 增加菜品
		if(clickType == "main") {
			plusDish(clickObj, clickFlag);
		} else if(clickType == "single") {
			plusDish2(clickObj, clickFlag);
		}
		// 重置点击类型，防止下单页面点击备注后仍执行加菜
		clickType = "";
		
		// 显示已选择的附加项
		if(selectedItem != "附加项：") {
			$('#mymenu-table #div' + pubitem + dishidx + '_redefine').find('span').html(selectedItem);
			$('#mymenu-table #div' + pubitem + dishidx + '_redefine').show();
		} else {
			$('#mymenu-table #div' + pubitem + dishidx + '_redefine').find('span').html('');
			$('#mymenu-table #div' + pubitem + dishidx + '_redefine').hide();
		}
		
		// 显示已选择的附加产品
		if(prodAddItem != "附加产品：") {
			$('#mymenu-table #div' + pubitem + dishidx + '_prodadd').find('span').html(prodAddItem);
			$('#mymenu-table #div' + pubitem + dishidx + '_prodadd').show();
		} else {
			$('#mymenu-table #div' + pubitem + dishidx + '_prodadd').find('span').html('');
			$('#mymenu-table #div' + pubitem + dishidx + '_prodadd').hide();
		}
		
		// 保存备注
		if(type == 'last' || type == "tc") {
			$('#mymenu-table #list'+pubitem + dishidx).find('input').eq(0).val($('#remarmemo11').val());
			if($('#remarmemo11').val() != '') {
				$('#mymenu-table #div'+pubitem + dishidx).find('span').text("备注:" + $('#remarmemo11').val());
			} else {
				$('#mymenu-table #div'+pubitem + dishidx).find('span').text("");
				$('#mymenu-table #div'+pubitem + dishidx).hide();
			}
			$('#mymenu-table #div'+pubitem + dishidx).show();
		}
		
		listMymenu();
	}
}

/**
 * 点击菜名展示菜品详细信息
 */
function parentClick(obj) {
	$(obj).parent().prev().click();
}

function showMenu(data, typ) {
	$("#menu-table").find("tr").remove();
	//$("#menu-table").find("div").remove();
	var htmlContent;
	if(data.length <= 0) {
		$("#menu-table").append("<tr><td style='border:none'>没有找到符合条件的菜品</td></tr>");
	}
	for(var i=0;i<data.length;i++){
		var sl = 0;
		var backStyle = "bookBackSmall";
		var displayStyle = "display:none;";
		var minusDisplayStyle = "display:none;";
		if($("#list"+data[i].id).length > 0 && $("#list"+data[i].id).attr("flag")=="1" ){//如果我的菜单里存在这个菜品了，显示的时候加上数量；上个版本.attr("flag")=="0"，bug描述，已选套餐在套餐名称上不显示已选套餐的数量
			//sl = $("#list"+data[i].id).children().last().children(".number1").text();
			backStyle = "bookBackLarge";
			displayStyle = "";
			if($("#list"+data[i].id).attr("flag")=="0"){
				minusDisplayStyle = "";
			}
		} else if($(".list"+data[i].id).length > 0){//如果我的菜单里存在这个菜品了，显示的时候加上数量
			// 菜品包含必选附加项或必选附加产品时，菜品总数量为每条菜品数量的和
			for(var n = 0; n < $(".list"+data[i].id).length; n++) {
				sl += parseInt($(".list"+data[i].id).eq(n).find(".number1").text());
			}
			backStyle = "bookBackLarge";
			displayStyle = "";
			if($(".list"+data[i].id).eq(0).attr("flag")=="0" && data[i].reqredefine != "Y" && data[i].prodReqAddFlag != "Y"){
				minusDisplayStyle = "";
			}
		}

		var screenHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
		var plusClickAction = "plusClick(this,0)";
		var showSingleDishAction = "showSingleDish(this)";
		var sellOffHtml = "";
		var plusImage = "<%=path %>/image/wechat/plus_white.png";
		var dishImageHeight = 45 + (screenHeight - 460) / 10;
		var tdHeight = dishImageHeight / 2;
		var sellOffImageWidth = dishImageHeight * 1.2;
		var sellOffImageHeight = dishImageHeight * 0.9;
		var sellOffImage = "<div style='width:90%; height:" + dishImageHeight + "px; border-radius:15px;'>";
		if(typ=="tc" || data[i].pkgtag == "1"){//如果是套餐
			plusClickAction = "plusClick(this,1)";
		}
		if(data[i].hasSellOff == "Y") {
			// 已沽清商品不可点击增加按钮
			plusClickAction = "";
			showSingleDishAction = "";
			plusImage = "<%=path %>/image/wechat/plus_disable.png";
			sellOffHtml = "<div style='float:right; position:relative; '><img src='<%=path %>/image/wechat/selloff.png' width='" 
				+ sellOffImageWidth + "px' height='" + sellOffImageHeight + "px'/></div>";
			sellOffImage = "<div style='position:absolute; width:100%; height:" + dishImageHeight + "px; border-radius:15px; opacity:0.5; background-color:#000000;'>";
		}
		// 新品菜标志
		var newFlag = "";
		if(data[i].visnew == "Y") {
			newFlag = "&nbsp;<img style='width:15px;height:15px;' src=\""+ "<c:url value='/image/wechat/xinpin.png'/>" +"\"/>";
		}
		// 推荐菜标志
		var recFlag = "";
		if(data[i].visrec == "Y") {
			recFlag = "&nbsp;<img style='width:15px;height:15px;' src=\""+ "<c:url value='/image/wechat/recommend.png'/>" +"\"/>";
		}
		// 辣度标识
		var spicy = "";
		var spicyCnt = parseInt(data[i].vspicy);
		if(spicyCnt > 0) {
			spicy = "&nbsp;";
			for(var k = 0; k < spicyCnt; k++) {
				spicy += "<img style='width:15px;height:15px;' src=\""+ "<c:url value='/image/wechat/spicy.png'/>" +"\"/>";
			}
		}
		
		var plusRight = "";
		if(screen.width >= 600) {
			plusRight = "style='padding-right:10px;'";
		}
		
		//+ "<div style='position:relative; width:100%; height:70px; background:url(<%=Commons.vpiceure %>" + data[i].wxsmallpic + "); background-size:100% 70px;'>"
		//+ sellOffHtml + "</div></td>"
		//"<div style='position:relative;'><img src='<%=Commons.vpiceure %>" + data[i].wxsmallpic + "' width='90%' height='70px'/>"
		//菜品列表界面
		var plusButton = "";
		if(typ == "tc" || data[i].pkgtag == "1"){
			plusButton = "' d='"+data[i].unit + "' e='"+data[i].unit+"' f='"+data[i].itcode+"' g='"+data[i].grptyp+"' ><img src='" + plusImage + "' width='30px' height='30px'/>" ;
		}else{
			plusButton = "' d='"+data[i].unit + "' e='"+data[i].grptyp+"' f='"+data[i].itcode+"'><img src='" + plusImage + "' width='30px' height='30px'/>" ; 
		}
		var isVip = $("#isVip").val();
		var c = data[i].price;
		var oldprice = "";
		var newprice = "";
		if(isVip=="Y"){
			if(data[i].price4!=null && data[i].price4!="0.00" && data[i].price4!=""){
				c = data[i].price4;
				oldprice="<div class='oldcai'><div style='font-size:12px;font-family:Arial;text-decoration:line-through;color:#DCD7D6;'>￥" + data[i].price + "/" + data[i].unit+ "</div>" 
				+ "</div>";
			}
		}
		newprice = "<div class='cai2'><div style='font-size:13px;font-family:Arial;'>￥" + c + "/" + data[i].unit+ "</div>" 
		+ "</div>";
		var content = "<tr height='" + dishImageHeight + "px'><td rowspan='2' style=' border-bottom: 1px solid #eee;width:30%; ' onclick='" 
			+ showSingleDishAction + "' vpricetyp='"+data[i].vpricetyp+"' a='" + data[i].id + "' b='" + data[i].des 
			+ "' c='" + c + "' d='<%=Commons.vpiceure %>" + data[i].wxbigpic + "' e='" + data[i].unit + "' f='" + data[i].discription 
			+ "' g='" + data[i].grptyp + "' h='"+data[i].reqredefine+"' i='"+data[i].prodReqAddFlag+"'>"
			+ "<div style='position:relative; margin-left:5px; width:95%; height:" + dishImageHeight 
			+ "px; border-radius:5px; background:url(<%=Commons.vpiceure %>" + data[i].wxsmallpic + "); background-size:100% " + dishImageHeight + "px;'>"
			+ sellOffImage + "</div>" + sellOffHtml + "</div></td>"
			+ "<td class='texttd' colspan='2' style='height:" 
			+ tdHeight + "px; line-height:" + tdHeight + "px;'><div class='cai1' onclick='parentClick(this)'>" + data[i].des + newFlag + recFlag + spicy + "</div>" + "</td>"
			+ "</tr>"
			+ "<tr><td style='border-bottom: 1px solid #eee;width:30%'>"+oldprice+newprice+"</td>"
			+ "<td style='border-bottom: 1px solid #eee; width:55%'><div class='plus' " + plusRight + " onclick='" + plusClickAction + "' vpricetyp='"+data[i].vpricetyp+"' a='"+data[i].id+"' b='"+data[i].des+"' c='"+c
			+plusButton
			+ "<input type='hidden' value='" + data[i].reqredefine + "'>"
			+ "<input type='hidden' value='" + data[i].prodReqAddFlag + "'></div>"
			+ "<div class='number' id='num_" + data[i].id + "' style='" + displayStyle + "'>" + sl + "</div>"
			+ "<div class='minus' id='Div_"+data[i].id+"' style='" + minusDisplayStyle + "' onclick='minusClick(this)' a='"+data[i].id+"' b='"+data[i].des+"' c='"+c+"' d='"
			+ data[i].unit+"' e='"+data[i].grptyp+"' f='"+data[i].itcode+"'><img src='<%=path %>/image/wechat/minus_white.png' width='30px' height='30px'/></div></td></tr>";
			//
		htmlContent = "<div onclick='showSingleDish(this)' style='position:relative; width:100%; height:120px; background: url(<%=Commons.vpiceure %>" 
					+ data[i].wxsmallpic + "); -moz-background-size:100% 100%; background-size:100% 100%;' a='"
					+ data[i].id+"' b='"+data[i].des+"' c='"+c+"' d='<%=Commons.vpiceure %>"+data[i].wxbigpic+"' e='" 
					+ data[i].unit + "' f='" + data[i].discription + "' g='"+data[i].grptyp+"' h='"+data[i].reqredefine+"'>" 
					+ "<div class='" + backStyle + "' style='top:30%'>" 
					+ "<div class='plus' style='padding-right:8px;' onclick='plusClick(this,0); event.cancelBubble=true;' a='"+data[i].id+"' b='"+data[i].des+"' c='"+c+"' d='"+data[i].unit+"' e='"+data[i].grptyp+"' f='"+data[i].itcode+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/>"
					+ "<input type='hidden' value='" + data[i].reqredefine + "'></div>"
					+" <div class='number' id='num_" + data[i].id + "' style='" + displayStyle + " padding-top:7px;'>" + sl + "</div>"
					+" <div class='minus' style='" + displayStyle + "' onclick='minusClick(this); event.cancelBubble=true;'  a='"+data[i].id+"' b='"+data[i].des+"' c='"+c+"' d='"+data[i].unit+"' e='"+data[i].grptyp+"' f='"+data[i].itcode+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
					+ "</div>"
					+ "<div style='height:20px; width:100%; position:absolute; bottom:0px; opacity:0.5; background-color:#000000;'></div>" 
					+ "<span style='display:inline-block; position:relative; padding-top:100px; width:70%; height:20px; padding-left:10px; color:white;'>" 
					+ data[i].des + "</span>" 
					+ "<span style='display:inline-block; position:relative; padding-top:100px; height:20px; color:white;'>" 
					+ c + "元/" + data[i].unit + "</span>" 
					+ "</div>";
		
		if(data[i].wxsmallpic == null || data[i].wxsmallpic == "") {
			htmlContent = "<div onclick='showSingleDish(this)' style='position:relative; width:100%; height:80px;' a='"
				+ data[i].id + "' b='" + data[i].des + "' c='" + c + "' d='<%=Commons.vpiceure %>" + data[i].wxbigpic + "' e='" 
				+ data[i].unit + "' f='" + data[i].discription + "' g='"+data[i].grptyp+"' h='"+data[i].reqredefine+"'>" 
				+ "<table style='width:100%; height:100%;'>"
				+ "<tr>"
				+ "<td width='70%' style='text-align:left; border-bottom:1px dashed #EEEEEE;'><p><span style='font-size:110%; padding-left:10px;'>" + data[i].des + "</span><br />"
				+ "<span style='color:#FFB400; padding-left:10px;'>" + c + "元/" + data[i].unit +  "</span></p></td>"
				+ "<td width='30%' style='border-bottom:1px dashed #EEEEEE;'>"
				+ "<div class='" + backStyle + "' style='top:15%;'>" 
				+ "<div class='plus' style='padding-right:8px;' onclick='plusClick(this,0); event.cancelBubble=true;' a='"+data[i].id+"' b='"+data[i].des+"' c='"+c+"' d='"+data[i].unit+"' e='"+data[i].grptyp+"' f='"+data[i].itcode+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/>"
				+ "<input type='hidden' value='" + data[i].reqredefine + "'></div>"
				+" <div class='number' id='num_" + data[i].id + "' style='" + displayStyle + " padding-top:7px;'>" + sl + "</div>"
				+" <div class='minus' style='" + displayStyle + "' onclick='minusClick(this); event.cancelBubble=true;'  a='"+data[i].id+"' b='"+data[i].des+"' c='"+c+"' d='"+data[i].unit+"' e='"+data[i].grptyp+"' f='"+data[i].itcode+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
				+ "</td>"
				+ "</tr>"
				+ "</table>"
				+ "</div>";
		}			
					
		if(typ == "tc" || data[i].pkgtag == "1") {
			typ = "tc";
			//htmlContent = htmlContent.replace("showSingleDish", "showPackgeDtl");
			content = content.replace("showSingleDish", "showPackgeDtl");
		}
		$("#menu-table").append(content);
	}
	if(typ == "tc"){//已点套餐数量处理
		// 通过修改菜品进入时，显示套餐已点数量
		for(var im=0;im<=$("#mymenu-table").find("tr").length-1;im++){
			var grptyp = $("#mymenu-table").find("tr").eq(im).attr("grptyp");//菜品类别
			var iflag = $("#mymenu-table").find("tr").eq(im).attr("flag");//菜品标志 0-单点 1-套餐
			if(iflag == "1"){
				if(grptyp=="packageCode"){
					var packageId =  $("#mymenu-table").find("tr").eq(im).attr("foodsid");//菜品主键
					var packNum = parseInt($("#num_"+packageId).text());
						$("#num_"+packageId).text(packNum+1);
						$("#num_"+packageId).show();
				}
			}
		}
	}
	closeLayer();
}
// 点击购物车，显示已点菜品列表
function showcartmenu(cartFlag) {
	$("#cartmenutable" + cartFlag).find("tbody").remove();
	
	var cartHtml = $("#mymenu-table").html().replace(new RegExp("width:16%","gm"),"width:16%;display:none");
	$("#cartmenutable" + cartFlag).append(cartHtml);
	
	$("#cartmenutable" + cartFlag).append("<tr width='100%' height='50px'><td clospan='3'>&nbsp;</td></tr>");
	//myScrollMenu.refresh();
	//$("#showcartmenu").click();
	$("#cartmenu" + cartFlag).popup("open");
}

// 关闭已点菜品列表
function closeCartMenu(cartFlag) {
	$("#cartmenutable" + cartFlag).find("tbody").remove();
	$("#cartmenu" + cartFlag).popup("close");
}

$(function(){
	/**在微信浏览器打开**/
	/* var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)!="micromessenger") {
		$("body").text("请使用微信浏览器打开");
		return;
	} */
	
	//遍历   mymenu-table
	var zsl = 0;
	var zje = 0;
	for(var i=0;i<=$("#mymenu-table").find("tr").length-1;i++){
		var flag = $("#mymenu-table").find("tr").eq(i).attr("flag");//单价
		if(flag == "0" || flag == "1"){
			var foodnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text();//菜品数量
			var price = $("#mymenu-table").find("tr").eq(i).attr("price");//单价
			zsl += parseInt(foodnum);
			zje += parseInt(foodnum) * parseFloat(price);
			if(flag=="1"){
				var packageDtlTr = $("#mymenu-table").find("tr").eq(i).find("tr");
				for(var packageDtlTrIndex=0; packageDtlTrIndex<packageDtlTr.length; packageDtlTrIndex++){
					var trId = packageDtlTr.eq(packageDtlTrIndex).attr("id").substring(4);
					if(packageDtlTr.eq(packageDtlTrIndex).attr("flag")!="2"){
						continue;
					}
					// 遍历附加产品列表
					for(var i1 = 0; i1 <= $("#prodAddDataTable"+trId).find("tr").length - 1; i1++) {
						$("#prodAddDataTable"+trId).find("tr").eq(i1).find("td").each(function(){
							var price = $(this).find("input").eq(3).val();
							if(price != "") {
								var total = parseFloat(price);
								zje += total;
							}
						});
					}
					// 遍历附加项列表
					for(var i2 = 0; i2 <= $("#addItemDataTable"+trId).find("tr").length - 1; i2++) {
						$("#addItemDataTable"+trId).find("tr").eq(i2).find("td").each(function(){
							var price = $(this).find("input").eq(3).val();
							if(price != "") {
								var total = parseFloat(price);
								zje += total;
							}
						});
					}
				}
			}
		}
	}
	
	// 遍历附加产品
	for(var i = 0; i <= $("#prodAddDataTable").find("tr").find("td").length - 1; i++){
		var num = 1; //数量
		var pubitem = $("#prodAddDataTable").find("tr").find("td").eq(i).find("input").eq(2).val(); //菜品主键
		var idx = $("#prodAddDataTable").find("tr").find("td").eq(i).find("input").eq(5).val(); //序列号
		for(var j = 0; j <= $("#mymenu-table").find("tr").length - 1; j++){
			var foodsid = $("#mymenu-table").find("tr").eq(j).attr("foodsid");
			var foodidx = $("#mymenu-table").find("tr").eq(j).attr("dishidx");
			if(foodsid == pubitem && foodidx == idx) {
				num = $("#mymenu-table").find("tr").eq(j).children().last().children(".number1").text();
			}
		}
		
		var price = $("#prodAddDataTable").find("tr").find("td").eq(i).find("input").eq(3).val(); //单价
		var totalprice = parseInt(num) * parseFloat(price);//单个菜品总额
		
		zje += totalprice;
	}
	
	// 遍历附加项
	for(var i = 0; i <= $("#addItemDataTable").find("tr").find("td").length - 1; i++){
		var num = 1; //数量
		var pubitem = $("#addItemDataTable").find("tr").find("td").eq(i).find("input").eq(2).val(); //菜品主键
		var idx = $("#addItemDataTable").find("tr").find("td").eq(i).find("input").eq(5).val(); //序列号
		for(var j = 0; j <= $("#mymenu-table").find("tr").length - 1; j++){
			var foodsid = $("#mymenu-table").find("tr").eq(j).attr("foodsid");
			var foodidx = $("#mymenu-table").find("tr").eq(j).attr("dishidx");
			if(foodsid == pubitem && foodidx == idx) {
				num = $("#mymenu-table").find("tr").eq(j).children().last().children(".number1").text();
			}
		}
		
		var price = $("#addItemDataTable").find("tr").find("td").eq(i).find("input").eq(3).val(); //单价
		var totalprice = parseInt(num) * parseFloat(price);//单个菜品总额
		
		zje += totalprice;
	}
	
	zje = zje.toFixed(2);
	$("#menu-count2").text(zsl);
	$("#menu-price2").text(zje);
	$("#menu-price3").text(zje);
	$("#menu-count4").text(zsl);
	$("#menu-price4").text(zje);
	conPriSyn();
	
	/**一上来就选中全部菜品中的第一个*/
	//$("#menu-left table tbody tr td").first().tap();
	$(".leftItemType").first().tap();
	
	/*搜索*/
	$("#gotoSearch").click(function(){
		$("#searchdiv").show();
		return;
	});
	
	/*返回，搜索按钮点击*/
	$("#searchbtn").click(function(){
		if($(this).text() == '返回'){
			$("#searchdiv").hide();
			$("#searchtext").val();
		}else{
			var firmid = $("#firmid").val();
			var pdes = $.trim($("#searchtext").val());
			if(pdes == ""){
				$("#pop").text("输入菜品不能为空。");
				$("#dhk").click();
				return;
			}
			var project = {};
			project["des"]=pdes;
			project["firmid"]=firmid;
			project["type"]="${type}";
			project["pageFrom"]="${pageFrom}";
			//发送搜索请求
			InitLayer();
			$.post("<%=path %>/bookMeal/getPubitemByName.do?firmCode=${firm.firmCode}",project,function(data){
				showMenu(data, 'dc');
			});
			$("#searchdiv").hide();
			$("#searchtext").val();
			return;
		}
	});
	
	/*取消搜索*/
	$(document).bind("click",function(e){
	  var target  = $(e.target);
	  if(target.closest("#gotoSearch").length == 0 && target.closest("#search-left").length == 0){
		   $("#searchtext").val("");
	       $("#searchbtn").text("返回");
	       $("#searchdiv").hide();
	  }
	 });
	/*按钮变成搜索*/
	$("#searchtext").focus(function(){
		$("#searchbtn").text("搜索");
	});
	
	//
	$("#mymenudiv-right-b").click(function(){
		if(parseInt($("#menu-count1").text()) == 0){//如果没点菜，则提示没有点菜
			$("#pop").text("对不起，您还没有点菜。");
			$("#dhk").click();
		}else{//跳转到我的菜单
			$("#dhk2").click();
		}
	});
	
	$("#commitFromSingle").click(function(){
		/* if(parseInt($("#menu-count1").text()) == 0){//如果没点菜，则提示没有点菜
			$("#pop").text("对不起，您还没有点菜。");
			$("#dhk").click();
		}else{//跳转到我的菜单
			$("#dhk2").click();
		} */
		returnLastPage();
	});
	//套餐保存事件
	$("#commitFromPackage").click(function(){
		var taocansequence = $("#taocansequence").val();
		if($("#taocanDtlEdit").val()=="0"){
			var flag = false;
			//判断当前套餐是否都已经按照规则选择了菜品
			$(".tcAllChecked").each(function(){
				if($(this).val()!="true"){
					$(this).parent().css("background-color","red");
					flag = true;
				}
			});
			//如果没有按照规则点菜，添加标志并返回
			if(flag){
				return;
			}
			var totle = 0;//单价汇总
			var taocanp = parseFloat($("#taocan-money").attr("price"));
			var price;
			//固定价格类型
			if(vpricetyp=="1"){
				
				var trs = $("#taocan-table").find(".packageDetailGroupInfo");
				for(var ii = 0;ii<trs.length;ii++){
					var div = $(trs[ii]).find(".packagedtlDiv");
					var foodeds = $(div[0]).children("tr");
					for(var j = 0;j<foodeds.length;j++){
						fooded = foodeds[j];
						var p = $(fooded).attr("price");
						var v = $(fooded).attr("nadjustprice");
						if(v==undefined){
							v="0";
						}
						totle+=parseFloat(p);
						taocanp+=parseFloat(v);
					}
				}
				for(var ii = 0;ii<trs.length;ii++){
					var div = $(trs[ii]).find(".packagedtlDiv");
					var foodeds = $(div[0]).children("tr");
					for(var j = 0;j<foodeds.length;j++){
						fooded = foodeds[j];
						var p = $(fooded).attr("price");
						var pf = parseFloat(p);
						var pp = pf*(taocanp/totle);
						$(fooded).attr("price",pp);
					}
				}
				price = taocanp;
			}else{//汇总高优先
				var trs = $("#taocan-table").find(".packageDetailGroupInfo");
				for(var ii = 0;ii<trs.length;ii++){
					var div = $(trs[ii]).find(".packagedtlDiv");
					var foodeds = $(div[0]).children("tr");
					for(var j = 0;j<foodeds.length;j++){
						fooded = foodeds[j];
						var p = $(fooded).attr("price");
						totle+=parseFloat(p);
					}
				}
				price = totle;
			}
			var grptyp = "packageCode";
			var packagetyp = $("#packagetyp").val();
			if(packagetyp != null && packagetyp!="" && packagetyp!="null"){
				grptyp = packagetyp;
			}
			showDishNumByItemType(grptyp, "add");
			$("#mymenu-table").append(packageDataTr);//将所点套餐追加到我的点菜单
// 			var menucount = parseInt($("#menu-count1").text());
// 			$("#menu-count1").text(menucount+1);//更新点菜界面点菜数量
// 			var menucount2 = parseInt($("#menu-count2").text());
// 			$("#menu-count2").text(menucount2+1);//更新我的点菜单界面点菜数量
// 			var menuprice = Number($("#menu-price1").text());
// 			$("#menu-price1").text(Number(menuprice+Number(packagePrice)).toFixed(2));//更新点菜界面点菜金额
// 			var menuprice2 = Number($("#menu-price2").text());
// 			$("#menu-price2").text(Number(menuprice2+Number(packagePrice)).toFixed(2));//更新我的点菜单界面点菜金额
// 			$("#menu-price3").text(Number(menuprice2+Number(packagePrice)).toFixed(2));//更新我的点菜单界面总金额
			packageDataTr = "";
			packagePrice = 0;
			var tmpTr = "";
			//循环套餐点菜明细，组建套餐明细数据
			$("#taocan-table").find(".packageDetailGroupInfo").find(".packagedtlDiv").each(function(){
				tmpTr += $(this).html();
			});
			$("#mymenu-table").find("tr").last().attr("price", taocanp);
			$("#mymenu-table").find("tr").last().find("td").eq(2).find("span").eq(1).html(taocanp);//加上此行，使得查看已选菜单时，已选套餐的价格为加上可换菜的价格，使套餐价格显示更合理
			$("#mymenu-table").find("tr").last().find("#div"+$("#pk_taocan").val()+taocansequence+"_dtl").append(tmpTr);
			var tcCnt = parseInt($("#num_"+$("#pk_taocan").val()).text());
			$("#num_"+$("#pk_taocan").val()).text(tcCnt+1);
			$("#num_"+$("#pk_taocan").val()).show();
// 			listMymenu();
			returnLastPage();
		}else{
			//此时只有一个套餐中明细div为空
// 			$("#div"+$("#pk_taocan").val()+"_dtl").each(function(){
// 				if($(this).html()=="" || $(this).html()==null){
					$("#div"+$("#pk_taocan").val()+taocansequence+"_dtl").html($("#taocan-table").find("tbody").html());
// 					$(this).html($("#taocan-table").find("tbody").html());
// 				}
// 			});
			$("#hidetaocanbtn").click();
			$("#taocanDtlEdit").val("0");//将编辑状态重置为0
		}
		$("#pk_taocan").val("");//清空套餐主键
		$("#packagetyp").val("");//清空套餐主键
		$("#taocansequence").val("");//清空套餐序列号
		listMymenu();
	});
});
/*****************************************************************************点菜页面**********************************************/
/*改变左侧菜品类型列表的选中状态*//*改变头部热门菜品和全部菜品的选中状态*/
function regainChoose(){
	//$("#menu-left table tr td").css("background","#fff");
	//$("#menu-left table tr td").css("color","#117ad6");
	$(".leftItemType").css("background","#E5E5E5");
	$(".leftItemType").css("color","#484848");
	$(".leftItemType").css("border-style","none");
	//$(".leftItemType").css("border","1px solid #434343");
}
/*点菜页面中的减菜*/
function minusClick(obj){
	InitLayer();
	var sl = parseInt($(obj).prev().text());
	
	// 左侧类别栏显示此类别点了多少菜
	var grptyp = $(obj).attr("e");
	if(sl >= 1) {
		showDishNumByItemType(grptyp, "minus");
	}
	
	sl -= 1;
	$(obj).prev().text(sl);
	
	var pubitem = $(obj).attr("a");
	
	if(sl == 0){
		//$(obj).parent().attr("class", "bookBackSmall");
		
		// 删除已选附加项
		deleteMustAddItem(pubitem, -1);
		
		$(obj).hide();
		$(obj).prev().hide();
		$(".list"+pubitem).remove();//下个页面table删掉这个菜品
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
		return;
	}
	$("#mymenu-table #list"+pubitem+clickDishIdx).children().last().children(".number1").text(sl);//下个页面table中数量变化
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
}

var clickObj;
var clickFlag;
var clickType;
var clickDishIdx = 10;//17版程序为1，bug描述：先点套餐，再点与套餐里面包含的相同的单品时，购物车数量和价格不变

/*点菜页面中的加菜 flag:0-单点菜，1-点套餐*/
function plusClick(obj,flag){
	if(flag=="1"){//如果是套餐，进入套餐界面
		showPackgeDtl(obj);
		return;
	}
	
	var reqredefine = $(obj).find("input").first().val();
	var prodReqAddFlag = $(obj).find("input").eq(1).val();
	var appendDishIdx = "";
	if(reqredefine == 'Y' || prodReqAddFlag == 'Y') {
		dishSeq = parseInt(dishSeq) + 1;
		appendDishIdx = dishSeq;
		listCnt = 1;
		clickObj = obj;
		clickFlag = flag;
		clickType = "main";
		clickDishIdx = appendDishIdx;
		showMustAddItem($(obj).attr("a"), '', reqredefine, prodReqAddFlag, '', dishSeq);
	} else {
		plusDish(obj, flag);
	}
}

function plusDish(obj, flag) {
	InitLayer();
	dh1(obj, "shoppingCart");//动画效果
	$(obj).nextAll().show();
	var sl = parseInt($(obj).next().text());
	sl += 1;
	$(obj).next().text(sl);	

	var reqredefine = $(obj).find("input").first().val();
	var prodReqAddFlag = $(obj).find("input").eq(1).val();
	var appendDishIdx = clickDishIdx;
	//clickDishIdx++;
	//if(sl == 1) {
		//$(obj).parent().attr("class", "bookBackLarge");
		var listCnt = sl;
		if(reqredefine == 'Y' || prodReqAddFlag == 'Y') {
			if(sl > 1) {
				$(obj).next().next().hide();
			}
		}
	//}
	
	// 左侧类别栏显示此类别点了多少菜
	var grptyp = $(obj).attr("e");
	showDishNumByItemType(grptyp, "add");
	
	var pubitem = $(obj).attr("a");
	var pdes = $(obj).attr("b");
	var price = $(obj).attr("c");
	var unit = $(obj).attr("d");
	var pcode = $(obj).attr("f");
	if($("#list"+pubitem+appendDishIdx).length > 0 ){//如果存在这个菜品了
		$("#list"+pubitem+appendDishIdx).children().last().children(".number1").text(sl);
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
		return;
	}
	//var fun = "showRemark('" + pubitem + "','" + pdes + "')";
	var fun = "showMustAddItem('" + pubitem + "','last','" + reqredefine + "','" + prodReqAddFlag + "','','" + appendDishIdx + "')";
	var menudata = "<tr width='100%' height='50px' id='list"+pubitem+appendDishIdx+"' class='list" + pubitem + "' foodsid='"+pubitem+"' foodsname='"+pdes+"' price='"+price+"' flag='"+flag+"' unit='" + unit + "' grptyp='" + grptyp + "' pcode='" + pcode + "' dishidx='" + appendDishIdx + "'>"
	+"<td style='width:0px;' hidden='hidden'><input type='hidden'/></td>"
	+"<td style='width:16%;' onclick=" + fun + "><div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'><span style='color:#FFF; font-size:13px; line-height:35px'>备注</span></td>"
	+"<td style='width:45%;'><div class='cai11'><span style='font-size:16px;'>" + pdes + "</span></div>"
	+"<div class='cai22'><span style='color:#FFB400; font-size:15px; font-family:Arial;'>" + price + "</span><span style='color:#FFB400; font-size:13px;'>元/" + unit + "</span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "_redefine' style='width:100%;text-align:left;display:none;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "_prodadd' style='width:100%;text-align:left;display:none;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "' style='width:100%;text-align:left;display;none;word-break:break-all;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"</td><td style='width:39%;'><div class='plus1' onclick='plusClick1(this)' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/>"
	+ "<input type='hidden' value='" + reqredefine + "'>"
	+ "<input type='hidden' value='" + prodReqAddFlag + "'></div>"
	+"<div class='number1' style='display:;'>1</div>"
	+"<div class='minus1' style='display:;' onclick='minusClick1(this, \"" + appendDishIdx + "\")' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' d='"+unit+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
	+"</td></tr>";
	$("#mymenu-table").append(menudata);
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
}
<%-- function plusClick(obj,flag){
	if(flag=="1"){//如果是套餐，进入套餐界面
		showPackgeDtl(obj);
		return;
	}
	InitLayer();
	dh1(obj, "shoppingCart");//动画效果
	$(obj).nextAll().show();
	var sl = parseInt($(obj).next().text());
	sl += 1;
	$(obj).next().text(sl);	

	var reqredefine = $(obj).find("input").first().val();
	var prodReqAddFlag = $(obj).find("input").eq(1).val();
	var appendDishIdx = "";
	//if(sl == 1) {
		//$(obj).parent().attr("class", "bookBackLarge");
		var listCnt = sl;
		if(reqredefine == 'Y' || prodReqAddFlag == 'Y') {
			dishSeq = parseInt(dishSeq) + 1;
			appendDishIdx = dishSeq;
			listCnt = 1;
			showMustAddItem($(obj).attr("a"), '', reqredefine, prodReqAddFlag, '', dishSeq);
			if(sl > 1) {
				$(obj).next().next().hide();
			}
		}
	//}
	
	// 左侧类别栏显示此类别点了多少菜
	var grptyp = $(obj).attr("e");
	showDishNumByItemType(grptyp, "add");
	
	var pubitem = $(obj).attr("a");
	var pdes = $(obj).attr("b");
	var price = $(obj).attr("c");
	var unit = $(obj).attr("d");
	var pcode = $(obj).attr("f");
	if($("#list"+pubitem+appendDishIdx).length > 0 ){//如果存在这个菜品了
		$("#list"+pubitem+appendDishIdx).children().last().children(".number1").text(sl);
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
		return;
	}
	//var fun = "showRemark('" + pubitem + "','" + pdes + "')";
	var fun = "showMustAddItem('" + pubitem + "','last','" + reqredefine + "','" + prodReqAddFlag + "','','" + appendDishIdx + "')";
	var menudata = "<tr width='100%' height='50px' id='list"+pubitem+appendDishIdx+"' class='list" + pubitem + "' foodsid='"+pubitem+"' foodsname='"+pdes+"' price='"+price+"' flag='"+flag+"' unit='" + unit + "' grptyp='" + grptyp + "' pcode='" + pcode + "' dishidx='" + appendDishIdx + "'>"
	+"<td style='width:0px;' hidden='hidden'><input type='hidden'/></td>"
	+"<td style='width:16%;' onclick=" + fun + "><div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'><span style='color:#FFF; font-size:13px; line-height:35px'>备注</span></td>"
	+"<td style='width:45%;'><div class='cai11'><span style='font-size:16px;'>" + pdes + "</span></div>"
	+"<div class='cai22'><span style='color:#FFB400; font-size:15px; font-family:Arial;'>" + price + "</span><span style='color:#FFB400; font-size:13px;'>元/" + unit + "</span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "_redefine' style='width:100%;text-align:left;display:none;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "_prodadd' style='width:100%;text-align:left;display:none;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "' style='width:100%;text-align:left;display;none;word-break:break-all;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"</td><td style='width:39%;'><div class='plus1' onclick='plusClick1(this)' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/>"
	+ "<input type='hidden' value='" + reqredefine + "'>"
	+ "<input type='hidden' value='" + prodReqAddFlag + "'></div>"
	+"<div class='number1' style='display:;'>"+listCnt+"</div>"
	+"<div class='minus1' style='display:;' onclick='minusClick1(this, \"" + appendDishIdx + "\")' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' d='"+unit+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
	+"</td></tr>";
	$("#mymenu-table").append(menudata);
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
} --%>
/*加菜效果*/
function dh1(obj, targetId){
	$("#yddiv").remove();
	var left = $(obj).offset().left;
	var top = $(obj).offset().top;
	var top1 = $("#" + targetId).offset().top;
	var div = "<div id='yddiv' style='background:#FFB400;position:fixed;z-index:99;width:10px;height:10px;top:"+top+"px;left:"+left+"px'></div>";
	$("#menupage").append(div);
	$("#yddiv").animate({top:top1+$("#" + targetId).height()-35, left:"33%"}, 500).animate({opacity:"0"},500);
}  

// 类别中显示此类别共点了多少菜
function showDishNumByItemType(grptyp, type) {
	var dishNum = $("#itemType_" + grptyp).text();
	if(type == "add") {
		$("#itemType_" + grptyp).text(parseInt(dishNum) + 1);
		$("#itemType_" + grptyp).show();
	} else if(parseInt(dishNum) >= 1) {
		$("#itemType_" + grptyp).text(parseInt(dishNum) - 1);
		if(parseInt(dishNum) == 1) {
			$("#itemType_" + grptyp).hide();
		}
	}
}

// 展示必选附加项
function showMustAddItem(pubitem, type, mustType, prodReqAddFlag,tcfujiaParam, dishidx) {
	// 已选附加项总数量重置为0
	totalCnt = 0;
	
	if(type=="tc"){
// 		tcfujiaParam=clickPackageCnt;
		dishidx = "";
	}else{
		tcfujiaParam = "";
	}
	var notMustHeight = "60%";
	var textHeight = "17%";
	$("#mustadditemtable" + type).find("div").remove();
	var itemHtml = "<div id='appendDiv' style='height:100%;z-index:999;'>";
	
	if(type != "single") {
		if(prodReqAddFlag == 'Y' && mustType == 'Y') {
			notMustHeight = "20%";
			$("#mustadditemlast").css("height", "100%");
		} else if(prodReqAddFlag == 'Y' || mustType == 'Y') {
			notMustHeight = "30%";
			$("#mustadditemlast").css("height", "70%");
		} else {
			$("#mustadditemlast").css("height", "70%");
		}
	}
	
	// 添加附加产品
	if(prodReqAddFlag == 'Y') {
		itemHtml = itemHtml + "<div><span style='padding-top:5px;'>附加产品</span></div>";
		<c:forEach items="${prodReqAddMap }" var="item">
			if('${item.key}' == pubitem) {
				<c:forEach items="${item.value }" var="data">
					// 已选附加项个数
					var existItemObj = $("#prodadd_${data.pk_pubitem }"+tcfujiaParam+dishidx+"_${data.pk_prodReqAdd }");
					var nowCnt = existItemObj.find("td").length;
					itemHtml = itemHtml + "<div name='prodadd' class='itemGroup'>" 
						+ "<span class='itemGroupSpan'>-${data.vdes }(最少选${data.minCount }个，最多选${data.maxCount }个)</span>"
						+ "<input type='hidden' value='" + nowCnt + "'>"
						+ "<input type='hidden' value='${data.minCount }'>"
						+ "<input type='hidden' value='${data.maxCount }'>"
						+ "<input type='hidden' value='" + pubitem+tcfujiaParam + "'>"
						+ "<input type='hidden' value='${data.pk_prodReqAdd }'>";
					<c:forEach items="${data.listProductAdditional }" var="detail">
						// 判断此附加产品是否已选中，如已选中，样式使用itemSelected
						var itemClass = "itemNoSelect";
						existItemObj.find("input").each(function(){
							if($(this).val() == '${detail.pk_prodAdd }') {
								itemClass = "itemSelected";
								totalCnt = totalCnt + 1;
							}
						});
						
						var priceHtml = "";
						if(parseFloat("${detail.price }") > 0.0) {
							priceHtml = "<br /><span style='color:red; font-size:70%'>￥${detail.price }</span>";
						}
						
						itemHtml = itemHtml + "<div class='" + itemClass + "' onclick='javascript:chooseItem(this)'>" 
							+ "<span style='font-size:110%; line-height:20px;'>${detail.prodReqAddName }" 
							+ priceHtml + "</span>"
							+ "<input type='hidden' value='${detail.pk_prodAdd }'>"
							+ "<input type='hidden' value='${detail.price }'>"
							+ "<input type='hidden' value='${detail.prodReqAddName }'>"
							+ "<input type='hidden' value='${detail.unit }'></div>";
					</c:forEach>
					itemHtml = itemHtml + "</div>";
				</c:forEach>
			}
		</c:forEach>
	}
	
	// 添加必选附加项
	if(mustType == 'Y') {
		textHeight = "10%";
		itemHtml = itemHtml + "<div><span style='padding-top:5px;'>必选附加项</span></div>";
		<c:forEach items="${menuReqAttAcMap }" var="item">
			if('${item.key}' == pubitem) {
				<c:forEach items="${item.value }" var="data">
					// 已选附加项个数
					var existItemObj = $("#additem_${data.pk_PubItem }"+tcfujiaParam+dishidx+"_${data.pk_ProdcutReqAttAc }");
					var nowCnt = existItemObj.find("td").length;
					itemHtml = itemHtml + "<div name='additem' class='itemGroup'>" 
						+ "<span class='itemGroupSpan'>-${data.vdes }(最少选${data.minCount }个，最多选${data.maxCount }个)</span>"
						+ "<input type='hidden' value='" + nowCnt + "'>"
						+ "<input type='hidden' value='${data.minCount }'>"
						+ "<input type='hidden' value='${data.maxCount }'>"
						+ "<input type='hidden' value='" + pubitem+tcfujiaParam + "'>"
						+ "<input type='hidden' value='${data.pk_ProdcutReqAttAc }'>";
					<c:forEach items="${data.productRedfineList }" var="detail">
						// 判断此附加项是否已选中，如已选中，样式使用itemSelected
						var itemClass = "itemNoSelect";
						existItemObj.find("input").each(function(){
							if($(this).val() == '${detail.pk_Redefine }') {
								itemClass = "itemSelected";
								totalCnt = totalCnt + 1;
							}
						});
						
						var priceHtml = "";
						if(parseFloat("${detail.namt }") > 0.0) {
							priceHtml = "<br /><span style='color:red; font-size:70%'>￥${detail.namt }</span>";
						}
						
						itemHtml = itemHtml + "<div class='" + itemClass + "' onclick='javascript:chooseItem(this)'>" 
							+ "<span style='font-size:110%; line-height:20px;'>${detail.vname }"
							+ priceHtml + "</span>"
							+ "<input type='hidden' value='${detail.pk_Redefine }'>"
							+ "<input type='hidden' value='${detail.namt }'>"
							+ "<input type='hidden' value='${detail.vname }'></div>";
					</c:forEach>
					itemHtml = itemHtml + "</div>";
				</c:forEach>
			}
		</c:forEach>
	}
	/* if(type == 'last') {
		itemHtml = itemHtml + "<input type='hidden' value='" + pubitem + "'>";
	} */
	if(type == 'last' || type=='tc') {
		if("${redefineListSize}" > 0) {
			// 已选可选附加项
			var existItemObj = $("#additem_" + pubitem+tcfujiaParam + dishidx + "_canSelect");
			
			itemHtml = itemHtml + "<div style='clear:both;'></div>"
				+ "<div style='padding-top:5px;'><table height='100%'><tr><td><span>可选附加项</span></td></tr></table></div>";
			itemHtml = itemHtml + "<div id='itemGroup_last' style='overflow:hidden;height:" + notMustHeight 
				+ ";position:relative;z-index:1100;width:100%;'><div class='itemGroup' name='additem'>" 
				+ "<input type='hidden' value='" + pubitem+tcfujiaParam + "'>";
			<c:forEach items="${redefineList }" var="item">
				// 判断此附加项是否已选中，如已选中，样式使用itemSelected
				var itemClass = "itemNoSelect";
				existItemObj.find("input").each(function(){
					if($(this).val() == '${item.pk_Redefine }') {
						itemClass = "itemSelected";
						totalCnt = totalCnt + 1;
					}
				});
				
				var priceHtml = "";
				if(parseFloat("${item.namt }") > 0.0) {
					priceHtml = "<br /><span style='color:red; font-size:70%'>￥${item.namt }</span>";
				}
				
				itemHtml = itemHtml + "<div class='" + itemClass + "' onclick='javascript:chooseCanSelectItem(this)'>" 
					+ "<span style='font-size:110%; line-height:20px;'>${item.vname }" 
					+ priceHtml + "</span>"
					+ "<input type='hidden' value='${item.pk_Redefine }'>"
					+ "<input type='hidden' value='${item.namt }'>"
					+ "<input type='hidden' value='${item.vname }'></div>";
			</c:forEach>
			itemHtml = itemHtml + "</div></div>";
		}else{
			if(mustType == 'Y' && prodReqAddFlag == 'Y') {
				textHeight = "20%";
				$("#mustadditemlast").css("height", "80%");
			} else if(mustType == 'Y' || prodReqAddFlag == 'Y') {
				textHeight = "20%";
				$("#mustadditemlast").css("height", "60%");
			} else {
				textHeight = "60%";
				$("#mustadditemlast").css("height", "45%");
			}
			itemHtml = itemHtml + "<div></div>"
			+ "<div></div>";
		itemHtml = itemHtml + "<div id='itemGroup_last'><div class='itemGroup'>" 
			+ "&nbsp;</div></div>";
		}
		
		var remarkDisplay = "display:;";
		if(showOrderRemark != 'Y') {
			remarkDisplay = "display:none;";
		}
		
		itemHtml = itemHtml + "<div style='overflow:hidden;height:" + textHeight + ";text-align:center;" + remarkDisplay + "'><textarea id='remarmemo11' maxlength='50' data-role='none' rows='2' placeholder='您对当前菜品还有什么需求，请在此备注(最多输入50字)' " 
			+ "style='margin:5px auto;padding-top:5px;background-color: #FFFFFF;border:1px solid #EEEEEE;border-radius:4px; width:100%; color:#717171;'></textarea></div>";
	}
	itemHtml = itemHtml + "<div class='saveAddItemBtn' style='height:35px;' onclick='javascript:saveAddItem(this, \"" + type + "\", \"" + pubitem+tcfujiaParam + "\", \"" + dishidx + "\")'>" 
		+ "<table border='0' cellspacing='0' cellpadding='0' align='center'><tr><td><span style='font-size:120%; line-height:35px; text-align:center;'>确定</span></td></tr></table></div>"+ "</div>";
	
	$("#mustadditemtable" + type).append(itemHtml);
	$('#remarmemo11').val($('#mymenu-table #list'+pubitem+tcfujiaParam+dishidx).find('input').eq(0).val());
	
	var width = $(document).width();
	width = (width*0.9-36)/3;
	$(".itemNoSelect").width(width);
	$(".itemSelected").width(width);
	if(type=="last"){
		if(myScrollMustAddLast!=null){
			myScrollMustAddLast.destroy();
		}
		myScrollMustAddLast = new iScroll("itemGroup_last",{hScrollbar:false,vScrollbar:false});
	}else if(type=="single"){
		if(myScrollMustAddSingle == null){
			myScrollMustAddSingle = new iScroll("fixedDivMustAddSingle",{hScrollbar:false,vScrollbar:false});
		}else{
			myScrollMustAddSingle.refresh();
		}
	} else {
		if(myScrollMustAddItem == null){
			myScrollMustAddItem = new iScroll("fixedDivMustAddItem",{hScrollbar:false,vScrollbar:false});
		}else{
			myScrollMustAddItem.refresh();
		}
	}
	
	//$("#showmustadditem" + type).click();
	$("#mustadditem" + type).popup("open");
	
}

// 菜品数量减为0时，根据菜品ID删除附加项及附加产品
function deleteMustAddItem(pubitem, itemIdx) {
	for(var i = $("#addItemDataTable").find("tr").length-1; i >= 0; i--){
		var trObj = $("#addItemDataTable").find("tr").eq(i);
		if(trObj.attr("id").indexOf(pubitem) > 0 && (itemIdx == -1 || trObj.attr("dishidx") == itemIdx)) {
			trObj.remove();
		}
	}
	
	for(var i = $("#prodAddDataTable").find("tr").length-1; i >= 0; i--){
		var trObj = $("#prodAddDataTable").find("tr").eq(i);
		if(trObj.attr("id").indexOf(pubitem) > 0 && (itemIdx == -1 || trObj.attr("dishidx") == itemIdx)) {
			trObj.remove();
		}
	}
}

/**遍历我的订单，给总数量和总价格赋值*/
function listMymenu(){
	//遍历   mymenu-table
	var menuCount = 0;
	var menuPrice = 0;
	for(var i=0;i<=$("#mymenu-table").find("tr").length-1;i++){
		var flag = $("#mymenu-table").find("tr").eq(i).attr("flag");//单价
		if(flag == "0" || flag == "1"){
			var foodnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text();//菜品数量
			var price = $("#mymenu-table").find("tr").eq(i).attr("price");//单价
			foodnum = parseInt(foodnum);
			var totalprice = parseInt(foodnum) * parseFloat(price);//单个菜品总额
			menuCount += foodnum;
			menuPrice += totalprice;
			if(flag=="1"){
				var packageDtlTr = $("#mymenu-table").find("tr").eq(i).find("tr");
				for(var packageDtlTrIndex=0; packageDtlTrIndex<packageDtlTr.length; packageDtlTrIndex++){
					var trId = packageDtlTr.eq(packageDtlTrIndex).attr("id").substring(4);
					if(packageDtlTr.eq(packageDtlTrIndex).attr("flag")!="2"){
						continue;
					}
					// 遍历附加产品列表
					for(var i1 = 0; i1 <= $("#prodAddDataTable"+trId).find("tr").length - 1; i1++) {
						$("#prodAddDataTable"+trId).find("tr").eq(i1).find("td").each(function(){
							var price = $(this).find("input").eq(3).val();
							if(price != "") {
								var total = parseFloat(price);
								menuPrice += total;
							}
						});
// 						var price = $("#prodAddDataTable"+trId).find("tr").eq(i1).find("td").find("input").eq(3).val();
// 						if(price != "") {
// 							var total = parseFloat(price);
// 							menuPrice += total;
// 						}
					}
					// 遍历附加项列表
					for(var i2 = 0; i2 <= $("#addItemDataTable"+trId).find("tr").length - 1; i2++) {
						$("#addItemDataTable"+trId).find("tr").eq(i2).find("td").each(function(){
							var price = $(this).find("input").eq(3).val();
							if(price != "") {
								var total = parseFloat(price);
								menuPrice += total;
							}
						});
// 						var price = $("#addItemDataTable"+trId).find("tr").eq(i2).find("td").find("input").eq(3).val();
// 						if(price != "") {
// 							var total = parseFloat(price);
// 							menuPrice += total;
// 						}
					}
				}
			}
		}
	}
	
	// 遍历附加产品列表
	for(var i = 0; i <= $("#prodAddDataTable").find("tr").find("td").length - 1; i++) {
		var num = 1;
		var pubitem = $("#prodAddDataTable").find("tr").find("td").eq(i).find("input").eq(2).val();
		var idx = $("#prodAddDataTable").find("tr").find("td").eq(i).find("input").eq(5).val();
		for(var j = 0; j <= $("#mymenu-table").find("tr").length - 1; j++){
			var foodsid = $("#mymenu-table").find("tr").eq(j).attr("foodsid");
			var foodidx = $("#mymenu-table").find("tr").eq(j).attr("dishidx");
			if(foodsid == pubitem && foodidx == idx) {
				num = $("#mymenu-table").find("tr").eq(j).children().last().children(".number1").text();
			}
		}
		
		var price = $("#prodAddDataTable").find("tr").find("td").eq(i).find("input").eq(3).val();
		if(price != "") {
			var total = num * parseFloat(price);
			menuPrice += total;
		}
	}
	
	// 遍历附加项列表
	for(var i = 0; i <= $("#addItemDataTable").find("tr").find("td").length - 1; i++) {
		var num = 1;
		var pubitem = $("#addItemDataTable").find("tr").find("td").eq(i).find("input").eq(2).val();
		var idx = $("#addItemDataTable").find("tr").find("td").eq(i).find("input").eq(5).val();
		for(var j = 0; j <= $("#mymenu-table").find("tr").length - 1; j++){
			var foodsid = $("#mymenu-table").find("tr").eq(j).attr("foodsid");
			var foodidx = $("#mymenu-table").find("tr").eq(j).attr("dishidx");
			if(foodsid == pubitem && foodidx == idx) {
				num = $("#mymenu-table").find("tr").eq(j).children().last().children(".number1").text();
			}
		}
		
		var price = $("#addItemDataTable").find("tr").find("td").eq(i).find("input").eq(3).val();
		if(price != "") {
			var total = num * parseFloat(price);
			menuPrice += total;
		}
	}
	
	menuPrice = menuPrice.toFixed(2);
	$("#menu-count2").text(menuCount);//本页面和下个页面底部数量和价格变化
	$("#menu-price2").text(menuPrice);
	$("#menu-price3").text(menuPrice);
	$("#menu-count4").text(menuCount);
	$("#menu-price4").text(menuPrice);
	conPriSyn();
	
	// 如果是外卖，判断是否已到达起送价
	if("${type}" == "takeout"){
		var startPrice = parseFloat("${startPrice}");
		if(menuPrice < startPrice) {
			// 未到达起送价
			$("#commitBtnDiv").find("span").eq(0).text("起送价${startPrice}");
			canCommit = 1;
		} else {
			$("#commitBtnDiv").find("span").eq(0).text("保存菜单");
			canCommit = 0;
		}
	}
}
/****前一个页面同步后一个页面的数量和价格*****/
function conPriSyn(){
	$("#menu-count1").text($("#menu-count2").text());//本页面和下个页面底部数量和价格变化
	$("#menu-price1").text($("#menu-price2").text());
	$("#menu-price3").text($("#menu-price2").text());
	/*
	var menuCount = $("#menu-count2").text();
	var length = (menuCount.toString()).length;
	var width = (1+(0.25*length))+"em";
	$(".cartNumDiv").css({"width":width,"height":width,"line-height":width});
	*/
}

/*****************************************************************************我的订单页面**********************************************/
/*减菜*/
function minusClick1(obj, idx){
	InitLayer();
	var sl = parseInt($(obj).prev().text());
	
	// 左侧类别栏显示此类别点了多少菜
	var grptyp = $(obj).attr("e");
	if(sl >= 1) {
		showDishNumByItemType(grptyp, "minus");
	}
	
	sl -= 1;
	$(obj).prev().text(sl);
	var pubitem = $(obj).attr("a");
	
	$("#num_" + pubitem).text(parseInt($("#num_" + pubitem).text()) - 1);
	if(parseInt($("#num_" + pubitem).text()) == 0) {
		$("#num_" + pubitem).next().hide();
		$("#num_" + pubitem).hide();
	}
	
	if(sl == 0){
		//$("#num_" + pubitem).parent().attr("class", "bookBackSmall");
		//$("#num_" + pubitem).next().hide();
		//$("#num_" + pubitem).hide();
		
		// 删除已选附加项
		deleteMustAddItem(pubitem, idx);
		
		//将整条菜品删除
		$("#list" + pubitem + idx).remove();
		$("#mymenu-table #list" + pubitem + idx).remove();
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
		return;
	}
	
	//大图界面，修改菜品数量
	//$("#singleDishMenuTable number1").text(sl);
	
	// 我的点菜单中修改相应数量及显示
	$("#mymenu-table #list" + pubitem + idx + " .number1").text(sl);
	
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
}
/*套餐减菜*/
function packageMinusClick(obj){
	InitLayer();
	var sl = parseInt($(obj).prev().text());
	
	// 左侧类别栏显示此类别点了多少菜
	var grptyp = $(obj).attr("e");
	//套餐类别点菜数量减1
	showDishNumByItemType(grptyp, "minus");
	//将整条菜品删除
	var pubitem = $(obj).attr("a");
	//将整条菜品删除 购物车里的删除
	
	var bs = $("#cartmenutable #list"+pubitem).siblings();
	var now = $(obj).parent().parent();
	var befor;
	for(var i =0;i<bs.length;i++){
		if($(now).attr("id")=="list"+pubitem){
			$(now).remove();
			break;
		}
		
		befor = $(now).prev();
		$(now).remove();
		now = befor;
	}
	//$("#cartmenutable #list"+pubitem).remove();
	//删除我的菜单中的	--hxl
	$("#mymenu-table #list"+pubitem).remove();
	//$(obj).parent().parent().remove();
	
	var taocanCnt = parseInt($("#num_" + pubitem).text());
	$("#num_" + pubitem).text(taocanCnt-1);
	
	if((taocanCnt-1) == 0){
		$("#num_" + pubitem).next().hide();
		$("#num_" + pubitem).hide();
		
		
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
		return;
	}
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
}
/*加菜*/
function plusClick1(obj){
	closeLayer();
	$(obj).nextAll().show();
	var sl = parseInt($(obj).next().text());
	sl += 1;
	$(obj).next().text(sl);

	$("#num_" + $(obj).attr("a")).show();
	$("#num_" + $(obj).attr("a")).next().show();
	$("#num_" + $(obj).attr("a")).text(parseInt($("#num_" + $(obj).attr("a")).text()) + 1);
	//$("#num_" + $(obj).attr("a")).parent().attr("class", "bookBackLarge");

	var reqredefine = $(obj).find("input").first().val();
	var prodReqAddFlag = $(obj).find("input").eq(1).val();
	// 如果菜品包含必选附加项或必选附加产品，数量大于1时不显示减号。因为此时各菜品所选附加项或附加产品不一定相同
	if(reqredefine == 'Y' || prodReqAddFlag == 'Y') {
		if(parseInt($("#num_" + $(obj).attr("a")).text()) > 1) {
			$("#num_" + $(obj).attr("a")).next().hide();
		}
	}
	
	// 左侧类别栏显示此类别点了多少菜
	var grptyp = $(obj).attr("e");
	showDishNumByItemType(grptyp, "add");
	
	//大图界面，修改菜品数量
	//$("#singleDishMenuTable .number1").text(sl);
	
	// 我的点菜单中修改相应数量及显示
	$("#mymenu-table #list" + $(obj).attr("a")+clickDishIdx + " .number1").text(sl);
	
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
}
/*清空**/
function clear1(){
	$("#mymenu-table").find("tr").remove();
	$("#menu-count2").text("0");//本页面和下个页面底部数量和价格变化
	$("#menu-price2").text("0.00");
	$("#menu-price3").text("0.00");
	conPriSyn();
}
/**继续加菜*/
function plusmenu(){
	$("#showmenupagebtn").click();//显示第一个页面
	/**一上来就选中第一个*/
	//$("#menu-left table tbody tr td").first().tap();
	$(".leftItemType").first().tap();
	
}

function showRemark(pubitem, des){
	$('#remarmemo').find('input').first().val(pubitem);
	$('#remarmemo').find('td').first().text(des);
	$('#remarmemo11').val($('#list'+pubitem).find('input').val());
//  	$('#remarmemo').find('textarea').val("");
	//$('#remarmemo').show();
	$('#showremarmemo').click();
}
function commitRemarmemo(){
	var pubitem = $('#remarmemo').find('input').val();
	$('#list'+pubitem).find('input').val($('#remarmemo11').val());
	if($('#remarmemo11').val() != '') {
		$('#div'+pubitem).find('span').text("附加项:" + $('#remarmemo11').val());
	} else {
		$('#div'+pubitem).find('span').text("");
		$('#div'+pubitem).hide();
	}
	$('#div'+pubitem).show();
	//$('#remarmemo').hide();
	$("#remarmemo").popup("close");
}

function cancelRemarmemo(){
	$("#remarmemo").popup("close");
}

// 已到店，直接跳转到扫码下单
function hasInStore() {
	gotoDetail("1", "0");
}

// 未到店，展示订单信息
function notInStore() {
	//gotoDetail("0", "0");
	wx.closeWindow();
}

// 跳转到订单详情页面
// type:是否已到店
// scanType：是否扫码点餐 0否；1是
function gotoDetail(type, scanType) {
	var link1=$("#commitsuccessbtn").attr("href");
	var orderid = $("#inStoreOrNot").find("input").eq(0).val();
	
	var bookDeskOrderId = "${orders.bookDeskOrderID}";
	if(bookDeskOrderId != null && bookDeskOrderId != "") {
		link1 +="&pageFrom=${pageFrom}";
	}
	
	window.location.href = link1 + "&orderid=" + orderid + "&scanType=" + scanType + "&inStoreType=" + type + "&openid=" + $("#openid").val() + "&firmid=" + $("#firmid").val();
}

//下单操作ajax提交
function confirmOrder(){
	$("#confirmSingleDiv").popup("close");//隐藏提示框
	InitLayer();
	var ordersid = $("#inStoreOrNot").find("input").eq(0).val();
	var tableId = $("#inStoreOrNot").find("input").eq(1).val();
	$.ajaxSetup({async:false});
	$.post("<c:url value='/bookDesk/commitOrdr.do?' />",{"pk_group":"${pk_group}","id":ordersid,"tables":tableId},function(data){
		if(data.code == "1"){
			alertMsg("下单成功。");
			InitLayer();
			window.setTimeout(function(){
				location.href = location.href;
			},100);
		}else if(data.code == "0"){
			alertMsg("没有要更新的数据");
		}else{
			alertMsg("下单失败。");
		}
		closeLayer();
	});
}

/********************************************单菜，套餐页面********************************************************************/
/**单菜详细页面**/
function showSingleDish(obj){
	var id = $(obj).attr("a");
	var des = $(obj).attr("b");
	var price = $(obj).attr("c");
	var img = $(obj).attr("d");
	var unit = $(obj).attr("e");
	var discription = $(obj).attr("f");
	var grptyp = $(obj).attr("g");
	var reqredefine = $(obj).attr("h");
	var prodReqAddFlag = $(obj).attr("i");
	var sl = $("#list"+id).children().last().children(".number1").text();
	if(sl == "") {
		sl = "0";
	}
	$("#singleDishMenuTable").find("tr").remove();
	$("#singleDishImg").find("div").remove();
	
	var imgHtml = "<div id='dancaidiv' style='width:100%;height:100%;background:transparent;z-index:99;color:#ccc;padding-top:0px;'>"
		+"<div style='width:100%;height:50%;'>"
		+"<div style='width:100%;height:90%;text-align:center;position:relative;'>"
		+"<img src='"+img+"' width='100%' height='100%'/>"
		+"</div></div></div>";
	
	if(img != null && img != "" && img.indexOf('null') < 0) {
		$("#singleDishImg").append(imgHtml);
	} else {
		$("#singleDishImg").append("<div style='height:50px;'></div>");
	}
	
	des = $(obj).next().find("div").eq(0).html();
	
	var menudata = "<tr height='50px' foodsid='"+id+"' foodsname='"+des+"' price='"+price+"' flag=0'>"
	+"<td width='5%'></td>"
	+"<td width='50%'><div class='cai11'><span style='font-size:20px;'>" + des + "</span></div>"
	+"<div class='cai22' style='font-size: 100%;'><span style='color:#FFB400;font-family:Arial;'>￥" + price + "</span><span style='color:#FFB400; font-size:15px;'>/" + unit + "</span></div>"
	+"</td><td width='40%'><div class='plus1' style='display:none' onclick='plusClick2(this, 0)' a='"+id+"' b='"+des+"' c='"+price+"' d='"+unit+"' e='"+grptyp+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/>"
	+"<input type='hidden' value='" + reqredefine + "'>"
	+"<input type='hidden' value='" + prodReqAddFlag + "'></div>"
	+"<div class='number1' style='text-align:center;width:18px;margin-left: 5px; display:none;'>" + sl + "</div>"
	+"<div class='minus1'  style='width: 35%; display:none;' onclick='minusClick2(this, 0)' a='"+id+"' b='"+des+"' c='"+price+"' d='"+unit+"' e='"+grptyp+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
	+"</td><td width='5%'></td></tr>";
	
	$("#singleDishMenuTable").append(menudata);
	
	$("#dishDes").text(discription);
	
	$("#dhk4").click();
}

/**隐藏单菜详情**/
function closedc(){
	$("#dancaidiv").remove();
}
/**点菜时点击套餐打开详细页面**/
var vpricetyp = "";//套餐的价格类型  固定价格、汇总价格、高优先价格
function showPackgeDtl(obj){
	//点套餐顺序号
	clickPackageCnt++;
	$("#taocan-table").find("tr").remove();
	var data = "";
	var packageId = $(obj).attr("a");
	var taocanname = $(obj).attr("b");
	var price = $(obj).attr("c");
	var img = $(obj).attr("d");
	var unit = $(obj).attr("e");
	var packagetyp = $(obj).attr("g");
	vpricetyp = $(obj).attr("vpricetyp");//套餐价格类型
	var dishmincnt = 0;
	<c:forEach items='${packageDtl }' var="item">
		if('${item.key}' == packageId) {
			data =  eval('${item.value}');
		}
	</c:forEach>
	var taocanimg = "<img src='"+ img +"' width='100%' height='100%'/>";
	var firmId = $("#firmid").val();
	$("#taocan-name").text(taocanname);
	$("#taocan-money").text("￥"+price+"/"+unit);
	$("#taocan-money").attr("price",price);
	$("#taocan-img").empty().append(taocanimg);
	$("#pk_taocan").val(packageId);
	$("#packagetyp").val(packagetyp);
	//请求数据，进入套餐详细页面
	for(var i=0;i<data.length;i++){
		//循环map，map数据格式{套餐明细主键:[{菜品1},{菜品2}...]}		
		for(var key in data[i]){
			var taocanData = data[i][key];
			var group = 1;
			for(var ii = 0;ii<taocanData.length;ii++){
				var sl = "0";
// 				var backStyle = "bookBackSmall";
				var displayStyle = "display:none;";
				//默认选中的菜品   将加号减号数量  去掉
				var displayStylePlus = "";
// 				if($("#list"+taocanData[ii].pk_package).length > 0){//如果我的菜单里存在这个菜品了，显示的时候加上数量
// 					sl = $("#list"+taocanData[ii].pk_package).children().last().children(".number1").text();
// 					backStyle = "bookBackLarge";
// 					displayStyle = "";
// 				}

				var screenHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
				var plusClickAction = "plusClickPackageDtl(this,\'"+taocanData[ii].pk_parentid+"\')";
				var sellOffHtml = "";
				var plusImage = "<%=path %>/image/wechat/plus_white.png";
				var dishImageHeight = 45 + (screenHeight - 460) / 10;
				var tdHeight = dishImageHeight / 2;
				var sellOffImageWidth = dishImageHeight * 1.2;
				var sellOffImageHeight = dishImageHeight * 0.9;
				var sellOffImage = "<div style='width:90%; height:" + dishImageHeight + "px; border-radius:15px;'>";
				var content = "";
				if(taocanData[ii].hasSellOff == "Y") {
					// 已沽清商品不可点击增加按钮
					plusClickAction = "";
					plusImage = "<%=path %>/image/wechat/plus_disable.png";
					sellOffHtml = "<div style='float:right; position:relative; '><img src='<%=path %>/image/wechat/selloff.png' width='" 
						+ sellOffImageWidth + "px' height='" + sellOffImageHeight + "px'/></div>";
					sellOffImage = "<div style='position:absolute; width:100%; height:" + dishImageHeight + "px; border-radius:15px; opacity:0.5; background-color:#000000;'>";
				}
				var plusRight = "";
				if(screen.width >= 600) {
					plusRight = "style='padding-right:10px;'";
				}
				if(ii==0){//第一行为分组信息
					var tcAllChecked = false;
					content = "<tr style='height:50px;background-color:#fff;font-size:16px;' class='packageDetailGroupInfo'>"+
							"<td colspan='3' style='text-align:left;font-family:Arial;border-bottom: 1px solid #eee;padding-bottom:10px;padding-top: 10px;'>"+((taocanData[ii].vshowinfo==""||taocanData[ii].vshowinfo==null)?taocanData[ii].producttc_order:taocanData[ii].vshowinfo)+
							//"(<span id='"+taocanData[ii].pk_package+"Now' >0</span>/"+taocanData[ii].imaxcount+")"+
							"(可选" + taocanData[ii].imaxcount + "份，已选  <span id='"+taocanData[ii].pk_package+"Now' >0</span>份)"+
							"<input type='hidden' id='"+taocanData[ii].pk_package+"min' value='"+taocanData[ii].imincount+"' />"+
							"<input type='hidden' id='"+taocanData[ii].pk_package+"max' value='"+taocanData[ii].imaxcount+"' />";
							if(taocanData[ii].imincount>0){
								tcAllChecked = false;
							}else{
								tcAllChecked = true;
							}
							content += "<input type='hidden' class='tcAllChecked' value='"+tcAllChecked+"' />"+
							"<div class='packagedtlDiv' style='display:none;'></div>"+
							"</td>"+
							"</tr>";
				}else{//各分组菜品列表
					//菜品列表界面
					//如果没有可换菜且没有必选附加产品、附加项默认选择最小数量的菜品
					sl = taocanData[ii].imincount;
					dishmincnt = taocanData[ii].imincount;//设置菜品的最小数量，当点击减菜按钮时如果当前菜品选择数量小于该值就不再减少了
					if(sl!=0 && taocanData[ii].bchange == "0" && taocanData[ii].prodreqaddflag!="Y" && taocanData[ii].reqredefine!="Y"){
						$("#"+taocanData[ii].pk_parentid+"Now").text(sl);
						$("#"+taocanData[ii].pk_parentid+"max").parent().find(".tcAllChecked").val("true");
						for(var ik=0;ik<parseInt(sl);ik++){
							//本分组的套餐明细
							var pagckagedtlpubitem = taocanData[ii].pk_package;//套餐内分组主键
							var pagckagedtlpdes = taocanData[ii].psname;//菜品名称
							var pagckagedtlprice = taocanData[ii].nprice;//菜品价格
							var pagckagedtlunit =  taocanData[ii].unit;//菜品单位
							var pagckagedtlprice1 = taocanData[ii].price1;//套餐内价格
							var grptyp = "grptyp",pcode="";
							var pagckagedtlpubitempk =taocanData[ii].pk_pubitem;
							var finalPackageCnt = "";
							if($("#div"+pagckagedtlpubitempk+clickPackageCnt+"_redefine").length > 0){
								finalPackageCnt = clickPackageCnt+"_"+(tempCntSeq++);
							}else{
								finalPackageCnt = clickPackageCnt;
							}
							var fun = "showMustAddItem('" + pagckagedtlpubitempk + "','tc','"+taocanData[ii].reqredefine+"','"+taocanData[ii].prodreqaddflag+"','"+finalPackageCnt+"')";
							var appenStr =  "<tr width='100%' height='50px' id='list"+pagckagedtlpubitempk+finalPackageCnt+"' foodsid='"+pagckagedtlpubitempk+"' foodsname='"+pagckagedtlpdes+"' vpricetyp='"+taocanData[ii].vpricetyp+"' price='"+pagckagedtlprice+"' price1='"+pagckagedtlprice1+"' flag='2' unit='" + pagckagedtlunit + "' grptyp='"+grptyp+"' pcode='" + pcode + "'>"
							+"<td style='width:0px;' hidden='hidden'><input type='hidden'/></td>"
							+"<td style='width:16%;' onclick=" + fun + "><div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'><span style='color:#FFF; font-size:13px; line-height:35px'>备注</span></td>"
							+"<td style='width:45%;'><div class='cai11'><span style='font-size:16px;'>" + pagckagedtlpdes + "</span></div>"
							+"<div class='cai22'><span style='color:#FFB400; font-size:15px; font-family:Arial;'>" + pagckagedtlprice + "</span><span style='color:#FFB400; font-size:13px;'>元/" + pagckagedtlunit + "</span></div>"
							+"<div id='divredefienproadd' style='width:100%;text-align:left;display:none'><table id='addItemDataTable"+pagckagedtlpubitempk+finalPackageCnt+"'></table><table id='prodAddDataTable"+pagckagedtlpubitempk+finalPackageCnt+"'></table></div>"
							+"<div id='div" + pagckagedtlpubitempk+finalPackageCnt + "_redefine' style='width:100%;text-align:left;display:none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
							+"<div id='div" + pagckagedtlpubitempk+finalPackageCnt + "_prodadd' style='width:100%;text-align:left;display:none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
							+"<div id='div" + pagckagedtlpubitempk+finalPackageCnt + "' style='width:100%;text-align:left;display;none;word-break:break-all;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
							+"</td><td style='width:39%;'><div class='plus1' style='display:none;' onclick='' vpricetyp='"+taocanData[ii].vpricetyp+"' a='"+pagckagedtlpubitem+"' b='"+pagckagedtlpdes+"' c='"+pagckagedtlprice+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/></div>"
							+"<div class='number1' style='display:;'>1</div>"
							+"<div class='minus1' style='display:none;' onclick='minusClick1(this)' vpricetyp='"+taocanData[ii].vpricetyp+"' a='"+pagckagedtlpubitem+"' b='"+pagckagedtlpdes+"' c='"+pagckagedtlprice+"' d='"+pagckagedtlunit+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
							+"</td></tr>";
							$("#"+taocanData[ii].pk_parentid+"max").parent().find(".packagedtlDiv").append(appenStr);
						}
						displayStylePlus = "display:none;"
					}else{
						displayStylePlus = "";
						sl = "0";
					}
					var nadjustpriceStr = "";
					if(taocanData[ii].nadjustprice>"0" && vpricetyp=="1"){
						nadjustpriceStr = "<span style='color:#F39800;font-size:16px;right:10%;'>另加"+taocanData[ii].nadjustprice+"元</span>";
					}
					
					
					content = "<tr id='"+i+"-"+ii+"' style='height:40px;'><td rowspan='2' style=' border-bottom: 1px solid #eee;width:30%; ' onclick='" 
						+ "' nadjustprice='"+taocanData[ii].nadjustprice+"' a='" + taocanData[ii].pk_package + "' b='" + taocanData[ii].psname 
						+ "' c='" + taocanData[ii].nprice + "' d='<%=Commons.vpiceure %>" + taocanData[ii].wxbigpic + "' e='" + taocanData[ii].unit + "' f='" + taocanData[ii].discription 
						+ "' g='" + taocanData[ii].grptyp + "' h='"+taocanData[ii].reqredefine+"' i='"+taocanData[ii].prodreqaddflag+"' price1='"+taocanData[ii].price1+"'> "
						+ "<div style='position:relative; margin-left:5px; width:95%; height:" + dishImageHeight 
						+ "px; border-radius:5px; background:url(<%=Commons.vpiceure %>" + taocanData[ii].wxsmallpic + "); background-size:100% " + dishImageHeight + "px;'>"
						+ sellOffImage + "</div>" + sellOffHtml + "</div></td>"
						+ "<td class='texttd' colspan='2' style='height:" 
						+ tdHeight + "px; line-height:" + tdHeight + "px;border-bottom:0px;'><div class='cai1'>" + taocanData[ii].psname + "</div>" + "</td>"
						+ "</tr>"
						+ "<tr style='height:45px;'><td style='border-bottom: 1px solid #eee; width:30%;'>"+nadjustpriceStr+"<div class='cai2' style='display:none'><div style='float: left;font-family:Arial; margin-top:0.5px;' >￥</div><div style='float: left;font-family:Arial;'>" + taocanData[ii].nprice + "/" + taocanData[ii].unit+ "</div>" 
						+ "</div></td>"
						+ "<td style='border-bottom: 1px solid #eee; width:55%'><div code='"+i+"-"+ii+"' style='"+displayStylePlus+"' class='plus' " + plusRight + " onclick=\"" + plusClickAction + "\" nadjustprice='"+taocanData[ii].nadjustprice+"' a='"+taocanData[ii].pk_package+"' b='"+taocanData[ii].psname+"' c='"+taocanData[ii].nprice
						+ "' d='"+taocanData[ii].unit + "' e='"+taocanData[ii].grptyp+"' f='"+taocanData[ii].itcode+"' h='"+taocanData[ii].reqredefine+"' i='"+taocanData[ii].prodreqaddflag+"' parentid='"+taocanData[ii].pk_parentid+"' price1='"+taocanData[ii].price1+"' pk_pubitem='"+taocanData[ii].pk_pubitem+"'><img src='" + plusImage + "' width='30px' height='30px'/>" 
						+ "<input type='hidden' value='" + taocanData[ii].reqredefine + "'>"
						+ "<input type='hidden' value='" + taocanData[ii].prodreqaddflag + "'>"
// 						+ "<input type='hidden' id='"+taocanData[ii].pk_package+"min' value='" + taocanData[ii].imincount + "'>"
						+ "<input type='hidden' id='"+taocanData[ii].pk_package+"min' value='" + dishmincnt + "'>"
						+ "<input type='hidden' id='"+taocanData[ii].pk_package+"max' value='" + taocanData[ii].imaxcount + "'></div>"
						+ "<div class='number "+taocanData[ii].pk_parentid+"' id='num_" + taocanData[ii].pk_package + "' style='" + displayStyle + "'>" + sl + "</div>"
						+ "<div class='minus' style='" + displayStyle + "' onclick='minusPackageDtlClick(this,\""+taocanData[ii].pk_parentid+"\")' a='"+taocanData[ii].pk_package+"' b='"+taocanData[ii].psname+"' c='"+taocanData[ii].nprice+"' d='"
						+ taocanData[ii].unit+"' e='"+taocanData[ii].grptyp+"' f='"+taocanData[ii].itcode+"'  pk_pubitem='"+taocanData[ii].pk_pubitem+"'><img src='<%=path %>/image/wechat/minus_white.png' width='30px' height='30px'/></div></td></tr>";
				}
				$("#taocan-table").append(content);
			}
		}
	}

	var pubitem = $(obj).attr("a");
	var pdes = $(obj).attr("b");
	var pcode = $(obj).attr("f");
	var packagetyp = $(obj).attr("g");
	var grptyp = "packageCode";
	if(packagetyp != null && packagetyp!="" && packagetyp!="null"){
		grptyp = packagetyp;
	}
	var finalPackageIdParam = "";
	var fun ="";
	if($("#list"+pubitem).length>0){
		finalPackageIdParam =pubitem + tempPageckSeq;
		fun = "showPackageDtlInfo(this,'"+tempPageckSeq+"')";
	 	$("#taocansequence").val(tempPageckSeq);
		tempPageckSeq++;
	}else{
		finalPackageIdParam = pubitem;
		fun = "showPackageDtlInfo(this,'')";
	}
	//我的点菜单套餐点击记录行
	packageDataTr = "<tr width='100%' height='50px' id='list"+finalPackageIdParam+"' foodsid='"+pubitem+"' foodsname='"+pdes+"' price='"+price+"' flag='1' unit='" + unit + "' grptyp='"+grptyp+"' pcode='" + pcode + "'>"
	+"<td style='width:0px;' hidden='hidden'><input type='hidden'/></td>"
	+"<td style='width:16%;' onclick=" + fun + "><div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'><span style='color:#00FFFF; font-size:13px; line-height:35px'>详情</span></td>"
	+"<td style='width:45%;'><div class='cai11'><span style='font-size:16px;'>" + pdes + "</span></div>"
	+"<div class='cai22'><span style='color:#FFB400; font-size:15px; font-family:Arial;'>" + price + "</span><span style='color:#FFB400; font-size:13px;'>元/" + unit + "</span></div>"
	+"<div id='div" + finalPackageIdParam + "_dtl' style='width:100%;text-align:left;display:none;'></div>"
	+"<div id='div" + finalPackageIdParam + "_redefine' style='width:100%;text-align:left;display:none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + finalPackageIdParam + "_prodadd' style='width:100%;text-align:left;display:none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + finalPackageIdParam + "' style='width:100%;text-align:left;display;none;word-break:break-all;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"</td><td style='width:39%;'><div class='plus1' onclick='' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/></div>"
	+"<div class='number1' style='display:;'>1</div>"
	+"<div class='minus1' style='display:;' onclick='packageMinusClick(this)' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' d='"+unit+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
	+"</td></tr>";
	packagePrice = price;
	$("#showtaocanbtn").click();
}
//点击套餐明细数据加
function plusClickPackageDtl(obj,id){
	//分组点菜总数量
	var groupNowCnt = parseInt($("#"+id+"Now").text());
	//菜品最小数量
	var pubMax =  parseInt($("#"+$(obj).attr("a")+"max").val());
	//判断当前分组点菜数量是否小于分组最大菜品数量
	if(groupNowCnt>=parseInt($("#"+id+"max").val())){
		return;
	}
	$(obj).nextAll().show();
	//当前菜品数量
	var sl = parseInt($(obj).next().text());
	sl += 1;
	//如果菜品数量加1后大于设置的菜品最小数量，返回不处理加菜操作
	if(sl>pubMax){
		return;
	}
	groupNowCnt +=1;
	//判断当前分组菜品数量总和是否大于分组最大菜品数量
	if(groupNowCnt>parseInt($("#"+id+"max").val())){
		return;
	}else if(groupNowCnt >= parseInt($("#"+id+"min").val())){//如果当前分组数量大于等于最小数量
		$("#"+id+"max").parent().css("background-color","white");
		$("#"+id+"max").parent().find(".tcAllChecked").val("true");
	}
	$("#"+id+"Now").text(groupNowCnt);
	$(obj).next().text(sl);	
	//弹出必选附加项、必选附加产品选择框
	var reqredefine=$(obj).attr("h");
	var prodReqAddFlag=$(obj).attr("i");
	if(reqredefine == 'Y' || prodReqAddFlag == 'Y') {
		showMustAddItem($(obj).attr("pk_pubitem"), 'tc', reqredefine, prodReqAddFlag,clickPackageCnt);
	}
	//本分组的套餐明细
	var pubitem = $(obj).attr("a");//套餐内分组主键
	var pdes = $(obj).attr("b");//菜品名称
	//var price = $(obj).attr("c");//菜品价格
	var price;//菜品价格
	var danjia = $(obj).attr("c");//选中菜品价格
	var nadjustprice = $(obj).attr("nadjustprice");//换购加价
 	if(vpricetyp=="1"){//固定价格类型
		price = danjia;
	}else if(vpricetyp=="2"){//汇总价格类型
		price = danjia;
	}else if(vpricetyp=="3"){//高优先价格类型
		var group = $(obj).attr("code").split('-')[0];
		var first = $("#"+group+"-1").find("td").eq(0);
		var pricefirst = parseFloat($(first).attr("c"));//该分组默认菜品的价格  即第一个菜品
		price = pricefirst>parseFloat(danjia)?pricefirst:parseFloat(danjia);//两个价格比较   选较大的。
	}
	var unit = $(obj).attr("d");//菜品单位
	var price1 = $(obj).attr("price1");//套餐内价格
	var grptyp = "grptyp",pcode="";
	var pubitempk =$(obj).attr("pk_pubitem");
	var finalPackageCnt = "";
	if($("#div"+pubitempk+clickPackageCnt+"_redefine").length > 0){
		finalPackageCnt = clickPackageCnt+"_"+(tempCntSeq++);
	}else{
		finalPackageCnt = clickPackageCnt;
	}
	var fun = "showMustAddItem('" + pubitempk + "','tc','"+reqredefine+"','"+prodReqAddFlag+"','"+finalPackageCnt+"')";
	var appenStr =  "<tr width='100%' height='50px' id='list"+pubitempk+finalPackageCnt+"' foodsid='"+pubitempk+"' foodsname='"+pdes+"' nadjustprice='"+nadjustprice+"' price='"+price+"' price1='"+price1+"' flag='2' unit='" + unit + "' grptyp='"+grptyp+"' pcode='" + pcode + "'>"
	+"<td style='width:0px;' hidden='hidden'><input type='hidden'/></td>"
	+"<td style='width:16%;' onclick=" + fun + "><div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'><span style='color:#FFF; font-size:13px; line-height:35px'>备注</span></td>"
	+"<td style='width:45%;'><div class='cai11'><span style='font-size:16px;'>" + pdes + "</span></div>"
	+"<div class='cai22'><span style='color:#FFB400; font-size:15px; font-family:Arial;'>" + price + "</span><span style='color:#FFB400; font-size:13px;'>元/" + unit + "</span></div>"
	+"<div id='divredefienproadd' style='width:100%;text-align:left;display:none'><table id='addItemDataTable"+pubitempk+finalPackageCnt+"'></table><table id='prodAddDataTable"+pubitempk+finalPackageCnt+"'></table></div>"
	+"<div id='div" + pubitempk+finalPackageCnt + "_redefine' style='width:100%;text-align:left;display:none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitempk+finalPackageCnt + "_prodadd' style='width:100%;text-align:left;display:none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitempk+finalPackageCnt + "' style='width:100%;text-align:left;display;none;word-break:break-all;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"</td><td style='width:39%;'><div class='plus1' style='display:none;' onclick='' a='"+pubitem+"' b='"+pdes+"' nadjustprice='"+nadjustprice+"' c='"+price+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/></div>"
	+"<div class='number1' style='display:;'>1</div>"
	+"<div class='minus1' style='display:none;' onclick='minusClick1(this)' nadjustprice='"+nadjustprice+"' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' d='"+unit+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
	+"</td></tr>";
	$("#"+id+"max").parent().find(".packagedtlDiv").append(appenStr);
}
//套餐明细减
function minusPackageDtlClick(obj,id){
	//分组点菜总数量
	var groupNowCnt = parseInt($("#"+id+"Now").text());
	//菜品最小数量
	var pubMin =  parseInt($("#"+$(obj).attr("a")+"min").val());
	//当前菜品数量
	var sl = parseInt($(obj).prev().text());
	sl -= 1;
	//如果菜品数量减1后小于设置的菜品最小数量，返回不处理减菜操作
	if(sl<pubMin){
		return;
	}
	groupNowCnt -= 1;
	$(obj).prev().text(sl);
	$("#"+id+"Now").text(groupNowCnt);
	if(sl == 0){
		$(obj).hide();
		$(obj).prev().hide();
	}
	//如果分组数量小于分组的最小数量
	if(groupNowCnt <parseInt($("#"+id+"min").val())){
		$("#"+id+"min").parent().css("background-color","red");
		$("#"+id+"min").parent().find(".tcAllChecked").val("false");
	}
	//本分组的套餐明细
	$("#"+id+"min").parent().find(".packagedtlDiv").find("tr").each(function(){
		if($(this).attr("foodsid") == $(obj).attr("pk_pubitem")){//找到第一个主键相等的菜品删除
			$(this).remove();
			return false;
		}
	});
}
//显示套餐点菜明细
function showPackageDtlInfo(obj,sequence){
	$("#taocan-table").empty();
// 	$("#taocan-table").find("tr").remove();
	$("#taocanDtlEdit").val("1");//标记为套餐修改状态
	$("#taocansequence").val(sequence);//套餐顺序号
	$("#pk_taocan").val($(obj).parent().attr("foodsid"));
	$("#taocan-name").text($(obj).parent().attr("foodsname"));
	$("#taocan-money").text("￥"+$(obj).parent().attr("price")+"/"+$(obj).parent().attr("unit"));
	$("#taocan-table").append($(obj).parent().find("#div"+$(obj).parent().attr("foodsid")+sequence+"_dtl").html());
	$("#packagetyp").val($(obj).parent().attr("g"));
// 	$(obj).parent().find("#div"+$(obj).parent().attr("foodsid")+sequence+"_dtl").html("");//暂时清空,在套餐详情界面操作完各个菜后重新放回
	$("#showtaocanbtn").click();
	
}
/**返回点菜页面*/
function returnLastPage(){
	if($("#taocanDtlEdit").val()=="1"){
		$("#hidetaocanbtn").click();
		$("#taocanDtlEdit").val("0");
	}else{
		$("#dhk3").click();
	}
}

/*点菜页面中的减菜*/
function minusClick2(obj){
	InitLayer();
	var sl = parseInt($(obj).prev().text());
	
	// 左侧类别栏显示此类别点了多少菜
	var grptyp = $(obj).attr("e");
	if(sl >= 1) {
		showDishNumByItemType(grptyp, "minus");
	}
	
	if(sl > 0) {
		sl -= 1;
		$(obj).prev().text(sl);
		
		var pubitem = $(obj).attr("a");
		$("#num_" + pubitem).text(sl);
		if(sl == 0){
			//$("#num_" + pubitem).parent().attr("class", "bookBackSmall");
			$("#num_" + pubitem).next().hide();
			$("#num_" + pubitem).hide();
			$("#mymenu-table #list"+pubitem).remove();//下个页面table删掉这个菜品
			
			// 删除已选附加项
			deleteMustAddItem(pubitem);
		}
		
		$("#mymenu-table #list"+pubitem).children().last().children(".number1").text(sl);//下个页面table中数量变化
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	}
	
	closeLayer();
}
/*点菜页面中的加菜*/
function plusClick2(obj,flag){
	// 弹出必选附加项选择页面
	var reqredefine = $(obj).find("input").first().val();
	var prodReqAddFlag = $(obj).find("input").eq(1).val();
	var appendDishIdx = "";
	if(reqredefine == 'Y' || prodReqAddFlag == 'Y') {
		dishSeq = parseInt(dishSeq) + 1;
		appendDishIdx = dishSeq;
		clickObj = obj;
		clickFlag = flag;
		clickType = "single";
		clickDishIdx = appendDishIdx;
		showMustAddItem($(obj).attr("a"), 'single', reqredefine, prodReqAddFlag, '', dishSeq);
	} else {
		plusDish2(obj, flag);
	}
}


function plusDish2(obj,flag){
	InitLayer();
	dh1(obj, "singleShoppingCart");//动画效果
	var sl = parseInt($(obj).next().text());
	sl += 1;
	$(obj).next().text(sl);	

	$("#num_" + $(obj).attr("a")).show();
	$("#num_" + $(obj).attr("a")).next().show();
	$("#num_" + $(obj).attr("a")).text(sl);
	//$("#num_" + $(obj).attr("a")).parent().attr("class", "bookBackLarge");
	
	// 左侧类别栏显示此类别点了多少菜
	var grptyp = $(obj).attr("e");
	showDishNumByItemType(grptyp, "add");

	// 弹出必选附加项选择页面
	var reqredefine = $(obj).find("input").first().val();
	var prodReqAddFlag = $(obj).find("input").eq(1).val();
	var appendDishIdx = "";
	
	var pubitem = $(obj).attr("a");
	var pdes = $(obj).attr("b");
	var price = $(obj).attr("c");
	var unit = $(obj).attr("d");
	var pcode = $(obj).attr("f");
	if($("#mymenu-table #list"+pubitem).length > 0 ){//如果存在这个菜品了
		$("#mymenu-table #list"+pubitem).children().last().children(".number1").text(sl);
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
		return;
	}
	//var fun = "showRemark('" + pubitem + "','" + pdes + "')";
	var fun = "showMustAddItem('" + pubitem + "','last','" + reqredefine + "','" + prodReqAddFlag + "', '', '" + appendDishIdx + "')";
	var menudata = "<tr width='100%' height='50px' id='list"+pubitem+appendDishIdx+"' class='list" + pubitem + "' foodsid='"+pubitem+"' foodsname='"+pdes+"' price='"+price+"' flag='"+flag+"' unit='" + unit + "' grptyp='" + grptyp + "' pcode='" + pcode + "' dishidx='" + appendDishIdx + "'>"
	+"<td style='width:0px;' hidden='hidden'><input type='hidden'/></td>"
	+"<td style='width:16%;' onclick=" + fun + "><div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'><span style='color:#FFF; font-size:13px; line-height:35px'>备注</span></td>"
	+"<td style='width:45%;'><div class='cai11'><span style='font-size:16px;'>" + pdes + "</span></div>"
	+"<div class='cai22'><span style='color:#FFB400; font-size:15px; font-family:Arial;'>" + price + "</span><span style='color:#FFB400; font-size:13px;'>元/" + unit + "</span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "_redefine' style='width:100%;text-align:left;display;none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "_prodadd' style='width:100%;text-align:left;display;none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "' style='width:100%;text-align:left;display;none;word-break:break-all;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"</td><td style='width:39%;'><div class='plus1' onclick='plusClick1(this)' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/>"
	+ "<input type='hidden' value='" + reqredefine + "'>"
	+ "<input type='hidden' value='" + prodReqAddFlag + "'></div>"
	+"<div class='number1' style='display:;'>"+sl+"</div>"
	+"<div class='minus1' style='display:;' onclick='minusClick1(this, \"" + appendDishIdx + "\")' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' d='"+unit+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
	+"</td></tr>";
	$("#mymenu-table").append(menudata);
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
}

<%-- 
function plusClick2(obj,flag){
	InitLayer();
	dh1(obj, "singleShoppingCart");//动画效果
	var sl = parseInt($(obj).next().text());
	sl += 1;
	$(obj).next().text(sl);	

	$("#num_" + $(obj).attr("a")).show();
	$("#num_" + $(obj).attr("a")).next().show();
	$("#num_" + $(obj).attr("a")).text(sl);
	//$("#num_" + $(obj).attr("a")).parent().attr("class", "bookBackLarge");
	
	// 左侧类别栏显示此类别点了多少菜
	var grptyp = $(obj).attr("e");
	showDishNumByItemType(grptyp, "add");

	// 弹出必选附加项选择页面
	var reqredefine = $(obj).find("input").first().val();
	var prodReqAddFlag = $(obj).find("input").eq(1).val();
	var appendDishIdx = "";
	if(sl == 1) {
		if(reqredefine == 'Y' || prodReqAddFlag == 'Y') {
			dishSeq = parseInt(dishSeq) + 1;
			appendDishIdx = dishSeq;
			showMustAddItem($(obj).attr("a"), 'single', reqredefine, prodReqAddFlag, '', dishSeq);
		}
	}
	
	var pubitem = $(obj).attr("a");
	var pdes = $(obj).attr("b");
	var price = $(obj).attr("c");
	var unit = $(obj).attr("d");
	var pcode = $(obj).attr("f");
	if($("#mymenu-table #list"+pubitem).length > 0 ){//如果存在这个菜品了
		$("#mymenu-table #list"+pubitem).children().last().children(".number1").text(sl);
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
		return;
	}
	//var fun = "showRemark('" + pubitem + "','" + pdes + "')";
	var fun = "showMustAddItem('" + pubitem + "','last','" + reqredefine + "','" + prodReqAddFlag + "', '', '" + appendDishIdx + "')";
	var menudata = "<tr width='100%' height='50px' id='list"+pubitem+appendDishIdx+"' class='list" + pubitem + "' foodsid='"+pubitem+"' foodsname='"+pdes+"' price='"+price+"' flag='"+flag+"' unit='" + unit + "' grptyp='" + grptyp + "' pcode='" + pcode + "' dishidx='" + appendDishIdx + "'>"
	+"<td style='width:0px;' hidden='hidden'><input type='hidden'/></td>"
	+"<td style='width:16%;' onclick=" + fun + "><div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'><span style='color:#FFF; font-size:13px; line-height:35px'>备注</span></td>"
	+"<td style='width:45%;'><div class='cai11'><span style='font-size:16px;'>" + pdes + "</span></div>"
	+"<div class='cai22'><span style='color:#FFB400; font-size:15px; font-family:Arial;'>" + price + "</span><span style='color:#FFB400; font-size:13px;'>元/" + unit + "</span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "_redefine' style='width:100%;text-align:left;display;none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "_prodadd' style='width:100%;text-align:left;display;none'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"<div id='div" + pubitem + appendDishIdx + "' style='width:100%;text-align:left;display;none;word-break:break-all;'><span style='font-size:12px; color:#A4A4A4'></span></div>"
	+"</td><td style='width:39%;'><div class='plus1' onclick='plusClick1(this)' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/>"
	+ "<input type='hidden' value='" + reqredefine + "'>"
	+ "<input type='hidden' value='" + prodReqAddFlag + "'></div>"
	+"<div class='number1' style='display:;'>"+sl+"</div>"
	+"<div class='minus1' style='display:;' onclick='minusClick1(this, \"" + appendDishIdx + "\")' a='"+pubitem+"' b='"+pdes+"' c='"+price+"' d='"+unit+"' e='"+grptyp+"' f='"+pcode+"'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>"
	+"</td></tr>";
	$("#mymenu-table").append(menudata);
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
} --%>

function changePayType(obj, payType) {
	$("#wxPay").hide();
	$("#afterPay").hide();
	$("#paymentType").val(payType);
	$(obj).find("img").show();
}

// 弹出选择人数窗口
function showSelectPerson() {
	// 默认选择两人
	if($(".popupSelect").length == 0) {
		$("#scrollerPersons").find("tr").eq(2).find("td").addClass("popupSelect");
	}
	$("#selectPersonDiv").popup("open");
}

// 选择人数
function changePersonNum(obj) {
	$("#scrollerPersons .popupSelect").removeClass("popupSelect");
	$(obj).addClass("popupSelect");
}

// 确认选择，将选择的人数回填到人数展示框
function popupComplate() {
	$("#totalPerson").text($("#scrollerPersons .popupSelect").find("span").text());
	$("#selectPersonDiv").popup("close");
}

// 取消下单
function cancelPushOrder() {
	$("#pushOrderOrNot").popup("close");
}

// 下单
function pushOrder() {
	// 加菜时，不校验就餐人数
	if("add" == "${type}") {
		InitLayer();
		saveResvItems("1");
	} else {
		// 校验就餐人数
		var validate1 = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'totalPerson',
				validateType:['intege'],
				param:['F'],
				error:['就餐人数输入错误']
			}]
		});
		if(validate1._submitValidate()){
			InitLayer();
			saveResvItems("1");
		}
	}
}
</script>
<title>自助点餐</title>
</head>
<body>
<!-- ---------------------------------------在线点餐----------------------------------------------------------------------- -->
<div data-role="page" id="menupage" class="page">
	<div id="searchdiv" style="display:none;">
		<div id="searchdivtop">
			<div id="search-left"><input type="search" id="searchtext" placeholder="搜索菜品..." /></div>
			<div id="search-right">
				<%-- <img id="searchbtn" src="<%=path %>/image/return.png" width="100%" height="60%"/> --%>
				<a id="searchbtn" href="#" data-role="button" style="display:inline-block; padding:0.5em 0.8em;">返回</a>
			</div>
		</div>
	</div>
	<!-- <div style="width:100%;height:12px;background:url(<%=path%>/image/touying.png) repeat-x"></div> -->
	<div id="menumain" class="menumain" style="width:100%; height:92%; overflow:auto; padding-bottom:-1px; -webkit-overflow-scrolling:touch;">
		<!-- <div id="menu-left" style="background:url(<%=path%>/image/wechat/classBack.jpg) no-repeat; background-size:100% 100%; "> -->
		<div id="menu-left">
		  <!-- <div style="width:100%; height:100%; background-color:#000000; opacity:0.7;"> -->
			<div id="gotoSearch"><img src="<%=path%>/image/search.png" width="33px" height="30px"/></div>
			<c:forEach items="${listItemType }" var="type">
				<div id="${type.id }" class="leftItemType" itemCode="${type.code }"><span style="margin-top:5px;display:block;font-size:80%;line-height:15px;">${type.name }</span>
					<div id="itemType_${type.code }" class="itemNumDiv" style="display:none">0</div>
				</div>
			</c:forEach>
			<c:if test="${count ne null && count ne '' }">
				<div id="packageDiv" class="leftItemType" itemcode="packageCode"><span  style="font-size:80%;">${count }</span>
					<div id="itemType_packageCode" class="itemNumDiv" style="display:none">0</div>
				</div>
			</c:if>
		  <!-- </div> -->
		</div>
			<!-- 
			<table id="list-table">
				<c:forEach items="${listItemType }" var="type">
					<tr><td class="${type.id }"  style="background-color:red">${type.name }</td></tr>
				</c:forEach>
			</table>
			<br/>
			<br/>
			<div style="background-color:#535353; opacity:0.5;"></div>
			 -->
		<div id="menu-right">
			<table id="menu-table" width="100%;" style="overflow:hidden;position:relative;">
				<div style="width:100%; height:10px;"></div>
			</table>
			<br/>
			<br/>
		</div>
	</div>
	
	<div class="bottDiv" style="width:100%; height:8%; background-color:#272727; padding-top:1px;" >
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:-1px;">
			<tr>
			<!--  
				<td class="ui-a">
					<a href="<c:url value='/dining/gotoFirm.do?pk_group=${pk_group}&firmid=${orders.firmid }&openid=${orders.openid }' />" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
			-->
				<td class="ui-a">
					<c:if test="${pageFrom eq 'takeout' }">
						<a href="<c:url value='/dining/listFirmFromCity.do?openid=${orders.openid}&pk_group=${pk_group}'/>" data-ajax="false">
							<img align="middle" src="<c:url value='/image/wechat/whiteBack.png'/>"/>
						</a>
					</c:if>
				</td>
				<td id="shoppingCart" class="ui-b">
					<div class="cartDiv" onclick="JavaScript:showcartmenu('')">
						<span id="menu-count1" class="cartNumDiv">0</span>
					</div>
				</td>
				<td class="ui-c">
					<span style="vertical-align: bottom; font-size:150%;">￥</span>
					<span id="menu-price1" class="totalPrice" style="font-size:150%;">0.00</span>
				</td>
				<td id="mymenudiv-right-b" class="ui-d">
					<span>选好了</span>
				</td>
			</tr>
		</table>
  	</div>
	<a id="dhk" href="#pagetwo" data-rel="dialog" data-transition="pop"></a> <!-- 没有选择菜品弹出框 -->
	<a id="dhk2" href="#mymenu" data-transition="slideup"></a>               <!-- 进入我的菜单 -->
	<a id="dhk3" href="#menupage" data-transition="slideup"></a>             <!-- 进入菜单页面 -->
	<a id="dhk4" href="#singleDish" data-transition="slideup"></a>           <!-- 进入单个菜品 -->
	<a id="dhk5" href="#loadImage" data-transition="slideup"></a>            <!-- 进入图片页面 -->

	<!-- -----------------------------------------------点击购物车显示已点菜品------------------------------------------------------ -->
	<a href="#cartmenu" id="showcartmenu" data-rel="popup" data-position-to="window"></a>
	<div id="cartmenu" data-role="popup" style="position:fixed; bottom:8%; width:100%; left:0px;height:70%;" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
<!-- 		<div class="downArrow"></div> -->
		<div id="fixedDivCartMenu" style="overflow:auto;position:relative;z-index:1100;height:88%;float:left;width:100%;">
			<table id="cartmenutable" width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="border-collapse:collapse;">
			</table>
		</div>
		<div style="position:relative;z-index:1100;height:10%;float:left;width:100%;">
			<table width="90%" height="10%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="popBtnCol">
						<%-- <input type="button" value="<fmt:message key='btnCancel' />" data-role='none' data-ajax="false" onclick="javaScript:cancelNumber()" /> --%> 
						<input type="button" value="关闭" data-role='none' data-ajax="false" onclick="javaScript:closeCartMenu('');" /> 
					</td>
				</tr>
			</table>
		</div>
	</div>

	<!-- -----------------------------------------------必须附加项------------------------------------------------------ -->
	<a href="#mustadditem" id="showmustadditem" data-rel="popup" data-position-to="window"></a>
	<div id="mustadditem" data-role="popup" style="position:fixed; bottom:8%; width:100%; left:0px;height:70%;" data-overlay-theme="b" data-theme="a"  
			data-shadow="false" data-position-to="origin" data-corners="false" data-swipe-close="false" data-history="false">
		<div class="downArrow"></div>
		<div id="fixedDivMustAddItem" style="position:relative;z-index:1100;height:98%;float:left;width:100%;">
			<table id="mustadditemtable" width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="border-collapse:collapse;">
			</table>
		</div>
	</div>
</div>
	
<!-- -----------------------------------------------保存必选附加项------------------------------------------------------ -->
<div data-role="page" id="pageadditem" data-overlay-theme="e">
	<div style="display:none">
		<table id="addItemDataTable">
			<c:forEach items="${netDishAddItemMap }" var="data">
				<tr id="${data.key }" dishidx="${data.value[0].seq }">
				<c:forEach items="${data.value }" var="item">
					<td>
						<input type="hidden" value="${item.pk_prodcutReqAttAc }">
						<input type="hidden" value="${item.pk_redefine }">
						<input type="hidden" value="${item.pk_pubItem }">
						<input type="hidden" value="${item.nprice }">
						<input type="hidden" value="${item.redefineName }">
						<input type="hidden" value="${item.seq }">
					</td>
				</c:forEach>
			</c:forEach>
		</table>
	</div>
</div>
	
<!-- -----------------------------------------------保存附加产品------------------------------------------------------ -->
<div data-role="page" id="pageprodadd" data-overlay-theme="e">
	<div style="display:none">
		<table id="prodAddDataTable">
			<c:forEach items="${netDishProdAddMap }" var="data">
				<tr id="${data.key }" dishidx="${data.value[0].seq }">
				<c:forEach items="${data.value }" var="item">
					<td>
						<input type="hidden" value="${item.pk_prodReqAdd }">
						<input type="hidden" value="${item.pk_prodAdd }">
						<input type="hidden" value="${item.pk_pubitem }">
						<input type="hidden" value="${item.nprice }">
						<input type="hidden" value="${item.prodAddName }">
						<input type="hidden" value="${item.seq }">
					</td>
				</c:forEach>
			</c:forEach>
		</table>
	</div>
</div>

<!----------------------------------------------- 没有选择菜品弹出框 -------------------------------------------------->
<div data-role="page" id="pagetwo" data-overlay-theme="e">
  <div data-role="header">
    <h1>温馨提示</h1>
  </div>
  <div data-role="content">
    <p id="pop"></p>
    <a href="#menupage" data-role="button" data-transition="slide">确定</a>
  </div>
</div> 





<!------------------------------------------------ 单个菜品 --------------------------------------------------------->
<div data-role="page" id="singleDish" class="page"  style="margin-top:0;list-style:none;padding:0;">
	<div style="width:100%; height:92%; padding-bottom:-1px;">
		<div id="singleDishImg"></div>
		<div id="singleDishMenu">
			<table id="singleDishMenuTable" cellspacing="0" style="width:100%; height:50%;">
			</table>
		</div>
		<!-- <div class="space">&nbsp;</div>
		<div id="singleDishCamp" style="padding-left:9%; border-top:1px solid #ccc; padding-top:10px;">参与会员折扣</div> -->
		<div class="space">&nbsp;</div>
		<div id="singleDishInfo" style="width:100%; border-top:1px solid #ccc; padding-top:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td width="5%"></td>
					<td class="detail1">菜品介绍:&nbsp;&nbsp;<span id="dishDes"></span></td>
				</tr>
			</table>
		</div>
	</div>
	
	<div class="commitSingleDiv" style="height:8%; background-color:#272727; padding-top:1px;" >
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:-1px;">
			<tr>
				<td class="ui-a">
					<a href="javascript:returnLastPage();" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
				<td id="singleShoppingCart" class="ui-b">
					<div class="cartDiv" onclick="JavaScript:showcartmenu('single')">
						<span id="menu-count4" class="cartNumDiv">0</span>
					</div>
				</td>
				<td class="ui-c">
					<span style="font-size:150%;">￥</span>
					<span id="menu-price4" class="totalPrice" style="font-size:150%;">0.00</span>
				</td>
				<td id="commitFromSingle" class="ui-d">
					<span>继续点菜</span>
				</td>
			</tr>
		</table>
	</div>

	<!-- -----------------------------------------------必须附加项------------------------------------------------------ -->
	<a href="#mustadditemsingle" id="showmustadditemsingle" data-rel="popup" data-position-to="window"></a>
	<div id="mustadditemsingle" data-role="popup" style="position:fixed; bottom:0px; width:100%; left:0px;height:70%;" data-overlay-theme="b" data-theme="a"  
			data-shadow="false" data-position-to="origin" data-corners="false" data-swipe-close="false" data-dismissible="false" data-history="false">
		<div class="downArrow"></div>
		<div id="fixedDivMustAddSingle" style="position:relative;z-index:1100;height:98%;float:left;width:100%;">
			<table id="mustadditemtablesingle" width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="border-collapse:collapse;">
			</table>
		</div>
	</div>
	<a href="#cartmenusingle" id="showcartmenusingle" data-rel="popup" data-position-to="window"></a>
	<div id="cartmenusingle" data-role="popup" style="position:fixed; bottom:8%; width:100%; left:0px;height:70%;" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
		<div id="fixedDivCartMenusingle" style="position:relative;z-index:1100;height:88%;float:left;width:100%;">
			<table id="cartmenutablesingle" width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="border-collapse:collapse;">
			</table>
		</div>
		<div style="position:relative;z-index:1100;height:10%;float:left;width:100%;">
			<table width="90%" height="10%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="popBtnCol">
						<%-- <input type="button" value="<fmt:message key='btnCancel' />" data-role='none' data-ajax="false" onclick="javaScript:cancelNumber()" /> --%> 
						<input type="button" value="关闭" data-role='none' data-ajax="false" onclick="javaScript:closeCartMenu('single');" /> 
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>

<!-- -----------------------------------------------套餐页面------------------------------------------------------ -->
<a href="#taocan" id="showtaocanbtn"></a><!-- 套餐详情 -->
<a href="#mymenu" id="hidetaocanbtn"></a><!-- 隐藏套餐详情 -->
<div data-role="page" id="taocan" class="page">
	<div id="taocan-main" class="main" style="width:100%;  height: 92%;-webkit-overflow-scrolling:touch;overflow-x:hidden; overflow-y: overlay;">
		<div id="taocan-top" class="top" style="background-color:#FFB400;">套餐详情</div>
		<div id="taocan-img" style="height:150px;"></div>
		<div style="width:100%; padding-top:10px; padding-bottom:10px; border-radius:5px; color:#656565;  font-size: 25px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background-color:#FFFFFF;">
				<tfoot>
					<tr>
						<td><span id="taocan-name" class='cai1'>0</span>
							<input type="hidden" id="pk_taocan" />
							<input type="hidden" id="packagetyp" />
							<input type="hidden" id="taocanDtlEdit" value="0" />
							<input type="hidden" id="taocansequence" value="" />
						</td>
						<td style="text-align: right;font-family: Arial;width: 25%;"  class='cai1'><span id="taocan-money" style="color:red;">0</span></td>
					</tr>
				</tfoot>
			</table>
		</div>
		<div style="width:100%; padding-top:10px; padding-bottom:10px; border-radius:5px; color:#656565;">
			<table id="taocan-table" width="100%" border="0" cellspacing="0" cellpadding="0" style='border:0px;'></table>
		</div>
	</div>
	<div class="committcDiv" style="height:8%; background-color:#272727; padding-top:1px;" >
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:-1px;">
			<tr>
				<td class="ui-a">
					<a href="javascript:returnLastPage();" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>&nbsp;
					</a>
				</td>
				<td id="commitFromPackage" class="ui-d"  style="width:10%;">
					<span>继续点菜</span>
				</td>
			</tr>
		</table>
	</div>
	<!-- -----------------------------------------------必须附加项------------------------------------------------------ -->
	<a href="#mustadditemtc" id="showmustadditemtc" data-rel="popup" data-position-to="window"></a>
<!-- 	<div id="mustadditemtc" data-role="popup" style="position:fixed; bottom:0px; width:100%; left:0px;height:70%;overflow: auto;-webkit-overflow-scrolling:touch;" data-overlay-theme="b" data-theme="a"   -->
	<div id="mustadditemtc" data-role="popup" style="position:fixed; bottom:0px; width:100%; left:0px;height:70%;overflow: auto;overflow-y: overlay;" data-overlay-theme="b" data-theme="a"  
			data-shadow="false" data-position-to="origin" data-corners="false" data-swipe-close="false" data-dismissible="false" data-history="false">
		<div class="downArrow"></div>
		<div id="fixedDivMustAddtc" style="position:relative;z-index:1100;height:98%;float:left;width:100%;">
			<table id="mustadditemtabletc" width="95%" border="0" cellspacing="0" cellpadding="0" align="center" style="border-collapse:collapse;">
			</table>
		</div>
	</div>
</div>
<!------------------------------------------------ 我的菜单 --------------------------------------------------------->
<div data-role="page" id="mymenu" class="page"  style="margin-top:0;list-style:none;padding:0;">
	<div style="width:100%; height:92%; overflow-y:auto; padding-bottom:-1px; background-color:#FFFFFF; -webkit-overflow-scrolling:touch;">
		<div class="header">
			<div class="space">&nbsp;</div>
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right" style="width: 75%">
					<span class="home" style="font-size:110%;font-weight: bolder;">${firm.firmdes}</span>
				</div>
				<c:if test="${firm.addr ne null && firm.addr ne '' }">
					<div onclick="alertMsg('${firm.addr}');" style="float:left;width:10%;margin-top: -4px;">
						<img src="<c:url value='/image/wechat/local.png'/>"/>
					</div>
				</c:if>
				<c:if test="${firm.tele ne null && firm.tele ne '' }">
					<div style="float:left;width:10%;margin-top: -4px;">
						<a href="tel:${firm.tele }"><img style="width:30px;height:30px;" src="<c:url value='/image/wechat/dianhua.png'/>"/></a>
					</div>
				</c:if>
			</div>
			<div class="space">&nbsp;</div>
		</div>
		<!-- 
		<div class="orderBack">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span>订单信息</span></div>
			</div>
		</div>
		 -->
		<div class="orderContent">
			<table width="100%" height="50px" border="0" cellspacing="0" cellpadding="0" align="center" style="display:none;">
				<tr onclick="javascript:showSelectPerson();">
					<td class="td_left">就餐人数</td>
					<td class="orderMoney" style="color:#000000">
						<span id="personNum">2</span>
						<div style="float:right;">
							<img src="<c:url value='/image/wechat/r.png'/>" width="20px" height="20px" />
						</div>
					</td>
				</tr>
			</table>
			<div id="selectPersonDiv" class="mypopup" data-role="popup" data-corners="false" data-shadow="false" data-overlay-theme="b" data-theme="a" data-history="false"><!-- 选择人数 -->
            	<div class="popupTitle">
               		<fmt:message key='personNumber' />
                </div>
        		<div id="wrapperPersons" class="mypopupScroller">
					<table id="scrollerPersons" style="width:100%;">
                    	<tr><td>&nbsp;</td></tr>
						<c:forEach begin="1" end="16" step="1" varStatus="sta">
							<tr>
								<td class="popupNoSelect" onclick="changePersonNum(this);">
									<span>${sta.index}</span><fmt:message key='personUnit' />
								</td>
							</tr>
						</c:forEach>
                        <tr><td>&nbsp;</td></tr>
					</table>
				</div><!-- wrapper -->
				<div class="popupComplate" onclick="popupComplate();">
                   	<fmt:message key='btnOK' />
                </div>
        	</div><!-- person -->
		</div>
		<div class="orderContent" style="background-color: #F7F7F7;">
			<table width="100%" height="50px" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="orderMoney" style="padding-right: 10px;">
						<div style="float: left;">
							<span style="font-size:100%; color:#000000; padding-left: 20px;">已点菜单</span>
						</div>
						<div style="float: left;margin-top: -5px;float: right;">
							<span style="font-size:100%; color:#000000;">￥</span>
							<span id="menu-price3" style="font-size:150%; font-family:Arial;">0.00</span>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<!-- 选择支付方式 -->
		<!-- 
		<div class="orderBack">
			<div class="space">&nbsp;</div>
			<div class="father_div">
				<div class="space-left">&nbsp;</div>
				<div class="store_right"><span>支付方式</span></div>
			</div>
		</div>
		<div class="orderContent">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr onclick="changePayType(this, '1')">
					<td class="td_left">
						<p>
							<span class="nonsupport">微信支付</span><br />
							<span class="message">使用微信支付</span>
						</p>
					</td>
					<td class="pay-right">
						<img id="wxPay" style="display:none;" src="<c:url value='/image/wechat/right.png'/>"/>
					</td>
				</tr>
				<tr onclick="changePayType(this, '2')">
					<td class="td_left" style="border-top:1px dashed gray">
						<p>
							<span class="nonsupport">现场支付</span><br />
						</p>
					</td>
					<td class="pay-right" style="border-top:1px dashed gray">
						<img id="afterPay" src="<c:url value='/image/wechat/right.png'/>"/>
					</td>
				</tr>
			</table>
		</div>
		 -->
		<div class="orderContent" style="overflow-y:auto;">
			<table id="mymenu-table">
				<c:forEach items="${orders.listNetOrderDtl }" var="food" varStatus="idx">
					<tr width='100%' height='50px' id='list${food.foodsid }${food.tcseq}${food.seq}' foodsid='${food.foodsid }' foodsname='${food.foodsname }' 
							price='${food.price }' flag='${food.ispackage }' unit='份' grptyp='${food.grptyp }' dishidx='${food.seq}' class='list${food.foodsid }'>
						<td style='width:0px;' hidden='hidden'><input type='hidden' value='${food.remark }'/></td>
						<c:choose>
							<c:when test="${food.ispackage == '0' }">
								<td style='width:16%' onclick='showMustAddItem("${food.foodsid }", "last", "${food.reqredefine }", "${food.prodReqAddFlag }", "", "${food.seq }")'>
									<div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'>
										<span style='color:#FFF; font-size:13px; line-height:35px'>备注</span>
									</div>
								</td>
							</c:when>
							<c:otherwise>
								<td style='width:16%' onclick="showPackageDtlInfo(this,'${food.tcseq}')">
									<div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'>
										<span style='color:#00FFFF; font-size:13px; line-height:35px'>详情</span>
									</div>
								</td>
							</c:otherwise>
						</c:choose>
						<td style='width:45%;'>
							<div class='cai11'><span style='font-size:16px;'>${food.foodsname }</span></div>
							<div class='cai22'>
								<span style='color:#FFB400; font-size:15px; font-family:Arial;'>${food.price }</span>
								<span style='color:#FFB400; font-size:13px;'>元/份</span>
							</div>
							<!-- 套餐明细 -->
							<c:choose>
								<c:when test="${fn:length(food.orderPackageDetailList) > 0 }">
									<div id='div${food.foodsid }${food.tcseq}_dtl' style='width:100%;text-align:left;display:none;'>
										<table style="width:100%;">
										<c:forEach items="${food.orderPackageDetailList }" var="packageDtl">
											<tr width='100%' height='50px' id='list${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}' foodsid='${packageDtl.pk_pubitem}' foodsname='${packageDtl.vname}' price='${packageDtl.nprice1}' price1='${packageDtl.nprice}' flag='2' unit='${packageDtl.unit}' grptyp='grptyp' pcode='${packageDtl.vpcode}'>
												<td style='width:0px;' hidden='hidden'><input type='hidden' value="${packageDtl.vremark}"/></td>
												<td style='width:16%;' onclick="showMustAddItem('${packageDtl.pk_pubitem}','tc','${packageDtl.reqredefine}','${packageDtl.prodReqAddFlag}','${packageDtl.tcPubitemSeq}')"><div style='background-color:#FF0000; width:35px; height:35px; border:1px solid #FFB400; border-radius:5px;'><span style='color:#FFF; font-size:13px; line-height:35px'>备注</span></td>
												<td style='width:45%;'><div class='cai11'><span style='font-size:16px;'>${packageDtl.vname}</span></div>
													<div class='cai22'><span style='color:#FFB400; font-size:15px; font-family:Arial;'>${packageDtl.nprice1}</span><span style='color:#FFB400; font-size:13px;'>元/${packageDtl.unit}</span></div>
													<div id='divredefienproadd' style='width:100%;text-align:left;display:none'>
														<table id='addItemDataTable${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}'>
															<c:forEach var="dishAddItemMust" items="${packageDtl.mapDishAddItemMust }">
																<tr id="additem_${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}_${dishAddItemMust.key}">
																	<c:forEach var="dishAddItemMustValue" items="${dishAddItemMust.value }">
																		<td>
																			<input type="hidden" value="${dishAddItemMustValue.pk_prodcutReqAttAc }">
																			<input type="hidden" value="${dishAddItemMustValue.pk_redefine }">
																			<input type="hidden" value="${dishAddItemMustValue.pk_pubItem }${packageDtl.tcPubitemSeq}">
																			<input type="hidden" value="${dishAddItemMustValue.nprice }">
																			<input type="hidden" value="${dishAddItemMustValue.redefineName }">
																		</td>
																	</c:forEach>
																</tr>
															</c:forEach>
															<tr id="additem_${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}_canSelect">
																<c:forEach var="dishAddItemCanselect" items="${packageDtl.listDishAddItemCanselect }">
																	<td>
																		<input type="hidden" value="${dishAddItemCanselect.pk_prodcutReqAttAc }">
																		<input type="hidden" value="${dishAddItemCanselect.pk_redefine }">
																		<input type="hidden" value="${dishAddItemCanselect.pk_pubItem }${packageDtl.tcPubitemSeq}">
																		<input type="hidden" value="${dishAddItemCanselect.nprice }">
																		<input type="hidden" value="${dishAddItemCanselect.redefineName }">
																	</td>
																</c:forEach>
															</tr>
														</table>
														<table id='prodAddDataTable${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}'>
															<c:forEach var="dishProdAddMust" items="${packageDtl.mapDishProdAddMust }">
																<tr id="prodadd_${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}_${dishProdAddMust.key}">
																	<c:forEach var="dishProdAddMustValue" items="${dishProdAddMust.value }">
																		<td>
																			<input type="hidden" value="${dishProdAddMustValue.pk_prodReqAdd }">
																			<input type="hidden" value="${dishProdAddMustValue.pk_prodAdd }">
																			<input type="hidden" value="${dishProdAddMustValue.pk_pubitem }${packageDtl.tcPubitemSeq}">
																			<input type="hidden" value="${dishProdAddMustValue.nprice }">
																			<input type="hidden" value="${dishProdAddMustValue.prodAddName }">
																		</td>
																	</c:forEach>
																</tr>
															</c:forEach>
															<tr id="prodadd_${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}_canSelect">
																<c:forEach var="dishProdAddCanselect" items="${packageDtl.listDishProdAddCanselect }">
																	<td>
																		<input type="hidden" value="${dishProdAddCanselect.pk_prodReqAdd }">
																		<input type="hidden" value="${dishProdAddCanselect.pk_prodAdd }">
																		<input type="hidden" value="${dishProdAddCanselect.pk_pubitem }${packageDtl.tcPubitemSeq}">
																		<input type="hidden" value="${dishProdAddCanselect.nprice }">
																		<input type="hidden" value="${dishProdAddCanselect.prodAddName }">
																	</td>
																</c:forEach>
															</tr>
														</table>
													</div>
													<div id='div${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}_redefine' style='width:100%;text-align:left;<c:if test="${packageDtl.netDishAddItemtitle ne null && packageDtl.netDishAddItemtitle ne '' }">display;none;</c:if>'>
														<span style='font-size:12px; color:#A4A4A4'><c:if test="${packageDtl.netDishAddItemtitle ne null && packageDtl.netDishAddItemtitle ne '' }">附加项：</c:if>${packageDtl.netDishAddItemtitle }</span>
													</div>
													<div id='div${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}_prodadd' style='width:100%;text-align:left;<c:if test="${packageDtl.netDishProdAddtitle ne null && packageDtl.netDishProdAddtitle ne '' }">display;none;</c:if>'>
														<span style='font-size:12px; color:#A4A4A4'><c:if test="${packageDtl.netDishProdAddtitle ne null && packageDtl.netDishProdAddtitle ne '' }">附加产品：</c:if>${packageDtl.netDishProdAddtitle }</span>
													</div>
													<div id='div${packageDtl.pk_pubitem}${packageDtl.tcPubitemSeq}' style='width:100%;text-align:left;<c:if test="${packageDtl.vremark ne null && packageDtl.vremark ne '' }">display;none;</c:if>word-break:break-all;'>
														<span style='font-size:12px; color:#A4A4A4'><c:if test="${packageDtl.vremark ne null && packageDtl.vremark ne '' }">备注:</c:if>${packageDtl.vremark }</span>
													</div>
												</td>
												<td style='width:39%;'>
													<div class='plus1' style='display:none;' onclick='' a='${packageDtl.pk_pubitem}' b='${packageDtl.vname}' c='${packageDtl.nprice}' e='grptyp' f='${packageDtl.vpcode}'><img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/></div>
													<div class='number1' style='display:;'>1</div>
													<div class='minus1' style='display:none;' onclick='minusClick1(this)' a='${packageDtl.pk_pubitem}' b='${packageDtl.vname}' c='${packageDtl.nprice}' e='grptyp' f='${packageDtl.vpcode}'><img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>
												</td>
											</tr>
										</c:forEach>
										</table>
									</div>
								</c:when>
								<c:otherwise>
									<div id='div${food.foodsid }${food.tcseq}_dtl' style='width:100%;text-align:left;display:none;'>
									</div>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${fn:length(food.listDishProdAdd) > 0 }">
									<div id='div${food.foodsid }${food.tcseq}${food.seq}_prodadd' style='width:100%;text-align:left;'>
										<span style='font-size:12px; color:#A4A4A4'>附加产品:
											<c:forEach items="${food.listDishProdAdd }" var="addItem">
												${addItem.prodAddName }
												<c:if test="${addItem.nprice > 0.0 }">
													<span style="color:red">(${addItem.nprice }元)</span>
												</c:if>
												&nbsp;
											</c:forEach>
										</span>
									</div>
								</c:when>
								<c:otherwise>
									<div id='div${food.foodsid }${food.tcseq}${food.seq}_prodadd' style='width:100%;text-align:left;display;none'>
										<span style='font-size:12px; color:#A4A4A4'></span>
									</div>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${fn:length(food.listDishAddItem) > 0 }">
									<div id='div${food.foodsid }${food.tcseq}${food.seq}_redefine' style='width:100%;text-align:left;'>
										<span style='font-size:12px; color:#A4A4A4'>附加项:
											<c:forEach items="${food.listDishAddItem }" var="addItem">
												${addItem.redefineName }
												<c:if test="${addItem.nprice > 0.0 }">
													<span style="color:red">(${addItem.nprice }元)</span>
												</c:if>
												&nbsp;
											</c:forEach>
										</span>
									</div>
								</c:when>
								<c:otherwise>
									<div id='div${food.foodsid }${food.tcseq}${food.seq}_redefine' style='width:100%;text-align:left;display;none'>
										<span style='font-size:12px; color:#A4A4A4'></span>
									</div>
								</c:otherwise>
							</c:choose>
							<div id='div${food.foodsid }${food.tcseq}${food.seq}' style='width:100%;text-align:left;display;none'>
								<span style='font-size:12px; color:#A4A4A4'>
									<c:if test="${food.remark ne null && food.remark ne '' }">
										备注：${food.remark }
									</c:if>
								</span>
							</div>
						</td>
						<td style='width:39%;'>
							<div class='plus1'  onclick='plusClick1(this)' a='${food.foodsid }' b='${food.foodsname }' c='${food.price }' d='份' e='${food.grptyp }'>
								<img src='<%=path %>/image/wechat/plus.png' width='45px' height='45px'/>
								<input type="hidden" value="${food.reqredefine }">
								<input type="hidden" value="${food.prodReqAddFlag }">
							</div>
							<div class='number1' style='display:;'>${food.foodnum }</div>
							<div class='minus1' style='display:;' onclick='minusClick1(this, "${food.seq}")' a='${food.foodsid }' b='${food.foodsname }' c='${food.price }' d='份' e='${food.grptyp }'>
								<img src='<%=path %>/image/wechat/minus.png' width='45px' height='45px'/></div>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div id="orderRemark" class="orderContent" style="background-color:#F7F7F7">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td>
						<textarea id="remark" maxlength="200" data-role="none" rows="4" placeholder="还要给商家交代点什么吗？(最多输入200字)" style="margin-top:10px; background-color: #FFFFFF;border:1px solid #EEEEEE;border-radius:4px;width:100%;color:#717171;"></textarea>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<!-- 选择支付方式 -->
	
	<!-- <div id="payment" >
		 <legend>支付方式:</legend> 
		<input type="radio" name="paymentname" id="payment1"  value="1" checked="checked"/><label for="payment1">微支付</label>
		<input type="radio" name="paymentname" id="payment2"  value="2" /><label for="payment2">现场付</label>
	</div>
	<br/>
	<br/> -->
	<!-- ----------------------------- -->
	<div class="commitdiv" style="height:8%; background-color:#272727; padding-top:1px;" >
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:-1px;">
			<tr>
				<td class="ui-a">
					<a href="javascript:returnLastPage();" data-ajax="false">
						<img src="<c:url value='/image/wechat/whiteBack.png'/>"/>
					</a>
				</td>
				<td class="ui-b">
					<div class="cartDiv">
						<span id="menu-count2" class="cartNumDiv">0</span>
					</div>
				</td>
				<td class="ui-c">
					<span  style="vertical-align: bottom; font-size:150%;">￥</span>
					<span id="menu-price2" class="totalPrice" style="font-size:150%;">0.00</span>
				</td>
				<td id="commitBtnDiv" class="ui-d">
					<span>保存菜单</span>
				</td>
			</tr>
		</table>
	</div>
<!-- ---------菜品单个备注-------- -->
<a href="#remarmemo" id="showremarmemo" data-rel="popup" data-position-to="window"></a>
<div data-role="popup" id="remarmemo" style="position:fixed; top:20%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
	<input type="hidden" />
	<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr height="50px">
			<td align="center" style="font-size:120%"></td>
		</tr>
		<tr height="50px">
			<td>
				<textarea id="remarmemo11bak" maxlength="100" data-role="none" rows="2" placeholder="您对当前菜品还有什么需求，请在此备注..." style="margin:5px auto;background-color: #FFFFFF;border:1px solid #EEEEEE;border-radius:4px;width:100%;color:#717171;"></textarea>
			</td>
		</tr>
		<tr height="50px">
			<td class="detail2">
				<input type="button" value="取消" data-role='none' data-ajax="false" onclick="cancelRemarmemo()" /> 
				<input type="button" value="确认" data-role='none' data-ajax="false" onclick="commitRemarmemo()" /> 
			</td>
		</tr>
	</table>
</div>

	<!-- -----------------------------------------------必须附加项------------------------------------------------------ -->
	<a href="#mustadditemlast" id="showmustadditemlast" data-rel="popup" data-position-to="window"></a>
	<div id="mustadditemlast" data-role="popup" style="position:fixed; bottom:0px; width:100%; left:0px;height:70%;overflow:hidden;" data-overlay-theme="b" data-theme="a"  
			data-shadow="false" data-position-to="origin" data-corners="false"  data-history="false">
		<div class="downArrow"></div>
		<div id="fixedDivMustAddLast" style="height:99%; overflow:auto; padding-top:5px;">
			<div id="mustadditemtablelast" style="border-collapse:collapse;width:90%;height:100%;padding-left:5%;">
			</div>
		</div>
	</div>

	<!-- -----------------------------------------------是否已到店------------------------------------------------------ -->
	<a href="#inStoreOrNot" id="showInStoreOrNot" data-rel="popup" data-position-to="window"></a>
	<div data-role="popup" id="inStoreOrNot" style="position:fixed; top:20%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  
		data-shadow="false" data-position-to="origin" data-corners="false" data-swipe-close="false" data-dismissible="false" data-history="false">
		<input type="hidden" />
		<input type="hidden" />
		<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr height="75px">
				<td align="center">
					<span style="font-size:120%">是否已到店</span>
					<br />
					<span style="color:#999999">如果您已到店，请扫码下单！</span>
				</td>
			</tr>
			<tr height="75px">
				<td class="detail2">
					<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="未到店" data-role='none' data-ajax="false" onclick="notInStore()" /> 
					<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="已到店" data-role='none' data-ajax="false" onclick="hasInStore()" /> 
				</td>
			</tr>
		</table>
	</div>

	<!-- -----------------------------------------------是否下单------------------------------------------------------ -->
	<a href="#pushOrderOrNot" id="showPushOrderOrNot" data-rel="popup" data-position-to="window"></a>
	<div data-role="popup" id="pushOrderOrNot" style="position:fixed; top:20%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  
		data-shadow="false" data-position-to="origin" data-corners="false" data-swipe-close="false" data-dismissible="false" data-history="false">
		<input type="hidden" />
		<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr height="60px" id="personTr"  style="display:none">
				<td width="40%" align="right" style="padding-right:15px">
					就餐人数
				</td>
				<td width="60%">
					<input id="totalPerson" type="number" value="1" min="1" max="99" oninput="this.value=this.value.slice(0,2);"
						style="border:1px solid #D5D5D5;border-radius:4px;height:35px;width:75%;-webkit-appearance: none;" data-role="none"/>
				</td>
			</tr>
			<tr height="45px">
				<td align="center" colspan="2">
					<!-- <span style="font-size:120%">是否下单</span>
					<br /> -->
					<c:choose>
						<c:when test="${mustPayBeforeOrder eq 'Y' }">
							<span style="color:#999999">该订单需要先支付再下单，确认支付吗？</span>
						</c:when>
						<c:otherwise>
							<span style="color:#999999">下单后，订单将不可取消</span>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr height="65px">
				<td class="detail2" colspan="2">
					<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="取消" data-role='none' data-ajax="false" onclick="cancelPushOrder()" /> 
					<input type="button" style="background-color:#FFB400; color:#FFFFFF" value="确定" data-role='none' data-ajax="false" onclick="pushOrder()" /> 
				</td>
			</tr>
		</table>
	</div>
	
	<!-- -----------------------------------------------提示：是否下单------------------------------------------------------ -->
	<a href="#confirmSingleDiv" id="cancelLinkSingle" data-rel="popup" data-position-to="window"></a>
	<div data-role="popup" id="confirmSingleDiv" style="position:fixed; top:30%; left:5%; width:90%;" data-overlay-theme="b" data-theme="a"  data-shadow="false" data-position-to="origin" data-corners="false" data-history="false">
		<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr height="50px">
				<td align="center">确定下单吗？</td>
			</tr>
			<tr height="50px">
				<td class="popTd">
					<input type="button" value="取消" data-role='none' data-ajax="false" onclick="noConfirm()" /> 
					<input type="button" value="确认" data-role='none' data-ajax="false" onclick="javaScript:confirmOrder('${pk_group}','${orderDetail.id}');" /> 
				</td>
			</tr>
		</table>
	</div> 
<!-- 加菜按钮 -->
<a id="showmenupagebtn" href="#menupage"></a>
</div>

<!-- 隐藏域 这里存放订单信息 -->
<input type="hidden" id="isVip" name="isVip" value="${isVip }" />
<input type="hidden"  id="addr" name="addr" value="${orders.addr }"/>
<input type="hidden" id="openid" name="openid" value="${orders.openid }"/>
<input type="hidden" id="pk_group" name="pk_group" value="${orders.pk_group }"/>
<input type="hidden" id="firmid" name="firmid" value="${orders.firmid }"/>
<input type="hidden" id="dat" name="dat" value="${orders.dat }"/>
<input type="hidden" id="sft" name="sft" value="${orders.sft }"/>
<input type="hidden" id="datmins" name="datmins" value="${orders.datmins }"/>
<input type="hidden" id="tables" name="tables" value="${orders.tables }"/>
<input type="hidden" id="paymentType" name="paymentType" value="1"/>
<input type="hidden" id="appid" name="appid" value="${APPID }"/>
<input type="hidden" id="package" name="package" value="${PACKEGEVALUE }"/>
<input type="hidden" id="sign" name="sign" value="${SIGN }"/>
<input type="hidden" id="timestamp" name="timestamp" value="${TIMESTAMP }"/>
<input type="hidden" id="nonce" name="nonce" value="${NONCE }"/>
<input type="hidden" id="signtype" name="signtype" value="${SIGNTYPE }"/>
<input type="hidden" name="code" id="code" value="${code}">

<input id="orderid" type="hidden" value="${orders.id }"/>
<!-- <a href="<%=path %>/bookMeal/successpage.do?" data-ajax="false" id="commitsuccessbtn"> </a>--><!-- 点菜成功提示 -->
<a href="<%=path %>/bookMeal/orderDetail.do?code=${code}" data-ajax="false" id="commitsuccessbtn"></a><!-- 点菜成功提示 -->
<a href="#commitfail" id="commitfailbtn" data-rel="dialog"></a><!-- 点菜失败提示 -->

<!------------------------------------------------ 订单失败--------------------------------------------------------->
<div data-role="page" id="commitfail" data-overlay-theme="e">
  <div data-role="header">
    <h1>温馨提示</h1>
  </div>
  <div data-role="content">
    <p>不好意思，预定失败！</p>
    <a href="#" data-role="button">确定</a>
  </div>
</div> 

<!------------------------------------------------ 进入点餐页面前先加载一张图片--------------------------------------------------------->
<div data-role="page" id="loadImage" data-overlay-theme="e">
  <div data-role="content">
    <img src="<c:url value='/image/wechat/waitforpay.png'/>">
  </div>
</div>
</body>
</html>