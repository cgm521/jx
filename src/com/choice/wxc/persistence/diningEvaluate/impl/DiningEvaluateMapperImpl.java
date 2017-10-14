package com.choice.wxc.persistence.diningEvaluate.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.choice.wxc.domain.diningEvaluate.NetOrderDtl;
import com.choice.wxc.domain.diningEvaluate.OlpEvaluate;
import com.choice.wxc.domain.diningEvaluate.OlpEvaluateDtl;
import com.choice.wxc.persistence.diningEvaluate.DiningEvaluateMapper;
import com.choice.wxc.util.CodeHelper;

@Repository
public class DiningEvaluateMapperImpl implements DiningEvaluateMapper {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取订单菜品列表
	 * @param dtl
	 * @return
	 */
	public List<NetOrderDtl> getListOrderDtl(NetOrderDtl dtl) {
		StringBuilder sb = new StringBuilder("");
		List<Object> valuesList = new ArrayList<Object>();
		
		sb.append("SELECT O.ORDERSID, O.FOODSID, O.FOODSNAME, NVL(E.IPOINT, 5) AS IPOINT ")
		.append("FROM NET_ORDERDETAIL O ")
		.append("LEFT JOIN OLP_EVALUATEDTL E ON O.ORDERSID = E.VORDERSID AND O.FOODSID = E.PK_PUBITEM  ")
		.append("WHERE O.ORDERSID = ? ")
		.append("AND NOT EXISTS (SELECT 1 FROM OLP_EVALUATEFILTER F WHERE F.PK_PUBITEM = O.FOODSID)");
		valuesList.add(dtl.getOrdersid());
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<NetOrderDtl>() {
			public NetOrderDtl mapRow(ResultSet rs, int i) throws SQLException {
				NetOrderDtl dtl = new NetOrderDtl();
				dtl.setOrdersid(rs.getString("ORDERSID"));
				dtl.setPkPubitem(rs.getString("FOODSID"));
				dtl.setVname(rs.getString("FOODSNAME"));
				dtl.setPoint(rs.getString("IPOINT"));
				
				return dtl;
			}
		});
	}
	
	/**
	 * 根据订单ID获取订单评价详情
	 * @param eva
	 * @return
	 */
	public List<OlpEvaluate> getListEvaluate(OlpEvaluate eva) {
		StringBuilder sb = new StringBuilder("");
		List<Object> valuesList = new ArrayList<Object>();
		
		sb.append("SELECT ID, VORDERSID, IENTIRETYPOINT, ")
		.append("ITASTEPOINT, IENVPOINT, ISERVICEPOINT, VREMARK ")
		.append("FROM OLP_EVALUATE E WHERE E.VORDERSID = ?");
		valuesList.add(eva.getOrdersId());
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<OlpEvaluate>() {
			public OlpEvaluate mapRow(ResultSet rs, int i) throws SQLException {
				OlpEvaluate info = new OlpEvaluate();
				info.setId(rs.getString("ID"));
				info.setOrdersId(rs.getString("VORDERSID"));
				info.setEntiretyPoint(rs.getString("IENTIRETYPOINT"));
				info.setEnvPoint(rs.getString("IENVPOINT"));
				info.setRemark(rs.getString("VREMARK"));
				info.setServicePoint(rs.getString("ISERVICEPOINT"));
				info.setTastePoint(rs.getString("ITASTEPOINT"));
				
				return info;
			}
		});
	}
	
	/**
	 * 保存评价结果
	 * @param evaluateInfo
	 * @return
	 */
	public int saveEvaluateInfo(OlpEvaluate evaluateInfo) {
		StringBuilder sb = new StringBuilder("");
		List<Object> valuesList = new ArrayList<Object>();
		
		sb.append("INSERT INTO OLP_EVALUATE(ID, VORDERSID, IENTIRETYPOINT, ")
		.append("ITASTEPOINT, IENVPOINT, ISERVICEPOINT, VREMARK) ")
		.append("VALUES(?, ?, ?, ?, ?, ?, ?)");
		valuesList.add(CodeHelper.createUUID());
		valuesList.add(evaluateInfo.getOrdersId());
		valuesList.add(evaluateInfo.getEntiretyPoint());
		valuesList.add(evaluateInfo.getTastePoint());
		valuesList.add(evaluateInfo.getEnvPoint());
		valuesList.add(evaluateInfo.getServicePoint());
		valuesList.add(evaluateInfo.getRemark());
		
		// 插入订单评价主表
		int result = jdbcTemplate.update(sb.toString(), valuesList.toArray());
		
		// 订单评价子表
		List<OlpEvaluateDtl> evaluateDtlList = evaluateInfo.getEvaluateDtlList();
		String subSql = "INSERT INTO OLP_EVALUATEDTL(ID, VORDERSID, PK_PUBITEM, IPOINT)"
				+ "VALUES(?, ?, ?, ?)";
		if(null != evaluateDtlList && !evaluateDtlList.isEmpty()) {
			for(OlpEvaluateDtl dtl : evaluateDtlList) {
				sb = new StringBuilder(subSql);
				valuesList = new ArrayList<Object>();
				valuesList.add(CodeHelper.createUUID());
				valuesList.add(dtl.getOrdersId());
				valuesList.add(dtl.getPkPubitem());
				valuesList.add(dtl.getPoint());
				
				result = jdbcTemplate.update(sb.toString(), valuesList.toArray());
			}
		}
		
		return result;
	}
}
