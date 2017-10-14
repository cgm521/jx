package com.choice.wechat.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.choice.test.utils.Commons;
import com.choice.wechat.domain.bookDesk.Firm;

public class BaiduMapApi {
	public static String baidumapAK = Commons.baidumap;
	private static Map<String, JSONObject> firmLatLngMap = new HashMap<String, JSONObject>();
	private static Logger log = Logger.getLogger(BaiduMapApi.class);
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public final static String getLocationUrl = "http://api.map.baidu.com/geocoder/v2/?ak=BMAK&coordtype=wgs84ll&location=LAT,LNG&output=json";
	/**
	 * 
	 * @param lat 纬度
	 * @param lng 经度
	 * @return 逆地址解析
	 */
	public static Map<String,String> getLocation(String lat, String lng){
		Map<String,String> map = new HashMap<String,String>();
		map.put("city", "");
		
		if(StringUtils.hasText(lat) && StringUtils.hasText(lng)){
			if(!StringUtils.hasText(baidumapAK)){
				baidumapAK = "oj20za5UvsLB3hpRa54XEXk86ul7dhhX";
			}
			String requestUrl = getLocationUrl.replace("BMAK", baidumapAK).replace("LAT", lat).replace("LNG", lng);
			try{
				JSONObject json = httpRequest(requestUrl, "GET", null);
				if(json.containsKey("result")) {
					map.put("city", json.getJSONObject("result").getJSONObject("addressComponent").getString("city"));
					map.put("address", json.getJSONObject("result").getString("formatted_address"));
				} else {
					LogUtil.writeToTxt(LogUtil.BUSINESS, "getLocation:" + json);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public final static String getLatLngUrl = "http://api.map.baidu.com/geocoder/v2/?ak=BMAK&address=ADDRESS&output=json";
	/**
	 * 
	 * @return 地址解析
	 */
	public static String getLatLng(String firmid, String address){
		String result = "";
		
		if(StringUtils.hasText(address)){
			if(!StringUtils.hasText(baidumapAK)){
				baidumapAK = "oj20za5UvsLB3hpRa54XEXk86ul7dhhX";
			}
			String requestUrl = getLatLngUrl.replace("BMAK", baidumapAK)
					.replace("ADDRESS", address.replaceAll(" ", "").replaceAll(" ", "").trim());
			JSONObject json;
			//门店经纬度放入内存，不必每次调用百度地图API查询
			if(firmLatLngMap.containsKey(firmid)) {
				json = firmLatLngMap.get(firmid);
			} else {
				json = httpRequest(requestUrl, "GET", null);
				firmLatLngMap.put(firmid, json);
			}
			if(null != json && json.containsKey("result")) {
				try{
					StringBuilder sb = new StringBuilder();
					sb.append(json.getJSONObject("result").getJSONObject("location").getString("lat"));
					sb.append(",");
					sb.append(json.getJSONObject("result").getJSONObject("location").getString("lng"));
					result = sb.toString();
				}catch(Exception e){
					log.error("门店地址填写有误，调用百度地图无法定位该地址的经纬度！-地址："+address);
				}
			} else {
				LogUtil.writeToTxt(LogUtil.BUSINESS, "getLatLng:" + json + "！-地址：" + address + ";requestUrl:" + requestUrl);
			}
		}
		return result;
	}
	
	public final static String geoconvUrl = "http://api.map.baidu.com/geoconv/v1/?ak=BMAK&coords=LNG,LAT";
	/**
	 * 
	 * @param lat
	 * @param lng
	 * @return 坐标转换，gps坐标转换为百度坐标
	 */
	public static String geoconv(String lat, String lng){
		String LatLng = "";
		if(StringUtils.hasText(lat) && StringUtils.hasText(lng)){
			if(!StringUtils.hasText(baidumapAK)){
				baidumapAK = "oj20za5UvsLB3hpRa54XEXk86ul7dhhX";
			}
			String requestUrl = geoconvUrl.replace("BMAK", baidumapAK).replace("LAT", lat).replace("LNG", lng);
			try{
				JSONObject json = httpRequest(requestUrl, "GET", null);
				if(json.containsKey("result")) {
					String blng = json.getJSONArray("result").getJSONObject(0).getString("x");
					String blat = json.getJSONArray("result").getJSONObject(0).getString("y");
					return blat+","+blng;
				} else {
					LogUtil.writeToTxt(LogUtil.BUSINESS, "geoconv:" + json);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return LatLng;
	}
	
	public final static String getDistanceUrl = "http://api.map.baidu.com/direction/v1/routematrix?output=json&origins=LAT,LNG&destinations=DESTINATIONS&ak=BMAK";
	 //origins=origin1|orgin2为请求的起点，destinations=destination1|destination2为请求的终点,起终点最多分别为5个
	/**
	 * 
	 * @param list
	 * @param lat
	 * @param lng
	 * @return
	 * 门店距离
	 */
	public static Map<String,String> getDistance(List<Firm> list, String lat, String lng){
		Map<String,String> map = new HashMap<String,String>();
		try{
			if(list == null){
				return map;
			}
			
			if(!StringUtils.hasText(baidumapAK)){
				baidumapAK = "oj20za5UvsLB3hpRa54XEXk86ul7dhhX";
			}
			
			if(!StringUtils.hasText(lat) || !StringUtils.hasText(lng)){
				return map;
			}
			String requestUrl = getDistanceUrl.replace("BMAK", baidumapAK).replace("LAT", lat).replace("LNG", lng);
			String tempUrl = "";
			
			int index = 1;
			StringBuilder sb = new StringBuilder();
			List<Firm> tempFirmList = new ArrayList<Firm>();
			while(index <= list.size()){
				Firm firm = list.get(index-1);
				tempFirmList.add(firm);
				
				if(StringUtils.hasText(firm.getPosition())){
					String position[] = firm.getPosition().split(",");
					sb.append(position[1]).append(",").append(position[0]).append("|");
				}else{
					sb.append(getLatLng(firm.getFirmid(), firm.getAddr())).append("|");
					
				}
				if(index % 5 == 0){//每5个目的地请求一次百度地图
					sb.deleteCharAt(sb.length()-1);
					tempUrl = requestUrl.replace("DESTINATIONS", sb.toString());
					sb.delete(0, sb.length());
					JSONObject json = httpRequest(tempUrl, "GET", null);
					JSONArray array = null;
					try{
						array = json.getJSONObject("result").getJSONArray("elements");
					}catch(Exception e){
						array = new JSONArray();
					}
					
					int i = 0;
					for(Firm f : tempFirmList){
						try{
							JSONObject jo = ((JSONObject)array.get(i)).getJSONObject("distance");
							map.put(f.getFirmid(), jo.getString("text"));
							f.setDistance(jo.getLong("value"));
							f.setDistanceText(map.get(f.getFirmid()));
						}catch(Exception e){
							f.setDistance(Long.MAX_VALUE);
						}
						
						i++;
					}
					tempFirmList.clear();
				}
				index++;
			}
			if(tempFirmList.size()>0){
				sb.deleteCharAt(sb.length()-1);
				tempUrl = requestUrl.replace("DESTINATIONS", sb.toString());
				sb.delete(0, sb.length());
				JSONObject json = httpRequest(tempUrl, "GET", null);
				JSONArray array = null;
				try{
					array = json.getJSONObject("result").getJSONArray("elements");
				}catch(Exception e){
					array = new JSONArray();
				}
				
				int i = 0;
				for(Firm f : tempFirmList){
					try{
						JSONObject jo = ((JSONObject)array.get(i)).getJSONObject("distance");
						map.put(f.getFirmid(), jo.getString("text"));
						f.setDistance(jo.getLong("value"));
						f.setDistanceText(map.get(f.getFirmid()));
					}catch(Exception e){
						f.setDistance(Long.MAX_VALUE);
					}
					i++;
				}
				tempFirmList.clear();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 根据经纬度计算经纬度。
	 * 
	 * */
	/*public static Map<String,String> getDistance(List<Firm> list, String lat, String lng){
		Map<String,String> map = new HashMap<String,String>();
		String firmid = "";
		String firmaddr= "";
		

		if(list == null){
			return map;
		}
		
		if(!StringUtils.hasText(baidumapAK)){
			baidumapAK = "oj20za5UvsLB3hpRa54XEXk86ul7dhhX";
		}
		
		if(!StringUtils.hasText(lat) || !StringUtils.hasText(lng)){
			return map;
		}
		String[] s;
		
		for(Firm f : list){
			try{
				firmid = f.getFirmid();
				firmaddr = f.getAddr();
				if(StringUtils.hasText(f.getPosition())){
					s = f.getPosition().split(",");
				}else if(StringUtils.hasText(f.getAddr())){
					s = getLatLng(f.getFirmid(), f.getAddr()).split(",");
				}else{
					continue;
				}
				if(s.length!=2){
					continue;
				}

				double lat1 = Double.parseDouble(s[0]);
				double lng1 = Double.parseDouble(s[1]);
				double d = GetDistance(lat1,lng1,Double.parseDouble(lat),Double.parseDouble(lng));
				f.setDistance((long)d);
				String disTxt = Math.round(d/100.0)/10.0+"公里";
				map.put(f.getFirmid(), disTxt);
				f.setDistanceText(disTxt);
			}catch(Exception e){
				LogUtil.writeToTxt(LogUtil.BUSINESS, "geoconv:" + "门店地址填写有误，调用百度地图无法定位该地址的经纬度！门店id："+firmid+"-地址："+firmaddr);
			}
			
		}
		
		return map;
	}*/
	private static double EARTH_RADIUS = 6378137;//地球半径  单位 m
	
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
//	   s *=1.25;//加上误差
	   s = Math.round(s);
	   return s;
	}
	public static void main(String args[]){
		//System.out.println(getLatLng("济南市高新区康虹路2677号"));
		//System.out.println(getLatLng("济南市历城区山大路47号"));
		//System.out.println(getLatLng("济南市历城区工业北路63号"));
	}
}
