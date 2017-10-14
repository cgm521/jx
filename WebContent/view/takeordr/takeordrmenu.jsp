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
<link rel="stylesheet" href="<%=path %>/css/default/global.css" />
<link rel="stylesheet" href="<%=path %>/css/default/pubitem/menulist.css" />
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/layer.js"></script>
<script type="text/javascript">
/**去除微信返回，刷新，分享**/
/* document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
}); */

$(document).on("pageinit","#menupage",function(){
	/**********************************************在线选餐页面********************************************************************/
	/*热门菜品点击*/
	$("#hot-menu").on("tap",function(){
		if($(this).css("background-image").indexOf("blue") == -1){//如果热门菜品背景不是蓝色的，则查询热门菜品
			regainChoose();//其他的选中都恢复
			$(this).css("background","url(<%=path%>/image/blue.png) repeat");//全部菜品样式为选中
			$(this).css("color","#fff");
			var firmid = $("#firmid").val();
			var protyp = {};
			/**功能:查询热门菜单*/
			InitLayer();
			$.post("<%=path %>/pubitem/getHotPubitem.do?firmId="+firmid,protyp,function(data){
				$("#menu-table").find("tr").remove();
				for(var i=0;i<data.length;i++){
					/**要区分是不是点过了**/
					if($("#list"+data[i].pubitem).length > 0 ){//如果我的菜单里存在这个菜品了，显示的时候加上数量
						var sl = $("#list"+data[i].pubitem).children().last().children(".number1").text();
						$("#menu-table").append("<tr>"
						+"<td rowspan='2' onclick='dancai(this)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"' d='<%=Commons.vpiceure %>"+data[i].url+"'><img src='<%=Commons.vpiceure %>"+data[i].smallUrl+"' width='100px' height='70px'/></td>"
						+"<td class='texttd' colspan='2'><div class='cai1'><img src='<%=path %>/image/cai1.png' width='18px' height='18px'/>"+data[i].pdes+"</div></td>"
						+"</tr>"
						+"<tr><td><div class='cai2'><img src='<%=path %>/image/cai2.png' width='18px' height='16px'/><span>"+data[i].price+"</span>元/个</div></td>"
						+"<td><div class='plus' onclick='plusClick(this,0)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/plus.png' width='20px' height='20px'/></div>"
						+" <div class='number' style='display:;'>"+sl+"</div>"
						+" <div class='minus' style='display:;' onclick='minusClick(this)'  a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/minus.png' width='20px' height='20px'/></div>	</td></tr>");
					}else{
						$("#menu-table").append("<tr>"
						+"<td rowspan='2' onclick='dancai(this)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"' d='<%=Commons.vpiceure %>"+data[i].url+"'><img src='<%=Commons.vpiceure %>"+data[i].smallUrl+"' width='100px' height='70px'/></td>"
						+"<td class='texttd' colspan='2'><div class='cai1'><img src='<%=path %>/image/cai1.png' width='18px' height='18px'/>"+data[i].pdes+"</div></td>"
						+"</tr>"
						+"<tr><td><div class='cai2'><img src='<%=path %>/image/cai2.png' width='18px' height='16px'/><span>"+data[i].price+"</span>元/个</div></td>"
						+"<td><div class='plus' onclick='plusClick(this,0)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/plus.png' width='20px' height='20px'/></div>"
						+" <div class='number' style='display:none;'>0</div>"
						+" <div class='minus' style='display:none;' onclick='minusClick(this)'  a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/minus.png' width='20px' height='20px'/></div>	</td></tr>");
					}
				}
				closeLayer();
			});
		}
	});
	/**全部菜品点击**/
	$("#all-menu").on("tap",function(){
		if($(this).css("background-image").indexOf("blue") == -1){
			regainChoose();//其他的选中都恢复
			/*功能：查询所有菜品下的第一个*/
			$("#menu-left table tbody tr td").first().tap();
		}
	});
	/*菜单类型点击*/
	$("#menu-left table tbody tr td").on("tap",function(){
		regainChoose();//其他的选中都恢复
		$("#all-menu").css("background","url(<%=path%>/image/blue.png) repeat");//全部菜品样式为选中
		$("#all-menu").css("color","#fff");
		$(this).css("background","#117ad6");
		$(this).css("color","#fff");
		/*功能：分类查询菜品*/
		var firmid = $("#firmid").val();//门店id
		var protypid = $(this).attr("class");//菜品类型id
		var name = $(this).text();//用来判断套餐
		var protyp={};
		if(protypid == "tc" && name == "精美套餐"){  //进入套餐
			//发送请求，查询此门店下的所有套餐
			InitLayer();
			$.post("<%=path %>/pubitem/listPackages.do?firmId="+firmid,protyp,function(data){
				$("#menu-table").find("tr").remove();
				for(var i=0;i<data.length;i++){
					/**要区分是不是点过了**/
					if($("#list"+data[i].id).length > 0 ){//如果我的菜单里存在这个套餐了，显示的时候加上数量
						var sl = $("#list"+data[i].id).children().last().children(".number1").text();
						$("#menu-table").append("<tr>"
						+"<td rowspan='2' onclick='taocan(this)' a='"+data[i].id+"' b='"+data[i].des+"' c='"+data[i].price+"' d='<%=Commons.vpiceure %>"+data[i].picsrc+"'><img src='<%=Commons.vpiceure %>"+data[i].picsrc+"' width='100px' height='70px'/></td>"
						+"<td class='texttd' colspan='2'><div class='cai1'><img src='<%=path %>/image/cai1.png' width='18px' height='18px'/>"+data[i].des+"</div></td>"
						+"</tr>"
						+"<tr><td><div class='cai2'><img src='<%=path %>/image/cai2.png' width='18px' height='16px'/><span>"+data[i].price+"</span>元/份</div></td>"
						+"<td><div class='plus' onclick='plusClick(this,1)' a='"+data[i].id+"' b='"+data[i].des+"' c='"+data[i].price+"'><img src='<%=path %>/image/plus.png' width='20px' height='20px'/></div>"
						+" <div class='number' style='display:;'>"+sl+"</div>"
						+" <div class='minus' style='display:;' onclick='minusClick(this)'  a='"+data[i].id+"' b='"+data[i].des+"' c='"+data[i].price+"'><img src='<%=path %>/image/minus.png' width='20px' height='20px'/></div>	</td></tr>");
					}else{
						$("#menu-table").append("<tr>"
						+"<td rowspan='2' onclick='taocan(this)' a='"+data[i].id+"' b='"+data[i].des+"' c='"+data[i].price+"' d='<%=Commons.vpiceure %>"+data[i].picsrc+"'><img src='<%=Commons.vpiceure %>"+data[i].picsrc+"' width='100px' height='70px'/></td>"
						+"<td class='texttd' colspan='2'><div class='cai1'><img src='<%=path %>/image/cai1.png' width='18px' height='18px'/>"+data[i].des+"</div></td>"
						+"</tr>"
						+"<tr><td><div class='cai2'><img src='<%=path %>/image/cai2.png' width='18px' height='16px'/><span>"+data[i].price+"</span>元/份</div></td>"
						+"<td><div class='plus' onclick='plusClick(this,1)' a='"+data[i].id+"' b='"+data[i].des+"' c='"+data[i].price+"'><img src='<%=path %>/image/plus.png' width='20px' height='20px'/></div>"
						+" <div class='number' style='display:none;'>0</div>"
						+" <div class='minus' style='display:none;' onclick='minusClick(this)'  a='"+data[i].id+"' b='"+data[i].des+"' c='"+data[i].price+"'><img src='<%=path %>/image/minus.png' width='20px' height='20px'/></div>	</td></tr>");
					}
				}
				closeLayer();
			});
		}else if(protypid == "tc1" && name == "优惠活动"){  //进入套餐
			//发送请求，查询此门店下的所有套餐
			InitLayer();
			$.post("<%=path %>/pubitem/findStoreWebMsg.do?firmId="+firmid,protyp,function(data){
				$("#menu-table").find("tr").remove();
				for(var i=0;i<data.length;i++){
					/**显示所有的优惠活动信息**/
						$("#menu-table").append("<tr style='text-align:center'>"
								+"<td style='width:100%;height:40%;'><img width='100%' height='220px' width='15' height='15' src='"+data[i].wurl+"'></td>"
								+"</tr>"
								+"<tr>"
								+"	<td style='text-align:center;'><h4>"+data[i].title+"</h4></td>"
								+"</tr>"
								+"<tr>"
								+"	<td style='text-align:left;'>"+data[i].wcontent+"</td>"
								+"</tr>");
// 						+"<td rowspan='2'><img src='"+data[i].wurl+"' width='100px' height='70px'/></td>"
<%-- 						+"<td class='texttd' colspan='2'><div class='cai1'><img src='<%=path %>/image/cai1.png' width='18px' height='18px'/>"+data[i].title+"</div></td>" --%>
// 						+"</tr>"
<%-- 						+"<tr><td><div class='cai2'><img src='<%=path %>/image/cai2.png' width='18px' height='16px'/><span>"+data[i].wcontent+"</span></div></td>" --%>
// 						+"<td><div class='plus' ></div>"
// 						+" <div class='number' style='display:none;'></div>"
// 						+" <div class='minus' style='display:none;'></div>	</td></tr>");

				}
				closeLayer();
			});
		}else{//进入单菜
			InitLayer();
			$.post("<%=path %>/pubitem/listPubitemDtl.do?projecttyp="+protypid+"&firmId="+firmid,protyp,function(data){
				$("#menu-table").find("tr").remove();
				for(var i=0;i<data.length;i++){
					if($("#list"+data[i].pubitem).length > 0 ){//如果我的菜单里存在这个菜品了，显示的时候加上数量
						var sl = $("#list"+data[i].pubitem).children().last().children(".number1").text();
						$("#menu-table").append("<tr>"
						+"<td rowspan='2' onclick='dancai(this)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"' d='<%=Commons.vpiceure %>"+data[i].url+"'><img src='<%=Commons.vpiceure %>"+data[i].smallUrl+"' width='100px' height='70px'/></td>"
						+"<td class='texttd' colspan='2'><div class='cai1'><img src='<%=path %>/image/cai1.png' width='18px' height='18px'/>"+data[i].pdes+"</div></td>"
						+"</tr>"
						+"<tr><td><div class='cai2'><img src='<%=path %>/image/cai2.png' width='18px' height='16px'/><span>"+data[i].price+"</span>元/个</div></td>"
						+"<td><div class='plus' onclick='plusClick(this,0)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/plus.png' width='20px' height='20px'/></div>"
						+" <div class='number' style='display:;'>"+sl+"</div>"
						+" <div class='minus' style='display:;' onclick='minusClick(this)'  a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/minus.png' width='20px' height='20px'/></div>	</td></tr>");
					}else{
						$("#menu-table").append("<tr>"
						+"<td rowspan='2' onclick='dancai(this)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"' d='<%=Commons.vpiceure %>"+data[i].url+"'><img src='<%=Commons.vpiceure %>"+data[i].smallUrl+"' width='100px' height='70px'/></td>"
						+"<td class='texttd' colspan='2'><div class='cai1'><img src='<%=path %>/image/cai1.png' width='18px' height='18px'/>"+data[i].pdes+"</div></td>"
						+"</tr>"
						+"<tr><td><div class='cai2'><img src='<%=path %>/image/cai2.png' width='18px' height='16px'/><span>"+data[i].price+"</span>元/个</div></td>"
						+"<td><div class='plus' onclick='plusClick(this,0)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/plus.png' width='20px' height='20px'/></div>"
						+" <div class='number' style='display:none;'>0</div>"
						+" <div class='minus' style='display:none;' onclick='minusClick(this)'  a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/minus.png' width='20px' height='20px'/></div>	</td></tr>");
					}
				}
				closeLayer();
			});
		}
	});
	
	/*******************************确认提交订单********************************************************/
//调用微信支付的先关接口参数
var appid="";
var timeStamp="";
var nonceStr="";
var packagevalue="";
var paySign="";

var state = "";//支付状态（）
var result = "";//是否支付完成
$("#commit-right").on("tap",function(){
	
		//获取选择的支付方式 1:微信支付  2：现场支付
		var payment = $("#payment").find("input[type='radio']:checked").val();
		//微信支付，调用微信支付的流程
		var paymoney = $("#menu-price2").text();//支付金额
		if(payment == "1"){
			$.ajax({
				type:"post",
				url:"<%=path %>/pubitem/jspay.do",
				data:{"paymoney":$("#menu-price2").text()},
				ansyc:false,
				success:function(data){
					appid=data["APPID"];//应用密钥
					timeStamp=data["TIMESTAMP"];//时间戳
					nonceStr=data["NONCE"];//随机字符串
					packagevalue=data["PACKEGEVALUE"];//账单信息
					paySign=data["SIGN"];//支付签名
					callpay();
				},
				
			});
			
<%-- 			$.post("<%=path %>/pubitem/jspay.do?paymoney="+paymoney,function(data){ --%>
// 				appid=data["APPID"];//应用密钥
// 				timeStamp=data["TIMESTAMP"];//时间戳
// 				nonceStr=data["NONCE"];//随机字符串
// 				packagevalue=data["PACKEGEVALUE"];//账单信息
// 				paySign=data["SIGN"];//支付签名
// 				result = callpay();
// 				alert(result);
// 				if(result=="" || result=='' || result==undefined){
// 					state = "1";
// 				}else if(result == "1"){
// 					state = "6";//已支付	
// 				}else {
// 					state = "1";//未支付
// 				}
// 				saveResvItems(state);
// 			});
		//现场支付
		}else{
			state="1";
			saveResvItems(state);
		}
	});
	
	//调用微信支付接口
function callpay(){
	 WeixinJSBridge.invoke('getBrandWCPayRequest',{
		 		"appId" : appid,
	 			"timeStamp" : timeStamp,
	 		 	"nonceStr" : nonceStr,
	 		  	"package" : packagevalue,
	 		  	"signType" : "SHA1", 
	 		  	"paySign" : paySign 
		 },
		 function(res){
				WeixinJSBridge.log(res.err_msg);
// 				alert(res.err_msg);
	            if(res.err_msg == "get_brand_wcpay_request:ok"){  
// 	                return "1";
	                state="2";
	                saveResvItems(state);
// 	                alert("微信支付成功"); 
	            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
	            	return "0";
// 	                alert("您确定有取消支付吗？");
	            }else{  
	            	return "-1";
// 	                alert("支付失败"); 	 
	            }  
	});
}
	
	
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
	order["tables"] = $("#tables").val();
	order["stws"] = $("#stws option:selected").val();
	order["money"] = $("#menu-price2").text();
	order["state"] = state;
	//遍历   commit-menu
	for(var i=0;i<=$("#mymenu-table").find("tr").length-1;i++){
		var rearmemo=$("#mymenu-table").find("tr").eq(i).find("td").find("input").val();
		var foodsid = $("#mymenu-table").find("tr").eq(i).attr("foodsid");//菜品id
		var foodsname = $("#mymenu-table").find("tr").eq(i).attr("foodsname");//菜品名称
		var foodnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text();//菜品数量
		var price = $("#mymenu-table").find("tr").eq(i).attr("price");//单价
		var flag = $("#mymenu-table").find("tr").eq(i).attr("flag");//单菜还是套餐的标识
		var totalprice = parseInt(foodnum) * parseFloat(price);//单个菜品总额
		order["listNetOrderDtl["+i+"].foodsid"]=foodsid;
		order["listNetOrderDtl["+i+"].ordersid"]=orderid;
		order["listNetOrderDtl["+i+"].foodsname"]=foodsname;
		order["listNetOrderDtl["+i+"].foodnum"]=foodnum;
		order["listNetOrderDtl["+i+"].price"]=price;
		order["listNetOrderDtl["+i+"].totalprice"]=totalprice;
		order["listNetOrderDtl["+i+"].ispackage"]=flag;
		order["listNetOrderDtl["+i+"].remark"]=rearmemo;
	}
	InitLayer();
	//保存订单
	if(orderid==null || orderid=="" || orderid=='undefined'){//如果没有订单ID的情况（直接预订点餐）
		$.post("<%=path %>/pubitem/saveOrderOrDtl.do",order,function(data){
			$(".layer").hide();
			if(data != null){
				//保存成功
//					$("#commitsuccessbtn").click();
				var link1=$("#commitsuccessbtn").attr("href");
				window.location.href=link1;
			}else{
				$("#commitfailbtn").click();
			}
		});
	}else{//如果有订单ID（点桌或者预订里面跳转来的）
		$.post("<%=path %>/pubitem/saveOrderDtl.do",order,function(data){
			$(".layer").hide();
			if(data!=null){
				//保存成功
//					$("#commitsuccessbtn").click();
				var link1=$("#commitsuccessbtn").attr("href");
				window.location.href=link1;
			}else{
				$("#commitfailbtn").click();
			}
		});
	}
}
	
	
	
	/************************套餐返回*************************************/
	$("#taocan-return").on("tap",function(){
		$("#hidetaocanbtn").click();
	});

});



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
		var foodnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text();//菜品数量
		var price = $("#mymenu-table").find("tr").eq(i).attr("price");//单价
		zsl += parseInt(foodnum);
		zje += parseInt(foodnum) * parseInt(price);
	}
	$("#menu-count2").text(zsl);
	$("#menu-price2").text(zje);
	conPriSyn();
	
	/**一上来就选中全部菜品中的第一个*/
	$("#menu-left table tbody tr td").first().tap();
	
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
			//发送搜索请求
			InitLayer();
			$.post("<%=path %>/pubitem/getPubitemByName.do",project,function(data){
				$("#menu-table").find("tr").remove();
				for(var i=0;i<data.length;i++){
					/**要区分是不是点过了**/
					if($("#list"+data[i].pubitem).length > 0 ){//如果我的菜单里存在这个菜品了，显示的时候加上数量
						var sl = $("#list"+data[i].pubitem).children().last().children(".number1").text();
						$("#menu-table").append("<tr>"
						+"<td rowspan='2' onclick='dancai(this)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"' d='<%=Commons.vpiceure %>"+data[i].url+"'><img src='<%=Commons.vpiceure %>"+data[i].smallUrl+"' width='100px' height='70px'/></td>"
						+"<td class='texttd' colspan='2'><div class='cai1'><img src='<%=path %>/image/cai1.png' width='18px' height='18px'/>"+data[i].pdes+"</div></td>"
						+"</tr>"
						+"<tr><td><div class='cai2'><img src='<%=path %>/image/cai2.png' width='18px' height='16px'/><span>"+data[i].price+"</span>元/个</div></td>"
						+"<td><div class='plus' onclick='plusClick(this,0)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/plus.png' width='20px' height='20px'/></div>"
						+" <div class='number' style='display:;'>"+sl+"</div>"
						+" <div class='minus' style='display:;' onclick='minusClick(this)'  a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/minus.png' width='20px' height='20px'/></div>	</td></tr>");
					}else{
						$("#menu-table").append("<tr>"
						+"<td rowspan='2' onclick='dancai(this)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"' d='<%=Commons.vpiceure %>"+data[i].url+"'><img src='<%=Commons.vpiceure %>"+data[i].smallUrl+"' width='100px' height='70px'/></td>"
						+"<td class='texttd' colspan='2'><div class='cai1'><img src='<%=path %>/image/cai1.png' width='18px' height='18px'/>"+data[i].pdes+"</div></td>"
						+"</tr>"
						+"<tr><td><div class='cai2'><img src='<%=path %>/image/cai2.png' width='18px' height='16px'/><span>"+data[i].price+"</span>元/个</div></td>"
						+"<td><div class='plus' onclick='plusClick(this,0)' a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/plus.png' width='20px' height='20px'/></div>"
						+" <div class='number' style='display:none;'>0</div>"
						+" <div class='minus' style='display:none;' onclick='minusClick(this)'  a='"+data[i].pubitem+"' b='"+data[i].pdes+"' c='"+data[i].price+"'><img src='<%=path %>/image/minus.png' width='20px' height='20px'/></div>	</td></tr>");
					}
				}
				closeLayer();
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
	$("#mymenudiv-right").click(function(){
		if(parseInt($("#menu-count1").text()) == 0){//如果没点菜，则提示没有点菜
			$("#pop").text("对不起，您还没有点菜。");
			$("#dhk").click();
		}else{//跳转到我的菜单
			$("#dhk2").click();
		}
	});
});
/*****************************************************************************点菜页面**********************************************/
/*改变左侧菜品类型列表的选中状态*//*改变头部热门菜品和全部菜品的选中状态*/
function regainChoose(){
	$("#menu-left table tr td").css("background","#fff");
	$("#menu-left table tr td").css("color","#117ad6");
	$("#hot-menu").css("background","url(<%=path%>/image/white.png) repeat");
	$("#hot-menu").css("color","#117ad6");
	$("#all-menu").css("background","url(<%=path%>/image/white.png) repeat");
	$("#all-menu").css("color","#117ad6");
}

