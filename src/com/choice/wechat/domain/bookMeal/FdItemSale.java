package com.choice.wechat.domain.bookMeal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 菜品类别
 * @author 王恒军
 */
public class FdItemSale implements RowMapper<FdItemSale>{
	/**
	 * 企业编码
	 */
	private String pk_group;

	/**
	 * 菜品主键
	 */
	private String id;

	/**
	 * 门店主键
	 */
	private String firmid;

	/**
	 * 
	 */
	private String item;

	/**
	 * 菜品编码
	 */
	private String itcode;

	/**
	 * 
	 */
	private String rest;

	/**
	 * 菜品名称
	 */
	private String des;

	/**
	 * 
	 */
	private String dese;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 
	 */
	private String dept;

	/**
	 * 小类编码
	 */
	private String grp;

	/**
	 * 网站价格
	 */
	private String price;

	/**
	 * 名称缩写
	 */
	private String init;

	/**
	 * 
	 */
	private String pkgtag;

	/**
	 * 
	 */
	private String pubitcode;

	/**
	 * 中类编码
	 */
	private String grptyp;

	/**
	 * 
	 */
	private String statis;

	/**
	 * 
	 */
	private String nodisc;

	/**
	 * 
	 */
	private String deptprn1;

	/**
	 * 
	 */
	private String deptprn2;

	/**
	 * 
	 */
	private String deptprn3;

	/**
	 * 
	 */
	private String bakprn1;

	/**
	 * 
	 */
	private String bakprn2;

	/**
	 * 
	 */
	private String totalprn;

	/**
	 * 描述
	 */
	private String discription;

	/**
	 * 销量
	 */
	private String salenum;

	/**
	 * 人气  点击喜欢
	 */
	private String likenum;

	/**
	 * 
	 */
	private String prepay;

	/**
	 * 大图存放地址
	 */
	private String url;

	/**
	 * 是否美味菜品，默认N
	 */
	private String isdel;

	/**
	 * 菜品类别汉意
	 */
	private String grpstr;

	/**
	 * 是否删除，默认N
	 */
	private String ifuse;

	/**
	 * 展示类别id
	 */
	private String showtype;

	/**
	 * 排序
	 */
	private String sortnum;

	/**
	 * 是否外卖菜品，默认N
	 */
	private String takedis;

	/**
	 * 小图存放路径
	 */
	private String smallpic;

	/**
	 * 是否可用，默认Y
	 */
	private String available;

	/**
	 * 微信大图路径
	 */
	private String wxbigpic;

	/**
	 * 微信小图路径
	 */
	private String wxsmallpic;

	/**
	 * APP价格
	 */
	private String price2;

	/**
	 * 门店价格
	 */
	private String price3;

	/**
	 * 备用价格
	 */
	private String price4;

	/**
	 * 小类主键
	 */
	private String pk_grp;

	/**
	 * 中类主键
	 */
	private String pk_grptyp;

	/**
	 * 小类名称
	 */
	private String grpname;

	/**
	 * 中类名称
	 */
	private String grptypname;
	
	/**
	 * 是否有必选附加项
	 */
	private String reqredefine;
	
	/**
	 * 是否已沽清 Y：已沽清； N：未沽清
	 */
	private String hasSellOff;
	
	/**
	 * 是否新菜 Y是，N：不是
	 */
	private String visnew;
	
	/**
	 * 是否明星菜 Y是，N：不是
	 */
	private String visrec;
	
	/**
	 * 是否包含必选附加产品 Y是，N：不是
	 */
	private String prodReqAddFlag;
	
	/**
	 * 辣度标识0-5
	 */
	private String vspicy;
	
	/**
	 * 菜谱方案优先级
	 */
	private String vlev;
	
	/**
	 * 单位编码
	 */
	private String unitcode;
	
	/**
	 * 状态 2：可用
	 */
	private String enablestate;

	/**
	 * vpricetyp
	 * 套餐价格类型
	 * */
	private String vpricetyp;
	
	public String getVpricetyp() {
		return vpricetyp;
	}

