package com.choice.wechat.domain.bookMeal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 菜品必选附加产品
 * @author 王恒军
 */
public class ProdReqAdd implements RowMapper<ProdReqAdd>{
	
	/**
	 * 主键
	 */
	private String pk_prodReqAdd;
	
	/**
	 * 菜品主键
	 */
	private String pk_pubitem;
	
	/**
	 * 名称
	 */
	private String vdes;
	
	/**
	 * 最小数量
	 */
	private String minCount;
	
	/**
	 * 最大数量
	 */
	private String maxCount;
	
	/**
	 * 菜品附加产品列表
	 */
	private List<ProductAdditional> listProductAdditional;

	public String getPk_prodReqAdd() {
		return pk_prodReqAdd;
	}

	public void setPk_prodReqAdd(String pk_prodReqAdd) {
		this.pk_prodReqAdd = pk_prodReqAdd;
	}

	public String getPk_pubitem() {
		return pk_pubitem;
	}

	public void setPk_pubitem(String pk_pubitem) {
		this.pk_pubitem = pk_pubitem;
	}

	public String getVdes() {
		return vdes;
	}

	public void setVdes(String vdes) {
		this.vdes = vdes;
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

	public List<ProductAdditional> getListProductAdditional() {
		return listProductAdditional;
	}

	public void setListProductAdditional(
			List<ProductAdditional> listProductAdditional) {
		this.listProductAdditional = listProductAdditional;
	}

	public ProdReqAdd mapRow(ResultSet rs, int i) throws SQLException {
		ProdReqAdd d = new ProdReqAdd();
		d.setPk_prodReqAdd(rs.getString("pk_prodReqAdd"));
		d.setPk_pubitem(rs.getString("pk_pubitem"));
		d.setVdes(rs.getString("vdes"));
		d.setMinCount(rs.getString("minCount"));
		d.setMaxCount(rs.getString("maxCount"));
		return d;
	}
}
