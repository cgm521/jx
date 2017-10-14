package com.choice.test.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;

/**
 * 套餐点菜明细表
 * @author ZGL
 *
 */
public class Net_OrderPackageDetail  implements RowMapper<Net_OrderPackageDetail>{
	private String pk_orderpackagedetail;		//主键
	private String pk_orderid;							//账单表主键
	private String pk_orderdetail;						//菜品明细表主键
	private String pk_pubitem;							//菜品主键
	private String vpcode;									//菜品编码
	private String reqredefine;							//是否有必选附加项
	private String prodReqAddFlag;				//是否有必选附加产品
	private String vname;									//名称
	private Double ncnt;									//点菜数量
	private Double nzcnt;									//折扣数量
	private Double nprice;									//单价
	private String nprice1;									//单价1 标准价
	private String vremark;								//备注
	private String unit;										//备注
	private List<NetDishAddItem> listDishAddItem;//菜品附加项列表
	private List<NetDishProdAdd> listDishProdAdd;//菜品附加产品列表
	private String tcPubitemSeq;	//套餐内菜品顺序号
	private Map<String,List<NetDishAddItem>> mapDishAddItemMust;//菜品必选附加项列表
	private List<NetDishAddItem> listDishAddItemCanselect;//菜品可选附加项列表
	private Map<String,List<NetDishProdAdd>> mapDishProdAddMust;//菜品必选附加产品列表
	private List<NetDishProdAdd> listDishProdAddCanselect;//菜品可选附加产品列表
	private String netDishAddItemtitle;
	private String netDishProdAddtitle;
	
	public String getPk_orderpackagedetail() {
		return pk_orderpackagedetail;
	}
	public void setPk_orderpackagedetail(String pk_orderpackagedetail) {
		this.pk_orderpackagedetail = pk_orderpackagedetail;
	}
	public String getPk_orderid() {
		return pk_orderid;
	}
	public void setPk_orderid(String pk_orderid) {
		this.pk_orderid = pk_orderid;
	}
	public String getPk_orderdetail() {
		return pk_orderdetail;
	}
	public void setPk_orderdetail(String pk_orderdetail) {
		this.pk_orderdetail = pk_orderdetail;
	}
	public String getPk_pubitem() {
		return pk_pubitem;
	}
	public void setPk_pubitem(String pk_pubitem) {
		this.pk_pubitem = pk_pubitem;
	}
	public Double getNcnt() {
		return ncnt;
	}
	public void setNcnt(Double ncnt) {
		this.ncnt = ncnt;
	}
	public Double getNzcnt() {
		return nzcnt;
	}
	public void setNzcnt(Double nzcnt) {
		this.nzcnt = nzcnt;
	}
	public Double getNprice() {
		return nprice;
	}
	public void setNprice(Double nprice) {
		this.nprice = nprice;
	}
	public String getVremark() {
		return vremark;
	}
	public void setVremark(String vremark) {
		this.vremark = vremark;
	}
	public List<NetDishAddItem> getListDishAddItem() {
		return listDishAddItem;
	}
	public void setListDishAddItem(List<NetDishAddItem> listDishAddItem) {
		this.listDishAddItem = listDishAddItem;
	}
	public List<NetDishProdAdd> getListDishProdAdd() {
		return listDishProdAdd;
	}
	public void setListDishProdAdd(List<NetDishProdAdd> listDishProdAdd) {
		this.listDishProdAdd = listDishProdAdd;
	}
	@Override
	public Net_OrderPackageDetail mapRow(ResultSet rs, int i)	throws SQLException {
		Net_OrderPackageDetail d = new Net_OrderPackageDetail();
		d.setPk_orderpackagedetail(rs.getString("pk_orderpackagedetail"));
		d.setPk_orderid(rs.getString("pk_orderid"));
		d.setPk_orderdetail(rs.getString("pk_orderdetail"));
		d.setPk_pubitem(rs.getString("pk_pubitem"));
		d.setNcnt(rs.getDouble("ncnt"));
		d.setNzcnt(rs.getDouble("nzcnt"));
		d.setNprice(rs.getDouble("nprice"));
		d.setVremark(rs.getString("vremark"));
		return d;
	}
	public String getVpcode() {
		return vpcode;
	}
	public void setVpcode(String vpcode) {
		this.vpcode = vpcode;
	}
	public String getReqredefine() {
		return reqredefine;
	}
	public void setReqredefine(String reqredefine) {
		this.reqredefine = reqredefine;
	}
	public String getProdReqAddFlag() {
		return prodReqAddFlag;
	}
	public void setProdReqAddFlag(String prodReqAddFlag) {
		this.prodReqAddFlag = prodReqAddFlag;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getNprice1() {
		return nprice1;
	}
	public void setNprice1(String nprice1) {
		this.nprice1 = nprice1;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getTcPubitemSeq() {
		return tcPubitemSeq;
	}
	public void setTcPubitemSeq(String tcPubitemSeq) {
		this.tcPubitemSeq = tcPubitemSeq;
	}
	public List<NetDishAddItem> getListDishAddItemCanselect() {
		return listDishAddItemCanselect;
	}
	public void setListDishAddItemCanselect(
			List<NetDishAddItem> listDishAddItemCanselect) {
		this.listDishAddItemCanselect = listDishAddItemCanselect;
	}
	public List<NetDishProdAdd> getListDishProdAddCanselect() {
		return listDishProdAddCanselect;
	}
	public void setListDishProdAddCanselect(
			List<NetDishProdAdd> listDishProdAddCanselect) {
		this.listDishProdAddCanselect = listDishProdAddCanselect;
	}
	public String getNetDishAddItemtitle() {
		return netDishAddItemtitle;
	}
	public void setNetDishAddItemtitle(String netDishAddItemtitle) {
		this.netDishAddItemtitle = netDishAddItemtitle;
	}
	public String getNetDishProdAddtitle() {
		return netDishProdAddtitle;
	}
	public void setNetDishProdAddtitle(String netDishProdAddtitle) {
		this.netDishProdAddtitle = netDishProdAddtitle;
	}
	public Map<String, List<NetDishAddItem>> getMapDishAddItemMust() {
		return mapDishAddItemMust;
	}
	public void setMapDishAddItemMust(
			Map<String, List<NetDishAddItem>> mapDishAddItemMust) {
		this.mapDishAddItemMust = mapDishAddItemMust;
	}
	public Map<String, List<NetDishProdAdd>> getMapDishProdAddMust() {
		return mapDishProdAddMust;
	}
	public void setMapDishProdAddMust(
			Map<String, List<NetDishProdAdd>> mapDishProdAddMust) {
		this.mapDishProdAddMust = mapDishProdAddMust;
	}

}
