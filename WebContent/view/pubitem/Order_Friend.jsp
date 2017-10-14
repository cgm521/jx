<!DOCTYPE html>
<%String path=request.getContextPath(); %>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>好友菜单</title>
	<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
	<link rel="stylesheet" href="<%=path %>/css/validate.css" />
	<script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
	<script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
</head>
<body>
	<div data-role="page" class="jqm-demos" data-quicklinks="true">
		<div data-role="header" class="jqm-header">
			<h2>好友的菜单</h2>
		</div><!-- /header -->
		<div data-role="content" class="ui-body-d tablist-content" >
			<div class="jqm-block-content">
        		<h3>
        			<span style="width:500px;height:100px">门店名字</span>  
        			<span style="left:500px">共计:</span>
        			<span id="Count" style="color:red"></span>
        		</h3>
        	</div>
        	<div id="all-dishes" style="overflow-y: auto">
        	    <table>	
					<tr>
						<td style='width:50px'>#这里放图片#</td>
						<td>
							<table>
								<tr>
									<td>菜名:菜品名"+i+"<br>价格:<br>已被预订_份</td>
					 				<td><input type='text' style='width:40px' name='number' value=''>份
				       				</td>
				       			</tr>
				       		</table>
				       	</td>
				    </tr>
				</table>
       	 	</div>
        </div>
	</div>
</body>
</html>	