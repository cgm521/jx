package com.choice.wechat.persistence.takeout.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.choice.test.domain.Net_Orders;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.domain.takeout.Address;
import com.choice.wechat.domain.takeout.RangeCoordi;
import com.choice.wechat.domain.takeout.StoreRange;
import com.choice.wechat.persistence.takeout.TakeOutMapper;

@Repository
public class TakeOutMapperImpl implements TakeOutMapper{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static String getAddressSql = "SELECT PK_ID,OPENID,TELE,CONTENTNAME,ADDRESS,PK_GROUP,UPDATETIME FROM WX_ADDRESS ";
	public List<Address> getAddress(String openid, String pk_id) {
		StringBuilder sb = new StringBuilder(getAddressSql);
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.hasText(openid) || StringUtils.hasText(pk_id)){
			sb.append(" WHERE OPENID = ? ");
			params.add(openid);
			
			if(StringUtils.hasText(pk_id)){
				sb.append(" AND PK_ID = ? ");
				params.add(pk_id);
			}
			
			//sb.replace(0, sb.length(), sb.substring(0, sb.lastIndexOf("AND")));
		}
		
		sb.append(" ORDER BY UPDATETIME DESC");
		
		return jdbcTemplate.query(sb.toString(), params.toArray(),new RowMapper<Address>(){

			public Address mapRow(ResultSet rs, int i) throws SQLException {
				Address a = new Address();
				a.setAddress(rs.getString("ADDRESS"));
				a.setContentName(rs.getString("CONTENTNAME"));
				a.setOpenid(rs.getString("OPENID"));
				a.setPk_group(rs.getString("PK_GROUP"));
				a.setPk_id(rs.getString("PK_ID"));
				a.setTele(rs.getString("TELE"));
				a.setUpdateTime(DateFormat.getStringByDate(rs.getDate("UPDATETIME"), "yyyy-MM-dd HH:mm:ss"));
				return a;
			}
			
		});
	}

	private final static String addAddressSql = "INSERT INTO WX_ADDRESS(PK_ID,OPENID,TELE,CONTENTNAME,ADDRESS,PK_GROUP,UPDATETIME) VALUES(?,?,?,?,?,?,SYSDATE)";
	public void addAddress(Address address) {
		if(address != null){
			jdbcTemplate.update(addAddressSql,new Object[]{address.getPk_id(),address.getOpenid(),address.getTele(),
					address.getContentName(),address.getAddress(),address.getPk_group()});
		}
	}

	public void updateAddress(Address address) {
		
	}

	private final static String updateTakeOutOrdersSql = "UPDATE NET_ORDERS SET STATE=?,ADDR=?,NAM=?,CONTACT=?,DATMINS=?,PAYMENT=?,VINVOICETITLE=?,DELIVERYTIME=? WHERE ID=?";
	public int updateTakeOutOrders(Map<String, String> map) {
		if(map != null){
			if(StringUtils.hasText(map.get("orderid"))){
				return jdbcTemplate.update(updateTakeOutOrdersSql, new Object[] {map.get("state"), map.get("addr"), map.get("name"), 
						map.get("tele"), map.get("datmin"), map.get("payment"), map.get("vinvoicetitle"), map.get("deliverytime"), map.get("orderid")});
			}
		}
		return 0;
	}

	/**
	 * 获取订单列表
	 * 
	 * @param params
	 * @return
	 */
	private final static String getOrderMenusSql = " SELECT ORDERS.SUMPRICE AS SUMPRICE, ORDERS.STATE AS STATE,ORDERS.ID AS ID,ORDERS.RESV AS RESV,ORDERS.ORDERTIME AS ORDERTIME,F.PK_STORE AS FIRMID,F.VNAME AS FIRMDES,ORDERS.DAT AS DAT,ORDERS.DATMINS AS DATEMINS,"
			+ " ORDERS.PAX AS PAX,ORDERS.SFT AS SFT,ORDERS.TABLES AS TABLES,ORDERS.CONTACT AS CONTACT,ORDERS.NAM AS NAME,ORDERS.BOOKDESKORDERID AS BOOKDESKORDERID,ORDERS.PAYMENT,"
			+ " ORDERS.ADDR,F.VTEL AS TELE,ORDERS.OPENID AS OPENID,ORDERS.RANNUM AS RANNUM,ORDERS.REMARK AS REMARK,ORDERS.SUMPRICE AS MONEY,RESVTBL.ROOMTYP AS ROOMTYPE,RESVTBL.PAX AS ROOMPAX,ORDERS.VTRANSACTIONID,ORDERS.PAYMONEY,ORDERS.VINVOICETITLE " +
			"FROM NET_ORDERS ORDERS LEFT JOIN CBOH_STORE_3CH F ON F.PK_STORE=ORDERS.FIRMID LEFT JOIN NET_RESVTBL RESVTBL ON F.PK_STORE=RESVTBL.FIRMID AND ORDERS.TABLES=RESVTBL.TBL WHERE 1=1 AND ORDERS.DR=0 ";

	public List<Net_Orders> getOrderMenus(Map<String, String> params) {
		StringBuilder sbf = new StringBuilder(getOrderMenusSql);
		List<Object> valuesList = new ArrayList<Object>();

		if (StringUtils.hasText(params.get("id"))) {
			sbf.append(" AND ORDERS.ID=?");
			valuesList.add(params.get("id"));
		}
		if (StringUtils.hasText(params.get("state1"))) {
			sbf.append(" AND ORDERS.STATE>?");
			valuesList.add(params.get("state1"));
		}
		if (StringUtils.hasText(params.get("state2"))) {
			sbf.append(" AND ORDERS.STATE<=?");
			valuesList.add(params.get("state2"));
		}
		if (StringUtils.hasText(params.get("firmid"))) {
			sbf.append(" AND F.PK_STORE=?");
			valuesList.add(params.get("firmid"));
		}
		if (StringUtils.hasText(params.get("datetime"))) {
			sbf.append(" AND ORDERS.DATMINS=?");
			valuesList.add(params.get("datetime"));
		}
		if (StringUtils.hasText(params.get("dat"))) {
			sbf.append(" AND ORDERS.DAT=to_date(?,'yyyy-MM-dd')");
			valuesList.add(params.get("dat"));
		}
		if (StringUtils.hasText(params.get("openid"))) {
			sbf.append(" AND ORDERS.OPENID=?");
			valuesList.add(params.get("openid"));
		}
		if(StringUtils.hasText(params.get("orderType"))){
			sbf.append(" AND ORDERS.ISFEAST=?");
			valuesList.add(params.get("orderType"));
			if("1".equals(params.get("orderType"))){
				sbf.append(" AND DAT>= TO_DATE(?,'yyyy-MM-dd')");
				valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
			}
		}
		/*
		 * sbf.append(" AND DAT>= TO_DATE(?,'yyyy-MM-dd')");
		 * valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
		 */

		sbf.append(" ORDER BY ORDERS.DAT ASC");
		return jdbcTemplate.query(sbf.toString(), valuesList.toArray(), new RowMapper<Net_Orders>(){

			public Net_Orders mapRow(ResultSet rs, int i) throws SQLException {
				Net_Orders orders = new Net_Orders();
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
				orders.setPaymoney(rs.getString("PAYMONEY"));
				orders.setMoney(rs.getString("SUMPRICE"));
				orders.setBookDeskOrderID(rs.getString("BOOKDESKORDERID"));
				orders.setPayment(rs.getString("PAYMENT"));
				orders.setVinvoicetitle(rs.getString("VINVOICETITLE"));
				return orders;
			}
		});
	}
	
	/**
	 * 获取门店配送范围列表
	 * @param pkGroup
	 * @param pkStore
	 * @return
	 */
	public List<StoreRange> getListStoreRange(String pkGroup, String pkStore) {
		StringBuilder sb = new StringBuilder("");
		List<Object> valuesList = new ArrayList<Object>();
		
		sb.append("SELECT S.PK_STORERANGE, S.PK_GROUP, S.PK_STORE, S.IAREASEQ, S.VAREANAME, S.NSTARTPRICE, S.NDISTRIBUTFEE, ")
		.append("S.VCOLOR, S.TS, S.VDEF1, S.VDEF2, S.VDEF3, S.VDEF4, S.VDEF5 FROM CBOH_STORERANGE_3CH S ")
		.append("WHERE 1= 1 ");
		
		if(StringUtils.hasText(pkGroup)) {
			sb.append("AND S.PK_GROUP = ? ");
			valuesList.add(pkGroup);
		}
		
		if(StringUtils.hasText(pkStore)) {
			sb.append("AND S.PK_STORE = ? ");
			valuesList.add(pkStore);
		}
		
		sb.append("ORDER BY S.IAREASEQ ");
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<StoreRange>() {
			public StoreRange mapRow(ResultSet rs, int i) throws SQLException {
				StoreRange sr = new StoreRange();
				sr.setPkStorerange(rs.getString("PK_STORERANGE"));
				sr.setPkGroup(rs.getString("PK_GROUP"));
				sr.setPkStore(rs.getString("PK_STORE"));
				sr.setIareaseq(rs.getInt("IAREASEQ"));
				sr.setVareaname(rs.getString("VAREANAME"));
				sr.setNstartprice(rs.getDouble("NSTARTPRICE"));
				sr.setNdistributfee(rs.getDouble("NDISTRIBUTFEE"));
				sr.setVcolor(rs.getString("VCOLOR"));
				sr.setTs(rs.getString("TS"));
				sr.setVdef1(rs.getString("VDEF1"));
				sr.setVdef2(rs.getString("VDEF2"));
				sr.setVdef3(rs.getString("VDEF3"));
				sr.setVdef4(rs.getString("VDEF4"));
				sr.setVdef5(rs.getString("VDEF5"));
				
				List<RangeCoordi> listRC = new ArrayList<RangeCoordi>();
				sr.setListRangeCoordi(listRC);
				
				return sr;
			}
		});
	}
	
	/**
	 * 获取门店配送范围坐标点列表
	 * @param pkGroup
	 * @param pkStore
	 * @return
	 */
	public List<RangeCoordi> getListRangeCoordi(String pkGroup, String pkStore) {
		StringBuilder sb = new StringBuilder("");
		List<Object> valuesList = new ArrayList<Object>();
		
		sb.append("SELECT R.PK_RANGECOORDI, R.PK_GROUP, R.PK_STORE, R.IAREASEQ, R.VLNG, R.VLAT, R.TS, ")
		.append("R.VDEF1, R.VDEF2, R.VDEF3, R.VDEF4, R.VDEF5 FROM CBOH_RANGECOORDI_3CH R ")
		.append("WHERE 1= 1 ");
		
		if(StringUtils.hasText(pkGroup)) {
			sb.append("AND R.PK_GROUP = ? ");
			valuesList.add(pkGroup);
		}
		
		if(StringUtils.hasText(pkStore)) {
			sb.append("AND R.PK_STORE = ? ");
			valuesList.add(pkStore);
		}
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<RangeCoordi>() {
			public RangeCoordi mapRow(ResultSet rs, int i) throws SQLException {
				RangeCoordi rc = new RangeCoordi();
				rc.setPkRangecoordi(rs.getString("PK_RANGECOORDI"));
				rc.setPkGroup(rs.getString("PK_GROUP"));
				rc.setPkStore(rs.getString("PK_STORE"));
				rc.setIareaseq(rs.getInt("IAREASEQ"));
				rc.setVlng(rs.getString("VLNG"));
				rc.setVlat(rs.getString("VLAT"));
				rc.setTs(rs.getString("TS"));
				rc.setVdef1(rs.getString("VDEF1"));
				rc.setVdef2(rs.getString("VDEF2"));
				rc.setVdef3(rs.getString("VDEF3"));
				rc.setVdef4(rs.getString("VDEF4"));
				rc.setVdef5(rs.getString("VDEF5"));
				
				return rc;
			}
		});
	}
}
