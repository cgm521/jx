package com.choice.wechat.domain.bookDesk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.springframework.jdbc.core.RowMapper;

import com.choice.test.domain.Net_Orders;
import com.choice.test.utils.DateFormat;

public class NetOrderRowMapper implements RowMapper<Net_Orders>{

	public Net_Orders mapRow(ResultSet rs, int i) throws SQLException {
		Net_Orders orders = new Net_Orders();
		DecimalFormat df = new DecimalFormat("####0.00");
		orders.setId(rs.getString("ID"));
		orders.setResv(rs.getString("RESV"));
		orders.setOrderTimes(DateFormat.getStringByDate(rs.getTimestamp("ORDERTIME"), "yyyy-MM-dd HH:mm:ss"));
		orders.setFirmid(rs.getString("FIRMID"));
		orders.setFirmdes(rs.getString("FIRMDES"));
		orders.setDat(DateFormat.getStringByDate(rs.getTimestamp("DAT"), "yyyy-MM-dd"));
		orders.setDatmins(rs.getString("DATEMINS"));
		orders.setPax(rs.getString("PAX"));
		orders.setSft(rs.getString("SFT"));
		orders.setTables(rs.getString("TABLES"));
		orders.setContact(rs.getString("CONTACT"));
		orders.setAddr(rs.getString("ADDR"));
		orders.setOpenid(rs.getString("OPENID"));
		orders.setRannum(rs.getString("RANNUM"));
		orders.setRemark(rs.getString("REMARK"));
		orders.setTele(rs.getString("TELE"));
		orders.setState(rs.getString("STATE"));
		orders.setName(rs.getString("NAME"));
		orders.setRoomtype(rs.getString("ROOMTYPE"));
		orders.setRoompax(rs.getString("ROOMPAX"));
		orders.setMoney(String.valueOf(rs.getDouble("MONEY")));
		orders.setVtransactionid(rs.getString("VTRANSACTIONID"));
		orders.setPaymoney(rs.getString("PAYMONEY") == null ? "" : df.format(rs.getDouble("PAYMONEY")));
		orders.setSumprice(rs.getString("SUMPRICE"));
		orders.setBookDeskOrderID(rs.getString("BOOKDESKORDERID"));
		orders.setVinvoicetitle(rs.getString("VINVOICETITLE"));
		orders.setVcode(rs.getString("FIRMCODE"));
		orders.setTablesname(rs.getString("TABLESNAME"));
		orders.setIsfeast(rs.getString("ISFEAST"));
		orders.setOutTradeNo(rs.getString("OUTTRADENO"));
		orders.setArrtime(rs.getString("ARRTIME"));
		orders.setOrdfrom(rs.getString("ORDFROM"));
		orders.setEvaluationId(rs.getString("EVAID"));
		return orders;
	}

}
