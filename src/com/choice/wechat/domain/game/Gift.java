package com.choice.wechat.domain.game;

public class Gift {
	private String gameType;
	private int lucky;
	private String luckyName;
	private int quantity;
	private int usable;
	private int priority;
	private String pk_actm;
	private int ipercnt;
	
	public int getIpercnt() {
		return ipercnt;
	}
	public void setIpercnt(int ipercnt) {
		this.ipercnt = ipercnt;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public int getLucky() {
		return lucky;
	}
	public void setLucky(int lucky) {
		this.lucky = lucky;
	}
	public String getLuckyName() {
		return luckyName;
	}
	public void setLuckyName(String luckyName) {
		this.luckyName = luckyName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getUsable() {
		return usable;
	}
	public void setUsable(int usable) {
		this.usable = usable;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getPk_actm() {
		return pk_actm;
	}
	public void setPk_actm(String pk_actm) {
		this.pk_actm = pk_actm;
	}
}
