package com.choice.wechat.domain.myCard;

import java.io.Serializable;
import java.util.List;

import com.choice.wechat.domain.bookMeal.NetDishAddItem;


/**
 * 我的菜单详情
 * @author 孙胜彬
 */
public class NetGoodsOrderDtl  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4495918212300228206L;
	
	private String id;//订单明细主键
	
	private String ordersid;//订单主键
	
	private String goodsid;//商品主键
	
	private String goodsnum;//商品数量
	
	private String price;//商品单价
	
	private String totalprice;//商品总金额
	
	private String goodsname;//商品名称
	
	private String payType; //支付方式 1:现金 2:积分s
	
	private String remark;//备注
	
	private String couponCode; //电子券编码

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrdersid() {
		return ordersid;
	}

	public void setOrdersid(String ordersid) {
		this.ordersid = ordersid;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoodsnum() {
		return goodsnum;
	}

	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
}
