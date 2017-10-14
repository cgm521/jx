package com.choice.test.domain;


public class ChargeRecord {
	
	private String cardno;//卡号
	private String tim;//充值时间
	private String rmbamt;//充值金额
	private String giftamt;//赠送金额
	private String operater;//充值总额
	private String payment;//支付方式
	private String firmdes;//充值站点
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
	public String getRmbamt() {
		return rmbamt;
	}
	public void setRmbamt(String rmbamt) {
		this.rmbamt = rmbamt;
	}
	public String getGiftamt() {
		return giftamt;
	}
	public void setGiftamt(String giftamt) {
		this.giftamt = giftamt;
	}
	public String getOperater() {
		return operater;
	}
	public void setOperater(String operater) {
		this.operater = operater;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getFirmdes() {
		return firmdes;
	}
	public void setFirmdes(String firmdes) {
		this.firmdes = firmdes;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
