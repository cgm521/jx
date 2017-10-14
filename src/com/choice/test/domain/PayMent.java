package com.choice.test.domain;
/**
 * 结算成功后向POS返回数据的实体类
 * @author 孙胜彬
 */
public class PayMent {
	private String resv;//账单号
	private String operate;//支付编码
	private String operatename;//支付名称
	private double money;//支付金额
	private int isshow;//1 储值虚增和团购 0正常
	private double padmoney;//团购实时金额
	private int cashcount;//券数量
	private double overcash;//超收
	private double cashone;//券面值
	private String ctime;//时间
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getOperatename() {
		return operatename;
	}
	public void setOperatename(String operatename) {
		this.operatename = operatename;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getIsshow() {
		return isshow;
	}
	public void setIsshow(int isshow) {
		this.isshow = isshow;
	}
	public double getPadmoney() {
		return padmoney;
	}
	public void setPadmoney(double padmoney) {
		this.padmoney = padmoney;
	}
	public int getCashcount() {
		return cashcount;
	}
	public void setCashcount(int cashcount) {
		this.cashcount = cashcount;
	}
	public double getOvercash() {
		return overcash;
	}
	public void setOvercash(double overcash) {
		this.overcash = overcash;
	}
	public double getCashone() {
		return cashone;
	}
	public void setCashone(double cashone) {
		this.cashone = cashone;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getResv() {
		return resv;
	}
	public void setResv(String resv) {
		this.resv = resv;
	}
	
}
