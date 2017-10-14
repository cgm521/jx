package com.choice.wechat.domain;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
/**
 * list返回结果集通用类
 * @author ZGL
 *
 */
public class ListMapMapper implements RowMapper<Map<String,Object>>{

	public Map<String,Object> mapRow(ResultSet rs, int i) throws SQLException {
		Map<String,Object> map = new HashMap<String,Object>();
		ResultSetMetaData md =  rs.getMetaData();
		
		int columnCount = md.getColumnCount();
		for(int j=1;j<=columnCount;j++){
			String cname = md.getColumnName(j);
			map.put(cname.toLowerCase(), rs.getString(j));
		}
//		map.put("vvouchercode", rs.getString("vvouchercode"));
		return map;
	}

}
