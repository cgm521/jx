package com.choice.test.domain;


/**
 * 电子卷
 * @author 孙胜彬
 */
public class Voucher {
	private String id;//主键
	private String typdes;
	private String rest;//适用门店
	private String firmname;//门店名称
	private String bdate;//获取时间
	private String edate;//失效时间
	private String sta;//状态
	private String code;//电子券编码
	private String actmCode; // 活动编码
	private String amt; //金额
	private String pic; // 背景图片
	private String fontColor; // 字体颜色
	private String typId; // 券类型编码
	private String cardId; //会员编号
	private String memo; //使用说明
	private Integer istyle; //电子券类型
	private String discrate; //折扣比例
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypdes() {
		return typdes;
	}
	public void setTypdes(String typdes) {
		this.typdes = typdes;
	}
	public String getRest() {
		return rest;
	}
	public void setRest(String rest) {
		this.rest = rest;
	}
	public String getFirmname() {
		return firmname;
	}
	public void setFirmname(String firmname) {
		this.firmname = firmname;
	}
	public String getBdate() {
		return bdate;
	}
	public void setBdate(String bdate) {
		String date=bdate.substring(0,10);
		this.bdate = date;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		String date=edate.substring(0,10);
		this.edate = date;
	}
	public String getSta() {
		return sta;
	}
	public void setSta(String sta) {
		this.sta = sta;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getActmCode() {
		return actmCode;
	}
	public void setActmCode(String actmCode) {
		this.actmCode = actmCode;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public String getTypId() {
		return typId;
	}
	public void setTypId(String typId) {
		this.typId = typId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Integer getIstyle() {
		return istyle;
	}
	public void setIstyle(Integer istyle) {
		this.istyle = istyle;
	}
	public String getDiscrate() {
		return discrate;
	}
	public void setDiscrate(String discrate) {
		this.discrate = discrate;
	}
	
}
