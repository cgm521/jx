package com.choice.test.domain;


/**
 * 菜品类别
 * @author 孙胜彬
 */
public class ProjectType {
	private String id;//大类主键
	private String des;//大类名称
	private String code;//类别编码
	private String firmid;//门店ID
	private String typind;//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFirmid() {
		return firmid;
	}
	public void setFirmid(String firmid) {
		this.firmid = firmid;
	}
	public String getTypind() {
		return typind;
	}
	public void setTypind(String typind) {
		this.typind = typind;
	}
	
}
