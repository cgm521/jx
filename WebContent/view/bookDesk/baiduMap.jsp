<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<style type="text/css">
		body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;}
		#golist {display: none;}
		@media (max-device-width: 780px){#golist{display: block !important;}}
		a { 
			text-decoration:none;
			color:#3d3d3d;
		}
	</style>
	<script type="text/javascript" src="http://api.map.baidu.com/api?type=quick&ak=${ak}&v=1.0"></script>
	<title>门店导航</title>
</head>
<body>
</body>
</html>
<script type="text/javascript">
	var LatLng = '${LatLng}';
	var position = '${position}';
	var index = LatLng.indexOf(",");
	var lat = LatLng.substring(0,index);
	var lng = LatLng.substring(index+1);
	var start = {
			name : '${address}',
			latlng : new BMap.Point(lng, lat)
		}
		
		position = position + "";
		position = position.replace(/(^\s*)|(\s*$)/g, "");
		var targetPoint = null;
		if(position==null || ""==position){//门店经纬度为空的话，通过地址解析出经纬度
			// 创建地址解析器实例 
			var myGeo = new BMap.Geocoder();  
			// 将地址解析结果显示在地图上，并调整地图视野
			myGeo.getPoint("${addr}", function(point){  
			 if (point) {  
				 targetPoint = point;
			 }  
			}, "${city}");
			var end = {
					name : '${addr}',
					latlng : targetPoint
				}
			var opts = {
					mode : BMAP_MODE_DRIVING,
					region : '${city}'
				}
		var ss = new BMap.RouteSearch();
		ss.routeCall(start, end, opts);
		} else {
			index = position.indexOf(",");
			positionLng = position.substring(0, index);
			positionLat = position.substring(index + 1);
			var end = {
				name : '${addr}',
				latlng : new BMap.Point(positionLng, positionLat)
			}
			var opts = {
				mode : BMAP_MODE_DRIVING,
				region : '${city}'
			}
			var ss = new BMap.RouteSearch();
			ss.routeCall(start, end, opts);
		}
	
</script>
