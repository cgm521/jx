package com.choice.wechat.domain.campaign;

public class ActmItem {
	private String pk_item; //主键
	private String pk_actm; //活动主键
	private String pk_pubitem; //菜品主键/套餐主键/活动类别主键
	private int inum; //数量
	private String vgivetype; //优惠方式
	private double nvalue; //优惠值
	private String unitindex; //单位序号
	private String unitcode; //单位编码
	private double itypenum; //类别数量
	private String vitemtype; //项目标识，1为菜品，2为套餐，3为菜品活动类别，4为菜品类别
	
	public String getPk_item() {
		return pk_item;
	}
	public void setPk_item(String pk_item) {
		this.pk_item = pk_item;
	}
	public String getPk_actm() {
		return pk_actm;
	}
	public void setPk_actm(String pk_actm) {
		this.pk_actm = pk_actm;
	}
	public String getPk_pubitem() {
		return pk_pubitem;
	}
	public void setPk_pubitem(String pk_pubitem) {
		this.pk_pubitem = pk_pubitem;
	}
	public int getInum() {
		return inum;
	}
	public void setInum(int inum) {
		this.inum = inum;
	}
	public String getVgivetype() {
		return vgivetype;
	}
	public void setVgivetype(String vgivetype) {
		this.vgivetype = vgivetype;
	}
	public double getNvalue() {
		return nvalue;
	}
	public void setNvalue(double nvalue) {
		this.nvalue = nvalue;
	}
	public String getUnitindex() {
		return unitindex;
	}
	public void setUnitindex(String unitindex) {
		this.unitindex = unitindex;
	}
	public String getUnitcode() {
		return unitcode;
	}
	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}
	public double getItypenum() {
		return itypenum;
	}
	public void setItypenum(double itypenum) {
		this.itypenum = itypenum;
	}
	public String getVitemtype() {
		return vitemtype;
	}
	public void setVitemtype(String vitemtype) {
		this.vitemtype = vitemtype;
	}
}
