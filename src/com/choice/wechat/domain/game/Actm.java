package com.choice.wechat.domain.game;

public class Actm {
	private String pk_actm;//活动主键
	private String vcode;//编码
	private String vouchercode;//电子券编码
	private int vouchernum;//活动赠送数量
	private int ifennum;//积分数
	private int iticketnum;//活动限制张数数量
	
	private String vname; //活动名称
	private String startdate; //开始日期
	private String enddate; //结束日期
	private double iamount; //金额上限
	private double idownamount; //金额下限
	private String bisamount; //是否启用金额限制 Y/N
	private String bremit; //账单减免 Y/N
	private String bdiscount; //菜品折扣 Y/N
	private double nderatenum; //减免金额
	private double ndiscountrate; //折扣比率
	private String bismdxz; //适用门店
	private String badjust; //是否启用调价 Y/N
	private double nadjustmy; //调价金额
	private String jmrdo; //减免金额类型 0 金额,1 比例 ,2 自由输入 
	
	public String getPk_actm() {
		return pk_actm;
	}
	public void setPk_actm(String pk_actm) {
		this.pk_actm = pk_actm;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public String getVouchercode() {
		return vouchercode;
	}
	public void setVouchercode(String vouchercode) {
		this.vouchercode = vouchercode;
	}
	public int getVouchernum() {
		return vouchernum;
	}
	public void setVouchernum(int vouchernum) {
		this.vouchernum = vouchernum;
	}
	public int getIfennum() {
		return ifennum;
	}
	public void setIfennum(int ifennum) {
		this.ifennum = ifennum;
	}
	public int getIticketnum() {
		return iticketnum;
	}
	public void setIticketnum(int iticketnum) {
		this.iticketnum = iticketnum;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public double getIamount() {
		return iamount;
	}
	public void setIamount(double iamount) {
		this.iamount = iamount;
	}
	public double getIdownamount() {
		return idownamount;
	}
	public void setIdownamount(double idownamount) {
		this.idownamount = idownamount;
	}
	public String getBisamount() {
		return bisamount;
	}
	public void setBisamount(String bisamount) {
		this.bisamount = bisamount;
	}
	public String getBremit() {
		return bremit;
	}
	public void setBremit(String bremit) {
		this.bremit = bremit;
	}
	public String getBdiscount() {
		return bdiscount;
	}
	public void setBdiscount(String bdiscount) {
		this.bdiscount = bdiscount;
	}
	public double getNderatenum() {
		return nderatenum;
	}
	public void setNderatenum(double nderatenum) {
		this.nderatenum = nderatenum;
	}
	public double getNdiscountrate() {
		return ndiscountrate;
	}
	public void setNdiscountrate(double ndiscountrate) {
		this.ndiscountrate = ndiscountrate;
	}
	public String getBismdxz() {
		return bismdxz;
	}
	public void setBismdxz(String bismdxz) {
		this.bismdxz = bismdxz;
	}
	public String getBadjust() {
		return badjust;
	}
	public void setBadjust(String badjust) {
		this.badjust = badjust;
	}
	public double getNadjustmy() {
		return nadjustmy;
	}
	public void setNadjustmy(double nadjustmy) {
		this.nadjustmy = nadjustmy;
	}
	public String getJmrdo() {
		return jmrdo;
	}
	public void setJmrdo(String jmrdo) {
		this.jmrdo = jmrdo;
	}
}
