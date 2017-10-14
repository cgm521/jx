package com.choice.wechat.domain.bookDesk;


/**
 * 餐次
 * @author 孙胜彬
 */
public class Sft {
	private String id;		//主键
	private String code;	//编码
	private String name;	//名称
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
