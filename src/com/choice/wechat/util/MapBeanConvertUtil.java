package com.choice.wechat.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapBeanConvertUtil {
	/** 
     * 将一个 Map 对象转化为一个 JavaBean 
     * @param type 要转化的类型 
     * @param map 包含属性值的 map 
     * @return 转化出来的 JavaBean 对象 
	 * @throws Exception 
     * @throws IntrospectionException 
     *             如果分析类属性失败 
     * @throws IllegalAccessException 
     *             如果实例化 JavaBean 失败 
     * @throws InstantiationException 
     *             如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 
     *             如果调用属性的 setter 方法失败 
     */ 
    public static Object convertMap(Class<?> type, Map<String,Object> map) throws Exception{ 
    	try{
    		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性 
            Object obj = type.newInstance(); // 创建 JavaBean 对象 

            // 给 JavaBean 对象的属性赋值 
            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
            for (int i = 0; i< propertyDescriptors.length; i++) { 
                PropertyDescriptor descriptor = propertyDescriptors[i]; 
                String propertyName = descriptor.getName(); 

                if (map.containsKey(propertyName)) { 
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。 
                    Object value = map.get(propertyName); 

                    Object[] args = new Object[1]; 
                    
                    if(value instanceof java.math.BigDecimal){
                    	int intvalue = 0;
                    	intvalue = ((java.math.BigDecimal)value).intValue();
                    	args[0] = intvalue; 
                    }else{
                    	args[0] = value; 
                    }

                    descriptor.getWriteMethod().invoke(obj, args); 
                } 
            } 
            return obj; 
    	}catch(Exception e){
    		throw e;
    	}
    } 

    /** 
     * 将一个 JavaBean 对象转化为一个  Map 
     * @param bean 要转化的JavaBean 对象 
     * @return 转化出来的  Map 对象 
     * @throws Exception 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */ 
    public static Map<String,Object> convertBean(Object bean) throws Exception { 
    	try{
            if(bean==null){
                return null;
            }
    		Class<?> type = bean.getClass(); 
            Map<String,Object> returnMap = new HashMap<String,Object>(); 
            BeanInfo beanInfo = Introspector.getBeanInfo(type); 

            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
            for (int i = 0; i< propertyDescriptors.length; i++) { 
                PropertyDescriptor descriptor = propertyDescriptors[i]; 
                String propertyName = descriptor.getName(); 
                //过滤掉类型为List的属性
                if(descriptor.getPropertyType().getName().equals("java.util.List")){
                	continue;
                }
                if(propertyName.toUpperCase().equals("DR") || propertyName.toUpperCase().equals("TS")){
    				continue;
    			}
                if (!propertyName.equals("class")) { 
                    Method readMethod = descriptor.getReadMethod(); 
                    Object result = readMethod.invoke(bean, new Object[0]); 
                    if (result != null) { 
                    	if(!(result instanceof java.lang.Integer) && !(result instanceof java.lang.String) && !(result instanceof java.lang.Double) && !(result instanceof java.lang.Float) && !(result instanceof java.util.Date)){
                    		Map<String,Object> childmap = convertBean(result);
//                    		returnMap.put(propertyName, result);
                        	for(String key:childmap.keySet()){
                        		returnMap.put(propertyName+"."+key, childmap.get(key));
                        	}
                        }else{
                        	returnMap.put(propertyName, result); 
                        }
                    } else {
                        if((result instanceof java.lang.Integer) ||(result instanceof java.lang.String) ||(result instanceof java.lang.Double) ||(result instanceof java.lang.Float) ||(result instanceof java.util.Date)){
                            returnMap.put(propertyName, "");
                        }
                    }
                } 
            } 
            return returnMap; 
    	}catch(Exception e){
    		throw e;
    	}
    } 
    
    /**
     * 获取属性
     * @param bean
     * @return
     */
    public static String[] getFieldName(Class<?> bean){
    	Field[] fieldArray = bean.getDeclaredFields();
    	List<String> fieldList = new ArrayList<String>();
		for(Field field:fieldArray){
			String property = field.getName();
			if(property.toUpperCase().equals("TABLENAME") || property.toUpperCase().equals("PK_ID")){
				continue;
			}
			if(field.getType().getName().equals("java.util.List")){
				continue;
			}
			fieldList.add(property);
		}
		String[] resultArray = fieldList.toArray(new String[0]);
		return resultArray;
    }
    
    /**
     * 将实体集合转换成map
     * @param list
     * @return
     * @throws Exception
     */
    public static List<Map<String,Object>> convertBeanList(List<?> list){ 
    	List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
    	for(Object obj:list){
    		try {
				resultList.add(convertBean(obj));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return resultList;
    }
    
    /**
     * 将实体集合转换成map(Map的key大写)
     * @param list
     * @return
     * @throws Exception
     */
    public static List<Map<String,Object>> convertBeanListUpper(List<?> list){ 
    	List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
    	for(Object obj:list){
    		try {
				resultList.add(convertBeanUpper(obj));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return resultList;
    }
    
    /**
     * 将实体集合转换成map(Map的key大写)
     * @param list
     * @return
     * @throws Exception
     */
    public static List<Map<String,Object>> convertBeanListUpper(List<Map<String,Object>> prelist,List<?> list){ 
    	List<Map<String,Object>> resultList = prelist;
    	if(null==resultList){
    		resultList = new ArrayList<Map<String,Object>>();
    	}
    	
    	for(Object obj:list){
    		try {
				resultList.add(convertBeanUpper(obj));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return resultList;
    }
    
    /** 
     * 将一个 JavaBean 对象转化为一个  Map(Map的key大写) 
     * @param bean 要转化的JavaBean 对象 
     * @return 转化出来的  Map 对象 
     * @throws Exception 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */ 
    public static Map<String,Object> convertBeanUpper(Object bean) throws Exception { 
    	try{
    		Class<?> type = bean.getClass(); 
            Map<String,Object> returnMap = new HashMap<String,Object>(); 
            BeanInfo beanInfo = Introspector.getBeanInfo(type); 

            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
            for (int i = 0; i< propertyDescriptors.length; i++) { 
                PropertyDescriptor descriptor = propertyDescriptors[i]; 
                String propertyName = descriptor.getName(); 
                //过滤掉类型为List的属性
                if(descriptor.getPropertyType().getName().equals("java.util.List")){
                	continue;
                }
                if(propertyName.toUpperCase().equals("DR") || propertyName.toUpperCase().equals("TS")){
    				continue;
    			}
                if (!propertyName.equals("class")) { 
                    Method readMethod = descriptor.getReadMethod(); 
                    Object result = readMethod.invoke(bean, new Object[0]); 
                    if (result != null) { 
                    	if(!(result instanceof java.lang.Integer) && !(result instanceof java.lang.String) && !(result instanceof java.lang.Double) && !(result instanceof java.lang.Float) && !(result instanceof java.util.Date)){
                    		Map<String,Object> childmap = convertBean(result);
                        	for(String key:childmap.keySet()){
                        		returnMap.put((propertyName+"."+key).toUpperCase(), childmap.get(key));
                        	}
                        }else{
                        	returnMap.put(propertyName.toUpperCase(), result); 
                        }
                    } else { 
                        returnMap.put(propertyName.toUpperCase(), ""); 
                    } 
                } 
            } 
            return returnMap; 
    	}catch(Exception e){
    		throw e;
    	}
    }
    
    public static void main(String[] args) {}
} 
