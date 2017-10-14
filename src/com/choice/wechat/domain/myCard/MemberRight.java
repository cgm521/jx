package com.choice.wechat.domain.myCard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 优惠券
 * @author 王恒军
 */
public class MemberRight implements RowMapper<MemberRight>{
	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 企业编码
	 */
	private String pk_group;
	
	/**
	 * 会员卡类型
	 */
	private String cardType;
	
	/**
	 * 会员特权
	 */
	private String memberRight;
	
	/**
	 * 时间戳
	 */
	private String ts;
	
	/**
	 * 删除标志
	 */
	private int dr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getMemberRight() {
		return memberRight;
	}

	public void setMemberRight(String memberRight) {
		this.memberRight = memberRight;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}

	public MemberRight mapRow(ResultSet rs, int i) throws SQLException {
		MemberRight f = new MemberRight();
		f.setId(rs.getString("ID"));
		f.setPk_group(rs.getString("PK_GROUP"));
		f.setCardType(rs.getString("CARDTYPE"));
		f.setMemberRight(rs.getString("RIGHT"));
		f.setTs(rs.getString("TS"));
		f.setDr(rs.getInt("DR"));
		return f;
	}
}
