package com.choice.wxc.util;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonFormatUtil {

	/**
	 * 通过对象返回json串
	 * @param object
	 * @return
	 */
	public static String getObjectJson(Object object){
		return JSONObject.fromObject(object).toString();
	}
	
	/**
	 * 通过集合返回json串
	 * @param prelist
	 * @return
	 */
	public static String getArrayJson(List<?> prelist){
		return JSONArray.fromObject(prelist).toString();
	}
	
	/**
	 * 通过json串获取对象
	 * @param jsonString
	 * @param bean
	 * @return
	 */
	public static Object formatObjectJson(String jsonString,Class<?> bean){
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return JSONObject.toBean(jsonObject, bean);
	}
	
	/**
	 * 通过json串获取list集合
	 * @param jsonString
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Class<?>> formatArrayJson(String jsonString,Class<?> bean){
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return (List<Class<?>>)JSONArray.toCollection(jsonArray, bean);
	}
	
	/**
	 * 通过json串获取主子表对象
	 * @param jsonString
	 * @param parentclass 父类
	 * @param childrenClassMap 子类map key为属性名 value为子表类
	 * @return
	 */
	public static Object formatAggObjectJson(String jsonString,Class<?> parentclass,Map<String, Class<?>> childrenClassMap)  throws Exception{
		try{
			if(ValueCheck.IsEmpty(jsonString)){
				return null;
			}
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(parentclass);   
	        jsonConfig.setClassMap(childrenClassMap); 
			return JSONObject.toBean(jsonObject, jsonConfig);
		}catch(Exception e){
			throw new Exception(e);
		}
	}
	
	public static void main(String[] args) {
	}
}
