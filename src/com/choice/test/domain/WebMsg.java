package com.choice.test.domain;

/**
 * 优惠信息
 * @author 孙胜彬
 */
public class WebMsg {
	private String id;//主键,
	private String dat;//日期
	private String title;//标题
	private String wurl;//图片地址
	private String wcontent;//内容
	private String keyword;
	private String firmid;//门店主键
	private String firmnm;//门店名称
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDat() {
		return dat;
	}
	public void setDat(String dat) {
		this.dat = dat;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWurl() {
		return wurl;
	}
	public void setWurl(String wurl) {
		this.wurl = wurl;
	}
	public String getWcontent() {
		return wcontent;
	}
	public void setWcontent(String wcontent) {
		this.wcontent = wcontent;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getFirmid() {
		return firmid;
	}
	public void setFirmid(String firmid) {
		this.firmid = firmid;
	}
	public String getFirmnm() {
		return firmnm;
	}
	public void setFirmnm(String firmnm) {
		this.firmnm = firmnm;
	}
	
}
