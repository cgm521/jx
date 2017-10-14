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
<link rel="stylesheet" href="<%=path %>/css/default/pubitem/choiceDesk.css"/>
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script src="<%=path %>/js/layer.js"></script>
<title>选择台位</title>
</head>
<body>
<div data-role="page" id="page" class="page">
	<div id="main" class="main1">
		<div id="img">
			<c:if test="${firm.wbigpic !=null }">
				<img alt="" src="${firm.wbigpic}">
			</c:if>
			<c:if test="${firm.wbigpic ==null }">
				<img alt="" src="<%=path %>/image/firm.jpg">
			</c:if>
		</div>
		<form action="<%=path%>/pubitem/confirmOrder.do" method="post" data-ajax="false" id="form">
		<input type="hidden" value="${firm.firmid }" id="firmid" name="firmId" />
		<input type="hidden" value="${sft }" id="sft" name="sft" />
		<input type="hidden" value="${dat }" id="dat" name="dat" />
		<input type="hidden" value="${openid }" name="openid" />
		<input type="hidden" name="roomtyp" id="roomtyp" />
		<input type="hidden" name="roomId" id="roomId" />
		<input type="hidden" name="pax" id="pax" />
		<input type="hidden" name="des" id="des" />
		<div id="desks">
			<table>
			<tr>
			<c:forEach items="${firm.listStoreTable }" var="table" varStatus="status">
				<c:if test="${table.roomtyp=='大厅' }">
					<td>
						<c:if test="${table.num==0 }">
							<img src="<%=path %>/image/radio_disable.png"/>
						</c:if>
						<c:if test="${table.num!=0 }">
							<img src="<%=path %>/image/radio_init.png" class="imgRadio" id="${table.pax }"/>
						</c:if>
					</td>
					<td style="text-align:left">&nbsp;&nbsp;&nbsp;${table.pax }人桌 </td>
				</c:if>
				<c:if test="${table.roomtyp=='包间' }">
					<td>
						<c:if test="${table.num==0 }">
							<img src="<%=path %>/image/radio_disable.png"/>
						</c:if>
						<c:if test="${table.num!=0 }">
							<img src="<%=path %>/image/radio_init.png" class="imgRadio" id="room"/>
						</c:if>
					</td>
					<td style="text-align:left">&nbsp;&nbsp;&nbsp;包间</td>
				</c:if>
				<c:if test="${status.count%2==0 }">
					</tr><tr>
				</c:if>
				<c:if test="${status.last }">
					</tr>
				</c:if>
			</c:forEach> 
			</table>
			<span id="suitPax">适合1 ~ 2人</span>
		</div>
		<div class="rooms">
			<table cellpadding="0" cellspacing="0">
			</table>
		</div>
		<div class="next_button_div" style="width:90%;margin:0 auto;">
		    <a href="#" data-role="button" id="next-button" data-ajax="false">下一步
		    </a>
		</div>
		<br/>
		</form>
   </div>
</div>
<a href="#dialogPage" id="dialogbtn" data-rel="dialog"></a>
<div data-role="page" id="dialogPage" data-overlay-theme="e">
  <div data-role="header">
    <h1>温馨提示</h1>
  </div>
  <div data-role="content">
    <p id="hint" style="text-aling:center;">请选择台位</p>
    <a href="#page" data-role="button">确定</a>
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
	});
	//注册下一步点击事件
	$("#next-button").click(function(){
		var pax = $("#pax").val();
		if(pax==null || pax==''){
			$("#dialogbtn").click();
			return;
		}
		InitLayer();//遮罩
		$("#form").submit();
	});
	//点击包间加载包间数据
	$("#room").click(function(){
		$("#pax").val('');
		$("#suitPax").hide();
		var sft = $("#sft").val();
		var dat = $("#dat").val();
		var firmid = $("#firmid").val();
		$("#roomtyp").val('包间');
		$.ajax({
			url:"<%=path%>/pubitem/listRooms.do",
			data:{
				roomtyp:'包间',
				sft:sft,
				dat:dat,
				firmid:firmid
			},
			type:'post',
			success:function(result){
				var html = "<col width='50%'/><col width='30%'/><col width='20%'/><tbody><tr><td>&nbsp;&nbsp;名称</td><td>人数</td><td></td></tr>";
				for(var i=0;i<result.length;i++){
					html += "<tr style='border-top: solid red 1px;'><td>&nbsp;&nbsp;"+result[i].des+"</td><td>"+result[i].pax+"</td><td>";
					html+="<img src='<%=path %>/image/radio_init.png' class='roomtyp' alt='"+result[i].des+"' id='"+result[i].id+"' title='"+result[i].pax+"'/></td></tr>";
				}
				html += "</tbody>";
				$(".rooms").show();
				$(".rooms").find("table").html(html);
				
			}
		});
	});
	//选择桌台
	$("body").on('tap',".imgRadio",function(){
		//图片改变
		var radioInit = "<%=path%>/image/radio_init.png";
		var radioChecked = "<%=path%>/image/radio_checked.png";
		$(".imgRadio").each(function(){
			$(this)[0].src=radioInit;
		})
		$(this)[0].src=radioChecked;
		//处理参数以及数据展示
		var pax = $(this).attr('id');
		if(pax=='room'){
			return;
		}
		$(".rooms").hide();
		$("#suitPax").show();
		$("#roomtyp").val('大厅');
		$("#pax").val(pax);
		//alert(pax);
		$("#suitPax").html("适合1 ~ "+pax+"人");
	});
	//选择包间
	$("body").on('tap',".roomtyp",function(){
		//图片改变
		var radioInit = "<%=path%>/image/radio_init.png";
		var radioChecked = "<%=path%>/image/radio_checked.png";
		$(".roomtyp").each(function(){
			$(this)[0].src=radioInit;
		})
		$(this)[0].src=radioChecked;
		//处理参数
		var roomId = $(this).attr('id');
		var des = $(this).attr('alt');
		var pax = $(this).attr('title');
		$("#roomId").val(roomId);
		$("#des").val(des);
		$("#pax").val(pax);
	});
</script>
</body>
</html>