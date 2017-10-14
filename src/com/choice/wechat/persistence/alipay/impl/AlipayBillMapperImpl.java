package com.choice.wechat.persistence.alipay.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.choice.wechat.persistence.alipay.AlipayBillMapper;

@Repository
public class AlipayBillMapperImpl implements AlipayBillMapper{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static String deleteBillsSql = "DELETE FROM CBOH_ALIPAYBILLS_3CH WHERE PARTNER=? AND DAT=?";
	public void deleteBills(String partner, String dat) {
		if(StringUtils.hasText(partner) && StringUtils.hasText(dat)){
			jdbcTemplate.update(deleteBillsSql,new Object[]{partner,dat});
		}
	}

	private final static String insertBillsSql = "INSERT INTO CBOH_ALIPAYBILLS_3CH(PK_ENPAYORDERS,VTRANSACTIONID,PK_ORDERS,VTYPE,SUB_VTYPE,VSTATUS,VBANKORDERID,IW_ACCOUNT_LOG_ID,NMONEY,VBACKORDERID,VBACKMONEY,VBACKSTATUS,NFEE,NFEERATE,VMEMO,TS,PARTNER,DAT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public void batchInsertBills(List<Map<String, String>> list) {
		if(list!=null && list.size()>0){
			final List<Map<String, String>> tempList = list;
			jdbcTemplate.batchUpdate(insertBillsSql, new BatchPreparedStatementSetter() { 
				
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					Map<String,String> map = tempList.get(i);
				    ps.setString(1, map.get("primaryKey"));
				    ps.setString(2, map.get("trans_out_order_no"));
				    ps.setString(3, map.get("merchant_out_order_no"));
				    ps.setString(4, map.get("trans_code_msg"));
				    ps.setString(5, map.get("sub_trans_code_msg"));
				    ps.setString(6, map.get("deposit_bank_no"));
				    ps.setString(7, map.get("deposit_bank_no"));
				    ps.setString(8, map.get("iw_account_log_id"));
				    try{
				    	ps.setDouble(9, Double.parseDouble(map.get("total_fee")==null?"0":map.get("total_fee")));
				    }catch(Exception e){
				    	ps.setDouble(9,0);
				    	e.printStackTrace();
				    }
				    ps.setString(10, "");
				    ps.setDouble(11,0);//退款金额，支付宝根据业务子类型判断是不是退款
				    ps.setString(12, "");
				    try{
				    	ps.setDouble(13, Double.parseDouble(map.get("service_fee")==null?"0":map.get("service_fee")));
				    }catch(Exception e){
				    	ps.setDouble(13,0);
				    	e.printStackTrace();
				    }
				    ps.setString(14, map.get("rate"));
				    ps.setString(15, map.get("memo"));
				    ps.setString(16, map.get("trans_date"));
				    ps.setString(17, map.get("partner_id"));
				    ps.setString(18, map.get("dat"));
				}

				public int getBatchSize() {
					return tempList.size();
				} 
			}); 
		}
		
	}

}
