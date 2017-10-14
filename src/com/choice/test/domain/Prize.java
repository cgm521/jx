package com.choice.test.domain;
/**
 * 奖项
 * @author lwj
 * 2014-3-19
 */
public class Prize {
	private String id;//主键
	private String gamesId;//抽奖游戏关联id
	private String level;//奖品等级，例一等奖、二等奖
	private String name;//奖品名称
	private Integer count;//奖品数量
	private Double probability;//中奖概率
	private String type;//奖品类型，1-积分 2-优惠券 3-实物
	private Integer integral;//积分数量
	private String vouchers;//优惠券号
	private String physical;//实物名称
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGamesId() {
		return gamesId;
	}
	public void setGamesId(String gamesId) {
		this.gamesId = gamesId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getProbability() {
		return probability;
	}
	public void setProbability(Double probability) {
		this.probability = probability;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	public String getVouchers() {
		return vouchers;
	}
	public void setVouchers(String vouchers) {
		this.vouchers = vouchers;
	}
	public String getPhysical() {
		return physical;
	}
	public void setPhysical(String physical) {
		this.physical = physical;
	}
	
	
}
