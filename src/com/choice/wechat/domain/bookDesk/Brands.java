package com.choice.wechat.domain.bookDesk;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Brands implements RowMapper<Brands>{
	private String pk_group;
	private String pk_brand;
	private String vname;
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getPk_brand() {
		return pk_brand;
	}
	public void setPk_brand(String pk_brand) {
		this.pk_brand = pk_brand;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	
	public Brands mapRow(ResultSet rs, int i) throws SQLException {
		Brands sb = new Brands();
		sb.setPk_group(rs.getString("PK_GROUP"));
		sb.setPk_brand(rs.getString("PK_BRAND"));
		sb.setVname(rs.getString("VNAME"));
		return sb;
	}
}
