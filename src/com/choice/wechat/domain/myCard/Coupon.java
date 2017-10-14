package com.choice.wechat.domain.myCard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 优惠券
 * @author 王恒军
 */
public class Coupon implements RowMapper<Coupon>{
	/**
	 * 主键
	 */
	private String pk_id;
	
	/**
	 * 企业编码
	 */
	private String pk_group;
	
	/**
	 * 电子券编号
	 */
	private String vcode;
	
	/**
	 * 电子券名称
	 */
	private String vname;
	
	/**
	 * 价格
	 */
	private String nprice;
	
	/**
	 * 兑换所需积分
	 */
	private String nfen;
	
	/**
	 * 类型(积分购买/现金购买)
	 */
	private String vtype;
	
	/**
	 * 图片
	 */
	private String vpic;
	
	/**
	 * 备注
	 */
	private String vmemo;
	
	/**
	 * 有效期(月数)
	 */
	private int validMonth;
	
	/**
	 * 面值
	 */
	private double nmoney;
	
	/**
	 * 折扣比例
	 */
	private double discrate;
	
	/**
	 * 折扣金额
	 */
	private double discamt;
	
	/**
	 * 兑换所需积分(coupon定义)
	 */
	private int needfen;
	
	/**
	 * 活动编码
	 */
	private String vactmCode;
	
	/**
	 * 限制门店
	 */
	private String limitFirm;
	
	/**
	 * 字体颜色
	 */
	private String fontColor;
	
	/**
	 * 时间戳
	 */
	private String ts;
	
	/**
	 * 删除标志
	 */
	private int dr;

	public String getPk_id() {
		return pk_id;
	}

	public void setPk_id(String pk_id) {
		this.pk_id = pk_id;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
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

	public String getNprice() {
		return nprice;
	}

	public void setNprice(String nprice) {
		this.nprice = nprice;
	}

	public String getNfen() {
		return nfen;
	}

	public void setNfen(String nfen) {
		this.nfen = nfen;
	}

	public String getVtype() {
		return vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}

	public String getVpic() {
		return vpic;
	}

	public void setVpic(String vpic) {
		this.vpic = vpic;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}

	public int getValidMonth() {
		return validMonth;
	}

	public void setValidMonth(int validMonth) {
		this.validMonth = validMonth;
	}

	public double getNmoney() {
		return nmoney;
	}

	public void setNmoney(double nmoney) {
		this.nmoney = nmoney;
	}

	public double getDiscrate() {
		return discrate;
	}

	public void setDiscrate(double discrate) {
		this.discrate = discrate;
	}

	public double getDiscamt() {
		return discamt;
	}

	public void setDiscamt(double discamt) {
		this.discamt = discamt;
	}

	public int getNeedfen() {
		return needfen;
	}

	public void setNeedfen(int needfen) {
		this.needfen = needfen;
	}

	public String getVactmCode() {
		return vactmCode;
	}

	public void setVactmCode(String vactmCode) {
		this.vactmCode = vactmCode;
	}

	public String getLimitFirm() {
		return limitFirm;
	}

	public void setLimitFirm(String limitFirm) {
		this.limitFirm = limitFirm;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
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

	public Coupon mapRow(ResultSet rs, int i) throws SQLException {
		Coupon f = new Coupon();
		f.setPk_id(rs.getString("PK_ID"));
		f.setPk_group(rs.getString("PK_GROUP"));
		f.setVcode(rs.getString("VCODE"));
		f.setVname(rs.getString("VNAME"));
		f.setNprice(rs.getString("NPRICE"));
		f.setNfen(rs.getString("NFEN"));
		f.setVtype(rs.getString("VTYPE"));
		f.setVpic(rs.getString("VPIC"));
		f.setVmemo(rs.getString("VMEMO"));
		f.setValidMonth(rs.getInt("VALIDMONTH"));
		f.setNmoney(rs.getDouble("NMONEY"));
		f.setDiscrate(rs.getDouble("DISCRATE"));
		f.setDiscamt(rs.getDouble("DISCAMT"));
		f.setNeedfen(rs.getInt("NEEDFEN"));
		f.setVactmCode(rs.getString("VACTMCODE"));
		f.setTs(rs.getString("TS"));
		f.setDr(rs.getInt("DR"));
		return f;
	}
}
