package com.choice.wechat.domain.bookMeal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * 菜品附加产品
 * @author 王恒军
 */
public class NetDishProdAdd implements RowMapper<NetDishProdAdd> {
	
	/**
	 * 菜品附加产品主键
	 */
	private String pk_dishProdAdd;
	
	/**
	 * 企业编号
	 */
	private String pk_group;
	
	/**
	 * 订单主键
	 */
	private String pk_ordersId;
	
	/**
	 * 订单明细主键
	 */
	private String pk_orderDtlId;
	
	/**
	 * 菜品主键
	 */
	private String pk_pubitem;
	
	/**
	 * 附加产品主键
	 */
	private String pk_prodAdd;
	
	/**
	 * 附加产品名称
	 */
	private String prodAddName;
	
	/**
	 * 菜品必选附加产品主键
	 */
	private String pk_prodReqAdd;
	
	/**
	 * 数量
	 */
	private String ncount;
	
	/**
	 * 价格
	 */
	private String nprice;
	
	/**
	 * 附加产品编码
	 * @return
	 */
	private String fcode;
	
	/**
	 * 顺序号
	 * @return
	 */
	private Integer seq;

	/**
	 * 单位
	 */
	private String unit;
	
	/**
	 * 附加产品的菜品主键
	 */
	private String selfPkPubitem;

	public String getPk_dishProdAdd() {
		return pk_dishProdAdd;
	}

	public void setPk_dishProdAdd(String pk_dishProdAdd) {
		this.pk_dishProdAdd = pk_dishProdAdd;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getPk_ordersId() {
		return pk_ordersId;
	}

	public void setPk_ordersId(String pk_ordersId) {
		this.pk_ordersId = pk_ordersId;
	}

	public String getPk_orderDtlId() {
		return pk_orderDtlId;
	}

	public void setPk_orderDtlId(String pk_orderDtlId) {
		this.pk_orderDtlId = pk_orderDtlId;
	}

	public String getPk_pubitem() {
		return pk_pubitem;
	}

	public void setPk_pubitem(String pk_pubitem) {
		this.pk_pubitem = pk_pubitem;
	}

	public String getPk_prodAdd() {
		return pk_prodAdd;
	}

	public void setPk_prodAdd(String pk_prodAdd) {
		this.pk_prodAdd = pk_prodAdd;
	}

	public String getProdAddName() {
		return prodAddName;
	}

	public void setProdAddName(String prodAddName) {
		this.prodAddName = prodAddName;
	}

	public String getPk_prodReqAdd() {
		return pk_prodReqAdd;
	}

	public void setPk_prodReqAdd(String pk_prodReqAdd) {
		this.pk_prodReqAdd = pk_prodReqAdd;
	}

	public String getNcount() {
		return ncount;
	}

	public void setNcount(String ncount) {
		this.ncount = ncount;
	}

	public String getNprice() {
		return nprice;
	}

	public void setNprice(String nprice) {
		this.nprice = nprice;
	}

	public String getFcode() {
		return fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSelfPkPubitem() {
		return selfPkPubitem;
	}

	public void setSelfPkPubitem(String selfPkPubitem) {
		this.selfPkPubitem = selfPkPubitem;
	}

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
		return d;
	}
}
