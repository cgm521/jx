package com.choice.test.domain;
/**
 * 抽奖
 * @author lwj
 * 2014-3-19
 */
public class Games {
	private String id;//主键
	private String type;//游戏标志 
	private String name;//抽奖名称
	private Integer count;//每人抽奖次数  不限制设为空
	private String remark;//抽奖活动说明
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
