<!DOCTYPE html> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%String path=request.getContextPath(); %>
<html> 
<head> 
<title></title> 
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<link rel="stylesheet" href="<%=path %>/css/validate.css" />
<link rel="stylesheet" href="<%=path %>/css/default/global.css"/>
<link rel="stylesheet" href="<%=path %>/css/default/wechatPay/payMenu.css"/>
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/validate.js"></script>
<script src="<%=path %>/js/layer.js"></script>
</head>
<body>
<!-- ----------------------------------------------------------------支付页面--------------------------------------------------------------------------------- -->
<div data-role="page" id="payMenuPage" class="page">
<div id="payMenuPage-top" class="top">${firmdes }&nbsp;${tables }号桌餐费</div>
	<div id="payMenuPage-main" class="main" style="width:95%;margin:15px auto;">
		<table id="payMenu-table0">
			<tr>
				<td style='width:20%;'>台&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位：</td>
				<td style='width:25%;'>${tables }</td>
				<td style='width:20%;'>单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：	</td>
				<td style='width:35%;'>${resv }</td>
			</tr>
			<tr>
				<td>人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：</td>
				<td>${pax }</td>
				<td>时&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间：	</td>
				<td>${dat }</td>
			</tr>
			<tr>
				<td>印单员工：</td>
				<td>${printName }</td>
				<td>开单员工：	</td>
				<td>${empName }</td>
			</tr>
		</table>
		<table id="payMenu-table1">
			<thead>
				<tr>
					<td>菜品</td>
					<td>数量</td>
					<td>合计</td>
				</tr>
			</thead>
			<tbody id="pay-menu">
				<c:forEach items="${listNetOrderDtl }" var="order">
					<tr foodsid='${order.foodsid }' foodsname='${order.foodsname }' foodnum='${order.foodnum }' price='${order.price }' totalprice='${order.totalprice }' flag='${order.ispackage }'>
						<td>${order.foodsname }</td>
						<td>${order.foodnum }</td>
						<td>${order.price }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<table id="payMenu-table2">
			<tr>
				<td style='width:30%;text-align:right;'>定额折扣：</td>
				<td style='width:20%;'><span style='color:red;'>${quotaDiscount }</span>元</td>
				<td style='width:25%;text-align:right;'>抹零：	</td>
				<td style='width:25%;'><span style='color:red;'>${delLittleNum }</span>元</td>
			</tr>
			<tr>
				<td colspan='4' style='text-align:center;background:#e5e5e5;font-size:26px;'>应付金额：￥<span style='color:red;'>${outAmt }</span>元</td>
			</tr>
		</table>
		<br/>
		<div id="payMenu-yespay" class="next_button_div"><a href="#" data-role="button" id="payMenu-yespay-button" data-ajax="false">确认支付</a></div>
		<br/>
    </div>
    
  </div>
<a id="showConfigPay" href="#configPay"></a><!-- 显示会员卡支付界面 -->
<a id="showSuccessPay" href="#paySuccess"></a><!-- 支付成功 -->
<a id="showFailPay" href="#payFail" data-rel="dialog"></a><!-- 支付失败 -->
<!-- ----------------------------------------------------------------会员卡支付页面--------------------------------------------------------------------------------- -->
<div data-role="page" id="configPay" class="page">
<div id="configPay-top" class="top">会员卡支付</div>
	<div id="configPay-main" class="main" style="width:95%;margin:15px auto;">
		<table id="configPay-table" >
			<tbody>
				<tr>
					<td  colspan="2" style='color:red;text-align:center;padding:5%;font-size:24px;border:0px;'>请输入您的会员卡信息</td>
				</tr>
				<tr>
					<th style='width:20%;'><label for='name'>卡&nbsp;&nbsp;&nbsp;&nbsp;号：</label></th>
					<td style='width:80%;'><span><input type="text" data-role="none" name="cardNo" id="cardNo" value="${cardNo }" /></span></td>
				</tr>
				<tr>
					<th><label for='pass'>密&nbsp;&nbsp;&nbsp;&nbsp;码：</label></th>
					<td><span><input type="password" data-role="none" name="pass" id="pass" value="" /></span></td>
				</tr>
				<tr>
					<th><label for='telephone'>手机号：</label></th>
					<td><span><input type="text" data-role="none" name="telephone" id="telephone" value="" /></span></td>
				</tr>
				<tr>
					<th style='border:0px;'><label for='valicode'>验证码：</label></th>
					<td style='border:0px;'>
						<input type="text" data-role="none" name="valicode" id="valicode" value="" style='width:55%;'/>
						<input type="button" style="font-size:14px;width:40%;" data-role="none" id="sendSms" value="获取验证码"/>
						<input type="hidden" id="rannum" value="" />
					</td>
				</tr>
			</tbody>
		</table>
		<div id="configPay-yespay" class="next_button_div"><a href="#" data-role="button" id="configPay-yespay-button" data-ajax="false">确认支付</a></div>
  </div>
  
