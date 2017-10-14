package com.choice.test.domain;


public class Card {
	private String cardId;//会员卡ID
	private String cardNo;//会员卡号
	private String name;//会员姓名
	private String zAmt;//卡余额
	private String ttlFen;//卡积分
	private String credit;//信用额度
	private String tele;//手机号
	private String typDes;//卡类型
	private String openid;//微信ID
	private String ranNum;//手机验证码
	private String runNum;//随机码
	private String qrordr;//二维码
	private String outAmt;//扣除金额
	private String firmId;//门店ID
	private String date;//扣款日期
	private String invoiceAmt;//发票额
	private String empName;//业务员
	private String bindate;//会员有效开始日期
	private String enddate;//会员有效截止日期
	private String chgAmt;//累计充值总额
	
	
	private String sex; //性别
	private String bridate;//生日
	private String addr;//地址
	private String mail;//邮箱
	
	private String pk_group; //企业编码
	private String wechatId;//微信id
	private String chlb;//客户类别 会员/--/--
	private String typ;//卡类别id
	private String cond;//入会时间
	private String passwd;//密码
	
	private String rechargeOnline; //是否可在线充值； Y:是   N：否
	
	private String rest;//办理地点

	private String city;//所在城市

	private String firmName;//门店名称
	
	private String myselfShareCode;//本人的推荐码
	private String othersShareCode;//他人的推荐码
	private String label;//会员标签
	private String favorite;//会员的兴趣爱好
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getFavorite() {
		return favorite;
	}
	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}
	public String getMyselfShareCode() {
		return myselfShareCode;
	}
	public void setMyselfShareCode(String myselfShareCode) {
		this.myselfShareCode = myselfShareCode;
	}
	public String getOthersShareCode() {
		return othersShareCode;
	}
	public void setOthersShareCode(String othersShareCode) {
		this.othersShareCode = othersShareCode;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getzAmt() {
		return zAmt;
	}
	public void setzAmt(String zAmt) {
		this.zAmt = zAmt;
	}
	public String getTtlFen() {
		return ttlFen;
	}
	public void setTtlFen(String ttlFen) {
		this.ttlFen = ttlFen;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getTypDes() {
		return typDes;
	}
	public void setTypDes(String typDes) {
		this.typDes = typDes;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getRanNum() {
		return ranNum;
	}
	public void setRanNum(String ranNum) {
		this.ranNum = ranNum;
	}
	public String getRunNum() {
		return runNum;
	}
	public void setRunNum(String runNum) {
		this.runNum = runNum;
	}
	public String getQrordr() {
		return qrordr;
	}
	public void setQrordr(String qrordr) {
		this.qrordr = qrordr;
	}
	public String getOutAmt() {
		return outAmt;
	}
	public void setOutAmt(String outAmt) {
		this.outAmt = outAmt;
	}
	public String getFirmId() {
		return firmId;
	}
	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getInvoiceAmt() {
		return invoiceAmt;
	}
	public void setInvoiceAmt(String invoiceAmt) {
		this.invoiceAmt = invoiceAmt;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getChgAmt() {
		return chgAmt;
	}
	public void setChgAmt(String chgAmt) {
		this.chgAmt = chgAmt;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBridate() {
		return bridate;
	}
	public void setBridate(String bridate) {
		this.bridate = bridate;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getWechatId() {
		return wechatId;
	}
	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}
	public String getChlb() {
		return chlb;
	}
	public void setChlb(String chlb) {
		this.chlb = chlb;
	}
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	public String getBindate() {
		return bindate;
	}
	public void setBindate(String bindate) {
		this.bindate = bindate;
	}
	public String getCond() {
		return cond;
	}
	public void setCond(String cond) {
		this.cond = cond;
	}
	public String getRechargeOnline() {
		return rechargeOnline;
	}
	public void setRechargeOnline(String rechargeOnline) {
		this.rechargeOnline = rechargeOnline;
	}
	public String getRest() {
		return rest;
	}
	public void setRest(String rest) {
		this.rest = rest;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
}
