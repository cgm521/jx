package com.choice.test.domain;

import java.io.Serializable;
import java.util.List;

import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;


/**
 * 我的菜单
 * @author 孙胜彬
 */
public class Net_Orders  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7576476667066631198L;
	private String id;//主键
	private String resv;//订单号
	private String firmid;//门店id
	private String firmdes;//门店名称
	private String dat;//就餐日期
	private String pax;//就餐人数
	private String sft;//类型
	private String tables;//台位
	private String contact;//电话
	private String addr;//门店地址
	private String openid;//微信号
	private String rannum;//随机码
	private String pubitem;//菜品
	private String datmins;//时间
	private String remark;//备注
	private String tele;//门店电话
	private String isfeast;//订单订桌
	private String state;//状态  1:生效未支付,2：订单已生效、订单已下发（）,3：订单已取消、取消下发中，4：申请退款 5：已退款 6：订单结账 7：已下单
	private String orderTimes;//时间
	private String printstate;//打印状态
	private String cardzamt;//小计
	private String cashier;//收银员
	private String receivable;//应收金额
	private String cardamt;//卡余额
	private String payment;//支付方式
	private String stws = "2";//是否外送  1外送   2 不外送
	private String money;//账单金额
	private String name;//联系姓名
	private String roomtype;//桌位类型
	private String roompax;//桌位人数
	private String pk_group;
	private List<Net_OrderDtl> listNetOrderDtl;//菜品详细
	private String sumprice;//账单金额
	private List<NetDishAddItem> listDishAddItem;//菜品附加项列表
	private String realmoney;//实际消费额
	private String paymoney;//支付金额
	private String vtransactionid;//微信支付时财付通返回的订单号
	private String serialid;//mq流水号
	private String type;//类型 1 -菜品 2-支付
	private String bookDeskOrderID; //订位单单号
	private List<NetDishProdAdd> listDishProdAdd;//菜品附加产品列表
	private String vinvoicetitle;//发票抬头
	
	private String vcode;//门店编码
	private String tablesname;//门店编码
	private String outTradeNo; //微信支付商户订单号
	
	private String food;//称谓 先生 女士
	private String arrtime;//预计到达时间，在预订中表示世纪的日期，以解决跨天的情况
	private String deliverytime;//外卖送达时间统一字段
	private String ordfrom;//订单来源
	
	private String evaluationId;
	
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
	public String getFirmid() {
		return firmid;
	}
	public void setFirmid(String firmid) {
		this.firmid = firmid;
	}
	public String getDat() {
		return dat;
	}
	public void setDat(String dat) {
		this.dat = dat;
	}
	public String getPax() {
		return pax;
	}
	public void setPax(String pax) {
		this.pax = pax;
	}
	public String getSft() {
		return sft;
	}
	public void setSft(String sft) {
		this.sft = sft;
	}
	public String getTables() {
		return tables;
	}
	public void setTables(String tables) {
		this.tables = tables;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
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
	public String getFirmdes() {
		return firmdes;
	}
	public void setFirmdes(String firmdes) {
		this.firmdes = firmdes;
	}
	public List<Net_OrderDtl> getListNetOrderDtl() {
		return listNetOrderDtl;
	}
	public void setListNetOrderDtl(List<Net_OrderDtl> listNetOrderDtl) {
		this.listNetOrderDtl = listNetOrderDtl;
	}
	public String getPubitem() {
		return pubitem;
	}
	public void setPubitem(String pubitem) {
		this.pubitem = pubitem;
	}
	public String getDatmins() {
		return datmins;
	}
	public void setDatmins(String datmins) {
		this.datmins = datmins;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getIsfeast() {
		return isfeast;
	}
	public void setIsfeast(String isfeast) {
		this.isfeast = isfeast;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOrderTimes() {
		return orderTimes;
	}
	public void setOrderTimes(String orderTimes) {
		this.orderTimes = orderTimes;
	}
	public String getPrintstate() {
		return printstate;
	}
	public void setPrintstate(String printstate) {
		this.printstate = printstate;
	}
	public String getCardzamt() {
		return cardzamt;
	}
	public void setCardzamt(String cardzamt) {
		this.cardzamt = cardzamt;
	}
	public String getCashier() {
		return cashier;
	}
	public void setCashier(String cashier) {
		this.cashier = cashier;
	}
	public String getReceivable() {
		return receivable;
	}
	public void setReceivable(String receivable) {
		this.receivable = receivable;
	}
	public String getCardamt() {
		return cardamt;
	}
	public void setCardamt(String cardamt) {
		this.cardamt = cardamt;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getStws() {
		return stws;
	}
	public void setStws(String stws) {
		this.stws = stws;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoomtype() {
		return roomtype;
	}
	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}
	public String getRoompax() {
		return roompax;
	}
	public void setRoompax(String roompax) {
		this.roompax = roompax;
	}
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getSumprice() {
		return sumprice;
	}
	public void setSumprice(String sumprice) {
		this.sumprice = sumprice;
	}
	public List<NetDishAddItem> getListDishAddItem() {
		return listDishAddItem;
	}
	public void setListDishAddItem(List<NetDishAddItem> listDishAddItem) {
		this.listDishAddItem = listDishAddItem;
	}
	public String getRealmoney() {
		return realmoney;
	}
	public void setRealmoney(String realmoney) {
		this.realmoney = realmoney;
	}
	public String getPaymoney() {
		return paymoney;
	}
	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}
	public String getVtransactionid() {
		return vtransactionid;
	}
	public void setVtransactionid(String vtransactionid) {
		this.vtransactionid = vtransactionid;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public String getSerialid() {
		return serialid;
	}
	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBookDeskOrderID() {
		return bookDeskOrderID;
	}
	public void setBookDeskOrderID(String bookDeskOrderID) {
		this.bookDeskOrderID = bookDeskOrderID;
	}
	public List<NetDishProdAdd> getListDishProdAdd() {
		return listDishProdAdd;
	}
	public void setListDishProdAdd(List<NetDishProdAdd> listDishProdAdd) {
		this.listDishProdAdd = listDishProdAdd;
	}
	public String getVinvoicetitle() {
		return vinvoicetitle;
	}
	public void setVinvoicetitle(String vinvoicetitle) {
		this.vinvoicetitle = vinvoicetitle;
	}
	public String getTablesname() {
		return tablesname;
	}
	public void setTablesname(String tablesname) {
		this.tablesname = tablesname;
	}
	public String getFood() {
		return food;
	}
	public void setFood(String food) {
		this.food = food;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getArrtime() {
		return arrtime;
	}
	public void setArrtime(String arrtime) {
		this.arrtime = arrtime;
	}
	public String getDeliverytime() {
		return deliverytime;
	}
	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}
	public String getOrdfrom() {
		return ordfrom;
	}
	public void setOrdfrom(String ordfrom) {
		this.ordfrom = ordfrom;
	}
	public String getEvaluationId() {
		return evaluationId;
	}
	public void setEvaluationId(String evaluationId) {
		this.evaluationId = evaluationId;
	}
}
