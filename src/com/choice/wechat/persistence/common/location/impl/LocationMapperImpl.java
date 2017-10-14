package com.choice.wechat.persistence.common.location.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.choice.wechat.persistence.common.location.LocationMapper;

@Repository
public class LocationMapperImpl implements LocationMapper{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static String getLocationSql = "SELECT LAT,LNG FROM WX_LOCATION WHERE OPENID=?";
	public Map<String, Object> getLocation(String openid) {
		Map<String,Object> map = null;
		if(StringUtils.hasText(openid)){
			try{
				List<Map<String,Object>> list = jdbcTemplate.query(getLocationSql,new Object[]{openid},new LocationRowMapper());
				if(list != null && list.size()>0){
					map = list.get(0);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return map;
	}

	private final static String insertLocationSql = "INSERT INTO WX_LOCATION(OPENID,LAT,LNG,CREATETIME) VALUES(?,?,?,?)";
	private final static String updateLocationSql = "UPDATE WX_LOCATION SET LAT=?,LNG=?,CREATETIME=? WHERE OPENID=?";
	public void saveOrUpdate(String openid, String lat, String lng, String time) {
		
		if(!StringUtils.hasText(openid)){
			return;
		}
		
		int count = jdbcTemplate.update(updateLocationSql, new Object[]{lat,lng,time,openid});
		if(count <= 0){
			jdbcTemplate.update(insertLocationSql, new Object[]{openid,lat,lng,time});
		}
	}


	private class LocationRowMapper implements RowMapper<Map<String,Object>>{
		public Map<String, Object> mapRow(ResultSet rs, int i)
				throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("LAT", rs.getString("LAT"));
			map.put("LNG", rs.getString("LNG"));
			return map;
		}
		
	}
}
