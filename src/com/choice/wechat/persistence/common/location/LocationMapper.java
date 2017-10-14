package com.choice.wechat.persistence.common.location;

import java.util.Map;

public interface LocationMapper {
	/**
	 * 
	 * @param openid
	 * @return
	 */
	public Map<String,Object> getLocation(String openid);
	
	/**
	 * 微信号认为是唯一的，在某一时刻认为只有一个微信号发送了地理位置，可忽略并发问题
	 * @param openid
	 * @param lat 纬度
	 * @param lng 经度
	 * @param time 创建时间
	 */
	public void saveOrUpdate(String openid,String lat,String lng,String time);
}
