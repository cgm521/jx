package com.choice.wechat.persistence.bookDesk.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.Net_Orders;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.domain.ListMapMapper;
import com.choice.wechat.domain.bookDesk.Brands;
import com.choice.wechat.domain.bookDesk.City;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookDesk.FirmRowMapper;
import com.choice.wechat.domain.bookDesk.MealTime;
import com.choice.wechat.domain.bookDesk.NetOrderRowMapper;
import com.choice.wechat.domain.bookDesk.StoreTable;
import com.choice.wechat.domain.bookDesk.StoreTableRowMapper;
import com.choice.wechat.domain.bookDesk.Street;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;

@Repository
public class BookDeskMapperImpl implements BookDeskMapper{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static String getCityListSql = "SELECT PK_GROUP,PK_CITY,VNAME FROM CBOH_CITY_3CH WHERE ENABLESTATE=2 ";
	public List<City> getCityList(String pk_group, String vname) {
		
		StringBuilder sb = new StringBuilder(getCityListSql);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(pk_group)){
			sb.append(" AND pk_group=? ");
			valuesList.add(pk_group);
		}
		
		if(StringUtils.hasText(vname)){
			sb.append(" AND VNAME=? ");
			valuesList.add(vname);
		}
		
		sb.append("ORDER BY VCODE ASC");
		
