package com.choice.wechat.domain.myCard;

import java.io.Serializable;
import java.util.List;


/**
 * 商品订单
 * @author 王恒军
 */
public class NetGoodsOrders  implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3152350944499453213L;
	private String id;//主键
	private String resv;//订单号
	private double sumPrice;//账单金额
	private int sumPoint;//账单积分
	private double payMoney; //支付金额
	private int payPoint; //支付积分
	private String orderTimes;//时间
	private String endTime;//最晚支付时间
	private String state;//状态  0:未支付 1:已支付 2:已取消
	private String contact;//电话
	private String name;//联系姓名
	private String sex;//性别
	private String remark;//备注
	private String paymentType;//支付方式 0:微信支付 1:银联支付
	private String sendMsg; //是否下发短信
	private String custId; //客户编号
	private String ydown;
	private String downTime;
	private String ycelDown;
	private String celDownTime;
	private String isSued;
	private String ynget;
	private String openid;//微信号
	private String rannum;//随机码
	private String ordFrom;//订单来源，NET网路，APP手机，WECHAT微信
	private String pk_group;
	private String vtransactionid;//微信支付时财付通返回的订单号
	private List<NetGoodsOrderDtl> listNetGoodsOrderDtl;//商品详细

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResv() {
		return resv;
	}

	public void setResv(String resv) {
		this.resv = resv;
	}

	public double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(double sumPrice) {
		this.sumPrice = sumPrice;
	}

	public int getSumPoint() {
		return sumPoint;
	}

	public void setSumPoint(int sumPoint) {
		this.sumPoint = sumPoint;
	}

	public double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}

	public int getPayPoint() {
		return payPoint;
	}

	public void setPayPoint(int payPoint) {
		this.payPoint = payPoint;
	}

	public String getOrderTimes() {
		return orderTimes;
	}

	public void setOrderTimes(String orderTimes) {
		this.orderTimes = orderTimes;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getSendMsg() {
		return sendMsg;
	}

	public void setSendMsg(String sendMsg) {
		this.sendMsg = sendMsg;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getYdown() {
		return ydown;
	}

	public void setYdown(String ydown) {
		this.ydown = ydown;
	}

	public String getDownTime() {
		return downTime;
	}

	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}

	public String getYcelDown() {
		return ycelDown;
	}

	public void setYcelDown(String ycelDown) {
		this.ycelDown = ycelDown;
	}

	public String getCelDownTime() {
		return celDownTime;
	}

	public void setCelDownTime(String celDownTime) {
		this.celDownTime = celDownTime;
	}

	public String getIsSued() {
		return isSued;
	}

	public void setIsSued(String isSued) {
		this.isSued = isSued;
	}

	public String getYnget() {
		return ynget;
	}

	public void setYnget(String ynget) {
		this.ynget = ynget;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getRannum() {
		return rannum;
	}

	public void setRannum(String rannum) {
		this.rannum = rannum;
	}

	public String getOrdFrom() {
		return ordFrom;
	}

	public void setOrdFrom(String ordFrom) {
		this.ordFrom = ordFrom;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getVtransactionid() {
		return vtransactionid;
	}

	public void setVtransactionid(String vtransactionid) {
		this.vtransactionid = vtransactionid;
	}

	public List<NetGoodsOrderDtl> getListNetGoodsOrderDtl() {
		return listNetGoodsOrderDtl;
	}

	public void setListNetGoodsOrderDtl(List<NetGoodsOrderDtl> listNetGoodsOrderDtl) {
		this.listNetGoodsOrderDtl = listNetGoodsOrderDtl;
	}
}
