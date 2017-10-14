package com.choice.test.domain;



/**
 * 菜品
 * @author 孙胜彬
 */
public class Project {
	private String pubitem;//菜品主键
	private String pitcode;//菜品编码
	private String pdes;//菜品名称
	private String pinit;//缩写
	private String price;//价格
	private String url;//图片地址
	private String smallUrl;//图片地址
	public String getPubitem() {
		return pubitem;
	}
	public void setPubitem(String pubitem) {
		this.pubitem = pubitem;
	}
	public String getPitcode() {
		return pitcode;
	}
	public void setPitcode(String pitcode) {
		this.pitcode = pitcode;
	}
	public String getPdes() {
		return pdes;
	}
	public void setPdes(String pdes) {
		this.pdes = pdes;
	}
	public String getPinit() {
		return pinit;
	}
	public void setPinit(String pinit) {
		this.pinit = pinit;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSmallUrl() {
		return smallUrl;
	}
	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}
	
}
