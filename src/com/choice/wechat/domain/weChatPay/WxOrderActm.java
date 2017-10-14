package com.choice.wechat.domain.weChatPay;

public class WxOrderActm {
	private String pk_orderactm; //主键
	private String vordersid; //账单号
	private String vactmcode; //活动编码
	private String vvouchercode; //优惠券编码
	private Double ndiscamt; //折扣金额
	private Integer ncnt; //数量
	private String vtype; //支付类型
	
	public String getPk_orderactm() {
		return pk_orderactm;
	}
	public void setPk_orderactm(String pk_orderactm) {
		this.pk_orderactm = pk_orderactm;
	}
	public String getVordersid() {
		return vordersid;
	}
	public void setVordersid(String vordersid) {
		this.vordersid = vordersid;
	}
	public String getVactmcode() {
		return vactmcode;
	}
	public void setVactmcode(String vactmcode) {
		this.vactmcode = vactmcode;
	}
	public String getVvouchercode() {
		return vvouchercode;
	}
	public void setVvouchercode(String vvouchercode) {
		this.vvouchercode = vvouchercode;
	}
	public Double getNdiscamt() {
		return ndiscamt;
	}
	public void setNdiscamt(Double ndiscamt) {
		this.ndiscamt = ndiscamt;
	}
	public Integer getNcnt() {
		return ncnt;
	}
	public void setNcnt(Integer ncnt) {
		this.ncnt = ncnt;
	}
	public String getVtype() {
		return vtype;
	}
	public void setVtype(String vtype) {
		this.vtype = vtype;
	}
}