</div>
<!-- ----------------------------------------------------------------支付成功页面--------------------------------------------------------------------------------- -->
<div data-role="page" id="paySuccess" class="page">
<div id="paySuccess-top" class="top">${orders.firmdes }</div>
	<div id="paySuccess-main" class="main" style="width:95%;margin:15px auto;">
		<div id="paySucMain1"></div>
		<div id="paySucMain2"></div>
		<table id="paySucMain-table" >
			<tbody>
				<tr>
					<th>应付金额：</th>
					<td><span id="shouldPay" style='color:red;'>0</span>元</td>
				</tr>
				<tr>
					<th>已付金额：</th>
					<td><span id="alreadyPay" style='color:red;'>0</span>元</td>
				</tr>
				<tr>
					<th>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额：</th>
					<td><span id="surplusMoney" style='color:red;'>0</span>元</td>
				</tr>
			</tbody>
		</table>
		<!-- <div id="finishPay"></div> -->
   </div>
 </div>
 <!-- ----------------------------------------------------------------支付失败页面--------------------------------------------------------------------------------- -->
<%-- <div data-role="page" id="payFail">
<div data-role="header" id="payFail-top">${orders.firmdes }</div>
	<div data-role="content" id="payFail-main">
		<div id="payFailMain1"></div>
		<div id="payFailMain2"></div>
		<div id="payFailMain3">
			对不起，您的卡号，密码与手机号不正确，请重新输入！
		</div>
		<!-- <a href="#configPay"><div id="payFailReturn"></div></a> -->
   </div>
 </div> --%>
<div data-role="page" id="payFail">
<div data-role="header"><h1>温馨提示</h1></div>
	<div data-role="content">
		<p id="payFailMain3">对不起，您的卡号，密码与手机号不正确，请重新输入.</p>
   </div>
 </div>
<input type="hidden" id="resv" name="resv" value="${resv }" />
<input type="hidden" id="firmid" name="firmid" value="${firmid }" />
<input type="hidden" id="firmdes" name="firmdes" value="${firmdes }" />
<input type="hidden" id="dat" name="dat" value="${dat }" />
<input type="hidden" id="pax" name="pax" value="${pax }" />
<input type="hidden" id="sft" name="sft" value="${sft }" />
<input type="hidden" id="tables" name="tables" value="${tables }" />
<input type="hidden" id="contact" name="contact" value="${contact }" />
<input type="hidden" id="addr" name="addr" value="${addr }" />
<input type="hidden" id="rannum" name="rannum" value="" />
<input type="hidden" id="pubitem" name="pubitem" value="" />
<input type="hidden" id="openid" name="openid" value="" />
<input type="hidden" id="datmins" name="datmins" value="" />
<input type="hidden" id="remark" name="remark" value="" />
<input type="hidden" id="tele" name="tele" value="" />
<input type="hidden" id="isfeast" name="isfeast" value="0" />

<input type="hidden" id="outAmt" name="outAmt" value="${outAmt }" />
<input type="hidden" id="firmId" name="firmId" value="${firmId }" />
<input type="hidden" id="date" name="date" value="${date }" />
<input type="hidden" id="invoiceAmt" name="invoiceAmt" value="${invoiceAmt }" />
<input type="hidden" id="empName" name="empName" value="${empName }" />
<script type="text/javascript">
document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    WeixinJSBridge.call('hideToolbar');
    WeixinJSBridge.call('hideOptionMenu');
});

