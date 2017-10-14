package com.choice.wechat.domain.templateMsg;

import java.util.Map;

public class BaseTemplateMsg {
	private String touser;//openid
	private String template_id;//模版id ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY
	private String url;
	private String topcolor;//#FF0000
	private Map<String,MsgData> data;
	
	/** 调用绿数接口下发消息需要的字段 */
	private String key; //授权用商户号， 由“AQUA“分配
	private String time; //发送时间戳， 长度固定为时间戳的前10位（精确到秒）
	private String token; //授权唯一口令， 根据’Token’生成规则生成
	private String pub_id; //公众号base_id
	private String template_id_short; //模板库中模板的编号
	
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTopcolor() {
		return topcolor;
	}
	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}
	public Map<String, MsgData> getData() {
		return data;
	}
	public void setData(Map<String, MsgData> data) {
		this.data = data;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPub_id() {
		return pub_id;
	}
	public void setPub_id(String pub_id) {
		this.pub_id = pub_id;
	}
	public String getTemplate_id_short() {
		return template_id_short;
	}
	public void setTemplate_id_short(String template_id_short) {
		this.template_id_short = template_id_short;
	}
}
