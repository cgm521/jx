package com.choice.test.domain;


/**
 * 台位
 * @author 孙胜彬
 */
public class StoreTable {
	private String tbl;//台位主键
	private String des;//台位名称
	private String area;//区域
	private String id;//台位主键
	private String firmid;//分店ID
	private String init;//缩写
	private String pax;//人数
	private String mincost;//
	private String roomtyp;//类型
	private String num;//可预订数
	public String getTbl() {
		return tbl;
	}
	public void setTbl(String tbl) {
		this.tbl = tbl;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
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
	public String getPax() {
		return pax;
	}
	public void setPax(String pax) {
		this.pax = pax;
	}
	public String getMincost() {
		return mincost;
	}
	public void setMincost(String mincost) {
		this.mincost = mincost;
	}
	public String getRoomtyp() {
		return roomtyp;
	}
	public void setRoomtyp(String roomtyp) {
		this.roomtyp = roomtyp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInit() {
		return init;
	}
	public void setInit(String init) {
		this.init = init;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	
}
