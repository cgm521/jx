package com.choice.test.domain;

import java.util.List;

public class FavorArea {
	private String id;//主键
	private String area;//区域
	private String firmid;//使用门店
	private List<Firm> listFirm;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getFirmid() {
		return firmid;
	}
	public void setFirmid(String firmid) {
		this.firmid = firmid;
	}
	public List<Firm> getListFirm() {
		return listFirm;
	}
	public void setListFirm(List<Firm> listFirm) {
		this.listFirm = listFirm;
	}
	
}
