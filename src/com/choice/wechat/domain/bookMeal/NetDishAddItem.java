package com.choice.wechat.domain.bookMeal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * 菜品附加项设置
 * @author 王恒军
 */
public class NetDishAddItem implements RowMapper<NetDishAddItem> {
	
	/**
	 * 菜品附加项主键
	 */
	private String pk_dishAddItem;
	
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
	private String pk_pubItem;
	
	/**
	 * 附加项主键
	 */
	private String pk_redefine;
	
	/**
	 * 附加项名称
	 */
	private String redefineName;
	
	/**
	 * 菜品必选附加项主键
	 */
	private String pk_prodcutReqAttAc;
	
	/**
	 * 数量
	 */
	private String ncount;
	
	/**
	 * 价格
	 */
	private String nprice;
	
	/**
	 * 附加项编码
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
	
	public String getPk_dishAddItem() {
		return pk_dishAddItem;
	}

	public void setPk_dishAddItem(String pk_dishAddItem) {
		this.pk_dishAddItem = pk_dishAddItem;
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

	public String getPk_pubItem() {
		return pk_pubItem;
	}

	public void setPk_pubItem(String pk_pubItem) {
		this.pk_pubItem = pk_pubItem;
	}

	public String getPk_redefine() {
		return pk_redefine;
	}

	public void setPk_redefine(String pk_redefine) {
		this.pk_redefine = pk_redefine;
	}

	public String getRedefineName() {
		return redefineName;
	}

	public void setRedefineName(String redefineName) {
		this.redefineName = redefineName;
	}

	public String getPk_prodcutReqAttAc() {
		return pk_prodcutReqAttAc;
	}

	public void setPk_prodcutReqAttAc(String pk_prodcutReqAttAc) {
		this.pk_prodcutReqAttAc = pk_prodcutReqAttAc;
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

	public NetDishAddItem mapRow(ResultSet rs, int i) throws SQLException {
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
		return d;
	}
}
