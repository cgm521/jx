<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html> 
<html>
	<head>
		<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">
		<title>查询账单状态</title>
		<link href="<%=path%>/css/jquery.mobile-1.4.0.min.css" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/css/mobiscroll.custom-2.5.0.min.css" rel="stylesheet" type="text/css"/>
		<script src="<%=path%>/js/jquery.js" type="text/javascript"></script>
		<script src="<%=path%>/js/jquery-1.11.0.min.js" type="text/javascript"></script>
		<script src="<%=path%>/js/jquery.mobile-1.4.0.min.js" type="text/javascript"></script>
		<script src="<%=path%>/js/mobiscroll.js" type="text/javascript"></script>
	</head> 
	<body> 
		<div data-role="page" id="page">
		  <div data-role="header" data-theme="b" align="">
			<h1>查询账单状态</h1>
		  </div>
		  <form action="<%=path %>/pubitem/queryfolio.do" id="queryform">
			  <div data-role="fieldcontain">
			    <label for="foliono"><span style="color: red">*</span>账单号：</label>
			    <input type="text" name="foliono" id="foliono" value="${netOrder.resv}">
			    <label for="foliostatus">账单状态：</label>
			    <input type="text" name="foliostatus" id="foliostatus" value="${netOrder.state}">
			    <input type="button" name="query" id="query" value="查询" onclick="queryfolio()" />
			  </div>
		  </form>
		</div>
		<script type="text/javascript">	
			function queryfolio(){
				$("#queryform").submit();
			}
		</script>
	</body>
</html>
