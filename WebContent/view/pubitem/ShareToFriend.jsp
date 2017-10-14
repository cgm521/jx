<!DOCTYPE html>
<%String path=request.getContextPath(); %>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title></title>
	<link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
    <link rel="stylesheet" href="<%=path %>/css/validate.css" />
　     <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　     <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
    <script src="<%=path %>/js/validate.js"></script>
</head>
<body>
	<div data-role="page" class="jqm-demos jqm-home">
		<div data-role="header" class="jqm-header">
			<h1>请选择联系人</h1>
			<a href="#" data-role="button" data-icon="search">搜索好友</a>
        </div><!-- /header -->
        <div data-role="content" class="ui-body-d tablist-content" style="overflow-y: scroll"> 
        		<legend>A</legend>  		
        		<input type="checkbox" name="checkbox-h-6a" id="checkbox-h-6a">
        		<label for="checkbox-h-6a">
        			<table>
        				<tr>
        					<td>
        						<img src="<%=path %>/image/02.jpg" style="width:50px;height:50px"></img>
        					</td>
        					<td style="width:100px">
        						<span >A好友1</span>
        					</td>
        				</tr>
        			</table>
        		</label>
        		<input type="checkbox" name="checkbox-h-6b" id="checkbox-h-6b">
        		<label for="checkbox-h-6b">
        			<table>
        				<tr>
        					<td>
        						<img src="<%=path %>/image/02.jpg" style="width:50px;height:50px"></img>
        					</td>
        					<td style="width:100px">
        						<span >B好友2</span>
        					</td>
        				</tr>
        			</table>
        		</label>
        		<input type="checkbox" name="checkbox-h-6b" id="checkbox-h-6c">
        		<label for="checkbox-h-6c">
        			<table>
        				<tr>
        					<td>
        						<img src="<%=path %>/image/02.jpg" style="width:50px;height:50px"></img>
        					</td>
        					<td style="width:100px">
        						<span >C好友2</span>
        					</td>
        				</tr>
        			</table>
        		</label>
        </div><!-- content -->
        <div data-role="footer">
        	<span id="selected"><img src="<%=path %>/image/02.jpg" style="width:30px;height:30px"></img></span>
        	<input type="button" name="submit" value="确定"  ></input>
        </div>
	</div>
	
</body>
</html>