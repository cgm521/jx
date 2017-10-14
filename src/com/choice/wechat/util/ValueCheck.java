package com.choice.wechat.util;

import java.util.List;
import java.util.Map;

/**
 * @author ZGL
 * @version 创建时间：2013-10-12 上午10:29:41
 * 
 */

public class ValueCheck {
	/**
	 * 判断参数非空
	 * @param param
	 * @return
	 */
	public static Boolean IsNotEmpty(Object param){
		if(null !=  param && !"".equals( param) && !"null".equals(param))
			return true;
		else
			return false;
	}
	/**
	 * 判断参数为空
	 * @param param
	 * @return
	 */
	public static Boolean IsEmpty(Object param){
		if(null ==  param || "".equals(param) || "null".equals(param)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 处理空字符串返回数据
	 * @param obj
	 * @return
	 */
	public static String formatStringForBlank(Object obj){
		return ValueCheck.IsEmpty(obj)==true?"":obj.toString();
	}
	/**
	 * 判断参数map非空
	 * @param param
	 * @return
	 */
	public static Boolean IsNotEmptyForMap(Map<String,Object> map){
		if(null !=  map && map.size()>0)
			return true;
		else
			return false;
	}
	/**
	 * 判断参数List非空
	 * @param param
	 * @return
	 */
	public static Boolean IsNotEmptyForList(List<Map<String,Object>> list){
		if(null !=  list && list.size()>0 && !list.isEmpty())
			return true;
		else
			return false;
	}
}
