package com.choice.test.domain;

import java.util.Date;

public class CardTyp {

	private String id;
	private String nam ;
	private double upgrade ;
	private String credit;//授信额度
	private double disc;//折扣比例
	private String locked;
	private String rank;
	private String fdiscSta;
	private String cardClass ;//积分来源0消费生成1充值生成
	private double disc1 ;
	private double disc2 ;//折扣比例0固定折扣比例1分店不同折扣
	private String modId ;//消费积分模式
	private String modDes;//消费积分模式
	private double money ;//消费积分 转换比例 元=1积分
	private String chgModId;//充值积分模式
	private String chgModDes;//充值积分模式
	private double chgMoney;//充值积分转换比例 元=1积分
	private int rountNum;//小数取舍0全部舍掉1全部进位2四舍五入3保持原数
	private String fenAmtSta ;//积分基准0按消费额1按卡金额
	private String fensta;
	private int chgComId;//充值赠送模式（赠送比例规则ID）
	private String chgComDes;//充值赠送模式（赠送比例规则）
	private double chgDisc ;//赠送比例
	private Date tim ;
	private double firstChgAmt ;//首次充值最低限额
	private double chgNumAmt ;//以后每次以此金额倍数充值
	private double extbl ;//退卡扣除余额比例
	private double gbFee ;//工本费
	private String setCnt;//限充值次数
	private double zdisc ;//扣分摊比例
	private String codId ;
	private String ygz ;
	private String ycz ;
	private double maxAmtDisc;//充值当日最高可消费充值金额的百分比
	private String yVip;//允许执行会员价
	private double vipYear ;//会员价有效期
	private String yDisc ;//Y卡内无余额是否可执行会员折扣
	private double discYear;//折扣有效期
	private String yFen;
	private double yAmt;//会员有效期内累计消费销售会员价有效期自动延期
	private double addVipYear;//销售会员价有效期自动延期年数
	private String ynBirthFen;//会员生日发送短信赠送积分0不发送不赠送1发送赠送
	private String birthFenDay ;//生日赠送积分有效期
	private double birthFen;//生日赠送积分
	private String giftVoucher ;//消费赠送餐券规则
	private String birthWakeDay;//会员生日赠送积分提前发送短信天数
	private String newVoucher;//开卡赠送餐券规则
	private String kksms ;//开卡发送短信0不发送1发送
	private String ksmsMode;//开卡发送短信模板
	private String czsms ;//充值发送短信0不发送1发送
	private String czsmsMode ;//充值发送短信模板
	private String xfsms ;//首次消费发送短信0不发送1发送
	private String xfsmsMode ;//首次消费发送短信模板
	private double maxAmt;//单次充值最高限额
	private double maxcz ;//允许剩余最大余额
	private String mxfsms;//每次消费发送短信0不发送1发送
	private String mxfMode ;//每次消费发送短信模板
	private String voucherId ;//会员生日赠送券ID
	private String vCnt;//会员生日赠送券张数
	private String vDefDays;//会员生日赠送券有效期
	private String yGifVoucher ;//会员生日发送短信赠送券0不发送不赠送1发送赠送
	private String vWakeday;//会员生日赠送券提前发送短信天数
	private String voucherDes;//会员生日赠送券
	private String yant;
	private String pos ;//收银可办理Y可以N不可
	private Integer bytee;//卡号位数
	private String cardUnit;//卡号组成0不限制1纯数字2字母数字组合
	private double backTim ;//充值返充有效期
	private String inPassword;//消费必须输入密码 0不需要1需要
	

