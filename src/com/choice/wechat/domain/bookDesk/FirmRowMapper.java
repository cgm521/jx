package com.choice.wechat.domain.bookDesk;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class FirmRowMapper implements RowMapper<Firm>{

	public Firm mapRow(ResultSet rs, int i) throws SQLException {
		Firm f = new Firm();
		f.setAddr(rs.getString("addr"));
		f.setFirmid(rs.getString("firmid"));
		f.setFirmdes(rs.getString("firmdes"));
		f.setPk_city(rs.getString("pk_city"));
		f.setTele(rs.getString("tele"));
		f.setWbigpic(rs.getString("wbigpic"));
		f.setInit(rs.getString("init"));
		f.setDinnerendtime(rs.getString("dinnerendtime"));
		f.setLunchendtime(rs.getString("lunchendtime"));
		f.setPosition(rs.getString("position"));
		f.setTclosetim(rs.getString("tclosetim"));
		f.setTopentim(rs.getString("topentim"));
		f.setStoreupvinit(rs.getString("storeupvinit"));
		return f;
	}

}
