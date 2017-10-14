package com.choice.wechat.domain.takeout;

public class RangeCoordi {
	/**
	 * 主键
	 */
	private String pkRangecoordi;

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
	 * 经度
	 */
	private String vlng;

	/**
	 * 维度
	 */
	private String vlat;

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

	public String getPkRangecoordi() {
		return pkRangecoordi;
	}

	public void setPkRangecoordi(String pkRangecoordi) {
		this.pkRangecoordi = pkRangecoordi;
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

	public String getVlng() {
		return vlng;
	}

	public void setVlng(String vlng) {
		this.vlng = vlng;
	}

	public String getVlat() {
		return vlat;
	}

	public void setVlat(String vlat) {
		this.vlat = vlat;
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
}
