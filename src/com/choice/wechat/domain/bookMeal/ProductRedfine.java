package com.choice.wechat.domain.bookMeal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 菜品附加项设置
 * @author 王恒军
 */
public class ProductRedfine implements RowMapper<ProductRedfine>{
	
	/**
	 * 菜品附加项主键
	 */
	private String pk_ProductRedfine;
	
	/**
	 * 菜品主键
	 */
	private String pk_PubItem;
	
	/**
	 * 附加项主键
	 */
	private String pk_Redefine;
	
	/**
	 * 菜品必选附加项主键
	 */
	private String pk_ProdcutReqAttAc;
	
	/**
	 * 最小数量
	 */
	private String minCount;
	
	/**
	 * 最大数量
	 */
	private String maxCount;
	
	/**
	 * 附加项编码
	 */
	private String vcode;
	
	/**
	 * 附加项名称
	 */
	private String vname;
	
	/**
	 * 会员价格
	 */
	private String namt;

	public String getPk_ProductRedfine() {
		return pk_ProductRedfine;
	}

	public void setPk_ProductRedfine(String pk_ProductRedfine) {
		this.pk_ProductRedfine = pk_ProductRedfine;
	}

	public String getPk_PubItem() {
		return pk_PubItem;
	}

	public void setPk_PubItem(String pk_PubItem) {
		this.pk_PubItem = pk_PubItem;
	}

	public String getPk_Redefine() {
		return pk_Redefine;
	}

	public void setPk_Redefine(String pk_Redefine) {
		this.pk_Redefine = pk_Redefine;
	}

	public String getPk_ProdcutReqAttAc() {
		return pk_ProdcutReqAttAc;
	}

	public void setPk_ProdcutReqAttAc(String pk_ProdcutReqAttAc) {
		this.pk_ProdcutReqAttAc = pk_ProdcutReqAttAc;
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

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getNamt() {
		return namt;
	}

	public void setNamt(String namt) {
		this.namt = namt;
	}

	public ProductRedfine mapRow(ResultSet rs, int i) throws SQLException {
		ProductRedfine d = new ProductRedfine();
		d.setMaxCount(rs.getString("maxCount"));
		d.setMinCount(rs.getString("minCount"));
		d.setNamt(rs.getString("namt"));
		d.setPk_ProductRedfine(rs.getString("pk_ProductRedfine"));
		d.setPk_ProdcutReqAttAc(rs.getString("pk_ProdcutReqAttAc"));
		d.setPk_PubItem(rs.getString("pk_PubItem"));
		d.setPk_Redefine(rs.getString("pk_Redefine"));
		d.setVcode(rs.getString("vcode"));
		d.setVname(rs.getString("vname"));
		return d;
	}
}
