package com.choice.wechat.domain.bookDesk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.choice.wechat.domain.game.Actm;


/**
 * 门店
 * @author 孙胜彬
 */
public class Firm implements RowMapper<Firm>{
	private String firmid;//门店主键
	private String firmdes;//门店名称
	private String init;//门店缩写
	private String pk_city;//门店隶属城市
	private String addr;//门店地址
	private String tele;//门店电话
	private String wbigpic;//门店图片
	private String lunchendtime;//午餐预订截止时间
	private String dinnerendtime;//晚餐预订截止时间
	private List<StoreTable> listStoreTable;
	private String position;//经纬度
	private Long distance;//距离 1000
	private String distanceText;//距离文本  1公里
	private int deskState;//桌位状态  0 空闲  1 中等  2 紧张
	private String waitTime;//等位时间
	private String topentim;//营业时间
	private String tclosetim;//营业时间
	private String storeupvinit;//是否收藏店铺
	/*
	private String book = "1";//门店可预订 0 可 1不
	private String tuan;//门店可团 0 可 1不
	private String fen;//门店可积分 0 可 1不
	private String quan;//门店可使用券 0 可 1不
	*/
	private List<Map<String,Object>> actmList;
	private String quanname;//优惠券名称
	private String isQueue; //是否在排队
	private String myNum; //我的排队号
	private String nowNum; //当前叫号
	private String firmCode;//门店编码
	
	private String vtpaccount; //商户号
	private String vtpkey; //商户号密钥
	
	private List<Actm> listActm; //优惠活动列表
	
	private String vareaOrder; //是否按区域点餐
	
	public String getFirmid() {
		return firmid;
	}
	public void setFirmid(String firmid) {
		this.firmid = firmid;
	}
	public String getFirmdes() {
		return firmdes;
	}
	public void setFirmdes(String firmdes) {
		this.firmdes = firmdes;
	}
	public String getInit() {
		return init;
	}
	public void setInit(String init) {
		this.init = init;
	}
	public String getPk_city() {
		return pk_city;
	}
	public void setPk_city(String pk_city) {
		this.pk_city = pk_city;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getWbigpic() {
		return wbigpic;
	}
	public void setWbigpic(String wbigpic) {
		this.wbigpic = wbigpic;
	}
	public String getLunchendtime() {
		return lunchendtime;
	}
	public void setLunchendtime(String lunchendtime) {
		this.lunchendtime = lunchendtime;
	}
	public String getDinnerendtime() {
		return dinnerendtime;
	}
	public void setDinnerendtime(String dinnerendtime) {
		this.dinnerendtime = dinnerendtime;
	}
	public List<StoreTable> getListStoreTable() {
		return listStoreTable;
	}
	public void setListStoreTable(List<StoreTable> listStoreTable) {
		this.listStoreTable = listStoreTable;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Long getDistance() {
		return distance;
	}
	public void setDistance(Long distance) {
		this.distance = distance;
	}
	public int getDeskState() {
		return deskState;
	}
	public void setDeskState(int deskState) {
		this.deskState = deskState;
	}
	public String getDistanceText() {
		return distanceText;
	}
	public void setDistanceText(String distanceText) {
		this.distanceText = distanceText;
	}
	public String getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
	public String getQuanname() {
		return quanname;
	}
	public void setQuanname(String quanname) {
		this.quanname = quanname;
	}
	public List<Map<String, Object>> getActmList() {
		return actmList;
	}
	public void setActmList(List<Map<String, Object>> actmList) {
		this.actmList = actmList;
	}
	public String getTopentim() {
		return topentim;
	}
	public void setTopentim(String topentim) {
		this.topentim = topentim;
	}
	public String getTclosetim() {
		return tclosetim;
	}
	public void setTclosetim(String tclosetim) {
		this.tclosetim = tclosetim;
	}
	public String getStoreupvinit() {
		return storeupvinit;
	}
	public void setStoreupvinit(String storeupvinit) {
		this.storeupvinit = storeupvinit;
	}
	public String getIsQueue() {
		return isQueue;
	}
	public void setIsQueue(String isQueue) {
		this.isQueue = isQueue;
	}
	public String getMyNum() {
		return myNum;
	}
	public void setMyNum(String myNum) {
		this.myNum = myNum;
	}
	public String getNowNum() {
		return nowNum;
	}
	public void setNowNum(String nowNum) {
		this.nowNum = nowNum;
	}
	public Firm mapRow(ResultSet rs, int i) throws SQLException {
		Firm f = new Firm();
		f.setAddr(rs.getString("addr"));
		f.setFirmid(rs.getString("firmid"));
		f.setFirmdes(rs.getString("firmdes"));
		f.setPk_city(rs.getString("pk_city"));
		f.setTele(rs.getString("tele"));
		f.setWbigpic(rs.getString("wbigpic"));
		f.setInit(rs.getString("init"));
		f.setDinnerendtime(rs.getString("dinnerendtime"));
		f.setLunchendtime(rs.getString("lunchendtime"));
		f.setFirmCode(rs.getString("firmcode"));
		return f;
	}
	public String getFirmCode() {
		return firmCode;
	}
	public void setFirmCode(String firmCode) {
		this.firmCode = firmCode;
	}
	public String getVtpaccount() {
		return vtpaccount;
	}
	public void setVtpaccount(String vtpaccount) {
		this.vtpaccount = vtpaccount;
	}
	public String getVtpkey() {
		return vtpkey;
	}
	public void setVtpkey(String vtpkey) {
		this.vtpkey = vtpkey;
	}
	public List<Actm> getListActm() {
		return listActm;
	}
	public void setListActm(List<Actm> listActm) {
		this.listActm = listActm;
	}
	public String getVareaOrder() {
		return vareaOrder;
	}
	public void setVareaOrder(String vareaOrder) {
		this.vareaOrder = vareaOrder;
	}
}
