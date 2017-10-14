package com.choice.wxc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {

	/**
	 * 根据日期和转换格式得到字符串
	 * @param date 日期
	 * @param formatType 转换格式 例如  yyyy-MM-dd
	 * @return
	 */
	public static String getStringByDate(Date date,String formatType){
		return new SimpleDateFormat(formatType).format(null==date?new Date():date);
	}
	
	/**
	 * 根据日期字符串和转换格式得到日期
	 * @param date 日期字符串 例如 20120506
	 * @param formatType 转换格式 例如 yyyyMMdd
	 * @return
	 */
	public static Date getDateByString(String date,String formatType){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(formatType);
			return sdf.parse(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据传入的日期获取指定类型的日期对象
	 * @param date 传入的日期
	 * @param formatType 转换格式
	 * @return
	 */
	public static Date formatDate(Date date,String formatType){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(formatType);
			String dateString = sdf.format(null==date?new Date():date);
			return sdf.parse(dateString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据传入的日期和类型 得到指定时间  (例如 getDateBefore（new Date(),"day",-1,5） 为得到当前日期之前5天的日期)
	 * @param date 指定日期
	 * @param type 日期类型  year month day
	 * @param beforeOrAfter  之前还是之后  -1为之前 1为之后 
	 * @param number  指定的数字   前5个月  即为5
	 * @return 结果日期
	 */
	public static Date getDateBefore(Date date,String type,int beforeOrAfter,int number){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(beforeOrAfter==1){
			if(type.equals("day")){
				calendar.add(Calendar.DATE, number);
			}else if(type.equals("month")){
				calendar.add(Calendar.MONTH, number);
			}else if(type.equals("year")){
				calendar.add(Calendar.YEAR, number);
			}
		}else{
			if(type.equals("day")){
				calendar.add(Calendar.DATE, -number);
			}else if(type.equals("month")){
				calendar.add(Calendar.MONTH, -number);
			}else if(type.equals("year")){
				calendar.add(Calendar.YEAR, -number);
			}
		}
		
		return calendar.getTime();
	}
	
	/**
	 * 得到当月第一天的日期
	 * @return
	 */
	public static Date getFirstDayOfCurrMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		return calendar.getTime();
	}
	/**
	 * 得到当月最后一天的日期
	 * @return
	 */
	public static Date getEndDayOfCurrMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); 
		calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
		return calendar.getTime();
	}
	/**
	 * 得到当年第一天的日期
	 * @return
	 */
	public static Date getFirstDayOfCurrYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}
	
	/**
	 * 得到指定年份第一天的日期
	 * @return
	 */
	public static Date getFirstDayOfCurrYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}
	
	/**
	 * 根据传入的日期得到天
	 * @param args
	 */
	public static int getDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}
	
	/**
	 * 根据传入的日期得到月
	 * @param args
	 */
	public static int getMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH)+1;
	}
	
	/**
	 * 根据传入的日期得到年
	 * @param args
	 */
	public static int getYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	//测试
	public static void main(String[] args) {
		System.out.println(getEndDayOfCurrMonth(getDateByString("2013-06-01", "yyyy-MM-DD")));
	}
}