	public void setVpricetyp(String vpricetyp) {
		this.vpricetyp = vpricetyp;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirmid() {
		return firmid;
	}

	public void setFirmid(String firmid) {
		this.firmid = firmid;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItcode() {
		return itcode;
	}

	public void setItcode(String itcode) {
		this.itcode = itcode;
	}

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getDese() {
		return dese;
	}

	public void setDese(String dese) {
		this.dese = dese;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getGrp() {
		return grp;
	}

	public void setGrp(String grp) {
		this.grp = grp;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getPkgtag() {
		return pkgtag;
	}

	public void setPkgtag(String pkgtag) {
		this.pkgtag = pkgtag;
	}

	public String getPubitcode() {
		return pubitcode;
	}

	public void setPubitcode(String pubitcode) {
		this.pubitcode = pubitcode;
	}

	public String getGrptyp() {
		return grptyp;
	}

	public void setGrptyp(String grptyp) {
		this.grptyp = grptyp;
	}

	public String getStatis() {
		return statis;
	}

	public void setStatis(String statis) {
		this.statis = statis;
	}

	public String getNodisc() {
		return nodisc;
	}

	public void setNodisc(String nodisc) {
		this.nodisc = nodisc;
	}

	public String getDeptprn1() {
		return deptprn1;
	}

	public void setDeptprn1(String deptprn1) {
		this.deptprn1 = deptprn1;
	}

	public String getDeptprn2() {
		return deptprn2;
	}

	public void setDeptprn2(String deptprn2) {
		this.deptprn2 = deptprn2;
	}

	public String getDeptprn3() {
		return deptprn3;
	}

	public void setDeptprn3(String deptprn3) {
		this.deptprn3 = deptprn3;
	}

	public String getBakprn1() {
		return bakprn1;
	}

	public void setBakprn1(String bakprn1) {
		this.bakprn1 = bakprn1;
	}

	public String getBakprn2() {
		return bakprn2;
	}

	public void setBakprn2(String bakprn2) {
		this.bakprn2 = bakprn2;
	}

	public String getTotalprn() {
		return totalprn;
	}

	public void setTotalprn(String totalprn) {
		this.totalprn = totalprn;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getSalenum() {
		return salenum;
	}

	public void setSalenum(String salenum) {
		this.salenum = salenum;
	}

	public String getLikenum() {
		return likenum;
	}

	public void setLikenum(String likenum) {
		this.likenum = likenum;
	}

	public String getPrepay() {
		return prepay;
	}

	public void setPrepay(String prepay) {
		this.prepay = prepay;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public String getGrpstr() {
		return grpstr;
	}

	public void setGrpstr(String grpstr) {
		this.grpstr = grpstr;
	}

	public String getIfuse() {
		return ifuse;
	}

	public void setIfuse(String ifuse) {
		this.ifuse = ifuse;
	}

	public String getShowtype() {
		return showtype;
	}

	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}

	public String getSortnum() {
		return sortnum;
	}

	public void setSortnum(String sortnum) {
		this.sortnum = sortnum;
	}

	public String getTakedis() {
		return takedis;
	}

	public void setTakedis(String takedis) {
		this.takedis = takedis;
	}

	public String getSmallpic() {
		return smallpic;
	}

	public void setSmallpic(String smallpic) {
		this.smallpic = smallpic;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getWxbigpic() {
		return wxbigpic;
	}

	public void setWxbigpic(String wxbigpic) {
		this.wxbigpic = wxbigpic;
	}

	public String getWxsmallpic() {
		return wxsmallpic;
	}

	public void setWxsmallpic(String wxsmallpic) {
		this.wxsmallpic = wxsmallpic;
	}

	public String getPrice2() {
		return price2;
	}

	public void setPrice2(String price2) {
		this.price2 = price2;
	}

	public String getPrice3() {
		return price3;
	}

	public void setPrice3(String price3) {
		this.price3 = price3;
	}

	public String getPrice4() {
		return price4;
	}

	public void setPrice4(String price4) {
		this.price4 = price4;
	}

	public String getPk_grp() {
		return pk_grp;
	}

	public void setPk_grp(String pk_grp) {
		this.pk_grp = pk_grp;
	}

	public String getPk_grptyp() {
		return pk_grptyp;
	}

	public void setPk_grptyp(String pk_grptyp) {
		this.pk_grptyp = pk_grptyp;
	}

	public String getGrpname() {
		return grpname;
	}

	public void setGrpname(String grpname) {
		this.grpname = grpname;
	}

	public String getGrptypname() {
		return grptypname;
	}

	public void setGrptypname(String grptypname) {
		this.grptypname = grptypname;
	}

	public String getReqredefine() {
		return reqredefine;
	}

	public void setReqredefine(String reqredefine) {
		this.reqredefine = reqredefine;
	}

	public String getHasSellOff() {
		return hasSellOff;
	}

	public void setHasSellOff(String hasSellOff) {
		this.hasSellOff = hasSellOff;
	}

	public String getVisnew() {
		return visnew;
	}

	public void setVisnew(String visnew) {
		this.visnew = visnew;
	}

	public String getVisrec() {
		return visrec;
	}

	public void setVisrec(String visrec) {
		this.visrec = visrec;
	}

	public String getProdReqAddFlag() {
		return prodReqAddFlag;
	}

	public void setProdReqAddFlag(String prodReqAddFlag) {
		this.prodReqAddFlag = prodReqAddFlag;
	}

	public String getVspicy() {
		return vspicy;
	}

	public void setVspicy(String vspicy) {
		this.vspicy = vspicy;
	}

	public String getVlev() {
		return vlev;
	}

	public void setVlev(String vlev) {
		this.vlev = vlev;
	}

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getEnablestate() {
		return enablestate;
	}

	public void setEnablestate(String enablestate) {
		this.enablestate = enablestate;
	}

	public FdItemSale mapRow(ResultSet rs, int i) throws SQLException {
		FdItemSale f = new FdItemSale();
		//f.setPk_group(rs.getString("pk_group"));
		f.setId(rs.getString("id"));
		f.setItcode(rs.getString("itcode"));
		f.setDes(rs.getString("des"));
		f.setInit(rs.getString("init"));
		f.setPrice(rs.getString("price"));
		f.setUnit(rs.getString("unit"));
		f.setWxbigpic(rs.getString("wxbigpic"));
		f.setWxsmallpic(rs.getString("wxsmallpic"));
		f.setDiscription(rs.getString("discription") == null ? "" : rs.getString("discription"));
		f.setGrpstr(rs.getString("grpstr") == null ? "" : rs.getString("grpstr"));
		return f;
	}
}
