package com.choice.wxc.domain.diningEvaluate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;


/**
 * 订单评价主表
 * @author 王恒军
 */
public class OlpEvaluate implements RowMapper<OlpEvaluate>{
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 订单主键
	 */
	private String ordersId;

	/**
	 * 总体评价分数
	 */
	private String entiretyPoint;

	/**
	 * 口味评价分数
	 */
	private String tastePoint;

	/**
	 * 环境评价分数
	 */
	private String envPoint;

	/**
	 * 服务评价分数
	 */
	private String servicePoint;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 时间戳
	 */
	private String ts;
	
	/**
	 * 菜品评价列表
	 */
	List<OlpEvaluateDtl> evaluateDtlList = new ArrayList<OlpEvaluateDtl>();

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

	public String getEntiretyPoint() {
		return entiretyPoint;
	}

	public void setEntiretyPoint(String entiretyPoint) {
		this.entiretyPoint = entiretyPoint;
	}

	public String getTastePoint() {
		return tastePoint;
	}

	public void setTastePoint(String tastePoint) {
		this.tastePoint = tastePoint;
	}

	public String getEnvPoint() {
		return envPoint;
	}

	public void setEnvPoint(String envPoint) {
		this.envPoint = envPoint;
	}

	public String getServicePoint() {
		return servicePoint;
	}

	public void setServicePoint(String servicePoint) {
		this.servicePoint = servicePoint;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}
	
	public List<OlpEvaluateDtl> getEvaluateDtlList() {
		return evaluateDtlList;
	}

	public void setEvaluateDtlList(List<OlpEvaluateDtl> evaluateDtlList) {
		this.evaluateDtlList = evaluateDtlList;
	}

	public OlpEvaluate mapRow(ResultSet rs, int i) throws SQLException {
		OlpEvaluate e = new OlpEvaluate();
		e.setId(rs.getString("ID"));
		e.setOrdersId(rs.getString("ORDERSID"));
		e.setEntiretyPoint(rs.getString("ENTIRETYPOINT"));
		e.setEnvPoint(rs.getString("ENVPOINT"));
		e.setRemark(rs.getString("REMARK"));
		e.setServicePoint(rs.getString("SERVICEPOINT"));
		e.setTastePoint(rs.getString("TASTEPOINT"));
		e.setTs(rs.getString("TS"));
		
		return e;
	}
}
