package com.choice.wechat.domain.bookDesk;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Street implements RowMapper<Street>{
	private String pk_street;
	private String streetName;
	public String getPk_street() {
		return pk_street;
	}
	public void setPk_street(String pk_street) {
		this.pk_street = pk_street;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public Street mapRow(ResultSet rs, int i) throws SQLException {
		Street s = new Street();
		s.setPk_street(rs.getString("pk_street"));
		s.setStreetName(rs.getString("vname"));
		return s;
	}
}
