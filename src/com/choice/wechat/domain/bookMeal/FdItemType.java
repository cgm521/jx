package com.choice.wechat.domain.bookMeal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 菜品类别
 * @author 王恒军
 */
public class FdItemType implements RowMapper<FdItemType>{
	/**
	 * 企业编码
	 */
	private String pk_group;

	/**
	 * 菜品主键
	 */
	private String id;

	/**
	 * 菜品名称
	 */
	private String name;

	/**
	 * 菜品编码
	 */
	private Integer code;

	/**
	 * 门店ID
	 */
	private Integer firmid;

	/**
	 * 排序号
	 */
	private Integer typind;
	
	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getFirmid() {
		return firmid;
	}

	public void setFirmid(Integer firmid) {
		this.firmid = firmid;
	}

	public Integer getTypind() {
		return typind;
	}

	public void setTypind(Integer typind) {
		this.typind = typind;
	}

	public FdItemType mapRow(ResultSet rs, int i) throws SQLException {
		FdItemType f = new FdItemType();
		f.setPk_group(rs.getString("pk_group"));
		f.setId(rs.getString("id"));
		f.setName(rs.getString("name"));
		f.setCode(rs.getInt("code"));
		f.setFirmid(rs.getInt("firmid"));
		f.setTypind(rs.getInt("typind"));
		return f;
	}
}