/*点菜页面中的减菜*/
function minusClick(obj){
	InitLayer();
	var sl = parseInt($(obj).prev().text());
	sl -= 1;
	$(obj).prev().text(sl);
	
	var pubitem = $(obj).attr("a");
	
	if(sl == 0){
		$(obj).hide();
		$(obj).prev().hide();
		$("#list"+pubitem).remove();//下个页面table删掉这个菜品
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
		return;
	}
	$("#list"+pubitem).children().last().children(".number1").text(sl);//下个页面table中数量变化
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
}
/*点菜页面中的加菜*/
function plusClick(obj,flag){
	InitLayer();
	dh1(obj);//动画效果
	$(obj).nextAll().show();
	var sl = parseInt($(obj).next().text());
	sl += 1;
	$(obj).next().text(sl);	
	
	var pubitem = $(obj).attr("a");
	var pdes = $(obj).attr("b");
	var price = $(obj).attr("c");
	if($("#list"+pubitem).length > 0 ){//如果存在这个菜品了
		$("#list"+pubitem).children().last().children(".number1").text(sl);
		listMymenu();//遍历我的菜单，为总数量和总价格赋值。
		closeLayer();
		return;
	}
	var fun = "showRemark('"+pubitem+"')";
	var menudata = "<tr id='list"+pubitem+"' foodsid='"+pubitem+"' foodsname='"+pdes+"' price='"+price+"' flag='"+flag+"'>"
	+"<td style='width:0px;'><input type='hidden'/></td>"
	+"<td onclick="+fun+"><div class='cai11'><img src='<%=path %>/image/cai11.png' width='18px' height='18px'/><span style='color:red;'>"+pdes+"</span></div>"
	+"<div class='cai22'><img src='<%=path %>/image/cai22.png' width='18px' height='18px'/><span>"+price+"</span>元/个</div>"
	+"</td><td><div class='plus1'  onclick='plusClick1(this)' a='"+pubitem+"' b='"+pdes+"' c='"+price+"'><img src='<%=path %>/image/plus.png' width='34px' height='34px'/></div>"
	+"<div class='number1' style='display:;'>"+sl+"</div>"
	+"<div class='minus1' style='display:;' onclick='minusClick1(this)' a='"+pubitem+"' b='"+pdes+"' c='"+price+"'><img src='<%=path %>/image/minus.png' width='34px' height='34px'/></div>"
	+"</td></tr>";
	$("#mymenu-table").append(menudata);
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
}
/*加菜效果*/
function dh1(obj){
	$("#yddiv").remove();
	var left = $(obj).offset().left;
	var top = $(obj).offset().top;
	var top1 = $("#mymenudiv-right").offset().top;
	var div = "<div id='yddiv' style='background:red;position:fixed;z-index:99;width:10px;height:10px;top:"+top+"px;left:"+left+"px'></div>";
	$("#menupage").append(div);
	$("#yddiv").animate({top:top1+$("#mymenudiv-right").height()}, 500).animate({opacity:"0"},500);
}  

