package com.choice.wxc.domain.diningEvaluate;

import java.io.Serializable;

/**
 * 菜品
 * @author 王恒军
 */
public class NetOrderDtl  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6165414314272574746L;
	private String ordersid; //订单主键
	private String pkPubitem; //菜品主键
	private String vname; //菜品名称
	
	private String point; //评价分数
	
	public String getOrdersid() {
		return ordersid;
	}
	
	public void setOrdersid(String ordersid) {
		this.ordersid = ordersid;
	}
	
	public String getPkPubitem() {
		return pkPubitem;
	}
	
	public void setPkPubitem(String pkPubitem) {
		this.pkPubitem = pkPubitem;
	}
	
	public String getVname() {
		return vname;
	}
	
	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}
}
