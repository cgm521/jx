package com.choice.test.domain;


/**
 * 时间桌台
 * @author 孙胜彬
 */
public class DeskTimes {
	private String id;			//主键
	private String resvtblid;	//台位主键
	private String dat;			//日期
	private String sft;			//餐次
	private String remark;		//
	private String state;		//台位状态
	private String ordersid;	//订单主键
	private String vcode;//台位编码
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResvtblid() {
		return resvtblid;
	}
	public void setResvtblid(String resvtblid) {
		this.resvtblid = resvtblid;
	}
	public String getDat() {
		return dat;
	}
	public void setDat(String dat) {
		this.dat = dat;
	}
	public String getSft() {
		return sft;
	}
	public void setSft(String sft) {
		this.sft = sft;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOrdersid() {
		return ordersid;
	}
	public void setOrdersid(String ordersid) {
		this.ordersid = ordersid;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	
}
