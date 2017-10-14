package com.choice.test.domain;


/**
 * 
 * @author changlibing 2010/12/23
 *
 */
public class CALLBILL_Order {
	/**订单编码  	ORDCODE	VARCHART(100)*/	
	private String ordcode="";
	
	/**顾客编码    	CUSTOMCODE	VARCHART(100)	外键*/
	private String customcode="";
	
	/**顾客名称  	CNAME		*/
	private String cname="";
	
	/**性别    	SEX	INT (0||1)	0 女 1 男 */
	private String sex ="";
	
	/**外送商圈编码 	DCODE 		外键*/
	private String dcode="";
	
	/**外送地址编码  	WADDRESSID		外键*/
	private String waddressid="";
	
	private String waddress="";
	
	/**客户详细地址  	ADDRESS	VARCHART(100)	*/
	private String address="";
	
	/**客户电话 	TEL	VARCHART(100)	*/
	private String tel="";
	
	/**客户来电号码	LTEL	VARCHART(100)*/	
	private String ltel="";
	
	/**接线员	JCODE		*/
	private String jcode="";
	
	/**接线员名称	JNAME	*/	
	private String jname="";
	
	/**外送员工	ECODE	*/	
	private String ecode="";
	
	/**外送员工名称	ENAME	*/	
	private String ename="";
	
	/**状态	STATE	INT	见状态主目录*/
	private String state="";
	
	/**合计金额    	TOTALMONEY	MONEY	*/
	private String totalmoney="";
	
	/**合计税金 	TOTALSCOT		*/
	private String totalscot="";
	
	/**失败原因	FEILREASON		*/
	private String feilreason="";
	
	/**开单时间  	FTIME		第一个点菜时间（秒）*/
	private String ftime="";
	
	/**T1时间       	ETIME		订单保存时间（秒）*/
	private String etime="";
	
	/**审核时间	CHKTIME		状态为2时候更新*/
	private String chktime="";
	
	/**取单时间	GETTIME		状态为10时候时间*/
	private String gettime="";
	
	/**完成时间	FINSHTIME		状态为3,4时候时间*/
	private String finshtime="";
	
	/**店铺	SCODE		*/
	private String scode="";
	
	/**POSID	POSID        */		
	private String posid="";
	
	/**收银员	ECCODE       	*/	
	private String eccode="";
	
	/**订单类型  	ORCLASS      */		
	private String orclass="";
	
	/**餐次的类型	CCLASS       		1整单OK  2整单撤销 3 单品撤销 0 为24小时  1为早餐 2 为午餐 3 为晚餐 4 夜宵*/
	private String cclass="";
	
	/**餐别类型	LCLASS       		1食堂 2外送 3其它*/
	private String lclass ="";
	
	/**订单总额   	MONEY   		实际金额 =   使用金额 +  折扣金额*/
	private String money="";
	
	/**营收金额	YMONEY  		*/
	private String ymoney ="";
	
	/**折扣金额	ZMONEY		*/
	private String zmoney ="";
	
	/**打印次数    	PRINTCOUNT   */		
	private String printcount 
	="";
	/**是否给顾客	SHOUCLIEN		*/
	private String showclien ="";
	
	/**是否外送	STWS         		0 为外送  1 为POS  ，2  POS结账的外卖(区分POS和外买)*/
	private String stws ="";
	
	/**原分店编码	YSCODE       		用在推单临时表中推单原店铺编码*/
	private String yscode="";
	
	/**外送编码  	WRCODE	Varchart(100)	*/
	private String wrcode="";
	
	/**LSTAT        			*/
	private String lstat="";
	
	/**targettime        			*/
	private String targettime="";
	
	/**cmemo描述        			*/
	private String cmemo="";
	
	private String e_num="";
	
	private String messges="";

