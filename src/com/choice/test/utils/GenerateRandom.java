package com.choice.test.utils;

public class GenerateRandom {

	/**
	 * 产生0-9之间的随机数
	 * @return
	 */
	public static String ranNum(){
		return ""+(int)(Math.random()*10);
	}
	
	/**
	 * 得到指定位数的随机数字串
	 * @param num 数字串位数
	 */
	public static String getRanNum(int num){
		if(num<1){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<num;i++){
			String tempNum = ranNum();
			if("0".equals(tempNum) && i == 0) {
				tempNum = "1";
			}
			sb.append(tempNum);
		}
		String result = sb.toString();
		//System.out.println("手机验证码："+result);
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getRanNum(6));
	}
}
