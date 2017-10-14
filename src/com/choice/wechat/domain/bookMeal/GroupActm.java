package com.choice.wechat.domain.bookMeal;
/**
 * 团购活动
 * @author King
 *
 */
public class GroupActm {
	private String pk_actm;//活动主键
	private String vcode;//编码
	private String vname;//活动名称
	private String couponcode;//验证来源
	private String couponname;//验证来源名称
	private String nDerateNum;//金额
	private String groupcode;//验证码
	public String getPk_actm() {
		return pk_actm;
	}
	public void setPk_actm(String pk_actm) {
		this.pk_actm = pk_actm;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public String getCouponcode() {
		return couponcode;
	}
	public void setCouponcode(String couponcode) {
		this.couponcode = couponcode;
	}
	public String getCouponname() {
		return couponname;
	}
	public void setCouponname(String couponname) {
		this.couponname = couponname;
	}
	public String getnDerateNum() {
		return nDerateNum;
	}
	public void setnDerateNum(String nDerateNum) {
		this.nDerateNum = nDerateNum;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getGroupcode() {
		return groupcode;
	}
	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}
}
