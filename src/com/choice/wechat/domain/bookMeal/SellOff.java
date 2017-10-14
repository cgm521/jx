package com.choice.wechat.domain.bookMeal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 菜品类别
 * @author 王恒军
 */
public class SellOff implements RowMapper<SellOff>{
	/**
	 * 企业编码
	 */
	private String pk_group;

	/**
	 * 主键
	 */
	private String pk_sellOff;

	/**
	 * 门店主键
	 */
	private String pk_store;

	/**
	 * 菜品主键
	 */
	private String pk_pubItem;

	/**
	 * 沽清标识，0：未沽清，1：临时沽清，2：永久沽清
	 */
	private String nflag;

	/**
	 * 剩余数量
	 */
	private String nremain;

	/**
	 * 开始日期
	 */
	private String dbeginDate;

	/**
	 * 结束日期
	 */
	private String dendDate;

	/**
	 * 操作时间
	 */
	private String doperateTime;

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getPk_sellOff() {
		return pk_sellOff;
	}

	public void setPk_sellOff(String pk_sellOff) {
		this.pk_sellOff = pk_sellOff;
	}

	public String getPk_store() {
		return pk_store;
	}

	public void setPk_store(String pk_store) {
		this.pk_store = pk_store;
	}

	public String getPk_pubItem() {
		return pk_pubItem;
	}

	public void setPk_pubItem(String pk_pubItem) {
		this.pk_pubItem = pk_pubItem;
	}

	public String getNflag() {
		return nflag;
	}

	public void setNflag(String nflag) {
		this.nflag = nflag;
	}

	public String getNremain() {
		return nremain;
	}

	public void setNremain(String nremain) {
		this.nremain = nremain;
	}

	public String getDbeginDate() {
		return dbeginDate;
	}

	public void setDbeginDate(String dbeginDate) {
		this.dbeginDate = dbeginDate;
	}

	public String getDendDate() {
		return dendDate;
	}

	public void setDendDate(String dendDate) {
		this.dendDate = dendDate;
	}

	public String getDoperateTime() {
		return doperateTime;
	}

	public void setDoperateTime(String doperateTime) {
		this.doperateTime = doperateTime;
	}

	public SellOff mapRow(ResultSet rs, int i) throws SQLException {
		SellOff f = new SellOff();
		f.setPk_group(rs.getString("pk_group"));
		f.setPk_sellOff(rs.getString("pk_sellOff"));
		f.setPk_store(rs.getString("pk_store"));
		f.setPk_pubItem(rs.getString("pk_pubItem"));
		f.setNflag(rs.getString("nflag"));
		f.setNremain(rs.getString("nremain"));
		f.setDbeginDate(rs.getString("dbeginDate"));
		f.setDendDate(rs.getString("dendDate"));
		f.setDoperateTime(rs.getString("doperateTime"));
		return f;
	}
}
