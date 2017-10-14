package com.choice.wechat.persistence.campaign.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.choice.test.utils.JdbcConnection;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.campaign.ActmItem;
import com.choice.wechat.domain.game.Actm;
import com.choice.wechat.persistence.campaign.CampaignMapper;

@Repository
public class CampaignMapperImpl implements CampaignMapper {
	
	/**
	 * 查询外送活动
	 * @param firmid
	 * @return
	 */
	public List<Actm> listTakeOutActm(String firmid) {
		StringBuilder sb = new StringBuilder("");
		sb.append("select a.pk_actm, a.vcode, a.vname, a.bremit, a.bdiscount, a.idownamount, ")
		.append("a.nderatenum, a.ndiscountrate, a.bismdxz, a.dstartdate, a.denddate, a.badjust, a.jmrdo ")
		.append("from cboh_actm_3ch a ")
		.append("where a.enablestate = 2 ")
		.append("and a.vthirdactm = 2 ")
		.append("and (a.dstartdate is null or to_date(a.dstartdate || '00:00:00', 'yyyy/MM/dd hh24:mi:ss') <= sysdate) ")
		.append("and (a.denddate is null or to_date(a.denddate || '23:59:59', 'yyyy/MM/dd hh24:mi:ss') >= sysdate) ")
		.append("and (a.bismdxz = 'N' or a.pk_actm in (select s.pk_actm from cboh_actstr_3ch s where s.pk_store = '")
		.append(firmid)
		.append("')) ");

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Actm> listActm = new ArrayList<Actm>();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sb.toString());
			while(rs.next()){
				Actm actm = new Actm();
				actm.setPk_actm(rs.getString("pk_actm"));
				actm.setVcode(rs.getString("vcode"));
				actm.setVname(rs.getString("vname"));
				actm.setBremit(rs.getString("bremit"));
				actm.setBdiscount(rs.getString("bdiscount"));
				actm.setIdownamount(rs.getDouble("idownamount"));
				actm.setNderatenum(rs.getDouble("nderatenum"));
				actm.setNdiscountrate(rs.getDouble("ndiscountrate"));
				actm.setBismdxz(rs.getString("bismdxz"));
				actm.setStartdate(rs.getString("dstartdate"));
				actm.setEnddate(rs.getString("denddate"));
				actm.setBadjust(rs.getString("badjust"));
				actm.setJmrdo(rs.getString("jmrdo"));
				
				listActm.add(actm);
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
		return listActm;
	}
	
	/**
	 * 获取活动菜品明细 （账单减免和启用调价菜品）
	 * @param pk_actm
	 * @param actmCode
	 * @return
	 */
	public List<ActmItem> listActmItem(String pk_actm, String actmCode) {
		StringBuilder sb = new StringBuilder("");
		sb.append("select i.pk_pubitem, i.inum, i.pk_actm ")
		.append("from cboh_item_3ch i ")
		.append("left join cboh_actm_3ch a on i.pk_actm = a.pk_actm ")
		.append("where 1=1 ");
		
		if(StringUtils.hasText(pk_actm)) {
			sb.append("and i.pk_actm = '")
			.append(pk_actm)
			.append("'");
		}
		
		if(StringUtils.hasText(actmCode)) {
			sb.append("and a.vcode = '")
			.append(actmCode)
			.append("'");
		}

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<ActmItem> listActmItem = new ArrayList<ActmItem>();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sb.toString());
			while(rs.next()){
				ActmItem item = new ActmItem();
				item.setPk_actm(rs.getString("pk_actm"));
				item.setPk_pubitem(rs.getString("pk_pubitem"));
				item.setInum(rs.getInt("inum"));
				
				listActmItem.add(item);
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
		return listActmItem;
	}
	
	/**
	 * 根据菜品编码获取菜品
	 * @param code
	 * @return
	 */
	public List<FdItemSale> getItemByCode(String code) {
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT P.PK_PUBITEM, P.VNAME, M.NAME AS UNIT ")
		.append("FROM CBOH_PUBITEM_3CH P ")
		.append("LEFT JOIN BD_MEASDOC M ON P.VUNIT = M.PK_MEASDOC ")
		.append("WHERE P.VCODE = '")
		.append(code)
		.append("'");
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<FdItemSale> listFdItemSale = new ArrayList<FdItemSale>();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sb.toString());
			while(rs.next()){
				FdItemSale f = new FdItemSale();
				f.setId(rs.getString("PK_PUBITEM"));
				f.setDes(rs.getString("VNAME"));
				f.setUnit(rs.getString("UNIT"));
				
				listFdItemSale.add(f);
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
		
		return listFdItemSale;
	}
}