		return jdbcTemplate.query(sb.toString(),valuesList.toArray(),new City());
	}
	
	private final static String getFirmListSql = "SELECT FIRMID,FIRMDES,PK_CITY,INIT,ADDR,TELE,BIGPIC AS WBIGPIC,LUNCHENDTIME,DINNERENDTIME,POSITION,CBOH_BRAND_3CH.VNAME AS BRANDS,AREA FROM FIRM LEFT JOIN　CBOH_BRAND_3CH ON FIRM.BRANDS=CBOH_BRAND_3CH.VCODE WHERE 1=1 AND ORDERSHOW='Y' ";
	public List<Firm> getFirmList(String pk_group, String pk_city, String firmid) {
		StringBuilder sb = new StringBuilder(getFirmListSql);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(pk_group)){
			sb.append(" AND pk_brand=? ");//租户标识PK_BRAND
			valuesList.add(pk_group);
		}
		
		if(StringUtils.hasText(pk_city)){
			sb.append(" AND pk_city=? ");
			valuesList.add(pk_city);
		}
		
		if(StringUtils.hasText(firmid)){
			sb.append(" AND firmid=? ");
			valuesList.add(firmid);
		}
		
		sb.append("ORDER BY init ASC");
		
		return jdbcTemplate.query(sb.toString(),valuesList.toArray(),new FirmRowMapper());
	}
	
	//"SELECT FIRMID,FIRMDES,PK_CITY,INIT,ADDR,TELE,BIGPIC AS WBIGPIC,LUNCHENDTIME,DINNERENDTIME,POSITION,CBOH_BRAND_3CH.VNAME AS BRANDS,AREA FROM FIRM LEFT JOIN　CBOH_BRAND_3CH ON FIRM.BRANDS=CBOH_BRAND_3CH.VCODE WHERE 1=1 AND ORDERSHOW='Y' ";
	private final static String getFirmListBOH = "SELECT B.PK_STORE AS FIRMID,B.VCODE AS VCODE,B.VNAME AS FIRMDES,B.PK_CITY AS PK_CITY,B.VINIT AS INIT,B.VADDRESS AS ADDR,B.VTEL AS TELE,BIGPIC AS WBIGPIC,LUNCHEND AS LUNCHENDTIME,DINNEREND AS DINNERENDTIME,POSITION,TOPENTIM,TCLOSETIM,CBOH_BRAND_3CH.VNAME AS BRANDS,CBOH_STREET_3CH.VNAME AS AREA";
			
	public List<Firm> getFirmList(Map<String,String> map) {
		String pk_group = map.get("pk_group");
		String pk_city = map.get("pk_city");
		String firmid =  map.get("firmid");
		String brands =  map.get("brands");
		String street =  map.get("street");
		String vname = map.get("vname");
		String openid = map.get("openid");
		String nextType = map.get("nextType");
		String brandFilter = map.get("brandFilter");
		
		StringBuilder colSql = new StringBuilder(getFirmListBOH);
		
		StringBuilder formSql = new StringBuilder(" FROM CBOH_STORE_3CH B ")
			.append("LEFT JOIN　CBOH_BRAND_3CH ON B.PK_BRANDID=CBOH_BRAND_3CH.VCODE ")
			.append("LEFT JOIN CBOH_STREET_3CH ON CBOH_STREET_3CH.PK_STREET=B.PK_STREET ");

		StringBuilder orderbySql =  new StringBuilder("ORDER BY PK_CITY,");
		
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(openid)){
			colSql.append(",NVL(C.VINIT,'-1') AS STOREUPVINIT");
			formSql.append("LEFT JOIN (SELECT PK_STORE,VINIT FROM STORE_UP WHERE OPENID=?) C ON B.PK_STORE=C.PK_STORE ");
			orderbySql.append("STOREUPVINIT DESC, ");
			valuesList.add(openid);
		}else{
			colSql.append(",'-2' AS STOREUPVINIT");
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(colSql).append(formSql);
		
		sb.append(" WHERE 1=1 ");
		
		if(StringUtils.hasText(pk_group)){
			sb.append(" AND B.PK_GROUP=? ");//租户标识PK_BRAND
			valuesList.add(pk_group);
		}
		
		if(StringUtils.hasText(pk_city)){
			sb.append(" AND B.PK_CITY=? ");
			valuesList.add(pk_city);
		}
		
		if(StringUtils.hasText(brands)){
			sb.append(" AND B.PK_BRANDID=? ");
			valuesList.add(brands);
		}else{//如果未选择品牌，并且配置文件中品牌过滤不为空
			if(StringUtils.hasText(brandFilter)){
				sb.append(" AND B.PK_BRANDID IN ( ").append(brandFilter).append(") ");
			}
		}
		
		if(StringUtils.hasText(street)){
			sb.append(" AND B.PK_STREET=? ");
			valuesList.add(street);
		}
		
		if(StringUtils.hasText(firmid)){
			sb.append(" AND B.PK_STORE=? ");
			valuesList.add(firmid);
		}
		//订位
		if("1".equals(nextType)){
			sb.append(" AND B.ORDERSHOW='Y' ");
		}
		//点餐
		if("2".equals(nextType)){
			sb.append(" AND B.NETSHOW='Y' ");
		}
		//外卖
		if("4".equals(nextType)){
			sb.append(" AND B.VISDELIVERY='Y' ");
		}
		
		if(StringUtils.hasText(vname)){
			String temp = vname.replaceAll(".*([';]+|(--)+).*", " ");//过滤非法字符
			sb.append(" AND B.VNAME like '%").append(temp).append("%' ");
		}
		
		sb.append(orderbySql).append("INIT ASC");
		//return jdbcTemplate.query(sb.toString(),valuesList.toArray(),new FirmRowMapper());
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<Firm>(){
			public Firm mapRow(ResultSet rs, int i)	throws SQLException {
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
				f.setFirmCode(rs.getString("vcode"));
				return f;
			}
			
		});
	}
	
	public List<StoreTable> getDeskFormFirm(String pk_group, String firmID, String sft, String dat) {
		/*
		List<Object> valuesList = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder("SELECT RESV.PAX AS PAX,COUNT(RESV.ID) AS NUM ,roomtyp FROM NET_RESVTBL RESV ");
		
		StringBuilder whereSql = new StringBuilder();
		List<Object> tempList = new ArrayList<Object>();
		if(StringUtils.hasText(sft)){
			whereSql.append(" AND ND.SFT= ?");
			tempList.add(sft);
		}
		if(StringUtils.hasText(dat)){
			whereSql.append(" AND ND.DAT=TO_DATE(?,'yyyy-MM-dd')");
			tempList.add(dat);
		}
		
		sb.append(" WHERE 1 = 1  ")
			.append(" AND RESV.ISDEL = 'Y'  ")
			.append(" AND RESV.FIRMID = ?");
		
		valuesList.add(firmID);
		
		sb.append(" AND roomtyp='大厅' ")
			.append("AND RESV.ID NOT IN(")
			.append(" SELECT ND.RESVTBLID FROM NET_DESKTIMES ND WHERE ND.STATE = '1' ")
			.append(whereSql.toString());
		
		valuesList.addAll(tempList);
		
		sb.append(")")
			.append(" GROUP BY RESV.PAX,roomtyp  ")
			.append(" UNION ")
			.append(" SELECT 15 AS PAX,COUNT(RESV.ID) AS NUM ,roomtyp ")
			.append(" FROM NET_RESVTBL RESV  ")
			.append(" WHERE 1 = 1  ")
			.append(" AND RESV.ISDEL = 'Y'  ")
			.append(" AND RESV.FIRMID = ?");

		valuesList.add(firmID);
		
		sb.append("  AND roomtyp='包间' ")
			.append(" AND RESV.ID NOT IN(  ")//not in 有待优化
			.append(" SELECT ND.RESVTBLID FROM NET_DESKTIMES ND WHERE ND.STATE = '1' ")
			.append(whereSql.toString());

		valuesList.addAll(tempList);
		
		sb.append(")")
			.append(" GROUP BY roomtyp ");
		System.out.println(sb.toString());
		*/
		List<Object> valuesList = new ArrayList<Object>();
		
		StringBuilder whereSql = new StringBuilder();
		List<Object> tempList = new ArrayList<Object>();
		if(StringUtils.hasText(sft)){
			whereSql.append(" AND NET_DESKTIMES.SFT= ?");
			tempList.add(sft);
		}
		if(StringUtils.hasText(dat)){
			whereSql.append(" AND NET_DESKTIMES.DAT=TO_DATE(?,'yyyy-MM-dd')");
			tempList.add(dat);
		}
		/*
		SELECT A.ROOMTYP ,B.*,CBOH_SITETYPE_3CH.ISORTNO FROM (SELECT PAX,ROOMTYP FROM NET_RESVTBL GROUP BY PAX,ROOMTYP ) A LEFT JOIN (SELECT RESV.PAX AS PAX,COUNT(RESV.ID) AS NUM,ROOMTYP,MAX(MAXPAX) AS MAXPAX,MIN(MINPAX) AS MINPAX
				FROM NET_RESVTBL RESV LEFT JOIN (SELECT RESVTBLID FROM NET_DESKTIMES WHERE STATE = '1'  AND SFT= '1' AND DAT=TO_DATE('2015-03-15','yyyy-MM-dd')) ND on RESV.ID = ND.RESVTBLID WHERE 1 = 1   AND RESV.ISDEL = 'Y'  AND RESV.FIRMID = '3101' AND ND.RESVTBLID is NULL GROUP BY PAX,ROOMTYP  ) B
				ON A.ROOMTYP=B.ROOMTYP
				LEFT JOIN CBOH_SITETYPE_3CH ON CBOH_SITETYPE_3CH.VNAME=A.ROOMTYP
				ORDER BY ISORTNO
		*/
		/** 因炳胜台位人数设置为0，修改  20150910 **/
		/** 总部台位修改后，主键会改变。修改为RESVTBLID保存台位号 **/
		StringBuilder sb = new StringBuilder("SELECT A.PK_SITETYPE AS ID,A.VNAME AS ROOMTYP,CASE WHEN B.PAX IS NULL THEN 0 ELSE B.PAX END AS PAX ,CASE WHEN B.NUM IS NULL THEN 0 ELSE B.NUM END AS NUM,CASE WHEN B.MAXPAX IS NULL THEN 0 ELSE B.MAXPAX END AS MAXPAX,CASE WHEN B.MINPAX IS NULL THEN 0 ELSE B.MINPAX END AS MINPAX,")
				.append("A.ISORTNO FROM CBOH_SITETYPE_3CH A LEFT JOIN (SELECT IPERSONS AS PAX,COUNT(RESV.PK_SITED) AS NUM,MAX(MAXPAX) AS MAXPAX,MIN(MINPAX) AS MINPAX,ITYP ")
				.append("FROM CBOH_SITEDEFINE_3CH RESV LEFT JOIN (SELECT RESVTBLID,FIRMID FROM NET_DESKTIMES,NET_ORDERS WHERE NET_DESKTIMES.ORDERSID=NET_ORDERS.ID AND NET_DESKTIMES.STATE = '1' ")
				.append(whereSql.toString());
		
		valuesList.addAll(tempList);
		
		sb.append(") ND ON RESV.VCODE = ND.RESVTBLID AND RESV.PK_STOREID=ND.FIRMID WHERE RESV.IFUSE = 'Y' AND RESV.PK_STOREID = ? AND ND.RESVTBLID IS NULL ");
					
		valuesList.add(firmID);
		sb.append(" GROUP BY ITYP,IPERSONS) B ");
		sb.append(" ON A.PK_SITETYPE=B.ITYP WHERE A.ENABLESTATE=2");
		sb.append(" ORDER BY ISORTNO,PAX ");
		return jdbcTemplate.query(sb.toString(),valuesList.toArray(),new StoreTableRowMapper());
	}
	
	/**
	 * 查询可预订的包间或者大厅
	 */
	public List<StoreTable> findResvTbl(String roomTyp, String sft, String dat,
			String firmId,String pax){
		/*
		List<Object> valuesList = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder("SELECT RESV.PAX AS PAX,RESV.ID AS ID,ROOMTYP,TBL AS TBL,RESV.DES AS DES");
		sb.append("FROM NET_RESVTBL RESV  ")
			.append(" WHERE 1 = 1  AND RESV.ISDEL = 'Y' ");
		
		if(StringUtils.hasText(firmId)){
			sb.append(" AND RESV.FIRMID = ?");
			valuesList.add(firmId);
		}
		if(StringUtils.hasText(pax)){
			sb.append(" AND RESV.PAX= ?");
			valuesList.add(pax);
		}
		sb.append(" AND RESV.ID NOT IN( ")
			.append(" SELECT ND.RESVTBLID FROM NET_DESKTIMES ND WHERE ND.STATE = '1' ");
		
		if(StringUtils.hasText(pax)){
			sb.append(" AND RESV.PAX= ?");
			valuesList.add(pax);
		}
		
		if(StringUtils.hasText(sft)){
			sb.append("  AND ND.SFT=?");
			valuesList.add(sft);
		}
		if(StringUtils.hasText(dat)){
			sb.append(" AND ND.DAT=TO_DATE(?,'yyyy-MM-dd')");
			valuesList.add(dat);
		}
		
		sb.append(" ) ");
		*/
		List<Object> valuesList = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder("SELECT RESV.IPERSONS AS PAX,RESV.PK_SITED AS ID,ITYP AS ROOMTYP,VCODE AS TBL,RESV.VNAME AS DES ");
		sb.append("FROM CBOH_SITEDEFINE_3CH RESV  ");
		
		sb.append("LEFT JOIN (SELECT RESVTBLID,FIRMID FROM NET_DESKTIMES,NET_ORDERS WHERE NET_DESKTIMES.ORDERSID=NET_ORDERS.ID AND NET_DESKTIMES.STATE = '1' ");
		
		if(StringUtils.hasText(sft)){
			sb.append(" AND NET_DESKTIMES.SFT= ?");
			valuesList.add(sft);
		}
		if(StringUtils.hasText(dat)){
			sb.append(" AND NET_DESKTIMES.DAT=TO_DATE(?,'yyyy-MM-dd')");
			valuesList.add(dat);
		}
		
		sb.append(" ) ND ON RESV.VCODE = ND.RESVTBLID AND RESV.PK_STOREID=ND.FIRMID");//RESVTBLID保存的为台位号
		
		sb.append(" WHERE RESV.IFUSE = 'Y' ");
		
		if(StringUtils.hasText(firmId)){
			sb.append(" AND RESV.PK_STOREID = ?");
			valuesList.add(firmId);
		}
		if(StringUtils.hasText(pax)){
			sb.append(" AND RESV.MAXPAX >= ?");
			valuesList.add(pax);
		}
		if(StringUtils.hasText(roomTyp)){
			sb.append(" AND RESV.ITYP= ?");
			valuesList.add(roomTyp);
		}
		sb.append(" AND ND.RESVTBLID IS NULL ORDER BY MAXPAX");//按最大人数升序排序
		return jdbcTemplate.query(sb.toString(),valuesList.toArray(),new StoreTableRowMapper());
	}

	private final static String getBookDeskSql = "SELECT VCODE AS ID FROM CBOH_SITEDEFINE_3CH WHERE 1=1 ";
	public String getBookDesk(String id, String firmid) {
		StringBuilder sb = new StringBuilder(getBookDeskSql);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(id)){
			sb.append(" AND PK_SITED = ?");
			valuesList.add(id);
		}
		if(StringUtils.hasText(firmid)){
			sb.append(" AND PK_STOREID= ?");
			valuesList.add(firmid);
		}
		
		sb.append(" FOR UPDATE");
		return jdbcTemplate.queryForObject(sb.toString(), valuesList.toArray(), String.class);
	}
	
	private final static String getCountDeskTimesSql = "SELECT COUNT(NET_DESKTIMES.ID) FROM NET_DESKTIMES,NET_ORDERS WHERE NET_DESKTIMES.ORDERSID=NET_ORDERS.ID ";
	public int getCountDeskTimes(String resvtblid, String ordersid, String sft, String dat, String firmid) {
		List<Object> valuesList = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder(getCountDeskTimesSql);
		if(StringUtils.hasText(resvtblid)){
			sb.append(" AND RESVTBLID=?");//台位号，非主键
			valuesList.add(resvtblid);
		}
		if(StringUtils.hasText(ordersid)){
			sb.append(" AND ORDERSID=?");
			valuesList.add(ordersid);
		}
		if(StringUtils.hasText(sft)){
			sb.append(" AND NET_DESKTIMES.SFT=?");
			valuesList.add(sft);
		}
		if(StringUtils.hasText(dat)){
			sb.append(" AND NET_DESKTIMES.DAT=to_date(?,'yyyy-MM-dd')");
			valuesList.add(dat);
		}
		if(StringUtils.hasText(firmid)){
			sb.append(" AND NET_ORDERS.FIRMID=?");
			valuesList.add(firmid);
		}else{
			return 99999;
		}
		
		sb.append(" AND NET_DESKTIMES.STATE='1'");
		return jdbcTemplate.queryForInt(sb.toString(), valuesList.toArray());
	}
	
	/*总部台位修改后，主键会改变。修改为RESVTBLID保存台位号*/
	private final static String saveDeskTimesSql = "INSERT INTO NET_DESKTIMES(ID, RESVTBLID, DAT, SFT, REMARK, STATE, ORDERSID) VALUES(?,?,to_date(?,'yyyy-MM-dd'),?,?,?,?)"; 
	public void saveDeskTimes(DeskTimes deskTimes){
		List<Object> valuesList = new ArrayList<Object>();
		valuesList.add(deskTimes.getId());
		valuesList.add(deskTimes.getVcode());
		valuesList.add(deskTimes.getDat());
		valuesList.add(deskTimes.getSft());
		valuesList.add(deskTimes.getRemark());
		valuesList.add(deskTimes.getState());
		valuesList.add(deskTimes.getOrdersid());
		jdbcTemplate.update(saveDeskTimesSql, valuesList.toArray());
	}
	
	private final static String saveOrdersSql = "INSERT INTO net_orders(ID, resv, TABLES, pax, ordertime, dat, sft, state, contact, food, nam, firmid, bev, isfeast, addr, datmins, openid, rannum, ordfrom, clientID, arrtime"; 
	public void saveOrders(Net_Orders orders,String clientID) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder cloumn = new StringBuilder("");
		StringBuilder params = new StringBuilder(" values(?,?,?,?,sysdate,to_date(?,'yyyy-mm-dd'),?,'0',?,?,?,?,3,?,?,?,?,?,'WECHAT',?,?");
		
		valuesList.add(orders.getId());
		valuesList.add(orders.getResv());
		valuesList.add(Integer.parseInt(orders.getTables()));
		valuesList.add(Integer.parseInt(orders.getPax()));
		valuesList.add(orders.getDat());
		valuesList.add(orders.getSft());
		valuesList.add(orders.getContact());
		valuesList.add(orders.getFood());
		valuesList.add(orders.getName());
		valuesList.add(orders.getFirmid());
		valuesList.add(orders.getIsfeast());
		valuesList.add(orders.getAddr());
		valuesList.add(orders.getDatmins());
		valuesList.add(orders.getOpenid());
		valuesList.add(orders.getRannum());
		valuesList.add(clientID);
		valuesList.add(orders.getArrtime());
		if(StringUtils.hasText(orders.getPk_group())){
			cloumn.append(", pk_group");
			params.append(",?");
			valuesList.add(orders.getPk_group());
		}
		if(StringUtils.hasText(orders.getRemark())){
			cloumn.append(",remark");
			params.append(",?");
			valuesList.add(orders.getRemark());
		}
		cloumn.append(")");
		params.append(")");
		
		StringBuilder sb = new StringBuilder(saveOrdersSql);
		sb.append(cloumn).append(params);
		jdbcTemplate.update(sb.toString(), valuesList.toArray());
	}
	
	private final static String cancelOrdersSql = "UPDATE NET_ORDERS SET STATE=? WHERE ID=?";
	public void cancelOrders(String id) {
		jdbcTemplate.update(cancelOrdersSql, new Object[]{"3",id});
	}
	
	private final static String cancelOrdersDeskSql = "UPDATE NET_DESKTIMES SET STATE=? WHERE ORDERSID=?";
	public void cancelOrdersDesk(String id) {
		jdbcTemplate.update(cancelOrdersDeskSql, new Object[]{"2",id});
	}
	
	private final static String getOrderMenusSql = " SELECT ORDERS.SUMPRICE AS SUMPRICE, ORDERS.STATE AS STATE,ORDERS.ID AS ID,ORDERS.RESV AS RESV,ORDERS.ORDERTIME AS ORDERTIME,F.PK_STORE AS FIRMID,F.VNAME AS FIRMDES,ORDERS.DAT AS DAT,ORDERS.DATMINS AS DATEMINS,ORDERS.ISFEAST," +
			 " ORDERS.PAX AS PAX,ORDERS.SFT AS SFT,ORDERS.TABLES AS TABLES,ORDERS.CONTACT AS CONTACT,ORDERS.NAM AS NAME,ORDERS.BOOKDESKORDERID AS BOOKDESKORDERID,ORDERS.OUTTRADENO AS OUTTRADENO,ORDERS.ARRTIME," +
			 " F.VADDRESS AS ADDR,F.TELE AS TELE,ORDERS.OPENID AS OPENID,ORDERS.RANNUM AS RANNUM,ORDERS.REMARK AS REMARK,ORDERS.SUMPRICE AS MONEY,CBOH_SITETYPE_3CH.VNAME AS ROOMTYPE,RESVTBL.IPERSONS AS ROOMPAX,ORDERS.VTRANSACTIONID,ORDERS.PAYMONEY,ORDERS.VINVOICETITLE,F.VCODE AS FIRMCODE," +
			 " RESVTBL.VNAME AS TABLESNAME,ORDERS.ORDFROM,EVA.ID AS EVAID " +
			 " FROM NET_ORDERS ORDERS " + 
			 " LEFT JOIN CBOH_STORE_3CH F ON F.PK_STORE=ORDERS.FIRMID LEFT JOIN CBOH_SITEDEFINE_3CH RESVTBL ON F.PK_STORE=RESVTBL.PK_STOREID AND TO_CHAR(ORDERS.TABLES)=RESVTBL.VCODE " + 
			 " LEFT JOIN CBOH_SITETYPE_3CH ON RESVTBL.ITYP=CBOH_SITETYPE_3CH.PK_SITETYPE" + 
			 " LEFT JOIN OLP_EVALUATE EVA ON ORDERS.ID=EVA.VORDERSID" + 
			 " WHERE 1=1 AND ORDERS.DR=0 ";
	public List<Net_Orders> getOrderMenus(Map<String,String> params){
		StringBuilder sbf = new StringBuilder(getOrderMenusSql);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(params.get("id"))){
			sbf.append(" AND ORDERS.ID=?");
			valuesList.add(params.get("id"));
		}
		if(StringUtils.hasText(params.get("state1"))){
			sbf.append(" AND (ORDERS.STATE>? and ORDERS.STATE!=7)");
			valuesList.add(params.get("state1"));
		}
		if(StringUtils.hasText(params.get("state2"))){
			sbf.append(" AND (ORDERS.STATE<=?  or ORDERS.STATE=7)");
			valuesList.add(params.get("state2"));
		}
		if(StringUtils.hasText(params.get("firmid"))){
			sbf.append(" AND F.PK_STORE=?");
			valuesList.add(params.get("firmid"));
		}
		if(StringUtils.hasText(params.get("datetime"))){
			sbf.append(" AND ORDERS.DATMINS=?");
			valuesList.add(params.get("datetime"));
		}
		if(StringUtils.hasText(params.get("dat"))){
			sbf.append(" AND ORDERS.DAT=to_date(?,'yyyy-MM-dd')");
			valuesList.add(params.get("dat"));
		}
		if(StringUtils.hasText(params.get("openid"))){
			sbf.append(" AND ORDERS.OPENID=?");
			valuesList.add(params.get("openid"));
		}
		/*if(StringUtils.hasText(params.get("orderType"))){
			sbf.append(" AND ORDERS.ISFEAST=?");
			valuesList.add(params.get("orderType"));
			if("1".equals(params.get("orderType"))){
				sbf.append(" AND DAT>= TO_DATE(?,'yyyy-MM-dd')");
				valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
			}
		}*/
		if(StringUtils.hasText(params.get("resv"))){
			sbf.append(" AND ORDERS.RESV=?");
			valuesList.add(params.get("resv"));
		}
		
		sbf.append(" ORDER BY ORDERS.ORDERTIME DESC,ORDERS.DAT ASC");
		return jdbcTemplate.query(sbf.toString(),valuesList.toArray(),new NetOrderRowMapper());
	}

	private final static String findRestTblSql = "SELECT RESV.PK_SITED AS ID, RESV.PK_STOREID AS FIRMID, RESV.VCODE AS TBL, S.PK_GROUP AS PK_GROUP FROM CBOH_SITEDEFINE_3CH RESV JOIN CBOH_STORE_3CH S ON RESV.PK_STOREID = S.PK_STORE WHERE RESV.SCENE_ID = ?";
	public List<StoreTable> findRestTbl(String scene_id) {
		return jdbcTemplate.query(findRestTblSql,new Object[]{scene_id},new StoreTableRowMapper());
	}
	/**
	 * 修改订单
	 */
	@Override
	public String updateOrdr(Net_Orders orders) {
		String updateOrderSql="update net_orders set ";
		StringBuffer sbf = new StringBuffer(); 
		List<Object> valuesList = new ArrayList<Object>();


		LogUtil.writeToTxt(LogUtil.BUSINESS, "更新订单时参数："+JSONObject.fromObject(orders).toString());
		
		//----------------------需要更新的值----------------------------
		//台位
		if(StringUtils.hasText(orders.getTables())){
			sbf.append(",tables = ?");
			valuesList.add(orders.getTables());
		}
		//订单状态
		if(StringUtils.hasText(orders.getState())){
			sbf.append(",state = ?");
			valuesList.add(orders.getState());
			if("7".equals(orders.getState())){//骑手端商家确认接单状态修改
				sbf.append(",operatestate = ?");
				valuesList.add("1");
			}
		}
		//实际消费金额
		if(StringUtils.hasText(orders.getRealmoney())){
			sbf.append(",realmoney = ?");
			valuesList.add(orders.getRealmoney());
		}
		//支付金额
		if(StringUtils.hasText(orders.getPaymoney())){
			sbf.append(",paymoney = ?");
			valuesList.add(orders.getPaymoney());
		}
		//财付通支付流水号
		if(StringUtils.hasText(orders.getVtransactionid())){
			sbf.append(",vtransactionid = ?");
			valuesList.add(orders.getVtransactionid());
		}
		//发票抬头
		if(StringUtils.hasText(orders.getVinvoicetitle())){
			sbf.append(",vinvoicetitle = ?");
			valuesList.add(orders.getVinvoicetitle());
		}
		if(!StringUtils.hasText(sbf)){
			LogUtil.writeToTxt(LogUtil.BUSINESS, "更新订单时参数为空");
			return "0";//没有要更新的数据
		}else{
			//格式化时间
			if("oracle".equals(Commons.databaseType)){
				sbf.append(",ORDERTIME=to_date('"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"','yyyy-mm-dd hh24:mi:ss')");
			}else if("sqlserver".equals(Commons.databaseType)){
				sbf.append(",ORDERTIME=cast('"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"' as datetime)");
			}else{
				sbf.append(",ORDERTIME=DATE_FORMA('"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"','%Y-%m-%d %h:%i:%s')");
			}
			sbf = new StringBuffer(updateOrderSql+sbf.substring(1));
		}
		//-----------------------where 条件--------------------------
		if(StringUtils.hasText(orders.getId())){
			sbf.append(" where id = ?");
			valuesList.add(orders.getId());
		}else{
			LogUtil.writeToTxt(LogUtil.BUSINESS, "更新订单时参数主键为空");
			return "-1";//主键不能为空
		}
		if(StringUtils.hasText(orders.getPk_group())){
			sbf.append(" and pk_group = ?");
			valuesList.add(orders.getPk_group());
		}
		return jdbcTemplate.update(sbf.toString(), valuesList.toArray())+"";
	}

	private final static String getDeskStateSql = "SELECT COUNT(*) AS TOTAL,SUM(CASE WHEN ND.RESVTBLID IS NULL THEN 1 ELSE 0 END) AS BOOK FROM CBOH_SITEDEFINE_3CH RESV LEFT JOIN (SELECT RESVTBLID FROM NET_DESKTIMES WHERE STATE = '1' ";
	public Map<String, Object> getDeskState(String firmid, String dat, String sft) {
		StringBuilder sb = new StringBuilder(getDeskStateSql);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(sft)){
			sb.append(" AND SFT= ?");
			valuesList.add(sft);
		}
		if(StringUtils.hasText(dat)){
			sb.append(" AND DAT=TO_DATE(?,'yyyy-MM-dd')");
			valuesList.add(dat);
		}
		
		sb.append(" ) ND ON RESV.PK_SITED = ND.RESVTBLID");
		
		sb.append(" WHERE 1 = 1  AND RESV.IFUSE = 'Y' ");
		
		if(StringUtils.hasText(firmid)){
			sb.append(" AND RESV.PK_STOREID = ?");
			valuesList.add(firmid);
		}
		return jdbcTemplate.queryForMap(sb.toString(), valuesList.toArray());
	}

	private final static String getBrandsSql = "SELECT PK_GROUP,VNAME,PK_BRAND FROM CBOH_BRAND_3CH WHERE 1=1 ";
	public List<Brands> getBrands(String pk_group,String brandFilter) {
		StringBuilder sb = new StringBuilder(getBrandsSql);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(pk_group)){
			sb.append(" AND PK_GROUP= ?");
			valuesList.add(pk_group);
		}
		
		if(StringUtils.hasText(brandFilter)){
			sb.append(" AND PK_BRAND IN (").append(brandFilter).append(")");
		}
		return jdbcTemplate.query(sb.toString(),valuesList.toArray(),new Brands());
	}

	private final static String getFirmActmSql = "select case when vwxshowtyp is null then '-1' else vwxshowtyp end as vwxshowtyp, max(vname) as vname  from cboh_actstr_3ch s join cboh_actm_3ch m on s.PK_ACTM=m.PK_ACTM where enablestate='2' and vapplyto='2' and s.PK_STORE=? group by vwxshowtyp";
	public List<Map<String, Object>> getFirmActm(String firmid) {
		List<Map<String, Object>> list = jdbcTemplate.query(getFirmActmSql, new Object[]{firmid}, new RowMapper<Map<String,Object>>(){
			public Map<String, Object> mapRow(ResultSet rs, int i)
					throws SQLException {
				Map<String,Object> map = new HashMap<String,Object>();
				String vwxshowtyp = rs.getString("vwxshowtyp");
				String vname = rs.getString("vname");
				map.put(vwxshowtyp, vname);
				return map;
			}
			
		});
		return list;
	}

	private final static String getStreetSql = "select pk_street,s.vname from cboh_area_3ch a join cboh_street_3ch s on a.pk_area=s.pk_area where a.PK_CITY=? order by s.vinit";
	public List<Street> getStreet(String pk_city) {
		return jdbcTemplate.query(getStreetSql, new Object[]{pk_city}, new Street());
	}

	private final static String storeUpSql = "INSERT　INTO STORE_UP(PK_STORE,OPENID,PK_GROUP) VALUES(?,?,?)";
	public void storeUp(String firmid, String openid, String pk_group) {
		jdbcTemplate.update(storeUpSql,new Object[]{firmid,openid,pk_group});
	}

	private final static String cancelStoreUpSql = "DELETE FROM STORE_UP WHERE 1=1 ";
	public void cancelStoreUp(String firmid, String openid, String pk_group) {
		StringBuilder sb = new StringBuilder(cancelStoreUpSql);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(firmid)){
			sb.append(" AND PK_STORE= ?");
			valuesList.add(firmid);
		}
		if(StringUtils.hasText(openid)){
			sb.append(" AND OPENID= ?");
			valuesList.add(openid);
		}
		if(StringUtils.hasText(pk_group)){
			sb.append(" AND PK_GROUP= ?");
			valuesList.add(pk_group);
		}
		jdbcTemplate.update(sb.toString(),valuesList.toArray());
	}

	private final static String getMealTimeSql = "SELECT CASE WHEN BISLUNCH='Y' THEN 1 ELSE 0 END AS BISLUNCH,CASE WHEN BISDINNER='Y' THEN 1 ELSE 0 END AS BISDINNER,CASE WHEN BISNIGHT='Y' THEN 1 ELSE 0 END AS BISNIGHT,VLUNCHEONSTW,VLUNCHEONETW,VDINNERSTW,VDINNERETW,VNIGHTEONSTW,VNIGHTEONETW FROM CBOH_STORE_3CH WHERE PK_STORE=?";
	public Map<String, Object> getMealTime(String firmid) {
		return jdbcTemplate.queryForMap(getMealTimeSql, new Object[]{firmid});
	}

	private final static String getMealTimeFromViewSql = "SELECT PK_STORE,VSCODE,VCODE,VNAME,BEGINTIME,ENDTIME FROM VIEW_SFT WHERE PK_STORE=?";
	public List<MealTime> getMealTimeFromView(String firmid){
		return jdbcTemplate.query(getMealTimeFromViewSql, new Object[]{firmid},new RowMapper<MealTime>(){

			public MealTime mapRow(ResultSet rs, int i) throws SQLException {
				MealTime m = new MealTime();
				m.setPk_store(rs.getString("PK_STORE"));
				m.setEndTime(rs.getString("ENDTIME"));
				m.setBeginTime(rs.getString("BEGINTIME"));
				m.setVcode(rs.getString("VCODE"));
				m.setVname(rs.getString("VNAME"));
				m.setVscode(rs.getString("VSCODE"));
				return m;
			}
			
		});
	}
	
	private final static String getMealTimeFromViewCnSql = "SELECT PK_STORE,VSCODE,VSFTCNCODE AS VCODE,VNAME,BEGINTIME,ENDTIME FROM VIEW_SFT WHERE VSFTCNCODE IS NOT NULL AND PK_STORE=? ORDER BY VSFTCNCODE";
	public List<MealTime> getMealTimeFromViewCn(String firmid){
		return jdbcTemplate.query(getMealTimeFromViewCnSql, new Object[]{firmid},new RowMapper<MealTime>(){

			public MealTime mapRow(ResultSet rs, int i) throws SQLException {
				MealTime m = new MealTime();
				m.setPk_store(rs.getString("PK_STORE"));
				m.setEndTime(rs.getString("ENDTIME"));
				m.setBeginTime(rs.getString("BEGINTIME"));
				m.setVcode(rs.getString("VCODE"));
				m.setVname(rs.getString("VNAME"));
				m.setVscode(rs.getString("VSCODE"));
				return m;
			}
			
		});
	}
	
	private final static String getFirmNameSql = "SELECT VNAME FROM　CBOH_STORE_3CH WHERE PK_STORE=?";
	public String getFirmName(String firmid) {
		return jdbcTemplate.queryForObject(getFirmNameSql,new Object[]{firmid}, String.class);
	}
	/**
	 * 支付方式反结算
	 * 将支付方式的金额置为负数存到系统中 将菜品均摊的折扣记录删除
	 */
	private final static String getOrderPaymentSql = "SELECT * FROM NET_FOLIOPAYMENT WHERE resv= (select resv from net_orders where id= ?)";
	@Override
	public void cancelOrderPayment(String id) {
		String insertFolioPayment = "insert into net_foliopayment(pk_foliopayment,resv,vsettlementcode,vsettlementname,visactive," +
				"npayamt,nrefundamt,noveramt,namt,voperator,vpaydate,vpayaccount,nserviceamt,vactmtyp)" +
			" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String delOrderPayment = "delete from net_orderpayment where pk_foliopayment = ?";
		List<Map<String,Object>> listOrderPayment = jdbcTemplate.query(getOrderPaymentSql, new Object[]{id}, new ListMapMapper());
		if(ValueCheck.IsNotEmptyForList(listOrderPayment)){
			for(Map<String,Object> map : listOrderPayment){
				List<Object> valuesList = new ArrayList<Object>();
				
				valuesList.add(CodeHelper.createUUID());//主键
				valuesList.add(map.get("resv"));//账单号
				valuesList.add(map.get("vsettlementcode"));//支付方式编码
				valuesList.add(map.get("vsettlementname"));//支付方式名称
				valuesList.add(map.get("visactive"));//是否活动
				valuesList.add(WeChatUtil.subtractNum("0", map.get("npayamt")));//支付的总金额
				valuesList.add(WeChatUtil.subtractNum("0", map.get("nrefundamt")));//优免金额
				valuesList.add(WeChatUtil.subtractNum("0", map.get("noveramt")));//超收
				valuesList.add(WeChatUtil.subtractNum("0", map.get("namt")));//实收
				valuesList.add(map.get("voperator"));//操作员
				valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//支付时间
				valuesList.add(map.get("vpayaccount"));//付款账号
				valuesList.add(WeChatUtil.subtractNum("0", map.get("nserviceamt")));//手续费
				valuesList.add(map.get("vactmtyp"));//活动类型
				//插入反支付方式
				jdbcTemplate.update(insertFolioPayment, valuesList.toArray());
				//删除菜品折扣信息
				jdbcTemplate.update(delOrderPayment, new Object[]{map.get("pk_foliopayment")});
			}
		}
	}
	/**
	 * 将本次发送的mq信息流水号存储下来
	 * @author ZGL
	 * @date 2015-04-24 10:27:09
	 */
	private final static String insertMqlogsSql = "INSERT INTO WX_MQLOGS(PK_MQLOGS,ORDERID,VTYPE,ERRMSG,STATE) values(?,?,?,?,?)";
	@Override
	public void addMqLogs(Map<String, Object> mqMap) {
		//查询该订单、该type类型是不是已经有记录了，有的话就直接返回不走新增记录方法
		String selctMqlogsSql  = "SELECT PK_MQLOGS,ORDERID,VTYPE,ERRMSG,STATE FROM WX_MQLOGS WHERE VTYPE='"+mqMap.get("vtype")+"' AND ORDERID='"+mqMap.get("orderid")+"'";
		List<Map<String,Object>> listMqlogs = jdbcTemplate.query(selctMqlogsSql, new ListMapMapper());
		if(ValueCheck.IsNotEmptyForList(listMqlogs)){//有的话就直接返回不走新增记录，修改map中的主键
			mqMap.put("pk_mqlogs",listMqlogs.get(0).get("pk_mqlogs"));
			//重置消息方式状态为未发送
			String updateMqlogs = "UPDATE WX_MQLOGS SET STATE = '',VSENDMSG='N' WHERE PK_MQLOGS = '"+listMqlogs.get(0).get("pk_mqlogs")+"'";
			jdbcTemplate.update(updateMqlogs);
		}else{//没有的话就新增记录
			List<Object> valuesList = new ArrayList<Object>();
			valuesList.add(mqMap.get("pk_mqlogs"));
			valuesList.add(mqMap.get("orderid"));
			valuesList.add(mqMap.get("vtype"));
			valuesList.add(mqMap.get("errmsg"));
			valuesList.add(mqMap.get("state"));
			jdbcTemplate.update(insertMqlogsSql, valuesList.toArray());
		}
	}
	/**
	 * 将本次发送的mq信息流水号存储下来
	 * @author ZGL
	 * @date 2015-04-24 10:27:09
	 */
	private final static String updateMqlogsSql = "UPDATE WX_MQLOGS SET ";
	@Override
	public int updateMqlogs(Map<String, Object> mqMap) {
		List<Object> valuesList = new ArrayList<Object>();
		String setSql = "";
		if(ValueCheck.IsEmpty(mqMap.get("pk_mqlogs"))){
			return 0;
		}
		if(ValueCheck.IsNotEmpty(mqMap.get("errmsg"))){
			setSql += ", errmsg = ?";
			valuesList.add(mqMap.get("errmsg"));
		}
		if(ValueCheck.IsNotEmpty(mqMap.get("state"))){
			setSql += ", state = ?";
			valuesList.add(mqMap.get("state"));
		}
		if(ValueCheck.IsNotEmpty(setSql)){
			setSql = setSql.substring(1);
			setSql += " where pk_mqlogs = ? ";
			valuesList.add(mqMap.get("pk_mqlogs"));
		}else{
			return -1;
		}
		return jdbcTemplate.update(updateMqlogsSql+setSql, valuesList.toArray());
	}
	/**
	 * 查询订单信息及店铺信息
	 */
	private final static String queryOrderFromMqlogsSql = "select o.id,o.resv,s.vname,o.openid,o.tables,o.firmid,o.paymoney,o.vtransactionid,o.sumprice "+
			" from net_orders o "+
			" left join wx_mqlogs l on o.id=l.orderid "+
			" left join cboh_store_3ch s on o.firmid = s.pk_store " +
			" where l.pk_mqlogs= ? or l.orderid= ?";
	@Override
	public List<Map<String, Object>> queryOrderFromMqlogs(Map<String, Object> mqMap) {
		return jdbcTemplate.query(queryOrderFromMqlogsSql,new Object[]{mqMap.get("pk_mqlogs"), mqMap.get("pk_mqlogs")}, new ListMapMapper());
	}

	/**
	 * 获取等位类型
	 * @param pk_store
	 * @return
	 */
	private final static String getLineNoSql = "SELECT L.VNAME AS DES, L.BZPAX AS PAX, L.MINPAX AS MINPAX, L.MAXPAX AS MAXPAX "
			+ "FROM CBOH_LINENO_3CH L WHERE L.ENABLESTATE = 2 ";
	public List<StoreTable> getLineNoList(String pk_store) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder(getLineNoSql);
		if(StringUtils.hasText(pk_store)){
			sb.append(" AND L.PK_STORE = ?");
			valuesList.add(pk_store);
		}
		
		sb.append(" ORDER BY L.ISORTNO");
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new StoreTableRowMapper());
	}
	/**
	 * 删除订单
	 */
	private final static String deleteDetails = "UPDATE NET_ORDERS SET DR=1 WHERE ID=?";
	@Override
	public void deleteDetails(Net_Orders orders) {
		List<Object> valuesList = new ArrayList<Object>();
		valuesList.add(orders.getId());
		jdbcTemplate.update(deleteDetails, valuesList.toArray());
	}
	
	/**
	 * 发起我要结帐时更新paymoney为空，否则多次发起时只能取到上一次的数据
	 * @param orders
	 */
	private final static String resetPayMoenySql = "UPDATE NET_ORDERS SET PAYMONEY = NULL, OUTTRADENO = NULL WHERE ID=?";
	@Override
	public void resetPayMoeny(Net_Orders orders) {
		List<Object> valuesList = new ArrayList<Object>();
		valuesList.add(orders.getId());
		jdbcTemplate.update(resetPayMoenySql, valuesList.toArray());
	}

	private final static String getCountOrdersWithOpenidSql = "SELECT COUNT(ID) AS ORDERSNUM FROM NET_ORDERS WHERE DR=0 ";
	public int getCountOrdersWithOpenid(Map<String, String> paramMap) {
		List<Object> valuesList = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder(getCountOrdersWithOpenidSql);
		if(StringUtils.hasText(paramMap.get("openid"))){
			sb.append(" AND OPENID=?");
			valuesList.add(paramMap.get("openid"));
		}
		if(StringUtils.hasText(paramMap.get("dat"))){
			sb.append(" AND DAT=TO_DATE(?,'yyyy-MM-dd')");
			valuesList.add(paramMap.get("dat"));
		}
		if(StringUtils.hasText(paramMap.get("sft"))){
			sb.append(" AND SFT=?");
			valuesList.add(paramMap.get("sft"));
		}
		if(StringUtils.hasText(paramMap.get("isfeast"))){
			sb.append(" AND ISFEAST=?");
			valuesList.add(paramMap.get("isfeast"));
		}
		if(StringUtils.hasText(paramMap.get("state"))){
			sb.append(" AND STATE<=?");
			valuesList.add(paramMap.get("state"));
		}
		if(StringUtils.hasText(paramMap.get("firmid"))){
			sb.append(" AND PK_STORE=?");
			valuesList.add(paramMap.get("firmid"));
		}
		return jdbcTemplate.queryForInt(sb.toString(), valuesList.toArray());
	}

	private final static String getShiftSftSql = "SELECT VCODE,VNAME FROM CBOH_SHIFTSFT_3CH WHERE VCODE IS NOT NULL";
	public Map<String, String> getShiftSft() {
		final Map<String,String> result = new HashMap<String,String>();
		jdbcTemplate.query(getShiftSftSql, new RowMapper<Object>(){
			public Map<String, String> mapRow(ResultSet rs, int i)
					throws SQLException {
				result.put(rs.getString("VCODE"),rs.getString("VNAME"));
				return null;
			}
			
		});
		return result;
	}

	private final static String getFirmSftSql = "SELECT VSFTCNCODE,VNAME FROM VIEW_SFT WHERE VCODE IS NOT NULL AND VSFTCNCODE IS NOT NULL AND PK_STORE=?";
	public Map<String, String> getFirmSft(String firmid) {
		final Map<String,String> result = new HashMap<String,String>();
		jdbcTemplate.query(getFirmSftSql, new Object[]{firmid}, new RowMapper<Object>(){
			public Map<String, String> mapRow(ResultSet rs, int i)
					throws SQLException {
				result.put(rs.getString("VSFTCNCODE"),rs.getString("VNAME"));
				return null;
			}
			
		});
		return result;
	}
}
