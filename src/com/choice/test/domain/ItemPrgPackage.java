package com.choice.test.domain;

/**
 * 套餐
 * @author 孙胜彬
 */
public class ItemPrgPackage {
	private String id;//主键
	private String prgid;//
	private String packages;//
	private String des;//名称
	private String price;//价格
	private String sno;//
	private String snodes;//
	private String firmid;//门店ID
	private String picsrc;//套餐地址
	private String wxbigpic;//微信大图地址
	private String wxsmallpic;//微信小图地址
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrgid() {
		return prgid;
	}
	public void setPrgid(String prgid) {
		this.prgid = prgid;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getSnodes() {
		return snodes;
	}
	public void setSnodes(String snodes) {
		this.snodes = snodes;
	}
	public String getFirmid() {
		return firmid;
	}
	public void setFirmid(String firmid) {
		this.firmid = firmid;
	}
	public String getPicsrc() {
		return picsrc;
	}
	public void setPicsrc(String picsrc) {
		this.picsrc = picsrc;
	}
	public String getWxbigpic() {
		return wxbigpic;
	}
	public void setWxbigpic(String wxbigpic) {
		this.wxbigpic = wxbigpic;
	}
	public String getWxsmallpic() {
		return wxsmallpic;
	}
	public void setWxsmallpic(String wxsmallpic) {
		this.wxsmallpic = wxsmallpic;
	}
	
}
