package com.choice.wechat.domain.bookDesk;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


/**
 * 城市
 * @author 孙胜彬
 */
public class City implements RowMapper<City>{
	private String pk_group; 
	private String pk_city; //编码
	private String vname; //名称
	public String getPk_city() {
		return pk_city;
	}
	public void setPk_city(String pk_city) {
		this.pk_city = pk_city;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public City mapRow(ResultSet rs, int i) throws SQLException {
		City c = new City();
		c.setPk_group(rs.getString("pk_group"));
		c.setPk_city(rs.getString("pk_city"));
		c.setVname(rs.getString("vname"));
		return c;
	}
}
