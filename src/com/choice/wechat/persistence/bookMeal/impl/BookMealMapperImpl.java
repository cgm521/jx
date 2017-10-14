package com.choice.wechat.persistence.bookMeal.impl;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_OrderPackageDetail;
import com.choice.test.domain.Net_Orders;
import com.choice.test.domain.Voucher;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.domain.ListMapMapper;
import com.choice.wechat.domain.bookDesk.City;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookDesk.NetOrderRowMapper;
import com.choice.wechat.domain.bookDesk.StoreTable;
import com.choice.wechat.domain.bookDesk.StoreTableRowMapper;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.bookMeal.FdItemType;
import com.choice.wechat.domain.bookMeal.GroupActm;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.domain.bookMeal.ProdReqAdd;
import com.choice.wechat.domain.bookMeal.ProductAdditional;
import com.choice.wechat.domain.bookMeal.ProductRedfine;
import com.choice.wechat.domain.bookMeal.ProductReqAttAc;
import com.choice.wechat.domain.bookMeal.SellOff;
import com.choice.wechat.domain.takeout.StoreRange;
import com.choice.wechat.persistence.WeChatPay.WxPayMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.takeout.TakeOutMapper;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.web.controller.myCard.MyCardController;
import com.choice.wxc.util.LogUtil;

@Repository
public class BookMealMapperImpl implements BookMealMapper {

	@Autowired
	private WxPayMapper wxPayMapper;
	@Autowired
	private TakeOutMapper takeOutMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplateCrm;
	
	private final static String getCityListSql = "SELECT PK_GROUP,PK_CITY,VNAME FROM CBOH_CITY_3CH WHERE ENABLESTATE=2 ";

	public List<City> getCityList(String pk_group, String vname) {

		StringBuilder sb = new StringBuilder(getCityListSql);
		List<Object> valuesList = new ArrayList<Object>();

		if (StringUtils.hasText(pk_group)) {
			sb.append(" AND pk_group=? ");
			valuesList.add(pk_group);
		}

		if (StringUtils.hasText(vname)) {
			sb.append(" AND VNAME=? ");
			valuesList.add(vname);
		}

		sb.append("ORDER BY VCODE ASC");

		return jdbcTemplate.query(sb.toString(), valuesList.toArray(),
				new City());
	}

