package com.choice.wechat.domain.bookMeal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 菜品类别
 * @author 王恒军
 */
public class ProductReqAttAc implements RowMapper<ProductReqAttAc>{
	
	/**
	 * 菜品必选附加项主键
	 */
	private String pk_ProdcutReqAttAc;
	
	/**
	 * 菜品主键
	 */
	private String pk_PubItem;
	
	/**
	 * 最小数量
	 */
	private String minCount;
	
	/**
	 * 最大数量
	 */
	private String maxCount;
	
	/**
	 * 必选附加项名称
	 */
	private String vdes;
	
	/**
	 * 必选附加项明细列表
	 */
	private List<ProductRedfine> productRedfineList;

	public String getPk_ProdcutReqAttAc() {
		return pk_ProdcutReqAttAc;
	}

	public void setPk_ProdcutReqAttAc(String pk_ProdcutReqAttAc) {
		this.pk_ProdcutReqAttAc = pk_ProdcutReqAttAc;
	}

	public String getPk_PubItem() {
		return pk_PubItem;
	}

	public void setPk_PubItem(String pk_PubItem) {
		this.pk_PubItem = pk_PubItem;
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

	public String getVdes() {
		return vdes;
	}

	public void setVdes(String vdes) {
		this.vdes = vdes;
	}

	public List<ProductRedfine> getProductRedfineList() {
		return productRedfineList;
	}

	public void setProductRedfineList(List<ProductRedfine> productRedfineList) {
		this.productRedfineList = productRedfineList;
	}

	public ProductReqAttAc mapRow(ResultSet rs, int i) throws SQLException {
		ProductReqAttAc d = new ProductReqAttAc();
		d.setMaxCount(rs.getString("maxCount"));
		d.setMinCount(rs.getString("minCount"));
		d.setPk_ProdcutReqAttAc(rs.getString("pk_ProdcutReqAttAc"));
		d.setPk_PubItem(rs.getString("pk_PubItem"));
		d.setVdes(rs.getString("vdes"));
		return d;
	}
}
