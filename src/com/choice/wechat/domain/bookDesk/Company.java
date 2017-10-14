package com.choice.wechat.domain.bookDesk;

import java.io.Serializable;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Company implements RowMapper<Object>, Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2967433378291704238L;
	
	private String pk_group;
	private String vcontent;
	private String vtitle;
	private String vcomUrl;
	private String vcomPic;
	private String vtakeOrdr;
	private String vregestPic;
	private String appId;//应用id
	private String secret;//应用密钥
	private String app_key;//paysignkey 128位字符串(非appkey)
	private String partner;
	private String partner_key;
	private String wx_title;
	private String softwareSerialNo;
	private String softwareKey;
	private String isGift;
	private double inAmt;
	private int minBookHours;
	private String sessionType;//市别。保存格式：1;2;3
	private String vafterpay;//是否支付后下单 Y支付后 N支付前
	private String vaeskey;//加密key（新增企业时自动生成）
	
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getVcontent() {
		return vcontent;
	}
	public void setVcontent(String vcontent) {
		this.vcontent = vcontent;
	}
	public String getVtitle() {
		return vtitle;
	}
	public void setVtitle(String vtitle) {
		this.vtitle = vtitle;
	}
	public String getVcomUrl() {
		return vcomUrl;
	}
	public void setVcomUrl(String vcomUrl) {
		this.vcomUrl = vcomUrl;
	}
	public String getVcomPic() {
		return vcomPic;
	}
	public void setVcomPic(String vcomPic) {
		this.vcomPic = vcomPic;
	}
	public String getVtakeOrdr() {
		return vtakeOrdr;
	}
	public void setVtakeOrdr(String vtakeOrdr) {
		this.vtakeOrdr = vtakeOrdr;
	}
	public String getVregestPic() {
		return vregestPic;
	}
	public void setVregestPic(String vregestPic) {
		this.vregestPic = vregestPic;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getApp_key() {
		return app_key;
	}
	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getPartner_key() {
		return partner_key;
	}
	public void setPartner_key(String partner_key) {
		this.partner_key = partner_key;
	}
	public String getWx_title() {
		return wx_title;
	}
	public void setWx_title(String wx_title) {
		this.wx_title = wx_title;
	}
	public String getSoftwareSerialNo() {
		return softwareSerialNo;
	}
	public void setSoftwareSerialNo(String softwareSerialNo) {
		this.softwareSerialNo = softwareSerialNo;
	}
	public String getSoftwareKey() {
		return softwareKey;
	}
	public void setSoftwareKey(String softwareKey) {
		this.softwareKey = softwareKey;
	}
	public String getIsGift() {
		return isGift;
	}
	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}
	public double getInAmt() {
		return inAmt;
	}
	public void setInAmt(double inAmt) {
		this.inAmt = inAmt;
	}
	public int getMinBookHours() {
		return minBookHours;
	}
	public void setMinBookHours(int minBookHours) {
		this.minBookHours = minBookHours;
	}
	public String getSessionType() {
		return sessionType;
	}
	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}
	
	public Object mapRow(ResultSet rs, int i) throws SQLException {
		Company company = new Company();
		company.setSecret(rs.getString("secret"));
		company.setAppId(rs.getString("appid"));
		company.setPk_group(rs.getString("pk_group"));
		return company;
	}
	public String getVafterpay() {
		return vafterpay;
	}
	public void setVafterpay(String vafterpay) {
		this.vafterpay = vafterpay;
	}
	public String getVaeskey() {
		return vaeskey;
	}
	public void setVaeskey(String vaeskey) {
		this.vaeskey = vaeskey;
	}
}
