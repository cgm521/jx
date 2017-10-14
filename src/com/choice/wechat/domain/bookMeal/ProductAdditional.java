package com.choice.wechat.domain.bookMeal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 菜品附加产品
 * @author 王恒军
 */
public class ProductAdditional implements RowMapper<ProductAdditional>{
	
	/**
	 * 主键
	 */
	private String pk_prodAdd;
	
	/**
	 * 菜品主键
	 */
	private String pk_pubitem;
	
	/**
	 * 必选附加产品表主键
	 */
	private String pk_addPubitem;
	
	/**
	 * 附加产品主键，即菜品主键
	 */
	private String pk_prodReqAdd;
	
	/**
	 * 是否必须，0为否，1为是，默认0
	 */
	private String visMust;
	
	/**
	 * 最小数量
	 */
	private String minCount;
	
	/**
	 * 最大数量
	 */
	private String maxCount;
	
	/**
	 * 菜品名称
	 */
	private String pubitemName;
	
	/**
	 * 附加产品名称
	 */
	private String prodReqAddName;
	
	/**
	 * 附加产品价格
	 */
	private String price;
	
	/**
	 * 附加产品编码
	 */
	private String vcode;
	
	/**
	 * 菜谱方案优先级
	 */
	private String vlev;

	/**
	 * 单位
	 */
	private String unit;
	
	public String getPk_prodAdd() {
		return pk_prodAdd;
	}

	public void setPk_prodAdd(String pk_prodAdd) {
		this.pk_prodAdd = pk_prodAdd;
	}

	public String getPk_pubitem() {
		return pk_pubitem;
	}

	public void setPk_pubitem(String pk_pubitem) {
		this.pk_pubitem = pk_pubitem;
	}

	public String getPk_addPubitem() {
		return pk_addPubitem;
	}

	public void setPk_addPubitem(String pk_addPubitem) {
		this.pk_addPubitem = pk_addPubitem;
	}

	public String getPk_prodReqAdd() {
		return pk_prodReqAdd;
	}

	public void setPk_prodReqAdd(String pk_prodReqAdd) {
		this.pk_prodReqAdd = pk_prodReqAdd;
	}

	public String getVisMust() {
		return visMust;
	}

	public void setVisMust(String visMust) {
		this.visMust = visMust;
	}

	public String getMinCount() {
		return minCount;
	}

	public void setMinCount(String minCount) {
		this.minCount = minCount;
	}

	public String getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(String maxCount) {
		this.maxCount = maxCount;
	}

	public String getPubitemName() {
		return pubitemName;
	}

	public void setPubitemName(String pubitemName) {
		this.pubitemName = pubitemName;
	}

	public String getProdReqAddName() {
		return prodReqAddName;
	}

	public void setProdReqAddName(String prodReqAddName) {
		this.prodReqAddName = prodReqAddName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getVlev() {
		return vlev;
	}

	public void setVlev(String vlev) {
		this.vlev = vlev;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public ProductAdditional mapRow(ResultSet rs, int i) throws SQLException {
		ProductAdditional d = new ProductAdditional();
		d.setPk_addPubitem(rs.getString("pk_addPubitem"));
		d.setPk_prodAdd(rs.getString("pk_prodAdd"));
		d.setPk_prodReqAdd(rs.getString("pk_prodReqAdd"));
		d.setPk_pubitem(rs.getString("pk_pubitem"));
		d.setPubitemName(rs.getString("pubitemName"));
		d.setProdReqAddName(rs.getString("prodReqAddName"));
		d.setMaxCount(rs.getString("maxCount"));
		d.setMinCount(rs.getString("minCount"));
		return d;
	}
}
