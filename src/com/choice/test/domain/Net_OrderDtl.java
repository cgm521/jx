package com.choice.test.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;


/**
 * 我的菜单详情
 * @author 孙胜彬
 */
public class Net_OrderDtl  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6165414314272574746L;
	private String id;//订单主键
	private String foodsid;//菜品主键
	private String ordersid;//订单主键
	private String foodnum;//菜品数量
	private String totalprice;//菜品总金额
	private String foodsname;//菜品名称
	private String price;//菜品单价
	private String ispackage;//是否套餐
	private String remark;//备注
	private String reqredefine;//是否有必选附加项
	private String grptyp;//类别编码
	private List<NetDishAddItem> listDishAddItem;//菜品附加项列表
	private List<NetDishProdAdd> listDishProdAdd;//菜品附加项列表
	private String prodReqAddFlag;//是否包含必选附加产品 Y是，N：不是
	
	private String pcode;//菜品编码
	private String foodzcnt="0";//菜品赠送数量
	private List<Net_OrderPackageDetail> orderPackageDetailList;//套餐明细
	private List<Map<String,Object>> listDishTcItem;//订单下发到pos套餐明细
	private Integer tcseq;	//套餐顺序号
	private Integer seq; //菜品顺序号，用于有必选附加产品或必选附加项时每次菜品加一都弹出选择页面
	private String unit;//菜品单位
	
	private String enablestate; //是否可用
	
	public String getOrdersid() {
		return ordersid;
	}
	public void setOrdersid(String ordersid) {
		this.ordersid = ordersid;
	}
	public String getFoodnum() {
		return foodnum;
	}
	public void setFoodnum(String foodnum) {
		this.foodnum = foodnum;
	}
	public String getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}
	public String getFoodsname() {
		return foodsname;
	}
	public void setFoodsname(String foodsname) {
		this.foodsname = foodsname;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFoodsid() {
		return foodsid;
	}
	public void setFoodsid(String foodsid) {
		this.foodsid = foodsid;
	}
	public String getIspackage() {
		return ispackage;
	}
	public void setIspackage(String ispackage) {
		this.ispackage = ispackage;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<NetDishAddItem> getListDishAddItem() {
		return listDishAddItem;
	}
	public void setListDishAddItem(List<NetDishAddItem> listDishAddItem) {
		this.listDishAddItem = listDishAddItem;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public String getReqredefine() {
		return reqredefine;
	}
	public void setReqredefine(String reqredefine) {
		this.reqredefine = reqredefine;
	}
	public String getGrptyp() {
		return grptyp;
	}
	public void setGrptyp(String grptyp) {
		this.grptyp = grptyp;
	}
	public String getFoodzcnt() {
		return foodzcnt;
	}
	public void setFoodzcnt(String foodzcnt) {
		this.foodzcnt = foodzcnt;
	}
	public List<NetDishProdAdd> getListDishProdAdd() {
		return listDishProdAdd;
	}
	public void setListDishProdAdd(List<NetDishProdAdd> listDishProdAdd) {
		this.listDishProdAdd = listDishProdAdd;
	}
	public String getProdReqAddFlag() {
		return prodReqAddFlag;
	}
	public void setProdReqAddFlag(String prodReqAddFlag) {
		this.prodReqAddFlag = prodReqAddFlag;
	}
	public List<Net_OrderPackageDetail> getOrderPackageDetailList() {
		return orderPackageDetailList;
	}
	public void setOrderPackageDetailList(
			List<Net_OrderPackageDetail> orderPackageDetailList) {
		this.orderPackageDetailList = orderPackageDetailList;
	}
	public List<Map<String, Object>> getListDishTcItem() {
		return listDishTcItem;
	}
	public void setListDishTcItem(List<Map<String, Object>> listDishTcItem) {
		this.listDishTcItem = listDishTcItem;
	}
	public Integer getTcseq() {
		return tcseq;
	}
	public void setTcseq(Integer tcseq) {
		this.tcseq = tcseq;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getEnablestate() {
		return enablestate;
	}
	public void setEnablestate(String enablestate) {
		this.enablestate = enablestate;
	}
	
}