/**遍历我的订单，给总数量和总价格赋值*/
function listMymenu(){
	//遍历   mymenu-table
	var menuCount = 0;
	var menuPrice = 0;
	for(var i=0;i<=$("#mymenu-table").find("tr").length-1;i++){
		var foodnum = $("#mymenu-table").find("tr").eq(i).children().last().children(".number1").text();//菜品数量
		var price = $("#mymenu-table").find("tr").eq(i).attr("price");//单价
		foodnum = parseInt(foodnum);
		var totalprice = parseInt(foodnum) * parseFloat(price);//单个菜品总额
		menuCount += foodnum;
		menuPrice += totalprice;
	}
	$("#menu-count2").text(menuCount);//本页面和下个页面底部数量和价格变化
	$("#menu-price2").text(menuPrice);
	conPriSyn();
}
/****前一个页面同步后一个页面的数量和价格*****/
function conPriSyn(){
	$("#menu-count1").text($("#menu-count2").text());//本页面和下个页面底部数量和价格变化
	$("#menu-price1").text($("#menu-price2").text());
}

/*****************************************************************************我的订单页面**********************************************/
/*减菜*/
function minusClick1(obj){
	InitLayer();
	var sl = parseInt($(obj).prev().text());
	sl -= 1;
	$(obj).prev().text(sl);
	var pubitem = $(obj).attr("a");
	
	if(sl == 0){
		//将整条菜品删除
		$("#list"+pubitem).remove();
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
	
	listMymenu();//遍历我的菜单，为总数量和总价格赋值。
	closeLayer();
}
/*清空**/
function clear1(){
	$("#mymenu-table").find("tr").remove();
	$("#menu-count2").text("0");//本页面和下个页面底部数量和价格变化
	$("#menu-price2").text("0");
	conPriSyn();
}
/**继续加菜*/
function plusmenu(){
	$("#showmenupagebtn").click();//显示第一个页面
	/**一上来就选中第一个*/
	$("#menu-left table tbody tr td").first().tap();
}

function showRemark(pubitem){
	$('#remarmemo').find('input').val(pubitem);
	$('#remarmemo11').val($('#list'+pubitem).find('input').val());
//  	$('#remarmemo').find('textarea').val("");
	$('#remarmemo').show();
}
function commitRemarmemo(){
	var pubitem = $('#remarmemo').find('input').val();
	$('#list'+pubitem).find('input').val($('#remarmemo11').val());
	$('#remarmemo').hide();
}



/********************************************单菜，套餐页面********************************************************************/
/**单菜详细页面**/
function dancai(obj){
	var pdes = $(obj).attr("b");
	var price = $(obj).attr("c");
	var img = $(obj).attr("d");
	var html="<div id='dancaidiv' style='width:100%;height:100%;background:transparent;z-index:99;position:fixed;color:#ccc;padding-bottom:80px;'>"
	+"<div style='width:80%;height:50%;margin:25% auto;'>"
	+"<div style='width:100%;height:90%;text-align:center;position:relative;'>"
	+"<img src='<%=path %>/image/close.png' width='20px' height='20px' style='position:absolute;top:1px;right:1px;' onclick='closedc()'/>"
	+"<img src='"+img+"' width='100%' height='100%'/>"
	+"</div>"
	+"<div style='width:100%;height:auto;text-align:center;color:black;text-shadow:0px 0px 0px;background:#ccc;'>"+pdes+"("+price+"元/份)"+"</div></div></div>";
	$('body').append(html);
}
/**隐藏单菜详情**/
function closedc(){
	$("#dancaidiv").remove();
}
/**套餐详细页面**/
function taocan(obj){
	var packageId = $(obj).attr("a");
	var price = $(obj).attr("c");
	var img = $(obj).attr("d");
	var taocanimg = "<img src='"+ img +"' width='100%' height='50%'/>";
	var firmId = $("#firmid").val();
	$("#taocan-money").text(price);
	$("#taocan-img").empty().append(taocanimg);
	var taocan = {"firmId":firmId,"packageId":packageId};
	//请求数据，进入套餐详细页面
	InitLayer();
	$.post("<%=path %>/pubitem/findPackageDetail.do?",taocan,function(data){
		$("#taocan-table-tbody").find("tr").remove();
		$("#taocan-count").text(data.length);
		for(var i=0;i<data.length;i++){
			$("#taocan-table-tbody").append("<tr><td style='text-align:left;'>"+data[i].des+"</td><td>"+data[i].cnt+"</td></tr>");
		}
		closeLayer();
	});
	$("#showtaocanbtn").click();
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
				<a id="searchbtn" href="#" data-role="button" style="display:inline-block;padding:0.5em 0.8em;">返回</a>
			</div>
		</div>
	</div>
	<div id="menutop" class="top">
		<div id="hot-menu">热门菜品</div>
		<div id="all-menu">全部菜品</div>
	</div>
	<div style="width:100%;height:12px;background:url(<%=path%>/image/touying.png) repeat-x"></div>
	<div id="menumain" class="main">
		<div id="menu-left">
			<div id="gotoSearch"><img src="<%=path%>/image/search.png" width="33px" height="30px"/></div>
			<table id="list-table">
				<c:forEach items="${listProjectType }" var="protyp">
					<tr><td class="${protyp.id }">${protyp.des }</td></tr>
				</c:forEach>
				<tr><td class='tc'>${count }</td></tr>
				<tr><td class='tc1'>${count1 }</td></tr>
			</table>
			<br/>
			<br/>
		</div>
		<div id="menu-right">
			<table id="menu-table">
			  
			</table>
			<br/>
			<br/>
		</div>
	</div>
	<div id="mymenudiv">
		<div id="mymenudiv-left">已点菜品：<span id="menu-count1">0</span>&nbsp;&nbsp;&nbsp;总金额：<span id="menu-price1">0.0</span>元</div>
		<div id="mymenudiv-right" ><div style="margin:0.2em 0px 0.2em 1.5em;">我的菜单</div></div>
	</div>
	<a id="dhk" href="#pagetwo" data-rel="dialog" data-transition="pop"></a> <!-- 没有选择菜品弹出框 -->
	<a id="dhk2" href="#mymenu" data-transition="slideup"></a>                   <!-- 进入我的菜单 -->
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

<!------------------------------------------------ 我的菜单 --------------------------------------------------------->
<div data-role="page" id="mymenu" class="page">
<div id="mymenutop">
	<div id="mymenutop-left">
		已点菜品
	</div>
	<div id="mymenutop-right">
		<div style="float:left;width:45%;height:100%;text-align:right"><a data-role="button" style="margin:0.2em;padding:0.4em;display:block;" onclick="clear1()">清空</a></div>
		<div style="float:right;width:45%;height:100%;text-align:left;"><a data-role="button" style="margin:0.2em;padding:0.4em;display:block;" onclick="plusmenu()">加菜</a></div>
		<%-- <img src="<%=path %>/image/clear.png" width="30%" height="70%" onclick="clear1()"/>&nbsp;&nbsp;
		<img src="<%=path %>/image/plusmenu.png" width="30%" height="70%" onclick="plusmenu()"/> --%>
	</div>
</div>
<div id="mymenu-main" class="main">
	<table id="mymenu-table">
		<c:forEach items="${orders.listNetOrderDtl }" var="food">
			<tr id='list${food.foodsid }' foodsid='${food.foodsid }' foodsname='${food.foodsname }' price='${food.price }' flag='${food.ispackage }'>
				<td>
					<div class='cai11'><img src='<%=path %>/image/cai11.png' width='18px' height='18px'/>${food.foodsname }</div>
					<div class='cai22'><img src='<%=path %>/image/cai22.png' width='18px' height='18px'/><span>${food.price }</span>元/个</div>
				</td>
				<td>
					<div class='plus1'  onclick='plusClick1(this)' a='${food.foodsid }' b='${food.foodsname }' c='${food.price }'>
						<img src='<%=path %>/image/plus.png' width='34px' height='34px'/>
					</div>
					<div class='number1' style='display:;'>${food.foodnum }</div>
					<div class='minus1' style='display:;' onclick='minusClick1(this)' a='${food.foodsid }' b='${food.foodsname }' c='${food.price }'>
						<img src='<%=path %>/image/minus.png' width='34px' height='34px'/></div>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div id="bak" style="width:95%;height:auto;background:#f3f3f3;margin:0 auto;">
		<textarea name="remark" id="remark" style="color:red;margin:5px auto;" placeholder="您还有什么需求，请在此留言..." maxlength="100">${orders.remark }</textarea>
	</div>
	<!-- 选择支付方式 -->
	<div id="payment" >
		 <legend>支付方式:</legend> 
		<input type="radio" name="paymentname" id="payment1"  value="1" checked="checked"/><label for="payment1">微支付</label>
		<input type="radio" name="paymentname" id="payment2"  value="2" /><label for="payment2">现场付</label>
<!-- 		<input id="aa" type="button" value="按钮" onclick="jspay()" /> -->
<!-- 		<input type="button" onclick="test1()" value="test1"> -->
<!-- 		<button type="button" onclick="test()" ><h1>测试支付</h1></button> -->
	</div>
	<br/>
	<br/>
</div>
<!-- ---------菜品单个备注-------- -->
<a href="#remarmemo" id="showremarmemo"></a>
<div data-role="page" class="page" id="remarmemo">
	<input type="hidden" />
	<textarea id="remarmemo11" style="color:red;margin:5px auto;" placeholder="您对当前菜品还有什么需求，请在此备注..." maxlength="100"></textarea>
	<a href="#" data-role="button" id="remarmemo-button" data-ajax="false" onclick="commitRemarmemo()">确认提交</a>
</div>
<!-- ----------------------------- -->
<div id="commitdiv">
	<div id="commit-left">已点菜品：<span id="menu-count2">0</span>&nbsp;&nbsp;&nbsp;总金额：<span id="menu-price2">0.0</span>元</div>
	<div id="commit-right" ><div style="margin:0.2em 0px 0.2em 1.5em;">提交订单</div></div>
</div>
<!-- 加菜按钮 -->
<a id="showmenupagebtn" href="#menupage"></a>
</div>
  <!-- 隐藏域 这里存放订单信息 -->
<input type="hidden"  id="addr" name="addr" value="${orders.addr }"/>
<input type="hidden" id="openid" name="openid" value="${orders.openid }"/>
<input type="hidden" id="firmid" name="firmid" value="${orders.firmid }"/>
<input type="hidden" id="dat" name="dat" value="${orders.dat }"/>
<input type="hidden" id="sft" name="sft" value="${orders.sft }"/>
<input type="hidden" id="datmins" name="datmins" value="${orders.datmins }"/>
<input type="hidden" id="tables" name="tables" value="${orders.tables }"/>
<input id="orderid" type="hidden" value="${orders.id }"/>
<a href="<%=path %>/pubitem/getOrderMenus.do?firmid=${orders.firmid }&openid=${orders.openid }&dat=${orders.dat }" id="commitsuccessbtn"></a><!-- 点菜成功提示 -->
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
<!-- -----------------------------------------------套餐页面------------------------------------------------------ -->
<a href="#taocan" id="showtaocanbtn"></a><!-- 套餐详情 -->
<a href="#menupage" id="hidetaocanbtn"></a><!-- 隐藏套餐详情 -->
<div data-role="page" id="taocan" class="page">
<div id="taocan-top" class="top">套餐详情</div>
	<div id="taocan-main" class="main" style="width:95%;margin:15px auto;">
		<div id="taocan-img"></div>
	    <table id="taocan-table">
	    	<thead>
				<tr>
					<td style="width:55%;">菜品</td>
					<td style="width:45%;">数量</td>
				</tr>
			</thead>
			<tbody id="taocan-table-tbody">
				
			</tbody>
		<tfoot>
			<tr>
				<td>总计菜品：<span id="taocan-count" style="color:red;">0</span></td>
				<td>套餐金额：<span id="taocan-money" style="color:red;">0</span>元</td>
			</tr>
		</table>
		<div id="taocan-return" class="next_button_div"><a href="#" data-role="button" data-ajax="false">返&nbsp;&nbsp;&nbsp;&nbsp;回</a></div>
  </div>
</div>
</body>
</html>