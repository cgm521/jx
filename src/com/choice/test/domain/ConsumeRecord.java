package com.choice.test.domain;


public class ConsumeRecord {
	
	private String cardno;//卡号
	private String tim;//消费时间
	private String amt;//消费金额
	private String balaamt;//卡结金额
	private String firmdes;//消费门店
	private String qczamt;//期初余额
	private String qmzamt;//期末余额
	private String openid;//openid
	
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getTim() {
		return tim;
	}
	public void setTim(String tim) {
		this.tim = tim;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getBalaamt() {
		return balaamt;
	}
	public void setBalaamt(String balaamt) {
		this.balaamt = balaamt;
	}
	public String getFirmdes() {
		return firmdes;
	}
	public void setFirmdes(String firmdes) {
		this.firmdes = firmdes;
	}
	public String getQczamt() {
		return qczamt;
	}
	public void setQczamt(String qczamt) {
		this.qczamt = qczamt;
	}
	public String getQmzamt() {
		return qmzamt;
	}
	public void setQmzamt(String qmzamt) {
		this.qmzamt = qmzamt;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}

}