	private String payState="";
	private String assigntime = "";
	private String districtname = "";
	private String checkcode = "";
	private String checkname = "";
	private String addrstate = "";
	private String ctitime = "";
	private String paytime = "";
	private String needinvoice = "";
	private String invoicetitle = "";
	private String paymethod = "";
	private String smstime = "";
	private String smsstate = "";
	private String lat_lngvalue = "";
	private String modifystate = "";
	private String pmtime = "";
	private String ordersource = "";
	private String isscheduled = "";
	
	
	private String storename = "";
	
	
	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public String getOrdcode() {
		return getStr(ordcode);
	}

	public void setOrdcode(String ordcode) {
		this.ordcode = ordcode;
	}

	public String getCustomcode() {
		return getStr(customcode);
	}

	public void setCustomcode(String customcode) {
		this.customcode = customcode;
	}

	public String getCname() {
		return getStr(cname);
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getSex() {
		return getStr(sex);
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDcode() {
		return getStr(dcode);
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	public String getWaddressid() {
		return getStr(waddressid);
	}

	public void setWaddressid(String waddressid) {
		this.waddressid = waddressid;
	}

	public String getWaddress() {
		return getStr(waddress);
	}

	public void setWaddress(String waddress) {
		this.waddress = waddress;
	}

	public String getAddress() {
		return getStr(address);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return getStr(tel);
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getLtel() {
		return getStr(ltel);
	}

	public void setLtel(String ltel) {
		this.ltel = ltel;
	}

	public String getJcode() {
		return getStr(jcode);
	}

	public void setJcode(String jcode) {
		this.jcode = jcode;
	}

	public String getJname() {
		return getStr(jname);
	}

	public void setJname(String jname) {
		this.jname = jname;
	}

	public String getEcode() {
		return getStr(ecode);
	}

	public void setEcode(String ecode) {
		this.ecode = ecode;
	}

	public String getEname() {
		return getStr(ename);
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getState() {
		return getStr(state);
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTotalmoney() {
		return getStr(totalmoney);
	}

	public void setTotalmoney(String totalmoney) {
		this.totalmoney = totalmoney;
	}

	public String getTotalscot() {
		return getStr(totalscot);
	}

	public void setTotalscot(String totalscot) {
		this.totalscot = totalscot;
	}

	public String getFeilreason() {
		return getStr(feilreason);
	}

	public void setFeilreason(String feilreason) {
		this.feilreason = feilreason;
	}

	public String getFtime() {
		return getStr(ftime);
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public String getEtime() {
		return getStr(etime);
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getChktime() {
		return getStr(chktime);
	}

	public void setChktime(String chktime) {
		this.chktime = chktime;
	}

	public String getGettime() {
		return getStr(gettime);
	}

	public void setGettime(String gettime) {
		this.gettime = gettime;
	}

	public String getFinshtime() {
		return getStr(finshtime);
	}

	public void setFinshtime(String finshtime) {
		this.finshtime = finshtime;
	}

	public String getScode() {
		return getStr(scode);
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getPosid() {
		return getStr(posid);
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public String getEccode() {
		return getStr(eccode);
	}

	public void setEccode(String eccode) {
		this.eccode = eccode;
	}

	public String getOrclass() {
		return getStr(orclass);
	}

	public void setOrclass(String orclass) {
		this.orclass = orclass;
	}

	public String getCclass() {
		return getStr(cclass);
	}

	public void setCclass(String cclass) {
		this.cclass = cclass;
	}

	public String getLclass() {
		return getStr(lclass);
	}

	public void setLclass(String lclass) {
		this.lclass = lclass;
	}

	public String getMoney() {
		return getStr(money);
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getYmoney() {
		return getStr(ymoney);
	}

	public void setYmoney(String ymoney) {
		this.ymoney = ymoney;
	}

	public String getZmoney() {
		return getStr(zmoney);
	}

	public void setZmoney(String zmoney) {
		this.zmoney = zmoney;
	}

	public String getPrintcount() {
		return getStr(printcount);
	}

	public void setPrintcount(String printcount) {
		this.printcount = printcount;
	}

	public String getShowclien() {
		return getStr(showclien);
	}

	public void setShowclien(String showclien) {
		this.showclien = showclien;
	}

	public String getStws() {
		return getStr(stws);
	}

	public void setStws(String stws) {
		this.stws = stws;
	}

	public String getYscode() {
		return getStr(yscode);
	}

	public void setYscode(String yscode) {
		this.yscode = yscode;
	}

	public String getWrcode() {
		return getStr(wrcode);
	}

	public void setWrcode(String wrcode) {
		this.wrcode = wrcode;
	}

	public String getLstat() {
		return getStr(lstat);
	}

	public void setLstat(String lstat) {
		this.lstat = lstat;
	}

	public String getTargettime() {
		return getStr(targettime);
	}

	public void setTargettime(String targettime) {
		this.targettime = targettime;
	}

	public String getCmemo() {
		return getStr(cmemo);
	}

	public void setCmemo(String cmemo) {
		this.cmemo = cmemo;
	}

	public String getE_num() {
		return getStr(e_num);
	}

	public void setE_num(String e_num) {
		this.e_num = e_num;
	}

	public String getMessges() {
		return getStr(messges);
	}

	public void setMessges(String messges) {
		this.messges = messges;
	}

	public String getPayState() {
		return getStr(payState);
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	
	public String getAssigntime() {
		return getStr(assigntime);
	}

	public void setAssigntime(String assigntime) {
		this.assigntime = assigntime;
	}

	public String getDistrictname() {
		return getStr(districtname);
	}

	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}

	public String getCheckcode() {
		return getStr(checkcode);
	}

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	public String getCheckname() {
		return getStr(checkname);
	}

	public void setCheckname(String checkname) {
		this.checkname = checkname;
	}

	public String getAddrstate() {
		return getStr(addrstate);
	}

	public void setAddrstate(String addrstate) {
		this.addrstate = addrstate;
	}

	public String getCtitime() {
		return getStr(ctitime);
	}

	public void setCtitime(String ctitime) {
		this.ctitime = ctitime;
	}

	public String getPaytime() {
		return getStr(paytime);
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public String getNeedinvoice() {
		return getStr(needinvoice);
	}

	public void setNeedinvoice(String needinvoice) {
		this.needinvoice = needinvoice;
	}

	public String getInvoicetitle() {
		return getStr(invoicetitle);
	}

	public void setInvoicetitle(String invoicetitle) {
		this.invoicetitle = invoicetitle;
	}

	public String getPaymethod() {
		return getStr(paymethod);
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getSmstime() {
		return getStr(smstime);
	}

	public void setSmstime(String smstime) {
		this.smstime = smstime;
	}

	public String getSmsstate() {
		return getStr(smsstate);
	}

	public void setSmsstate(String smsstate) {
		this.smsstate = smsstate;
	}

	public String getLat_lngvalue() {
		return getStr(lat_lngvalue);
	}

	public void setLat_lngvalue(String lat_lngvalue) {
		this.lat_lngvalue = lat_lngvalue;
	}

	public String getModifystate() {
		return getStr(modifystate);
	}

	public void setModifystate(String modifystate) {
		this.modifystate = modifystate;
	}

	public String getPmtime() {
		return getStr(pmtime);
	}

	public void setPmtime(String pmtime) {
		this.pmtime = pmtime;
	}

	public String getOrdersource() {
		return getStr(ordersource);
	}

	public void setOrdersource(String ordersource) {
		this.ordersource = ordersource;
	}

	public String getIsscheduled() {
		return getStr(isscheduled);
	}

	public void setIsscheduled(String isscheduled) {
		this.isscheduled = isscheduled;
	}

	public String getStr(String str){
		if (str==null) {
			return "";
		}else{
			return str;
		}
	}
	
}
