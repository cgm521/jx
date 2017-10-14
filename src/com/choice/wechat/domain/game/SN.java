package com.choice.wechat.domain.game;

public class SN {
	private String sn;
	private String openid;
	private String lucky;
	private String gameType;
	private String updateTime;//DateFormat.getStringByDate(rs.getTimestamp("UPDATETIME"), "yyyy-MM-dd HH:mm:ss")
	private Integer Draw;
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getLucky() {
		return lucky;
	}
	public void setLucky(String lucky) {
		this.lucky = lucky;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getDraw() {
		return Draw;
	}
	public void setDraw(Integer draw) {
		Draw = draw;
	}
}