	public String getNam() {
		return nam;
	}
	public void setNam(String nam) {
		this.nam = nam;
	}
	public double getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(double upgrade) {
		this.upgrade = upgrade;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public double getDisc() {
		return disc;
	}
	public void setDisc(double disc) {
		this.disc = disc;
	}
	public String getLocked() {
		return locked;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getFdiscSta() {
		return fdiscSta;
	}
	public void setFdiscSta(String fdiscSta) {
		this.fdiscSta = fdiscSta;
	}
	public String getCardClass() {
		return cardClass;
	}
	public void setCardClass(String cardClass) {
		this.cardClass = cardClass;
	}
	public double getDisc1() {
		return disc1;
	}
	public void setDisc1(double disc1) {
		this.disc1 = disc1;
	}
	public double getDisc2() {
		return disc2;
	}
	public void setDisc2(double disc2) {
		this.disc2 = disc2;
	}
	public String getModId() {
		return modId;
	}
	public void setModId(String modId) {
		this.modId = modId;
	}
	public String getModDes() {
		return modDes;
	}
	public void setModDes(String modDes) {
		this.modDes = modDes;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getChgModId() {
		return chgModId;
	}
	public void setChgModId(String chgModId) {
		this.chgModId = chgModId;
	}
	public String getChgModDes() {
		return chgModDes;
	}
	public void setChgModDes(String chgModDes) {
		this.chgModDes = chgModDes;
	}
	public double getChgMoney() {
		return chgMoney;
	}
	public void setChgMoney(double chgMoney) {
		this.chgMoney = chgMoney;
	}
	public int getRountNum() {
		return rountNum;
	}
	public void setRountNum(int rountNum) {
		this.rountNum = rountNum;
	}
	public String getFenAmtSta() {
		return fenAmtSta;
	}
	public void setFenAmtSta(String fenAmtSta) {
		this.fenAmtSta = fenAmtSta;
	}
	public String getFensta() {
		return fensta;
	}
	public void setFensta(String fensta) {
		this.fensta = fensta;
	}
	public int getChgComId() {
		return chgComId;
	}
	public void setChgComId(int chgComId) {
		this.chgComId = chgComId;
	}
	public String getChgComDes() {
		return chgComDes;
	}
	public void setChgComDes(String chgComDes) {
		this.chgComDes = chgComDes;
	}
	public double getChgDisc() {
		return chgDisc;
	}
	public void setChgDisc(double chgDisc) {
		this.chgDisc = chgDisc;
	}
	public Date getTim() {
		return tim;
	}
	public void setTim(Date tim) {
		this.tim = tim;
	}
	public double getFirstChgAmt() {
		return firstChgAmt;
	}
	public void setFirstChgAmt(double firstChgAmt) {
		this.firstChgAmt = firstChgAmt;
	}
	public double getChgNumAmt() {
		return chgNumAmt;
	}
	public void setChgNumAmt(double chgNumAmt) {
		this.chgNumAmt = chgNumAmt;
	}
	public double getExtbl() {
		return extbl;
	}
	public void setExtbl(double extbl) {
		this.extbl = extbl;
	}
	public double getGbFee() {
		return gbFee;
	}
	public void setGbFee(double gbFee) {
		this.gbFee = gbFee;
	}
	public String getSetCnt() {
		return setCnt;
	}
	public void setSetCnt(String setCnt) {
		this.setCnt = setCnt;
	}
	public double getZdisc() {
		return zdisc;
	}
	public void setZdisc(double zdisc) {
		this.zdisc = zdisc;
	}
	public String getCodId() {
		return codId;
	}
	public void setCodId(String codId) {
		this.codId = codId;
	}
	public String getYgz() {
		return ygz;
	}
	public void setYgz(String ygz) {
		this.ygz = ygz;
	}
	public String getYcz() {
		return ycz;
	}
	public void setYcz(String ycz) {
		this.ycz = ycz;
	}
	public double getMaxAmtDisc() {
		return maxAmtDisc;
	}
	public void setMaxAmtDisc(double maxAmtDisc) {
		this.maxAmtDisc = maxAmtDisc;
	}
	public String getyVip() {
		return yVip;
	}
	public void setyVip(String yVip) {
		this.yVip = yVip;
	}
	public double getVipYear() {
		return vipYear;
	}
	public void setVipYear(double vipYear) {
		this.vipYear = vipYear;
	}
	public String getyDisc() {
		return yDisc;
	}
	public void setyDisc(String yDisc) {
		this.yDisc = yDisc;
	}
	public double getDiscYear() {
		return discYear;
	}
	public void setDiscYear(double discYear) {
		this.discYear = discYear;
	}
	public String getyFen() {
		return yFen;
	}
	public void setyFen(String yFen) {
		this.yFen = yFen;
	}
	public double getyAmt() {
		return yAmt;
	}
	public void setyAmt(double yAmt) {
		this.yAmt = yAmt;
	}
	public double getAddVipYear() {
		return addVipYear;
	}
	public void setAddVipYear(double addVipYear) {
		this.addVipYear = addVipYear;
	}
	public String getYnBirthFen() {
		return ynBirthFen;
	}
	public void setYnBirthFen(String ynBirthFen) {
		this.ynBirthFen = ynBirthFen;
	}
	public String getBirthFenDay() {
		return birthFenDay;
	}
	public void setBirthFenDay(String birthFenDay) {
		this.birthFenDay = birthFenDay;
	}
	public double getBirthFen() {
		return birthFen;
	}
	public void setBirthFen(double birthFen) {
		this.birthFen = birthFen;
	}
	public String getGiftVoucher() {
		return giftVoucher;
	}
	public void setGiftVoucher(String giftVoucher) {
		this.giftVoucher = giftVoucher;
	}
	public String getBirthWakeDay() {
		return birthWakeDay;
	}
	public void setBirthWakeDay(String birthWakeDay) {
		this.birthWakeDay = birthWakeDay;
	}
	public String getNewVoucher() {
		return newVoucher;
	}
	public void setNewVoucher(String newVoucher) {
		this.newVoucher = newVoucher;
	}
	public String getKksms() {
		return kksms;
	}
	public void setKksms(String kksms) {
		this.kksms = kksms;
	}
	public String getKsmsMode() {
		return ksmsMode;
	}
	public void setKsmsMode(String ksmsMode) {
		this.ksmsMode = ksmsMode;
	}
	public String getCzsms() {
		return czsms;
	}
	public void setCzsms(String czsms) {
		this.czsms = czsms;
	}
	public String getCzsmsMode() {
		return czsmsMode;
	}
	public void setCzsmsMode(String czsmsMode) {
		this.czsmsMode = czsmsMode;
	}
	public String getXfsms() {
		return xfsms;
	}
	public void setXfsms(String xfsms) {
		this.xfsms = xfsms;
	}
	public String getXfsmsMode() {
		return xfsmsMode;
	}
	public void setXfsmsMode(String xfsmsMode) {
		this.xfsmsMode = xfsmsMode;
	}
	public double getMaxAmt() {
		return maxAmt;
	}
	public void setMaxAmt(double maxAmt) {
		this.maxAmt = maxAmt;
	}
	public double getMaxcz() {
		return maxcz;
	}
	public void setMaxcz(double maxcz) {
		this.maxcz = maxcz;
	}
	public String getMxfsms() {
		return mxfsms;
	}
	public void setMxfsms(String mxfsms) {
		this.mxfsms = mxfsms;
	}
	public String getMxfMode() {
		return mxfMode;
	}
	public void setMxfMode(String mxfMode) {
		this.mxfMode = mxfMode;
	}
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	public String getvCnt() {
		return vCnt;
	}
	public void setvCnt(String vCnt) {
		this.vCnt = vCnt;
	}
	public String getvDefDays() {
		return vDefDays;
	}
	public void setvDefDays(String vDefDays) {
		this.vDefDays = vDefDays;
	}
	public String getyGifVoucher() {
		return yGifVoucher;
	}
	public void setyGifVoucher(String yGifVoucher) {
		this.yGifVoucher = yGifVoucher;
	}
	public String getvWakeday() {
		return vWakeday;
	}
	public void setvWakeday(String vWakeday) {
		this.vWakeday = vWakeday;
	}
	public String getVoucherDes() {
		return voucherDes;
	}
	public void setVoucherDes(String voucherDes) {
		this.voucherDes = voucherDes;
	}
	public String getYant() {
		return yant;
	}
	public void setYant(String yant) {
		this.yant = yant;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public Integer getBytee() {
		return bytee;
	}
	public void setBytee(Integer bytee) {
		this.bytee = bytee;
	}
	public String getCardUnit() {
		return cardUnit;
	}
	public void setCardUnit(String cardUnit) {
		this.cardUnit = cardUnit;
	}
	public double getBackTim() {
		return backTim;
	}
	public void setBackTim(double backTim) {
		this.backTim = backTim;
	}
	public String getInPassword() {
		return inPassword;
	}
	public void setInPassword(String inPassword) {
		this.inPassword = inPassword;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
