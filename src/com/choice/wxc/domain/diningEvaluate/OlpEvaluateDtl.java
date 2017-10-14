package com.choice.wxc.domain.diningEvaluate;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


/**
 * 订单评价子表
 * @author 王恒军
 */
public class OlpEvaluateDtl implements RowMapper<OlpEvaluateDtl>{
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 订单主键
	 */
	private String ordersId;

	/**
	 * 菜品主键
	 */
	private String pkPubitem;

	/**
	 * 分数
	 */
	private String point;

	/**
	 * 时间戳
	 */
	private String ts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId;
	}

	public String getPkPubitem() {
		return pkPubitem;
	}

	public void setPkPubitem(String pkPubitem) {
		this.pkPubitem = pkPubitem;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}
	
	public OlpEvaluateDtl mapRow(ResultSet rs, int i) throws SQLException {
		OlpEvaluateDtl e = new OlpEvaluateDtl();
		e.setId(rs.getString("ID"));
		e.setOrdersId(rs.getString("ORDERSID"));
		e.setPkPubitem(rs.getString("PK_PUBITEM"));
		e.setPoint(rs.getString("POINT"));
		e.setTs(rs.getString("TS"));
		
		return e;
	}
}
