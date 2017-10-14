package com.choice.wechat.domain.myCard;
/**
 * 标签和兴趣爱好（对应boh里面的参数字典)
 * @author xqd
 *
 */
public class LabelFavorite {
	private String sno;
	private String code;
	private String des;//描述
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	
}
