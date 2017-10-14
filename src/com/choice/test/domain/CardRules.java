package com.choice.test.domain;


/**
 * 会员卡规则表
 * @author 孙胜彬
 */
public class CardRules {
	private String ID;//主键
	private String chgrules;//充值规则
	private String jifenrules;//积分规则
	private String exclusprivle;//会员专享特权
	private String cardexplan;//会员卡说明
	private String cardtele;//会员卡中心电话
	private String STORE;//使用门店

	public String getChgrules() {
		return chgrules;
	}
	public void setChgrules(String chgrules) {
		this.chgrules = chgrules;
	}
	public String getJifenrules() {
		return jifenrules;
	}
	public void setJifenrules(String jifenrules) {
		this.jifenrules = jifenrules;
	}
	public String getExclusprivle() {
		return exclusprivle;
	}
	public void setExclusprivle(String exclusprivle) {
		this.exclusprivle = exclusprivle;
	}
	public String getCardexplan() {
		return cardexplan;
	}
	public void setCardexplan(String cardexplan) {
		this.cardexplan = cardexplan;
	}
	public String getCardtele() {
		return cardtele;
	}
	public void setCardtele(String cardtele) {
		this.cardtele = cardtele;
	}
	public String getSTORE() {
		return STORE;
	}
	public void setSTORE(String sTORE) {
		STORE = sTORE;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
}