$(document).on("pageinit","#payMenuPage",function(){
	/*确认支付*/
	$("#payMenu-yespay").on("tap",function(){
		$("#showConfigPay").click();
	});
});
$(function(){
	/**必须使用微信打开**/
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
			$("#sendSms").val("获取验证码");
			wait = 60;
		} else {
			$("#sendSms").attr("disabled",true);
			$("#sendSms").val("重新发送(" + wait + ")");
			wait--;
			timeID=setTimeout(function() {
				time();
			},
			1000);
		}
	}
	var validate = new Validate({
		validateItem:[{
			type:'text',
			validateObj:'cardNo',
			validateType:['canNull'],
			param:['F',10],
			error:['会员姓名不能为空']
		},{
			type:'text',
			validateObj:'telephone',
			validateType:['canNull'],
			param:['F'],
			error:['手机号码不能为空']
		},{
			type:'text',
			validateObj:'valicode',
			validateType:['canNull'],
			param:['F'],
			error:['验证码不能为空']
		},{
			type:'text',
			validateObj:'telephone',
			validateType:['mobile'],
			error:['手机号码格式不正确']
		}]
	});

	$("#sendSms").click(function(){
		var validate1 = new Validate({
			validateItem:[{
				type:'text',
				validateObj:'cardNo',
				validateType:['canNull'],
				param:['F'],
				error:['会员姓名不能为空']
			},{
				type:'text',
				validateObj:'telephone',
				validateType:['canNull'],
				param:['F'],
				error:['手机号码不能为空']
			},{
				type:'text',
				validateObj:'telephone',
				validateType:['mobile'],
				error:['手机号码格式不正确']
			}]
		});
		if(validate1._submitValidate()){
			time();
			var card={};
			card["tele"]=$("#telephone").val();
			$.post("<%=path %>/card/sendSms.do",card,function(data){
				$("#rannum").val(data);
			});
		}
	});
	
	$("#configPay-yespay").click(function(){
		if(validate._submitValidate()){
			var rannum=$("#rannum").val();
			var valicode=$("#valicode").val();
			if(rannum == valicode){//确认支付
				var order={};
				var cardNo = $("#cardNo").val();
				var pass = $("#pass").val();
				var telephone = $("#telephone").val();
				InitLayer();
				$.post("<%=path %>/card/loginCard.do?cardNo="+cardNo+"&pass="+pass+"&telephone="+telephone,order,function(data){
					if(data != null && data!= ""){//成功
						var outAmt = $("#outAmt").val();
						if(data.zAmt*1 > outAmt*1 ){ //如果有余额
							var card ={};
							var firmId = $("#firmId").val();
							var date = $("#date").val();
							var invoiceAmt = $("#invoiceAmt").val();
							var empName = $("#empName").val();
							card["cardNo"] = cardNo;
							card["outAmt"] = outAmt;
							card["firmId"] = firmId;
							card["date"] = date;
							card["invoiceAmt"] = invoiceAmt;
							card["empName"] = empName;
							$.post("<%=path %>/card/cardOutAmt.do",card,function(data){//扣款
								if(data != null && data!= ""){//扣款成功
									//确认支付按钮不可再用
									$("#configPay-yespay-button").css("background-image","url(<%=path%>/image/yespay2.png)");
									$("#configPay-yespay").unbind();
									//为支付成功页面赋值
									$("#shouldPay").text(data.outAmt);
									$("#alreadyPay").text(data.outAmt);
									$("#surplusMoney").text(data.zAmt);
									
									var orderid=$("#orderid").val();
									order["id"]=orderid;
									order["resv"] = $("#resv").val();
									order["firmid"] = $("#firmid").val();
									order["firmdes"] = $("#firmdes").val();
									order["dat"] = $("#dat").val();
									order["pax"] = $("#pax").val();
									order["sft"] = $("#sft").val();
									order["tables"] = $("#tables").val();
									order["contact"] = $("#contact").val();
									order["addr"] = $("#addr").val();
									order["rannum"] = $("#rannum").val();
									order["pubitem"] = $("#pubitem").val();
									order["openid"] = $("#openid").val();
									order["datmins"] = $("#datmins").val();
									order["remark"] = $("#remark").val();
									order["tele"] = $("#tele").val();
									order["isfeast"] = $("#isfeast").val();
									
									order["printstate"] = 1;
									order["cardzamt"] = data.zAmt;
									order["cashier"] = empName;
									order["receivable"] = data.outAmt;
									order["cardamt"] = data.outAmt;
									order["payment"] = 1;
									//遍历   commit-menu
									for(var i=0;i<=$("#pay-menu").find("tr").length-1;i++){
										var foodsid = $("#pay-menu").find("tr").eq(i).attr("foodsid");//菜品id
										var foodsname = $("#pay-menu").find("tr").eq(i).attr("foodsname");//菜品名称
							 			var foodnum = $("#pay-menu").find("tr").eq(i).attr("foodnum");//菜品数量
							 			var price = $("#pay-menu").find("tr").eq(i).attr("price");//单价
							 			var totalprice = $("#pay-menu").find("tr").eq(i).attr("totalprice");//单个菜品总额
							 			var flag = $("#pay-menu").find("tr").eq(i).attr("flag");//单菜还是套餐的标识
							 			order["listNetOrderDtl["+i+"].foodsid"]=foodsid;
										order["listNetOrderDtl["+i+"].ordersid"]=orderid;
										order["listNetOrderDtl["+i+"].foodsname"]=foodsname;
										order["listNetOrderDtl["+i+"].foodnum"]=foodnum;
										order["listNetOrderDtl["+i+"].price"]=price;
										order["listNetOrderDtl["+i+"].totalprice"]=totalprice;
										order["listNetOrderDtl["+i+"].ispackage"]=flag;
									}
									$.post("<%=path %>/pubitem/yespay.do",order,function(data){ //生成账单记录
										if(data == "1"){
											closeLayer();
											$("#showSuccessPay").click();
											return;
										}else{
											closeLayer();
											$("#payFailMain3").text("支付成功！但由于网络中断，生成账单失败.");
											$("#showFailPay").click();
											return;
										}
									});
								}else{
									closeLayer();
									$("#payFailMain3").text("对不起，由于网络不给力，扣款失败.");
									$("#showFailPay").click();
									return;
								}
							});
						}else{
							closeLayer();
							$("#payFailMain3").text("对不起，您的卡内余额不足.");
							$("#showFailPay").click();
							return;
						}
					}else{
						closeLayer();
						$("#payFailMain3").text("对不起，您的卡号，密码与手机号不正确，请重新输入.");
						$("#showFailPay").click();
						return;
					}
				});
			}else{
				$("#valicode").css("background", "#F33");
			}
		}
	});
});
function changeNum(e){
	var num=$(e).val();
	if(num.length>10){
		$(e).val(num.substr(0,10));
	}
}
 </script>
</body>
</html>
