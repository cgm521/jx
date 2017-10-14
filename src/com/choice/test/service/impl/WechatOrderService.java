package com.choice.test.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import com.choice.test.domain.Arear;
import com.choice.test.domain.City;
import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.FavorArea;
import com.choice.test.domain.Firm;
import com.choice.test.domain.ItemPrgPackage;
import com.choice.test.domain.ItemPrgpackAgedtl;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_Orders;
import com.choice.test.domain.Project;
import com.choice.test.domain.ProjectType;
import com.choice.test.domain.Sft;
import com.choice.test.domain.StoreTable;
import com.choice.test.domain.WebMsg;
import com.choice.test.persistence.WeChatOrderMapper;
import com.choice.test.service.CardQrCode;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.JdbcConnection;
import com.choice.wechat.domain.ListMapMapper;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;

/**
 * 
 * @author 王吉峰
 * @param 2014-3-9 下午2:41:26
 */
public class WechatOrderService implements WeChatOrderMapper {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 查询城市
	 * @param des
	 * @return
	 * @throws Exception
	 */
	public List<City> getCity(String des){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<City> listCity=new ArrayList<City>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getTELEConnection();
			st = conn.createStatement();
			if((null!=des && !des.equals(""))){
				sbf.append(" AND VNAME='"+des+"'");
			}
//			String sql = "SELECT CODID,CODTYP,DES FROM CODDES WHERE CODTYP=1"+sbf+"  ORDER BY SORT ASC";
			String sql = "SELECT PK_CITY,VCODE,VNAME,VINIT FROM CBOH_CITY_3CH WHERE ENABLESTATE=2 "+sbf+" ORDER BY VCODE ASC";
			rs = st.executeQuery(sql);
			while(rs.next()){
				City city=new City();
				city.setSno(rs.getString("PK_CITY"));
				city.setDes(rs.getString("VNAME"));
				listCity.add(city);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listCity;
	}
	
	/**
	 * 门店
	 */
	public List<Firm> getTeleStore(String code, String name,String area){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Firm> listFirm=new ArrayList<Firm>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=code && !code.equals(""))){
				sbf.append(" AND T.PK_STORE IN (");
				List list=Arrays.asList(code.split(","));
				for(int i=0;i<list.size();i++){
					if(i==0){
						sbf.append("'"+list.get(i)+"'");
					}else{
						sbf.append(",'"+list.get(i)+"'");
					}
				}
				sbf.append(")");
			}
			if(null!=name && !name.equals("")){
				sbf.append(" AND FIRMDES='"+name+"'");
			}
			if(null!=area && !area.equals("")){
				sbf.append(" AND pk_city='"+area+"'");
			}
			String sql = "SELECT T.PK_STORE AS FIRMID, T.VCODE AS VCODE, T.VNAME AS FIRMDES, T.VINIT AS INIT, "
					+ "T.PK_AREA AS AREA, T.VADDRESS AS ADDR, T.VTEL AS TELE, T.BIGPIC AS WBIGPIC FROM CBOH_STORE_3CH T "
					+ "WHERE 1 = 1 "
					+ sbf
					+ "ORDER BY T.VINIT ASC";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Firm firm=new Firm();
				firm.setFirmid(rs.getString("FIRMID"));
				firm.setVcode(rs.getString("VCODE"));
				firm.setFirmdes(rs.getString("FIRMDES"));
				firm.setInit(rs.getString("INIT"));
				firm.setArea(rs.getString("AREA"));
				firm.setAddr(rs.getString("ADDR"));
				firm.setTele(rs.getString("TELE"));
				firm.setWbigpic(rs.getString("WBIGPIC"));
				listFirm.add(firm);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listFirm;
	}
	
	/**
	 * 菜品类别
	 */
	public List<ProjectType> getTeleProjectType(String code, String name,String firmId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<ProjectType> listProjectType=new ArrayList<ProjectType>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=code && !code.equals(""))){
				sbf.append(" AND ID="+code);
			}
			if(null!=name && !name.equals("")){
				sbf.append(" AND DES='"+name+"'");
			}
			if(null!=firmId && !firmId.equals("")){
				sbf.append(" AND FIRMID='"+firmId+"'");
			}
			String sql = "SELECT ID,NAME,CODE,FIRMID,TYPIND FROM FD_ITEMTYPE WHERE 1=1"+sbf;
			rs = st.executeQuery(sql);
			while(rs.next()){
				ProjectType projectType=new ProjectType();
				projectType.setId(rs.getString("ID"));
				projectType.setDes(rs.getString("NAME"));
				projectType.setCode(rs.getString("CODE"));
				projectType.setFirmid(rs.getString("FIRMID"));
				projectType.setTypind(rs.getString("TYPIND"));
				listProjectType.add(projectType);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listProjectType;
	}
	
	/**
	 * 查询我的订单
	 * @param state
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<Net_Orders> getOrderMenus(String id,String state, String openid,String firmid,String datetime,String dat){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Net_Orders> listNet_Orders=new ArrayList<Net_Orders>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=id && !id.equals(""))){
				sbf.append(" AND ORDERS.ID='"+id+"'");
			}
			if((null!=state && !state.equals(""))){
				sbf.append(" AND ORDERS.STATE="+state);
			}else{
				//sbf.append(" AND ORDERS.STATE IN(1,2) ");
			}
			if((null!=firmid && !firmid.equals(""))){
				sbf.append(" AND F.FIRMID='"+firmid+"'");
			}
			if((null!=openid && !openid.equals(""))){
				sbf.append(" AND ORDERS.OPENID='"+openid+"'");
			}
			if((null!=datetime && !datetime.equals(""))){
				sbf.append(" AND ORDERS.DATMINS='"+datetime+"'");
			}
			if((null!=dat && !dat.equals(""))){
				sbf.append(" AND ORDERS.DAT=to_date('"+dat+"','yyyy-MM-dd')");
			}
			String sql = " SELECT ORDERS.STATE AS STATE,ORDERS.ID AS ID,ORDERS.RESV AS RESV,ORDERS.ORDERTIME AS ORDERTIME,F.PK_STORE AS FIRMID,F.VNAME AS FIRMDES,ORDERS.DAT AS DAT,ORDERS.DATMINS AS DATEMINS," +
						 " ORDERS.PAX AS PAX,ORDERS.SFT AS SFT,ORDERS.TABLES AS TABLES,ORDERS.CONTACT AS CONTACT,ORDERS.ISFEAST AS ISFEAST,ORDERS.SUMPRICE AS MONEY," +
						 " ORDERS.PAYMONEY AS PAYMONEY, ORDERS.VTRANSACTIONID AS VTRANSACTIONID, ORDERS.OUTTRADENO AS OUTTRADENO, ORDERS.VINVOICETITLE AS VINVOICETITLE, " + 
						 " ORDERS.ADDR AS ADDR,F.TELE AS TELE,ORDERS.OPENID AS OPENID,ORDERS.RANNUM AS RANNUM,ORDERS.REMARK AS REMARK FROM NET_ORDERS ORDERS LEFT JOIN CBOH_STORE_3CH F ON F.PK_STORE=ORDERS.FIRMID WHERE 1=1 AND ORDERS.DR=0 AND DAT>= TO_DATE('"+
						 DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")+"','yyyy-MM-dd') "+sbf+" ORDER BY ORDERS.DAT ASC";
//			System.out.println(sql);
			rs = st.executeQuery(sql);
			while(rs.next()){
				Net_Orders orders=new Net_Orders();
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
				orders.setIsfeast(rs.getString("ISFEAST"));
				orders.setMoney(rs.getString("MONEY"));
				orders.setPaymoney(WeChatUtil.formatDoubleLength(rs.getString("PAYMONEY"), 2));
				orders.setVtransactionid(rs.getString("VTRANSACTIONID"));
				orders.setOutTradeNo(rs.getString("OUTTRADENO"));
				orders.setVinvoicetitle(rs.getString("VINVOICETITLE"));
				listNet_Orders.add(orders);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listNet_Orders;
	}
	
	/**
	 * 查询我的订单详情
	 * @param state
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<Net_OrderDtl> getOrderDtlMenus(String orderid){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Net_OrderDtl> listNet_OrdersDtl=new ArrayList<Net_OrderDtl>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=orderid && !orderid.equals(""))){
				sbf.append(" AND ORDERSID='"+orderid+"'");
			}
			String sql = "SELECT O.ID,O.FOODSID,O.ORDERSID,O.FOODNUM,TO_CHAR(O.TOTALPRICE,'FM999990.00') AS TOTALPRICE,O.FOODSNAME,O.UNIT," + 
					"TO_CHAR((O.TOTALPRICE/O.FOODNUM),'FM999990.00') AS PRICE,O.ISPACKAGE,O.REMARK,P.VCODE AS PCODE," + 
					"P.VREQREDEFINE AS REQREDEFINE,T.CODE AS GRPTYP,P.PRODREQADDFLAG AS PRODREQADDFLAG " +
					" FROM NET_ORDERDETAIL O" +
					" LEFT JOIN CBOH_PUBITEM_3CH P ON O.FOODSID = P.PK_PUBITEM " +
					" LEFT JOIN FD_ITEMTYPE T ON P.PK_ITEMTYPE = T.ID WHERE 1=1"+sbf;
			rs = st.executeQuery(sql);
			while(rs.next()){
				Net_OrderDtl ordersDtl=new Net_OrderDtl();
				ordersDtl.setId(rs.getString("ID"));
				ordersDtl.setFoodsid(rs.getString("FOODSID"));
				ordersDtl.setOrdersid(rs.getString("ORDERSID"));
				ordersDtl.setFoodnum(rs.getString("FOODNUM"));
				ordersDtl.setTotalprice(rs.getString("TOTALPRICE"));
				ordersDtl.setFoodsname(rs.getString("FOODSNAME"));
				ordersDtl.setPrice(rs.getString("PRICE"));
				ordersDtl.setIspackage(rs.getString("ISPACKAGE"));
				ordersDtl.setRemark(rs.getString("REMARK") == null ? "" : rs.getString("REMARK"));
				ordersDtl.setPcode(rs.getString("PCODE"));
				ordersDtl.setReqredefine(rs.getString("REQREDEFINE"));
				ordersDtl.setGrptyp(rs.getString("GRPTYP"));
				ordersDtl.setProdReqAddFlag(rs.getString("PRODREQADDFLAG"));
				ordersDtl.setUnit(rs.getString("UNIT"));
				listNet_OrdersDtl.add(ordersDtl);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listNet_OrdersDtl;
	}
	
	/**
	 * 查询是否有套餐
	 */
	public String flagPkgPackAge(String firmId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String count="";
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql = "SELECT COUNT(ID) AS ID FROM ITEMPRGPACKAGE WHERE firmid = '"+firmId+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				count=rs.getString("ID");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	
	/**
	 * 查询套餐
	 */
	public List<ItemPrgPackage> findPkgPackAge(String id, String firmId) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<ItemPrgPackage> listItemPrgPackage=new ArrayList<ItemPrgPackage>();
		StringBuffer sbf=new StringBuffer();
		try {
			if(null!=id && !id.equals("")){
				sbf.append(" AND ID='"+id+"'");
			}
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql = "SELECT ID,PRGID,PACKAGE,DES,PRICE,SNO,SNODES,FIRMID, PICSRC AS WPICSRC FROM ITEMPRGPACKAGE " +
						" WHERE STA='Y' AND FIRMID='"+firmId+"'";
			
			rs = st.executeQuery(sql);
			while(rs.next()){
				ItemPrgPackage itemPrgPackage=new ItemPrgPackage();
				itemPrgPackage.setId(rs.getString("ID"));
				itemPrgPackage.setPrgid(rs.getString("PRGID"));
				itemPrgPackage.setPackages(rs.getString("PACKAGE"));
				itemPrgPackage.setDes(rs.getString("DES"));
				itemPrgPackage.setPrice(rs.getString("PRICE"));
				itemPrgPackage.setSno(rs.getString("SNO"));
				itemPrgPackage.setSnodes(rs.getString("SNODES"));
				itemPrgPackage.setFirmid(rs.getString("FIRMID"));
				itemPrgPackage.setPicsrc(rs.getString("WPICSRC"));
				listItemPrgPackage.add(itemPrgPackage);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listItemPrgPackage;
	}
	
	/**
	 * 查询套餐明细
	 */
	public List<ItemPrgpackAgedtl> findPkgPackAgeDtl(String idCode, String firmId,
			String packAgeId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<ItemPrgpackAgedtl> listItemPrgpackAgedtl=new ArrayList<ItemPrgpackAgedtl>();
		StringBuffer sbf=new StringBuffer();
		try {
			if(null!=idCode && !idCode.equals("")){
				sbf.append(" AND IDCODE='"+idCode+"'");
			}
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql = "SELECT IDCODE,PRGID,PUBITEM,PITCODE,PACKAGE,ID,ITEM,DES,CNT,UNIT,FIRMID,PACKAGEID FROM ITEMPRGPACKAGEDTL " +
						"WHERE FIRMID='"+firmId+"' AND PACKAGEID='"+packAgeId+"'"+sbf;
			rs = st.executeQuery(sql);
			while(rs.next()){
				ItemPrgpackAgedtl itemPrgpackAgedtl=new ItemPrgpackAgedtl();
				itemPrgpackAgedtl.setIdcode(rs.getString("IDCODE"));
				itemPrgpackAgedtl.setPrgid(rs.getString("PRGID"));
				itemPrgpackAgedtl.setPubitem(rs.getString("PUBITEM"));
				itemPrgpackAgedtl.setPitcode(rs.getString("PITCODE"));
				itemPrgpackAgedtl.setPackages(rs.getString("PACKAGE"));
				itemPrgpackAgedtl.setId(rs.getString("ID"));
				itemPrgpackAgedtl.setItem(rs.getString("ITEM"));
				itemPrgpackAgedtl.setDes(rs.getString("DES"));
				itemPrgpackAgedtl.setCnt(rs.getString("CNT"));
				itemPrgpackAgedtl.setUnit(rs.getString("UNIT"));
				itemPrgpackAgedtl.setFirmid(rs.getString("FIRMID"));
				itemPrgpackAgedtl.setPackageid(rs.getString("PACKAGEID"));
				listItemPrgpackAgedtl.add(itemPrgpackAgedtl);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listItemPrgpackAgedtl;
	}
	
	/**
	 * 菜品
	 */
	public List<Project> getTeleProject(String protypeid, String scode, String name,String firmId,String id){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Project> listProject=new ArrayList<Project>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=protypeid && !protypeid.equals(""))){
				sbf.append(" AND ITEMTYPE.ID='"+protypeid+"'");
			}
			if((null!=scode && !scode.equals(""))){
				sbf.append(" AND ITEMSALE.ITCODE='"+scode+"'");
			}
			if(null!=name && !name.equals("")){
				sbf.append(" AND ITEMSALE.DES like '%"+name+"%'");
			}
			if(null!=firmId && !firmId.equals("")){
				sbf.append(" AND ITEMSALE.FIRMID='"+firmId+"'");
			}
			if((null!=id && !id.equals(""))){
				sbf.append(" AND ITEMSALE.ID='"+id+"'");
			}
			String sql = "SELECT distinct ITEMSALE.ID AS ID,ITEMSALE.FIRMID AS FIRMID,ITEMSALE.ITCODE AS ITCODE," +
						"ITEMSALE.DES AS DES,ITEMSALE.UNIT AS UNIT,ITEMSALE.PRICE AS PRICE,ITEMSALE.INIT AS INIT,ITEMSALE.WXBIGPIC AS WXBIGPIC," +
						"ITEMSALE.WXSMALLPIC AS WXSMALLPIC,ITEMSALE.GRPSTR AS GRPSTR FROM FD_ITEMSALE ITEMSALE "
						+" LEFT JOIN FD_ITEMTYPE ITEMTYPE ON ITEMTYPE.NAME=ITEMSALE.GRPSTR WHERE ITEMSALE.IFUSE='Y' "+sbf;
			rs = st.executeQuery(sql);
			while(rs.next()){
				Project project=new Project();
				project.setPubitem(rs.getString("ID"));
				project.setPitcode(rs.getString("ITCODE"));
				project.setPdes(rs.getString("DES"));
				project.setPinit(rs.getString("INIT"));
				project.setPrice(rs.getString("PRICE"));
				project.setUrl(rs.getString("WXBIGPIC"));
				project.setSmallUrl(rs.getString("WXSMALLPIC"));
				listProject.add(project);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listProject;
	}
	
	/**
	 * 查询热门菜品
	 */
	public List<Project> findHotItem(String firmId) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Project> listProject=new ArrayList<Project>();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql = "SELECT ID,DES,PRICE,FOODNUM,WXSMALLPIC,WXBIGPIC FROM "+
						" (SELECT "+
						"       ITEMSALE.ID AS ID,"+
						"       ITEMSALE.DES AS DES,"+
						"       ITEMSALE.PRICE AS PRICE,"+
						"       ITEMSALE.WXSMALLPIC AS WXSMALLPIC,"+
						"		ITEMSALE.WXBIGPIC AS WXBIGPIC,"+
						"       COUNT(orderdtl.FOODNUM) AS FOODNUM"+
						"   FROM NET_ORDERDETAIL ORDERDTL,FD_ITEMSALE ITEMSALE"+
						"   WHERE ORDERDTL.FOODSID=ITEMSALE.ID"+
						"         AND ITEMSALE.FIRMID='"+firmId+"'"+
						"   GROUP BY ITEMSALE.ID,ITEMSALE.DES,ITEMSALE.PRICE,ITEMSALE.WXSMALLPIC,ITEMSALE.WXBIGPIC"+
						"   ORDER BY COUNT(ORDERDTL.FOODNUM) DESC)"+
						"   WHERE ROWNUM <= 20";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Project project=new Project();
				project.setPubitem(rs.getString("ID"));
				project.setPdes(rs.getString("DES"));
				project.setPrice(rs.getString("PRICE"));
				project.setUrl(rs.getString("WXBIGPIC"));
				project.setSmallUrl(rs.getString("WXSMALLPIC"));
				listProject.add(project);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listProject;
	}
	
	/**
	 * 查询我的订单隶属门店
	 * @param state
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<Net_Orders> getOrderCity(String state, String openid){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Net_Orders> listNet_Orders=new ArrayList<Net_Orders>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
//			if((null!=state && !state.equals(""))){
//				sbf.append(" AND ORDERS.STATE IN (1,6)");
//			}
//			String sqlUpd="UPDATE NET_ORDERS SET STATE=3 WHERE STATE=1 AND OPENID='"+openid+"' AND DAT< TO_DATE('"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")+"','yyyy-MM-dd')";
//			
//			st.executeUpdate(sqlUpd);
			
			String sql = " SELECT F.FIRMID AS FIRMID,"+
					     " F.FIRMDES AS FIRMDES,"+
					     " ORDERS.DAT AS DAT,"+
					     " ORDERS.DATMINS AS DATEMINS,"+
					     " ORDERS.OPENID AS OPENID"+
					 " FROM NET_ORDERS ORDERS"+
					 " LEFT JOIN FIRM F ON F.FIRMID = ORDERS.FIRMID"+
					 " WHERE 1=1 AND ORDERS.DR=0  AND DAT>= TO_DATE('"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")+"','yyyy-MM-dd') AND ORDERS.OPENID='"+openid+"'  AND ORDERS.STATE IN (1,2) "+
					 " GROUP BY F.FIRMID,F.FIRMDES,ORDERS.DAT,ORDERS.DATMINS,ORDERS.OPENID  ORDER BY ORDERS.DAT ASC";
			System.out.println(sql);
			rs = st.executeQuery(sql);
			
			while(rs.next()){
				Net_Orders orders=new Net_Orders();
				orders.setFirmid(rs.getString("FIRMID"));
				orders.setFirmdes(rs.getString("FIRMDES"));
				orders.setDat(DateFormat.getStringByDate(rs.getTimestamp("DAT"), "yyyy-MM-dd"));
				orders.setDatmins(rs.getString("DATEMINS"));
				orders.setOpenid(rs.getString("OPENID"));
				listNet_Orders.add(orders);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listNet_Orders;
	}
	
	/**
	 * 查询班次
	 * @param des
	 * @return
	 * @throws Exception
	 */
	public List<Sft> getSFT(String des) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Sft> listSft=new ArrayList<Sft>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=des && !des.equals(""))){
				sbf.append(" AND NAME='"+des+"'");
			}
			String sql = "SELECT ID,CODE,NAME FROM NET_CODE WHERE PAGETYP = 2 ORDER BY CODE";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Sft sft=new Sft();
				sft.setId(rs.getString("ID"));
				sft.setCode(rs.getString("CODE"));
				sft.setName(rs.getString("NAME"));
				listSft.add(sft);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listSft;
	}
	
	/**
	 * 查询可预订台位
	 */
	public List<StoreTable> findStoreTable(String sft, String dat, String firmId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<StoreTable> listStoreTable=new ArrayList<StoreTable>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=sft && !sft.equals(""))){
				sbf.append(" AND ND.SFT="+sft);
			}
			if(null!=dat && !dat.equals("")){
				sbf.append(" AND ND.DAT=TO_DATE('"+dat+"','yyyy-MM-dd')");
			}
			String sql = "SELECT RESV.PAX AS PAX,COUNT(RESV.ID) AS NUM ,roomtyp"+
			" FROM NET_RESVTBL RESV  "+
			" WHERE 1 = 1  "+
			" AND RESV.ISDEL = 'Y'  "+
			" AND RESV.FIRMID = '"+firmId+"'"+
			" AND roomtyp='大厅' "+
			" AND RESV.ID NOT IN(  "+
			" SELECT ND.RESVTBLID FROM NET_DESKTIMES ND WHERE ND.STATE = '1' "+ sbf+" ) "+
			" GROUP BY RESV.PAX,roomtyp  "+
			" UNION "+
			" SELECT 15 AS PAX,COUNT(RESV.ID) AS NUM ,roomtyp "+
			" FROM NET_RESVTBL RESV  "+
			" WHERE 1 = 1  "+
			" AND RESV.ISDEL = 'Y'  "+
			" AND RESV.FIRMID = '"+firmId+"'"+
			"  AND roomtyp='包间' "+
			" AND RESV.ID NOT IN(  "+
			" SELECT ND.RESVTBLID FROM NET_DESKTIMES ND WHERE ND.STATE = '1' "+ sbf+" ) "+
			" GROUP BY roomtyp ";
			rs = st.executeQuery(sql);
			while(rs.next()){
				StoreTable storeTable=new StoreTable();
				storeTable.setPax(rs.getString("PAX"));
				storeTable.setNum(rs.getString("NUM"));
				storeTable.setRoomtyp(rs.getString("ROOMTYP"));
				
				listStoreTable.add(storeTable);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listStoreTable;
	}
	
	/**
	 * 查询可预订的包间或者大厅
	 */
	public List<StoreTable> findResvTbl(String roomTyp, String sft, String dat,
			String firmId,String pax){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<StoreTable> listStoreTable=new ArrayList<StoreTable>();
		StringBuffer sbf=new StringBuffer();
		String paxs="";
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=sft && !sft.equals(""))){
				sbf.append(" AND ND.SFT="+sft);
			}
			if((null!=pax && !pax.equals(""))){
				paxs=" AND RESV.PAX="+pax;
			}
			if(null!=dat && !dat.equals("")){
				sbf.append(" AND ND.DAT=TO_DATE('"+dat+"','yyyy-MM-dd')");
			}
			String sql = "SELECT RESV.PAX AS PAX,RESV.ID AS ID,ROOMTYP,TBL AS TBL,RESV.DES AS DES "+
					     "FROM NET_RESVTBL RESV  "+
							" WHERE 1 = 1  "+
							" AND RESV.ISDEL = 'Y' "+ 
							" AND RESV.FIRMID = '"+firmId+"'"+
							" AND ROOMTYP='"+roomTyp+"' "+
							paxs+
							" AND RESV.ID NOT IN(  "+
							" SELECT ND.RESVTBLID FROM NET_DESKTIMES ND WHERE ND.STATE = '1' "+sbf+" ) ";
			rs = st.executeQuery(sql);
			while(rs.next()){
				StoreTable storeTable=new StoreTable();
				storeTable.setPax(rs.getString("PAX"));
				storeTable.setId(rs.getString("ID"));
				storeTable.setRoomtyp(rs.getString("ROOMTYP"));
				storeTable.setDes(rs.getString("DES"));
				storeTable.setTbl(rs.getString("TBL"));
				listStoreTable.add(storeTable);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listStoreTable;
	}
	
	/**
	 * 查询时间桌台，台位状态
	 * @param resvtblid
	 * @param ordersid
	 * @return
	 * @throws Exception
	 */
	public List<DeskTimes> getDeskTimes(String resvtblid, String ordersid,String sft,String dat) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<DeskTimes> listDeskTimes=new ArrayList<DeskTimes>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=resvtblid && !resvtblid.equals(""))){
				sbf.append(" AND RESVTBLID='"+resvtblid+"'");
			}
			if((null!=ordersid && !ordersid.equals(""))){
				sbf.append(" AND ORDERSID='"+ordersid+"'");
			}
			if((null!=sft && !sft.equals(""))){
				sbf.append(" AND SFT='"+sft+"'");
			}
			if((null!=dat && !dat.equals(""))){
				sbf.append(" AND DAT=to_date('"+dat+"','yyyy-MM-dd')");
			}
			String sql = "SELECT ID,RESVTBLID,DAT,SFT,REMARK,STATE,ORDERSID FROM NET_DESKTIMES WHERE 1=1"+sbf;
			rs = st.executeQuery(sql);
			while(rs.next()){
				DeskTimes deskTimes=new DeskTimes();
				deskTimes.setId(rs.getString("ID"));
				deskTimes.setResvtblid(rs.getString("RESVTBLID"));
				deskTimes.setDat(rs.getString("DAT"));
				deskTimes.setSft(rs.getString("SFT"));
				deskTimes.setRemark(rs.getString("REMARK"));
				deskTimes.setState(rs.getString("STATE"));
				deskTimes.setOrdersid(rs.getString("ORDERSID"));
				listDeskTimes.add(deskTimes);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listDeskTimes;
	}
	
	/**
	 * 保存时间桌台
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	public String saveDeskTimes(DeskTimes deskTimes) {
		Connection conn = null;
		Statement st = null;
		int i=0;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			
			String sql = "INSERT INTO NET_DESKTIMES"+
						 " (ID, RESVTBLID, DAT, SFT, REMARK, STATE, ORDERSID)"+
						" VALUES (" +
						"'"+deskTimes.getId()+"'" +
						 ", '"+deskTimes.getResvtblid()+"'" +
						 ", to_date('"+deskTimes.getDat()+"','yyyy-MM-dd')" +
						 ", '"+deskTimes.getSft()+"'" +
						 ", '"+deskTimes.getRemark()+"'" +
						 ", '"+deskTimes.getState()+"'" +
						 ", '"+deskTimes.getOrdersid()+"')";
		    i=st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i+"";
	}
	
	/**
	 * 点菜保存
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	public String saveMenus(Net_Orders orders) {
		Connection conn = null;
		Statement st = null;
		int i=0;
		String remark="";
		String remarkVal="";
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=orders.getRemark() && !orders.getRemark().equals(""))){
				remark=" ,remark";
				remarkVal=",'"+orders.getRemark()+"'";
			}
			String sql = "INSERT INTO net_orders "
						+"(ID, resv, TABLES,pax, ordertime, dat, sft, state, contact, firmid "+remark+",bev, isfeast, addr, datmins, openid, rannum)"
						+" VALUES ('"+orders.getId()+"',"+orders.getResv()+",'"+orders.getTables()+"',"+orders.getPax()+",sysdate,to_date('"
						+orders.getDat()+"','yyyy-mm-dd'),'"+orders.getSft()+"',"+
						"'1','"+orders.getContact()+"','"+orders.getFirmid()+"'"+remarkVal+",3,"+orders.getIsfeast()+",'"+orders.getAddr()
						+"','"+orders.getDatmins()+"','"+
						orders.getOpenid()+"','"+orders.getRannum()+"'"+")";
			i=st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(i>0){
			String path=CodeHelper.QrcodePic+orders.getRannum()+".png";
			CardQrCode handler = new CardQrCode();  
	        handler.encoderQRCode(orders.getRannum(), path, "png");  
			return "1";
		}
		return "0";
	}
	
	/**
	 * 台位
	 */
	public List<StoreTable> getTeleStoreTable(String areaid, String code, String name,String firmId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<StoreTable> listStoreTable=new ArrayList<StoreTable>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			if((null!=code && !code.equals(""))){
				sbf.append(" AND TBL="+code);
			}
			if(null!=name && !name.equals("")){
				sbf.append(" AND DES='"+name+"'");
			}
			if(null!=areaid && !areaid.equals("")){
				sbf.append(" AND AREA='"+areaid+"'");
			}
			if(null!=firmId && !firmId.equals("")){
				sbf.append(" AND firmid='"+firmId+"'");
			}
			String sql = "SELECT ID,FIRMID,TBL,DES,INIT,PAX,MINCOST,ROOMTYP,AREA FROM NET_RESVTBL WHERE 1=1 AND isdel='Y'"+sbf;
			rs = st.executeQuery(sql);
			while(rs.next()){
				StoreTable storeTable=new StoreTable();
				storeTable.setId(rs.getString("ID"));
				storeTable.setFirmid(rs.getString("FIRMID"));
				storeTable.setTbl(rs.getString("TBL"));
				storeTable.setDes(rs.getString("DES"));
				storeTable.setInit(rs.getString("INIT"));
				storeTable.setPax(rs.getString("PAX"));
				storeTable.setMincost(rs.getString("MINCOST"));
				storeTable.setRoomtyp(rs.getString("ROOMTYP"));
				storeTable.setArea(rs.getString("AREA"));
				
				listStoreTable.add(storeTable);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listStoreTable;
	}
	
	/**
	 * 修改账单备注
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	public String updateOrders(String id, String remark){
		Connection conn = null;
		Statement st = null;
		int i=0;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			
			String sql = "UPDATE NET_ORDERS SET REMARK='"+remark+"' WHERE ID='"+id+"'";
		    i=st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i+"";
	}
	
	/**
	 * 区域
	 */
	public List<Arear> getTeleArea(String code, String name,String firmid) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Arear> listArea=new ArrayList<Arear>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getTELEConnection();
			st = conn.createStatement();
			if((null!=code && !code.equals(""))){
				sbf.append(" AND CODE='"+code+"'");
			}
			if((null!=firmid && !firmid.equals(""))){
				sbf.append(" AND FIRMID='"+firmid+"'");
			}
			if(null!=name && !name.equals("")){
				sbf.append(" AND DES='"+name+"'");
			}
			String sql = "SELECT FIRMID,CODE,SNO,DES FROM FD_CODEDESC WHERE 1=1"+sbf;
			rs = st.executeQuery(sql);
			while(rs.next()){
				Arear arear=new Arear();
				arear.setAreaid(rs.getString("SNO"));
				arear.setAreaname(rs.getString("DES"));
				listArea.add(arear);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listArea;
	}
	
	/**
	 * 点菜保存菜单详情
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	public String saveOrderDtl(Net_OrderDtl ordersDtl) {
		Connection conn = null;
		Statement st = null;
		int i=0;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql1 ="INSERT INTO net_orderdetail "
						+"(ID, foodsid, ordersid, foodnum, totalprice, foodsname, ispackage) "
						+"VALUES ('"+ordersDtl.getId()+"','"+ordersDtl.getFoodsid()+"','"+
						ordersDtl.getOrdersid()+"',"+ordersDtl.getFoodnum()+","+ordersDtl.getTotalprice()+",'"+
						ordersDtl.getFoodsname()+"','"+ordersDtl.getIspackage()+"')";
			i=st.executeUpdate(sql1);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i+"";
	}
	/**
	 * 取消订单
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	public String deleteOrder(String state,String resv) {
		Connection conn = null;
		Statement st = null;
		int i=0;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql ="UPDATE NET_ORDERS SET STATE="+state+" WHERE RESV='"+resv+"'";
			i=st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i+"";
	}
	/**
	 * 删除菜品
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	public String deleteOrderDtl(String resv,String id) {
		Connection conn = null;
		Statement st = null;
		int i=0;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			StringBuffer sbf=new StringBuffer();
			if((null!=resv && !resv.equals(""))){
				sbf.append("ORDERSID='"+resv+"'");
			}
			if((null!=id && !id.equals(""))){
				sbf.append("ID='"+id+"'");
			}
			String sql ="DELETE NET_ORDERDETAIL WHERE "+sbf;
			i=st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i+"";
	}
	
	/**
	 * 自助点菜保存菜品
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	public String saveOrdersOrDtl(Net_Orders orders) {
		Connection conn = null;
		Statement st = null;
		int i=0;
		String remark="";
		String remarkVal="";
		String tables="";
		String tablesVal="";
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			
			if((null!=orders.getRemark() && !orders.getRemark().equals(""))){
				remark=" ,remark";
				remarkVal=",'"+orders.getRemark()+"'";
			}
			if((null!=orders.getTables() && !orders.getTables().equals(""))){
				tables=" ,tables";
				tablesVal=",'"+orders.getTables()+"'";
			}
			String sql = "INSERT INTO net_orders "
					+"(ID, resv "+tables+" , ordertime, dat, sft, state, firmid "+remark+",bev,isfeast, addr, datmins, openid, rannum)"
					+" VALUES ('"+orders.getId()+"','"+orders.getResv()+"'"+tablesVal+",sysdate,"+"to_date('"
					+orders.getDat()+"','yyyy-mm-dd'),'"+orders.getSft()+"',"+"'"+orders.getState()+"','"
					+orders.getFirmid()+"'"+remarkVal+",3,"+orders.getIsfeast()+",'"+orders.getAddr()
					+"','"+orders.getDatmins()+"','"+
					orders.getOpenid()+"','"+orders.getRannum()+"'"+")";
		    i=st.executeUpdate(sql);
			
			if(i>0){
				for(int j=0;j<orders.getListNetOrderDtl().size();j++){
					Net_OrderDtl ordersDtl=orders.getListNetOrderDtl().get(j);
					String sql1 ="INSERT INTO net_orderdetail "
								+"(ID, foodsid, ordersid, remark, foodnum, totalprice, foodsname, ispackage) "
								+"VALUES ('"+ordersDtl.getId()+"','"+ordersDtl.getFoodsid()+"','"+
								ordersDtl.getOrdersid()+"','"+ordersDtl.getRemark()+"',"+ordersDtl.getFoodnum()+","+ordersDtl.getTotalprice()+",'"+
								ordersDtl.getFoodsname()+"','"+ordersDtl.getIspackage()+"')";
					i=st.executeUpdate(sql1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i+"";
	}
	
	/**
	 * 查询优惠信息
	 */
	public List<WebMsg> findWebMsg(String dat, String firmId,String FavorAreaId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<WebMsg> listWebMsg=new ArrayList<WebMsg>();
		StringBuffer sbf=new StringBuffer();
		String nowDate=DateFormat.getStringByDate(new Date(), "yyyy-MM-dd");
		try {
			if((null!=dat && !dat.equals(""))){
				sbf.append(" AND TO_CHAR(DAT,'YYYY-MM-DD')='"+dat+"'");
			}
			if((null!=firmId && !firmId.equals(""))){
				sbf.append(" AND FIRMID='"+firmId+"'");
			}
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql = "SELECT  ID,DAT,TITLE,WURL,WCONTENT,KEYWORD,FIRMID,FIRMNM,FAVORAREAID,STARTDATE,ENDDATE FROM WEB_MSG "+
						" WHERE FAVORAREAID= '"+FavorAreaId+"' AND TO_DATE('"+nowDate+"','yyyy-MM-dd') BETWEEN TO_DATE(STARTDATE,'yyyy-MM-dd') AND TO_DATE(ENDDATE,'yyyy-MM-dd')"+
						 " "+sbf+"  ORDER BY ENDDATE DESC";
			rs = st.executeQuery(sql);
			while(rs.next()){
				WebMsg webMsg=new WebMsg();
				webMsg.setId(rs.getString("ID"));
				webMsg.setDat(rs.getString("DAT"));
				webMsg.setTitle(rs.getString("TITLE"));
				webMsg.setWurl(rs.getString("WURL"));
				webMsg.setWcontent(rs.getString("WCONTENT"));
				webMsg.setKeyword(rs.getString("KEYWORD"));
				webMsg.setFirmid(rs.getString("FIRMID"));
				webMsg.setFirmnm(rs.getString("FIRMNM"));
				listWebMsg.add(webMsg);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listWebMsg;
	}
	/**
	 * 查询优惠信息区域
	 */
	public List<FavorArea> findFavorArea(String id){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<FavorArea> listFavorArea=new ArrayList<FavorArea>();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql = "SELECT ID,AREA,FIRMID FROM FAVORAREA";
			rs = st.executeQuery(sql);
			while(rs.next()){
				FavorArea favorArea=new FavorArea();
				favorArea.setId(rs.getString("ID"));
				favorArea.setArea(rs.getString("AREA"));
				favorArea.setFirmid(rs.getString("FIRMID"));
				listFavorArea.add(favorArea);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listFavorArea;
	}
	
	/**
	 * 修改账单状态
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	public String updateOrderState(Net_Orders orders){
		Connection conn = null;
		Statement st = null;
		int i=0;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql = "UPDATE NET_ORDERS SET state="+orders.getState()+" WHERE ID='"+orders.getId()+"'";
		    i=st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i+"";
	}
	/**
	 * 生成账单消费记录
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	public String orderOutAmt(Net_Orders orders) {
		Connection conn = null;
		Statement st = null;
		int i=0;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			
			String sql = "INSERT INTO net_orders "+
						 " (ID, resv, TABLES, pax, ordertime, dat, sft, state, contact, firmid, payment, addr) "+
						"VALUES "+
						 " ('"+orders.getId()+"', '"+orders.getResv()+"', '"+orders.getTables()+"', "+orders.getPax()+", SYSDATE, " +
						 "to_date('"+orders.getDat()+"','yyyy-MM-dd'), '"+orders.getSft()+"', "+orders.getState()+", " +
						 "'"+orders.getContact()+"', '"+orders.getFirmid()+"', "+orders.getPayment()+", '"+orders.getAddr()+"')";

		    i=st.executeUpdate(sql);
			String sql2 = "INSERT INTO printorders "+
					 " (ID, resv, TABLES, pax, ordertime, dat, sft, state, contact, firmid, payment, addr, printstate, cardzamt, cashier, receivable, cardamt) "+
					"VALUES "+
					 " ('"+orders.getId()+"', '"+orders.getResv()+"', '"+orders.getTables()+"', "+orders.getPax()+", SYSDATE, " +
					 "to_date('"+orders.getDat()+"','yyyy-MM-dd'), '"+orders.getSft()+"', "+orders.getState()+", " +
					 "'"+orders.getContact()+"', '"+orders.getFirmid()+"', "+orders.getPayment()+", '"+orders.getAddr()+"', " +
					 "'"+orders.getPrintstate()+"', '"+orders.getCardzamt()+"', '"+orders.getCashier()+"', '"+orders.getReceivable()+"', '"+orders.getCardamt()+"') ";

		    i=st.executeUpdate(sql2);
			if(i>0){
				for(int j=0;j<orders.getListNetOrderDtl().size();j++){
					Net_OrderDtl ordersDtl=orders.getListNetOrderDtl().get(j);
					String sql1 ="INSERT INTO net_orderdetail "
								+"(ID, foodsid, ordersid, foodnum, totalprice, foodsname, ispackage) "
								+"VALUES ('"+ordersDtl.getId()+"','"+ordersDtl.getFoodsid()+"','"+
								ordersDtl.getOrdersid()+"',"+ordersDtl.getFoodnum()+","+ordersDtl.getTotalprice()+",'"+
								ordersDtl.getFoodsname()+"','"+ordersDtl.getIspackage()+"')";
					i=st.executeUpdate(sql1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i+"";
	}

	/**
	 * 通过门店查询优惠信息
	 * @param firmid
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<WebMsg> findStoreWebMsg(String firmId) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<WebMsg> listWebMsg=new ArrayList<WebMsg>();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql = "SELECT  ID,DAT,TITLE,WURL,WCONTENT,KEYWORD,FIRMID,FIRMNM,FAVORAREAID,STARTDATE,ENDDATE FROM WEB_MSG "+
						" WHERE FIRMID='"+firmId+"' ORDER BY ENDDATE DESC";
			rs = st.executeQuery(sql);
			while(rs.next()){
				WebMsg webMsg=new WebMsg();
				webMsg.setId(rs.getString("ID"));
				webMsg.setDat(rs.getString("DAT"));
				webMsg.setTitle(rs.getString("TITLE"));
				webMsg.setWurl(rs.getString("WURL"));
				webMsg.setWcontent(rs.getString("WCONTENT"));
				webMsg.setKeyword(rs.getString("KEYWORD"));
				webMsg.setFirmid(rs.getString("FIRMID"));
				webMsg.setFirmnm(rs.getString("FIRMNM"));
				listWebMsg.add(webMsg);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listWebMsg;
	}
	/**
	 * 通过扫码表示获取台位信息
	 * @param scene_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<StoreTable> findRestTbl(String scene_id){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<StoreTable> listStoreTable=new ArrayList<StoreTable>();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			String sql = "SELECT ID, FIRMID, TBL, DES FROM NET_RESVTBL WHERE SCENE_ID="+scene_id;
			rs = st.executeQuery(sql);
			while(rs.next()){
				StoreTable storeTable=new StoreTable();
				storeTable.setId(rs.getString("ID"));
				storeTable.setFirmid(rs.getString("FIRMID"));
				storeTable.setTbl(rs.getString("TBL"));
				storeTable.setDes(rs.getString("DES"));
				listStoreTable.add(storeTable);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listStoreTable;
	}
	
	
	/**
	 * 查询我的订单
	 * @param state
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<Net_Orders> queryfolio(String resvno,String state,String firmid,String dat){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Net_Orders> listNet_Orders=new ArrayList<Net_Orders>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			
			if((null!=resvno && !resvno.equals(""))){
				sbf.append(" AND ORDERS.RESV='"+resvno+"'");
			}
			
			if((null!=state && !state.equals(""))){
				sbf.append(" AND ORDERS.STATE="+state);
			}else{
				sbf.append(" AND ORDERS.STATE IN(1,2) ");
			}
			if((null!=firmid && !firmid.equals(""))){
				sbf.append(" AND F.FIRMID='"+firmid+"'");
			}
			if((null!=dat && !dat.equals(""))){
				sbf.append(" AND ORDERS.DAT=to_date('"+dat+"','yyyy-MM-dd')");
			}
			String sql = " SELECT ORDERS.STATE AS STATE,ORDERS.ID AS ID,ORDERS.RESV AS RESV,ORDERS.ORDERTIME AS ORDERTIME,F.FIRMID AS FIRMID,F.FIRMDES AS FIRMDES,ORDERS.DAT AS DAT,ORDERS.DATMINS AS DATEMINS," +
						 " ORDERS.PAX AS PAX,ORDERS.SFT AS SFT,ORDERS.TABLES AS TABLES,ORDERS.CONTACT AS CONTACT," +
						 " ORDERS.ADDR AS ADDR,F.TELE AS TELE,ORDERS.OPENID AS OPENID,ORDERS.RANNUM AS RANNUM,ORDERS.REMARK AS REMARK FROM NET_ORDERS ORDERS LEFT JOIN FIRM F ON F.FIRMID=ORDERS.FIRMID WHERE 1=1 AND ORDERS.DR=0 "+sbf+" ORDER BY ORDERS.DAT DESC";
//			System.out.println(sql);
			rs = st.executeQuery(sql);
			while(rs.next()){
				Net_Orders orders=new Net_Orders();
				orders.setId(rs.getString("ID"));
				orders.setResv(rs.getString("RESV"));
				orders.setOrderTimes(DateFormat.getStringByDate(rs.getTimestamp("ORDERTIME"), "yyyy-MM-dd"));
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
				listNet_Orders.add(orders);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listNet_Orders;
	}
	/**
	 * 修改订单
	 */
	@Override
	public String updateOrdr(Net_Orders orders) {
		String updateOrderSql="update net_orders set ";
		StringBuffer sbf = new StringBuffer(); 
		//----------------------需要更新的值----------------------------
		//台位
		if(StringUtils.hasText(orders.getTables())){
			sbf.append(",tables = '").append(orders.getTables()).append("' ");
		}
		//订单状态
		if(StringUtils.hasText(orders.getState())){
			sbf.append(",state = '").append(orders.getState()).append("' ");
		}
		//就餐人数
		if(StringUtils.hasText(orders.getPax())){
			sbf.append(",pax = '").append(orders.getPax()).append("' ");
		}
		//订单总金额
		if(StringUtils.hasText(orders.getSumprice())){
			sbf.append(",sumprice = ").append(orders.getSumprice());
		}
		
		if(!StringUtils.hasText(sbf)){
			return "0";//没有要更新的数据
		}else{
			//格式化时间
			if("oracle".equals(Commons.databaseType)){
				sbf.append(",dat=to_date('"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")+"','yyyy-mm-dd')");
			}else if("sqlserver".equals(Commons.databaseType)){
				sbf.append(",dat=cast('"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")+"' as datetime)");
			}else{
				sbf.append(",dat=DATE_FORMA('"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd")+"','%Y-%m-%d')");
			}
			sbf = new StringBuffer(updateOrderSql+sbf.substring(1));
		}
		//-----------------------where 条件--------------------------
		if(StringUtils.hasText(orders.getId())){
			sbf.append(" where id = '").append(orders.getId()).append("' ");
		}else{
			return "-1";//主键不能为空
		}
		if(StringUtils.hasText(orders.getPk_group())){
			sbf.append(" and pk_group = '").append(orders.getPk_group()).append("' ");
		}

		Connection conn = null;
		Statement st = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			return st.executeUpdate(sbf.toString()) + "";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	/**
	 * 获取菜品必选附加项列表
	 * @param pk_group
	 * @param orderId
	 * @return
	 */
	public List<NetDishAddItem> getDishAddItemList(String pk_group, String orderId) {
		List<NetDishAddItem> netDishAddItemList = new ArrayList<NetDishAddItem>();
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT D.PK_DISHADDITEM, D.PK_GROUP, D.PK_ORDERSID, D.PK_ORDERDTLID, D.PK_PUBITEM, ")
		.append("D.PK_REDEFINE, D.PK_PRODCUTREQATTAC, D.NCOUNT, D.NPRICE, D.PK_BRAND, R.VNAME AS REDEFINENAME,R.VCODE AS FCODE ")
		.append("FROM NET_DISHADDITEM D, CBOH_REDEFINE_3CH R ")
		.append("WHERE D.PK_REDEFINE = R.PK_REDEFINE ")
		.append("AND R.ENABLESTATE = 2 ");

		if (StringUtils.hasText(pk_group)) {
			sb.append(" AND D.PK_GROUP = '").append(pk_group).append("' ");
		}

		if (StringUtils.hasText(orderId)) {
			sb.append(" AND D.PK_ORDERSID = '").append(orderId).append("' ");
		}
		
		sb.append("ORDER BY R.ISORTNO");
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			
			rs = st.executeQuery(sb.toString());
			while(rs.next()) {
				NetDishAddItem d = new NetDishAddItem();
				d.setPk_dishAddItem(rs.getString("pk_dishAddItem"));
				d.setPk_group(rs.getString("pk_group"));
				d.setPk_ordersId(rs.getString("pk_ordersId"));
				d.setPk_orderDtlId(rs.getString("pk_orderDtlId"));
				d.setPk_prodcutReqAttAc(rs.getString("pk_prodcutReqAttAc"));
				d.setPk_pubItem(rs.getString("pk_pubItem"));
				d.setPk_redefine(rs.getString("pk_redefine"));
				d.setRedefineName(rs.getString("redefineName"));
				d.setNcount(rs.getString("ncount"));
				d.setNprice(rs.getString("nprice"));
				d.setFcode(rs.getString("fcode"));
				
				netDishAddItemList.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return netDishAddItemList;
	}
	
	/**
	 * 获取菜品附加产品列表
	 * @param pk_group
	 * @param orderId
	 * @return
	 */
	public List<NetDishProdAdd> getDishProdAddList(String pk_group, String orderId) {
		List<NetDishProdAdd> listNetDishProdAdd = new ArrayList<NetDishProdAdd>();
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT D.PK_DISHPRODADD, D.PK_GROUP, D.PK_ORDERSID, D.PK_ORDERDTLID, D.PK_PUBITEM, D.UNIT, ")
		.append("D.PK_PRODADD, D.PK_PRODREQADD, D.NCOUNT, D.NPRICE, D.PK_BRAND, P.VNAME AS PRODADDNAME, P.VCODE AS FCODE ")
		.append("FROM NET_DISHPRODADD D, CBOH_PUBITEM_3CH P, CBOH_PRODUCTADDITIONAL_3CH A ")
		.append("WHERE D.PK_PRODADD = A.PK_PRODADD ")
		.append("AND A.PK_PRODREQADD = P.PK_PUBITEM ")
		.append("AND P.ENABLESTATE = 2 ");

		if (StringUtils.hasText(pk_group)) {
			sb.append(" AND D.PK_GROUP = '").append(pk_group).append("' ");
		}

		if (StringUtils.hasText(orderId)) {
			sb.append(" AND D.PK_ORDERSID = '").append(orderId).append("' ");
		}
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			
			rs = st.executeQuery(sb.toString());
			while(rs.next()) {
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
				d.setUnit(rs.getString("UNIT"));
				listNetDishProdAdd.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return listNetDishProdAdd;
	}

	/**
	 * 将本次发送的mq信息流水号存储下来
	 * @author ZGL
	 * @date 2015-04-24 10:27:09
	 */
	public void addMqLogs(Map<String, Object> mqMap) {
		
		Connection conn = null;
		Statement st = null;
		int i=0;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			//查询该订单、该type类型是不是已经有记录了，有的话就直接返回不走新增记录方法
			String selctMqlogsSql  = "SELECT PK_MQLOGS,ORDERID,VTYPE,ERRMSG,STATE FROM WX_MQLOGS WHERE VTYPE='"+mqMap.get("vtype")+"' AND ORDERID='"+mqMap.get("orderid")+"'";
			ResultSet rs = st.executeQuery(selctMqlogsSql);
			if(rs.next()){//有的话就直接返回不走新增记录，修改map中的主键
				mqMap.put("pk_mqlogs",rs.getString("PK_MQLOGS"));
				//重置消息方式状态为未发送
				String updateMqlogs = "UPDATE WX_MQLOGS SET STATE = '',VSENDMSG='N' WHERE PK_MQLOGS = '"+rs.getString("PK_MQLOGS")+"'";
				st.executeUpdate(updateMqlogs);
			}else{//没有的话就新增记录
				StringBuilder sb = new StringBuilder("INSERT INTO WX_MQLOGS(PK_MQLOGS,ORDERID,VTYPE,ERRMSG,STATE) values('")
				.append(mqMap.get("pk_mqlogs")).append("','")
				.append(mqMap.get("orderid")).append("','")
				.append(mqMap.get("vtype")).append("','")
				.append(mqMap.get("errmsg")).append("','")
				.append(mqMap.get("state")).append("')");
				i=st.executeUpdate(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 增加开台菜品
	 * @param order
	 */
	public void addOpenItem(Net_Orders order) {
		List<Net_OrderDtl> dtlInfo = new ArrayList<Net_OrderDtl>();
		// 查询开台菜品
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT I.PK_PUBITEM, I.VNAME, I.NPRICE, I.VITCODE ")
		.append("FROM CBOH_SITEDEFINE_3CH S, CBOH_ITEMPRGD_3CH I ")
		.append("WHERE S.PK_PUBITEM = I.PK_PUBITEM ")
		.append("AND S.PK_STOREID = '")
		.append(order.getFirmid())
		.append("' AND S.VCODE = '")
		.append(order.getTables())
		.append("' AND I.PK_ITEMPRGM = (SELECT PK_ITEMPRGM ")
		.append("FROM (SELECT M.PK_ITEMPRGM ")
		.append("FROM CBOH_ITEMPRGM_3CH M, ")
		.append("CBOH_ITEMPRGFIRM_3CH FIRM ")
		.append("WHERE M.PK_ITEMPRGM = FIRM.PK_ITEMPRGM ")
		.append("AND M.ENABLESTATE = 2 ")
		.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') ")
		.append("BETWEEN TO_DATE(M.DBDAT, 'YYYY-MM-DD') AND TO_DATE(M.DEDAT, 'YYYY-MM-DD') ")
		.append("AND FIRM.PK_STORE = '")
		.append(order.getFirmid())
		.append("' ORDER BY M.VLEV DESC) ")
		.append("WHERE ROWNUM = 1) ");
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			
			rs = st.executeQuery(sb.toString());
			while(rs.next()) {
				Net_OrderDtl f = new Net_OrderDtl();
				f.setId(CodeHelper.createUUID());
				f.setFoodsid(rs.getString("PK_PUBITEM"));
				f.setFoodsname(rs.getString("VNAME"));
				f.setPrice(rs.getString("NPRICE"));
				f.setPcode(rs.getString("VITCODE"));
				f.setIspackage("0");
				f.setRemark("");
				
				dtlInfo.add(f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!dtlInfo.isEmpty()) {
			Net_OrderDtl addItem = dtlInfo.get(0);
			addItem.setOrdersid(order.getId());
			addItem.setFoodnum(order.getPax());
			addItem.setTotalprice(String.valueOf(Integer.parseInt(order.getPax()) * Double.parseDouble(addItem.getPrice())));
			
			order.getListNetOrderDtl().add(addItem);
		}
	}

	/**
	 * 保存订单明细
	 * @param order
	 * @param cnt
	 * @return
	 */
	public int saveOrderDtl(Net_Orders order) {
		int cnt = -1;
		Connection conn = null;
		Statement st = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			
			for (Net_OrderDtl ordersDtl : order.getListNetOrderDtl()) {
				String dtlSql = "INSERT INTO net_orderdetail "
						+ "(ID, foodsid, ordersid, remark, foodnum, totalprice, foodsname, ispackage) "
						+ "VALUES ('" + ordersDtl.getId() + "','"
						+ ordersDtl.getFoodsid() + "','"
						+ ordersDtl.getOrdersid() + "','"
						+ ordersDtl.getRemark() + "'," + ordersDtl.getFoodnum()
						+ "," + ordersDtl.getTotalprice() + ",'"
						+ ordersDtl.getFoodsname() + "','"
						+ ordersDtl.getIspackage() + "')";
				cnt = st.executeUpdate(dtlSql.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cnt;
	}
	
	/**
	 * 获取菜品门店编码
	 * @param firmid
	 * @return
	 */
	public List<FdItemSale> getListPubItemCode(String firmid) {
		StringBuilder sb = new StringBuilder("");

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<FdItemSale> listPubItemCode = new ArrayList<FdItemSale>();
		Map<String,FdItemSale> mapPubitem = new HashMap<String,FdItemSale>();
		String pk_itemprgm = "",creationtime="",vfoodsign="";
		
		//查询门店信息
		sb.append("SELECT PK_STORE,VCODE,VNAME,VFOODSIGN FROM CBOH_STORE_3CH WHERE PK_STORE = '"+firmid+"'");

		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			//查询全新菜谱方案
			rs = st.executeQuery(sb.toString());
			while(rs.next()){
				vfoodsign = rs.getString("VFOODSIGN");
				break;
			}
			if(ValueCheck.IsEmpty(vfoodsign)){
				return null;
			}
			sb = new StringBuilder();
			if("1".equals(vfoodsign)){//如果是choice3  1-choice3，2-choice7，3-choice8
				//中餐处理逻辑
				//获取全新菜谱方案最新的一个
				sb.append("SELECT M.PK_ITEMPRGM, M.VCODE, M.VNAME, M.CREATIONTIME ")
				.append("  FROM CBOH_ITEMPRGM_3CH M,CBOH_ITEMPRGFIRM_3CH F")
				.append(" WHERE M.PK_ITEMPRGM = F.PK_ITEMPRGM AND M.VITEMTYP = 1 AND M.ENABLESTATE != 1");
				if (StringUtils.hasText(firmid)) {
					sb.append(" AND F.PK_STORE = '")
					.append(firmid)
					.append("' ");
				}
				sb.append(" ORDER BY CREATIONTIME DESC");
				//查询全新菜谱方案
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					pk_itemprgm = rs.getString("PK_ITEMPRGM");
					creationtime = rs.getString("CREATIONTIME");
					break;
				}
				if(ValueCheck.IsEmpty(pk_itemprgm)){
					return null;
				}

				//查询全新方案明细
				sb = new StringBuilder();
				sb.append("SELECT D.PK_PUBITEM, D.VITCODE AS VCODE,D.VNAME,MD.NAME AS UNIT,MD.CODE AS UNITCODE ")
				.append("FROM CBOH_ITEMPRGD_3CH D,CBOH_PUBITEM_3CH PB,BD_MEASDOC MD ")
				.append("WHERE D.PK_PUBITEM = PB.PK_PUBITEM AND PB.VUNIT = MD.PK_MEASDOC ")
				.append("AND D.PK_ITEMPRGM = '"+pk_itemprgm+"' ");
				//查询全新方案的菜品明细
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					FdItemSale item = new FdItemSale();
					item.setId(rs.getString("PK_PUBITEM"));
					item.setItcode(rs.getString("VCODE"));
					item.setDes(rs.getString("VNAME"));
					item.setUnitcode(rs.getString("UNITCODE"));
					item.setUnit(rs.getString("UNIT"));
					
					mapPubitem.put(rs.getString("PK_PUBITEM"), item);
				}
			}else{//如果是choice7或者choice8

				sb = new StringBuilder();
				sb.append("SELECT C.PK_PUBITEM, C.PK_BRAND, C.VCODE,D.VNAME,GM.VLEV ")
				.append("FROM CBOH_PUBITEMCODE_3CH C, CBOH_ITEMPRGD_3CH D ,CBOH_ITEMPRGM_3CH GM ")
				.append("WHERE C.PK_PUBITEM = D.PK_PUBITEM AND D.PK_ITEMPRGM=GM.PK_ITEMPRGM ")
				.append("AND (D.PK_ITEMPRGM, C.PK_BRAND) = (SELECT PK_ITEMPRGM, PK_BRAND ")
				.append("FROM (SELECT M.PK_ITEMPRGM, M.PK_BRAND ")
				.append("FROM CBOH_ITEMPRGM_3CH M, ")
				.append("CBOH_ITEMPRGFIRM_3CH FIRM ")
				.append("WHERE M.PK_ITEMPRGM = FIRM.PK_ITEMPRGM ")
				.append("AND M.ENABLESTATE = 2 AND M.VITEMTYP = 1")
				.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') ")
				.append("BETWEEN TO_DATE(M.DBDAT, 'YYYY-MM-DD') AND TO_DATE(M.DEDAT, 'YYYY-MM-DD') ");
				
				if (StringUtils.hasText(firmid)) {
					sb.append("AND FIRM.PK_STORE = '")
					.append(firmid)
					.append("' ");
				}
				sb.append("ORDER BY M.VLEV DESC) ")
						.append("WHERE ROWNUM = 1) ");
			
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					FdItemSale item = new FdItemSale();
					item.setId(rs.getString("PK_PUBITEM"));
					item.setPk_group(rs.getString("PK_BRAND"));
					item.setItcode(rs.getString("VCODE"));
					item.setDes(rs.getString("VNAME"));
					item.setVlev(rs.getString("VLEV"));
//					listPubItemCode.add(item);
					mapPubitem.put(rs.getString("PK_PUBITEM"), item);
				}
			}
			
			//查询全部的追加方案sql
			sb = new StringBuilder();
			sb.append("SELECT M.PK_ITEMPRGM, M.VCODE, M.VNAME, M.CREATIONTIME,M.VLEV ")
			.append("  FROM CBOH_ITEMPRGM_3CH M,CBOH_ITEMPRGFIRM_3CH F");
			if("1".equals(vfoodsign)){//如果是choice3
				sb.append(" WHERE M.PK_ITEMPRGM = F.PK_ITEMPRGM AND M.VITEMTYP = 2 AND M.ENABLESTATE != 1");
			}else{//如果是choice7或者choice8
				sb.append(" WHERE M.PK_ITEMPRGM = F.PK_ITEMPRGM AND M.VITEMTYP = 2 AND M.ENABLESTATE =2");
			}
			if (StringUtils.hasText(firmid)) {
				sb.append(" AND F.PK_STORE = '")
				.append(firmid)
				.append("' ");
			}
			if (StringUtils.hasText(creationtime)) {
				sb.append(" AND M.CREATIONTIME >= '")
				.append(creationtime)
				.append("' ");
			}
			sb.append(" ORDER BY CREATIONTIME ASC");
			//查询全部的追加方案
			rs = st.executeQuery(sb.toString());
			while(rs.next()){

				//查询的追加方案明细sql
				sb = new StringBuilder();
				sb.append("SELECT D.PK_PUBITEM, D.VITCODE AS VCODE,D.VNAME,MD.NAME AS UNIT,MD.CODE AS UNITCODE,M.VLEV ")
				.append("FROM CBOH_ITEMPRGM_3CH M,CBOH_ITEMPRGD_3CH D,CBOH_PUBITEM_3CH PB,BD_MEASDOC MD ")
				.append("WHERE M.PK_ITEMPRGM=D.PK_ITEMPRGM AND D.PK_PUBITEM = PB.PK_PUBITEM AND PB.VUNIT = MD.PK_MEASDOC ")
				.append("AND D.PK_ITEMPRGM = '"+rs.getString("PK_ITEMPRGM")+"' ");
				//查询追加方案的菜品明细
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					FdItemSale item = new FdItemSale();
					item.setId(rs.getString("PK_PUBITEM"));
					item.setItcode(rs.getString("VCODE"));
					item.setDes(rs.getString("VNAME"));
					item.setUnitcode(rs.getString("UNITCODE"));
					item.setUnit(rs.getString("UNIT"));
					item.setVlev(rs.getString("VLEV"));
					
					if("1".equals(vfoodsign)){//如果是choice3
						mapPubitem.put(rs.getString("PK_PUBITEM"), item);
					}else{//如果是choice7或者choice8
						//判断当前方案的优先级是否比已经存在的菜品所属菜谱方案的优先级高，高就更新
						if(ValueCheck.IsNotEmpty(mapPubitem.get(rs.getString("PK_PUBITEM")))){//如果该菜品已经存在
							FdItemSale fdItemSale = mapPubitem.get(rs.getString("PK_PUBITEM"));
							if(null != rs.getString("VLEV") && rs.getString("VLEV").compareTo(fdItemSale.getVlev())>=0){//判断优先级，如果当前优先级高就更新数据
								mapPubitem.put(rs.getString("PK_PUBITEM"), item);
							}
						}else{//不存在就直接放到变量中
							mapPubitem.put(rs.getString("PK_PUBITEM"), item);
						}
					}
				}
			}
			//处理为list数据
			if(mapPubitem != null &&  !mapPubitem.isEmpty()){
				for (Entry<String, FdItemSale> entry : mapPubitem.entrySet()) {
					listPubItemCode.add(entry.getValue());
				  }
			}
			
			// 从门店个性列表获取菜品门店编码
			sb = new StringBuilder("");
			sb.append("SELECT S.PK_PUBITEM AS PK_PUBITEM, S.VITCODE AS ITCODE ")
			.append("FROM CBOH_STORE_ITEMPRGD_3CH S ")
			.append("WHERE S.PK_STORE = '")
			.append(firmid)
			.append("' ")
			.append("AND S.ENABLESTATE = 2");
			
			try {
				//连接数据库
				conn = new JdbcConnection().getOSPConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					FdItemSale item = new FdItemSale();
					item.setId(rs.getString("PK_PUBITEM"));
					item.setPk_group("");
					item.setItcode(rs.getString("ITCODE"));
					
					listPubItemCode.add(item);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != st){
						st.close();
					}
					if(null !=conn){
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return listPubItemCode;
	}
	/**
	 * 查询订单菜品套餐明细
	 */
	@Override
	public List<Map<String, Object>> listFolioPacageDtl(Net_OrderDtl dtl) {
		try{
			StringBuilder sql = new StringBuilder();
			sql.append("select pk_orderpackagedetail,pk_pubitem,ncnt as tcfoodnum,nzcnt,nprice as tcprice,vremark as tcremark,unit")
				.append(" from net_orderpackagedetail ")
				.append(" where pk_orderid = '"+dtl.getOrdersid()+"' and pk_orderdetail = '"+dtl.getId()+"'");
			
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			List<Map<String, Object>> listFolioPacageDtl = new ArrayList<Map<String, Object>>();
			try {
				//连接数据库
				conn = new JdbcConnection().getOSPConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql.toString());
				while(rs.next()){
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("pk_orderpackagedetail",rs.getString("pk_orderpackagedetail"));
					item.put("pk_pubitem",rs.getString("pk_pubitem"));
					item.put("tcfoodnum",rs.getString("tcfoodnum"));
					item.put("nzcnt",rs.getString("nzcnt"));
					item.put("tcprice",rs.getString("tcprice"));
					item.put("tcremark",rs.getString("tcremark"));
					item.put("unit", rs.getString("unit"));
					listFolioPacageDtl.add(item);
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != st){
						st.close();
					}
					if(null !=conn){
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return listFolioPacageDtl;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取套餐门店编码
	 */
	@Override
	public List<FdItemSale> getListPackageCode(String firmid) {
		StringBuilder sb = new StringBuilder("");

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<FdItemSale> listPubItemCode = new ArrayList<FdItemSale>();
		Map<String,FdItemSale> mapPubitem = new HashMap<String,FdItemSale>();
		String pk_itemprgm = "",creationtime="",vfoodsign="";
		
		//查询门店信息
		sb.append("SELECT PK_STORE,VCODE,VNAME,VFOODSIGN FROM CBOH_STORE_3CH WHERE PK_STORE = '"+firmid+"'");

		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			//查询全新菜谱方案
			rs = st.executeQuery(sb.toString());
			while(rs.next()){
				vfoodsign = rs.getString("VFOODSIGN");
				break;
			}
			if(ValueCheck.IsEmpty(vfoodsign)){
				return null;
			}
			sb = new StringBuilder();
			if("1".equals(vfoodsign)){//如果是choice3  1-choice3，2-choice7，3-choice8
				//中餐处理逻辑
				//获取全新菜谱方案最新的一个
				sb.append("SELECT M.PK_ITEMPRGM, M.VCODE, M.VNAME, M.CREATIONTIME ")
				.append("  FROM CBOH_ITEMPRGM_3CH M,CBOH_ITEMPRGFIRM_3CH F")
				.append(" WHERE M.PK_ITEMPRGM = F.PK_ITEMPRGM AND M.VITEMTYP = 1 AND M.ENABLESTATE != 1");
				if (StringUtils.hasText(firmid)) {
					sb.append(" AND F.PK_STORE = '")
					.append(firmid)
					.append("' ");
				}
				sb.append(" ORDER BY CREATIONTIME DESC");
				//查询全新菜谱方案
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					pk_itemprgm = rs.getString("PK_ITEMPRGM");
					creationtime = rs.getString("CREATIONTIME");
					break;
				}
				if(ValueCheck.IsEmpty(pk_itemprgm)){
					return null;
				}

				//查询全新方案明细
				sb = new StringBuilder();
				sb.append("SELECT D.PK_PUBPACK, D.VCODE,D.VNAME,MD.NAME AS UNIT,MD.CODE AS UNITCODE ")
				.append("FROM CBOH_ITEMPRGPACKAGE_3CH D,CBOH_PUBPACK_3CH PB,BD_MEASDOC MD ")
				.append("WHERE D.PK_PUBPACK = PB.PK_PUBPACK AND PB.VSALEUNIT = MD.PK_MEASDOC ")
				.append("AND D.PK_ITEMPRGM = '"+pk_itemprgm+"' ");
				//查询全新方案的菜品明细
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					FdItemSale item = new FdItemSale();
					item.setId(rs.getString("PK_PUBPACK"));
					item.setItcode(rs.getString("VCODE"));
					item.setDes(rs.getString("VNAME"));
					item.setUnitcode(rs.getString("UNITCODE"));
					item.setUnit(rs.getString("UNIT"));

					mapPubitem.put(rs.getString("PK_PUBPACK"), item);
				}
			}else{//如果是choice7或者choice8

				sb = new StringBuilder();
				sb.append("SELECT C.PK_PUBPACK, C.PK_BRAND, C.VCODE,D.VNAME ")
				.append("FROM CBOH_PUBPACKCODE_3CH C, CBOH_ITEMPRGPACKAGE_3CH D ")
				.append("WHERE C.PK_PUBPACK = D.PK_PUBPACK ")
				.append("AND (D.PK_ITEMPRGM, C.PK_BRAND) = (SELECT PK_ITEMPRGM, PK_BRAND ")
				.append("FROM (SELECT M.PK_ITEMPRGM, M.PK_BRAND ")
				.append("FROM CBOH_ITEMPRGM_3CH M, ")
				.append("CBOH_ITEMPRGFIRM_3CH FIRM ")
				.append("WHERE M.PK_ITEMPRGM = FIRM.PK_ITEMPRGM ")
				.append("AND M.ENABLESTATE = 2 ")
				.append("AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') ")
				.append("BETWEEN TO_DATE(M.DBDAT, 'YYYY-MM-DD') AND TO_DATE(M.DEDAT, 'YYYY-MM-DD') ");
				
				if (StringUtils.hasText(firmid)) {
					sb.append("AND FIRM.PK_STORE = '")
					.append(firmid)
					.append("' ");
				}
				sb.append("ORDER BY M.VLEV DESC) ")
						.append("WHERE ROWNUM = 1) ");
				
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					FdItemSale item = new FdItemSale();
					item.setId(rs.getString("PK_PUBPACK"));
					item.setPk_group(rs.getString("PK_BRAND"));
					item.setItcode(rs.getString("VCODE"));
					item.setDes(rs.getString("VNAME"));
					
					mapPubitem.put(rs.getString("PK_PUBPACK"), item);
				}
				
				//查询门店菜谱方案
				sb = new StringBuilder();
				sb.append("SELECT P.PK_PUBPACK, C.PK_BRAND, C.VCODE, P.VNAME ")
				.append("FROM CBOH_PUBPACKCODE_3CH C, CBOH_STORE_ITEMPRGPACKAGE_3CH P ")
				.append("WHERE C.PK_PUBPACK = P.PK_PUBPACK ")
				.append("AND P.PK_STORE = '")
				.append(firmid)
				.append("'");
				
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					FdItemSale item = new FdItemSale();
					item.setId(rs.getString("PK_PUBPACK"));
					item.setPk_group(rs.getString("PK_BRAND"));
					item.setItcode(rs.getString("VCODE"));
					item.setDes(rs.getString("VNAME"));
					
					mapPubitem.put(rs.getString("PK_PUBPACK"), item);
				}
			}

			//查询全部的追加方案sql
			sb = new StringBuilder();
			sb.append("SELECT M.PK_ITEMPRGM, M.VCODE, M.VNAME, M.CREATIONTIME,M.VLEV ")
			.append("  FROM CBOH_ITEMPRGM_3CH M,CBOH_ITEMPRGFIRM_3CH F");
			if("1".equals(vfoodsign)){//如果是choice3
				sb.append(" WHERE M.PK_ITEMPRGM = F.PK_ITEMPRGM AND M.VITEMTYP = 2 AND M.ENABLESTATE != 1");
			}else{//如果是choice7或者choice8
				sb.append(" WHERE M.PK_ITEMPRGM = F.PK_ITEMPRGM AND M.VITEMTYP = 2 AND M.ENABLESTATE =2");
			}
			if (StringUtils.hasText(firmid)) {
				sb.append(" AND F.PK_STORE = '")
				.append(firmid)
				.append("' ");
			}
			if (StringUtils.hasText(creationtime)) {
				sb.append(" AND M.CREATIONTIME >= '")
				.append(creationtime)
				.append("' ");
			}
			sb.append(" ORDER BY CREATIONTIME ASC");
			//查询全部的追加方案
			rs = st.executeQuery(sb.toString());
			while(rs.next()){

				//查询的追加方案明细sql
				sb = new StringBuilder();
				sb.append("SELECT D.PK_PUBPACK, D.VCODE,D.VNAME,MD.NAME AS UNIT,MD.CODE AS UNITCODE ")
				.append("FROM CBOH_ITEMPRGPACKAGE_3CH D,CBOH_PUBPACK_3CH PB,BD_MEASDOC MD ")
				.append("WHERE D.PK_PUBPACK = PB.PK_PUBPACK AND PB.VSALEUNIT = MD.PK_MEASDOC ")
				.append("AND D.PK_ITEMPRGM = '"+rs.getString("PK_ITEMPRGM")+"' ");
				//查询追加方案的菜品明细
				rs = st.executeQuery(sb.toString());
				while(rs.next()){
					FdItemSale item = new FdItemSale();
					item.setId(rs.getString("PK_PUBPACK"));
					item.setItcode(rs.getString("VCODE"));
					item.setDes(rs.getString("VNAME"));
					item.setUnitcode(rs.getString("UNITCODE"));
					item.setUnit(rs.getString("UNIT"));
					item.setVlev("0");
					if("1".equals(vfoodsign)){//如果是choice3
						mapPubitem.put(rs.getString("PK_PUBPACK"), item);
					}else{//如果是choice7或者choice8
						//判断当前方案的优先级是否比已经存在的菜品所属菜谱方案的优先级高，高就更新
						if(ValueCheck.IsNotEmpty(mapPubitem.get(rs.getString("PK_PUBPACK")))){//如果该菜品已经存在
							FdItemSale fdItemSale = mapPubitem.get(rs.getString("PK_PUBPACK"));
							if(rs.getString("VLEV").compareTo(fdItemSale.getVlev())>=0){//判断优先级，如果当前优先级高就更新数据
								mapPubitem.put(rs.getString("PK_PUBPACK"), item);
							}
						}else{//不存在就直接放到变量中
							mapPubitem.put(rs.getString("PK_PUBPACK"), item);
						}
					}
				}
			}
			//处理为list数据
			if(mapPubitem != null &&  !mapPubitem.isEmpty()){
				for (Entry<String, FdItemSale> entry : mapPubitem.entrySet()) {
					listPubItemCode.add(entry.getValue());
				  }
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return listPubItemCode;
	}
	/**
	 * 查询门店台位信息
	 */
	@Override
	public List<Map<String, Object>> getStoreSeatInfo(String firmId,String tableid) {
		String sql = "select pk_sited,vcode,vname from cboh_sitedefine_3ch where pk_storeid='"+firmId+"' and vcode = '"+tableid+"' ";

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Map<String, Object>> listStoreSeatInfo = new ArrayList<Map<String, Object>>();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String, Object> mapSecond = new HashMap<String, Object>();
				mapSecond.put("pk_sited",rs.getString("pk_sited"));
				mapSecond.put("vcode",rs.getString("vcode"));
				mapSecond.put("vname",rs.getString("vname"));
				
				listStoreSeatInfo.add(mapSecond);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return listStoreSeatInfo;
	}
	 /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
	 * @return 
     */
    public static Map<String, Object> readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        Map<String,Object> returnMap = new HashMap<String,Object>();//二级
        List<Map<String,Object>> listFirst = new ArrayList<Map<String,Object>>();//一级
        Map<String,Object> mapSecond = new HashMap<String,Object>();//二级
        Map<String,Object> mapThird = new HashMap<String,Object>();//三级
        Map<String,Object> mapTemp = new HashMap<String,Object>();//临时map
        List<Map<String,Object>> listTemp = new ArrayList<Map<String,Object>>();//临时list
        try {
            reader = new BufferedReader(new FileReader(file));//以行为单位读取文件内容，一次读一整行,直到读入null为文件结束
            String tempString = null;
            String sheng = "";
            String shengName="",shiName="";
            while ((tempString = reader.readLine()) != null) {
                String[] ss = tempString.split("\t");//分割行内容
                int i=0;
                for(String s : ss){//处理分割后的内容
                    if(ValueCheck.IsNotEmpty(s)){
                    	if(i==0){
                        	if(!s.equals(sheng)){//处理一级
                    			listTemp = new ArrayList<Map<String,Object>>();
                    			mapTemp = new HashMap<String,Object>();
                    			mapTemp.put("name", s);
                    			listFirst.add(mapTemp);
                        		sheng = s;
                        	}
                    		if(mapSecond.get(s)!=null && mapSecond.get(s)!=""){
                    		}else{
                    			listTemp = new ArrayList<Map<String,Object>>();
                    			mapSecond.put(s, listTemp);
                    			shengName = s;
                    		}
                    	}
                    	if(i==1 && !s.equals(shiName)){//处理二级
                			listTemp = (List<Map<String, Object>>) mapSecond.get(shengName);
                			if(listTemp == null || listTemp.isEmpty()){
               				 listTemp = new ArrayList<Map<String,Object>>();
               			}
                			mapTemp = new HashMap<String,Object>();
                			mapTemp.put("name", s);
                			listTemp.add(mapTemp);
                			mapSecond.put(shengName, listTemp);
                			shiName= s;
                    	}
                    	if(i==2){//处理三级
                			listTemp = (List<Map<String, Object>>) mapThird.get(shiName);
                			if(listTemp == null || listTemp.isEmpty()){
                				 listTemp = new ArrayList<Map<String,Object>>();
                			}
                			mapTemp = new HashMap<String,Object>();
                			mapTemp.put("name", s);
                			listTemp.add(mapTemp);
                			mapThird.put(shiName, listTemp);
                    	}
                    	i++;
                    }
                }
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        returnMap.put("listFirst", listFirst);
        returnMap.put("mapSecond", mapSecond);
        returnMap.put("mapThird", mapThird);
        return returnMap;
    }

	public static void main(String[] args) {
		Map<String,Object> map = readFileByLines("C:\\Users\\ZGL\\Desktop\\文件\\category.txt");
		System.out.println((List<Map<String,Object>>)map.get("listFirst"));
		System.out.println(map.get("mapSecond"));
		System.out.println(map.get("mapThird"));
	}
}
