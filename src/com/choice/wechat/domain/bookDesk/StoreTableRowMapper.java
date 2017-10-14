package com.choice.wechat.domain.bookDesk;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class StoreTableRowMapper implements RowMapper<StoreTable>{

	public StoreTable mapRow(ResultSet rs, int i) throws SQLException {
		StoreTable st = new StoreTable();
		ResultSetMetaData md =  rs.getMetaData();
		
		int columnCount = md.getColumnCount();
		for(int j=1;j<=columnCount;j++){
			String cname = md.getColumnName(j);

			StringBuilder sb = new StringBuilder("set")
				.append(cname.charAt(0))
				.append(cname.substring(1).toLowerCase());

			try {
				st.getClass().getMethod(sb.toString(), String.class).invoke(st, rs.getString(j));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*
		if(columnCount==3){
			st.setPax(rs.getString("PAX"));
			st.setNum(rs.getString("NUM"));
			st.setRoomtyp(rs.getString("ROOMTYP"));
		}else if(columnCount==5){
			st.setPax(rs.getString("PAX"));
			st.setId(rs.getString("ID"));
			st.setRoomtyp(rs.getString("ROOMTYP"));
			st.setDes(rs.getString("DES"));
			st.setTbl(rs.getString("TBL"));
		}else{
			st.setId(rs.getString("ID"));
			st.setFirmid(rs.getString("FIRMID"));
			st.setTbl(rs.getString("TBL"));
			st.setPk_group(rs.getString("PK_GROUP"));
		}
		*/
		return st;
	}

}