	//private final static String getFirmListSql = "SELECT FIRMID,FIRMDES,PK_CITY,INIT,ADDR,TELE,BIGPIC AS WBIGPIC,LUNCHENDTIME,DINNERENDTIME FROM FIRM WHERE ORDERSHOW = 'Y' ";
	private final static String getFirmListSql = "SELECT PK_STORE AS FIRMID,VNAME AS FIRMDES,PK_CITY,VINIT AS INIT,VADDRESS AS ADDR,"
			+ "VTEL AS TELE,BIGPIC AS WBIGPIC,VLUNCHEONETW AS LUNCHENDTIME,VDINNERETW AS DINNERENDTIME,VCODE AS FIRMCODE, "
			+ "VTPACCOUNT AS VTPACCOUNT, VTPKEY AS VTPKEY, VAREAORDER AS VAREAORDER FROM cboh_store_3ch WHERE ENABLESTATE = 1 ";
	public List<Firm> getFirmList(String pk_group, String pk_city, String firmid) {

		StringBuilder sb = new StringBuilder(getFirmListSql);
		List<Object> valuesList = new ArrayList<Object>();

		if (StringUtils.hasText(pk_group)) {
			//sb.append(" AND pk_brand=? ");// 租户标识PK_BRAND
			sb.append(" AND PK_GROUP = ? ");// 租户标识PK_BRAND
			valuesList.add(pk_group);
		}

		if (StringUtils.hasText(pk_city)) {
			sb.append(" AND pk_city=? ");
			valuesList.add(pk_city);
		}

		if (StringUtils.hasText(firmid)) {
			//sb.append(" AND firmid=? ");
			sb.append(" AND PK_STORE = ? ");
			valuesList.add(firmid);
		}

		sb.append("ORDER BY init ASC");

		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<Firm>(){
			public Firm mapRow(ResultSet rs, int i)	throws SQLException {
				Firm f = new Firm();
				f.setAddr(rs.getString("ADDR"));
				f.setFirmid(rs.getString("FIRMID"));
				f.setFirmdes(rs.getString("FIRMDES"));
				f.setPk_city(rs.getString("PK_CITY"));
				f.setTele(rs.getString("TELE"));
				f.setWbigpic(rs.getString("WBIGPIC"));
				f.setInit(rs.getString("INIT"));
				f.setDinnerendtime(rs.getString("DINNERENDTIME"));
				f.setLunchendtime(rs.getString("LUNCHENDTIME"));
				f.setFirmCode(rs.getString("FIRMCODE"));
				f.setVtpaccount(rs.getString("VTPACCOUNT"));
				f.setVtpkey(rs.getString("VTPKEY"));
				f.setVareaOrder(rs.getString("VAREAORDER"));
				return f;
			}
			
		});
	}

	public List<StoreTable> getDeskFormFirm(String pk_group, String firmID,
			String sft, String dat) {
		List<Object> valuesList = new ArrayList<Object>();

		StringBuilder whereSql = new StringBuilder();
		List<Object> tempList = new ArrayList<Object>();
		if (StringUtils.hasText(sft)) {
			whereSql.append(" AND SFT= ?");
			tempList.add(sft);
		}
		if (StringUtils.hasText(dat)) {
			whereSql.append(" AND DAT=TO_DATE(?,'yyyy-MM-dd')");
			tempList.add(dat);
		}

		StringBuilder sb = new StringBuilder(
				"SELECT RESV.PAX AS PAX,COUNT(RESV.ID) AS NUM ,roomtyp FROM NET_RESVTBL RESV ");
		sb.append(
				"LEFT JOIN (SELECT RESVTBLID FROM NET_DESKTIMES WHERE STATE = '1' ")
				.append(whereSql.toString());

		valuesList.addAll(tempList);

		sb.append(") ND on RESV.ID = ND.RESVTBLID").append(" WHERE 1 = 1  ")
				.append(" AND RESV.ISDEL = 'Y' ")
				.append(" AND RESV.FIRMID = ?")
				.append(" AND ND.RESVTBLID is NULL");

		valuesList.add(firmID);

		sb.append(" GROUP BY RESV.PAX,ROOMTYP  ");

		return jdbcTemplate.query(sb.toString(), valuesList.toArray(),
				new StoreTableRowMapper());
	}

	/**
	 * 查询可预订的包间或者大厅
	 */
	public List<StoreTable> findResvTbl(String roomTyp, String sft, String dat,
			String firmId, String pax) {
		/*
		 * List<Object> valuesList = new ArrayList<Object>();
		 * 
		 * StringBuilder sb = new StringBuilder(
		 * "SELECT RESV.PAX AS PAX,RESV.ID AS ID,ROOMTYP,TBL AS TBL,RESV.DES AS DES"
		 * ); sb.append("FROM NET_RESVTBL RESV  ")
		 * .append(" WHERE 1 = 1  AND RESV.ISDEL = 'Y' ");
		 * 
		 * if(StringUtils.hasText(firmId)){ sb.append(" AND RESV.FIRMID = ?");
		 * valuesList.add(firmId); } if(StringUtils.hasText(pax)){
		 * sb.append(" AND RESV.PAX= ?"); valuesList.add(pax); }
		 * sb.append(" AND RESV.ID NOT IN( ") .append(
		 * " SELECT ND.RESVTBLID FROM NET_DESKTIMES ND WHERE ND.STATE = '1' ");
		 * 
		 * if(StringUtils.hasText(pax)){ sb.append(" AND RESV.PAX= ?");
		 * valuesList.add(pax); }
		 * 
		 * if(StringUtils.hasText(sft)){ sb.append("  AND ND.SFT=?");
		 * valuesList.add(sft); } if(StringUtils.hasText(dat)){
		 * sb.append(" AND ND.DAT=TO_DATE(?,'yyyy-MM-dd')");
		 * valuesList.add(dat); }
		 * 
		 * sb.append(" ) ");
		 */
		List<Object> valuesList = new ArrayList<Object>();

		StringBuilder sb = new StringBuilder(
				"SELECT RESV.PAX AS PAX,RESV.ID AS ID,ROOMTYP,TBL AS TBL,RESV.DES AS DES ");
		sb.append("FROM NET_RESVTBL RESV  ");

		sb.append("LEFT JOIN (SELECT RESVTBLID FROM NET_DESKTIMES WHERE STATE = '1' ");

		if (StringUtils.hasText(sft)) {
			sb.append(" AND SFT= ?");
			valuesList.add(sft);
		}
		if (StringUtils.hasText(dat)) {
			sb.append(" AND DAT=TO_DATE(?,'yyyy-MM-dd')");
			valuesList.add(dat);
		}

		sb.append(" ) ND on RESV.ID = ND.RESVTBLID");

		sb.append(" WHERE 1 = 1  AND RESV.ISDEL = 'Y' ");

		if (StringUtils.hasText(firmId)) {
			sb.append(" AND RESV.FIRMID = ?");
			valuesList.add(firmId);
		}
		if (StringUtils.hasText(pax)) {
			sb.append(" AND RESV.PAX= ?");
			valuesList.add(pax);
		}
		sb.append(" AND ND.RESVTBLID is NULL");

		return jdbcTemplate.query(sb.toString(), valuesList.toArray(),
				new StoreTableRowMapper());
	}

	private final static String getBookDeskSql = "SELECT ID FROM NET_RESVTBL WHERE 1=1 ";

	public String getBookDesk(String id, String firmid) {
		StringBuilder sb = new StringBuilder(getBookDeskSql);
		List<Object> valuesList = new ArrayList<Object>();

		if (StringUtils.hasText(id)) {
			sb.append(" AND ID = ?");
			valuesList.add(id);
		}
		if (StringUtils.hasText(firmid)) {
			sb.append(" AND FIRMID= ?");
			valuesList.add(firmid);
		}

		sb.append(" FOR UPDATE");
		return jdbcTemplate.queryForObject(sb.toString(), valuesList.toArray(),
				String.class);
	}

	private final static String getCountDeskTimesSql = "SELECT COUNT(ID) FROM NET_DESKTIMES WHERE 1=1";

	public int getCountDeskTimes(String resvtblid, String ordersid, String sft,
			String dat) {
		List<Object> valuesList = new ArrayList<Object>();

		StringBuilder sb = new StringBuilder(getCountDeskTimesSql);
		if (StringUtils.hasText(resvtblid)) {
			sb.append(" AND RESVTBLID=?");
			valuesList.add(resvtblid);
		}
		if (StringUtils.hasText(ordersid)) {
			sb.append(" AND ORDERSID=?");
			valuesList.add(ordersid);
		}
		if (StringUtils.hasText(sft)) {
			sb.append(" AND SFT=?");
			valuesList.add(sft);
		}
		if (StringUtils.hasText(dat)) {
			sb.append(" AND DAT=to_date(?,'yyyy-MM-dd')");
			valuesList.add(dat);
		}
		return jdbcTemplate.queryForInt(sb.toString(), valuesList.toArray());
	}

	private final static String saveDeskTimesSql = "INSERT INTO NET_DESKTIMES(ID, RESVTBLID, DAT, SFT, REMARK, STATE, ORDERSID) VALUES(?,?,to_date(?,'yyyy-MM-dd'),?,?,?,?)";

	public void saveDeskTimes(DeskTimes deskTimes) {
		List<Object> valuesList = new ArrayList<Object>();
		valuesList.add(deskTimes.getId());
		valuesList.add(deskTimes.getResvtblid());
		valuesList.add(deskTimes.getDat());
		valuesList.add(deskTimes.getSft());
		valuesList.add(deskTimes.getRemark());
		valuesList.add(deskTimes.getState());
		valuesList.add(deskTimes.getOrdersid());
		jdbcTemplate.update(saveDeskTimesSql, valuesList.toArray());
	}

	/**
	 * 根据企业编码及门店编码获取菜品类型列表
	 * 
	 * @param pkGroup 企业编码
	 * @param firmId 门店编码
	 * @param tables  台位编码
	 * @param vareaOrder  是否按区域点餐
	 */
	private final static String getFdItemTypeListSql = "SELECT PK_GROUP, ID, NAME, CODE, FIRMID, TYPIND FROM FD_ITEMTYPE WHERE ENABLESTATE = 2 ";

	public List<FdItemType> getFdItemTypeList(String pkGroup, String firmId, String tables, String vareaOrder) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder(getFdItemTypeListSql);

		if (StringUtils.hasText(pkGroup)) {
			sb.append(" AND PK_GROUP = ? ");
			valuesList.add(pkGroup);
		}

		// 以后展示类别不区分门店，一个集团用一套
		/*if (StringUtils.hasText(firmId)) {
			sb.append(" AND FIRMID = ?");
			valuesList.add(firmId);
		}*/
		//通过门店id筛选菜品类别
		/*if (StringUtils.hasText(firmId)) {
			String sql_where= "select pk_itemtype from cboh_itypestore_3ch where pk_store ='"+firmId+"'";
			if(jdbcTemplate.queryForList(sql_where) != null && !jdbcTemplate.queryForList(sql_where).isEmpty())
			{
				sb.append(" AND id in ("+sql_where+")");
			}
		}*/
		sb.append(" ORDER BY TYPIND");
		
		if("Y".equals(vareaOrder) && StringUtils.hasText(tables)) {
			// 按区域点餐
			sb = new StringBuilder("");
			valuesList = new ArrayList<Object>();
			sb.append("SELECT I.PK_GROUP, I.ID, I.NAME, I.CODE, I.FIRMID, I.TYPIND ")
			.append("FROM FD_ITEMTYPE I ")
			.append("LEFT JOIN CBOH_AREAITEMTYPE_3CH A ON I.ID = A.PK_ITEMTYPE ")
			.append("LEFT JOIN CBOH_SITEDEFINE_3CH S ON S.PK_STOREAREARID = A.PK_AREA AND S.PK_STOREID = A.PK_STORE ")
			.append("WHERE I.ENABLESTATE = 2 ")
			.append("AND A.PK_STORE = ? ")
			.append("AND S.VCODE = ? ");
			
			valuesList.add(firmId);
			valuesList.add(tables);
			
			if (StringUtils.hasText(pkGroup)) {
				sb.append(" AND PK_GROUP = ? ");
				valuesList.add(pkGroup);
			}
		}

		return jdbcTemplate.query(sb.toString(), valuesList.toArray(),
				new FdItemType());
	}

	/**
	 * 根据企业编码及门店编码判断是否存在套餐
	 * 
	 * @param pkGroup
	 *            企业编码
	 * @param firmId
	 *            门店编码
	 */
	private final static String hasPackageSql = "SELECT COUNT(1) AS ID FROM ITEMPRGPACKAGE WHERE 1=1 ";

	public Integer hasPackage(String pkGroup, String firmId) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder(hasPackageSql);

		/*
		 * if(StringUtils.hasText(pkGroup)) { sb.append(" AND PK_GROUP = ?");
		 * valuesList.add(pkGroup); }
		 */

		if (StringUtils.hasText(firmId)) {
			sb.append(" AND FIRMID = ?");
			valuesList.add(firmId);
		}

		return jdbcTemplate.queryForInt(sb.toString(), valuesList.toArray());
	}

	/**
	 * 获取菜品列表
	 * 
	 * @param pkGroup
	 *            企业编码
	 * @param firmId
	 *            门店编码
	 * @param itemType
	 *            菜品类型
	 * @param name
	 *            菜品名称
	 * @param id
	 *            菜品编码
	 * @param scode
	 * @return
	 */
	public List<FdItemSale> getFdItemSaleList(String pkGroup, String firmId,
			String itemType, String name, String id, String scode, String type) {
		// 查询单位列表
		StringBuilder unitSb = new StringBuilder("");
		unitSb.append("SELECT M.CODE, M.NAME FROM BD_MEASDOC M WHERE M.ENABLESTATE = 2");
		Map<String, String> tempUnitMap = new HashMap<String, String>();
		
		List unitList = jdbcTemplate.queryForList(unitSb.toString());
		if(null != unitList && !unitList.isEmpty()) {
			for(int i = 0; i < unitList.size(); i++) {
				Map temp = (Map)unitList.get(i);
				tempUnitMap.put((String)temp.get("CODE"), (String)temp.get("NAME"));
			}
		}
		final Map<String, String> unitMap = tempUnitMap;
		
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("");
		
		sb.append("SELECT D.PK_PUBITEM AS ID, C.VCODE AS ITCODE, D.VNAME AS DES, M.NAME AS UNIT, TO_CHAR(I.NETPRICE,'FM999990.00') AS PRICE, ")
				.append("D.VINIT AS INIT, T.CODE AS GRPTYP, I.WXBIGPIC AS WXBIGPIC, I.WXSMALLPIC AS WXSMALLPIC, I.DISCRIPTION AS DISCRIPTION, ")
				.append("TO_CHAR(D.NPRICE4,'FM999990.00') AS PRICE4, I.VREQREDEFINE AS REQREDEFINE, TO_CHAR(D.NPRICE,'FM999990.00') AS PRICE1, ")
				.append("TO_CHAR(D.NPRICE2,'FM999990.00') AS PRICE2, I.VREQREDEFINE AS REQREDEFINE, TO_CHAR(D.NPRICE3,'FM999990.00') AS PRICE3, ")
				.append("TO_CHAR(D.NPRICE5,'FM999990.00') AS PRICE5, I.VREQREDEFINE AS REQREDEFINE, TO_CHAR(D.NPRICE6,'FM999990.00') AS PRICE6, ")
				.append("TO_CHAR(D.NPRICE7,'FM999990.00') AS PRICE7, I.VREQREDEFINE AS REQREDEFINE, TO_CHAR(D.NPRICE8,'FM999990.00') AS PRICE8, ")
				.append("TO_CHAR(D.NPRICE9,'FM999990.00') AS PRICE9, I.VREQREDEFINE AS REQREDEFINE, TO_CHAR(D.NPRICE10,'FM999990.00') AS PRICE10, ")
				.append("D.VUNIT AS VUNIT1, D.VUNIT2 AS VUNIT2, D.VUNIT3 AS VUNIT3, D.VUNIT4 AS VUNIT4, D.VUNIT5 AS VUNIT5, D.VUNIT6 AS VUNIT6, ")
				.append("I.VISNEW AS VISNEW, I.VISREC AS VISREC, I.PRODREQADDFLAG AS PRODREQADDFLAG, I.VSPICY AS VSPICY, P.VLEV AS VLEV, ")
				.append("I.ISORTNO AS ISORTNO ")
				.append("FROM CBOH_ITEMPRGD_3CH D, CBOH_PUBITEM_3CH I, BD_MEASDOC M, FD_ITEMTYPE T, CBOH_PUBITEMCODE_3CH C, CBOH_ITEMPRGM_3CH P ")
				.append("WHERE D.PK_PUBITEM = I.PK_PUBITEM ")
				.append("AND D.VUNIT = M.CODE ")
				.append("AND D.PK_PUBITEM = C.PK_PUBITEM ")
				.append("AND D.PK_ITEMPRGM = P.PK_ITEMPRGM ")
				.append("AND C.PK_BRAND = P.PK_BRAND ")
				.append("AND M.ENABLESTATE = 2 ")
				.append("AND I.ENABLESTATE = 2 ")
				.append("AND I.ISSHOW = 'Y' " )
				.append("AND ((D.VYVALDAT = 'Y' AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') BETWEEN D.DBEGINDATE AND D.DENDDATE) ")
				.append("OR (D.VYVALDAT = 'N' OR D.VYVALDAT IS NULL)) ");

		if (StringUtils.hasText(itemType)) {
			sb.append("AND T.ID = ? ");
			valuesList.add(itemType);
		}

		if (StringUtils.hasText(pkGroup)) {
			sb.append(" AND I.PK_GROUP = ? ");
			valuesList.add(pkGroup);
		}

		if (StringUtils.hasText(id)) {
			sb.append(" AND D.PK_PUBITEM = ? ");
			valuesList.add(id);
		}

		if (StringUtils.hasText(scode)) {
			sb.append(" AND D.VITCODE = ? ");
			valuesList.add(scode);
		}

		if (StringUtils.hasText(name)) {
			sb.append(" AND D.VNAME like ? ");
			valuesList.add("%" + name + "%");
		}
		
		if (type!=null && "takeout".equals(type)) {
			sb.append("AND I.ISTAKE = 'Y' ");
			sb.append("AND I.PK_ITEMTYPE = T.ID ");
		} else {
			sb.append("AND I.VSPTYP = T.ID ");
		}
		
		sb.append("AND ((D.PK_ITEMPRGM, C.PK_BRAND) = (SELECT PK_ITEMPRGM, PK_BRAND ")
				.append("FROM (SELECT M.PK_ITEMPRGM, M.PK_BRAND ")
				.append("FROM CBOH_ITEMPRGM_3CH M, ")
				.append("CBOH_ITEMPRGFIRM_3CH FIRM ")
				.append("WHERE M.PK_ITEMPRGM = FIRM.PK_ITEMPRGM ")
				.append("AND M.VITEMTYP = '1' ")
				.append("AND M.ENABLESTATE = 2 ")
				.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') ")
				.append("BETWEEN TO_DATE(M.DBDAT, 'YYYY-MM-DD') AND TO_DATE(M.DEDAT, 'YYYY-MM-DD') ");
		
		if (StringUtils.hasText(firmId)) {
			sb.append("AND FIRM.PK_STORE = ? ");
			valuesList.add(firmId);
		}
		sb.append("ORDER BY M.VLEV DESC) ")
				.append("WHERE ROWNUM = 1) ");
		
		sb.append("OR (D.PK_ITEMPRGM, C.PK_BRAND) = (SELECT PK_ITEMPRGM, PK_BRAND ")
				.append("FROM (SELECT M.PK_ITEMPRGM, M.PK_BRAND ")
				.append("FROM CBOH_ITEMPRGM_3CH M, ")
				.append("CBOH_ITEMPRGFIRM_3CH FIRM ")
				.append("WHERE M.PK_ITEMPRGM = FIRM.PK_ITEMPRGM ")
				.append("AND M.VITEMTYP = '2' ")
				.append("AND M.ENABLESTATE = 2 ")
				.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') ")
				.append("BETWEEN TO_DATE(M.DBDAT, 'YYYY-MM-DD') AND TO_DATE(M.DEDAT, 'YYYY-MM-DD') ");

		if (StringUtils.hasText(firmId)) {
			sb.append("AND FIRM.PK_STORE = ? ");
			valuesList.add(firmId);
		}
		sb.append("ORDER BY M.VLEV DESC) ").append("WHERE ROWNUM = 1)) ");
		
		sb.append(" ORDER BY I.ISORTNO, I.WXSMALLPIC");
		
		/*LogUtil.writeToTxt("bookMealMapper", "查询菜品明细sql:" + sb.toString());
		LogUtil.writeToTxt("bookMealMapper", "itemType:" + itemType);
		LogUtil.writeToTxt("bookMealMapper", "firmId:" + firmId);*/
		final String menuType = type;
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(),
				new RowMapper<FdItemSale>(){
			public FdItemSale mapRow(ResultSet rs, int i) throws SQLException {
				//读配置文件，确定取哪个价格
				String priceIndex = "1";
				String unitIndex = "1";
				String configPriceIndex = Commons.getConfig().getProperty("weChatPriceIndex");
				String configUnitIndex = Commons.getConfig().getProperty("weChatUnitIndex");
				if (menuType!=null && "takeout".equals(menuType)) {
					configPriceIndex = "3";
					configUnitIndex = "3";
					priceIndex = "3";
					unitIndex = "3";
				} else if(null != configPriceIndex && !"".equals(configPriceIndex)) {
					try {
						int tempPriceIndex = Integer.parseInt(configPriceIndex);
						if(tempPriceIndex >= 1 && tempPriceIndex <= 10) {
							priceIndex = Integer.toString(tempPriceIndex);
						}

						int tempUnitIndex = Integer.parseInt(configUnitIndex);
						if(tempUnitIndex >= 1 && tempUnitIndex <= 10) {
							unitIndex = Integer.toString(tempUnitIndex);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				FdItemSale f = new FdItemSale();
				f.setId(rs.getString("id"));
				f.setItcode(rs.getString("itcode"));
				f.setDes(rs.getString("des"));
				f.setInit(rs.getString("init"));
				//f.setPrice((rs.getString("NPRICE") == null || "".equals(rs.getString("NPRICE")) ? rs.getString("price") : rs.getString("NPRICE")));
				f.setPrice(rs.getString("PRICE" + priceIndex));
				f.setPrice4(rs.getString("price4"));
				//f.setUnit(rs.getString("unit"));
				f.setUnit(unitMap.get(rs.getString("VUNIT" + unitIndex)));
				f.setWxbigpic(rs.getString("wxbigpic"));
				f.setWxsmallpic(rs.getString("wxsmallpic"));
				f.setDiscription(rs.getString("discription") == null ? "" : rs.getString("discription"));
				f.setReqredefine(rs.getString("reqredefine") == null ? "" : rs.getString("reqredefine"));
				f.setGrptyp(rs.getString("grptyp") == null ? "" : rs.getString("grptyp"));
				f.setVisnew(rs.getString("visnew"));
				f.setVisrec(rs.getString("visrec"));
				f.setProdReqAddFlag(rs.getString("PRODREQADDFLAG"));
				f.setVspicy(rs.getString("VSPICY"));
				f.setVlev(rs.getString("VLEV"));
				f.setSortnum(rs.getString("ISORTNO"));
				return f;
			}
		});
	}

	/**
	 * 获取订单列表
	 * 
	 * @param params
	 * @return
	 */
	private final static String getOrderMenusSql = " SELECT ORDERS.SUMPRICE AS SUMPRICE, ORDERS.STATE AS STATE,ORDERS.ID AS ID,ORDERS.RESV AS RESV,ORDERS.ORDERTIME AS ORDERTIME,F.PK_STORE AS FIRMID,F.VNAME AS FIRMDES,ORDERS.DAT AS DAT,ORDERS.DATMINS AS DATEMINS,ORDERS.ISFEAST,"
			+ " ORDERS.PAX AS PAX,ORDERS.SFT AS SFT,ORDERS.TABLES AS TABLES,ORDERS.CONTACT AS CONTACT,ORDERS.NAM AS NAME,ORDERS.BOOKDESKORDERID AS BOOKDESKORDERID,ORDERS.OUTTRADENO AS OUTTRADENO,ORDERS.ARRTIME,"
			+ " F.VADDRESS AS ADDR,F.VTEL AS TELE,ORDERS.OPENID AS OPENID,ORDERS.RANNUM AS RANNUM,ORDERS.REMARK AS REMARK,ORDERS.SUMPRICE AS MONEY,RESVTBL.ROOMTYP AS ROOMTYPE,RESVTBL.PAX AS ROOMPAX,ORDERS.VTRANSACTIONID,ORDERS.PAYMONEY,ORDERS.VINVOICETITLE,F.VCODE AS FIRMCODE," +
			" SD.VNAME AS TABLESNAME,ORDERS.ORDFROM " +
			" FROM NET_ORDERS ORDERS LEFT JOIN CBOH_STORE_3CH F ON F.PK_STORE=ORDERS.FIRMID LEFT JOIN NET_RESVTBL RESVTBL ON F.PK_STORE=RESVTBL.FIRMID AND ORDERS.TABLES=RESVTBL.TBL " +
			" LEFT JOIN CBOH_SITEDEFINE_3CH SD ON  SD.VCODE = TO_CHAR(ORDERS.TABLES) AND SD.PK_STOREID = ORDERS.FIRMID  WHERE 1=1 AND ORDERS.DR=0 ";

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
		if (StringUtils.hasText(params.get("tables"))) {
			sbf.append(" AND ORDERS.TABLES=?");
			valuesList.add(params.get("tables"));
		}

		sbf.append(" ORDER BY ORDERS.DAT ASC");
		return jdbcTemplate.query(sbf.toString(), valuesList.toArray(),
				new NetOrderRowMapper());
	}

	/**
	 * 取消订单
	 * 
	 * @param id
	 *            订单编号
	 */
	private final static String cancelOrdersSql = "UPDATE NET_ORDERS SET STATE=? WHERE ID=?";

	public void cancelOrders(String id) {
		jdbcTemplate.update(cancelOrdersSql, new Object[] { "3", id });
	}
	
	/**
	 * 删除订单数据
	 * @param id
	 */
	public void deleteOrder(String id) {
		// 删除订单信息
		String deleteOrder = "DELETE FROM NET_ORDERS WHERE ID=?";
		jdbcTemplate.update(deleteOrder, new Object[] { id });
		
		// 删除明细信息
		String deleteDtl = "DELETE FROM NET_ORDERDETAIL WHERE ORDERSID=?";
		jdbcTemplate.update(deleteDtl, new Object[] { id });
		
		// 删除菜品附加项
		String deleteAdd = "DELETE FROM NET_DISHADDITEM WHERE PK_ORDERSID=?";
		jdbcTemplate.update(deleteAdd, new Object[] { id });
		
		// 删除菜品附加产品
		String deleteAddProduct = "DELETE FROM NET_DISHPRODADD WHERE PK_ORDERSID=?";
		jdbcTemplate.update(deleteAddProduct, new Object[] { id });
		
		// 删除账单套餐明细
		String deletePackageDtl = "DELETE FROM NET_ORDERPACKAGEDETAIL WHERE PK_ORDERID=?";
		jdbcTemplate.update(deletePackageDtl, new Object[] { id });
	}

	/**
	 * 保存订单
	 * 
	 * @param order
	 * @return
	 */
	public String saveOrder(Net_Orders order) {
		String remark = "";
		String remarkVal = "";
		String tables = "";
		String tablesVal = "";
		String pax = "";
		String paxVal = "";
		String bookDeskOrderID = "";
		String bookDeskOrderIDVal = "";
		String dat = "";
		String datVal = "";
		String nameAndTele = "";
		String nameAndTeleVal = "";

		if (null != order.getRemark() && !"".equals(order.getRemark())) {
			remark = " ,remark";
			remarkVal = ",'" + order.getRemark() + "'";
		}

		if (null != order.getTables() && !order.getTables().equals("")) {
			tables = " ,tables";
			tablesVal = ",'" + order.getTables() + "'";
		}

		if (null != order.getPax() && !order.getPax().equals("")) {
			pax = " ,pax";
			paxVal = ",'" + order.getPax() + "'";
		}
		
		if(null != order.getBookDeskOrderID() && !"".equals(order.getBookDeskOrderID())) {
			bookDeskOrderID = ",bookDeskOrderID";
			bookDeskOrderIDVal = ",'" + order.getBookDeskOrderID() + "'";
		}else{
			//20151112 订餐订单保存电话姓名 
			if(StringUtils.hasText(order.getName())){
				nameAndTele = nameAndTele + ",nam";
				nameAndTeleVal = nameAndTeleVal + ",'" + order.getName() + "'";
			}
			if(StringUtils.hasText(order.getContact())){
				nameAndTele = nameAndTele + ",contact";
				nameAndTeleVal = nameAndTeleVal + ",'" + order.getContact() + "'";
			}
		}
		
		if(null != order.getDat() && !"".equals(order.getDat())) {
			dat = ",dat";
			datVal = ",to_date('" + order.getDat() + "','yyyy-mm-dd')";
		}

		// 是否删除自动添加的菜品，如果所有菜品的类别和配置文件中的相同，则删除
		boolean delAutoItem = true;
		String delAutoPubitemType = Commons.getConfig().getProperty("delAutoPubitemType");
		for (Net_OrderDtl dtl : order.getListNetOrderDtl()) {
			if(null != dtl.getGrptyp() && !dtl.getGrptyp().equals(delAutoPubitemType)) {
				// 有不同的类别，不删除
				delAutoItem = false;
			}
		}
		
		// 自动添加一个菜品
		if(!delAutoItem && null != order.getTables() && !order.getTables().equals("")) {
			addOpenItem(order);
		}
		
		// 外送增加外送费
		if("2".equals(order.getIsfeast())) {
			addDeliverFee(order);
		}
		
		// 计算订单总金额
		double totalPrice = 0.0;
		for (Net_OrderDtl dtl : order.getListNetOrderDtl()) {
			if (null != dtl.getPrice() && !"".equals(dtl.getPrice())) {
				totalPrice += Double.parseDouble(dtl.getPrice()) * Integer.parseInt(dtl.getFoodnum());
			}
			if("1".equals(dtl.getIspackage())){
				Double packageDtlTotalmoney = 0.00;//套餐明细价格和
				if(dtl.getOrderPackageDetailList() != null && dtl.getOrderPackageDetailList().size()>0){
					for(Net_OrderPackageDetail orderPackageDetail : dtl.getOrderPackageDetailList()){
						packageDtlTotalmoney += orderPackageDetail.getNprice();//计算套餐明细的价格之和
						//套餐菜品附加项
						if(orderPackageDetail.getListDishAddItem() != null && orderPackageDetail.getListDishAddItem().size()>0){
							for(NetDishAddItem netDishAddItem : orderPackageDetail.getListDishAddItem()){
								if(null != netDishAddItem.getNprice() && !"".equals(netDishAddItem.getNprice())) {
									int cnt = Integer.parseInt(netDishAddItem.getNcount());
									totalPrice += Double.parseDouble(netDishAddItem.getNprice()) * cnt;
								}
							}
						}
						//套餐菜品附加产品
						if(orderPackageDetail.getListDishProdAdd()!= null && orderPackageDetail.getListDishProdAdd().size()>0){
							for(NetDishProdAdd netDishProdAdd : orderPackageDetail.getListDishProdAdd()){
								if(null != netDishProdAdd.getNprice() && !"".equals(netDishProdAdd.getNprice())) {
									int cnt = Integer.parseInt(netDishProdAdd.getNcount());
									totalPrice += Double.parseDouble(netDishProdAdd.getNprice()) * cnt;
								}
							}
						}
					}
					//重新计算套餐内菜品价格按比例均摊
					recalculatePackageDtlPrice(dtl,packageDtlTotalmoney);
				}
			}
			
			// 菜品数量
			int cnt = Integer.parseInt(dtl.getFoodnum());
			
			// 附加产品价格
			if(null != dtl.getListDishProdAdd() && !dtl.getListDishProdAdd().isEmpty()) {
				for(NetDishProdAdd add : dtl.getListDishProdAdd()) {
					if(null != add.getNprice() && !"".equals(add.getNprice())) {
						totalPrice += Double.parseDouble(add.getNprice()) * cnt;
					}
				}
			}
			
			// 附加项价格
			if(null != dtl.getListDishAddItem() && !dtl.getListDishAddItem().isEmpty()) {
				for(NetDishAddItem add : dtl.getListDishAddItem()) {
					if(null != add.getNprice() && !"".equals(add.getNprice())) {
						totalPrice += Double.parseDouble(add.getNprice()) * cnt;
					}
				}
			}
		}
		/*if(null != order.getListDishProdAdd() && !order.getListDishProdAdd().isEmpty()) {
			for(NetDishProdAdd add : order.getListDishProdAdd()) {
				if(null != add.getNprice() && !"".equals(add.getNprice())) {
					int cnt = Integer.parseInt(add.getNcount());
					// 根据菜品数量计算附加项数量
					for (Net_OrderDtl dtl : order.getListNetOrderDtl()) {
						if(dtl.getFoodsid().equals(add.getPk_pubitem())) {
							cnt = Integer.parseInt(dtl.getFoodnum());
						}
					}
					
					totalPrice += Double.parseDouble(add.getNprice()) * cnt;
				}
			}
		}
		if(null != order.getListDishAddItem() && !order.getListDishAddItem().isEmpty()) {
			for(NetDishAddItem add : order.getListDishAddItem()) {
				if(null != add.getNprice() && !"".equals(add.getNprice())) {
					int cnt = Integer.parseInt(add.getNcount());
					// 根据菜品数量计算附加项数量
					for (Net_OrderDtl dtl : order.getListNetOrderDtl()) {
						if(dtl.getFoodsid().equals(add.getPk_pubItem())) {
							cnt = Integer.parseInt(dtl.getFoodnum());
						}
					}
					
					totalPrice += Double.parseDouble(add.getNprice()) * cnt;
				}
			}
		}*/
		DecimalFormat df = new DecimalFormat("####0.00");
		totalPrice = Double.parseDouble(df.format(totalPrice));
		
		// 修改商户订单号生成规则
		String outTradeNo = order.getVcode() + order.getResv() + (new java.util.Random().nextInt(9000) + 1000);
		
		// 如果存在订位单单号，更新订位单总金额，商户订单号
		if(null != order.getBookDeskOrderID() && !"".equals(order.getBookDeskOrderID())) {
			String deskSql = "UPDATE NET_ORDERS SET SUMPRICE = " + totalPrice + ", OUTTRADENO = '" + outTradeNo
					+ "', BOOKDESKORDERID = '" + order.getId() + "' WHERE ID = '" + order.getBookDeskOrderID() + "'";
			jdbcTemplate.update(deskSql);
			
			// 获取桌号和人数
			String searchSql = "SELECT TABLES, PAX, TO_CHAR(DAT, 'yyyy-mm-dd') AS DAT FROM NET_ORDERS WHERE ID = '" + order.getBookDeskOrderID() + "'";
			List<Net_Orders> deskOrder = jdbcTemplate.query(searchSql, new RowMapper<Net_Orders>(){
				Net_Orders f = new Net_Orders();
				public Net_Orders mapRow(ResultSet rs, int i) throws SQLException {
					Net_Orders f = new Net_Orders();
					f.setTables(rs.getString("TABLES"));
					f.setPax(rs.getString("PAX"));
					f.setDat(rs.getString("DAT"));
					return f;
				}
			});
			
			if(null != deskOrder && !deskOrder.isEmpty()) {
				Net_Orders deskInfo = deskOrder.get(0);
				tables = " ,tables";
				tablesVal = ",'" + deskInfo.getTables() + "'";
				pax = " ,pax";
				paxVal = ",'" + deskInfo.getPax() + "'";
				dat = ",dat";
				datVal = ",to_date('" + deskInfo.getDat() + "','yyyy-mm-dd')";
			}
		}
		String payC = "";
		String payV = "";
		if("2".equals(order.getIsfeast())){
			payC+=", PAYMONEY";
			payV+="' ,'"+totalPrice;
			System.out.println("+++++++++++++修改了paymoney"+totalPrice);
		}
		String sql = "INSERT INTO net_orders " + "(ID, resv "
				+ tables
				+ pax
				+ bookDeskOrderID
				+ dat
				+ nameAndTele
				+ " , ordertime, sft, state, firmid "
				+ remark
				+ ",bev,isfeast, addr, datmins, openid, rannum, sumprice, pk_group, ordfrom, OUTTRADENO"+payC+")"
				+ " VALUES ('" + order.getId() + "','" + order.getResv() + "'"
				+ tablesVal + paxVal + bookDeskOrderIDVal + datVal + nameAndTeleVal + ",sysdate,'" + order.getSft() + "'," + "'"
				+ order.getState() + "','" + order.getFirmid() + "'"
				+ remarkVal + ",3," + order.getIsfeast() + ",'"
				+ order.getAddr() + "','" + order.getDatmins() + "','"
				+ order.getOpenid() + "','" + order.getRannum() + "',"
				+ totalPrice + ", '" + order.getPk_group() + "', 'WECHAT', '" + outTradeNo + payV+ "')";
		int cnt = jdbcTemplate.update(sql);
		//插入账单明细
		if (cnt > 0) {
			cnt = saveOrderDtl(order);
		}

		return cnt + "";
	}

	/**
	 * 增加开台菜品
	 * @param order
	 */
	public void addOpenItem(Net_Orders order) {
		StringBuilder sb = new StringBuilder();
		// 从门店个性菜谱方案查询开台菜品
		sb.append("SELECT I.PK_PUBITEM, I.VNAME, TO_CHAR(I.NPRICE,'FM999990.00') AS NPRICE, ")
		.append("I.VITCODE AS VITCODE, I.ENABLESTATE AS ENABLESTATE ")
		.append("FROM CBOH_SITEDEFINE_3CH S, CBOH_STORE_ITEMPRGD_3CH I ")
		.append("WHERE S.PK_PUBITEM = I.PK_PUBITEM ")
		.append("AND S.PK_STOREID = ? ")
		.append("AND S.VCODE = ? ")
		.append("AND I.PK_ITEMPRGM = (SELECT PK_ITEMPRGM ")
		.append("FROM (SELECT M.PK_ITEMPRGM ")
		.append("FROM CBOH_ITEMPRGM_3CH M, ")
		.append("CBOH_ITEMPRGFIRM_3CH FIRM ")
		.append("WHERE M.PK_ITEMPRGM = FIRM.PK_ITEMPRGM ")
		.append("AND M.ENABLESTATE = 2 ")
		.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') ")
		.append("BETWEEN TO_DATE(M.DBDAT, 'YYYY-MM-DD') AND TO_DATE(M.DEDAT, 'YYYY-MM-DD') ")
		.append("AND FIRM.PK_STORE = ? ")
		.append("ORDER BY M.VLEV DESC) ")
		.append("WHERE ROWNUM = 1) ");
		
		List<Net_OrderDtl> dtlInfo = jdbcTemplate.query(sb.toString(), new Object[] {order.getFirmid(), order.getTables(), order.getFirmid()},
				new RowMapper<Net_OrderDtl>(){
			public Net_OrderDtl mapRow(ResultSet rs, int i) throws SQLException {
				Net_OrderDtl f = new Net_OrderDtl();
				f.setId(CodeHelper.createUUID());
				f.setFoodsid(rs.getString("PK_PUBITEM"));
				f.setFoodsname(rs.getString("VNAME"));
				f.setPrice(rs.getString("NPRICE"));
				f.setIspackage("0");
				f.setRemark("");
				f.setPcode(rs.getString("VITCODE"));
				f.setEnablestate(rs.getString("ENABLESTATE"));
				return f;
			}
		});
		
		// 如果门店个性菜谱方案中没有，查询集团菜谱方案
		if(null == dtlInfo || dtlInfo.isEmpty()) {
			// 查询开台菜品
			sb = new StringBuilder();
			sb.append("SELECT I.PK_PUBITEM, I.VNAME, TO_CHAR(I.NPRICE,'FM999990.00') AS NPRICE, I.VITCODE AS VITCODE ")
			.append("FROM CBOH_SITEDEFINE_3CH S, CBOH_ITEMPRGD_3CH I ")
			.append("WHERE S.PK_PUBITEM = I.PK_PUBITEM ")
			.append("AND S.PK_STOREID = ? ")
			.append("AND S.VCODE = ? ")
			.append("AND I.PK_ITEMPRGM = (SELECT PK_ITEMPRGM ")
			.append("FROM (SELECT M.PK_ITEMPRGM ")
			.append("FROM CBOH_ITEMPRGM_3CH M, ")
			.append("CBOH_ITEMPRGFIRM_3CH FIRM ")
			.append("WHERE M.PK_ITEMPRGM = FIRM.PK_ITEMPRGM ")
			.append("AND M.ENABLESTATE = 2 ")
			.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') ")
			.append("BETWEEN TO_DATE(M.DBDAT, 'YYYY-MM-DD') AND TO_DATE(M.DEDAT, 'YYYY-MM-DD') ")
			.append("AND FIRM.PK_STORE = ? ")
			.append("ORDER BY M.VLEV DESC) ")
			.append("WHERE ROWNUM = 1) ");
			
			dtlInfo = jdbcTemplate.query(sb.toString(), new Object[] {order.getFirmid(), order.getTables(), order.getFirmid()},
					new RowMapper<Net_OrderDtl>(){
				public Net_OrderDtl mapRow(ResultSet rs, int i) throws SQLException {
					Net_OrderDtl f = new Net_OrderDtl();
					f.setId(CodeHelper.createUUID());
					f.setFoodsid(rs.getString("PK_PUBITEM"));
					f.setFoodsname(rs.getString("VNAME"));
					f.setPrice(rs.getString("NPRICE"));
					f.setIspackage("0");
					f.setRemark("");
					f.setPcode(rs.getString("VITCODE"));
					return f;
				}
			});
		} else {
			Net_OrderDtl dtl = dtlInfo.get(0);
			if("3".equals(dtl.getEnablestate())) {
				// 如果此菜品在门店个性菜谱方案中停用，不添加开台菜品
				dtlInfo = null;
			}
		}
		
		if(null != dtlInfo && !dtlInfo.isEmpty()) {
			Net_OrderDtl addItem = dtlInfo.get(0);
			addItem.setOrdersid(order.getId());
			addItem.setFoodnum(order.getPax());
			addItem.setTotalprice(String.valueOf(Integer.parseInt(order.getPax()) * Double.parseDouble(addItem.getPrice())));
			
			order.getListNetOrderDtl().add(addItem);
		}
	}
	
	/**
	 * 明细增加外送费
	 * @param order
	 */
	public void addDeliverFee(Net_Orders order) {
		List<StoreRange> listStoreRange = takeOutMapper.getListStoreRange(null, order.getFirmid());
		if(null != listStoreRange && !listStoreRange.isEmpty()) {
			String itemCode = Commons.getConfig().getProperty("itemCodeOfDeliver");
			StoreRange range = listStoreRange.get(0);
			// 如果外送费金额大于0，明细增加外送费
			if(range.getNdistributfee() > 0 && StringUtils.hasText(itemCode)) {
				// 根据编码查询菜品主键
				List<FdItemSale> listItem = getItemByCode(itemCode);
				if(null != listItem && !listItem.isEmpty()) {
					FdItemSale item = listItem.get(0);
					Net_OrderDtl data = new Net_OrderDtl();
					data.setId(CodeHelper.createUUID());
					data.setFoodsid(item.getId());
					data.setOrdersid(order.getId());
					data.setFoodnum("1");
					data.setPrice(Double.toString(range.getNdistributfee()));
					data.setFoodsname(item.getDes());
					data.setIspackage("0");
					data.setRemark("");
					data.setUnit(item.getUnit());
					data.setTotalprice(Double.toString(range.getNdistributfee()));
					
					// 如果明细中不包含外送费，增加一条数据
					boolean dataExist = false;
					for(Net_OrderDtl dtl : order.getListNetOrderDtl()) {
						if(dtl.getFoodsid().equals(data.getFoodsid())) {
							dataExist = true;
							break;
						}
					}
					if(!dataExist) {
						order.getListNetOrderDtl().add(data);
					}
				}
			}
		}
	}
	
	/**
	 * 根据菜品编码获取菜品
	 * @param code
	 * @return
	 */
	public List<FdItemSale> getItemByCode(String code) {
		List<Object> valuesList = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT P.PK_PUBITEM, P.VNAME, M.NAME AS UNIT ")
		.append("FROM CBOH_PUBITEM_3CH P ")
		.append("LEFT JOIN BD_MEASDOC M ON P.VUNIT = M.PK_MEASDOC ")
		.append("WHERE P.VCODE = ?");
		
		valuesList.add(code);
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<FdItemSale>(){
			public FdItemSale mapRow(ResultSet rs, int i) throws SQLException {
				FdItemSale f = new FdItemSale();
				f.setId(rs.getString("PK_PUBITEM"));
				f.setDes(rs.getString("VNAME"));
				f.setUnit(rs.getString("UNIT"));
				return f;
			}
		});
	}
	
	/**
	 * 加菜处理，更新订单总金额，插入订单明细
	 * @param order
	 * @return
	 */
	public String addMenu(Net_Orders order) {
		double totalPrice = 0.0;
		for (Net_OrderDtl dtl : order.getListNetOrderDtl()) {
			if (null != dtl.getPrice() && !"".equals(dtl.getPrice())) {
				totalPrice += Double.parseDouble(dtl.getPrice()) * Integer.parseInt(dtl.getFoodnum());
			}
			if("1".equals(dtl.getIspackage())){
				Double packageDtlTotalmoney = 0.00;//套餐明细价格和
				if(dtl.getOrderPackageDetailList() != null && dtl.getOrderPackageDetailList().size()>0){
					for(Net_OrderPackageDetail orderPackageDetail : dtl.getOrderPackageDetailList()){
						packageDtlTotalmoney += orderPackageDetail.getNprice();//计算套餐明细的价格之和
						//套餐菜品附加项
						if(orderPackageDetail.getListDishAddItem() != null && orderPackageDetail.getListDishAddItem().size()>0){
							for(NetDishAddItem netDishAddItem : orderPackageDetail.getListDishAddItem()){
								if(null != netDishAddItem.getNprice() && !"".equals(netDishAddItem.getNprice())) {
									int cnt = Integer.parseInt(netDishAddItem.getNcount());
									totalPrice += Double.parseDouble(netDishAddItem.getNprice()) * cnt;
								}
							}
						}
						//套餐菜品附加产品
						if(orderPackageDetail.getListDishProdAdd()!= null && orderPackageDetail.getListDishProdAdd().size()>0){
							for(NetDishProdAdd netDishProdAdd : orderPackageDetail.getListDishProdAdd()){
								if(null != netDishProdAdd.getNprice() && !"".equals(netDishProdAdd.getNprice())) {
									int cnt = Integer.parseInt(netDishProdAdd.getNcount());
									totalPrice += Double.parseDouble(netDishProdAdd.getNprice()) * cnt;
								}
							}
						}
					}
					//重新计算套餐内菜品价格按比例均摊
					recalculatePackageDtlPrice(dtl,packageDtlTotalmoney);
				}
			}
		}
		
		if(null != order.getListDishProdAdd() && !order.getListDishProdAdd().isEmpty()) {
			for(NetDishProdAdd add : order.getListDishProdAdd()) {
				if(null != add.getNprice() && !"".equals(add.getNprice())) {
					int cnt = Integer.parseInt(add.getNcount());
					// 根据菜品数量计算附加项数量
					for (Net_OrderDtl dtl : order.getListNetOrderDtl()) {
						if(dtl.getFoodsid().equals(add.getPk_pubitem())) {
							cnt = Integer.parseInt(dtl.getFoodnum());
						}
					}
					
					totalPrice += Double.parseDouble(add.getNprice()) * cnt;
				}
			}
		}
		if(null != order.getListDishAddItem() && !order.getListDishAddItem().isEmpty()) {
			for(NetDishAddItem add : order.getListDishAddItem()) {
				if(null != add.getNprice() && !"".equals(add.getNprice())) {
					int cnt = Integer.parseInt(add.getNcount());
					// 根据菜品数量计算附加项数量
					for (Net_OrderDtl dtl : order.getListNetOrderDtl()) {
						if(dtl.getFoodsid().equals(add.getPk_pubItem())) {
							cnt = Integer.parseInt(dtl.getFoodnum());
						}
					}
					
					totalPrice += Double.parseDouble(add.getNprice()) * cnt;
				}
			}
		}
		
		// 更新订单信息
		String updateSql = "UPDATE NET_ORDERS SET ORDERTIME=SYSDATE, SUMPRICE = SUMPRICE + ? WHERE ID = ?";
		int cnt = jdbcTemplate.update(updateSql, new Object[] {totalPrice, order.getId()});
		
		if (cnt > 0) {
			cnt = saveOrderDtl(order);
		}
		
		return cnt + "";
	}
	/**
	 * 重新计算套餐内菜品价格按比例均摊
	 * @param dtl
	 * @param packageDtlTotalmoney
	 */
	private void recalculatePackageDtlPrice(Net_OrderDtl dtl,Double packageDtlTotalmoney) {
		Double bili= packageDtlTotalmoney/Double.parseDouble(dtl.getPrice());
		Double tempDouble = 0.00,tempTotal=0.00,comparePrice=0.00;
		int maxIndex = 0;
		DecimalFormat df = new DecimalFormat("#0.00");
		df.setRoundingMode(RoundingMode.HALF_UP);//设置五入 默认为HALF_EVEN 2，4，6，8的不进位
		
		for(int i=0; i < dtl.getOrderPackageDetailList().size();i++){
			Net_OrderPackageDetail orderPackageDetail  = dtl.getOrderPackageDetailList().get(i);
			if(orderPackageDetail.getNprice()>comparePrice){
				comparePrice = orderPackageDetail.getNprice();
				maxIndex = i;
			}
			tempDouble = Double.parseDouble(df.format(orderPackageDetail.getNprice()*bili));
			tempTotal += tempDouble;
			orderPackageDetail.setNprice(tempDouble);
		}
		//计算最终的菜品明细金额与套餐金额的差值
		Double minusD = WeChatUtil.subtractNum(dtl.getPrice(),tempTotal);
		//如果差值不为0，将差值放到金额最高的菜品上
		if(minusD != 0){
			tempDouble = dtl.getOrderPackageDetailList().get(maxIndex).getNprice();
			dtl.getOrderPackageDetailList().get(maxIndex).setNprice(tempDouble+minusD);
		}
		
	}

	/**
	 * 保存订单明细
	 * @param order
	 * @param cnt
	 * @return
	 */
	public int saveOrderDtl(Net_Orders order) {
		String itemSql = "INSERT INTO NET_DISHADDITEM(PK_DISHADDITEM, PK_GROUP, PK_ORDERSID, PK_ORDERDTLID, PK_PUBITEM, "
				+ "PK_REDEFINE, PK_PRODCUTREQATTAC, NCOUNT, NPRICE, PK_BRAND) VALUES ";
		String itemproductSql = "INSERT INTO NET_DISHPRODADD(PK_DISHPRODADD, PK_GROUP, PK_ORDERSID, PK_ORDERDTLID, PK_PUBITEM, "
				+ "PK_PRODADD, PK_PRODREQADD, NCOUNT, NPRICE, PK_BRAND, UNIT) VALUES ";
		int cnt = -1;
		for (Net_OrderDtl ordersDtl : order.getListNetOrderDtl()) {
			String dtlSql = "INSERT INTO net_orderdetail "
					+ "(ID, foodsid, ordersid, remark, foodnum, totalprice, foodsname, ispackage,foodzcnt,unit) "
					+ "VALUES ('" + ordersDtl.getId() + "','"
					+ ordersDtl.getFoodsid() + "','"
					+ ordersDtl.getOrdersid() + "','"
					+ ordersDtl.getRemark() + "'," + ordersDtl.getFoodnum()
					+ "," + ordersDtl.getTotalprice() + ",'"
					+ ordersDtl.getFoodsname() + "','"
					+ ordersDtl.getIspackage() + "','" 
					+ ordersDtl.getFoodzcnt() +"','"
					+ (null == ordersDtl.getUnit() ? "" : ordersDtl.getUnit()) +"')";
			cnt = jdbcTemplate.update(dtlSql);
			//插入套餐明细
			if(ordersDtl.getOrderPackageDetailList()!=null && ordersDtl.getOrderPackageDetailList().size()>0){
				for(Net_OrderPackageDetail orderPackageDetail : ordersDtl.getOrderPackageDetailList()){
					String pk_id = CodeHelper.createUUID();
					String orderPackageDetailSql = "INSERT INTO net_orderpackagedetail "
							+ "(pk_orderpackagedetail, pk_orderid, pk_orderdetail, pk_pubitem, ncnt, nzcnt, nprice, vremark, unit) "
							+ "VALUES ('" + pk_id + "','"
							+ ordersDtl.getOrdersid() + "','"
							+ ordersDtl.getId() + "','" 
							+ orderPackageDetail.getPk_pubitem()+"',"
							+ orderPackageDetail.getNcnt() + ","
							+ orderPackageDetail.getNzcnt() + ","
							+ orderPackageDetail.getNprice() + ",'" 
							+ orderPackageDetail.getVremark() + "','" 
							+ (null == orderPackageDetail.getUnit() ? "" : orderPackageDetail.getUnit()) +"')";
					cnt = jdbcTemplate.update(orderPackageDetailSql);
					//插入菜品附加项
					if(orderPackageDetail.getListDishAddItem() != null && orderPackageDetail.getListDishAddItem().size()>0){
						for(NetDishAddItem netDishAddItemPackageDtl : orderPackageDetail.getListDishAddItem()){
							String sql = itemSql + "('"
							+ CodeHelper.createUUID() + "', '"
							+ netDishAddItemPackageDtl.getPk_group() + "', '"
							+ order.getId() + "', '"
							+ pk_id + "', '"
							+ netDishAddItemPackageDtl.getPk_pubItem() + "', '"
							+ netDishAddItemPackageDtl.getPk_redefine() + "', '"
							+ netDishAddItemPackageDtl.getPk_prodcutReqAttAc() + "', '"
							+ netDishAddItemPackageDtl.getNcount() + "', '"
							+ netDishAddItemPackageDtl.getNprice() + "', '')";
							cnt = jdbcTemplate.update(sql);
						}
					}
					//插入菜品附加产品
					if(orderPackageDetail.getListDishProdAdd() != null && orderPackageDetail.getListDishProdAdd().size()>0){
						for(NetDishProdAdd netDishProdAddPackageDtl : orderPackageDetail.getListDishProdAdd()){
							String sql = itemproductSql + "('"
									+ CodeHelper.createUUID() + "', '"
									+ netDishProdAddPackageDtl.getPk_group() + "', '"
									+ order.getId() + "', '"
									+ pk_id + "', '"
									+ netDishProdAddPackageDtl.getPk_pubitem() + "', '"
									+ netDishProdAddPackageDtl.getPk_prodAdd() + "', '"
									+ netDishProdAddPackageDtl.getPk_prodReqAdd() + "', '"
									+ netDishProdAddPackageDtl.getNcount() + "', '"
									+ netDishProdAddPackageDtl.getNprice() + "', '', '" 
									+ (null == netDishProdAddPackageDtl.getUnit() ? "" : netDishProdAddPackageDtl.getUnit()) + "')";
							cnt = jdbcTemplate.update(sql);
						}
					}
				}
			}
			
			// 保存附加项
			if(null != ordersDtl.getListDishAddItem() && !ordersDtl.getListDishAddItem().isEmpty()) {
				for (NetDishAddItem dishAddItem : ordersDtl.getListDishAddItem()) {
					String orderDtlId = "";
					// 如果附加项数据中订单明细主键不为空，此数据是从POS返回的，不改变明细主键。否则根据菜品主键去订单明细中取
					if(null != dishAddItem.getPk_orderDtlId() && !"".equals(dishAddItem.getPk_orderDtlId())) {
						orderDtlId = dishAddItem.getPk_orderDtlId();
					} else {
						orderDtlId = ordersDtl.getId();
					}
					String sql = itemSql + "('"
					+ CodeHelper.createUUID() + "', '"
					+ dishAddItem.getPk_group() + "', '"
					+ order.getId() + "', '"
					+ orderDtlId + "', '"
					+ dishAddItem.getPk_pubItem() + "', '"
					+ dishAddItem.getPk_redefine() + "', '"
					+ dishAddItem.getPk_prodcutReqAttAc() + "', '"
					+ dishAddItem.getNcount() + "', '"
					+ dishAddItem.getNprice() + "', '')";
					cnt = jdbcTemplate.update(sql);
				}
			}
			
			// 保存附加产品
			if(null != ordersDtl.getListDishProdAdd() && !ordersDtl.getListDishProdAdd().isEmpty()) {
				for (NetDishProdAdd dishProdAdd : ordersDtl.getListDishProdAdd()) {
					String orderDtlId = "";
					// 如果附加项数据中订单明细主键不为空，此数据是从POS返回的，不改变明细主键。否则根据菜品主键去订单明细中取
					if(null != dishProdAdd.getPk_orderDtlId() && !"".equals(dishProdAdd.getPk_orderDtlId())) {
						orderDtlId = dishProdAdd.getPk_orderDtlId();
					} else {
						orderDtlId = ordersDtl.getId();
					}
					String sql = itemproductSql +" ('"
							+ CodeHelper.createUUID() + "', '"
							+ dishProdAdd.getPk_group() + "', '"
							+ order.getId() + "', '"
							+ orderDtlId + "', '"
							+ dishProdAdd.getPk_pubitem() + "', '"
							+ dishProdAdd.getPk_prodAdd() + "', '"
							+ dishProdAdd.getPk_prodReqAdd() + "', '"
							+ dishProdAdd.getNcount() + "', '"
							+ dishProdAdd.getNprice() + "', '', '" 
							+ (null == dishProdAdd.getUnit() ? "" : dishProdAdd.getUnit()) + "')";
					cnt = jdbcTemplate.update(sql);
				}
			}
		}
		
		/*if(null != order.getListDishAddItem() && !order.getListDishAddItem().isEmpty()) {
			for (NetDishAddItem dishAddItem : order.getListDishAddItem()) {
				String orderDtlId = "";
				// 如果附加项数据中订单明细主键不为空，此数据是从POS返回的，不改变明细主键。否则根据菜品主键去订单明细中取
				if(null != dishAddItem.getPk_orderDtlId() && !"".equals(dishAddItem.getPk_orderDtlId())) {
					orderDtlId = dishAddItem.getPk_orderDtlId();
				} else {
					for (Net_OrderDtl ordersDtl : order.getListNetOrderDtl()) {
						// 取附加项对应的菜品明细
						if(ordersDtl.getFoodsid().equals(dishAddItem.getPk_pubItem())) {
							orderDtlId = ordersDtl.getId();
						}
					}
				}
				String sql = itemSql + "('"
				+ CodeHelper.createUUID() + "', '"
				+ dishAddItem.getPk_group() + "', '"
				+ order.getId() + "', '"
				+ orderDtlId + "', '"
				+ dishAddItem.getPk_pubItem() + "', '"
				+ dishAddItem.getPk_redefine() + "', '"
				+ dishAddItem.getPk_prodcutReqAttAc() + "', '"
				+ dishAddItem.getNcount() + "', '"
				+ dishAddItem.getNprice() + "', '')";
				cnt = jdbcTemplate.update(sql);
			}
		}
		
		if(null != order.getListDishProdAdd() && !order.getListDishProdAdd().isEmpty()) {
			for (NetDishProdAdd dishProdAdd : order.getListDishProdAdd()) {
				String orderDtlId = "";
				// 如果附加项数据中订单明细主键不为空，此数据是从POS返回的，不改变明细主键。否则根据菜品主键去订单明细中取
				if(null != dishProdAdd.getPk_orderDtlId() && !"".equals(dishProdAdd.getPk_orderDtlId())) {
					orderDtlId = dishProdAdd.getPk_orderDtlId();
				} else {
					for (Net_OrderDtl ordersDtl : order.getListNetOrderDtl()) {
						// 取附加项对应的菜品明细
						if(ordersDtl.getFoodsid().equals(dishProdAdd.getPk_pubitem())) {
							orderDtlId = ordersDtl.getId();
						}
					}
				}
				String sql = itemproductSql +" ('"
						+ CodeHelper.createUUID() + "', '"
						+ dishProdAdd.getPk_group() + "', '"
						+ order.getId() + "', '"
						+ orderDtlId + "', '"
						+ dishProdAdd.getPk_pubitem() + "', '"
						+ dishProdAdd.getPk_prodAdd() + "', '"
						+ dishProdAdd.getPk_prodReqAdd() + "', '"
						+ dishProdAdd.getNcount() + "', '"
						+ dishProdAdd.getNprice() + "', '')";
				cnt = jdbcTemplate.update(sql);
			}
		}*/
		
		return cnt;
	}
	
	/**
	 * 获取必选附加项列表
	 * @param pk_group
	 * @return
	 */
	public List<ProductReqAttAc> getProductReqAttAcList(String pk_group) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT P.PK_PRODCUTREQATTAC, P.PK_PUBITEM, P.VDES, P.MINCOUNT, P.MAXCOUNT ")
		.append("FROM CBOH_PRODCUTREQATTAC_3CH P ")
		.append("WHERE 1=1 ");

		if (StringUtils.hasText(pk_group)) {
			sb.append(" AND pk_group=? ");
			valuesList.add(pk_group);
		}
		
		sb.append("ORDER BY P.PK_PUBITEM");
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new ProductReqAttAc());
	}
	
	/**
	 * 获取附加项列表
	 * @param pk_group
	 * @return
	 */
	public List<ProductRedfine> getProductRedfineList(String pk_group) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT D.PK_PRODUCTREDFINE AS PK_PRODUCTREDFINE, D.PK_PUBITEM AS PK_PUBITEM, D.PK_REDEFINE AS PK_REDEFINE, D.PK_PRODCUTREQATTAC AS PK_PRODCUTREQATTAC, ")
		.append("D.MINCOUNT AS MINCOUNT, D.MAXCOUNT AS MAXCOUNT, R.VCODE AS VCODE, R.VNAME AS VNAME, R.NAMT AS NAMT, R.NPRICE1 AS NPRICE1 ")
		.append("FROM CBOH_PRODUCTREDFINE_3CH D, CBOH_REDEFINE_3CH R ")
		.append("WHERE D.PK_REDEFINE = R.PK_REDEFINE ")
		//.append("AND D.PK_GROUP = R.PK_GROUP ")
		.append("AND R.ENABLESTATE = 2 ");

		if (StringUtils.hasText(pk_group)) {
			sb.append(" AND D.PK_GROUP = ? ");
			valuesList.add(pk_group);
		}
		
		sb.append("ORDER BY D.PK_PUBITEM, D.PK_PRODCUTREQATTAC, R.ISORTNO");
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), 
				new RowMapper<ProductRedfine>(){
			public ProductRedfine mapRow(ResultSet rs, int i) throws SQLException {
				ProductRedfine d = new ProductRedfine();
				d.setMaxCount(rs.getString("maxCount"));
				d.setMinCount(rs.getString("minCount"));
				//d.setNamt(null == rs.getString("nprice1") || "".equals(rs.getString("nprice1")) ? rs.getString("namt") : rs.getString("nprice1"));
				d.setNamt(null != rs.getString("nprice1") && !"".equals(rs.getString("nprice1")) && Double.parseDouble(rs.getString("nprice1")) > 0 ?
						rs.getString("nprice1") : rs.getString("namt"));
				d.setPk_ProductRedfine(rs.getString("pk_ProductRedfine"));
				d.setPk_ProdcutReqAttAc(rs.getString("pk_ProdcutReqAttAc"));
				d.setPk_PubItem(rs.getString("pk_PubItem"));
				d.setPk_Redefine(rs.getString("pk_Redefine"));
				d.setVcode(rs.getString("vcode"));
				d.setVname(rs.getString("vname"));
				return d;
			}
		});
	}
	
	/**
	 * 获取可选附加项列表
	 * @param pk_group
	 * @param pk_store
	 * @return
	 */
	public List<ProductRedfine> getProductRedfineByProgList(String pk_group, String pk_store) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT R.PK_REDEFINE AS PK_REDEFINE, R.VCODE AS VCODE, R.VNAME AS VNAME, D.PRICE1 AS NAMT ")
		.append("FROM CBOH_ADDITIONALPROG_3CH H, CBOH_ADDITIONALPROGDTL_3CH D, CBOH_REDEFINE_3CH R ")
		.append("WHERE D.PK_ADDITIONALPROG = H.PK_ADDITIONALPROG ")
		.append("AND D.PK_REDEFINE = R.PK_REDEFINE ")
		.append("AND D.VISADDPROD = '0' ")
		.append("AND H.ENABLESTATE = '2' ");

		/*if (StringUtils.hasText(pk_group)) {
			sb.append(" AND D.PK_GROUP = ? ");
			valuesList.add(pk_group);
		}*/
		
		if (StringUtils.hasText(pk_store)) {
			sb.append(" AND H.PK_STORE = ? ");
			valuesList.add(pk_store);
		}
		
		sb.append("ORDER BY R.ISORTNO");
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), 
				new RowMapper<ProductRedfine>(){
			public ProductRedfine mapRow(ResultSet rs, int i) throws SQLException {
				ProductRedfine d = new ProductRedfine();
				d.setNamt(rs.getString("namt"));
				d.setPk_Redefine(rs.getString("pk_Redefine"));
				d.setVcode(rs.getString("vcode"));
				d.setVname(rs.getString("vname"));
				return d;
			}
		});
	}
	
	/**
	 * 获取菜品必选附加项列表
	 * @param pk_group
	 * @param orderId
	 * @return
	 */
	public List<NetDishAddItem> getDishAddItemList(String pk_group, String orderId) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT D.PK_DISHADDITEM, D.PK_GROUP, D.PK_ORDERSID, D.PK_ORDERDTLID, D.PK_PUBITEM, ")
		.append("D.PK_REDEFINE, D.PK_PRODCUTREQATTAC, D.NCOUNT, D.NPRICE, D.PK_BRAND, R.VNAME AS REDEFINENAME,R.VCODE AS FCODE ")
		.append("FROM NET_DISHADDITEM D, CBOH_REDEFINE_3CH R ")
		.append("WHERE D.PK_REDEFINE = R.PK_REDEFINE ")
		.append("AND R.ENABLESTATE = 2 ");

		if (StringUtils.hasText(pk_group)) {
			sb.append(" AND D.PK_GROUP = ? ");
			valuesList.add(pk_group);
		}

		if (StringUtils.hasText(orderId)) {
			sb.append(" AND D.PK_ORDERSID = ? ");
			valuesList.add(orderId);
		}
		
		sb.append("ORDER BY R.ISORTNO");
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new NetDishAddItem());
	}
	
	/**
	 * 获取菜品附加产品列表
	 * @param pk_group
	 * @param orderId
	 * @return
	 */
	public List<NetDishProdAdd> getDishProdAddList(String pk_group, String orderId) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT D.PK_DISHPRODADD, D.PK_GROUP, D.PK_ORDERSID, D.PK_ORDERDTLID, D.PK_PUBITEM, D.UNIT, P.PK_PUBITEM AS SELFPUBITEM,")
		.append("D.PK_PRODADD, D.PK_PRODREQADD, D.NCOUNT, TO_CHAR(D.NPRICE,'FM999990.00') AS NPRICE, D.PK_BRAND, P.VNAME AS PRODADDNAME, P.VCODE AS FCODE ")
		.append("FROM NET_DISHPRODADD D, CBOH_PUBITEM_3CH P, CBOH_PRODUCTADDITIONAL_3CH A ")
		.append("WHERE D.PK_PRODADD = A.PK_PRODADD ")
		.append("AND A.PK_PRODREQADD = P.PK_PUBITEM ")
		.append("AND P.ENABLESTATE = 2 ");

		if (StringUtils.hasText(pk_group)) {
			sb.append(" AND D.PK_GROUP = ? ");
			valuesList.add(pk_group);
		}

		if (StringUtils.hasText(orderId)) {
			sb.append(" AND D.PK_ORDERSID = ? ");
			valuesList.add(orderId);
		}
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(),
				new RowMapper<NetDishProdAdd>(){
			public NetDishProdAdd mapRow(ResultSet rs, int i) throws SQLException {
				NetDishProdAdd d = new NetDishProdAdd();
				d.setPk_dishProdAdd(rs.getString("pk_dishProdAdd"));
				d.setPk_group(rs.getString("pk_group"));
				d.setPk_ordersId(rs.getString("pk_ordersId"));
				d.setPk_orderDtlId(rs.getString("pk_orderDtlId"));
				d.setPk_prodAdd(rs.getString("pk_prodAdd"));
				d.setPk_prodReqAdd(rs.getString("pk_prodReqAdd"));
				d.setPk_pubitem(rs.getString("pk_pubitem"));
				d.setNcount(rs.getString("ncount"));
				d.setNprice(rs.getString("nprice"));
				d.setFcode(rs.getString("fcode"));
				d.setProdAddName(rs.getString("prodAddName"));
				d.setSelfPkPubitem(rs.getString("SELFPUBITEM"));
				return d;
			}
		});
	}
	
	/**
	 * 查询沽清菜品列表
	 * @param data
	 * @return
	 */
	public List<SellOff> getSellOffList(SellOff data) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT C.PK_SELLOFF, C.PK_GROUP, C.PK_STORE, C.PK_PUBITEM, C.NFLAG, ")
		.append("C.NREMAIN, C.DBEGINDATE, C.DENDDATE, C.DOPERATETIME FROM CBOH_SELLOFF_3CH C ")
		.append("WHERE C.NREMAIN <= 0 ")
		.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY/MM/DD'), 'YYYY/MM/DD') BETWEEN C.DBEGINDATE AND C.DENDDATE ");
		
		if(StringUtils.hasText(data.getPk_group())) {
			sb.append("AND C.PK_GROUP = ? ");
			valuesList.add(data.getPk_group());
		}
		
		if(StringUtils.hasText(data.getPk_store())) {
			sb.append("AND C.PK_STORE = ? ");
			valuesList.add(data.getPk_store());
		}
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new SellOff());
	}
	
	/**
	 * 更新订单状态
	 * @param id
	 * @param state
	 * @return
	 */
	public int updateOrderState(String id, String state) {
		String updateSql = "UPDATE NET_ORDERS SET STATE=? WHERE ID=?";
		
		return jdbcTemplate.update(updateSql, new Object[] {state, id});
	}

	
	/**
	 * 获取门店等位最大等位人数
	 * @param pk_store
	 * @return
	 */
	public int getMaxPerson(String pk_store) {
		String sql = "SELECT MAX(MAXPAX) AS MAXPAX FROM CBOH_LINENO_3CH L WHERE L.PK_STORE = ?";
		
		return jdbcTemplate.queryForInt(sql, new Object[]{pk_store});
	}
	
	/**
	 * 根据会员卡id获取电子券列表
	 * @param openId
	 * @return
	 */
	public List<Voucher> getListCouponInfo(String openId) {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT V.ID AS ID, V.CODE AS CODE, V.TYPDES AS TYPDES, V.REST AS REST, V.TYPID AS TYPID, ")
		.append("V.BDATE AS BDATE, V.EDATE AS EDATE, V.STA AS STA, A.VACTMCODE AS ACTIMCODE, V.AMT AS AMT ")
		.append("FROM VOUCHER V, COUPON A, CARD C ")
		.append("WHERE V.TYPID = A.VCODE ")
		.append("AND V.CARDID = C.CARDID ")
		.append("AND V.STA = 2 ")
		.append("AND C.WECHATID = ? ")
		.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') BETWEEN V.BDATE AND V.EDATE ");
		
		return jdbcTemplateCrm.query(sql.toString(), new Object[] {openId},
				new RowMapper<Voucher>(){
			public Voucher mapRow(ResultSet rs, int i) throws SQLException {
				Voucher voucher=new Voucher();
				voucher.setId(String.valueOf(rs.getInt("ID")));
				voucher.setCode(rs.getString("CODE"));
				voucher.setTypdes(rs.getString("TYPDES"));
				voucher.setRest(rs.getString("REST"));
				voucher.setBdate(DateFormat.getStringByDate(rs.getDate("BDATE"), "yyyy-MM-dd"));
				voucher.setEdate(DateFormat.getStringByDate(rs.getDate("EDATE"), "yyyy-MM-dd"));
				voucher.setSta(rs.getString("STA"));
				voucher.setActmCode(rs.getString("ACTIMCODE"));
				voucher.setAmt(rs.getString("AMT"));
				voucher.setTypId(rs.getString("TYPID"));
				return voucher;
			}
		});
	}
	
	/**
	 * 根据会员卡id获取电子券列表
	 * @param openId
	 * @param firmId
	 * @return
	 */
	public List<Voucher> getListCouponInfo(String openId, String firmId) {
		String dt = DateFormat.getStringByDate(new Date(), "yyyy-MM-dd,HHmm");
		String date = dt.split(",")[0];
		String time = dt.split(",")[1];
		Calendar calendar = Calendar.getInstance();//获得一个日历
	    int number = calendar.get(Calendar.DAY_OF_WEEK);//星期表示1-7，是从星期日开始，
		int week = number-1;
		if(week==0){
			week = 7;
		}
	    List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT V.ID AS ID, V.CODE AS CODE, V.TYPDES AS TYPDES, V.REST AS REST, V.TYPID AS TYPID, ")
		.append("V.BDATE AS BDATE, V.EDATE AS EDATE, V.STA AS STA, A.VACTMCODE AS ACTIMCODE, V.AMT AS AMT ")
		.append("FROM VOUCHER V, COUPON A, CARD C,CBOH_ACTM_3CH ACTM ")
		.append("WHERE V.TYPID = A.VCODE ")
		.append("AND V.CARDID = C.CARDID ")
		//--BEGIN 电子券关联活动  设置电子券使用时间段
		.append("AND A.VACTMCODE = ACTM.VCODE ")
		.append("AND ACTM.ENABLESTATE = '2' ")
		//日期限制
		.append("AND (ACTM.BISDATE IS NULL OR ( ? BETWEEN ACTM.dStartDate AND ACTM.dEndDate)) ")
		//适用门店
		.append("AND (ACTM.BISMDXZ = 'N' OR ACTM.PK_ACTM IN (select pk_actm from cboh_actstr_3ch  where pk_store = ? and (xzdate = 'N' or (")
		.append("(( mdbegintime1 is not null and mdendtime1 is not null and ? between mdbegintime1 and mdendtime1) or (mdbegintime1 is not null and mdendtime1 is null and ? >= mdbegintime1) or (mdbegintime1 is null and mdendtime1 is not null and ? <= mdendtime1)) ")
		.append(" and (( mdbegintime2 is not null and mdendtime2 is not null and ? not between mdbegintime2 and mdendtime2) or( mdbegintime2 is not null and mdendtime2 is null and ? not between mdbegintime2 and '2099-01-01') or( mdbegintime2 is null and mdendtime2 is not null and ? not between '1990-01-01' and mdendtime2) or ( mdbegintime2 is null and mdendtime2 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ")
		.append(" and (( mdbegintime3 is not null and mdendtime3 is not null and ? not between mdbegintime3 and mdendtime3) or( mdbegintime3 is not null and mdendtime3 is null and ? not between mdbegintime3 and '2099-01-01') or( mdbegintime3 is null and mdendtime3 is not null and ? not between '1990-01-01' and mdendtime3) or ( mdbegintime3 is null and mdendtime3 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ")
		.append(" and (( mdbegintime4 is not null and mdendtime4 is not null and ? not between mdbegintime4 and mdendtime4) or( mdbegintime4 is not null and mdendtime4 is null and ? not between mdbegintime4 and '2099-01-01') or( mdbegintime4 is null and mdendtime4 is not null and ? not between '1990-01-01' and mdendtime4) or ( mdbegintime4 is null and mdendtime4 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ")
		.append(" and (( mdbegintime5 is not null and mdendtime5 is not null and ? not between mdbegintime5 and mdendtime5) or( mdbegintime5 is not null and mdendtime5 is null and ? not between mdbegintime5 and '2099-01-01') or( mdbegintime5 is null and mdendtime5 is not null and ? not between '1990-01-01' and mdendtime5) or ( mdbegintime5 is null and mdendtime5 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ")
		.append(" and (( mdbegintime6 is not null and mdendtime6 is not null and ? not between mdbegintime6 and mdendtime6) or( mdbegintime6 is not null and mdendtime6 is null and ? not between mdbegintime6 and '2099-01-01') or( mdbegintime6 is null and mdendtime6 is not null and ? not between '1990-01-01' and mdendtime6) or ( mdbegintime6 is null and mdendtime6 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ")
		.append(" and (( mdbegintime7 is not null and mdendtime7 is not null and ? not between mdbegintime7 and mdendtime7) or( mdbegintime7 is not null and mdendtime7 is null and ? not between mdbegintime7 and '2099-01-01') or( mdbegintime7 is null and mdendtime7 is not null and ? not between '1990-01-01' and mdendtime7) or ( mdbegintime7 is null and mdendtime7 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ")
		.append("))")//--end select
		.append("))")//--end适用门店
		//时间限制
		.append("AND (ACTM.BISTIME = 'N' OR ACTM.PK_ACTM IN (select w.pk_actm from cboh_wptime_3ch w where  w.weekly = ? and (w.pertime = '0' or ")
		.append("( ? between w.vbegintime1 and  w.vendtime1) or ")//时间段1
		.append("( ? between w.vbegintime2 and  w.vendtime2) or ")//时间段2
		.append("( ? between w.vbegintime3 and  w.vendtime3) or ")//时间段3
		.append("( ? between w.vbegintime4 and  w.vendtime4) ")//时间段4
		.append(")")	
		.append("))")//end in
		//--END
		.append("AND V.STA = 2 ")
		.append("AND C.WECHATID = ? ")
		.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') BETWEEN V.BDATE AND V.EDATE ");
		//.append("AND (T.DSTARTDATE IS NULL OR TO_DATE(T.DSTARTDATE || '00:00:00', 'YYYY/MM/DD HH24:MI:SS') <= SYSDATE) ")
		//.append("AND (T.DENDDATE IS NULL OR TO_DATE(T.DENDDATE || '23:59:59', 'YYYY/MM/DD HH24:MI:SS') >= SYSDATE) ")
		//.append("AND (T.BISMDXZ = 'N' OR T.PK_ACTM IN (SELECT S.PK_ACTM FROM CBOH_ACTSTR_3CH S WHERE S.PK_STORE = ?)) ")
		//日期限制
		valuesList.add(date);
		
		//适用门店
		valuesList.add(firmId);
		valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date);
		valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date);
		valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date);
		valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date); valuesList.add(date);
		valuesList.add(date); valuesList.add(date); valuesList.add(date);
		//时间限制
		valuesList.add(week);
		valuesList.add(time);
		valuesList.add(time);
		valuesList.add(time);
		valuesList.add(time);
		valuesList.add(openId);
		
		return jdbcTemplateCrm.query(sql.toString(), valuesList.toArray(),
				new RowMapper<Voucher>(){
			public Voucher mapRow(ResultSet rs, int i) throws SQLException {
				Voucher voucher=new Voucher();
				voucher.setId(String.valueOf(rs.getInt("ID")));
				voucher.setCode(rs.getString("CODE"));
				voucher.setTypdes(rs.getString("TYPDES"));
				//voucher.setRest(String.valueOf(rs.getInt("REST")));
				voucher.setRest(rs.getString("REST"));
				voucher.setBdate(DateFormat.getStringByDate(rs.getDate("BDATE"), "yyyy-MM-dd"));
				voucher.setEdate(DateFormat.getStringByDate(rs.getDate("EDATE"), "yyyy-MM-dd"));
				voucher.setSta(rs.getString("STA"));
				voucher.setActmCode(rs.getString("ACTIMCODE"));
				voucher.setAmt(rs.getString("AMT"));
				voucher.setTypId(rs.getString("TYPID"));
				return voucher;
			}
		});
	}
	
	/**
	 * 查询菜品必选附加产品
	 * @param pk_group
	 * @return
	 */
	public List<ProdReqAdd> getListProdReqAdd(String pk_group) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT P.PK_PRODREQADD, P.PK_PUBITEM, P.VDES, P.MINCOUNT, P.MAXCOUNT FROM CBOH_PRODREQADD_3CH P WHERE 1=1 ");
		
		if (StringUtils.hasText(pk_group)) {
			sql.append(" AND P.PK_GROUP = ? ");
			valuesList.add(pk_group);
		}
		
		return jdbcTemplate.query(sql.toString(), valuesList.toArray(), new ProdReqAdd());
	}
	
	/**
	 * 查询菜品附加产品
	 * @param pk_group
	 * @param firmid
	 * @return
	 */
	public List<ProductAdditional> getListProductAdditional(String pk_group, String firmid) {
		// 查询单位列表
		StringBuilder unitSb = new StringBuilder("");
		unitSb.append("SELECT M.CODE, M.NAME FROM BD_MEASDOC M WHERE M.ENABLESTATE = 2");
		Map<String, String> tempUnitMap = new HashMap<String, String>();
		
		List unitList = jdbcTemplate.queryForList(unitSb.toString());
		if(null != unitList && !unitList.isEmpty()) {
			for(int i = 0; i < unitList.size(); i++) {
				Map temp = (Map)unitList.get(i);
				tempUnitMap.put((String)temp.get("CODE"), (String)temp.get("NAME"));
			}
		}
		final Map<String, String> unitMap = tempUnitMap;
		
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT P.PK_PRODADD, P.PK_PUBITEM, P.PK_ADDPUBITEM, P.PK_PRODREQADD, P.MINCOUNT, C.VCODE AS VCODE, P.MAXCOUNT, I.VNAME AS PUBITEMNAME, ")
		.append("A.VNAME AS PRODREQADDNAME, TO_CHAR(A.NETPRICE,'FM999990.00') AS NRESTPRICE, TO_CHAR(D.NPRICE,'FM999990.00') AS NETPRICE, M.VLEV AS VLEV, ")
		.append("TO_CHAR(D.NPRICE4,'FM999990.00') AS PRICE4, I.VREQREDEFINE AS REQREDEFINE, TO_CHAR(D.NPRICE,'FM999990.00') AS PRICE1, ")
		.append("TO_CHAR(D.NPRICE2,'FM999990.00') AS PRICE2, TO_CHAR(D.NPRICE3,'FM999990.00') AS PRICE3, ")
		.append("TO_CHAR(D.NPRICE5,'FM999990.00') AS PRICE5, TO_CHAR(D.NPRICE6,'FM999990.00') AS PRICE6, ")
		.append("TO_CHAR(D.NPRICE7,'FM999990.00') AS PRICE7, TO_CHAR(D.NPRICE8,'FM999990.00') AS PRICE8, ")
		.append("TO_CHAR(D.NPRICE9,'FM999990.00') AS PRICE9, TO_CHAR(D.NPRICE10,'FM999990.00') AS PRICE10, ")
		.append("D.VUNIT AS VUNIT1, D.VUNIT2 AS VUNIT2, D.VUNIT3 AS VUNIT3, D.VUNIT4 AS VUNIT4, D.VUNIT5 AS VUNIT5, D.VUNIT6 AS VUNIT6 ")
		.append("FROM CBOH_PRODUCTADDITIONAL_3CH P, CBOH_PUBITEM_3CH I, CBOH_PUBITEM_3CH A, CBOH_PUBITEMCODE_3CH C, ")
		.append("CBOH_ITEMPRGD_3CH D, CBOH_ITEMPRGM_3CH M, CBOH_STORE_3CH S ")
		.append("WHERE P.PK_PUBITEM = I.PK_PUBITEM ")
		.append("AND P.PK_PRODREQADD = A.PK_PUBITEM ")
		.append("AND P.PK_PRODREQADD = C.PK_PUBITEM ")
		.append("AND C.PK_BRAND = S.PK_BRANDID ")
		.append("AND D.PK_PUBITEM = A.PK_PUBITEM ")
		.append("AND D.PK_PUBITEM = C.PK_PUBITEM ")
		.append("AND D.PK_ITEMPRGM = M.PK_ITEMPRGM ")
		.append("AND C.PK_BRAND = M.PK_BRAND ")
		.append("AND ((D.VYVALDAT = 'Y' AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') BETWEEN D.DBEGINDATE AND D.DENDDATE) ")
		.append("OR (D.VYVALDAT = 'N' OR D.VYVALDAT IS NULL)) ");
		
		if (StringUtils.hasText(pk_group)) {
			sql.append(" AND P.PK_GROUP = ? ");
			valuesList.add(pk_group);
		}
		
		if (StringUtils.hasText(firmid)) {
			sql.append(" AND S.PK_STORE = ? ");
			valuesList.add(firmid);
		}
		
		sql.append("AND ((D.PK_ITEMPRGM, C.PK_BRAND) = (SELECT PK_ITEMPRGM, PK_BRAND ")
				.append("FROM (SELECT M.PK_ITEMPRGM, M.PK_BRAND ")
				.append("FROM CBOH_ITEMPRGM_3CH M, ")
				.append("CBOH_ITEMPRGFIRM_3CH FIRM ")
				.append("WHERE M.PK_ITEMPRGM = FIRM.PK_ITEMPRGM ")
				.append("AND M.VITEMTYP = '1' ")
				.append("AND M.ENABLESTATE = 2 ")
				.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') ")
				.append("BETWEEN TO_DATE(M.DBDAT, 'YYYY-MM-DD') AND TO_DATE(M.DEDAT, 'YYYY-MM-DD') ");
		
		if (StringUtils.hasText(firmid)) {
			sql.append("AND FIRM.PK_STORE = ? ");
			valuesList.add(firmid);
		}
		sql.append("ORDER BY M.VLEV DESC) ")
				.append("WHERE ROWNUM = 1) ");
		
		sql.append("OR (D.PK_ITEMPRGM, C.PK_BRAND) = (SELECT PK_ITEMPRGM, PK_BRAND ")
				.append("FROM (SELECT M.PK_ITEMPRGM, M.PK_BRAND ")
				.append("FROM CBOH_ITEMPRGM_3CH M, ")
				.append("CBOH_ITEMPRGFIRM_3CH FIRM ")
				.append("WHERE M.PK_ITEMPRGM = FIRM.PK_ITEMPRGM ")
				.append("AND M.VITEMTYP = '2' ")
				.append("AND M.ENABLESTATE = 2 ")
				.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') ")
				.append("BETWEEN TO_DATE(M.DBDAT, 'YYYY-MM-DD') AND TO_DATE(M.DEDAT, 'YYYY-MM-DD') ");

		if (StringUtils.hasText(firmid)) {
			sql.append("AND FIRM.PK_STORE = ? ");
			valuesList.add(firmid);
		}
		sql.append("ORDER BY M.VLEV DESC) ").append("WHERE ROWNUM = 1)) ");
		
		return jdbcTemplate.query(sql.toString(), valuesList.toArray(), 
				new RowMapper<ProductAdditional>(){
			public ProductAdditional mapRow(ResultSet rs, int i) throws SQLException {
				//读配置文件，确定取哪个价格
				String priceIndex = "1";
				String unitIndex = "1";
				String configPriceIndex = Commons.getConfig().getProperty("weChatPriceIndex");
				String configUnitIndex = Commons.getConfig().getProperty("weChatUnitIndex");
				if(null != configPriceIndex && !"".equals(configPriceIndex)) {
					try {
						int tempPriceIndex = Integer.parseInt(configPriceIndex);
						if(tempPriceIndex >= 1 && tempPriceIndex <= 10) {
							priceIndex = Integer.toString(tempPriceIndex);
						}

						int tempUnitIndex = Integer.parseInt(configUnitIndex);
						if(tempUnitIndex >= 1 && tempUnitIndex <= 10) {
							unitIndex = Integer.toString(tempUnitIndex);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				ProductAdditional d = new ProductAdditional();
				d.setPk_addPubitem(rs.getString("pk_addPubitem"));
				d.setPk_prodAdd(rs.getString("pk_prodAdd"));
				d.setPk_prodReqAdd(rs.getString("pk_prodReqAdd"));
				d.setPk_pubitem(rs.getString("pk_pubitem"));
				d.setPubitemName(rs.getString("pubitemName"));
				d.setProdReqAddName(rs.getString("prodReqAddName"));
				d.setMaxCount(rs.getString("maxCount"));
				d.setMinCount(rs.getString("minCount"));
				//d.setPrice(rs.getString("NETPRICE") != null && !"".equals(rs.getString("NETPRICE")) && Double.parseDouble(rs.getString("NETPRICE")) > 0 ?
				//		rs.getString("NETPRICE") : rs.getString("NRESTPRICE"));
				d.setPrice(rs.getString("PRICE" + priceIndex));
				//d.setPrice4(rs.getString("price4"));
				//d.setUnit(rs.getString("unit"));
				d.setUnit(unitMap.get(rs.getString("VUNIT" + unitIndex)));
				d.setVcode(rs.getString("VCODE"));
				d.setVlev(rs.getString("VLEV"));
				return d;
			}
		});
	}
	/**
	 * 查询套餐列表
	 */
	@Override
	public List<Map<String, Object>> selectPackage(String pk_group,String firmid,String type) {
		String dat = DateFormat.getStringByDate(new Date(), "yyyy-MM-dd");
		String pk_itemprgm="";
		/*if(ValueCheck.IsNotEmpty(firmid)){
			pk_itemprgm = wxPayMapper.queryItemprgmFromBOH("", "", firmid, dat);
		}*/
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("");
		
		//CBOH_PUBPACK_3CH.vpricetype 固定价格/汇总价格/高优先价格。  pa.vpricetyp,
		sql.append(" select p.pk_pubpack as id,p.vname as des,p.vcode as itcode,p.nprice as price,b.name as unit,pa.netprice as nprice,")
			.append("  pa.wxbigpic, pa.wxsmallpic, pa.discription, pa.isshow, pa.vpricetyp, pa.istake,t.code as grptyp ")
			.append(" from cboh_store_itemprgpackage_3ch p")
			.append(" left join CBOH_PUBPACK_3CH pa on p.pk_pubpack = pa.pk_pubpack")
			.append(" left join BD_MEASDOC b on pa.vsaleunit = b.pk_measdoc");
		
		if (type!=null && "takeout".equals(type)) {
			sql.append(" LEFT JOIN FD_ITEMTYPE T ON PA.PK_ITEMTYPE = T.ID ");
		} else {
			sql.append(" LEFT JOIN FD_ITEMTYPE T ON PA.VSPTYP = T.ID ");
		}
		
		sql.append(" WHERE PA.ENABLESTATE = 2  ")
			.append(" AND p.pk_store = ? " )
			.append(" AND pa.ISSHOW = 'Y' " )
			.append(" AND ((p.VYVALDAT = 'Y' AND TO_DATE('"+dat+"', 'YYYY-MM-DD') BETWEEN p.DBEGINDATE AND p.DENDDATE) ")
			.append(" OR (p.VYVALDAT = 'N' OR p.VYVALDAT IS NULL)) ");
		valuesList.add(firmid);
		if (StringUtils.hasText(pk_group)) {
			sql.append(" AND P.PK_GROUP = ? ");
			valuesList.add(pk_group);
		}
		if (type!=null && "takeout".equals(type)) {
			sql.append("AND PA.ISTAKE = 'Y' ");
		}
		sql.append(" ORDER BY P.VHANDS");
		//LogUtil.writeToTxt(LogUtil.BUSINESS, "查询套餐列表sql:" + sql.toString());
		return jdbcTemplate.query(sql.toString(), valuesList.toArray(), new ListMapMapper());
	}
	/**
	 * 查询套餐明细
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object selectPackageDtl(String pk_group, String firmid, Map<String, String> sellOffMap) {
		Map<String,Object> resMap = new HashMap<String,Object>();
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("");
		//查询单位
		sql.append("SELECT M.PK_MEASDOC,M.CODE, M.NAME FROM BD_MEASDOC M WHERE M.ENABLESTATE = 2");
		Map<String, String> tempUnitMap = new HashMap<String, String>();
		//LogUtil.writeToTxt(LogUtil.BUSINESS, "查询单位sql:" + sql.toString());
		List unitList = jdbcTemplate.queryForList(sql.toString());
		if(null != unitList && !unitList.isEmpty()) {
			for(int i = 0; i < unitList.size(); i++) {
				Map temp = (Map)unitList.get(i);
				tempUnitMap.put((String)temp.get("PK_MEASDOC"), (String)temp.get("NAME"));
			}
		}
		sql = new StringBuilder();
		//查询菜品设置中的单位设置1-6
		sql.append("SELECT PK_PUBITEM,VUNIT AS VUNIT1,UNITS AS VUNIT2,VUNIT3,VUNIT4,VUNIT5 FROM CBOH_PUBITEM_3CH ");
		Map<String, Object> tempPubitemMap = new HashMap<String, Object>();
		//LogUtil.writeToTxt(LogUtil.BUSINESS, "查询菜品设置中的单位设置sql:" + sql.toString());
		List<Map<String, Object>> pubitemList = jdbcTemplate.queryForList(sql.toString());
		if(null != unitList && !unitList.isEmpty()) {
			for(int i = 0; i < pubitemList.size(); i++) {
				Map<String, Object> temp = (Map<String, Object>)pubitemList.get(i);
				temp.put("UNITNAME1", tempUnitMap.get(temp.get("VUNIT1")));
				temp.put("UNITNAME2", tempUnitMap.get(temp.get("VUNIT2")));
				temp.put("UNITNAME3", tempUnitMap.get(temp.get("VUNIT3")));
				temp.put("UNITNAME4", tempUnitMap.get(temp.get("VUNIT4")));
				temp.put("UNITNAME5", tempUnitMap.get(temp.get("VUNIT5")));
				temp.put("UNITNAME6", tempUnitMap.get(temp.get("VUNIT6")));
				tempPubitemMap.put((String)temp.get("PK_PUBITEM"), temp);
			}
		}
		
		sql = new StringBuilder();
		String dat = DateFormat.getStringByDate(new Date(), "yyyy-MM-dd");
		String pk_itemprgm="";
		if(ValueCheck.IsNotEmpty(firmid)){
			pk_itemprgm = wxPayMapper.queryItemprgmFromBOH("", "", firmid, dat);
		}
		sql.append(" select distinct nvl(ppc.vcode,pp.vcode) as tpcode,pd.pk_package,pd.pk_pubitem,pd.pk_package as pk_parentid, ")
		.append("         nvl(pic.vcode,pi.vcode) as pcode, ")
		.append("         pi.vname as psname,pd.nprice,")
		.append("          nvl(pd.npackageprice,0) as price1, ")
		.append("         nvl(pd.npackageprice,0) as price2, ")
		.append("         nvl(pd.npackageprice,0) as price3, ")
		.append("         measdoc.name as unit,pd.icnt as cnt,0 as defualts, ")
		.append("         pd.producttc_order,pd.isortno,pd.pk_pubpack,")
		.append("         pd.ndiscountprice,pd.npackageprice,pd.nproportion,")
		.append("         nvl(pd.imincount,0) as imincount,")
		.append("         nvl(pd.imaxcount,0) as imaxcount,")
		.append("         nvl(imaxgcnt,0) as imaxgcnt,")
		.append("         nvl(pd.vshowinfo,'') as vshowinfo,")
		.append("         pi.vpicbig,pi.vpicsmall,pi.wxsmallpic,pi.wxbigpic,pi.vreqredefine as reqredefine,pi.prodReqAddFlag,pd.bchange,pd.unitindex,")
		.append("         0.00 as nadjustprice")
		.append(" from cboh_packgetdl_3ch pd  ")
		.append(" left join cboh_pubitem_3ch pi on pi.pk_pubitem = pd.pk_pubitem ")
		.append(" left join cboh_pubpack_3ch pp on pd.pk_pubpack = pp.pk_pubpack ")
		.append(" left join cboh_store_itemprgpackage_3ch ip on ip.pk_pubpack = pp.pk_pubpack ")
		.append(" LEFT JOIN CBOH_PUBITEMCODE_3CH PIC ON pi.PK_PUBITEM = PIC.PK_PUBITEM ")
		.append(" LEFT JOIN CBOH_PUBPACKCODE_3CH PPC ON pp.pk_pubpack = PPC.pk_pubpack ")
		.append(" left join bd_measdoc measdoc on measdoc.pk_measdoc = pi.vunit")
		.append(" where pp.vcode is not null and pi.enablestate = '2' ")
		.append(" 	   and ip.pk_store = ? ")
		.append(" ORDER BY tpcode,pd.PRODUCTTC_ORDER");
		String sqlStr = sql.toString();
		if("oracle".equals(Commons.databaseType)){
			sqlStr = sql.toString();
		}else if("sqlserver".equals(Commons.databaseType)){
			sqlStr = sqlStr.replace("nvl", "isnull");
		}else if("mysql".equals(Commons.databaseType)){
			sqlStr = sqlStr.replace("nvl", "ifnull");
		}else{
			sqlStr = sql.toString();
		}
		if (StringUtils.hasText(firmid)) {
			valuesList.add(firmid);
		}
		//查询套餐明细
		//LogUtil.writeToTxt(LogUtil.BUSINESS, "查询套餐明细sql:" + sqlStr);
		List<Map<String,Object>> listPackageDtls = jdbcTemplate.query(sqlStr, valuesList.toArray(), new ListMapMapper());
		listPackageDtls = WeChatUtil.formatNumForListResult(listPackageDtls,"nprice,price1,price2,price3",2);
		listPackageDtls = WeChatUtil.formatNumForListResult(listPackageDtls,"imincount,imaxcount,imaxgcnt",0);

		//一次性查询全部套餐的菜品可换菜
		List<Map<String,Object>> listPackageKhc = new ArrayList<Map<String,Object>>();
		sql = new StringBuilder("");
		sql.append(" select nvl(ppc.vcode,pp.vcode) as tpcode,itp.pk_itempkg as pk_package,itp.pk_pubitem,itp.pk_package as pk_parentid,")
		.append("        nvl(pic.vcode,pi.vcode) as pcode,")
		.append("        '' as producttc_order,itp.nprice,")
		.append("        pi.vname as psname,")
		.append("        nvl(itp.npackageprice, 0) as price1,")
		.append("        nvl(itp.npackageprice, 0) as price2,")
		.append("        nvl(itp.npackageprice, 0) as price3,")
		.append("        '' as mdept,")
		.append("        itp.isortno as isortno,")
		.append("        measdoc.name as unit,")
		.append("        itp.ncnt as cnt,")
		.append("        '' as defualts,")
		.append("        0 as defaultselected,")
		.append("        itp.ndiscountprice,")
		.append("        itp.npackageprice,")
		.append("        nvl(itp.imincount,0) as imincount,")
		.append("        nvl(itp.imaxcount,0) as imaxcount,")
		.append("        itp.unitcode,pi.vpicbig,pi.vpicsmall,pi.wxsmallpic,pi.wxbigpic,pi.vreqredefine as reqredefine,pi.prodReqAddFlag,'' as bchange,itp.unitindex,")
		.append("        nvl(itp.nadjustprice,0) as nadjustprice,itp.pk_pubpack ")
		.append(" FROM cboh_itempkg_3ch itp")
		.append(" left join cboh_pubitem_3ch pi on pi.pk_pubitem = itp.pk_pubitem")
		.append(" left join cboh_pubpack_3ch pp on itp.pk_pubpack = pp.pk_pubpack")
		.append(" left join (SELECT PK_PUBITEM,VCODE FROM CBOH_PUBITEMCODE_3CH ")
		.append(" 	  WHERE PK_BRAND = (SELECT PK_BRANDID FROM CBOH_STORE_3CH WHERE PK_STORE=?)) pic")
		.append(" on pic.pk_pubitem = itp.pk_pubitem")
		.append(" left join (SELECT PK_PUBPACK,VCODE FROM CBOH_PUBPACKCODE_3CH ")
		.append(" 	  WHERE PK_BRAND = (SELECT PK_BRANDID FROM CBOH_STORE_3CH WHERE PK_STORE=?)) PPC")
		.append(" ON ppc.pk_pubpack=pp.pk_pubpack")
		.append(" left join bd_measdoc measdoc on measdoc.pk_measdoc = pi.vunit")
		.append(" WHERE pp.vcode is not null and pi.enablestate = '2' ")
		.append(" order by itp.pk_pubpack,itp.pk_package,itp.isortno");
		sqlStr = sql.toString();
		if("oracle".equals(Commons.databaseType)){
			sqlStr = sql.toString();
		}else if("sqlserver".equals(Commons.databaseType)){
			sqlStr = sqlStr.replace("nvl", "isnull");
		}else if("mysql".equals(Commons.databaseType)){
			sqlStr = sqlStr.replace("nvl", "ifnull");
		}else{
			sqlStr = sql.toString();
		}
		valuesList = new ArrayList<Object>();
//		valuesList.add(map.get("producttc_order"));
		valuesList.add(firmid);
		valuesList.add(firmid);
//		valuesList.add(map.get("pk_package"));
//		valuesList.add(map.get("pk_pubpack"));
//		valuesList.add(map.get("pk_pubitem"));
		//LogUtil.writeToTxt(LogUtil.BUSINESS, "查询套餐菜品可换菜sql:" + sqlStr);
		listPackageKhc = jdbcTemplate.query(sqlStr.toString(), valuesList.toArray(), new ListMapMapper());
		listPackageKhc = WeChatUtil.formatNumForListResult(listPackageKhc,"nprice,price1,price2,price3",2);
		listPackageKhc = WeChatUtil.formatNumForListResult(listPackageKhc,"imincount,imaxcount,imaxgcnt",0);
		
		Map<String,List<Map<String,Object>>> khcFinalMap = new HashMap<String, List<Map<String,Object>>>();
		List<Map<String,Object>> listPackageKhcTemp = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> khcTempMap : listPackageKhc){
			//如果为空
			if(!ValueCheck.IsNotEmptyForList(khcFinalMap.get(khcTempMap.get("pk_pubpack")+"_"+khcTempMap.get("pk_parentid")))){
				listPackageKhcTemp = new ArrayList<Map<String,Object>>();
				listPackageKhcTemp.add(khcTempMap);
				khcFinalMap.put(khcTempMap.get("pk_pubpack")+"_"+khcTempMap.get("pk_parentid"),listPackageKhcTemp);
			}else{
				listPackageKhcTemp = khcFinalMap.get(khcTempMap.get("pk_pubpack")+"_"+khcTempMap.get("pk_parentid"));
				listPackageKhcTemp.add(khcTempMap);
				khcFinalMap.put(khcTempMap.get("pk_pubpack")+"_"+khcTempMap.get("pk_parentid"),listPackageKhcTemp);
			}
		}
		
		if(ValueCheck.IsNotEmptyForList(listPackageDtls)){
			//分组菜品容器
			List<Map<String,Object>> listPackageGroupInfo = new ArrayList<Map<String,Object>>();
			//各分组中菜品列表
			List<Map<String,Object>> listPackageGroupPub = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> map : listPackageDtls){

				map.put("hasSellOff", sellOffMap.get(map.get("pcode")));//设置沽清状态
				
				//处理套餐明细的单位
				String unitindex = map.get("unitindex")+"";
				unitindex = ValueCheck.IsEmpty(unitindex)==true?"1":unitindex;
				Map<String,Object> tempMap = (Map<String, Object>) tempPubitemMap.get(map.get("pk_pubitem")+"");
				if(ValueCheck.IsNotEmpty(tempMap.get("UNITNAME"+unitindex))){
					map.put("unit",tempMap.get("UNITNAME"+unitindex));
				}
				listPackageKhc = khcFinalMap.get(map.get("pk_pubpack")+"_"+map.get("pk_package"));
				if(ValueCheck.IsNotEmptyForList(listPackageKhc)){//如果可换菜列表不为空
					int indexDefault = 0;
					for(Map<String, Object> khcMap:listPackageKhc){
						khcMap.put("defualts", indexDefault++);
						khcMap.put("producttc_order", map.get("producttc_order"));
						//处理套餐明细可换菜的单位
						unitindex = khcMap.get("unitindex")+"";
						unitindex = ValueCheck.IsEmpty(unitindex)==true?"1":unitindex;
						tempMap = (Map<String, Object>) tempPubitemMap.get(khcMap.get("pk_pubitem")+"");
						if(ValueCheck.IsNotEmpty(tempMap.get("UNITNAME"+unitindex))){
							khcMap.put("unit",tempMap.get("UNITNAME"+unitindex));
						}
					}
				}
				if(ValueCheck.IsEmpty(resMap.get(map.get("pk_pubpack")))){
					listPackageGroupInfo = new ArrayList<Map<String,Object>>();
					listPackageGroupPub = new ArrayList<Map<String,Object>>();
					Map<String,Object> mapPub = new HashMap<String,Object>();
					listPackageGroupPub.add(map);//第一个作为分组的基础信息
					if(ValueCheck.IsNotEmptyForList(listPackageKhc)){//如果可换菜列表不为空
						for(Map<String, Object> mapKhc : listPackageKhc){
							mapKhc.put("hasSellOff", sellOffMap.get(mapKhc.get("pcode")));//设置沽清状态
						}
						listPackageGroupPub.addAll(listPackageKhc);
					}else{
//						listPackageGroupPub.add(map);//第二个为分组中的第一道菜
						Map<String, Object>  mapTemp = new HashMap<String, Object>(map);
						mapTemp.put("pk_package", CodeHelper.createUUID());//重置菜品主键防止与分组信息主键冲突
						listPackageGroupPub.add(mapTemp);//第二个为分组中的第一道菜
					}
					mapPub.put(map.get("pk_package")+"",listPackageGroupPub);
					listPackageGroupInfo.add(mapPub);
					resMap.put(map.get("pk_pubpack")+"", JSONArray.fromObject(listPackageGroupInfo));
				}else{
					listPackageGroupInfo =(List<Map<String, Object>>) resMap.get(map.get("pk_pubpack")+"");
					List<Map<String, Object>> listTemp = new ArrayList<Map<String, Object>>();
					Boolean  isnull = true;
					//如果当前分组没有在里面
//					if(isnull){
						Map<String,Object> mapPub = new HashMap<String,Object>();
						listPackageGroupPub = new ArrayList<Map<String,Object>>();
						listPackageGroupPub.add(map);//第一个作为分组的基础信息
						if(ValueCheck.IsNotEmptyForList(listPackageKhc)){//如果可换菜列表不为空
							for(Map<String, Object> mapKhc : listPackageKhc){
								mapKhc.put("hasSellOff", sellOffMap.get(mapKhc.get("pcode")));//设置沽清状态
							}
							listPackageGroupPub.addAll(listPackageKhc);
						}else{
//							listPackageGroupPub.add(map);//第二个为分组中的第一道菜
							Map<String, Object>  mapTemp = new HashMap<String, Object>(map);
							mapTemp.put("pk_package", CodeHelper.createUUID());//重置菜品主键防止与分组信息主键冲突
							listPackageGroupPub.add(mapTemp);//第二个为分组中的第一道菜
						}
						mapPub.put(map.get("pk_package")+"", listPackageGroupPub);
						listTemp.add(mapPub);
//					}
					listPackageGroupInfo.addAll(listTemp);
//					listPackageGroupInfo.addAll(listPackageKhc);//将搜索出来的可换菜加入到组中
					resMap.put(map.get("pk_pubpack")+"", JSONArray.fromObject(listPackageGroupInfo));
				}
			}
		}
		return resMap;
	}
	/**
	 * 查询订单中套餐明细
	 */
	@Override
	public List<Net_OrderPackageDetail> getOrderPackageDtl(Net_OrderDtl dtl) {
		try{
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT pk_orderpackagedetail,pk_orderid,pk_orderdetail,pk_pubitem,ncnt,nzcnt,nprice,vremark FROM NET_ORDERPACKAGEDETAIL P WHERE 1=1 ");
		
		if (StringUtils.hasText(dtl.getOrdersid())) {
			sql.append(" AND P.PK_ORDERID = ? ");
			valuesList.add(dtl.getOrdersid());
		}
		if (StringUtils.hasText(dtl.getId())) {
			sql.append(" AND P.PK_ORDERDETAIL = ? ");
			valuesList.add(dtl.getId());
		}
		
		return jdbcTemplate.query(sql.toString(), valuesList.toArray(), new Net_OrderPackageDetail());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取未完成点菜单数量
	 * @param order
	 * @return
	 */
	@Override
	public int getOrderCount(Net_Orders order) {
		StringBuilder sb = new StringBuilder("");
		List<Object> valuesList = new ArrayList<Object>();
		
		sb.append("SELECT COUNT(1) FROM NET_ORDERS WHERE 1 = 1");
		
		if(StringUtils.hasText(order.getFirmid())) {
			sb.append(" AND FIRMID = ? ");
			valuesList.add(order.getFirmid());
		}
		
		if(StringUtils.hasText(order.getDat())) {
			sb.append(" AND DAT = TO_DATE(?, 'YYYY-MM-DD') ");
			valuesList.add(order.getDat());
		}
		
		if(StringUtils.hasText(order.getOpenid())){
			sb.append(" AND OPENID= ? ");
			valuesList.add(order.getOpenid());
		}
		
		if(StringUtils.hasText(order.getIsfeast())){
			sb.append(" AND ISFEAST= ? ");
			valuesList.add(order.getIsfeast());
		}
		
		if(StringUtils.hasText(order.getState())){
			sb.append(" AND STATE <= ? ");
			valuesList.add(order.getState());
		}
		
		return jdbcTemplate.queryForInt(sb.toString(), valuesList.toArray());
	}
	
	/**
	 * 根据商户订单号查询订单
	 * @param outTradeNo
	 * @return
	 */
	public List<Net_Orders> getListNetOrdersByOutTradeNo(String outTradeNo) {
		StringBuilder sbf = new StringBuilder(getOrderMenusSql);
		List<Object> valuesList = new ArrayList<Object>();

		if (StringUtils.hasText(outTradeNo)) {
			sbf.append(" AND ORDERS.OUTTRADENO=?");
			valuesList.add(outTradeNo);
		}

		sbf.append(" ORDER BY ORDERS.DAT ASC");
		return jdbcTemplate.query(sbf.toString(), valuesList.toArray(),
				new NetOrderRowMapper());
	}
	
	private final static String getGroupActmSql = "SELECT ACTM.PK_ACTM,ACTM.VCODE,ACTM.VNAME,ACTM.COUPONCODE,COUPON.VNAME AS COUPONNAME,ACTM.NDERATENUM FROM CBOH_ACTM_3CH ACTM,CBOH_ACTSTR_3CH ACTSTR,CBOH_COUPON_3CH COUPON WHERE ACTM.PK_ACTM=ACTSTR.PK_ACTM AND ACTM.COUPONCODE=COUPON.VCODE AND ACTM.ENABLESTATE=2 AND ISVALIDATE='Y' AND BREMIT='Y' AND OPERATEGROUP='Y' ";
	public List<GroupActm> getGroupActm(String firmid, String date) {
		StringBuilder sb = new StringBuilder(getGroupActmSql);
		List<String> valuesList = new ArrayList<String>();
		
		if(StringUtils.hasText(firmid)){
			sb.append("AND ACTSTR.PK_STORE=? ");
			valuesList.add(firmid);
		}else{
			return null;
		}
		
		if(StringUtils.hasText(date)){
			sb.append("AND (CASE WHEN ACTSTR.MDBEGINTIME1 IS NULL THEN '1970-01-01' ELSE ACTSTR.MDBEGINTIME1 END)<=? ");
			valuesList.add(date);
			
			sb.append("AND (CASE WHEN ACTSTR.MDENDTIME1 IS NULL THEN '2199-12-30' ELSE ACTSTR.MDENDTIME1 END)>=? ");
			valuesList.add(date);
		}
		
		sb.append(" ORDER BY ACTM.COUPONCODE,ACTM.NDERATENUM DESC");
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<GroupActm>(){
			DecimalFormat df = new DecimalFormat("####0.00");
			public GroupActm mapRow(ResultSet rs, int i)
					throws SQLException {
				GroupActm ga = new GroupActm();
				ga.setCouponcode(rs.getString("COUPONCODE"));
				ga.setVname(rs.getString("VNAME"));
				ga.setPk_actm(rs.getString("PK_ACTM"));
				ga.setCouponname(rs.getString("COUPONNAME"));
				ga.setVcode(rs.getString("VCODE"));
				ga.setnDerateNum(df.format(rs.getDouble("NDERATENUM")).replace(".", "_"));//id不支持含有点号
				return ga;
			}
			
		});
	}
	
	/**
	 * 获取门店个性菜品列表
	 * @param pkGroup
	 * @param firmId
	 * @param isTake
	 * @return
	 */
	public List<FdItemSale> getStoreFdItemSaleList(String pkGroup, String firmId, String isTake) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("");
		
		sb.append("SELECT S.PK_PUBITEM AS ID, C.VCODE AS ITCODE, S.VNAME AS DES, M.NAME AS UNIT, TO_CHAR(I.NETPRICE,'FM999990.00') AS PRICE, ")
				.append("S.VINIT AS INIT, T.CODE AS GRPTYP, I.WXBIGPIC AS WXBIGPIC, I.WXSMALLPIC AS WXSMALLPIC, I.DISCRIPTION AS DISCRIPTION, ")
				.append("TO_CHAR(S.NPRICE4,'FM999990.00') AS PRICE4, I.VREQREDEFINE AS REQREDEFINE, TO_CHAR(S.NPRICE,'FM999990.00') AS NPRICE, ")
				.append("I.VISNEW AS VISNEW, I.VISREC AS VISREC, I.PRODREQADDFLAG AS PRODREQADDFLAG, I.VSPICY AS VSPICY, ")
				.append("S.ENABLESTATE AS ENABLESTATE, I.ISORTNO AS ISORTNO, S.NPRICE3 AS NPRICE3 ")
				.append("FROM CBOH_STORE_ITEMPRGD_3CH S, CBOH_PUBITEM_3CH I, BD_MEASDOC M, FD_ITEMTYPE T, CBOH_PUBITEMCODE_3CH C, CBOH_STORE_3CH ST ")
				.append("WHERE S.PK_PUBITEM = I.PK_PUBITEM ")
				.append("AND S.VUNIT = M.CODE ")
				.append("AND S.PK_PUBITEM = C.PK_PUBITEM ")
				.append("AND S.PK_STORE = ST.PK_STORE ")
				.append("AND C.PK_BRAND = ST.PK_BRANDID ")
				.append("AND M.ENABLESTATE = 2 ")
				.append("AND I.ENABLESTATE = 2 ")
				.append("AND I.ISSHOW = 'Y' " );
		
		if (StringUtils.hasText(isTake) && "takeout".equals(isTake)) {
			sb.append("AND I.ISTAKE = 'Y' ");
			sb.append("AND I.PK_ITEMTYPE = T.ID ");
		} else {
			sb.append("AND I.VSPTYP = T.ID ");
		}
		
		if (StringUtils.hasText(pkGroup)) {
			sb.append(" AND I.PK_GROUP = ? ");
			valuesList.add(pkGroup);
		}
		
		if (StringUtils.hasText(firmId)) {
			sb.append("AND S.PK_STORE = ? ");
			valuesList.add(firmId);
		}
		//LogUtil.writeToTxt(LogUtil.BUSINESS, "查询菜品sql：" + sb.toString());
		final String type = isTake;
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(),
				new RowMapper<FdItemSale>(){
			public FdItemSale mapRow(ResultSet rs, int i) throws SQLException {
				DecimalFormat df = new DecimalFormat("#0.00");
				FdItemSale f = new FdItemSale();
				f.setId(rs.getString("id"));
				f.setItcode(rs.getString("itcode"));
				f.setDes(rs.getString("des"));
				f.setInit(rs.getString("init"));
				f.setPrice((rs.getString("NPRICE") == null || "".equals(rs.getString("NPRICE")) ? rs.getString("price") : rs.getString("NPRICE")));
				f.setPrice4(rs.getString("price4"));
				f.setUnit(rs.getString("unit"));
				f.setWxbigpic(rs.getString("wxbigpic"));
				f.setWxsmallpic(rs.getString("wxsmallpic"));
				f.setDiscription(rs.getString("discription") == null ? "" : rs.getString("discription"));
				f.setReqredefine(rs.getString("reqredefine") == null ? "" : rs.getString("reqredefine"));
				f.setGrptyp(rs.getString("grptyp") == null ? "" : rs.getString("grptyp"));
				f.setVisnew(rs.getString("visnew"));
				f.setVisrec(rs.getString("visrec"));
				f.setProdReqAddFlag(rs.getString("PRODREQADDFLAG"));
				f.setVspicy(rs.getString("VSPICY"));
				f.setEnablestate(rs.getString("ENABLESTATE"));
				f.setSortnum(rs.getString("ISORTNO"));
				if (StringUtils.hasText(type) && "takeout".equals(type)) {
					try {
						f.setPrice(df.format(Double.parseDouble(rs.getString("NPRICE3"))));
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				return f;
			}
		});
	}
}
