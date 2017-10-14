package com.choice.wechat.domain.game;

public class DrawCount {
	private String openid;
	private Integer nowCount;
	private String gameType;
	private String updateTime;//DateFormat.getStringByDate(rs.getTimestamp("UPDATETIME"), "yyyy-MM-dd HH:mm:ss")
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Integer getNowCount() {
		return nowCount;
	}
	public void setNowCount(Integer nowCount) {
		this.nowCount = nowCount;
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
}
