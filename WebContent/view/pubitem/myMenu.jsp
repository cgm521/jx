<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"  charset="UTF-8"/>
<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
<script type="text/javascript">
$(document).on("pageinit","#mymenu",function(){
	$("#commit-right").on("tap",function(){
		$("#confirmForm").submit();
	});
});
</script>
<style type="text/css">
/*页面*/
#mymenu{
	width:100%;
	height:100%;
	text-shadow:0px 0px 0px;
	background:#fff;
	font-family:"方正细圆简体";
}
/*头部*/
#mymenutop{
	width:100%;
	height:40px;
	display:block;
	overflow:auto;
	background:#e4e5e5;
	font-size:16px;
}

/*已点菜品*/
#mymenutop-left{
	float:left;
	width:50%;
	height:100%;
	display:block;
	overflow:hidden;
	background:url(<%=path%>/image/choicemenu.png) no-repeat;
	background-size:cover;
}	

/*清空 加菜*/
#mymenutop-right{
	float:left;
	width:50%;
	height:100%;
	display:block;
	overflow:hidden;
	text-align:center;
	padding:1% 0px 0px 0px;
}

/*主要内容*/
#menumain{
	width:100%;
	height:90%;
	display:block;
	overflow:auto;
	padding:0px;
	
}	
#menu-table{
	width:100%;
	height:auto;
	border-spacing: 0px;
}
#menu-table tr{
	width:100%;
	height:14%;
} 
#menu-table tr td{
	text-align:center;
	padding:1% 5%;
	width:50%;
	border-bottom:2px dotted #dbdbdb;
}
/*菜品名称，价格*/
.cai1{
	width:100%;
	height:auto;
	text-align:left;
	font-size:15px;
}
.cai2{
	width:100%;
	height:auto;
	text-align:left;
	/* color:#ff4747; */
	font-size:10px;
}
/*加菜，减菜*/
.minus{
	float:right;
	width:30%;
	height:100%;
	text-align:center;
	padding:0px;
}
.number{
	float:right;
	width:34px;
	height:36px;
	text-align:center;
	background:url(<%=path%>/image/number.png) no-repeat;
	background-size:cover;
	color:#7a7a7a;
	line-height:38px;
}
.plus{
	float:right;
	width:30%;
	height:100%;
	text-align:center;
	padding:0px;
}
/*特别备注*/
#bak{
	width:100%;
	height:40px;
	background:#f3f3f3;
	line-height:55px;
}
#bak span{
	float:right;
	width:64%;
	height:100%;
	display:block;
	line-height:45px;
	color:red;
}

/**我的菜单**/
#commitdiv{
	position:fixed;
	height:7%;
	left:0px;
	right:0px;
	bottom:0px;
	background:#c9c9c9;
	opacity: 0.9;
	z-index:1;
}
#commit-left{
	float:left;
	width:53%;
	height:100%;
	font-size:12px;
	padding:3% 0 0 2%;
}
#commit-left span{
	color:#ff4747;
}
#commit-right{
	float:right;
	width:45%;
	height:100%;
	background:url(<%=path%>/image/commit.png) no-repeat;
	opacity: 1;
	background-size:cover;
}

</style>
<title>我的菜单</title>
</head>
<body>

<div data-role="page" id="mymenu">
<div id="mymenutop">
	<div id="mymenutop-left">
	</div>
	<div id="mymenutop-right">
		<img src="<%=path %>/image/clear.png" width="35%" height="85%"/>&nbsp;&nbsp;
		<img src="<%=path %>/image/plusmenu.png" width="35%" height="85%"/>
	</div>
</div>
<div id="mymenu-main">
	<table id="menu-table">
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:;">0</div>
				<div class="minus" style="display:;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:;">0</div>
				<div class="minus" style="display:;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:;">0</div>
				<div class="minus" style="display:;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:;">0</div>
				<div class="minus" style="display:;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:;">0</div>
				<div class="minus" style="display:;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:;">0</div>
				<div class="minus" style="display:;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:;">0</div>
				<div class="minus" style="display:;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:;">0</div>
				<div class="minus" style="display:;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:;">0</div>
				<div class="minus" style="display:;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cai1"><img src="<%=path %>/image/cai11.png" width="18px" height="18px"/>海鲜披萨海鲜披萨海鲜披萨海鲜披萨</div>
				<div class="cai2"><img src="<%=path %>/image/cai22.png" width="18px" height="18px"/><span>188</span>元/个</div>
			</td>
			<td>
				<div class="plus"  onclick="plusClick(this)"><img src="<%=path %>/image/plus.png" width="34px" height="34px"/></div>
				<div class="number" style="display:none;">0</div>
				<div class="minus" style="display:none;" onclick="minusClick(this)"><img src="<%=path %>/image/minus.png" width="34px" height="34px"/></div>
			</td>
		</tr>
	</table>
	<div id="bak"><img src="<%=path %>/image/bak.png" width="35%" height="55%"/><span>不要辣！</span></div>
	<br/>
	<br/>
</div>
<div id="commitdiv">
	<div id="commit-left">已点菜品：<span id="menu-count">0</span>&nbsp;&nbsp;&nbsp;总金额：<span id="menu-price">0.0</span>元</div>
	<div id="commit-right" ></div>
</div>
</div>
 <!-- 我的菜单form -->
<form id="confirmForm" action="<%=path%>/view/pubitem/confirmMenu.jsp" method="post" data-ajax="false"></form>
</body>
</html>