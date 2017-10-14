package com.choice.wechat.domain.takeout;

import java.util.List;

public class StoreRange {
	/**
	 * 主键
	 */
	private String pkStorerange;

	/**
	 * 企业编号
	 */
	private String pkGroup;

	/**
	 * 门店主键
	 */
	private String pkStore;

	/**
	 * 配送区域序号
	 */
	private int iareaseq;

	/**
	 * 配送区域名称
	 */
	private String vareaname;

	/**
	 * 起送价
	 */
	private double nstartprice;

	/**
	 * 配送费
	 */
	private double ndistributfee;

	/**
	 * 颜色
	 */
	private String vcolor;

	/**
	 * 保存时间
	 */
	private String ts;

	/**
	 * 扩展字段1
	 */
	private String vdef1;

	/**
	 * 扩展字段2
	 */
	private String vdef2;

	/**
	 * 扩展字段3
	 */
	private String vdef3;

	/**
	 * 扩展字段4
	 */
	private String vdef4;

	/**
	 * 扩展字段5
	 */
	private String vdef5;
	
	/**
	 * 对应坐标点列表
	 */
	private List<RangeCoordi> listRangeCoordi;

	public String getPkStorerange() {
		return pkStorerange;
	}

	public void setPkStorerange(String pkStorerange) {
		this.pkStorerange = pkStorerange;
	}

	public String getPkGroup() {
		return pkGroup;
	}

	public void setPkGroup(String pkGroup) {
		this.pkGroup = pkGroup;
	}

	public String getPkStore() {
		return pkStore;
	}

	public void setPkStore(String pkStore) {
		this.pkStore = pkStore;
	}

	public int getIareaseq() {
		return iareaseq;
	}

	public void setIareaseq(int iareaseq) {
		this.iareaseq = iareaseq;
	}

	public String getVareaname() {
		return vareaname;
	}

	public void setVareaname(String vareaname) {
		this.vareaname = vareaname;
	}

	public double getNstartprice() {
		return nstartprice;
	}

	public void setNstartprice(double nstartprice) {
		this.nstartprice = nstartprice;
	}

	public double getNdistributfee() {
		return ndistributfee;
	}

	public void setNdistributfee(double ndistributfee) {
		this.ndistributfee = ndistributfee;
	}

	public String getVcolor() {
		return vcolor;
	}

	public void setVcolor(String vcolor) {
		this.vcolor = vcolor;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	public List<RangeCoordi> getListRangeCoordi() {
		return listRangeCoordi;
	}

	public void setListRangeCoordi(List<RangeCoordi> listRangeCoordi) {
		this.listRangeCoordi = listRangeCoordi;
	}
}
