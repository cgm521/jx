<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%String path=request.getContextPath(); %>
<html> 
　<head> 
　 <title></title> 
　 <meta name="viewport" content="width=device-width, initial-scale=1" charset="utf-8"> 
　 <link rel="stylesheet" href="<%=path %>/css/jquery.mobile-1.4.0.min.css" />
   <link rel="stylesheet" href="<%=path %>/css/validate.css" />
　 <script src="<%=path %>/js/jquery-1.11.0.min.js"></script>
　 <script src="<%=path %>/js/jquery.mobile-1.4.0.min.js"></script>
   <script src="<%=path %>/js/validate.js"></script>
</head>
<body>
　<div data-role="page" style="background-color:white;">
	　<button onclick="weixinShareTimeline('a','b','c','<%=path %>/image/regHead.png')">saddfs</button>
　</div>
  <script type="text/javascript">
	  function weixinShareTimeline(title,desc,link,imgUrl){
		  WeixinJSBridge.invoke('shareTimeline',{
			  "img_url":imgUrl,
			  //”img_width”:”640″,
			  //”img_height”:”640″,
			  "link":link,
			  "desc": desc,
			  "title":title
		   });
	   }
  </script>
</body>
</html>