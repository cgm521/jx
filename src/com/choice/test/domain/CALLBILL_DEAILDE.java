package com.choice.test.domain;


public class CALLBILL_DEAILDE {
//	订单编码  	ORDCODE
	private String ordcode="";
//	订单明细主键	BDCODE
	private String bdcode="";
//	产品编码  	PCODE
	private String Pcode="";
//	产品名称  	PNAME
	private String pname="";
//	数量    	AMOUNT
	private String amount="";
//	单位   	UNIT
	private String unit="";
//	金额  	AMONEY
	private String amoney="";
//	税金  	SCOT
	private String scot="";
//	创建时间	DTIME
	private String dtime="";
//	店铺	SCODE
	private String scode="";
//	订单金额	
	private String money="";
//	实际价格	YMONEY  
	private String ymoney="";
//	折扣价格	ZMONEY
	private String zmoney="";
//	订单数量	COUNT   
	private String count="";
	
	private String zcount="";
	private String dept="";
	private String dprice="";
	private String yhjid="";
	private String yhname="";
	private String iscoupon="";
	private String cmemo="";
	private String ycount="";
	
	public String getOrdcode() {
		return getStr(ordcode);
	}
	public void setOrdcode(String ordcode) {
		this.ordcode = ordcode;
	}
	public String getBdcode() {
		return getStr(bdcode);
	}
	public void setBdcode(String bdcode) {
		this.bdcode = bdcode;
	}
	public String getPcode() {
		return getStr(Pcode);
	}
	public void setPcode(String pcode) {
		Pcode = pcode;
	}
	public String getPname() {
		return getStr(pname);
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getAmount() {
		return getStr(amount);
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getUnit() {
		return getStr(unit);
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAmoney() {
		return getStr(amoney);
	}
	public void setAmoney(String amoney) {
		this.amoney = amoney;
	}
	public String getScot() {
		return getStr(scot);
	}
	public void setScot(String scot) {
		this.scot = scot;
	}
	public String getDtime() {
		return getStr(dtime);
	}
	public void setDtime(String dtime) {
		this.dtime = dtime;
	}
	public String getScode() {
		return getStr(scode);
	}
	public void setScode(String scode) {
		this.scode = scode;
	}
	public String getMoney() {
		return getStr(money);
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getYmoney() {
		return getStr(ymoney);
	}
	public void setYmoney(String ymoney) {
		this.ymoney = ymoney;
	}
	public String getZmoney() {
		return getStr(zmoney);
	}
	public void setZmoney(String zmoney) {
		this.zmoney = zmoney;
	}
	public String getCount() {
		return getStr(count);
	}
	public void setCount(String count) {
		this.count = count;
	}

	public String getZcount() {
		return getStr(zcount);
	}
	public void setZcount(String zcount) {
		this.zcount = zcount;
	}
	public String getDept() {
		return getStr(dept);
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDprice() {
		return getStr(dprice);
	}
	public void setDprice(String dprice) {
		this.dprice = dprice;
	}
	public String getYhjid() {
		return getStr(yhjid);
	}
	public void setYhjid(String yhjid) {
		this.yhjid = yhjid;
	}
	public String getYhname() {
		return getStr(yhname);
	}
	public void setYhname(String yhname) {
		this.yhname = yhname;
	}
	public String getIscoupon() {
		return getStr(iscoupon);
	}
	public void setIscoupon(String iscoupon) {
		this.iscoupon = iscoupon;
	}
	public String getCmemo() {
		return getStr(cmemo);
	}
	public void setCmemo(String cmemo) {
		this.cmemo = cmemo;
	}
	
	public String getYcount() {
		return getStr(ycount);
	}
	public void setYcount(String ycount) {
		this.ycount = ycount;
	}
	public String getStr(String str){
		if (str==null) {
			return "";
		}else{
			return str;
		}
	}
}
