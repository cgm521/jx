package com.choice.wechat.domain.bookDesk;

/**
 * 台位
 * @author 孙胜彬
 */
public class StoreTable{
	private String tbl;//台位主键
	private String des;//台位名称
	private String area;//区域
	private String id;//台位主键  //预订台位时，用此字段表示台位类型主键
	private String firmid;//分店ID
	private String init;//缩写
	private String pax;//人数
	private String mincost;//
	private String roomtyp;//类型
	private String num;//可预订数
	private String maxpax;//最大认输
	private String minpax;//最小人数
	private String pk_group;
	private String isortno;//排序规则
	private String takedNumber; //已取号
	private String calldeNumber; //已叫号
	private String myNumber; //我的取号
	private String waitTblNum; //等待桌数
	private String waitTime; //等待时长
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
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getMaxpax() {
		return maxpax;
	}
	public void setMaxpax(String maxpax) {
		this.maxpax = maxpax;
	}
	public String getMinpax() {
		return minpax;
	}
	public void setMinpax(String minpax) {
		this.minpax = minpax;
	}
	public String getIsortno() {
		return isortno;
	}
	public void setIsortno(String isortno) {
		this.isortno = isortno;
	}
	public String getTakedNumber() {
		return takedNumber;
	}
	public void setTakedNumber(String takedNumber) {
		this.takedNumber = takedNumber;
	}
	public String getCalldeNumber() {
		return calldeNumber;
	}
	public void setCalldeNumber(String calldeNumber) {
		this.calldeNumber = calldeNumber;
	}
	public String getMyNumber() {
		return myNumber;
	}
	public void setMyNumber(String myNumber) {
		this.myNumber = myNumber;
	}
	public String getWaitTblNum() {
		return waitTblNum;
	}
	public void setWaitTblNum(String waitTblNum) {
		this.waitTblNum = waitTblNum;
	}
	public String getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
}
