package com.choice.test.domain;

import java.util.List;


/**
 * 门店
 * @author 孙胜彬
 */
public class Firm {
	private String firmid;//门店主键
	private String vcode;//门店编码
	private String firmdes;//门店名称
	private String init;//门店缩写
	private String area;//门店隶属城市
	private String addr;//门店地址
	private String tele;//门店电话
	private String wbigpic;//门店图片
	private List<StoreTable> listStoreTable;
	public String getFirmid() {
		return firmid;
	}
	public void setFirmid(String firmid) {
		this.firmid = firmid;
	}
	public String getFirmdes() {
		return firmdes;
	}
	public void setFirmdes(String firmdes) {
		this.firmdes = firmdes;
	}
	public String getInit() {
		return init;
	}
	public void setInit(String init) {
		this.init = init;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public List<StoreTable> getListStoreTable() {
		return listStoreTable;
	}
	public void setListStoreTable(List<StoreTable> listStoreTable) {
		this.listStoreTable = listStoreTable;
	}
	public String getWbigpic() {
		return wbigpic;
	}
	public void setWbigpic(String wbigpic) {
		this.wbigpic = wbigpic;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	
}
